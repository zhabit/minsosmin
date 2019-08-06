package com.common.utils;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.AllClientPNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.config.Global;

/**
 * 参数 { "requestUrl": "http://10.95.253.74:17001/bp220.go?method=init",
 * "requestData": "{}", "requestMode": "POST", "contentType":
 * "application/json;charset=UTF-8", "inputCharset": "UTF-8", "outputCharset":
 * "UTF-8", "clientOutputCharset": "UTF-8",
 * headers:[{"headerName":"Content-Type","headerValue":"application/json"},{
 * "headerName":"Accept","headerValue":"gzip"}] }
 * 
 * 
 * 返回值 { "ResultCode":200, "ResultMsg":"SUCCESS", "Data":"{"key","value"}"
 * 
 * }
 */

public class TransferApiResource{
    private static final Logger logger              = LoggerFactory.getLogger(TransferApiResource.class);
    private static final String requestData         = "";
    private static final String requestMode         = "POST";
    private static final String contentType         = "application/json";
    private static final String xinputCharset       = "UTF-8";
    private static final String xoutputCharset      = "UTF-8";
    private static final String clientOutputCharset = "UTF-8";
    /** 用于发送消息的httpClient */
    private HttpClient httpClient;
    /**
     * httpClient的配置内容
     *
     */
    private int maxConnPerHost = 40;
    private int maxTotalConn = 200;
    /**
     * 默认等待连接建立超时，单位:毫秒
     */
    private int connectionTimeout = 5000;
    /**
     * 默认等待数据返回超时，单位:毫秒
     */
    private int soTimeout = 10000;
    /**
     * 默认请求连接池连接超时,单位:毫秒
     */
    private int connectionManagerTimeout = 2000;

