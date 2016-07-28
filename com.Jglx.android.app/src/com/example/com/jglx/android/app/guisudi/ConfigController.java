package com.example.com.jglx.android.app.guisudi;


import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.text.TextUtils;

/**
 * �����ļ������� ���ڻ�ȡ�����ļ�
 * 
 * @author wwj
 * 
 */
public class ConfigController {
	private static ConfigController instance;

	public static ConfigController getInstance() {
		if (instance == null) {
			synchronized (ConfigController.class) {
				if (instance == null) {
					instance = new ConfigController();
				}
			}
		}
		return instance;
	}

	public ConfigController() {
		InputStream input = getClass().getResourceAsStream(
				"/res/raw/config.xml");
		try {
			config = new ConfigParser(input).getResult();
		} catch (Exception e) {
			config = new HashMap<String, String>();
			e.printStackTrace();
		}
	}

	private HashMap<String, String> config;

	public String get(String key) {
		return config.get(key);
	}

	/**
	 * ��ȡ/res/raw/config.xml�е�����
	 * 
	 * @param key
	 *            ������
	 * @param failedValue
	 *            ��ȡ����ʧ��ʱ��ȡֵ��û�����ã��������ò�Ϊboolean��
	 */
	public boolean get(String key, boolean failedValue) {
		String stringValue = config.get(key);
		if (TextUtils.isEmpty(stringValue)
				|| !("true".equalsIgnoreCase(stringValue) || "false"
						.equalsIgnoreCase(stringValue))) {
			return failedValue;
		} else {
			return Boolean.parseBoolean(stringValue);
		}
	}

	class ConfigParser extends DefaultHandler {
		private StringBuffer accumulator;
		private HashMap<String, String> result;

		public ConfigParser(InputStream input) throws Exception {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			parser.parse(input, this);
		}

		public void characters(char buffer[], int start, int length) {
			accumulator.append(buffer, start, length);
		}

		public void endDocument() throws SAXException {
			super.endDocument();
		}

		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			super.endElement(uri, localName, qName);

			if (!"config".equals(localName)) { // "config" �Ǹ�Ԫ��
				String key = localName;
				String value = accumulator.toString();
				result.put(key, value);
			}
		}

		public void startDocument() throws SAXException {
			super.startDocument();
			accumulator = new StringBuffer();
			result = new HashMap<String, String>();
		}

		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			accumulator.setLength(0);
		}

		public HashMap<String, String> getResult() {
			return result;
		}
	}
}
