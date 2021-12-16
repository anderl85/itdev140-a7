import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;


public class Database {
	private String url = "jdbc:sqlite:C:\\sqlite\\db\\itdev140.db";
	private Connection conn;

	//constructors
	public Database(){

	}

	public Database(String path){
		url = path;
	}

	//methods
	private void establishConn(){
		try{
			conn = DriverManager.getConnection(url);
		} catch (SQLException e){
			System.out.println("This is establishConn\n" + e.getMessage());
		}
	}

	private void closeConn(){
		try {
			conn.close();
		} catch (SQLException e){
			//sorta expect this to run
			System.out.println(e.getMessage());
		}
	}

	public String[] queryById(String id) throws SQLException{
		String[] retVal;
		String format = "SELECT name, hire_date FROM employee WHERE id = '%s'";
		String query = String.format(format, id.trim());
		ResultSet rs;

		try {
			establishConn();

			//create statement, run query
			Statement stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			
			//attempt to populate retval
			retVal = new String[2];
			retVal[0] = rs.getString("name");
			retVal[1] = rs.getString("hire_date");

			closeConn();
		} catch (SQLException e) {
			//attempt to close conn just in case
			closeConn();
			//change retval size
			retVal = new String[1];
			//report
			throw e;
		}

		return retVal;
	}	

	public int updateById(String id, String name, String date) throws SQLException{
		int retval = 999;
		String format = "UPDATE employee SET name = '%s', date = '%s' WHERE id = '%s'";
		String sqlStatement = String.format(format, name, date, id);
	
		try{
			establishConn();

			//create statement, exec sqlstatement
			Statement stmt = conn.createStatement();
			retval = stmt.executeUpdate(sqlStatement);
			
			closeConn();
		} catch (SQLException e) {
			//attempt close conn just in case
			closeConn();
			throw e;
		}

		return retval;
	}

	public int insert(String id, String name, String date) throws SQLException{
		int retval = 999;
		String format = "INSERT INTO employee (id, name, hire_date) VALUES ('%s', '%s', '%s')";
		String sqlStatement = String.format(format, id, name, date);

		try{
			establishConn();

			//create statement, exec sqlstatement
			Statement stmt = conn.createStatement();
			retval = stmt.executeUpdate(sqlStatement);

			closeConn();
		} catch (SQLException e){
			//attempt close conn just in case
			closeConn();
			throw e;
		}

		return retval;
	}

	public int deleteById(String id) throws SQLException{
		int retval = 999;
		String format = "DELETE FROM employee WHERE id = '%s'";
		String sqlStatement = String.format(format, id);

		try{
			establishConn();

			//create statement, exec sqlstatement
			Statement stmt = conn.createStatement();
			retval = stmt.executeUpdate(sqlStatement);

			closeConn();
		} catch (SQLException e){
			//attempt close conn just in case
			closeConn();
			throw e;
		}
		
		return retval;
	}

	public String getLastId(){
		String retval = "";
		String query = "SELECT id FROM employee ORDER BY id DESC LIMIT 1";
		ResultSet rs;

		try{
			establishConn();

			//create statement, exec query
			Statement stmt = conn.createStatement();
			rs = stmt.executeQuery(query);

			//attempt to populate retval
			retval = rs.getString("id");

			closeConn();
		} catch (SQLException e){
			//attempt close conn just in case
			closeConn();
			//report
			System.out.println("this is getLastID\n" + e.getMessage());
		}

		return retval;
	}


}
