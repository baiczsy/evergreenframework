package org.evergreen.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

public abstract class MultipartFile implements Serializable {

	private static final long serialVersionUID = -558551897462030467L;

	private String fileName;

	public String getFileName() {
		return fileName;
	}

	public MultipartFile setFileName(String fileName) {
		this.fileName = fileName;
		return this;
	}
	
	/**
	 * 获取文件大小
	 * @return
	 */
	public abstract long getSize();
	
	/**
	 * 获取文件类型
	 * @return
	 */
	public abstract String getContentType();

	/**
	 * 获取输入流
	 */
	public abstract InputStream getInputStream() throws IOException;
	
	/**
	 * 上传
	 * @throws IOException
	 */
	public abstract void upload(File file) throws IOException;

}
