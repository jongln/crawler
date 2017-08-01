package com.demo.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


/**
 * 排序算法
 * @author zhaojq
 * @since 2017-04-11
 */
public class ArraySort {
	
	/**
     * 快速排序算法，根据查找出中轴（默认是最低位low）的在numbers数组排序后所在位置
     * @param numbers 带查找数组
     * @param low   开始位置
     * @param high  结束位置
     * @return  中轴所在位置
     */
    public static int getMiddle(int[] numbers, int low,int high){
        int temp = numbers[low]; //数组的第一个作为中轴
        while(low < high)
        {
        while(low < high && numbers[high] >= temp)//从右向左找第一个
        {//小于等于基准值得index
            high--;
        }
        numbers[low] = numbers[high];//比中轴小的记录移到低端
        while(low < high && numbers[low] <= temp)//从左向右找第一个
        {//大于等于基准值的index
            low++;
        }
        numbers[high] = numbers[low] ; //比中轴大的记录移到高端
        }
        numbers[low] = temp ; //中轴记录到尾
        return low ; // 返回中轴的位置
    }
    
    
    
    /**
     * 冒泡排序
     * 比较相邻的元素。如果第一个比第二个大，就交换他们两个。  
     * 对每一对相邻元素作同样的工作，从开始第一对到结尾的最后一对。在这一点，最后的元素应该会是最大的数。  
     * 针对所有的元素重复以上的步骤，除了最后一个。
     * 持续每次对越来越少的元素重复上面的步骤，直到没有任何一对数字需要比较。 
     * @param numbers 需要排序的整型数组
     */
    public static void bubbleSort(int[] numbers){
        int temp = 0;
        int size = numbers.length;
        for(int i = 0 ; i < size-1; i ++)
        {
        for(int j = 0 ;j < size-1-i ; j++)
        {
            if(numbers[j] > numbers[j+1])  //交换两数位置
            {
            temp = numbers[j];
            numbers[j] = numbers[j+1];
            numbers[j+1] = temp;
            }
        }
        }
    }
    
    
    /**
     * 递归形式 分治排序算法
     * @param numbers 带排序数组
     * @param low  开始位置
     * @param high 结束位置
     */
    public static void quickSort(int[] numbers,int low,int high){
        if(low < high){
        	int middle = getMiddle(numbers,low,high); //将numbers数组进行一分为二
        	quickSort(numbers, low, middle-1);//对低字段表进行递归排序
        	quickSort(numbers, middle+1, high); //对高字段表进行递归排序
        	}
    }
    
    
    /**
     * 利用Java中容器来帮助判断元素是否重复。可以使用Set Map List等 
     * 这里我们利用Set容器不能存放相同的元素的特性，
     * 同样用index来表示不重复的元素应该存放的下标 
     * 当前元素如果能够成功加入到Set容器中，说明这个元素还没有重复的，
     * 那么当前元素就可以放到下标index的地方，index++；
     * 如果add失败，那么说明重复了，同样将对应参数从set里清除掉。
     * 可以保证元素的相对位置不变， 不过需要额外的Set容器的空间。
     * 时间复杂度是n，空间复杂度也是n
     * 
     * @param nums 输入需要去重的数组
     * @return 返回去重后数组的长度
     */
    public static Set<Integer> unique(int[] nums) {
        if (nums.length == 0) {
            return null;
        }
        Set<Integer> set = new HashSet<Integer>();
        int index = 0;
        for (int i = 0, len = nums.length; i < len; i++) {
            if (set.add(nums[i])) {
                nums[index++] = nums[i];
            }
            else{
            	if(set.contains(nums[i])){
            		set.remove(nums[i]);
            /*		if(set.contains(nums[i])){
            			set.remove(nums[i]);
            			System.out.println(111);
            		}*/
            	}
            }
        }
        return set;
    }
    
    
    
    /**
     * 进行数组合并
     * @param numbers_1
     * @param numbers_2
     * @return
     */
    public static int[] arrayConcat(int[] numbers_1, int[] numbers_2) {  
    	  int[] result = Arrays.copyOf(numbers_1, numbers_1.length + numbers_2.length);  
    	  System.arraycopy(numbers_2, 0, result, numbers_1.length, numbers_2.length);  
    	  return result;  
    	}   
    
    
    
    public static void main(String[] args) {
    	int[] numbers_1 = {10,20,15,0,6,7,2,1,-5,55,101};
    	int[] numbers_2 = {10,20,15,0,6,7,2,1,-5,55,55,59};
    	int[] ff = {10,20,15,0,6,7,2,1,-5,55,59,6};
    	 Set<Integer>num1 = unique(numbers_1);
    	 Set<Integer>num2 = unique(numbers_2);
    	/* quick(numbers_1);
    	 quick(numbers_2);*/
    	 
    	    int[] both= arrayConcat(numbers_1, numbers_2);
//    	    System.out.println(Arrays.toString(both));
    	    
    	 System.out.println("去重复前："+Arrays.toString(both));
    	 Set<Integer>num = unique(both);
    	System.out.println("去重复后："+num);
      /*  System.out.print("排序前：");
        for(int i = 0 ; i < numbers.length ; i ++ )
        {
        System.out.print(numbers[i] + ",");
        }
        quick(numbers);
        System.out.print("排序后：");
        for(int i = 0 ; i < numbers.length ; i ++ )
        {
        System.out.print(numbers[i] + ",");
        }*/
	}
    
   
    

}
