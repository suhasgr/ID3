package Algorithm;

import java.util.ArrayList;

public class Attribute 
{
	private String _name;
	
	private ArrayList<String> _varieties;
	
	public Attribute(String name)
	{
		_name = name;
		_varieties = new ArrayList<String>();
	}
	
	public Attribute() {
		// TODO Auto-generated constructor stub
	}

	public void addVariety(String variety)
	{
		if(!_varieties.contains(variety))
		{
			_varieties.add(variety);
		}
	}
	
	public ArrayList<String> getVariety()
	{
		return _varieties;
	}
	
	public void setVariety(ArrayList<String> variety)
	{
		_varieties = variety;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public void setName(String name)
	{
		_name = name;
	}
}
