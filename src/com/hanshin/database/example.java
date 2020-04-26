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
		rs.close(); // ������ �������� executeQuery �Ҵ� ����
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
			Class.forName(jdbc_driver).getConstructor().newInstance(); // ����̹� �ε�
			Connection con = DriverManager.getConnection(jdbc_url, "root", ""); // ����̹� ����
			PreparedStatement st = con.prepareStatement(jdbc_url); // ����̹� ��ü ����
			
			String DropAddressbookTableSql = "DROP TABLE `test`.`addressbook`";
			st.executeUpdate(DropAddressbookTableSql); //addressbook ���̺� ����
			st.executeUpdate(CREATE_TABLE_SQL); //�ش� ��ü�� ������ ����
			
			PreparedStatement pst = con.prepareStatement("INSERT INTO test.addressbook VALUES (?, ?, ?, ?, ?)");
			String[][] str = {
					{"������","010-1111-1111","email1","address1"},
					{"���ٶ�","010-2222-2222","email2","address2"},
					{"�ٶ�","010-3333-3333","email3","address3"},
					{"�󸶹�","010-4444-4444","email4","address4"},
					{"���ٻ�","010-5555-5555","email5","address5"}
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
			
			pst.close(); // prepareStatement ���� ������ ���� ������ ���� �Ҵ� ����
			st.close(); // ���������� ���� �Ҵ� ����
			con.close(); // jdbc driver �Ҵ� ����		
		} catch (Exception e) {
			e.printStackTrace(); //����ó��
		} 
	}

}
