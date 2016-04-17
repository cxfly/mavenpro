package com.cxfly.test;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

public class TestSCFHosts {
	static final Object emptyObj = new Object();

	static Map<String, String> hostAlias = new HashMap<String, String>();

	static {
		hostAlias.put("sy-uic", "UIC");
		hostAlias.put("sy-ci", "CIC");
		hostAlias.put("sy-oic", "OIC");
		hostAlias.put("sy-pi", "PIC");
		hostAlias.put("sy-jf", "JF");
		hostAlias.put("sy-xjf", "XJF");
		hostAlias.put("sy-culverin", "XF");
		hostAlias.put("sy-mh", "MH");

		hostAlias.put("yf-uic", "YF-UIC");
		hostAlias.put("yf-ci", "YF-CIC");
		hostAlias.put("yf-oic", "YF-OIC");
		hostAlias.put("yf-pi", "YF-PIC");
		hostAlias.put("yf-jf", "YF-JF");
		hostAlias.put("yf-xjf", "YF-XJF");
		hostAlias.put("yf-culverin", "YF-XF");
		hostAlias.put("yf-mh", "YF-MH");
	}

	public static void main(String[] args) throws Exception {
		// String[] split = "sy-ci-01.189read.com".replace(".189read.com",
		// "").split("-");
		// System.out.println(split.length);
		doParse();
	}

	static void doParse() throws IOException {

		Map<String, String> domainMap = new TreeMap<String, String>(
				new Comparator<String>() {
					@Override
					public int compare(String o1, String o2) {
						if (!o1.contains(".189read.com")
								|| !o2.contains(".189read.com")) {
							System.out.println("sttttttt1-->" + o1 + "," + o2);
							return -1;
						}

						o1 = o1.replace(".189read.com", "");
						o2 = o2.replace(".189read.com", "");

						if ((!o1.startsWith("sy") && !o1.startsWith("yf"))
								|| (!o2.startsWith("sy") && !o2
										.startsWith("yf"))) {
							System.out.println("sttttttt2-->" + o1 + "," + o2);
							return -1;
						}

						int idx1 = o1.lastIndexOf("-");
						int idx2 = o2.lastIndexOf("-");

						String p1 = o1.substring(0, idx1);
						String l1 = o1.substring(idx1 + 1);

						String p2 = o2.substring(0, idx2);
						String l2 = o2.substring(idx2 + 1);

						int compareTo = p1.compareTo(p2);
						if (compareTo == 0) {
							return (Integer.parseInt(l1) - Integer.parseInt(l2));
						}
						return compareTo;

					}
				});

		List<String> readLines = FileUtils.readLines(new File(
				"f:/tmp/tmp/scfhost.txt"), "UTF-8");

		for (String line : readLines) {
			line = StringUtils.trimToNull(line);
			if (line == null || line.indexOf(",") == -1) {
				System.out.println("err1");
				continue;
			}
			String[] split = line.split(",");
			if (split.length != 2) {
				System.out.println("err2");
				continue;
			}

			String value = null;
			if (split[1].endsWith(".189read.com")) {
				String o1 = split[1].replace(".189read.com", "");
				int idx1 = o1.lastIndexOf("-");
				String l1 = o1.substring(idx1 + 1);

				int no = Integer.parseInt(l1);
				String alias = getAlias(split[1]);
				if (alias != null) {
					value = split[0] + "=" + alias + no;
				} else {
					System.out.println("xxxxxxxxxxx1-->" + line);
				}
			} else {
				System.out.println("xxxxxxxxxxx2-->" + line);
			}
			if (value != null) {
				domainMap.put(split[1], value);
			}
		}

		// for (Entry<String, String> entry : domainMap.entrySet()) {
		// System.out.printf("%-22s %s\n", entry.getValue(), entry.getKey());
		// }

		for (String string : domainMap.values()) {
			System.out.println(string);
		}

	}

	static String getAlias(String hostname) {
		Set<Entry<String, String>> entrySet = hostAlias.entrySet();
		for (Entry<String, String> entry : entrySet) {
			if (hostname.startsWith(entry.getKey())) {
				return entry.getValue();
			}
		}
		return null;
	}
}
