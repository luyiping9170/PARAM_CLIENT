package com.dt.param.util;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import android.util.Log;

import com.dt.bean.Account;
import com.dt.bean.Multipart;
import com.dt.bean.Pack;

public class PackParser {

	public static Pack parseXML(String data) throws UnsupportedEncodingException, DocumentException {
		// parse the xml data with dom4j
		Log.d("PackParser:", "data received:" + data);
		SAXReader saxReader = new SAXReader();
		Document doc = saxReader.read(new ByteArrayInputStream(data
				.getBytes("utf-8")));
		// get the data type and package id
		Element pkg = doc.getRootElement();
		String pkgtype = pkg.attributeValue("type");
		long pid = Long.parseLong(pkg.attributeValue("pid"));
		int device = Integer.parseInt(pkg.attributeValue("device"));
		// get account information
		Element account = pkg.element("account");
		String aname = account.elementText("name");
		String imei = account.elementText("imei");
		String token = account.elementText("token");
		Account accobj = new Account(aname, imei, token);
		// parse multipart data
		List<Multipart> multi = new ArrayList<Multipart>();
		for (Iterator<Element> i = pkg.elementIterator("multipart"); i
				.hasNext();) {
			Element multipart = i.next();
			int type = Integer.parseInt(multipart.elementText("type"));
			String encoding = multipart.elementText("encoding");
			String mdata = multipart.elementText("data");
			Date time = null;
			if (multipart.element("time") != null) {
				time = new Date(Long.parseLong(multipart.elementText("time")));
			}
			multi.add(new Multipart(type, encoding, mdata, time));
		}
		return new Pack(pkgtype, pid, device, accobj, multi);
	}

}
