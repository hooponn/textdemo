package com.example.testdemo.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;

import com.example.testdemo.R;
import com.example.testdemo.adapter.DetailContentsAdapter;
import com.example.testdemo.adapter.DetailGridViewAdapter;
import com.example.testdemo.listener.OnDetailContentsFinishListener;
import com.example.testdemo.util.HttpUtil;

public class DetailActivity extends Activity {
	ListView lv;
	List<String[]> list;
	DetailContentsAdapter dcAdapter;
	String url;
    View view;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		url = getIntent().getStringExtra("detailUrl");
		lv = (ListView) findViewById(R.id.detail_lv);
		list = new ArrayList<String[]>();
		dcAdapter = new DetailContentsAdapter(this, list);
		lv.setAdapter(dcAdapter);
		view=getLayoutInflater().inflate(R.layout.detail_lv_item,lv,false);
	}
	@Override
	protected void onResume() {
		super.onResume();
		HttpUtil.getDetail(url, new OnDetailContentsFinishListener() {
			@Override
			public void onGetDetailContents(List<String[]> result) {
				  dcAdapter.refresh(result);
				/*for(int i=0;i<result.size();i++){
					Log.i("LISTS","Lists="+Arrays.toString(result.get(i)));
				}*/
			}
		});

	}

}
