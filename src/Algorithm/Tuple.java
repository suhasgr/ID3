package Algorithm;

import java.util.ArrayList;
import java.util.HashMap;

public class Tuple 
{
	HashMap<String, String> _keyValue;
	public Tuple(ArrayList<String> attributes)
	{
		_keyValue = new HashMap<String, String>();
		for(int i = 0; i< attributes.size();++i)
		{
			_keyValue.put(attributes.get(i), "");
		}
	}
	
	public void insert(String val,String attr)
	{
		_keyValue.put(attr, val);
	}
	
	public String getValue(String key)
	{
		if(_keyValue.containsKey(key))
			return _keyValue.get(key);
		return null;
	}
	
	public HashMap<String, String> getCompleteData()
	{
		return _keyValue;
	}
	
}

