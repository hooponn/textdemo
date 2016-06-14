package com.example.testdemo.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.testdemo.R;
import com.example.testdemo.util.ImageLoader;

public class DetailContentsAdapter extends BaseAdapter {
	private Context context;
	private List<String[]> list;
	private int screenWidth;
	private DetailGridViewAdapter dgAdapter;;
	public DetailContentsAdapter(Context context, List<String[]> list) {
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
	public String[] getItem(int position) {
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
	vh.gv=(GridView) convertView.findViewById(R.id.detail_gridView);
	convertView.setTag(vh);
		}else{
			vh=(ViewHolder) convertView.getTag();
		}
		if(list.get(position)[0].contains(".jpg")||list.get(position)[0].contains(".png")){
			vh.tv.setVisibility(View.GONE);
			vh.gv.setVisibility(View.VISIBLE);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(  
	                screenWidth/3*list.get(position).length, LinearLayout.LayoutParams.WRAP_CONTENT); 
			vh.gv.setLayoutParams(params);  
			vh.gv.setColumnWidth((int) (screenWidth/3));  
			vh.gv.setHorizontalSpacing(2);  
			vh.gv.setStretchMode(GridView.NO_STRETCH);  
			vh.gv.setNumColumns(list.get(position).length);  
			dgAdapter=new DetailGridViewAdapter(context,list.get(position));
			vh.gv.setAdapter(dgAdapter);
		}else{
			vh.tv.setVisibility(View.VISIBLE);
			vh.gv.setVisibility(View.GONE);
			if(list.get(position)[0].contains("µÚ")&&list.get(position)[0].length()<5){
				vh.tv.setTextColor(0xffff8866);
				vh.tv.setTextSize(20);
				vh.tv.setMaxLines(8);
				vh.tv.setEllipsize(TruncateAt.END);
				vh.tv.setText(list.get(position)[0]);
			}else{
				vh.tv.setTextColor(0xff666699);
				vh.tv.setTextSize(14);
				vh.tv.setMaxLines(8);
				vh.tv.setEllipsize(TruncateAt.END);
				vh.tv.setText(list.get(position)[0]);
			}
		}
		return convertView;
	}
public void refresh(List<String[]> contents){
	list.clear();
	list.addAll(contents);
	notifyDataSetChanged();
}
private class ViewHolder{
	TextView tv;
	GridView gv;
}







}
