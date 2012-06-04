package vn.ducquoc.jutil;

import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Helper class for JDBC operations, which aims to reduce boiler-plate code for
 * common database interactions as well as <code>SQLException</code>s
 * 
 * @author ducquoc
 * @see org.apache.commons.dbutils.QueryRunner
 * @see org.springframework.jdbc.core.JdbcTemplate
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
      throw new UtilException(ex.getMessage(), ex);
    }
    catch (SQLException ex) {
      throw new UtilException(ex.getMessage(), ex);
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
      throw new UtilException(ex.getMessage(), ex);
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
      throw new UtilException(ex.getMessage(), ex);
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
      throw new UtilException(ex.getMessage(), ex);
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

  public static boolean checkTableExists(Connection conn, String tableName) {
      try {
          DatabaseMetaData meta = conn.getMetaData();
          ResultSet rset = meta.getTables(null, null, null, new String[] { "TABLE" });
          while (rset.next()) {
              if (rset.getString("TABLE_NAME").equalsIgnoreCase(tableName)) {
                  return true;
              }
          }
      } catch (SQLException ex) {
          throw new UtilException(ex.getMessage(), ex);
      }
      return false;
  }

  public static boolean checkViewExists(Connection conn, String tableName) {
      try {
          DatabaseMetaData meta = conn.getMetaData();
          ResultSet rset = meta.getTables(null, null, null, new String[] { "VIEW" });
          while (rset.next()) {
              if (rset.getString("TABLE_NAME").equalsIgnoreCase(tableName)) {
                  return true;
              }
          }
      } catch (SQLException ex) {
          throw new UtilException(ex.getMessage(), ex);
      }
      return false;
  }

  public static boolean checkTableOrViewExists(Connection conn, String tableName) {
      return checkTableExists(conn, tableName) || checkViewExists(conn, tableName);
  }

  public static void printResultSetData(PrintWriter writer, ResultSet rset, String delimiter) {
      try {
          ResultSetMetaData rsetMeta = rset.getMetaData();
          int count = rsetMeta.getColumnCount();

          while (rset.next()) {
              for (int i = 1; i < count; i++) {
                  writer.print(rset.getString(i) + delimiter);
              }
              writer.println(rset.getString(count));
          }
      } catch (SQLException ex) {
          throw new UtilException(ex.getMessage(), ex);
      }
  }

  public static void printResultSetHeader(PrintWriter writer, ResultSet rset, String delimiter) {
      try {
          ResultSetMetaData rsetMeta = rset.getMetaData();
          int count = rsetMeta.getColumnCount();

          for (int i = 1; i < count; i++) {
              writer.print(rsetMeta.getColumnLabel(i) + delimiter);
          }
          writer.println(rsetMeta.getColumnLabel(count));
      } catch (SQLException ex) {
          throw new UtilException(ex.getMessage(), ex);
      }
  }

  public static void printResultSet(PrintWriter writer, ResultSet rset, String delimiter, boolean includingHeader) {
      if (includingHeader == true) {
          printResultSetHeader(writer, rset, delimiter);
      }
      printResultSetData(writer, rset, delimiter);
  }

  public static CallableStatement executeCall(Connection conn, String callableQuery, Object... params) {
      CallableStatement stmt = null;
      try {
        stmt = conn.prepareCall(callableQuery);
        fillStatement(stmt, params);
        stmt.execute();
      }
      catch (SQLException ex) {
        releaseDbConnection(conn, stmt, null);
        throw new UtilException(ex.getMessage(), ex);
      }
      return stmt;
  }

}
