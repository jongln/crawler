package com.demo.service;

import java.util.List;
import java.util.Map;

import com.demo.entity.CommonModel;
import com.demo.entity.NewsCommonModel;
import com.demo.utils.ListUrlType;

/**
 * 抓取数据service
 * @author zhaojq
 * @since 2017-03-14
 */
public interface IndexService {

	/**
	 * 查询新闻列表
	 * @param newsCommonModel
	 * @return
	 */
	public List<NewsCommonModel> queryCrawBendiNewsList(List<NewsCommonModel> newsCommonModel);
	
	/**
	 * 插入抓取的新闻列表到本地头条里
	 * @param newsCommonModel
	 * @return
	 */
	public int insertCrawBendiNewsData(List<NewsCommonModel> newsCommonModel);
	
	/**
	 * 插入抓取的新闻列表到本地头条里
	 * @param newsCommonModel
	 * @return
	 */
	public int insertNewsRecordList(List<NewsCommonModel> newsCommonModel);
	
	/**
	 * 抓取点评店铺信息数据
	 * @param countNum 总条数
	 * @param cityId 城市ID
	 * @param listUrl 公共URL
	 * @param type 业务类型
	 * @return
	 */
	public List<CommonModel> crawDianpingShopsInfo(int countNum,int cityId,String listUrl,ListUrlType type);
	
	/**
	 * 根据店铺ID抓取店铺商品信息
	 * @param shopId
	 * @return
	 */
	public int crawDianpingGoodsInfo(ListUrlType type,String shopIds);
	
	/**
	 * 查询店铺商品明细
	 * @param goodsId
	 * @return
	 */
	public List<CommonModel> queryDianpingGoodsInfo(Integer shopId);
	
	/**
	 * 根据选中的ID，从抓取店铺列表获取要上传的店铺信息
	 * @param params
	 * @return
	 */
	public int uploadDianpingShopsInfo(Map<String, Object> params);
	
	/**
	 * 查询该店铺是否已认领
	 * @param params
	 * @return
	 */
	public List<CommonModel> queryExistClaimShopsInfo(Integer shopId);
	
	/**
	 * 上传点评商品信息到已认领店铺列表里
	 * @param params
	 * @return
	 */
	public int uploadDianpingGoodsInfo(List<CommonModel> commonModel);
	
	/**
	 * 获取抓取页面点评店铺列表
	 * @param commonModel
	 * @return
	 */
	public List<CommonModel> queryDianpingShopList(List<CommonModel> commonModel);
}
