package com.tabledatagenerater.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * JDBCThread
 * 
 * 采用JDBC方式将数据批量插入表中
 * 
 * @Author:xxm
 * @version $id:AbstractJDBCThread.java,v0.1 2016年10月29日 上午9:09:30 xxm Exp$
 */
public abstract class AbstractJDBCThread extends Thread {

	/** driver 驱动 */
	public String	driver;
	/** url 数据库链接 */
	public String	url;
	/** user 帐号 */
	public String	user;
	/** password 密码 */
	public String	password;

	public AbstractJDBCThread(String driver, String url, String user, String password) {
		this.driver = driver;
		this.url = url;
		this.user = user;
		this.password = password;
	}

	@Override
	public void run() {

		try {
			// connection
			Class.forName(driver);
			Connection connection = DriverManager.getConnection(url, user, password);
			connection.setAutoCommit(false);

			// execute sqls
			Statement statement = connection.createStatement();
			List<String> sqls = generateSQLList();
			if (null != sqls && 0 < sqls.size()) {
				for (String sql : sqls) {
					statement.addBatch(sql);
				}
				statement.executeBatch();
			}

			// commit
			connection.commit();

			// close
			statement.close();
			connection.close();

			// sleep
			TimeUnit.SECONDS.sleep(10);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取时间
	 * 
	 * 当前时间的天数减去索引值，采用yyyy-MM-dd HH:mm:ss进行时间格式化
	 * 
	 * @param recordIndex
	 * @return
	 * @author: xxm v0.1 2016年10月29日
	 */
	public String getDate(int recordIndex) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, 0 - recordIndex);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return "to_date( '" + format.format(cal.getTime()) + "' , 'yyyy-mm-dd hh24:mi:ss')";
	}

	/**
	 * 构造SQL集合（用于批量执行）
	 * 
	 * 建议SQL采用占位符进行设计
	 * 
	 * @return
	 * @author: xxm v0.1 2016年10月29日
	 */
	public abstract List<String> generateSQLList();

}
