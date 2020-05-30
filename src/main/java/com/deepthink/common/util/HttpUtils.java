package com.deepthink.common.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * https://blog.csdn.net/remote_roamer/article/details/53644482
 * https://blog.csdn.net/zpf336/article/details/73480810
 * @author jerome
 *
 */
public class HttpUtils {

	
	private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);
	
	public static String parsePostBody(HttpServletRequest request) {
		StringBuffer sb = new StringBuffer();
		InputStream is;
		try {
			is = request.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String s = "";
			while ((s = br.readLine()) != null) {
				sb.append(s);
			}
			String str = sb.toString();
			return str;
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return "";
	}

	
	/**
	 * 获取http请求的参数
	 * @param request
	 * @return
	 */
	public static Map<String, String> getRequestParams(HttpServletRequest request) {
		Map<String, String> retMap = new HashMap<String, String>();
		Map<String, String[]> params = request.getParameterMap();
		for (String key : params.keySet()) {
			String[] temp = params.get(key);
			String value = temp[0];
			if (temp.length > 1) {
				// 数组
				String temp2 = "";
				for (int i = 1; i < temp.length; i++) {
					temp2 += "," + temp[i];
				}
				value += temp2;
			}
			retMap.put(key, value);
		}
		return retMap;
	}
	
	
	public static String executePost(String url, String body){
		try {
			HttpPost httpPost = new HttpPost(url);
			StringEntity entity = new StringEntity(body, "UTF-8");
			httpPost.setHeader("Content-Type", "application/json");
			httpPost.setEntity(entity);
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpResponse response = httpClient.execute(httpPost);
			String resultContent = new BasicResponseHandler()
					.handleResponse(response);
			return resultContent;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String executePost(String url, Map<String,String> params, String body){
		try {
			String relUrl = url + "?";
			for(Entry<String, String> entry:params.entrySet()){
				relUrl = relUrl + entry.getKey() + "=" + entry.getValue() + "&";
			}
			HttpPost httpPost = new HttpPost(relUrl);
			StringEntity entity = new StringEntity(body, "UTF-8");
			httpPost.setHeader("Content-Type", "application/json");
			httpPost.setEntity(entity);
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpResponse response = httpClient.execute(httpPost);
			String resultContent = new BasicResponseHandler()
					.handleResponse(response);
			return resultContent;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String executePost(String url, Map<String,String> params){
		try {
			String relUrl = url + "?";
			for(Entry<String, String> entry:params.entrySet()){
				relUrl = relUrl + entry.getKey() + "=" + entry.getValue() + "&";
			}
			HttpPost httpPost = new HttpPost(relUrl);
//			StringEntity entity = new StringEntity(body, "UTF-8");
//			httpPost.setEntity(entity);
			long beginTime = System.currentTimeMillis();
			CloseableHttpClient httpClient = HttpClients.createDefault();
			long secondTime = System.currentTimeMillis();
			System.out.println("second cost:"+ (secondTime - beginTime));
			HttpResponse response = httpClient.execute(httpPost);
			String resultContent = new BasicResponseHandler()
					.handleResponse(response);
			long respTime = System.currentTimeMillis();
			System.out.println("communacation cost:"+ (respTime - secondTime));
			return resultContent;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String executeGet(String url, Map<String,String> params){
		try {
			String relUrl = url + "?";
			for(Entry<String, String> entry:params.entrySet()){
				relUrl = relUrl + entry.getKey() + "=" + entry.getValue() + "&";
			}
			HttpGet httpGet = new HttpGet(relUrl);
			httpGet.setHeader("Content-Type", "application/json");
//			StringEntity entity = new StringEntity(body, "UTF-8");
//			httpPost.setEntity(entity);
			long beginTime = System.currentTimeMillis();
			CloseableHttpClient httpClient = HttpClients.createDefault();
			long secondTime = System.currentTimeMillis();
			System.out.println("second cost:"+ (secondTime - beginTime));
			HttpResponse response = httpClient.execute(httpGet);
			String resultContent = new BasicResponseHandler()
					.handleResponse(response);
			long respTime = System.currentTimeMillis();
			System.out.println("communacation cost:"+ (respTime - secondTime));
			return resultContent;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String executeAuthorizationGet(String url,String user,String password, Map<String,String> params){
		try {
			String relUrl = url;
			if (params != null) {
				relUrl = url + "?";
				for(Entry<String, String> entry:params.entrySet()){
					relUrl = relUrl + entry.getKey() + "=" + entry.getValue() + "&";
				}
			}

			HttpGet httpGet = new HttpGet(relUrl);
			httpGet.setHeader("Content-Type", "application/json");
			String plainCredentials = user+":"+password;
			String base64Credentials = new String(Base64.encodeBase64(plainCredentials.getBytes()));
			httpGet.setHeader("Authorization", "Basic "+base64Credentials);
//			StringEntity entity = new StringEntity(body, "UTF-8");
//			httpPost.setEntity(entity);
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpResponse response = httpClient.execute(httpGet);
			String resultContent = new BasicResponseHandler()
					.handleResponse(response);
			return resultContent;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Post请求
	 * 
	 * @param url
	 * @param params
	 * @param responseHandler
	 * @return
	 */
	public static String executePost(String url, Map<String, String> params, ResponseHandler<String> responseHandler) {
		if (url == null) {
			logger.error("url为空");
			return null;
		}
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(HttpUtils.wrapperRequestParams(params));
		return HttpUtils.executeHttpRequest(httpPost, responseHandler);
		
	}
	
	
	
	/**
	 * 包装请求参数
	 * 
	 * @param params
	 * @param charset
	 * @return
	 */
	public static HttpEntity wrapperRequestParams(Map<String, String> params) {
		// 设置参数
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		if ((params != null) && (params.size() != 0)) {
			for (String key : params.keySet()) {
				String value = params.get(key);
				nvps.add(new BasicNameValuePair(key, value));
			}
		}
		return new UrlEncodedFormEntity(nvps, Consts.UTF_8);
	}
	
	/**
	 * @param url
	 * @param httpRequestBase（httpPost
	 *            或 httpGet）
	 * @param responseHandler
	 *            (响应处理程序，使用者自定义)
	 * @return
	 */
	public static String executeHttpRequest(HttpRequestBase httpRequestBase, ResponseHandler<String> responseHandler) {
		CloseableHttpClient httpClient = HttpClients.createDefault();

		String responseBody = null;
		try {
			responseBody = httpClient.execute(httpRequestBase, responseHandler);
			
		} catch (Exception e) {
			logger.error("Exception error msg =" + e.getMessage(), e);
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				logger.error("IOException error msg =" + e.getMessage(), e);
			}
		}
		return responseBody;
	}
	
}
