package org.evergreen.web.view;

import org.evergreen.web.ViewResult;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class TextPlain extends ViewResult {

	private final static String TEXT_PLAIN = "text/plain;charset=utf-8";

	private String url;
	
	public TextPlain(){
	}

	public TextPlain(String url) {
		this.url = url;
	}
	
	public void setUrl(String url){
		this.url = url;
	}

	@SuppressWarnings("resource")
	protected void execute() throws IOException {
		if (url != null) {
			getResponse().setContentType(TEXT_PLAIN);
			StringBuilder builder = new StringBuilder(getRequest()
					.getServletContext().getRealPath("/"));
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(builder.append(url.trim()).toString()),
					"utf-8"));
			PrintWriter pw = getResponse().getWriter();
			while (br.ready()) {
				pw.println(br.readLine());
				pw.flush();
			}
		}
	}

}
