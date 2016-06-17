package com.example.testdemo.ui;

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.example.testdemo.R;
import com.example.testdemo.adapter.OneAdapter;
import com.example.testdemo.bean.OneList;
import com.example.testdemo.listener.OnOneListFinishListener;
import com.example.testdemo.util.HttpUtil;

public class MainActivity extends Activity {
ListView lv;
List<OneList> list;
OneAdapter oAdapter;
Button bt_search;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		bt_search=(Button) findViewById(R.id.main_search);
		lv=(ListView) findViewById(R.id.lv_one);
		list=new ArrayList<OneList>();
		oAdapter=new OneAdapter(this, list);
		lv.setAdapter(oAdapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent=new Intent(MainActivity.this,DetailActivity.class);
				String url=oAdapter.getItem(position).getBookUrl();
				intent.putExtra("detailUrl",url);
				startActivity(intent);
			}
		});
		bt_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				AnimatorSet animatorSet=new AnimatorSet();
				ObjectAnimator o1=ObjectAnimator.ofFloat(v, "scaleX",1.0f,2.0f);
				ObjectAnimator o2=ObjectAnimator.ofFloat(v, "scaleY",1.0f,2.0f);
				ObjectAnimator o3=ObjectAnimator.ofFloat(v, "alpha",1.0f,0.0f);
				ObjectAnimator o4=ObjectAnimator.ofFloat(v, "translationX",0.0f,150.0f);
				animatorSet.setDuration(800);
				animatorSet.playTogether(o1,o2);
				animatorSet.play(o3).after(o1);
				animatorSet.playTogether(o3,o4);
				animatorSet.setInterpolator(new BounceInterpolator());
				animatorSet.start();
				animatorSet.addListener(new AnimatorListener() {
					@Override
					public void onAnimationStart(Animator animation) {
					}
					@Override
					public void onAnimationRepeat(Animator animation) {
					}
					@Override
					public void onAnimationEnd(Animator animation) {
						Intent intent=new Intent(MainActivity.this,SearchActivity.class);
						startActivity(intent);
						v.setScaleX(1.0f);
						v.setScaleY(1.0f);
						v.setAlpha(1.0f);
						v.setX(v.getX()-150);
					}
					@Override
					public void onAnimationCancel(Animator animation) {
					}
				});
			}
		});
	}
@Override
	protected void onResume() {
		super.onResume();
		HttpUtil.getOneListRequest("",1,new OnOneListFinishListener() {
			@Override
			public void refreshList(List<OneList> onelist) {
				oAdapter.refresh(onelist);
			}
		});
	}
}
