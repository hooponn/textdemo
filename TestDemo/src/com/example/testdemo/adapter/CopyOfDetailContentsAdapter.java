package com.example.testdemo.adapter;

import java.util.List;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testdemo.R;
import com.example.testdemo.util.ImageLoader;

public class CopyOfDetailContentsAdapter extends BaseAdapter {
	private Context context;
	private List<String> list;
	private int screenWidth;
	public CopyOfDetailContentsAdapter(Context context, List<String> list) {
		this.context = context;
		this.list = list;
		ImageLoader.init(context);
		WindowManager manager=(WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dMetrics=new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(dMetrics);
		screenWidth=dMetrics.widthPixels;
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
	convertView.setTag(vh);
		}else{
			vh=(ViewHolder) convertView.getTag();
		}
		if(list.get(position).contains(".jpg")||list.get(position).contains(".png")){
			vh.tv.setVisibility(View.GONE);
			vh.iv.setVisibility(View.VISIBLE);
			vh.iv.getLayoutParams().width=(int) (screenWidth*0.6f);
			vh.iv.getLayoutParams().height=(int) (screenWidth*0.4f);
			vh.iv.setX(screenWidth*0.2f);
			ImageLoader.loadImage(list.get(position), vh.iv);
		}else{
			vh.iv.setVisibility(View.GONE);
			vh.tv.setVisibility(View.VISIBLE);
			if(list.get(position).contains("µÚ")&&list.get(position).length()<5){
				vh.tv.setTextColor(0xffff8866);
				vh.tv.setTextSize(20);
				vh.tv.setText(list.get(position));
			}else{
				vh.tv.setTextColor(0xff666699);
				vh.tv.setTextSize(14);
				vh.tv.setText(list.get(position));
			}
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
