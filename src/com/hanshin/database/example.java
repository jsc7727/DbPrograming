package com.hanshin.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class example {
	private static final String CREATE_TABLE_SQL="CREATE TABLE test.addressbook ("
            + "id INT NOT NULL AUTO_INCREMENT ,"
            + "name VARCHAR(45) NOT NULL,"
            + "email VARCHAR(60) NOT NULL,"
            + "address VARCHAR(60) NOT NULL,"
            + "PRIMARY KEY (id))";

	public static void main(String[] args) {
		String jdbc_driver = "com.mysql.cj.jdbc.Driver";
		String jdbc_url = "jdbc:mysql://localhost:3307/test?serverTimezone=UTC";
		
		try {
			Class.forName(jdbc_driver).getConstructor().newInstance(); // 드라이버 로드
			Connection con = DriverManager.getConnection(jdbc_url, "root", ""); // 드라이버 연결
			PreparedStatement st = con.prepareStatement(jdbc_url); // 드라이버 객체 생성
			st.executeUpdate(CREATE_TABLE_SQL); //해당 객체로 쿼리문 전송
//			PreparedStatement st = con.prepareStatement("INSERT INTO test.members VALUES (?, ?, ?, ?, ?)");
//			String sql = "SELECT * FROM test.members where dept = 'Computer'";
//			st.setInt(1,3);
//			st.setString(2,"test11");
//			st.setString(3, "oh");
//			st.setString(4, "oh");
//			st.setString(5, "oh");
//			st.executeUpdate();
//			ResultSet rs = st.executeQuery(sql);
//			while(rs.next()) {
//				String username = rs.getString("username");
//				String birth = rs.getString("birth");
//				String dept = rs.getString("dept");
//				String email = rs.getString("email");
//				System.out.printf("title: %s\n", email);
//			}
//			
			//rs.close();
			st.close();
			con.close();			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

}
