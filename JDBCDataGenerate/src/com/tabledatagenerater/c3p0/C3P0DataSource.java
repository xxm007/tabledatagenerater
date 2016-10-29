package com.tabledatagenerater.c3p0;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * C3P0数据源
 * 
 * @Author:xxm
 * @version $id:C3P0DataSource.java,v0.1 2016年10月29日 上午10:26:20 xxm Exp$
 */
public class C3P0DataSource {

	/** driver 驱动 */
	public String					driver;
	/** url 数据库链接 */
	public String					url;
	/** user 帐号 */
	public String					user;
	/** password 密码 */
	public String					password;
	/** dataSource C3P0数据源 */
	private static ComboPooledDataSource	dataSource;
	/** dsWorkerPool 数据源的工作池*/
	private ExecutorService dsWorkerPool;
	
	public C3P0DataSource(String driver, String url, String user, String password) {

		this.driver = driver;
		this.url = url;
		this.user = user;
		this.password = password;
	}

	/**
	 * 初始化数据源
	 * 
	 * @throws PropertyVetoException
	 * @author: xxm v0.1 2016年10月29日
	 */
	public void open() throws PropertyVetoException {

		// pool size
		int maxPoolSize = 2;
		
		// DataSource init
		dataSource = new ComboPooledDataSource();
		dataSource.setDriverClass(driver);
		dataSource.setJdbcUrl(url);
		dataSource.setUser(user);
		dataSource.setPassword(password);
		dataSource.setInitialPoolSize(2);
		dataSource.setMaxIdleTime(60);
		dataSource.setMaxPoolSize(maxPoolSize);
		dataSource.setIdleConnectionTestPeriod(60);
		dataSource.setForceIgnoreUnresolvedTransactions(false);
		dataSource.setAutoCommitOnClose(false);
		
		// DataSource WorkerPool init
		dsWorkerPool = Executors.newFixedThreadPool(maxPoolSize);
		dsWorkerPool.execute(new ConnectionRunnable());
	}
	
	/**
	 * 关闭数据源
	 * 
	 * @author: xxm v0.1 2016年10月29日
	 */
	public void close() {
		dataSource.close();
		dsWorkerPool.shutdown();
	};

	/**
	 * 批量执行SQL
	 * 
	 * @param sqlLists
	 * @author: xxm v0.1 2016年10月29日
	 */
	public void executeBatch(List<String> sqlLists) {
		// 参数检查
		if (null == sqlLists || sqlLists.isEmpty()){
			return ;
		}
		// 提交任务
		dsWorkerPool.submit(new ConnectionRunnable(sqlLists));
	}
	
	/**
	 * 连接的线程接口
	 * 
	 * @Author:xxm
	 * @version $id:ConnectionRunnable.java,v0.1  2016年10月29日 上午11:37:49 xxm Exp$
	 */
	public class ConnectionRunnable implements Runnable {
		
		/** sqls 待执行的SQL*/
		private List<String> sqls;
		
		/** connectionHolder 数据源连接*/
		private ThreadLocal<Connection>	connectionHolder	= new ThreadLocal<Connection>() {
			@Override
			protected Connection initialValue() {
				Connection conn = null;
				try {
					conn = dataSource.getConnection();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return conn;
			}															
		};
		
		public ConnectionRunnable() {
			super();
		}

		public ConnectionRunnable(List<String> sqlLists) {
			this.sqls = sqlLists;
		}

		@Override
		public void run() {
		
			try {
				// 参数检查
				if (null == sqls || sqls.isEmpty()){
					return ;
				}
				// 批量执行SQL
				executeBachSQL();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}

		/**
		 * 批量执行SQL
		 * 
		 * @throws SQLException
		 * @author: xxm v0.1 2016年10月29日
		 */
		private void executeBachSQL() throws SQLException {
			// get Connection
			Connection connection = connectionHolder.get();

			// get Statement
			Statement statement = connection.createStatement();
			if (null != sqls && 0 < sqls.size()) {
				for (String sql : sqls) {
					statement.addBatch(sql);
				}
				int[] rlts = statement.executeBatch();
				for (int rlt : rlts) {
					System.out.println(rlt);
				}
			}

			// commit
			connection.commit();
			statement.clearBatch();
			// close
			statement.close();
			connection.close();
		}
	}
}
