package cn.com.shine.hotel.parse;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import cn.com.shine.hotel.bean.ADbean;


public class ADSAXParseService extends DefaultHandler {

	String current = null;
	List<ADbean> list = null;
	ADbean d = null;

	@Override
	public void startDocument() throws SAXException {
		list = new ArrayList<ADbean>();
	}
	@Override
	public void endDocument() throws SAXException {

		super.endDocument();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if ("ad".equals(localName)) {
			d = new ADbean();
		} else if ("ad_name".equals(localName)) {
			current = localName;
		}else if ("ad_url".equals(localName)) {
			current = localName;
		}else if ("ad_time".equals(localName)) {
			current = localName;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if ("ad".equals(localName)) {
			list.add(d);
		} else {
			current = null;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String s = new String(ch, start, length);
		if ("ad_name".equals(current)) {
			d.setName(s);
		}else if ("ad_url".equals(current)) {
			d.setAddress(s);
		}else if ("ad_time".equals(current)) {
			d.setTime(s);
		}
	}

	public List<ADbean> getList() {
		return list;
	}

}
