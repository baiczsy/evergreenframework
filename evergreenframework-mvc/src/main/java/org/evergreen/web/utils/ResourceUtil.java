package org.evergreen.web.utils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class ResourceUtil {

	public static Map<String, String> getResource(String baseName,Locale locale) {
		Map<String, String> resourceMap = new HashMap<String, String>();
		ResourceBundle rb = ResourceBundle.getBundle(baseName, locale);
		for (String key : rb.keySet())
			resourceMap.put(key, rb.getString(key));
		return resourceMap;
	}
}
