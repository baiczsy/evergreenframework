package org.evergreen.web.utils;

import java.util.Set;

public class MatcherUtil {

	private final static PathMatcher matcher = new AntPathMatcher();

	/**
	 * 匹配请求的url
	 * 
	 * @param destUrl
	 * @param orginUrl
	 * @return
	 */
	public static boolean pathMatch(String destUrl, String orginUrl) {
		return matcher.match(destUrl, orginUrl);
	}

	/**
	 * 匹配请求方法的有效性
	 * 
	 * @return
	 */
	public static boolean requestMethodMatch(Set<String> requestMethods,
			String requestMethod) {
		if(requestMethods.size() == 0){
			return true;
		}
		for (String rm : requestMethods) {
			if(rm.equals(requestMethod)){
				return true;
			}
		}
		return false;
	}

}
