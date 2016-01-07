package clients;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.sql.RowSet;
import javax.sql.rowset.CachedRowSet;

import basic.Client;

/**
 * the client used to execute a {@link requests.CommandRequest} which belongs to
 * oracle's SQL and also can be single instated to handle oracle's SQL
 * 
 * @author zhangyutao
 *
 */
public class SQLClient implements Client {
	public SQLClient() {
	}

	// important variable in class to reuse.
	private Connection conn;
	private RowSet tempRS;

	public Connection connectToDB(String url, String dbuser, String dbpassword) {

		Connection myconn = null;

		try {
			myconn = DriverManager.getConnection(url, dbuser, dbpassword);
			myconn.setAutoCommit(false);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (myconn == null) {
			System.out.println("Failed to connect DB " + url);
		} else {

			System.out.println("Success to connect DB " + url);

		}
		this.conn = myconn;
		return myconn;
	}

	/**
	 * to set schema
	 * 
	 * @param conn
	 * @param schema
	 * @return
	 * @author Linus
	 */
	public boolean alterSession(String schema) {
		boolean bool = false;

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			stmt.execute("alter session set current_schema =\"" + schema + "\"");
			System.out.println("alter session set current_schema =\"" + schema + "\"");
			bool = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bool;
	}

	/**
	 * A intenal method for closing the connection of this class's instance.
	 * 
	 * @author Linus
	 */
	public void disconnect() {
		if (this.conn != null) {
			try {
				this.conn.rollback();
				this.conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Disconnect from DB");
	}

	/**
	 * execute sql statements and return the Row set.
	 * 
	 * @param conn
	 * @param strStmt
	 * @return
	 * @author Linus
	 */
	@SuppressWarnings("restriction")
	public RowSet executeSQL(String strStmt) {
		Statement stmt = null;
		ResultSet rs = null;
		System.out.println("Execute SQL:" + strStmt);
		CachedRowSet rowset = null;
		try {

			rowset = new com.sun.rowset.CachedRowSetImpl();
			stmt = conn.createStatement();
			rs = stmt.getResultSet();
			// Use regular expression to delete the last semicolon(;)
			Pattern p = Pattern.compile(";$");
			Matcher m = p.matcher(strStmt);
			String str = m.replaceAll("");
			boolean hasResultSet = stmt.execute(str);
			if (hasResultSet) {
				rs = stmt.getResultSet();
				rowset.populate(rs);

			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("SQL execution is over.");
		return rowset;
	}

	/**
	 * count the row number of the RowSet you provided.
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 * @author Linus
	 */
	public int rowCount(RowSet rs) {
		int rowNr = 0;
		try {
			rs.beforeFirst();
			while (rs.next()) {
				rowNr = rowNr + 1;
			}
			rs.beforeFirst();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new IllegalArgumentException("Error when proccess row set.");
		}

		return rowNr;
	}

	/**
	 * count the column number of the RowSet you provided.
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 * @author Linus
	 */
	public int colCount(RowSet rs) {
		Integer columnCount = null;

		ResultSetMetaData rsmd;
		try {
			rsmd = rs.getMetaData();
			columnCount = rsmd.getColumnCount();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new IllegalArgumentException("Error when proccess row set.");
		}
		return columnCount;
	}

	/**
	 * Get Column Type of a specified column name from the row set you provide
	 * and return a int which is referred to java.sql.Types.
	 * 
	 * @param rs
	 *            - the Rowset from you previous executed SQL statments.
	 * @param ColName
	 *            - the column name which type you want to get.
	 * @return if nothing found, it will return null.
	 * @throws SQLException
	 * @author Linus
	 */
	public int getColumnType(RowSet rs, String ColName) {
		Integer res = null;
		;

		if (rs == null) {
			throw new IllegalArgumentException("RowSet you provided was null.");
		} else {
			try {
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnCount = rsmd.getColumnCount();
				int columnIndext = 0;
				if (columnCount >= 1) {
					a: for (int i = 1; i <= columnCount; i++) {
						if (rsmd.getColumnName(i).toLowerCase().equals(ColName.toLowerCase())) {
							columnIndext = i;
							break a;
						}

					}
					if (columnIndext == 0) {
						throw new IllegalArgumentException(
								"Column Name \"" + ColName + "\" was not found in your resutl set.");
					} else {

						res = rsmd.getColumnType(columnIndext);

					}
				} else {
					throw new IllegalArgumentException("No column was not found in your row set.");
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new IllegalArgumentException("Error when proccess row set.");
			}
		}
		return res;
	}

	/**
	 * Get all values of a specified column name from the Row set you provide
	 * and return the all the value as String in an ArrayList type.
	 * 
	 * @param rs
	 *            - the Rowset from you previous executed SQL statments.
	 * @param ColName
	 *            - the column name in which the string values you want to get.
	 * @return if nothing found, it will return a empty ArrayList<String>().
	 * @throws SQLException
	 * @author Linus
	 */
	public ArrayList<String> getAllValueByColName(RowSet rs, String ColName) {
		ArrayList<String> res = new ArrayList<String>();

		if (rs == null) {
			throw new IllegalArgumentException("RowSet you provided was null.");
		} else {
			try {
				rs.beforeFirst();
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnCount = rsmd.getColumnCount();
				int columnIndext = 0;
				if (columnCount >= 1) {
					a: for (int i = 1; i <= columnCount; i++) {
						if (rsmd.getColumnName(i).toLowerCase().equals(ColName.toLowerCase())) {
							columnIndext = i;
							break a;
						}

					}

					if (columnIndext == 0) {
						throw new IllegalArgumentException(
								"Column Name \"" + ColName + "\" was not found in your resutl set.");
					} else {
						while (rs.next()) {
							res.add(rs.getString(columnIndext));
						}
					}
				} else {
					throw new IllegalArgumentException("No column was not found in your resutl set.");
				}

				rs.beforeFirst();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new IllegalArgumentException("Error when proccess Row set.");
			}
		}
		return res;
	}

	/**
	 * Get all column name of the Row set you provide and return the all the
	 * name in an ArrayList type.
	 * 
	 * @param rs
	 *            - the Rowset from you previous executed SQL statments.
	 * @return if nothing found, it will return a empty ArrayList<String>().
	 * @throws SQLException
	 * @author Linus
	 */
	public ArrayList<String> getAllColumnNames(RowSet rs) {
		ArrayList<String> res = new ArrayList<String>();

		if (rs == null) {
			throw new IllegalArgumentException("RowSet you provided was null.");
		} else {
			try {

				ResultSetMetaData rsmd = rs.getMetaData();
				int columnCount = rsmd.getColumnCount();
				if (columnCount >= 1) {
					for (int i = 1; i <= columnCount; i++) {
						res.add(rsmd.getColumnName(i));
					}
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new IllegalArgumentException("Error when proccess Row set.");
			}
		}

		return res;
	}

	/**
	 * Execute the SQL statement and return a Arraylist in your self defined
	 * content type.
	 * 
	 * @param <T>
	 * @param strStmt
	 * @param columnname
	 *            - which column you want to check. if give null, it would not
	 *            check the column name but requires there is only one column in
	 *            RowSet.
	 * @param sqlType
	 *            - use the indicate what type of value should be in DB. About
	 *            the exact value, please refer to java.sql.Types.
	 * @return -please use T to designate what type of Arraylist you need. like
	 *         "ArrayList<String>".
	 * @author Linus
	 */
	@SuppressWarnings("unchecked")
	private <T> ArrayList<T> getAllValueByColNameAndType(RowSet rs, String columnname, int sqlType) {
		ArrayList<T> res = new ArrayList<T>();
		String strValue = "";
		Integer coltype = null;
		if (rowCount(rs) >= 1 && colCount(rs) >= 1) {
			if (columnname != null && (!columnname.equals(""))) {
				coltype = getColumnType(rs, columnname);
			} else {
				if (colCount(rs) != 1) {
					throw new IllegalArgumentException("RowSet should only have one column.");
				} else {
					columnname = getAllColumnNames(rs).get(0);
					coltype = getColumnType(rs, columnname);
				}
			}
			if (coltype != null && coltype == sqlType) {
				ArrayList<String> allvalue = getAllValueByColName(rs, columnname);
				for (int i = 0; i < allvalue.size(); i++) {
					strValue = allvalue.get(i);
					// this switch should be extended to support all possible
					// data
					// type of DB.
					switch (coltype) {
					case java.sql.Types.INTEGER:
						res.add((T) Integer.valueOf(strValue));
						break;
					case java.sql.Types.VARCHAR:
						res.add((T) strValue);
						break;
					default:
						res.add((T) strValue);
					}

				}

			} else {
				throw new IllegalArgumentException(
						"Column \"" + columnname + "\" of DB is not a sql  type \"" + sqlType + "\".");
			}

		} else {
			throw new IllegalArgumentException("RowSet does not have row or column.");
		}

		return res;
	}

	/**
	 * Execute the single SQL statement and return a single String value result.
	 * 
	 * @param rs
	 * @return value or null if query returns no rows SQL that returns a single
	 *         row and a single integer value. If the SQL returns multiple rows,
	 *         multiple columns, an update count, etc. an exception will be
	 *         thrown.
	 * @author Linus
	 */
	public String singeSQLGetStringValue(RowSet rs) {
		String res = null;
		if (rowCount(rs) == 1 && colCount(rs) == 1) {
			ArrayList<String> reslist = getAllValueByColNameAndType(rs, "", java.sql.Types.VARCHAR);
			res = reslist.get(0);
		} else {
			throw new IllegalArgumentException("RowSet has more than one row or column.");
		}

		return res;
	}

	/**
	 * Execute the single SQL statement and return a single int value result.
	 * 
	 * @param rs
	 * @return value or null if query returns no rows SQL that returns a single
	 *         row and a single integer value. If the SQL returns multiple rows,
	 *         multiple columns, an update count, etc. an exception will be
	 *         thrown.
	 * @author Linus
	 */
	public int singeSQLGetINTValue(RowSet rs) {
		Integer res = null;
		if (rowCount(rs) == 1 && colCount(rs) == 1) {
			ArrayList<Integer> reslist = getAllValueByColNameAndType(rs, "", java.sql.Types.INTEGER);
			res = reslist.get(0);
		} else {
			throw new IllegalArgumentException("RowSet has more than one row or column.");
		}

		return res;
	}

	/**
	 * Transform RowSet to HashMap type.
	 * 
	 * @param rs
	 * @return - a map contains all data of a rowset. The key of the map is a
	 *         Integer[] type value which indicates the original position where
	 *         the data is stored in the Rowset. The Integer{0,n} of the map
	 *         stores column names."HashMap<Integer{1,2}, mydata>" means
	 *         "mydata" is linked to key the Integer{1,2} which means "mydata"
	 *         was in 1st row and 2end col of the rowset.
	 * @throws SQLException
	 * @author Linus
	 */
	public HashMap<Integer[], Object> transformToHashMap(RowSet rs) throws SQLException {
		HashMap<Integer[], Object> map = new HashMap<Integer[], Object>();
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();

		int rowIndex = 0;
		if (columnCount > 0) {
			for (int h = 1; h <= columnCount; h++) {
				Integer[] position = new Integer[] { rowIndex, h };
				map.put(position, rsmd.getColumnName(h));
			}

			while (rs.next()) {
				rowIndex = rowIndex + 1;
				for (int i = 1; i <= columnCount; i++) {
					int columnIndex = i;
					Integer[] position = new Integer[] { rowIndex, columnIndex };
					map.put(position, rs.getObject(columnIndex));

				}
			}
		}

		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public HashMap<Integer[], Object> getResponse() throws SQLException {
		return transformToHashMap(tempRS);
	}

	@Override
	public void close() throws Exception {
		disconnect();
	}

	@Override
	public void execute(Object content) throws Exception {
		tempRS = executeSQL((String) content);

	}

}
