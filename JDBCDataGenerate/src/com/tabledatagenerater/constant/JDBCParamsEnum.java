package com.tabledatagenerater.constant;

/**
 * JDBC访问的数据参数
 * 
 * @Author:xxm
 * @version $id:JDBCParamsEnum.java,v0.1 2016年10月29日 上午9:11:23 xxm Exp$
 */
public enum JDBCParamsEnum {

	ORACLE_DRIVER_URL("oracle.jdbc.driver.OracleDriver","jdbc:oracle:thin:@192.168.0.73:1521:orcl"),	
	MSSQL_DRIVER_URL("com.microsoft.sqlserver.jdbc.SQLServerDriver","jdbc:sqlserver://192.168.0.112:1433;DatabaseName=ipems_dev_if"),
	ACCOUNT_PWD("huaweipoc", "12345678");

	public String valueBefore;
	public String valueAfter;
	
	private JDBCParamsEnum(String valueBefore, String valueAfter) {
		this.valueBefore = valueBefore;
		this.valueAfter = valueAfter;
	}
}
