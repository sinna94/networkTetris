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
			System.out.println("����̹� ���� ����");
			con = DriverManager.getConnection(url, id, password);
			System.out.println("�����ͺ��̽� ���� ����");
		} catch (ClassNotFoundException e){
			System.out.println("����̹��� ã�� �� �����ϴ�.");
		} catch (SQLException e){
			System.out.println("���ῡ �����߽��ϴ�.");
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