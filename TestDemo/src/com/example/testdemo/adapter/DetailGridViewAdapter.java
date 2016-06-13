package com.example.testdemo.adapter;

import com.example.testdemo.R;
import com.example.testdemo.util.ImageLoader;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class DetailGridViewAdapter extends BaseAdapter{
		private Context context;
		private String[] strs;
		private int screenWidth;
	public DetailGridViewAdapter(Context context, String[] strs) {
			this.context = context;
			this.strs = strs;
			ImageLoader.init(context);
			WindowManager manager=(WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			DisplayMetrics dMetrics=new DisplayMetrics();
			manager.getDefaultDisplay().getMetrics(dMetrics);
			screenWidth=dMetrics.widthPixels;
		}
	@Override
	public int getCount() {
		return strs.length;
	}
	@Override
	public String getItem(int position) {
		return strs[position];
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
convertView=LayoutInflater.from(context).inflate(R.layout.detail_gridview_item,parent,false);
vh.iv=(ImageView) convertView.findViewById(R.id.detail_gridview_item_iv);
convertView.setTag(vh);
		}else{
			vh=(ViewHolder) convertView.getTag();
		}
		vh.iv.getLayoutParams().width=(int) (screenWidth/3);
		vh.iv.getLayoutParams().height=(int) (screenWidth/4);
		ImageLoader.loadImage(strs[position],vh.iv);
		return convertView;
	}
private class ViewHolder{
	ImageView iv;
}
}
