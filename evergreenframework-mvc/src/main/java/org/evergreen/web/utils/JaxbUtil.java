package org.evergreen.web.utils;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class JaxbUtil {

	public static String marshal(Object bean) {
		StringWriter sw = new StringWriter();
		try {
			// 要在JavaBean中使用@XmlRootElement注解指定XML根元素
			// 否则Marshal或者UnMarshal都会失败
			JAXBContext jaxbContext = JAXBContext.newInstance(bean.getClass());
			// 将JavaBean编排为XML字符串
			Marshaller marshaller = jaxbContext.createMarshaller();
			// 自动格式化
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			// 设置编码格式
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "utf-8");
			// 转换成xml字符串
			marshaller.marshal(bean, sw);
			return sw.toString();
		} catch (JAXBException e) {
			e.printStackTrace();
			return null;
		}

	}
}
