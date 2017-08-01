package com.demo.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.demo.dao.IndexMapper;
import com.demo.entity.CommonModel;
import com.demo.utils.ArraySort;
import com.demo.utils.ListUrlType;
import com.demo.utils.MyFutureCrawSitDataTask;
import com.demo.utils.PublicConstants;

public class DianPingSitCrawCore {
	
	@Autowired
	private IndexMapper indexMapper;
	
	/**
	 * 开启线程任务抓取店铺信息数据（根据业务类别）
	 * @param commonModel
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public List<CommonModel> crawShopsDataTask(int cityId,ListUrlType type,List<CommonModel> commonModel,List<CommonModel> dbOldShopInfoList) throws InterruptedException, ExecutionException{
		System.out.println("任务开始时间："+System.currentTimeMillis());
		//step1 线程池初始化20个线程（抓取点评列表)
		ExecutorService executor = Executors.newFixedThreadPool(20);
		//存放列表futureList
		List<FutureTask> futureList = new ArrayList<FutureTask>();
		//存放详情task
		List<FutureTask> futureDetail = new ArrayList<FutureTask>();
		MyFutureCrawSitDataTask future;
		//step2 添加抓取任务
		for (CommonModel _commonModel:commonModel) {
			future = new MyFutureCrawSitDataTask(new CrawSitDataTask(ListUrlType.LISTTASK,_commonModel));
			executor.submit(future);
			final Thread currentThread = Thread.currentThread();
		    final String oldName = currentThread.getName();
		    System.out.println("oldName=="+oldName);
		    currentThread.setName("Processing-" + _commonModel.getListUrl());
		    System.out.println("newsName=="+currentThread.getName());
			
		    if(futureList.size() > 15){//添加的线程任务数大于200条时等待
		    	 Thread.sleep(2000);
		    }
			futureList.add(future);
		}
		int threadCount = ((ThreadPoolExecutor)executor).getActiveCount();
     	System.out.println("当前列表活动线程数==="+threadCount);
   
        System.out.println("所有子线程全部运行结束？"+executor.isTerminated());  
        Thread.sleep(5000);//若存在运行中子线程，继续等待
     	
		//step4 进行筛选获取新增的店铺ID（和数据库里逐一比较）
		List<CommonModel> webCommonModelList = new ArrayList<CommonModel>();
		StringBuffer new_sb = new StringBuffer();
		for (int i = 0; i < futureList.size(); i++) {
			List<CommonModel> commonModelList = (List<CommonModel>) futureList.get(i).get();//主线程全部执行完成再调用，防止阻塞
			if(commonModelList.size() > 0){
				for (CommonModel _commonModel : commonModelList) {
					if(!new_sb.toString().contains(_commonModel.getShopId())){//去除抓取重复元素
						webCommonModelList.add(_commonModel);
						new_sb.append(_commonModel.getShopId()).append(",");
					}
				}
			}
		}
		//step5 获取该类别下所有店铺ID
		int[] newArryShopIds = StringtoInt(new_sb.toString());//数组转换
		//存放的是新旧数据对比后的差集（相同元素消掉）
		int[] allShopIds;
		List<CommonModel> commonModelList = new ArrayList<CommonModel>();
		StringBuffer old_sb = new StringBuffer();
		if(dbOldShopInfoList.size() > 0){
			for (CommonModel oldCommonModel : dbOldShopInfoList) {
				old_sb.append(oldCommonModel.getShopId()).append(",");
			}
			int[] oldArryShopIds = StringtoInt(old_sb.toString());
			//对新旧店铺ID进行合并，并通过排序算法排序后，获取最新的shopId
			allShopIds = ArraySort.arrayConcat(newArryShopIds, oldArryShopIds);
			ArraySort.quickSort(allShopIds, 0, allShopIds.length-1);
			Set<Integer> shopIds = ArraySort.unique(allShopIds);//注意：这里存放的是web端抓取的和后台该类型下相同的进行比较后，得到的最新shopIds
			//存放最新的需要抓取的list集合
			for (Integer shopId : shopIds) {
				for (CommonModel _commonModel : webCommonModelList) {
					if(_commonModel.getShopId().contains(shopId.toString())){
						commonModelList.add(_commonModel);
					}
				}
			}
		}else{
			commonModelList.addAll(webCommonModelList);
		}
		if(dbOldShopInfoList.size() == 0){//首次抓取该类型数据
		}
		//step1  点评详情开启线程请求并完成对应页面解析
     	if(commonModelList.size() > 0){
     		executor = Executors.newFixedThreadPool(100);
     		for (CommonModel _commonModel : commonModelList) {
     			if(null != _commonModel){
     				System.out.println(_commonModel.getDetailUrl());
     				future = new MyFutureCrawSitDataTask(new CrawSitDataTask(ListUrlType.DETAILTASK,_commonModel));
     				executor.submit(future);
     				futureDetail.add(future);
     			}
     		}
     	}
     	
     	executor.shutdown();//线程全部执行完毕，关闭线程池（非强制性立即关闭）
        while(executor.awaitTermination(1, TimeUnit.SECONDS)){//设定等待超过时间（单位秒），同步监测ExecutorService是否已经关闭，若关闭则返回true，否则返回false
        	if(executor.isTerminated()){
            	System.out.println("所有子线程全部运行结束！");  
            	break;  
	        	}
			Thread.sleep(2000);//若存在运行中子线程，继续等待
        }

		//step6 线程全部结束后，对taskList值进行获取
        List<CommonModel> list = new ArrayList<CommonModel>();
        for (int j = 0; j < futureDetail.size(); j++) {
        	System.out.println("size=="+j);
        	System.out.println("detail=="+futureDetail.get(j));
    		List<CommonModel> _commonModelList = (List<CommonModel>) futureDetail.get(j).get();
    		if(_commonModelList.size() > 0){
//    			System.out.println("glat==="+_commonModelList.get(0).getShopGlat());
    			list.add(_commonModelList.get(0));
    		}
        }
		return list;
	}

	
	/**
	 * 开启线程任务抓取商品信息
	 * @param commonModel
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public List<CommonModel> crawGoodsDataTask(int cityId,ListUrlType type,List<CommonModel> commonModelList) throws InterruptedException, ExecutionException{
		System.out.println("任务开始时间："+System.currentTimeMillis());
		//线程池初始化5个线程
		ExecutorService executor = Executors.newFixedThreadPool(5);
		//存放详情task
		List<FutureTask> futureDetail = new ArrayList<FutureTask>();
		//存放店铺商品task
		List<FutureTask> futureGoods = new ArrayList<FutureTask>();
		MyFutureCrawSitDataTask future;
	
     	//step1  点评详情开启线程请求并完成对应页面解析
     	if(commonModelList.size() > 0){
     		executor = Executors.newFixedThreadPool(100);
     		for (CommonModel _commonModel : commonModelList) {
     			if(null != _commonModel){
     				System.out.println(_commonModel.getDetailUrl());
     				future = new MyFutureCrawSitDataTask(new CrawSitDataTask(ListUrlType.STATIC_GOODS_TASK,_commonModel));
     				executor.submit(future);
     				futureDetail.add(future);
     			}
     		}
     	}
     	int threadCounts = ((ThreadPoolExecutor)executor).getActiveCount();
     	System.out.println("当前详情活动线程数==="+threadCounts);
     	
     	//step2 判断线程是否运行结束
		executor.shutdown();//线程全部执行完毕，关闭线程池（非强制性立即关闭）
        while(executor.awaitTermination(1, TimeUnit.SECONDS)){//设定等待超过时间（单位秒），同步监测ExecutorService是否已经关闭，若关闭则返回true，否则返回false
        	if(executor.isTerminated()){
            	System.out.println("所有子线程全部运行结束！");  
            	break;  
	        	}
			Thread.sleep(2000);//若存在运行中子线程，继续等待
        }

		//step6 线程全部结束后，对taskList值进行获取
        List<CommonModel> list = new ArrayList<CommonModel>();
        for (int j = 0; j < futureDetail.size(); j++) {
        	System.out.println("size=="+j);
        	System.out.println("detail=="+futureDetail.get(j));
    		List<CommonModel> _commonModelList = (List<CommonModel>) futureDetail.get(j).get();
    		if(_commonModelList.size() > 0){
//    			System.out.println("glat==="+_commonModelList.get(0).getShopGlat());
    			list.add(_commonModelList.get(0));
    		}
        }
        Thread.sleep(2000);
        //step5 获取店铺商品信息
        if(list.size() > 0){
     		executor = Executors.newFixedThreadPool(50);
			String strUrl =  "";
			switch (type) {
			case FOOD:
				for (CommonModel _commonModel : commonModelList) {
					if(StringUtils.isNotBlank(_commonModel.getShopId()) && StringUtils.isNotBlank(_commonModel.getShopType())
							&& !org.springframework.util.StringUtils.isEmpty((_commonModel.getMainCategoryId()))){//团购信息
						System.out.println(_commonModel.getDetailUrl());
						strUrl = PublicConstants.GENERAL_GROUP_DETAIL_URL.replaceAll("SHOP_ID", _commonModel.getShopId())
								.replace("SHOP_TYPE",_commonModel.getShopType()).replace("MAIN_CATEGORY_ID", _commonModel.getMainCategoryId())
								.replaceAll("CITY_ID", String.valueOf(cityId));
						_commonModel.setGoodsUrl(strUrl);
					}
					if(StringUtils.isNotBlank(_commonModel.getShopId()) && StringUtils.isNotBlank(_commonModel.getShopType())
							&& StringUtils.isNotBlank(_commonModel.getMainCategoryId())){//菜单信息
						System.out.println(_commonModel.getDetailUrl());
						strUrl = PublicConstants.GENERAL_GOODS_DETAIL_URL.replaceAll("SHOP_ID", _commonModel.getShopId())
								.replace("SHOP_TYPE",_commonModel.getShopType()).replace("MAIN_CATEGORY_ID", _commonModel.getMainCategoryId())
								.replaceAll("CITY_ID", String.valueOf(cityId));
						if(!org.springframework.util.StringUtils.isEmpty(_commonModel.getGoodsUrl())){
							strUrl =_commonModel.getGoodsUrl()+";"+strUrl;
						}
						_commonModel.setGoodsUrl(strUrl);
					}
					future = new MyFutureCrawSitDataTask(new CrawSitDataTask(ListUrlType.DYNAMIC_GOODS_TASK,_commonModel));
					executor.submit(future);
					futureGoods.add(future);
				}
				 break;
			case VIDEO:
				/*for (CommonModel _commonModel : newCommonModelList) {(原html页面解析获取)
					if(StringUtils.isNotBlank(_commonModel.getShopId())){
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						strUrl = PublicConstants.VIDEO_DETAIL_URL.replaceAll("SHOP_ID", _commonModel.getShopId()).replace("DATE", sdf.format(new Date()));
						_commonModel.setGoodsUrl(strUrl);
					}
					future = new MyFutureCrawSitDataTask(new CrawSitDataTask(ListUrlType.GOODSTASK,_commonModel));
					executor.submit(future);
					futureGoods.add(future);
				}*/
				for (CommonModel _commonModel : commonModelList) {
					if(StringUtils.isNotBlank(_commonModel.getShopId()) && StringUtils.isNotBlank(_commonModel.getShopType())
							&& StringUtils.isNotBlank(_commonModel.getMainCategoryId())){//菜单信息
						System.out.println(_commonModel.getDetailUrl());
						strUrl = PublicConstants.GENERAL_GOODS_DETAIL_URL.replaceAll("SHOP_ID", _commonModel.getShopId())
								.replace("SHOP_TYPE",_commonModel.getShopType()).replace("MAIN_CATEGORY_ID", _commonModel.getMainCategoryId())
								.replaceAll("CITY_ID", String.valueOf(cityId));
						_commonModel.setGoodsUrl(strUrl);
					}
					future = new MyFutureCrawSitDataTask(new CrawSitDataTask(ListUrlType.DYNAMIC_GOODS_TASK,_commonModel));
					executor.submit(future);
					futureGoods.add(future);
				}
				break;
			case LIFE:case KTV:case SPORT:case PLAY: case PET:case LIFESERVICE:case CAR:case HEALTH:
				for (CommonModel _commonModel : commonModelList) {
					if(StringUtils.isNotBlank(_commonModel.getShopId()) && StringUtils.isNotBlank(_commonModel.getShopType())
							&& !org.springframework.util.StringUtils.isEmpty((_commonModel.getMainCategoryId()))){
						strUrl = PublicConstants.GENERAL_GROUP_DETAIL_URL.replaceAll("SHOP_ID", _commonModel.getShopId())
								.replace("SHOP_TYPE", _commonModel.getShopType()).replace("MAIN_CATEGORY_ID", _commonModel.getMainCategoryId())
								.replaceAll("CITY_ID", String.valueOf(cityId));
						_commonModel.setGoodsUrl(strUrl);
					}else{
						System.out.println("缺失参数URL == "+_commonModel.getDetailUrl());
					}
					future = new MyFutureCrawSitDataTask(new CrawSitDataTask(ListUrlType.DYNAMIC_GOODS_TASK,_commonModel));
					executor.submit(future);
					futureGoods.add(future);
				}
			break;
			case HOTEL:
				for (CommonModel _commonModel : commonModelList) {
					if(StringUtils.isNotBlank(_commonModel.getShopId())){
						strUrl = PublicConstants.HOTEL_DETAIL_URL.replaceAll("SHOP_ID", _commonModel.getShopId());
						_commonModel.setGoodsUrl(strUrl);
					}
					future = new MyFutureCrawSitDataTask(new CrawSitDataTask(ListUrlType.DYNAMIC_GOODS_TASK,_commonModel));
					executor.submit(future);
					futureGoods.add(future);
				}
			break;
			case MARRIED:
				for (CommonModel _commonModel : commonModelList) {
					if(StringUtils.isNotBlank(_commonModel.getShopId())){
						strUrl = PublicConstants.MARRIED_DETAIL_URL.replaceAll("SHOP_ID", _commonModel.getShopId());
						_commonModel.setGoodsUrl(strUrl);
					}
					future = new MyFutureCrawSitDataTask(new CrawSitDataTask(ListUrlType.DYNAMIC_GOODS_TASK,_commonModel));
					executor.submit(future);
					futureGoods.add(future);
				}
			break;
				
