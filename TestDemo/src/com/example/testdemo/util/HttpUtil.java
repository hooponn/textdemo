package com.example.testdemo.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.example.testdemo.bean.OneList;
import com.example.testdemo.listener.OnDetailContentsFinishListener;
import com.example.testdemo.listener.OnOneListFinishListener;

import android.os.AsyncTask;
import android.util.Log;

public class HttpUtil {

	public static void getOneListRequest(final String city,final int pages,
			final OnOneListFinishListener listener) {
		new AsyncTask<Void, Void,List<OneList>>() {
			@Override
			protected List<OneList> doInBackground(Void... params) {
				List<OneList> list=new ArrayList<OneList>();
				BufferedReader reader = null;
				String result = null;
				StringBuffer sbf = null;
				for(int p=1;p<pages+1;p++){
					String httpUrl = "http://apis.baidu.com/qunartravel/travellist/travellist"
							+ "?" + "query="+city+"&page="+String.valueOf(p);
					try {
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
							list.add(onelist);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
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
		new AsyncTask<Void,Void,List<String>>() {
			List<String> contents=new ArrayList<String>();
			@Override
			protected List<String> doInBackground(Void... params) {
				try {
					Document doc=Jsoup.connect(url).timeout(3000).post();
					Elements elements=doc.getElementsByClass("date-content");
					for(int i=1;i<elements.size()-2;i++){
				//////////////////////////////////////时间/////////////////////////
						String dayAndTima=elements.get(i).select(".date").text();
						String day=dayAndTima.substring
								(0,dayAndTima.length()-11);
						String time=dayAndTima.substring
								(dayAndTima.length()-11,dayAndTima.length());
						contents.add("第"+day.substring(1,day.length())+"天");
						contents.add("日期:"+time);
						///////////////////////// 内容//////////////////////////////////////////
		Elements elements2=elements.select(".planboxday").get(i-1).select(".planbox");
		           for(int j=0;j<elements2.size();j++){
		        	   String content=elements2.get(j).text();
		        	   String[] ct=content.split("加载更多图片");
		        	   contents.add("\t\t"+ct[0]);
		     Elements elements3=elements2.get(j).select(".img_link");
		     			if(elements3.size()>0){
		     			for(int k=0;k<elements3.size();k++){
		     				String imageUrl=elements3.get(k).select("img").attr("data-src");
		     				Log.i("SRC","SRC="+imageUrl);
						contents.add(imageUrl);
		     			}
		     		}
		          }
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return contents;
			}
			@Override
			protected void onPostExecute(List<String> result) {
				listener.onGetDetailContents(result);
			}
		}.execute();
	}








}
