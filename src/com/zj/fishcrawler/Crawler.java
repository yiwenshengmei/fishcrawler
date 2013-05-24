package com.zj.fishcrawler;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zj.fishcrawler.Node;

public class Crawler {
	
	static {
		// 设定log输出目录
		System.setProperty("zhaojie.log4j.path", System.getProperty("user.dir"));
	}
	
	private final static  Logger logger = LoggerFactory.getLogger(Crawler.class);
	private final static int NODE_TYPE_CLASS = 10;
	private final static int NODE_TYPE_FINAL = 11;
	private final static String PROPERTIES_FILE_NAME = "config.properties";
	private final static String INPUT_FILE_NAME = "specieslist.txt";
	
	private String connectionURL;
	private String user;
	private String pwd;
	private String inputFileName;
	
	public String getInputFileName() {
		return inputFileName;
	}

	public void setInputFileName(String inputFileName) {
		this.inputFileName = inputFileName;
	}

	public String getConnectionURL() {
		return connectionURL;
	}

	public void setConnectionURL(String connectionURL) {
		this.connectionURL = connectionURL;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public static void main(String[] args) throws Exception {
		
		String userdir = System.getProperty("user.dir");
		
		Crawler c = new Crawler();
		Properties p = new Properties();
		try {
			p.load(new InputStreamReader(new FileInputStream(userdir + "\\" + PROPERTIES_FILE_NAME), "GBK"));
		} catch (UnsupportedEncodingException e) {
			logger.error("系统不支持GBK编码，无法解析配置文件");
			return;
		} catch (FileNotFoundException e) {
			logger.error("没有找到配置文件：config.properties");
			return;
		} catch (IOException e) {
			logger.debug("发生IO错误：" + e.getMessage());
			return;
		}
		c.setConnectionURL(p.getProperty("connectionURL", null));
		c.setUser(p.getProperty("user", null));
		c.setPwd(p.getProperty("password", null));
		c.setInputFileName(p.getProperty("input_file", INPUT_FILE_NAME));
		
		c.start();
	}
	
	private String getUUID() {
		return UUID.randomUUID().toString();
	}
	
	public void start() throws Exception {
		if (StringUtils.isBlank(connectionURL)) {
			logger.error("ConnectionURL can not be empty.");
			return;
		}
		if (StringUtils.isBlank(user)) {
			logger.error("User can not be empty.");
			return;
		}
		if (StringUtils.isBlank(pwd)) {
			logger.error("Password can not be empty");
			return;
		}
		
		BufferedReader reader = null;
		String line = null;
		String[] array;
		String lastuuid;
		String _lastuuid = null;
		Map<String, Node> nodemap = new HashMap<String, Node>();

		try {
			reader = new BufferedReader(new FileReader(inputFileName));
			
			while ((line = reader.readLine()) != null) {
				array = line.split("\",\"");
				if (array.length != 23) {
					logger.warn("无法解析的行: " + line);
					continue;
				}
				lastuuid = "";
				// Add Kingdom
				if ( !nodemap.containsKey(getKingdom(array)) ) {
					nodemap.put(getKingdom(array), new Node(
							_lastuuid = getUUID(), 
							getKingdomInChinese(array), 
							getKingdom(array), 
							"-1", NODE_TYPE_CLASS));
				}
				else {
					_lastuuid = nodemap.get(getKingdom(array)).getId();
				}
				lastuuid = _lastuuid;
				
				// Add Phylum
				if ( !nodemap.containsKey(getPhylum(array)) ) {
					nodemap.put(getPhylum(array), new Node(
							_lastuuid = getUUID(), 
							getPhylumInChinese(array), 
							getPhylum(array), 
							lastuuid, NODE_TYPE_CLASS));
				}
				else {
					_lastuuid = nodemap.get(getPhylum(array)).getId();
				}
				lastuuid = _lastuuid;
	
				// Add Class
				if ( !nodemap.containsKey(getClass(array)) ) {
					nodemap.put(getClass(array), new Node(
							_lastuuid = getUUID(), 
							getClassInChinese(array), 
							getClass(array), 
							lastuuid, NODE_TYPE_CLASS));
				}
				else {
					_lastuuid = nodemap.get(getClass(array)).getId();
				}
				lastuuid = _lastuuid;
				
				// Add SubClass
				if ( !nodemap.containsKey(getSubClass(array)) ) {
					nodemap.put(getSubClass(array), new Node(
							_lastuuid = getUUID(), 
							getSubclassInChinese(array), 
							getSubClass(array), 
							lastuuid, NODE_TYPE_CLASS));
				}
				else {
					_lastuuid = nodemap.get(getSubClass(array)).getId();
				}
				lastuuid = _lastuuid;
				
				// Add Order
				if ( !nodemap.containsKey(getOrder(array)) ) {
					nodemap.put(getOrder(array), new Node(
							_lastuuid = getUUID(), 
							getOrderInChinese(array), 
							getOrder(array), 
							lastuuid, NODE_TYPE_CLASS));
				}
				else {
					_lastuuid = nodemap.get(getOrder(array)).getId();
				}
				lastuuid = _lastuuid;
				
				// Add SubOrder
				if ( !nodemap.containsKey(getSubOrder(array)) ) {
					nodemap.put(getSubOrder(array), new Node(
							_lastuuid = getUUID(), 
							getSuborderInChinese(array), 
							getSubOrder(array), 
							lastuuid, NODE_TYPE_CLASS));
				}
				else {
					_lastuuid = nodemap.get(getSubOrder(array)).getId();
				}
				lastuuid = _lastuuid;
				
				// Add Family
				if ( !nodemap.containsKey(getFamily(array)) ) {
					nodemap.put(getFamily(array), new Node(
							_lastuuid = getUUID(), 
							getFamilyInChinese(array), 
							getFamily(array), 
							lastuuid, NODE_TYPE_CLASS));
				}
				else {
					_lastuuid = nodemap.get(getFamily(array)).getId();
				}
				lastuuid = _lastuuid;
				
				// Add Fish
				if ( !nodemap.containsKey(getScientificName(array)) ) {
					nodemap.put(getScientificName(array), new Node(
							_lastuuid = getUUID(), 
							getChineseName(array), 
							getScientificName(array), 
							lastuuid, NODE_TYPE_FINAL));
				}
				else {
					logger.warn("出现重复的鱼: " + getChineseName(array));
					continue;
				}
			}
		}
		catch (FileNotFoundException ex) {
			logger.error("没有找到输入文件");
			return;
		}
		finally {
			IOUtils.closeQuietly(reader);
		}
		
		saveFishSpeciallist(nodemap);
	}
	
	private void saveFishSpeciallist(Map<String, Node> nodemap) throws Exception {
		Connection conn = null;
		PreparedStatement prest = null;
		logger.info("共需要插入" + nodemap.size() + "条数据");
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(connectionURL, user, pwd);
			conn.setAutoCommit(false);
			String sql = "INSERT INTO `retrieval`.`t_node`(`ID`,`CNAME`,`ENAME`,`PARENT_ID`, `NODE_TYPE`) VALUES(?, ?, ?, ?, ?)";
			prest = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			logger.info("开始拼接SQL");
			for (Node nd : nodemap.values()) {
				prest.setString(1, nd.getId());
				prest.setString(2, nd.getCname());
				prest.setString(3, nd.getEname());
				prest.setString(4, nd.getParentId());
				prest.setInt(5, nd.getNodeType());
				prest.addBatch();
			}
			prest.executeBatch();
			logger.info("开始执行插入");
			long t1 = System.currentTimeMillis();
			conn.commit();
			float t = System.currentTimeMillis() - t1;
			logger.info(String.format("插入成功, 花费: %.2f秒",  t / 100f));
		}
		catch (Exception ex) {
			logger.error("写入数据库时发生错误：" + ex.getMessage());
		}
		finally {
			if (prest != null)
				prest.close();
			if (conn != null) 
				conn.close();
		}
		
	}
	
	private String getKingdom(String[] array) {
		return array[3];
	}
	
	private String getPhylum(String[] array) {
		return array[4];
	}
	
	private String getClass(String[] array) {
		return array[5];
	}
	
	private String getSubClass(String[] array) {
		return array[6];
	}
	
	private String getOrder(String[] array) {
		return array[7];
	}
	
	private String getSubOrder(String[] array) {
		return array[8];
	}
	
	private String getFamily(String[] array) {
		return array[9];
	}
	
	private String getKingdomInChinese(String[] array) {
		return array[13];
	}
	
	private String getPhylumInChinese(String[] array) {
		return array[14];
	}
	
	private String getClassInChinese(String[] array) {
		return array[15];
	}
	
	private String getSubclassInChinese(String[] array) {
		return array[16];
	}
	
	private String getOrderInChinese(String[] array) {
		return array[17];
	}
	
	private String getSuborderInChinese(String[] array) {
		return array[18];
	}
	
	private String getFamilyInChinese(String[] array) {
		return array[19];
	}
	
	private String getScientificName(String[] array) {
		return array[11];
	}
	
	private String getChineseName(String[] array) {
		return array[20];
	}
}
