package com.mitchell.claims.DbConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {

	
	static Connection con;
	public static Connection getConnection() throws ClassNotFoundException, SQLException{
		
		Class.forName( "com.mysql.jdbc.Driver" );
        String url = "jdbc:mysql://localhost:3306/test";
        String username = "root";
        String password = "root";

        
		
		con = DriverManager.getConnection(url,username,password);
		if(con!=null)
			System.out.println("Connection is established");
		else
			System.out.println("Connection is not established");

		return con;
	}
	public static void release(Connection con) throws SQLException {
		// TODO Auto-generated method stub
		if(con!=null)
			con.close();
	}
}
