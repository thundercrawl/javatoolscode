package scn.index.test;

import java.util.HashMap;
import java.util.Hashtable;

public class LongestSubStr_3 {
	 public int lengthOfLongestSubstring(String s) {
		
		 HashMap<Character,Integer> substr1 = new HashMap<Character,Integer>();
		 HashMap<Character,Integer> substr2 = new HashMap<Character,Integer>();
		 Integer bigest = -1;
		 
		 for( Integer start =0; start<s.length()-1;start++)
		 {
			String substring = s.substring(start,s.length()-1 );
			for(Character c:substring.toCharArray())
			{
				
			}
		 }
		 return 0;
	    }
	public static void main(String[] args) {
		new LongestSubStr_3().lengthOfLongestSubstring("hello");

	}

}
