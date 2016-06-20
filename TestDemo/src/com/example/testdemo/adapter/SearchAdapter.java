package com.example.testdemo.adapter;

import java.security.spec.ECField;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.testdemo.R;
import com.example.testdemo.bean.Place;

public class SearchAdapter extends BaseAdapter{
		private Context context;
		private List<Place> places;
		private String str;
		
	public SearchAdapter(Context context, List<Place> places) {
			this.context = context;
			this.places = places;
		}
	@Override
	public int getCount() {
		return places.size();
	}
	@Override
	public Place getItem(int position) {
		return places.get(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh=null;
		if(convertView==null){
		vh=new ViewHolder();
		convertView=LayoutInflater.from(context).inflate(R.layout.search_list_item,parent,false);
		vh.tv=(TextView) convertView.findViewById(R.id.search_item_tv);
		convertView.setTag(vh);
		}else{
			vh=(ViewHolder) convertView.getTag();
		}
		if(str!=null){
			int index=0;
			if(str.substring(0,1).matches("[a-z]")){
				index=places.get(position).getLowerCase().indexOf(str);
			}else if(str.substring(0,1).matches("[A-Z]")){
				index=places.get(position).getUpperCase().indexOf(str);
			}else if(str.substring(0,1).matches("[\u4E00-\u9FA5]")){
				index=places.get(position).getCity().indexOf(str);
			}
			SpannableStringBuilder builder=new SpannableStringBuilder(places.get(position).getCity());
			ForegroundColorSpan span=new ForegroundColorSpan(0xfff86f4f);
			builder.setSpan(span, index, index+str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			vh.tv.setText(builder);
		}else{
		vh.tv.setText(places.get(position).getCity());
		}
		return convertView;
	}
private class ViewHolder{
	TextView tv;
}
public void refresh(List<Place> list,String str){
	places.clear();
	this.str=str;
	places.addAll(list);
	notifyDataSetChanged();
}
}
