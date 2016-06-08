package com.example.testdemo.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.example.testdemo.util.DiskLruCache.Editor;
import com.example.testdemo.util.DiskLruCache.Snapshot;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

public class ImageLoader {
    protected static final int LOAD_FINISH = 101;
    protected static final int NEW_TASK = 102;
	public static int Thread_Count;//�̳߳��е����������ֻ�CPU�������.
    public static Context c;//������
    public static LruCache<String, Bitmap> MemCache;//�ڴ滺��
    public static LinkedBlockingDeque<Runnable> taskQueue;//˫�����   ��������ѡ��LIFO����FIFO
    public static ExecutorService exec;//�̳߳�
    public static Handler pollhandler;//ȡ���������handler
    public static Handler uiHandler;//����UI��handler
    public static Thread pollThread;//���� pollHandler;
    public static DiskLruCache DiskCache;//���̻���
    public static boolean isFirstTime=true;//ȷ��ֻ��ʼ��һ��
    /*��ʼ���������Ե�ֵ*/
    public static void init(Context context){
    	if(!isFirstTime){
			return;
		}
    	c=context;
    	Thread_Count=getNumberOfCores();
    	MemCache=new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory()/8)){
    		         @Override
    	             protected int sizeOf(String key, Bitmap value) {
						return value.getHeight()*value.getRowBytes();
    	             }
    	};
    	//��ʼ�����̻���
    			try {
    				DiskCache = DiskLruCache.open(directory(), appVersion(),1,1024*1024*10);
    			} catch (IOException e1) {
    				e1.printStackTrace();
    			}
    	taskQueue=new LinkedBlockingDeque<Runnable>();
    	exec=Executors.newFixedThreadPool(Thread_Count);
    	uiHandler=new Handler(Looper.getMainLooper()){
    		@Override
    		public void handleMessage(Message msg) {
    			if(msg.what==LOAD_FINISH){
    				ValueObject vo=(ValueObject) msg.obj;
    				ImageView iv=vo.iv;
    				String url=vo.url;
    				Bitmap bitmap=vo.bitmap;
    				if(iv.getTag().toString().equals(url)){
    					iv.setImageBitmap(bitmap);
    				}
    			}else{
    				super.handleMessage(msg);
    			}
    		}
    	};
    	pollThread=new Thread(){
    		@Override
			public void run() {
    			   Looper.prepare();
    	       pollhandler=new Handler(){
				@Override
    			public void handleMessage(Message msg) {
    				if(msg.what==NEW_TASK){
    					try {
							Runnable task=taskQueue.takeFirst();
							exec.execute(task);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
    				}else{
    			          	super.handleMessage(msg);
    				}
    				
    			}
    		};
    		Looper.loop();
    		}
    	};
    	pollThread.start();
    	isFirstTime=false;
    }
