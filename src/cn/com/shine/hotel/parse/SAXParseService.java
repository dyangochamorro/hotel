package cn.com.shine.hotel.parse;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import cn.com.shine.hotel.bean.ProgramBean;


public class SAXParseService extends DefaultHandler {

	String current = null;
	List<ProgramBean> list = null;
	ProgramBean d = null;

	@Override
	public void startDocument() throws SAXException {
		list = new ArrayList<ProgramBean>();
	}
	@Override
	public void endDocument() throws SAXException {

		super.endDocument();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if ("channel".equals(localName)) {
			d = new ProgramBean();
		} else if ("channel_name".equals(localName)) {
			current = localName;
		}else if ("channel_frequee".equals(localName)) {
			current = localName;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if ("channel".equals(localName)) {
			list.add(d);
		} else {
			current = null;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String s = new String(ch, start, length);
		if ("channel_name".equals(current)) {
			d.setChannel_name(s);
		}else if ("channel_frequee".equals(current)) {
		d.setChannel_frequee(s);
		}
	}

	public List<ProgramBean> getList() {
		return list;
	}

}
