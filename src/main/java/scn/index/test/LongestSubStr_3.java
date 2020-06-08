package scn.index.test;

import java.util.HashMap;

public class LongestSubStr_3 {
	 public int lengthOfLongestSubstring(String s) {
		 HashMap<Character,Boolean> longest = new HashMap<Character,Boolean>();
		 HashMap<Character,Boolean> ct = new HashMap<Character,Boolean>();
		 Boolean reset = false;
		 for(Character c:s.toCharArray())
		 {
			 if(longest.containsKey(c)&&!reset)
			 {
				 ct.put(c, false);
				 reset = true;
			 } 
			 else if(reset)
			 {
				 ct.put(c, false);
				 if(ct.size()>longest.size())
				 {
					 longest = ct;
					 ct = new HashMap<Character,Boolean>();
					 reset = false;
				 }
			 }
			else
				 longest.put(c, false);
			 
		 }
		 
		 return longest.keySet().size();
	    }
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
