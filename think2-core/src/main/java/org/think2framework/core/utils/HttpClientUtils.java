package org.think2framework.core.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * http请求工具类
 */
public class HttpClientUtils {

	private static final String DEFAULT_ENCODING = "UTF-8"; // 默认编码

	/**
	 * 获取编码格式，如果encoding为空返回默认编码
	 * 
	 * @param encoding
	 *            编码
	 * @return 编码
	 */
	private static String getEncoding(String encoding) {
		return StringUtils.isBlank(encoding) ? DEFAULT_ENCODING : encoding;
	}

	/**
	 * 获取参数对应的请求实体
	 * 
	 * @param params
	 *            参数
	 * @param encoding
	 *            编码
	 * @return 请求实体
	 */
	private static UrlEncodedFormEntity getUrlEncodedFormEntity(Map<String, Object> params, String encoding) {
		if (null != params && params.size() > 0) {
			List<NameValuePair> nameValuePairs = new ArrayList<>();
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				nameValuePairs.add(new BasicNameValuePair(entry.getKey(), StringUtils.toString(entry.getValue())));
			}
			try {
				return new UrlEncodedFormEntity(nameValuePairs, encoding);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		} else {
			return null;
		}
	}

	/**
	 * 获取http get的请求url
	 *
	 * @param url
	 *            url
	 * @param params
	 *            参数
	 * @param encoding
	 *            编码
	 * @return url拼接参数
	 */
	private static String getUrl(String url, Map<String, Object> params, String encoding) {
		String result = url;
		if (null != params && params.size() > 0) {
			UrlEncodedFormEntity entity = getUrlEncodedFormEntity(params, encoding);
			if (null != entity) {
				if (result.contains("?")) {
					result += "&" + StringUtils.toString(entity);
				} else {
					result += "?" + StringUtils.toString(entity);
				}
			}
		}
		return result;
	}

	/**
	 * 执行一个http请求，并返回string
	 *
	 * @param httpUriRequest
	 *            请求
	 * @param encoding
	 *            编码
	 * @param header
	 *            请求头
	 * @return 返回字符串
	 */
	private static String request(HttpUriRequest httpUriRequest, String encoding, Map<String, String> header) {
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			if (null != header && header.size() > 0) {
				for (Map.Entry<String, String> entry : header.entrySet()) {
					httpUriRequest.addHeader(entry.getKey(), entry.getValue());
				}
			}
			try (CloseableHttpResponse response = httpClient.execute(httpUriRequest)) {
				if (response.getStatusLine().getStatusCode() != 200) {
					throw new Exception("http response status line " + response.getStatusLine());
				}
				HttpEntity entity = response.getEntity();
				String result = EntityUtils.toString(entity, encoding);
				EntityUtils.consume(entity);
				return result;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * http get请求，UTF-8编码格式，返回字符串
	 *
	 * @param url
	 *            请求url
	 * @return 返回值字符串
	 */
	public static String get(String url) {
		return get(url, null, null, null);
	}

	/**
	 * http get请求，UTF-8编码格式，返回字符串
	 *
	 * @param url
	 *            请求url
	 * @param params
	 *            请求参数
	 * @return 返回值字符串
	 */
	public static String get(String url, Map<String, Object> params) {
		return get(url, params, null, null);
	}

	/**
	 * http get请求，返回字符串
	 *
	 * @param url
	 *            请求url
	 * @param encoding
	 *            编码格式
	 * @return 返回值字符串
	 */
	public static String get(String url, String encoding) {
		return get(url, null, encoding, null);
	}

	/**
	 * http get请求，返回字符串
	 *
	 * @param url
	 *            请求url
	 * @param params
	 *            请求参数
	 * @param encoding
	 *            编码格式
	 * @return 返回值字符串
	 */
	public static String get(String url, Map<String, Object> params, String encoding, Map<String, String> header) {
		String ec = getEncoding(encoding);
		HttpGet httpGet = new HttpGet(getUrl(url, params, ec));
		return request(httpGet, ec, header);
	}

	/***
	 * http post请求，UTF-8编码格式，返回字符串
	 *
	 * @param url
	 *            请求url
	 * @return 返回值字符串
	 */
	public static String post(String url) {
		return post(url, null);
	}

	/***
	 * http post请求，UTF-8编码格式，返回字符串
	 *
	 * @param url
	 *            请求url
	 * @param params
	 *            请求参数
	 * @return 返回值字符串
	 */
	public static String post(String url, Map<String, Object> params) {
		return post(url, params, null, null);
	}

	/**
	 * http post请求，返回字符串
	 *
	 * @param url
	 *            请求url
	 * @param params
	 *            请求参数
	 * @param encoding
	 *            编码格式
	 * @param header
	 *            请求头
	 * @return 返回值字符串
	 */
	public static String post(String url, Map<String, Object> params, String encoding, Map<String, String> header) {
		// 如果编码没有传入则使用默认utf-8
		String ec = getEncoding(encoding);
		HttpPost httpPost = new HttpPost(url);
		UrlEncodedFormEntity entity = getUrlEncodedFormEntity(params, ec);
		if (null != entity) {
			httpPost.setEntity(entity);
		}
		return request(httpPost, ec, header);
	}

	/**
	 * http post一个请求
	 *
	 * @param url
	 *            请求url
	 * @param params
	 *            请求参数
	 * @param encoding
	 *            编码
	 * @param header
	 *            http 除了contentType的其他header
	 * @return 返回字符串
	 */
	public static String post(String url, String params, String encoding, Map<String, String> header) {
		String ec = getEncoding(encoding);
		HttpPost httpPost = new HttpPost(url);
		StringEntity entity = new StringEntity(params, Charset.forName(ec));
		entity.setContentEncoding(encoding);
		if (null != header && header.size() > 0) {
			for (Map.Entry<String, String> entry : header.entrySet()) {
				if ("Content-type".equalsIgnoreCase(entry.getKey())) {
					entity.setContentType(entry.getValue());
					break;
				}
			}
		}
		httpPost.setEntity(entity);
		return request(httpPost, ec, header);
	}

	/**
	 * http get请求，返回byte数组
	 *
	 * @param url
	 *            请求url
	 * @return 返回byte数组
	 */
	public static byte[] getByteArray(String url) {
		return getByteArray(url, null, DEFAULT_ENCODING);
	}

	/**
	 * http get请求，返回byte数组
	 *
	 * @param url
	 *            请求url
	 * @param params
	 *            请求参数
	 * @return 返回byte数组
	 */
	public static byte[] getByteArray(String url, Map<String, Object> params, String encoding) {
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpGet httpGet = new HttpGet(getUrl(url, params, encoding));
			try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
				if (response.getStatusLine().getStatusCode() != 200) {
					throw new Exception("http get response status line " + response.getStatusLine());
				}
				HttpEntity entity = response.getEntity();
				byte[] result = EntityUtils.toByteArray(entity);
				EntityUtils.consume(entity);
				return result;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}