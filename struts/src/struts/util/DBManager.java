package struts.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBManager {
	private static String driverName = "org.postgresql.Driver";
	private static String url = "jdbc:postgresql://localhost:5432/TEST";
	private static String user = "postgres";
	private static String pass = "sinnyuu18551";

	public static Connection getConnection() {

		Connection con = null;
		try {
			Class.forName(driverName);
			con = DriverManager.getConnection(url, user, pass);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}


}
