package com.example.demo;

public class Sanitizing {

	public static String convert(String str) {
		if(str == null) {
			return str;
		}

		str = str.replaceAll("<", "&lt;");
		str = str.replaceAll(">", "&gt;");
		str = str.replaceAll("\"", "&quot;");
		str = str.replaceAll("'", "&#39;");
		str = str.replaceAll("&", "&amp;");

		return str;
	}

}
