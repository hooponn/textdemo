package com.example.testdemo.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.testdemo.R;
import com.example.testdemo.adapter.DetailContentsAdapter;
import com.example.testdemo.bean.Detail;
import com.example.testdemo.bean.DetailBean;
import com.example.testdemo.listener.OnDetailContentFinishListener;
import com.example.testdemo.listener.OnDetailContentsFinishListener;
import com.example.testdemo.listener.OnDetailFinishListener;
import com.example.testdemo.util.HttpUtil;

public class DetailActivity extends Activity {
	ListView lv;
	List<String[]> list;
	DetailContentsAdapter dcAdapter;
	String url;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		url = getIntent().getStringExtra("detailUrl");
		lv = (ListView) findViewById(R.id.detail_lv);
		list = new ArrayList<String[]>();
		dcAdapter = new DetailContentsAdapter(this, list);
		lv.setAdapter(dcAdapter);
	}
	@Override
	protected void onResume() {
		super.onResume();
		/*HttpUtil.getDetail(url, new OnDetailContentsFinishListener() {
			@Override
			public void onGetDetailContents(List<String[]> result) {
				  dcAdapter.refresh(result);
				for(int i=0;i<result.size();i++){
					Log.i("LISTS","Lists="+Arrays.toString(result.get(i)));
				}
			}
		});*/
       /* HttpUtil.getDetails(url,new OnDetailFinishListener() {
			@Override
			public void onGetDetailContents(List<Detail> result) {
				for(Detail d:result){
					Log.i("LIST","List="+d.toString());
				}
			}
		});*/
		HttpUtil.getDetailContent(url, new OnDetailContentFinishListener() {
			@Override
			public void onGetDetailContents(DetailBean result) {
				Log.i("TAG",result.getDayInfo()+"\n"+result.getPlanBox());
			}
		});
	}

}
