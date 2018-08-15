package org.evergreen.web.upload;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.Part;

import org.evergreen.web.MultipartFile;

public class ServletPartFile extends MultipartFile{

	private static final long serialVersionUID = 9202366354209980173L;
	
	private Part part;
	
	public ServletPartFile(Part part){
		this.part = part;
	}

    @Override
	public long getSize(){
		return part.getSize();
	}

    @Override
	public String getContentType(){
		return part.getContentType();
	}

	@Override
	public String getFileName() {
		return part.getSubmittedFileName();
	}

	@Override
    public InputStream getInputStream() throws IOException {
        return part.getInputStream();
    }

    @Override
	public void upload(File file) throws IOException {
		part.write(file.getPath());
	}

}
