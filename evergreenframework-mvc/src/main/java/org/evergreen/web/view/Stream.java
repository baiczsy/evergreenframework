package org.evergreen.web.view;

import org.apache.commons.io.IOUtils;
import org.evergreen.web.ViewResult;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by wangl on 2016/7/26.
 */
public class Stream extends ViewResult {

    private InputStream inputStream;

    private OutputStream outputStream;

    public Stream(){
    }

    public Stream(InputStream inputStream, OutputStream outputStream){
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void setContentType(String contentType){
        getResponse().setContentType(contentType);
    }

    public void setContentLength(int length){
        getResponse().setContentLength(length);
    }

    public void addHeader(String name, String value){
        getResponse().addHeader(name, value);
    }

    public void addDataHeader(String name, long value){
        getResponse().addDateHeader(name, value);
    }

    public void addIntHeader(String name, int value){
        getResponse().addIntHeader(name, value);
    }

    public void setHeader(String name, String value){
        getResponse().setHeader(name, value);
    }

    public void setDataHeader(String name, long value){
        getResponse().setDateHeader(name, value);
    }

    public void setIntHeader(String name, int value){
        getResponse().setIntHeader(name, value);
    }

    @Override
    protected void execute() throws IOException {
        IOUtils.copy(inputStream, outputStream);
        inputStream.close();
        outputStream.close();
    }
}
