package scn.index.test;

import java.util.HashMap;

import com.sun.javafx.collections.MappingChange.Map;

public class TwoSum_1 {
	 public int[] twoSum(int[] nums, int target) {
		    int[] res = new int[2];
		    HashMap<Integer,Integer> r2i = new HashMap<Integer, Integer>();
		    
		    for(int i=0;i<nums.length;i++)
		    {
		    	Integer r = target - nums[i];
		    	if(r2i.containsKey(r))
		    	{
		    		res[0] = i;
		    		res[1] = r2i.get(r);
		    		return res;
		    	}
		    	r2i.put(nums[i], i);
		    }
		    
		    return res;
		}
	
	public  static void main(String[] args)
	{
		
		
	}
}
