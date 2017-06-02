package com.example.lockscreen.ui;

import android.app.Activity;
import android.app.Application;

import com.example.lockscreen.utils.LogUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * @title 自定义的activity堆栈
 * @author robinchen
 *
 */
public class MainApplication extends Application {

	@Override
	public void onCreate(){
		LogUtils.i("mainApplication--onCreate()");
		super.onCreate();
	}

	private List<Activity> activityList = new LinkedList<Activity>();
	
	public List<Activity> getActivityList() {
		return activityList;
	}

	public void setActivityList(List<Activity> activityList) {
		this.activityList = activityList;
	}

	private static MainApplication instance;
	
	private MainApplication(){
	}
	
	//单例模式中获取唯一的MainApplication实例    
	public static MainApplication getInstance(){
		if(instance == null){
			instance = new MainApplication();
		}
		return instance;
	}
	
	/**
	 * 添加Activity到容器中   
	 * @param activity
	 */
	public void addActivity(Activity activity){   
		activityList.add(activity);   
	}  
	
	/**
	 * @title 关闭activity
	 * @param cla
	 */
	public void finishActivity(Class<?> cla){
		for(Activity activity:activityList){   
			if(activity!=null){
				try{
					if(activity.getClass().equals(cla)){
						activity.finish();   
						activityList.remove(cla);
					}
				}catch(Exception e){
					continue;
				}
				
			}
		}   
	}
	
	/**
	 * @title 移除容器中的单个activity
	 * @param cla
	 */
	public void removeActivity(Class<?> cla){
		for(Activity activity:activityList){   
			if(activity!=null){
				try{
					if(activity.getClass().equals(cla)){
						activityList.remove(cla);
					}
				}catch(Exception e){
					continue;
				}
				
			}
		}   
	}
	/**
	 * @title 移除容器中的多个activity
	 * @param clas
	 */
	public void removeActivity(Class<?>[] clas){
		for(Activity activity:activityList){   
			if(activity!=null){
				try{
					for(int i=0;i<clas.length;i++){
						if(activity.getClass().equals(clas[i])){
							activityList.remove(clas[i]);
						}
					}
				}catch(Exception e){
					continue;
				}
				
			}
		}   
	}
	
	public Activity getActivity(Class<?> cla){
		for(Activity activity:activityList){   
			if(activity!=null){
				try{
					if(activity.getClass().equals(cla)){
						return activity;   
					}
				}catch(Exception e){
					continue;
				}
			}
		}
		return null;
	}
	/**
	 * 遍历所有Activity并finish   
	 */
	public void exit(){   
		for(Activity activity:activityList){   
			if(activity!=null){
				try{
					activity.finish();   
				}catch(Exception e){
					continue;
				}
				
			}
		}   
		System.exit(0);   
	}  
	
	/**
	 * @title 遍历所有activity并关闭
	 */
	public void finishAllActivity(){
		for(Activity activity:activityList){   
			if(activity!=null){
				try{
					activity.finish();   
				}catch(Exception e){
					continue;
				}
			}
		}   
	}
}
