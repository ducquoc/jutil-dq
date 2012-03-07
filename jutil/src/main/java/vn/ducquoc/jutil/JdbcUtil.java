package vn.ducquoc.jutil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Helper class for JDBC operations.
 * 
 * @author ducquoc
 * @see org.apache.commons.dbutils.QueryRunner
 * @see org.skife.jdbi.v2.Handle
 */
public class JdbcUtil {

  public static Connection getDbConnection(String driver, String url, String username, String password) {
    Connection conn = null;
    try {
      Class.forName(driver);
      conn = DriverManager.getConnection(url, username, password);
    }
    catch (ClassNotFoundException ex) {
      throw new RuntimeException(ex.getMessage(), ex);
    }
    catch (SQLException ex) {
      throw new RuntimeException(ex.getMessage(), ex);
    }
    return conn;
  }

  public static void releaseDbConnection(Connection conn, Statement stmt, ResultSet rset) {
    try {
      if (rset != null) {
        rset.close();
      }
      if (stmt != null) {
        stmt.close();
      }
      if (conn != null) {
        conn.close();
      }
    }
    catch (SQLException ex) {
      throw new RuntimeException(ex.getMessage(), ex);
    }
  }

  public static int executeUpdate(Connection conn, String preparedQuery, Object... params) {
    int affectedRows = 0;
    PreparedStatement stmt = null;
    try {
      stmt = conn.prepareStatement(preparedQuery);
      fillStatement(stmt, params);
      affectedRows = stmt.executeUpdate();
    }
    catch (SQLException ex) {
      releaseDbConnection(conn, stmt, null);
      throw new RuntimeException(ex.getMessage(), ex);
    }
    return affectedRows;
  }

  public static ResultSet executeQuery(Connection conn, String preparedQuery, Object... params) {
    ResultSet result = null;
    PreparedStatement stmt = null;
    try {
      stmt = conn.prepareStatement(preparedQuery);
      fillStatement(stmt, params);
      result = stmt.executeQuery();
    }
    catch (SQLException ex) {
      releaseDbConnection(conn, stmt, null);
      throw new RuntimeException(ex.getMessage(), ex);
    }
    return result;
  }

  public static void fillStatement(PreparedStatement stmt, Object... params) throws SQLException {
    if (params != null) {
      ResultSetMetaData metadata = stmt.getMetaData();
      for (int i = 0; i < params.length; i++) {
        if (params[i] != null) {
          stmt.setObject(i + 1, params[i]);
        }
        else {
          try {
            stmt.setNull(i + 1, metadata.getColumnType(i + 1));
          } catch (NullPointerException ex) { // Oracle bug workaround
//            stmt.setNull(i + 1, java.sql.Types.VARCHAR);
            stmt.setObject(i + 1, null);
          }
        }
      }
    }
  }

}