			default:
				return list;
			}
			int _threadCounts = ((ThreadPoolExecutor)executor).getActiveCount();
	     	System.out.println("当前商品活动线程数==="+_threadCounts);
	     	System.out.println("当前详情活动线程数==="+threadCounts);
	     	//step2 判断线程是否运行结束
			executor.shutdown();//线程全部执行完毕，关闭线程池（非强制性立即关闭）
	        while(executor.awaitTermination(1, TimeUnit.SECONDS)){//设定等待超过时间（单位秒），同步监测ExecutorService是否已经关闭，若关闭则返回true，否则返回false
	        	if(executor.isTerminated()){
	            	System.out.println("所有子线程全部运行结束！");  
	            	break;  
		        	}
				Thread.sleep(2000);//若存在运行中子线程，继续等待
	        }
			//存在动态抓取的商品详情时，List循环获取
			list.clear();
			for (int j = 0; j < futureGoods.size(); j++) {
				System.out.println("size=="+j);
				System.out.println("detail=="+futureGoods.get(j));
				List<CommonModel> _commonModelList = (List<CommonModel>) futureGoods.get(j).get();
				if(_commonModelList.size() > 0){
					System.out.println("goods==="+_commonModelList.get(0).getGoodsNames());
					list.add(_commonModelList.get(0));
				}
			}
     	}
        
		return list;
	}
	
	
	/**
	 * string转int数组
	 * @param str
	 * @return
	 */
	 public static int[] StringtoInt(String str) {
	    String strr[] = str.split(",");  
		 int array[] = new int[strr.length];  
		 for(int i=0;i<strr.length;i++){  
		     array[i]=Integer.parseInt(strr[i]);
		 }
	   return array;  
	 } 
}
