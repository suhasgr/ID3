package Main;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Algorithm.Attribute;
import Algorithm.Tuple;
import Algorithm.Tuples;

public class MySQLConnection 
{
	private Statement _stmt;
	private Connection _conn;
	
	public MySQLConnection(String dataBase,String username,String password) 
	{
		//String JDBCDRIVER = "com.mysql.jdbc.Driver";
		String DB_URL = "jdbc:mysql://localhost/"+dataBase;
		
		_conn = null;
		
		try{
		      
		      Class.forName("com.mysql.jdbc.Driver");
		      //System.out.println("Connecting to database...");
		      _conn = DriverManager.getConnection(DB_URL,username,password);

		      //System.out.println("Creating statement...");
		      _stmt = _conn.createStatement();
		}
		catch(SQLException se)
		   {
		      
		      se.printStackTrace();
		   }catch(Exception e){
		      
		      e.printStackTrace();
		   }
	}
	
	
	void deleteTable(String table)
	{
		String query = "DROP TABLE IF EXISTS "+table;
		
		
			try {
					
				 _stmt.executeUpdate(query);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
	}
	
	void createTable(String table, ArrayList<String> attributes)
	{
		
		String query = "Create table "+table+" (" ;
		
		for(int i = 0; i< attributes.size(); ++i)
		{
			query+= attributes.get(i) + " varchar(20) ";
			if(i+1 != attributes.size())
				query+= ",";
		}
		query+=" )";
		
		try {
			
			 _stmt.executeUpdate(query);

			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	void infile(String fileName,String table)
	{
		String query = "load data infile " +"\"" +fileName+ "\""+" into table " +table+" fields terminated by ' ' IGNORE 1 LINES";
		ResultSet rs=null;
		try {
			
			rs = _stmt.executeQuery(query);

			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void fillTuplesAndAttributes(String query, Tuples tuples,ArrayList<String> attributesList,ArrayList<Attribute> attributes)
	{
		ResultSet rs=null;
		
		
		for(int i = 0; i< attributesList.size();++i)
		{
			Attribute a = new Attribute(attributesList.get(i));
			attributes.add(a);
		}
		
		try {
			rs = _stmt.executeQuery(query);
		
			while(rs.next())
			{
				Tuple t = new Tuple(attributesList);
				for(int i = 0; i< attributesList.size();++i)
				{
					t.insert(rs.getString(i+1),attributesList.get(i));
					Attribute a = attributes.get(i);
					String variety = rs.getString(i+1);
					variety.trim();
					a.addVariety(variety);
				}
				tuples.add(t);
			}
			rs.close();
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void stop()
	{
	      try {
			_stmt.close();
			_conn.close();
	      } catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	
};
