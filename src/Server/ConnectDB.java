package Server;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectDB {
	Connection con = null;
	
	public ConnectDB(){
		con = makeConnection();
	}
	
	public Connection makeConnection(){
		String url = "jdbc:mysql://localhost/tetris";
		
		String id = "root";
		String password = "cnddl060708";
		Connection con = null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("드라이버 적재 성공");
			con = DriverManager.getConnection(url, id, password);
			System.out.println("데이터베이스 연결 성공");
		} catch (ClassNotFoundException e){
			System.out.println("드라이버를 찾을 수 없습니다.");
		} catch (SQLException e){
			System.out.println("연결에 실패했습니다.");
		}
		return con;
	}
	
	public ResultSet getQueryResult(String query) throws SQLException
    {
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(query);

        return rs;
    }

    public int noExcuteQuery(String query) throws SQLException
    {
		Statement stmt = con.createStatement();
		int result = stmt.executeUpdate(query);

		return result;
    }
}