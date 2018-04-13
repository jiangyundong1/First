package com.wamdy.coder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.wamdy.entitys.TableColumn;

public class DBUtil {

    private static Connection conn=null;
    private static PreparedStatement pstmt=null;
    private static ResultSet rs=null;
    
    public static List<String> getNotesByTableName(Connection connection, String tableName){
    	List<String> list = new ArrayList<String>();
		String sql = "select comments from user_col_comments where table_name = ?";
		conn=connection;
		pstmt=null;
		rs=null;//定义存放查询结果的结果集
		try{
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1,tableName);
			rs=pstmt.executeQuery();//运行查询操作
			while (rs.next()){				
				list.add(rs.getString("comments"));
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			//按顺序进行关闭
			//closeConn();
		}
		
		return list;
	}
    
    public static List<TableColumn> getColAndDataTypeByTableName(Connection connection, String tableName){
    	List<TableColumn> list = new ArrayList<TableColumn>();
    	String sql = "select column_name, data_type from user_tab_columns where table_name = ?";
    	conn=connection;
    	pstmt=null;
    	rs=null;//定义存放查询结果的结果集
    	try{
    		pstmt=conn.prepareStatement(sql);
    		pstmt.setString(1,tableName);
    		rs=pstmt.executeQuery();//运行查询操作
    		TableColumn tableColumn = null;
    		while (rs.next()){				
    			tableColumn = new TableColumn();
    			tableColumn.setColumnName(rs.getString("column_name"));
    			tableColumn.setDataType(rs.getString("data_type"));
    			list.add(tableColumn);
    		}
    	}catch(SQLException e){
    		e.printStackTrace();
    	}finally{
    		//按顺序进行关闭
//    		closeConn();
    	}
    	
    	return list;
    }
    
    //将获得的数据库与java的链接返回（返回的类型为Connection）
    public static Connection getConnection(String url, String user, String password){
    	try {
            //1.加载驱动程序
//            Class.forName("com.mysql.jdbc.Driver"); //mysql
    		Class.forName("oracle.jdbc.driver.OracleDriver");
            //2.获得数据库的连接
            conn=DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    	
        return conn;
    }
    
    public static void closeConn() {
    	if (conn != null) {
    		try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	}
    	if (pstmt != null) {
    		try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	}
    	if (rs != null) {
    		try {
    			rs.close();
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
    	}
    }
}