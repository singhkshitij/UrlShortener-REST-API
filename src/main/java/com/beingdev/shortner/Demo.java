package com.beingdev.shortner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Demo {

	public static void main(String[] args) {

	      String regex = "\\bkeyspace_hits:\\b\\+?\\d+";
	      String text = "keyspace_hits:79124214 keyspace_misses:3";
	      
	      Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
	      Matcher matcher = pattern.matcher(text);

	      if (matcher.find()) {
	    	  String[] keyValue = matcher.group().split(":");
	    	  System.out.print(keyValue[1]);
	      }
	}

}
