package org.evergreen.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

public abstract class MultipartFile implements Serializable {

	private static final long serialVersionUID = -558551897462030467L;

	protected String fileName;

	/**
	 * 设置文件名
	 * @param fileName
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
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
	 * 获取文件名
	 * @return
	 */
	public abstract String getFileName();

	/**
	 * 获取输入流
	 */
	public abstract InputStream getInputStream() throws IOException;
	
	/**
	 * 上传
	 * @throws IOException
	 */
	public abstract void upload(String uploadPath) throws IOException;

}