private static int appVersion() {
		
		try {
			PackageInfo info = c.getPackageManager().
			getPackageInfo(c.getPackageName(),0);
			return info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 1;
	}
private static File directory() {
		//��Щ������ִ��getExternalCacheDir��ȡ
		//�ⲿ�Ļ���·����ʱ����ܻᱨ��ָ���쳣
		//��������������ڲ�����·��----getCacheDir()
		String path = c.getExternalCacheDir().getPath();
		if(path==null){
			path=c.getCacheDir().getPath();
		}
		return new File(path,"imageloadercache");
	}
public static void loadImage(final String url,final ImageView iv){
	   if(isFirstTime){
		   throw new RuntimeException("ImageLoadδ����ʼ��");
	   }
	   final String md5Url=getMD5(url);
	   iv.setTag(md5Url);
	   Bitmap bitmap=MemCache.get(md5Url);
	   if(bitmap!=null){
		//   Log.i("TAG","ͼƬ�Ǵӻ����м��ص�");
		   iv.setImageBitmap(bitmap);
		   return;
	   }
	   try {
			//�Ӵ��̻�������ͼȡ��url����Ӧ��ͼƬ
			Snapshot snap = DiskCache.get(md5Url);
			if(snap!=null){
				//Log.d("TAG","ͼ���ǴӴ��̻����л�ȡ��");
				InputStream in = snap.getInputStream(0);
				bitmap = BitmapFactory.decodeStream(in);
				//���Ӵ��̻����л�õ�ͼƬ�ŵ��ڴ滺��洢һ��
				MemCache.put(md5Url, bitmap);
				iv.setImageBitmap(bitmap);
				return;
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	   taskQueue.add(new Runnable() {
		@Override
		public void run() {
	//�����������ӣ����ͼƬ��Դ
			try {
				URL u=new URL(url);
				HttpURLConnection connection=(HttpURLConnection) u.openConnection();
				connection.setRequestMethod("GET");
				connection.setDoInput(true);
				connection.connect();
				InputStream is=connection.getInputStream();
				//Ҫ��ͼƬ����ѹ��
				Bitmap bitmap=compress(is,iv);
				is.close();
				MemCache.put(md5Url,bitmap);
				//��ѹ�����ͼ��ŵ����̻����д洢
				Editor editor = DiskCache.edit(md5Url);
				OutputStream os = editor.newOutputStream(0);
				bitmap.compress(CompressFormat.JPEG, 100, os);
				editor.commit();
				//д��־(��ѡ����)
				DiskCache.flush();
				ValueObject value=new ValueObject(iv, md5Url, bitmap);
				Message.obtain(uiHandler,LOAD_FINISH,value).sendToTarget();
			} catch (Exception e) {
				e.printStackTrace();
			}	
			}
	});
	   Message.obtain(pollhandler,NEW_TASK).sendToTarget();
   } 	
   
protected static Bitmap compress(InputStream is, ImageView iv) {
//����ImageView�ĳߴ��С����ѹ��
	Bitmap bitmap=null;
	try {
		//1���Ȼ��ԭʼͼ��ĳߴ��С
		ByteArrayOutputStream out=new ByteArrayOutputStream();
		byte[] buff=new byte[6144];
		int len=-1;
		while((len=is.read(buff))!=-1){
			out.write(buff,0,len);
		}
		Options opts=new Options();
		opts.inJustDecodeBounds=true;
		BitmapFactory.decodeByteArray(out.toByteArray(),0,out.toByteArray().length, opts);
		int width=opts.outWidth;//ͼ��Ŀ��
		int height=opts.outHeight;//ͼ��ĸ߶�
		//2�����ϣ���ĸߺͿ�
		int targetWidth=iv.getWidth();
		int targetHeight=iv.getHeight();
		if(targetHeight==0||targetWidth==0){//���ImageView�ĳߴ�ȡֵȡ����
			targetHeight=c.getResources().getDisplayMetrics().heightPixels;//�Ե�ǰ�豸��Ļ�߶ȸ�ֵ
			targetWidth=c.getResources().getDisplayMetrics().widthPixels;//�Ե�ǰ�豸��Ļ��ȸ�ֵ
		}
		//3������ѹ����
		int sampleSize=1;
		if(width*1.0/targetWidth>1||height*1.0/targetHeight>1){
		sampleSize=(int)Math.ceil(Math.max(width*1.0/targetWidth,height*1.0/targetHeight));
					//ceil---����ȡ��
		}
		//4��ѹ��ͼƬ
		opts.inSampleSize=sampleSize;
		opts.inJustDecodeBounds=false;
		bitmap=BitmapFactory.decodeByteArray(out.toByteArray(),0,out.toByteArray().length, opts);
		out.close();
	} catch (Exception e) {
		e.printStackTrace();
	}
	return bitmap;
}
private static String getMD5(String url) {
	String result="";
	try {
		MessageDigest md=MessageDigest.getInstance("md5");
		md.update(url.getBytes());
		byte[] bytes=md.digest();
		StringBuilder sb=new StringBuilder();
		for(byte b:bytes){
			String str=Integer.toHexString(b&0xFF);
			if(str.length()==1){
				sb.append("0");
			}
			sb.append(str);
		}
		result=sb.toString();
	} catch (NoSuchAlgorithmException e) {
		e.printStackTrace();
	}
	return result;
}

private static int getNumberOfCores() {
		    File file=new File("/sys/devices/system/cpu/");
		 File[] files= file.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String filename) {
					if(filename.matches("cpu[0-9]") ){
						return true;
					}else{
						return false;
					}
				}
			});
		if(files.length>0){
			return files.length;
		}else{
			return 1;
		}
	}
private static class ValueObject{
	ImageView iv;
	String url;
	Bitmap bitmap;
	public ValueObject(ImageView iv, String url, Bitmap bitmap) {
		super();
		this.iv = iv;
		this.url = url;
		this.bitmap = bitmap;
	}
	
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
