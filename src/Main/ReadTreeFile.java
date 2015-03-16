package Main;
import Algorithm.Attribute;
import Algorithm.Tuples;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ReadTreeFile 
{
	String _table;
	static ArrayList<String> _totalAttributes;
	static String _targetAttribute;
	static ArrayList<String> _featureAttributes;
	static Tuples _tuples;
	static ArrayList<Attribute> _attributes; 
	MySQLConnection _mySQL;
	
	ReadTreeFile()
	{
		_mySQL = new MySQLConnection("suhas_db", "root", "root");
	}
	
	void getTuples()
	{
		_tuples = new Tuples();
		String query = "select * from table1";
		
		_attributes = new ArrayList<Attribute>();
		_mySQL.fillTuplesAndAttributes(query,_tuples,_totalAttributes,_attributes);		
	}
	
	private void getTotalAttributes()
	{
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(_table));
			try {
				String firstLine = br.readLine();
				String[] attributes = firstLine.split(" ");
				_totalAttributes = new ArrayList(Arrays.asList(attributes));
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
						e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void getTable(File treeFile)
	{
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(treeFile));
			String tableName = br.readLine();
			_table = System.getProperty("user.dir") + "\\" + tableName;
			
			_mySQL.deleteTable("table1");

			getTotalAttributes();
			_mySQL.createTable("table1",_totalAttributes);
			_mySQL.infile(tableName,"table1");
			getTuples();
			
			getTargetAttribute(br.readLine());
			getFeatureAttribute(br.readLine());
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private void getTargetAttribute(String attribute)
	{
		_targetAttribute = attribute;
	}
	
	private void getFeatureAttribute(String attributes)
	{
		_featureAttributes = new ArrayList<String>();
		String[] features = attributes.split(" ");
		
		for(int i = 0; i< features.length;++i)
		{
			_featureAttributes.add(features[i]);
		}
	}
	
	boolean readTree(File treeFile)
	{
		boolean status  = true;
		
		getTable(treeFile);
		status = validate();
		return status;
	}
	
	private boolean validate()
	{
		boolean status = true;
		if(!_totalAttributes.contains(_targetAttribute))
		{
			status = false;
		}
		
		for(int i = 0; i< _featureAttributes.size();++i)
		{
			if(!_totalAttributes.contains(_featureAttributes.get(i)))
			{
				status  = false;
				break;
			}
		}
		
		return status;
	
	}
	
	public static Attribute getAttribute(String name)
	{
		for(int i = 0;i<_attributes.size();++i)
		{
			if(name.equals(_attributes.get(i).getName()))
			{
				return _attributes.get(i);
			}
		}
		return null;
	}

}
