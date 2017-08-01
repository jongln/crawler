package com.demo.utils;

/**
 * 大众点评url分类列表
 * @author zhaojq
 * @since 2017-03-30
 */
public enum ListUrlType {
	/**
	 * 店铺
	 */
	LISTTASK,
	/**
	 * 详情
	 */
	DETAILTASK,
	/**
	 * 静态商品
	 */
	STATIC_GOODS_TASK,
	/**
	 * 动态商品
	 */
	DYNAMIC_GOODS_TASK,
	FOOD,VIDEO,LIFE,HOTEL,BEAUTY,KTV,SPORT,PLAY,BABY,MARRIED,SHOP,PET,LIFESERVICE,
	/**
	 * 学习培训
	 */
	TRAIN,
	CAR,HEALTH,HOME,
	/**
	 * 婚宴
	 */
	FEAST,
	
	LIST_1(FOOD,"10/p#"),
	LIST_2(VIDEO,"25/p#"),
	LIST_3(LIFE,"30/p#"),
	LIST_4(HOTEL,"cityName/hotel/p#n10"),
	LIST_5(BEAUTY,"50/p#"),
	LIST_6(KTV,"15/p#"),
	LIST_7(SPORT,"45/p#"),
	LIST_8(PLAY,"35/p#"),
	LIST_9(BABY,"70/p#"),	
	LIST_10(MARRIED,"55/p#"),
	LIST_11(SHOP,"20/p#"),
	LIST_12(PET,"95/p#"),
	LIST_13(LIFESERVICE,"80/p#"),
	LIST_14(TRAIN,"75/p#"),
	LIST_15(CAR,"65/p#"),
	LIST_16(HEALTH,"85/p#"),
	LIST_17(HOME,"90/g90p#"),
	LIST_18(FEAST,"40?pageNo=#");
	
	private ListUrlType index;  //key
    private String value;  //value
    private ListUrlType() {
		// TODO Auto-generated constructor stub
	}
    // 方法重写  
    private ListUrlType(ListUrlType index,String value) {  
    	this.index = index;  
        this.value = value;  
    }  
    /**
     * 根据key获取value值 
     * @param index
     * @return
     */
    public static String getValue(ListUrlType index) {  
        for (ListUrlType k : ListUrlType.values()) {  
            if (k.getIndex() == index) {  
                return k.value;  
            }  
        }  
        return null;  
    }
    
    
    public static void main(String[] args) {
    	System.out.println(getValue(FOOD));
	}
    
    public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public ListUrlType getIndex() {
		return index;
	}
	public void setIndex(ListUrlType index) {
		this.index = index;
	}

}
