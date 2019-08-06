package com.common.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class MapUtils {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	  public static Map mapStringToMap(String text){
	        HashMap<String,String> data = new HashMap<String,String>();
	        Pattern p = Pattern.compile("[\\{\\}\\=\\, ]++");
	        String[] split = p.split(text);
	        for ( int i=0; i+2 <= split.length; i+=2 ){
	            data.put(split[i+1],split[i] );
	        }
	        return data;
	    }
}
