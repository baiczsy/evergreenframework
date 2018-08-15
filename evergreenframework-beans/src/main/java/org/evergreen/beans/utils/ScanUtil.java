package org.evergreen.beans.utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * class扫描工具
 * Created by wangl on 2017/7/6.
 */
public class ScanUtil {

	private static final Set<String> classNames = new HashSet<String>();

	/**
	 * 获取指定包下以及子包中所有的类
	 *
	 * @param packageName 包名
	 * @return 所有的完整类名
	 */
	public static Set<String> scan(String packageName) {
		if(packageName == null){
			throw new RuntimeException("The path can not be null.");
		}
		String packagePath = packageName.replace(".", "/");
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		try {
			Enumeration<URL> urls = loader.getResources(packagePath);
			while(urls.hasMoreElements()){
				URL url= urls.nextElement();
				if("file".equals(url.getProtocol())){
					scanFromDir(url.getPath(), packageName);
				}
				if("jar".equals(url.getProtocol())){
					JarURLConnection connection = (JarURLConnection)url.openConnection();
					scanFromJar(connection.getJarFile());
				}
			}
		} catch (IOException e) {
			throw new RuntimeException("Resolve path error.", e);
		}
		return classNames;
	}

	/**
	 * 从项目文件获取某包下所有类
	 *
	 * @param filePath 文件目录
	 * @param packageName 包名
	 */
	private static void scanFromDir(String filePath, String packageName) throws UnsupportedEncodingException {
		filePath = URLDecoder.decode(filePath, "utf-8");
		packageName = URLDecoder.decode(packageName, "utf-8");
		File[] files = new File(filePath).listFiles();
		if(files != null){
			packageName = packageName + ".";
			for (File childFile : files) {
				if (childFile.isDirectory()) {
					scanFromDir(childFile.getPath(), packageName + childFile.getName());
				} else {
					String fileName = childFile.getName();
					if (fileName.endsWith(".class")) {
						if(packageName.charAt(0) == '.'){
							packageName = packageName.substring(1, packageName.length());
						}
						String className = packageName + fileName.replace(".class", "");
						classNames.add(className);
					}
				}
			}
		}
	}

	/**
	 * 扫描jar文件
	 * @param jarFile
	 */
	private static void scanFromJar(JarFile jarFile) {
		Enumeration<JarEntry> files = jarFile.entries();
		while (files.hasMoreElements()) {
			JarEntry entry = files.nextElement();
			if (entry.getName().endsWith(".class")){
				String className = entry.getName().replace("/", ".").replace(".class", "");
				classNames.add(className);
			}
		}
	}

}
