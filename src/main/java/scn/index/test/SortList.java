package scn.index.test;

import java.util.Arrays;

public class SortList {

	static int[] mergedSort(int[] arr)
	{
		
		return arr;
	}
	static int[] mergedSortArray(int[] arr1,int[]arr2)
	{
		int[] result = new int[arr1.length+arr2.length];
		System.out.println("result:"+Arrays.toString(result)+result.length+" "+arr1.length+" "+arr2.length);
		int i1 =0;int i2=0;int i3=0;
		while(i1<arr1.length||i2<arr2.length)
		{
			if(i1<arr1.length&&i2<arr2.length)
			{
				if(arr1[i1]<arr2[i2])
					result[i3++] = arr1[i1++];
				else
					result[i3++] = arr2[i2++];
			}
			else if(i1==arr1.length)
			{
				for(int j = i2;j<arr2.length;j++)
				{
					result[i3++] = arr2[i2++];
				}
			}
			else if(i2 == arr2.length)
			{

				for(int j = i1;j<arr1.length;j++)
				{
					result[i3++] = arr1[i1++];
				}
			}
		}
		System.out.println("result:"+Arrays.toString(result)+result.length);
		return result;
	}
	public static void main(String[] args) {
		
		int[] input1 = {1,2,3,4,5,6,7,543,2231,3,4,1};
		int[] input2 = {7,4,58,1,3,2123,42,3,1345,5};
		
		System.out.println(Arrays.toString(mergedSort(input1)));
		System.out.println(Arrays.toString(mergedSort(input2)));
		System.out.println(Arrays.toString(mergedSortArray(input1,input2)));
	}

}
