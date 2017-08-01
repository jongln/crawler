package com.demo.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.jackson.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.demo.core.CrawSitDataTask;
import com.demo.core.DianPingSitCrawCore;
import com.demo.dao.IndexMapper;
import com.demo.entity.CommonModel;
import com.demo.entity.NewsCommonModel;
import com.demo.service.IndexService;
import com.demo.utils.ArraySort;
import com.demo.utils.CommonHttpClient;
import com.demo.utils.CommonUtils;
import com.demo.utils.HttpProxy;
import com.demo.utils.JsonUtils;
import com.demo.utils.JsoupHtmlResolver;
import com.demo.utils.ListUrlType;
import com.demo.utils.MyFutureCrawSitDataTask;
import com.demo.utils.PublicConstants;

import javax.script.Invocable;
import javax.script.ScriptEngineManager;  
import javax.script.ScriptEngine;
import javax.script.ScriptException;
@Service("IndexService")
public class IndexServiceImpl implements IndexService{

	@Autowired
	private IndexMapper indexMapper;
	
	@Override
	public List<NewsCommonModel> queryCrawBendiNewsList(List<NewsCommonModel> newsCommonModel) {
		return null;
	}
	
	@Override
	public int insertCrawBendiNewsData(List<NewsCommonModel> newsCommonModel) {
		return indexMapper.insertCrawBendiNewsData(newsCommonModel);
	}
	
	@Override
	public int insertNewsRecordList(List<NewsCommonModel> newsCommonModel) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	@Override
	public List<CommonModel> crawDianpingShopsInfo(int countNum,int cityId,String listUrl,ListUrlType type) {
		List<CommonModel> commonModelList = new ArrayList<CommonModel>();
		List<CommonModel> listData = new ArrayList<CommonModel>();
			for (int i = 1; i <= 3; i++) {
				CommonModel commonModel = new CommonModel();
				commonModel.setListUrl(listUrl.replace("#", String.valueOf(i)));
				commonModel.setUrlType(type);
				commonModel.setBusinessType(type.toString());
				commonModelList.add(commonModel);
				System.out.println(commonModel.getListUrl());
			}
			try {
				List<CommonModel> dbOldShopInfoList = indexMapper.selectDianpingRecordList(type.toString());
				listData = new DianPingSitCrawCore().crawShopsDataTask(cityId,type,commonModelList,dbOldShopInfoList);
				if(listData.size() > 0){
					int status =indexMapper.addDianpingRecordList(listData);
					if(status > 0){
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		return listData;
	}
	
	@Override
	public int crawDianpingGoodsInfo(ListUrlType type,String dianPingids) {
		int status = 0;
		String [] idsArry = dianPingids.split(",");
		Map<String,Object> params = new HashMap<String, Object>();
         params.put("ids", idsArry);
         List<CommonModel> commonModelList = indexMapper.selectDianpingShopInfo(params);
		String shopUrl= "";//店铺详情url
		for (CommonModel model : commonModelList) {
			shopUrl = PublicConstants.DIANPING_SHOP_URL.replace("#", model.getShopId());
			model.setDetailUrl(shopUrl);
			model.setUrlType(type);
		}
		String strHtml = HttpProxy.doProxy(commonModelList.get(0).getDetailUrl());
		if(!StringUtils.isEmpty(strHtml)){
			int cityId = JsoupHtmlResolver.dianpingScriptCityCodeHtmlResolver(strHtml);
			if(cityId > 0){
				try {
					List<CommonModel> listData = new DianPingSitCrawCore().crawGoodsDataTask(Integer.valueOf(cityId), type, commonModelList);
					
					/*for (int i = 0; i < listData.size(); i++) {
						listData.get(i).setGoodsNames("name"+i);
						listData.get(i).setGoodsPrices("price"+i);
						listData.get(i).setGoodsDetails("detail"+i);
					}*/
					if(listData.size() > 0){
						status = indexMapper.addGoodsInfoByShopIds(listData);
					}
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return status;
	}
	


	@Override
	public List<CommonModel> queryDianpingGoodsInfo(Integer shopId) {
		List<CommonModel> list = new ArrayList<CommonModel>();
		CommonModel commonModel = indexMapper.queryDianpingGoodsInfo(shopId);
		if(!StringUtils.isEmpty(commonModel)){
			String names = commonModel.getGoodsNames();
			String prices = commonModel.getGoodsPrices();
			String details = commonModel.getGoodsDetails();
			String [] nameArray = names.split(";");
			String [] pricesArray = prices.split(";");
			String [] detailsArray = details.split(";");
			CommonModel _commonModel = new CommonModel();
			for (String name : nameArray) {
				_commonModel.setGoodsNames(name);
				list.add(_commonModel);
			}
			for (String price : pricesArray) {
				_commonModel.setGoodsPrices(price);
				list.add(_commonModel);
			}
			for (String detail : detailsArray) {
				_commonModel.setGoodsDetails(detail);
				list.add(_commonModel);
			}
		}
		return list;
	}

	@Override
	public int uploadDianpingShopsInfo(Map<String, Object> params) {
		List<CommonModel> commonModelList = indexMapper.selectDianpingShopInfo(params);
		int status = 0;
		if(commonModelList.size() > 0){
			status = indexMapper.addDianpingShopsInfoToShopList(commonModelList);
		}
		
		return status;
	}

	@Override
	public List<CommonModel> queryExistClaimShopsInfo(Integer shopId) {
		return indexMapper.queryClaimshopInfo(shopId);
	}

	@Override
	public int uploadDianpingGoodsInfo(List<CommonModel> commonModel) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<CommonModel> queryDianpingShopList(List<CommonModel> commonModel) {
		// TODO Auto-generated method stub
		return null;
	}

	
	

}
