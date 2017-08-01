<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
<h2>本地头条数据抓取页面</h2>
<form action="index/crawBendiNewsData" method="post" ><br>
列表类型：
<span style="font-size:18px;"><select id="type" name="type">  
    <option value="1">默认列表</option>  
    <option value="2">手动下拉列表</option>  
</select> </span>

<br>
参数：<input type="text"  style="width:500px;height:50px" name="params" ><br>
<input type="submit" value="提交" >
</form>

<br><br>
<h2>大众点评数据抓取页面</h2>
<form action="index/crawDianpingShopsData" method="post" >
抓取城市：<input type="text"  style="width:100px;" name="cityName" ><br>
抓取类型：
<span style="font-size:18px;"><select id="type" name="type">  
    <option value="FOOD">美食</option>  
    <option value="VIDEO">电影</option>  
    <option value="LIFE">休闲娱乐</option>  
    <option value="HOTEL">酒店</option>  
    <option value="BEAUTY">丽人</option>  
    <option value="KTV">K歌</option>  
    <option value="SPORT">运动健身</option>  
    <option value="PLAY">周边游</option>  
    <option value="BABY">亲自</option>  
    <option value="MARRIED">结婚</option>  
    <option value="SHOP">购物</option>  
    <option value="PET">宠物</option>  
    <option value="LIFESERVICE">生活服务</option>  
    <option value="TRAIN">学习培训</option>  
    <option value="CAR">爱车</option>  
    <option value="HEALTH">医疗健康</option>  
    <option value="HOME">家装</option>  
    <option value="FEAST">宴会</option>  
</select> </span>
<br>
<input type="submit" value="提交" >
</form>

</body>
<script type="text/javascript" src="decode.js"></script>



</html>
