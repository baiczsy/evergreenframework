package org.evergreen.web.view;

import org.evergreen.web.ViewResult;
import org.evergreen.web.utils.JaxbUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class XmlMarshaller extends ViewResult {
	
	public final static String CONTENT_TYPE = "text/xml;charset=utf-8";

	private Object arg;

	public XmlMarshaller() {
	}

	public XmlMarshaller(Object arg) {
		this.arg = arg;
	}

	public void addObject(Object arg) {
		this.arg = arg;
	}

	@Override
	protected void execute() throws IOException {
		String stringXml = JaxbUtil.marshal(arg);
		write(stringXml);
	}
	
	private void write(String stringXml) throws IOException {
		HttpServletResponse response = getResponse();
		response.setContentType(CONTENT_TYPE);
		response.getWriter().println(stringXml);
	}

}
