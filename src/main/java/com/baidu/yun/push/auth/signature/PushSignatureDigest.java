package com.baidu.yun.push.auth.signature;

import java.util.Map;

import com.baidu.yun.core.utility.MessageDigestUtility;

public class PushSignatureDigest {
	
	public static final String URL_KEY = "url";
	public static final String HTTP_METHOD_KEY = "http_method";
	public static final String CLIENT_SECRET_KEY = "client_secret";
	
	public String digest(String accessKey, String secretKey, Map<String, String> params) {
		return null;
	}
	
	public String digest(String method, String url, String clientSecret, Map<String, String> params) {
		StringBuilder sb = new StringBuilder();
		sb.append(method).append(url);
		for (Map.Entry<String, String> entry : params.entrySet()) {
			String key = entry.getKey();
			if ( URL_KEY.equals(key) || HTTP_METHOD_KEY.equals(key) ) {
				continue;
			} else {
				sb.append(entry.getKey()).append('=').append(entry.getValue());
			}
		}
		sb.append(clientSecret);
		String encodeString = MessageDigestUtility.urlEncode(sb.toString());
		if ( encodeString != null ) {
			encodeString = encodeString.replaceAll("\\*", "%2A");
		}
 		return MessageDigestUtility.toMD5Hex(encodeString);
	}
	
	
	public boolean checkParams(Map<String, String> params) {
		return false;
	}
	//我再更新下试试
	//创建新的分支，测试一下
	//只在本地库，不提交远程但是要合并到master
}
