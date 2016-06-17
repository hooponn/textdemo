package com.example.testdemo.adapter;

import java.util.List;

import android.content.Context;
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
		vh.tv.setText(places.get(position).getCity());
		return convertView;
	}
private class ViewHolder{
	TextView tv;
}
public void refresh(List<Place> list){
	places.clear();
	places.addAll(list);
	notifyDataSetChanged();
}
}
