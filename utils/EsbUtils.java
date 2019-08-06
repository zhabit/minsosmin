package com.common.utils;

import java.util.Date;

import javax.xml.crypto.dsig.keyinfo.KeyValue;

public class H3CEsbUtils {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public static Object getDate4Logs(String dateStr){
		String now= DateUtils.getDate("yyyy/MM/dd");
		if(!StringUtils.isBlank(dateStr))
			now= DateUtils.getYear()+"/"+dateStr;
		Date endDate=DateUtils.parseDate(now+" 23:59:59");
		Date startDate=DateUtils.parseDate(now+" 00:00:00");
		
		 Date start7Date=new Date(startDate.getTime() - (long)6 * 24 * 60 * 60 * 1000); 
		return new Object[]{startDate,endDate,start7Date};	
	}

	//fixed:pro,random:9,date:yyyymmddhhMMss,extension:.xml
	public static String getFileNameRule(String fileNameRule) {
		try{
		String[] rules=fileNameRule.split(",");
		StringBuffer result=new StringBuffer();
		String data="";
		if(rules.length>0){
			for(String rule : rules){
				 data=getRuleData(rule);
				 if(StringUtils.isNotEmpty(data))
					 result.append(data);
			}
			return result.toString().length()>0?  result.toString() :null;
		}
		}catch (Exception e){
			return null;
		}
		return null;
	}

	private static String getRuleData(String rule) throws Exception {
		String[] keyVal=rule.split(":");
		String key=keyVal[0];
		String val=keyVal[1];
		String result=null;
		if("fixed".equals(key)){
			result=val;
		}else if("random".equals(key)){
			result=IdGen.randomBase62(Integer.parseInt(val)).toLowerCase();
			
		}else if("date".equals(key)){
			
			result=DateUtils.formatDate(new Date(), val);
		}else if("extension".equals(key)){
			
			result=val;
		}
		return result;
	}
	
	
	public static String getMIME(String end) {
		String type="";
		for (int i = 0; i < MIME_MapTable.length; i++) { 
			if (end.equals(MIME_MapTable[i][0]))
				type = MIME_MapTable[i][1];
			break;
			}
		return type;
	}


	
	private static final String[][] MIME_MapTable = {
			// {后缀名， MIME类型}
			{ "doc", "application/msword" },
			{ "docx","application/vnd.openxmlformats-officedocument.wordprocessingml.document" },
			{ "xls", "application/vnd.ms-excel" },
			{ "xlsx","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" },
			{ "pdf", "application/pdf" },
			{ "ppt", "application/vnd.ms-powerpoint" },
			{ "pptx","application/vnd.openxmlformats-officedocument.presentationml.presentation" },
			{ "txt", "text/plain" },
			{ "wps", "application/vnd.ms-works" },
			{ "xml", "text/xml" } };
	
}
