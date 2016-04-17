package com.cxfly.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;

public class TestHosts {
	static final Object emptyObj = new Object();

	public static void main(String[] args) throws Exception {
		doParse();
	}

	static void doParse() throws Exception {

		long start = System.currentTimeMillis();
		Map<String, Object> ipMap = new TreeMap<String, Object>();
		Map<String, Object> domainMap = new LinkedHashMap<String, Object>(10000);

		List<String> readLines = FileUtils.readLines(new File("f:/hosts"),
				"UTF-8");
		List<String[]> hosts = new ArrayList<String[]>();
		for (String line : readLines) {
			line = StringUtils.trimToNull(line);
			if (line == null || line.indexOf("#") != -1)
				continue;
			StringTokenizer st = new StringTokenizer(line);
			boolean flag = true;
			String ip = "";
			while (st.hasMoreTokens()) {
				String nextToken = st.nextToken();
				// System.out.println(nextToken);
				if (flag) {
					ip = nextToken;
					ipMap.put(nextToken, emptyObj);
					flag = false;
				} else {
					if (!domainMap.containsKey(nextToken)) {
						domainMap.put(nextToken, ip);
					}
					hosts.add(new String[] { ip, nextToken });
				}
			}
			// line.split("regex");
			// System.out.println(line);
		}
		/*
		 * Set<String> ipSet = ipMap.keySet();
		 * System.out.println("==========--IP--: "
		 * +ipSet.size()+"==============");
		 * 
		 * for (String ip : ipSet) { System.out.println(ip); }
		 * 
		 * Set<String> domainSet = domainMap.keySet();
		 * System.out.println("===========--HOSTS--: "
		 * +domainSet.size()+"=============");
		 * 
		 * 
		 * for (String ip : domainSet) { System.out.println(ip); }
		 */

		// FileUtils.writeLines(new File("f:/hosts2"), "UTF-8", readLines);
		// saveFile(domainMap);

		start = System.currentTimeMillis();
		batchHosts2(hosts);
		System.out.println("costTime: " + (System.currentTimeMillis() - start));
		// batchGoogleHosts(ipMap.keySet());

	}

	protected static void saveFile(Map<String, Object> domainMap)
			throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriterWithEncoding(
				new File("f:/hosts4"), "UTF-8"));
		for (String domain : domainMap.keySet()) {
			bw.write("74.125.129.122 ");
			bw.write(domain);
			bw.write("\n");
		}

		bw.close();
	}

	public static void batchHosts(Collection<String[]> parmas) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(
				DataSourceManager.getDataSource());
		String sql = "insert into `t_hosts` (`gmt_created`, `domain`, `ip`) values (? , ? , ?)";

		int[][] rows = jdbcTemplate.batchUpdate(sql, parmas, 50,
				new ParameterizedPreparedStatementSetter<String[]>() {
					@Override
					public void setValues(PreparedStatement ps, String[] parmas)
							throws SQLException {
						ps.setTimestamp(1,
								new Timestamp(System.currentTimeMillis()));
						ps.setString(2, parmas[1]);
						ps.setString(3, parmas[0]);
					}
				});
		System.out.println(rows);
	}

	public static void batchHosts2(Collection<String[]> parmas)
			throws Exception {
		String sql = "insert into `t_hosts` (`gmt_created`, `domain`, `ip`) values (? , ? , ?)";
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DataSourceManager.getConnectionLocal();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql);

			int i = 0;
			for (String[] item : parmas) {
				pstmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
				pstmt.setString(2, item[1]);
				pstmt.setString(3, item[0]);
				pstmt.addBatch();
				if (++i % 1000 == 0) {
					pstmt.executeBatch();
					conn.commit();
				}
			}
			pstmt.executeBatch();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			;
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
					;
				}
			}
		}
	}

	public static void batchGoogleHosts(Collection<String> ipAddrs) {
		String sql = "insert into `t_goo_hosts` (`gmt_created`, `ip`) values (?, ?)";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(
				DataSourceManager.getDataSource());
		int[][] rows = jdbcTemplate.batchUpdate(sql, ipAddrs, 500,
				new ParameterizedPreparedStatementSetter<String>() {
					@Override
					public void setValues(PreparedStatement ps, String ip)
							throws SQLException {
						ps.setTimestamp(1,
								new Timestamp(System.currentTimeMillis()));
						ps.setString(2, ip);
					}
				});
		System.out.println(rows);
	}

}
