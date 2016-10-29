package com.tabledatagenerater.constant;

/**
 * JDBC访问的数据参数
 * 
 * @Author:xxm
 * @version $id:JDBCParamsEnum.java,v0.1  2016年10月29日 上午9:11:23 xxm Exp$
 */
public enum JDBCParamsEnum {

	DRIVER("oracle.jdbc.driver.OracleDriver"),
	URL("jdbc:oracle:thin:@127.0.0.1:1521:demo"),
	ACCOUNT("demo"),
	PWD("12345678");
	
	public String value;

	private JDBCParamsEnum(String value) {
		this.value = value;
	}
}
