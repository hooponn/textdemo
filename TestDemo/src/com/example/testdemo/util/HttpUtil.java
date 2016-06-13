package com.example.testdemo.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.example.testdemo.bean.OneList;
import com.example.testdemo.bean.Place;
import com.example.testdemo.listener.OnDetailContentsFinishListener;
import com.example.testdemo.listener.OnOneListFinishListener;
import com.example.testdemo.listener.OnPlaceFinishListener;

import android.os.AsyncTask;
import android.util.Log;
import net.sourceforge.pinyin4j.PinyinHelper;

public class HttpUtil {

	public static void getOneListRequest(final String city,final int page,
			final OnOneListFinishListener listener) {
		new AsyncTask<Void, Void,List<OneList>>() {
			@Override
			protected List<OneList> doInBackground(Void... params) {
				List<OneList> list=new ArrayList<OneList>();
				BufferedReader reader = null;
				String result = null;
				StringBuffer sbf = null;
				try {
					String c=URLEncoder.encode(city, "utf-8");
					String httpUrl = "http://apis.baidu.com/qunartravel/travellist/travellist"
							+ "?query=%22%22"+c+"&page="+String.valueOf(page);
					URL url = new URL(httpUrl);
					HttpURLConnection connection = (HttpURLConnection) url
							.openConnection();
					connection.setRequestMethod("GET");
					// 填入apikey到HTTP header
					connection.setRequestProperty("apikey","53ccdfc46e871781f303524465102cee");
					connection.connect();
					InputStream is = connection.getInputStream();
					reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
					String strRead = null;
					sbf=new StringBuffer();
					while ((strRead = reader.readLine()) != null) {
						sbf.append(strRead);
						sbf.append("\r\n");
					}
					reader.close();
					result = sbf.toString();
					//Log.i("TAG","LIST="+result);
					JSONObject object=new JSONObject(result).getJSONObject("data");
					JSONArray array=object.getJSONArray("books");
					for(int i=0;i<array.length();i++){
						JSONObject js=array.getJSONObject(i);
						OneList onelist=new OneList();
						onelist.setTitle(js.getString("title"));
						onelist.setStartTime(js.getString("startTime"));
						onelist.setDays(js.getString("routeDays"));
						onelist.setLikeCount(js.getString("likeCount"));
						onelist.setBookUrl(js.getString("bookUrl"));
						onelist.setHeadImageUrl(js.getString("headImage"));
						Document doc=Jsoup.connect(onelist.getBookUrl())
								.userAgent("Mozilla").timeout(2000).post();
						Elements elements=doc.select(".main_leftbox").first().select(".text");
						onelist.setAbout("\t\t"+elements.text());
						list.add(onelist);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				//	Log.i("TAG","LIST="+list);
				return list;
			}
			@Override
			protected void onPostExecute(List<OneList> result) {
				listener.refreshList(result);;
			}
		}.execute();
	}

	public static void getDetail(final String url,final OnDetailContentsFinishListener listener){
		new AsyncTask<Void,Void,List<String[]>>() {
			String[] text=null;
			String[] pic=null;
			List<String[]> details=new ArrayList<String[]>();
			@Override
			protected List<String[]> doInBackground(Void... params) {
				try {
					Document doc=Jsoup.connect(url).timeout(3000).post();
					Elements elements=doc.getElementsByClass("date-content");
					for(int i=1;i<elements.size()-2;i++){
						//////////////////////////////////////时间/////////////////////////
						text=new String[1];
						String dayAndTima=elements.get(i).select(".date").text();
						String day=dayAndTima.substring
								(0,dayAndTima.length()-11);
						String time=dayAndTima.substring
								(dayAndTima.length()-11,dayAndTima.length());
						text[0]=("第"+day.substring(1,day.length())+"天");
						details.add(text);
						text=new String[1];
						text[0]=("日期:"+time);
						details.add(text);
						///////////////////////// 内容//////////////////////////////////////////
						Elements elements2=elements.select(".planboxday").get(i-1).select(".planbox");
						for(int j=0;j<elements2.size();j++){
							text=new String[1];
							String content=elements2.get(j).text();
							String[] ct=content.split("加载更多图片");
							text[0]=("\t\t"+ct[0]);
							details.add(text);
							Elements elements3=elements2.get(j).select(".img_link");
							if(elements3.size()>0){
								pic=new String[elements3.size()];
								for(int k=0;k<elements3.size();k++){
									String imageUrl=elements3.get(k).select("img").attr("data-src");
									pic[k]=(imageUrl);
								}
								details.add(pic);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return details;
			}
			@Override
			protected void onPostExecute(List<String[]> result) {
				listener.onGetDetailContents(result);
			}
		}.execute();
	}
	public static void getPlace(final OnPlaceFinishListener listener){
		new AsyncTask<Void,Void,List<Place>>() {
			List<Place> list=new ArrayList<Place>();	
			String url="http://travel.qunar.com/place/";
			@Override
			protected List<Place> doInBackground(Void... params) {
				try {
					Document doc=Jsoup.connect(url).
							data("query","Java").userAgent("Mozilla").cookie("auth","token").timeout(3000).get();
					Elements elements=doc.select(".sub_list");
					for(int i=0;i<elements.size();i++){
						for(int j=0;j<elements.get(i).select(".link").size();j++){
							Place place=new Place();
							place.setCity(elements.get(i).select(".link").get(j).text());
							place.setSortLetter(getFirstChar(place.getCity()));
							place.setCity_url(elements.get(i).select(".link").get(j).attr("href"));
							list.add(place);
						}
					}
					Elements elements2=doc.select(".current").get(9).select(".listbox");
					for(int i=0;i<elements2.size();i++){
						for(int j=0;j<elements2.get(i).select(".link").size();j++){
							Place place=new Place();
							place.setCity(elements2.get(i).select(".link").get(j).text());
							place.setSortLetter(getFirstChar(place.getCity()));
							place.setCity_url(elements2.get(i).select(".link").get(j).attr("href"));
							list.add(place);
						}
					}
					//Log.i("doc",""+list.size());
					Log.i("doc",""+list);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return list;
			}
			@Override
			protected void onPostExecute(List<Place> result) {
				listener.getPlace(result);;
			}
		}.execute();
	}
	public static String getFirstChar(String value) {  
		// 首字符  
		char firstChar = value.charAt(0);  
		// 首字母分类  
		String first = null;  
		// 是否是非汉字  
		String[] print = PinyinHelper.toHanyuPinyinStringArray(firstChar);  
		if (print == null) {  
			// 将小写字母改成大写  
			if ((firstChar >= 97 && firstChar <= 122)) {  
				firstChar -= 32;  
			}  
			if (firstChar >= 65 && firstChar <= 90) {  
				first = String.valueOf((char) firstChar);  
			} else {  
				// 认为首字符为数字或者特殊字符  
				first = "#";  
			}  
		} else {  
			// 如果是中文 分类大写字母  
			first = String.valueOf((char) (print[0].charAt(0) - 32));  
		}  
		if (first == null) {  
			first = "?";  
		}  
		return first;  
	} 



}
