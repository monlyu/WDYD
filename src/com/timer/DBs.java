package com.timer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBs {

	private Connection connection = null;

	private DBs() {

	}

	public static DBs instance() {
		DBs db = new DBs();
		try {
			db.initCfg();
		} catch (Exception e) {
			throw new RuntimeException("初始化数据库连接错误");
		}
		return db;
	}

	private void initCfg() throws Exception {
		String dbpath = allconfig().get("sqlitepath");
		Class.forName("org.sqlite.JDBC");
		connection = DriverManager.getConnection("jdbc:sqlite:" + dbpath);
	}

	public void close() {
		try {
			if (connection != null)
				connection.close();
		} catch (SQLException e) {
			System.err.println(e);
		}
	}

	public List<TimeItem> queryMonth(String ym) throws Exception {
		Statement statement = connection.createStatement();
		statement.setQueryTimeout(30);
		ResultSet rs = statement.executeQuery(String.format("select * from worktime where substr(start,1,7) = '%s' order by start desc", ym));
		List<TimeItem> items = new ArrayList<TimeItem>();
		while (rs.next()) {
			TimeItem itme = new TimeItem();
			itme.setUuid(rs.getInt("uuid"));
			itme.setStart(rs.getString("start"));
			itme.setEnd(rs.getString("end"));
			itme.setProject(rs.getString("project"));
			itme.setTask(rs.getString("task"));
			itme.setHour(rs.getString("hour"));
			items.add(itme);
		}

		rs.close();
		statement.close();

		return items;
	}

	public void inserOrUpdate(TimeItem item) throws Exception {
		Statement statement = connection.createStatement();
		statement.setQueryTimeout(30); // set timeout to 30 sec.

		if (item.getUuid() == 0) {
			String sql = "insert into worktime (start,end,project,task,hour) values('%s','%s','%s','%s','%s')";
			statement.executeUpdate(String.format(sql, item.start, item.end, item.project, item.task, item.hour));
		} else {
			String sql = "update worktime set start='%s',end='%s',project='%s',task='%s',hour='%s' where uuid=%s";
			statement.executeUpdate(String.format(sql, item.start, item.end, item.project, item.task, item.hour, item.getUuid()));
		}
		statement.close();
	}

	public static Map<String, String> allconfig() {
		Map<String, String> allcfg = new HashMap<String, String>();
		try {
			URL url = DBs.class.getResource("/");
			String path = url.getPath();
			File cfg = new File(path, "config.cfg");
			String line;
			InputStream fis = new FileInputStream(cfg);
			InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
			BufferedReader br = new BufferedReader(isr);
			while ((line = br.readLine()) != null) {
				String vals = line.trim();
				String[] lvs = vals.split("=");
				if (lvs.length == 2) {
					allcfg.put(lvs[0], lvs[1]);
				} else {
					allcfg.put(lvs[0], "");
				}
			}
			br.close();
			isr.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return allcfg;

	}

	public static void main(String[] args) throws Exception {
		DBs db = DBs.instance();
		List<TimeItem> itmes = db.queryMonth("2015-12");
		TimeItem item = itmes.get(0);
		item.setProject("test update2");
		db.inserOrUpdate(item);
		System.out.println();
	}

	public void delete(String uuid) throws Exception {
		Statement statement = connection.createStatement();
		statement.setQueryTimeout(30); // set timeout to 30 sec.
		statement.executeUpdate("delete from worktime where uuid=" + uuid);
	}

}
