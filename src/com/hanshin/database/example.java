package com.hanshin.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.Arrays;
class sqlClass {
	public static void getData(PreparedStatement st) throws SQLException{
		String getAddressbookDataSql = "SELECT * FROM `test`.`addressbook`";
		ResultSet rs = st.executeQuery(getAddressbookDataSql);
		while(rs.next()) {
			int id = rs.getInt("id");
			String name = rs.getString("name");
			String tel = rs.getString("tel");
			String email = rs.getString("email");
			String address = rs.getString("address");
			System.out.printf("id: %d / name: %s / tel: %s / email: %s / title: %s\n", id, name, tel, email, address);
		}
		System.out.println();
		rs.close(); // 데이터 가져오는 executeQuery 할당 해제
	}
	
}

public class example {
	private static final String CREATE_TABLE_SQL= ""
			+ "CREATE TABLE test.addressbook ("
            + "id INT NOT NULL,"
            + "name VARCHAR(45) NOT NULL,"
            + "tel VARCHAR(45) NOT NULL,"
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
			
			String DropAddressbookTableSql = "DROP TABLE `test`.`addressbook`";
			st.executeUpdate(DropAddressbookTableSql); //addressbook 테이블 삭제
			st.executeUpdate(CREATE_TABLE_SQL); //해당 객체로 쿼리문 전송
			
			PreparedStatement pst = con.prepareStatement("INSERT INTO test.addressbook VALUES (?, ?, ?, ?, ?)");
			String[][] str = {
					{"가나다","010-1111-1111","email1","address1"},
					{"나다라","010-2222-2222","email2","address2"},
					{"다라마","010-3333-3333","email3","address3"},
					{"라마바","010-4444-4444","email4","address4"},
					{"마바사","010-5555-5555","email5","address5"}
			};
			
			int i = 0;
			for(String[]a :str) {
				pst.setInt(1,i);
				pst.setString(2,a[0]);
				pst.setString(3,a[1]);
				pst.setString(4,a[2]);
				pst.setString(5,a[3]);
				i++;
				System.out.println(Arrays.toString(a));
				pst.addBatch();
				pst.clearParameters();
			}
			pst.executeBatch();
			sqlClass.getData(st);
			
			st.executeUpdate("SET SQL_SAFE_UPDATES=0;");
			st.executeUpdate("UPDATE addressbook SET email = CONCAT(email, '@naver.com');");
			st.executeUpdate("SET SQL_SAFE_UPDATES=1;");
			sqlClass.getData(st);
			
			st.executeUpdate("DELETE FROM addressbook ORDER BY id DESC LiMIT 2");
			sqlClass.getData(st);
			
			pst.close(); // prepareStatement 재사용 가능한 동적 쿼리문 실행 할당 해제
			st.close(); // 정적쿼리문 실행 할당 해제
			con.close(); // jdbc driver 할당 해제		
		} catch (Exception e) {
			e.printStackTrace(); //예외처리
		} 
	}

}
