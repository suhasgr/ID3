package Algorithm;

import java.util.ArrayList;

public class Tuples
{
	ArrayList<Tuple> _tuples;
	public Tuples()
	{
		_tuples = new ArrayList<Tuple>();
	}
	public void add(Tuple t)
	{
		_tuples.add(t);
	}
	
	public int getSize()
	{
		return _tuples.size();
	}
	
	public Tuple getAt(int i)
	{
		return _tuples.get(i);
	}
	
	public ArrayList<Tuple> getTuples()
	{
		return _tuples;
	}
}

