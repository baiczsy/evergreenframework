package org.evergreen.web.utils;

import java.io.File;

public class MultipartFileUtil {

	public static String parseFileName(String header) {
		String fileName = header.substring(header.lastIndexOf("=") + 2,
				header.length() - 1);
		// 某些浏览器会带上路径,因此去掉路径只留文件名
		return fileName.substring(fileName.lastIndexOf(File.separator) + 1,
				fileName.length());
	}
}
