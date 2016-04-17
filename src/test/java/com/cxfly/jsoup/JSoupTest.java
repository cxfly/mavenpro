package com.cxfly.jsoup;

import java.io.File;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

public class JSoupTest {

	@Test
	public void testRestoreContent() throws Exception {
		Document parse = Jsoup.parse(new File(
				"F:/Prjoect/Git/mavenpro/src/test/java/com/cxfly/jsoup/NewFile2.html"), "UTF-8");
		parse.outputSettings().prettyPrint(false);
		Element body = parse.body();
		System.out.println(body);
		System.out.println("=======================================");
		Elements contentsTag = body.getElementsByAttributeValue("contentType", "4");
		for (Element content : contentsTag) {
			Elements children = content.children();
			if (children!= null && children.size()==1) {
				content.replaceWith(children.get(0));
			}
		}
		System.out.println(body.html());
	}
	
	@Test
	public void testBuildContent() throws Exception {
		Document parse = Jsoup.parse(new File(
				"F:/Prjoect/Git/mavenpro/src/test/java/com/cxfly/jsoup/NewFile.html"), "UTF-8");
		parse.outputSettings().prettyPrint(false);
		Element body = parse.body();
		System.out.println(body);
		System.out.println("=======================================");
		Elements elementsByTag = body.getElementsByTag("a");
		for (Element element : elementsByTag) {
			Element aTagColone = element.clone();
			Element content = parse.createElement("content");
			content.attr("type", "-1");
			content.attr("contentType", "4");
			content.attr("url", element.attr("href"));
			content.appendChild(aTagColone);
			element.replaceWith(content);
		}
		System.out.println(body.html().replace("contenttype", "contentType"));
	}
	
	
}