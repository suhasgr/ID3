package Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import Algorithm.Attribute;

public class ReadTestFile 
{
	
	ArrayList<HashMap<String, String>> _testItems;
	public ReadTestFile() {
		// TODO Auto-generated constructor stub
		_testItems = new ArrayList<HashMap<String, String>>();
	}
	
	public void readTestFile(File test,ArrayList<Attribute> featureAttributes)
	{
		 
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(test));
			String line;
			try {
				while((line = br.readLine()) != null)
				{
					String[] data = line.split(" ");
					HashMap<String, String> input = new HashMap<String, String>();
					for(int i = 0; i< data.length;++i)
					{
						input.put(featureAttributes.get(i).getName(), data[i]);
					}
					_testItems.add(input);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public ArrayList<HashMap<String, String>> getTestItems()
	{
		return _testItems;
	}
}