    public String send(String body,String url, ServletInputStream servletInputStream){
        JSONObject bodyAsJson;
		try {		
        // 初始化参数
     //   String url = bodyAsJson.getString("requestUrl");
        String data =body;
        String inputCharset = xinputCharset;
        String outputCharset =  xoutputCharset;
        String clientOutput =  clientOutputCharset;
        String mode =  requestMode;
        String type =  contentType;

        JSONArray headArray = new JSONArray();

        Map<String, String> headers = jsonArray2Map(headArray);

        String result = getReturn(url, data, inputCharset, outputCharset, clientOutput, mode, type, headers,servletInputStream);
        return result;
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return null;
    }

    /**
     * @Title: jsonArray2Map
     * @Description:jsonarray转换成map
     * @param headArray
     * @return Map<String,String>
     */

    private Map<String, String> jsonArray2Map(JSONArray headArray){
        Map<String, String> map = new HashMap<String, String>();
        int len = headArray.length();
        for(int i = 0; i < len; i++){
            JSONObject object = headArray.getJSONObject(i);
            map.put(object.getString("headerName"), object.getString("headerValue"));
        }
        return map;
    }

    @SuppressWarnings({ "deprecation", "resource" })
    public static String getReturn(String restUrl, String requestData, String inputCharset, String outputCharset,
            String clientOutputCharset, String requestMode, String contentType, Map<String, String> headers, ServletInputStream servletInputStream){
    	  
    	  HttpConnectionManager httpConnectionManager = new MultiThreadedHttpConnectionManager();
          HttpConnectionManagerParams connectionManagerParams = new HttpConnectionManagerParams();
          connectionManagerParams.setConnectionTimeout(5000);
          connectionManagerParams.setSoTimeout(10000);
          connectionManagerParams.setMaxTotalConnections(200);
          connectionManagerParams.setDefaultMaxConnectionsPerHost(40);
          httpConnectionManager.setParams(connectionManagerParams);
          org.apache.commons.httpclient.HttpClient httpClient = new org.apache.commons.httpclient.HttpClient(httpConnectionManager);
          
         httpClient.getParams().setConnectionManagerTimeout(2000);
    	  

        PostMethod postMethod = new PostMethod(restUrl);
        try {
        	
        	//  postMethod.addParameter("resend", "yes");
      //      postMethod.setRequestEntity(requestEntity);
        	RequestEntity e=new StringRequestEntity(requestData,contentType,inputCharset);
        	postMethod.setRequestEntity(e);
         //   postMethod.setRequestBody(requestData);//.setRequestBody(servletInputStream);
            httpClient.executeMethod(postMethod);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
           return null;
        } finally {
            postMethod.releaseConnection();
        }
    	
    	
      /*  HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(AllClientPNames.CONNECTION_TIMEOUT, 30000);
        httpClient.getParams().setParameter(AllClientPNames.SO_TIMEOUT, 30000);
        // httpClient.getParams().setParameter("http.protocol.version",
        // HttpVersion.HTTP_1_1);
        httpClient.getParams().setParameter("http.protocol.content-charset", inputCharset);
        HttpGet httpGet = null;
        HttpPost httpPost = null;
        JSONObject result = new JSONObject();
        HttpEntity entity = null;
        HttpResponse response = null;
        try{
            if(TransferApiResource.requestMode.equalsIgnoreCase(requestMode)){
                // POST请求
                httpPost = new HttpPost(restUrl);
                
                com.alibaba.fastjson.JSONObject alMsgJSON=com.alibaba.fastjson.JSONObject.parseObject(requestData==null || "".equals(requestData) ?"{\"userId\" : \"c16433\"}":requestData);
                alMsgJSON.put("resend", "yes");
                
                StringEntity reqEntity = new StringEntity(alMsgJSON.toJSONString(), inputCharset);
                reqEntity.setContentType(contentType);
                reqEntity.setContentEncoding(inputCharset);
                httpPost.setEntity(reqEntity);
                HttpServletRequest request=null;
                httpPost.
                // httpPost.setHeader("Accept", MediaType.APPLICATION_JSON);

                // 设置header
                for(Map.Entry<String, String> entry : headers.entrySet()){
                    httpPost.addHeader(entry.getKey(), entry.getValue());
                }

                response = httpClient.execute(httpPost);

            } else{
                httpGet = new HttpGet(restUrl);
                // httpGet.setHeader("Accept", MediaType.APPLICATION_JSON);

                // 设置header
                for(Map.Entry<String, String> entry : headers.entrySet()){
                    httpGet.addHeader(entry.getKey(), entry.getValue());
                }

                response = httpClient.execute(httpGet);
            }

            int status = response.getStatusLine().getStatusCode();
            logger.info("网络状态:status=" + status);
            if(HttpStatus.SC_OK == status){
                entity = response.getEntity();
                String ret = "";
                if(entity != null){
                    ret = new String(EntityUtils.toString(entity).getBytes(outputCharset), clientOutputCharset);
                }

                result.put("ResultCode", HttpStatus.SC_OK);
                result.put("ResultMsg", "SUCCESS");
                result.put("Data", ret);

            } else{
                entity = response.getEntity();
                String error = new String(EntityUtils.toString(entity).getBytes(outputCharset), clientOutputCharset);
                String ret = "网络错误:错误代码" + status + "," + error;

                result.put("ResultCode", status);
                result.put("ResultMsg", "FAIL");
                result.put("Data", ret);
            }
            return result.toString();
        }
        catch(Exception e){
            e.printStackTrace();
            result.put("ResultCode", 500);
            result.put("ResultMsg", "EXCEPTION");
            return result.toString();
        }
        finally{
            if(null != entity){
                try{
                    EntityUtils.consume(entity);
                }
                catch(IOException e){
                    e.printStackTrace();
                }
            }
            if(null != httpGet && httpGet.isAborted()){
                httpGet.abort();
            }
            if(null != httpPost && httpPost.isAborted()){
                httpPost.abort();
            }
            if(null != httpClient){
                httpClient.getConnectionManager().shutdown();
            }
        }*/

    }
}