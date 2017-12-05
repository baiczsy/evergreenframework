package org.evergreen.web.params.converter.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Part;

import org.evergreen.web.exception.UploadFileException;
import org.evergreen.web.params.ParamInfo;
import org.evergreen.web.params.converter.ParamsConvertHandler;
import org.evergreen.web.MultipartFile;
import org.evergreen.web.upload.ServletPartFile;
import org.evergreen.web.utils.MultipartFileUtil;

public class MultipartFileConvertHandler extends ParamsConvertHandler {

	private final static String CONTENT_DISPOSITION = "content-disposition";

	public Object execute(ParamInfo paramInfo) {
		try {
			if (paramInfo.getParamType().equals(MultipartFile.class)) {
                return buildMultipartFile(paramInfo.getParamName());
            } else if (paramInfo.getParamType().isArray()
                    && paramInfo.getParamType().getComponentType()
                            .equals(MultipartFile.class)) {
                return buildMultipartFiles();
            }
		} catch (IOException e) {
			throw new UploadFileException("Upload file fail.", e);
		} catch (ServletException e) {
			throw new UploadFileException("Upload file fail.", e);
		}
		return null;
	}

	// 单个附件
	private MultipartFile buildMultipartFile(String paramName) throws IOException, ServletException {
		Part part = getRequest().getPart(paramName);
		MultipartFile multipartFile = null;
		if (part != null) {
			String fileName = MultipartFileUtil.parseFileName(part.getHeader(CONTENT_DISPOSITION));
			multipartFile = createPartFile(fileName, part);
		}
		return multipartFile;
	}

	// 多个附件
	private MultipartFile[] buildMultipartFiles() throws IOException,
			ServletException {
		List<Part> parts = (List<Part>) getRequest().getParts();
		List<MultipartFile> newParts = new ArrayList<MultipartFile>();
		for (Part part : parts) {
			if (part.getContentType() != null) {
				String fileName = MultipartFileUtil.parseFileName(part
						.getHeader(CONTENT_DISPOSITION));
				if (!"".equals(fileName)) {
					newParts.add(createPartFile(fileName, part));
				}
			}
		}
		return newParts.toArray(new ServletPartFile[0]);
	}


	// 创建ServletPartFile
	private MultipartFile createPartFile(String fileName, Part part) {
		MultipartFile filePart = new ServletPartFile(part);
		filePart.setFileName(fileName);
		return filePart;
	}

}
