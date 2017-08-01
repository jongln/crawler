package com.demo.dao;

import java.util.List;
import java.util.Map;
import com.demo.entity.CommonModel;
import com.demo.entity.NewsCommonModel;

public interface IndexMapper {
	
	
	/**
	 * 查询抓取的本地新闻列表
	 * @param newsCommonModel
	 * @return
	 */
	public List<NewsCommonModel> queryCrawBendiNewsList(List<NewsCommonModel> newsCommonModel);
	
	
	/**
	 * 插入抓取的本地记录
	 * @param newsList
	 * @return
	 */
	public int insertCrawBendiNewsData(List<NewsCommonModel> newsList);
	
	/**
	 * 查询抓取的本地新闻记录
	 * @return
	 */
	public List<NewsCommonModel> queryCrawBendiNewsList();
	
	/**
	 * 批量插入大众点评店铺信息数据
	 * @param commonModel
	 * @return
	 */
	public int addDianpingRecordList(List<CommonModel> commonModel);
	
	/**
	 * 获取点评店铺基本信息
	 * @param map
	 * @return
	 */
	public List<CommonModel> selectDianpingShopInfo(Map<String,Object> params);
	
	/**
	 * 获取插入的点评表数据
	 * @param businessType
	 * @return
	 */
	public List<CommonModel>  selectDianpingRecordList(String businessType);
	
	/**
	 * 批量添加商品信息根据店铺IDs
	 * @param commonModel
	 * @return
	 */
	public int addGoodsInfoByShopIds(List<CommonModel> commonModel);
	
	/**
	 * 查询点评商品信息根据店铺Id
	 * @param shopId
	 * @return
	 */
	public CommonModel queryDianpingGoodsInfo(Integer shopId);
	
	/**
	 * 添加抓取的店铺信息
	 * @param commonModelList
	 * @return
	 */
	public int addDianpingShopsInfoToShopList(List<CommonModel> commonModelList);
	
	/**
	 * 获取店铺列表中已认领的店铺信息
	 * @param shopId
	 * @return
	 */
	public List<CommonModel> queryClaimshopInfo(Integer shopId);
	
	

}
