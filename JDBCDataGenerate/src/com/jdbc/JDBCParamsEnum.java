package com.jdbc;

/**
 * JDBC访问的数据参数
 * 
 * @Author:xxm
 * @version $id:JDBCParamsEnum.java,v0.1  2016年10月29日 上午9:11:23 xxm Exp$
 */
public enum JDBCParamsEnum {

	DRIVER("oracle.jdbc.driver.OracleDriver"),
	URL("jdbc:oracle:thin:@192.168.0.73:1521:orcl"),
	ACCOUNT("huaweipoc"),
	PWD("12345678");
	
	public String value;

	private JDBCParamsEnum(String value) {
		this.value = value;
	}
}
