package org.evergreen.web.view;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

public class Attachment extends Stream {
	
	private File file;
	
	public Attachment(){		
	}

	public Attachment(File file) {
		this.file = file;
	}
	
	public void setFile(File file){
		this.file = file;
	}

	protected void execute() throws IOException {
		String fileName = URLEncoder.encode(file.getName(), "UTF-8") + "\"";
		String disposition = "attachment;filename=\"" + fileName;
		setHeader("Content-disposition", disposition);
        setContentType("application/octet-stream");
        setInputStream(new BufferedInputStream(new FileInputStream(file)));
		setOutputStream(new BufferedOutputStream(getResponse().getOutputStream()));
		super.execute();
	}

}
