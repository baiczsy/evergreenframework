package org.evergreen.web.utils;

import java.util.List;

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
	public static boolean requestMethodMatch(List<String> requestMethods,
			String requestMethod) {
		return requestMethods.size() == 0
				|| requestMethods.indexOf(requestMethod) != -1 ? true : false;
	}

}
