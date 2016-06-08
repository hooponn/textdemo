package com.example.testdemo.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testdemo.R;
import com.example.testdemo.util.ImageLoader;

public class DetailContentsAdapter extends BaseAdapter {
	private Context context;
	private List<String> list;
	public DetailContentsAdapter(Context context, List<String> list) {
		this.context = context;
		this.list = list;
		ImageLoader.init(context);
	}
	@Override
	public int getCount() {
		return list.size();
	}
	@Override
	public String getItem(int position) {
		return list.get(position);
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
	convertView=LayoutInflater.from(context).inflate(R.layout.detail_lv_item,parent,false);
	vh.tv=(TextView) convertView.findViewById(R.id.detail_lv_item_tv);
	vh.iv=(ImageView) convertView.findViewById(R.id.detail_lv_item_iv);
	convertView.setTag(vh);
		}else{
			vh=(ViewHolder) convertView.getTag();
		}
		if(list.get(position).contains("http://")&&list.get(position).contains(".jpg")){
			vh.tv.setVisibility(View.INVISIBLE);
			vh.iv.setVisibility(View.VISIBLE);
			ImageLoader.loadImage(list.get(position), vh.iv);
		}else{
			vh.iv.setVisibility(View.INVISIBLE);
			vh.tv.setVisibility(View.VISIBLE);
			vh.tv.setText(list.get(position));
		}
		return convertView;
	}
public void refresh(List<String> contents){
	list.clear();
	list.addAll(contents);
	notifyDataSetChanged();
}
private class ViewHolder{
	TextView tv;
	ImageView iv;
}







}
