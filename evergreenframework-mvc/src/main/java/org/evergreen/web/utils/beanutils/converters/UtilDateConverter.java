package org.evergreen.web.utils.beanutils.converters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.evergreen.web.utils.beanutils.ConversionException;

/**
 * wangl
 * 用于转换java.util.Date类型
 */
public class UtilDateConverter {

	private static SimpleDateFormat df = new SimpleDateFormat();
	private static Set<String> patterns = new HashSet<String>();
	static {
		patterns.add("yyyy-MM-dd");
		patterns.add("yyyy-MM-dd hh:mm");
		patterns.add("yyyy-MM-dd hh:mm:ss");
		patterns.add("yyyy-MM-dd hh:mm:ss.SSS");
		patterns.add("yyyy/MM/dd");
		patterns.add("yyyy/MM/dd hh:mm");
		patterns.add("yyyy/MM/dd hh:mm:ss");
		patterns.add("yyyy/MM/dd hh:mm:ss.SSS");
		patterns.add("yyyy.MM.dd");
		patterns.add("yyyy.MM.dd hh:mm");
		patterns.add("yyyy.MM.dd hh:mm:ss");
		patterns.add("yyyy.MM.dd hh:mm:ss.SSS");
	}

	static Object convert(String value) {
		// 如果为空，返回
		if (value == null)			
			return null;
		Object dateObj = null;
		Iterator it = patterns.iterator();
		while (it.hasNext()) {
			try {
				String pattern = (String) it.next();
				df.applyPattern(pattern);
				dateObj = df.parse((String) value);
				break;
			} catch (ParseException ex) {
				continue;
			}
		}
		if(dateObj == null) 
			throw new ConversionException("String can not convert to java.util.Date");
		return dateObj;
			
	}
	
	public static void main(String[] args) {
		System.out.println(UtilDateConverter.convert("dsadsa"));
	}
}
