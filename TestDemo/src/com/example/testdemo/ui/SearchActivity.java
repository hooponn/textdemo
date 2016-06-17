package com.example.testdemo.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import com.example.testdemo.R;
import com.example.testdemo.adapter.SearchAdapter;
import com.example.testdemo.bean.Place;
import com.example.testdemo.listener.OnPlaceFinishListener;
import com.example.testdemo.util.HttpUtil;

public class SearchActivity extends Activity {
List<Place> places;
List<Place> list;
EditText et_search;
ListView lv;
String[] cities=null;
SearchAdapter sa;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		et_search=(EditText) findViewById(R.id.search_et);
		lv=(ListView) findViewById(R.id.search_listView);
		places=new ArrayList<Place>();
		list=new ArrayList<Place>();
		sa=new SearchAdapter(this,places);
		lv.setAdapter(sa);
		et_search.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				if(s.toString().length()>0){
					for(Place p:places){
						int i;
						for(i=0;i<=p.getCity().length();i++){
							if(s.toString().equals(p.getCity().subSequence(0,i))||
									s.toString().equals(p.getLowerCase().substring(0,i))||
									s.toString().equals(p.getUpperCase().substring(0,i)))
							{
								list.add(p);
							}
						}
					}
				}else{
				list.clear();
				}
				sa.refresh(list);
			}
		});
	}
@Override
	protected void onResume() {
		super.onResume();
		HttpUtil.getPlace(new OnPlaceFinishListener() {
			@Override
			public void getPlace(List<Place> placeList) {
				places=placeList;
			}
		});
	}


}
