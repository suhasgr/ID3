package Algorithm;

import java.util.ArrayList;

public class Tree 
{
	ArrayList<Tuple> _tuples;
	ArrayList<Tree> _children;
	Attribute _attribute;
	static Attribute _targetAttribute;
	Tree _parent;
	
	ArrayList<Attribute> _featureAttributes;
	
	String _variety;
	
	public void setVariety(String variety)
	{
		_variety = variety;
	}
	
	public String getVarierty()
	{
		return _variety;
	}
	
	public Tree(ArrayList<Tuple> tuples, Attribute targetAttribute,ArrayList<Attribute> featureAttributes)
	{
		assignTuples(tuples);
		setTargetAttribute(targetAttribute);
		setFeatureAttributes(featureAttributes);		
		
		_children = new ArrayList<Tree>();
		
		if(!_featureAttributes.isEmpty())
			buildTree();
	}
	
	private void buildTree()
	{
		Attribute attWithMaxgain = null;
		double gainValue = -Double.MAX_VALUE;
		
		for(int i = 0; i< _featureAttributes.size();++i)
		{
			double g = gain(_featureAttributes.get(i));
			if(g > gainValue)
			{
				gainValue = g;
				attWithMaxgain = _featureAttributes.get(i);		
			}
		}
		
		
		if(gainValue != -Double.MAX_VALUE)
		{
			_attribute = new Attribute(attWithMaxgain.getName());
			_attribute.setVariety(attWithMaxgain.getVariety());
			
			_featureAttributes.remove(attWithMaxgain);

			
			for(int i = 0; i< _attribute.getVariety().size();++i)
			{
				ArrayList<Tuple> tuples = getTuplesMatchingVariety(_attribute.getName(),_attribute.getVariety().get(i));
		
				Tree t = new Tree(tuples,_targetAttribute,_featureAttributes);
					
				if(t!= null)
				{
					t.setVariety(_attribute.getVariety().get(i));
					_children.add(t);
				}
			}
			
			
			for(int i =0; i<_children.size();++i)
			{
				_children.get(i)._parent = this;
			}
		}
	}
	
	private ArrayList<Tuple> getTuplesMatchingVariety(String atrribute, String variety)
	{
		ArrayList<Tuple> tuples = new ArrayList<Tuple>();
		
		for(int i = 0;i< _tuples.size();++i)
		{
			if(_tuples.get(i).getValue(atrribute).equals(variety))
			{
				tuples.add(_tuples.get(i));
			}
		}
		
		return tuples;
	}
	
	private void assignTuples(ArrayList<Tuple> tuples)
	{
		_tuples = new ArrayList<Tuple>();
		_tuples.addAll(tuples);
	}
	
	private static void setTargetAttribute(Attribute targetAttribute)
	{
		_targetAttribute = targetAttribute;
	}
	
	private void setFeatureAttributes(ArrayList<Attribute> featureAttributes)
	{
		_featureAttributes = new ArrayList<Attribute>();
		_featureAttributes.addAll(featureAttributes);

	}
	
	private static double entropy(ArrayList<Double> probs)
	{
		double et = 0.0;
		
		for(int i = 0; i< probs.size();++i)
		{
			if(probs.get(i) != 0.0)
			{
				et+= (probs.get(i) * Math.log10(probs.get(i)) / Math.log10(2)); 
			}
		}
		if(et == 0.0)
			return et;
		return et * (-1);
	}
	
	private double gain(Attribute a)
	{
		double g = 0.0;
		
		ArrayList<String> varieties = a.getVariety();
		
		int totalTupleCount  = _tuples.size();
		for(int i = 0; i<varieties.size();++i )
		{
			int sv = countMatchingThisVariety(a.getName(),varieties.get(i));
			if(sv > 0)
			{
				ArrayList<Double> probsForThisVariety = new ArrayList<Double>();
				
				ArrayList<String> targetVariety = _targetAttribute.getVariety();
				
				for(int j = 0; j< targetVariety.size();++j)
				{
					int countG = getCountWRTTarget(a.getName(),varieties.get(i),targetVariety.get(j));
					probsForThisVariety.add(countG * 1.0 /sv);
				}
				
				double et = entropy(probsForThisVariety);
				g+= et * sv/totalTupleCount;
			}
		}
		
		double ec = getEC();
		g = ec- g;
		return g;
	}
	
	private int countMatchingThisVariety(String attributeName,String variety)
	{
		int count = 0;
		for(int i = 0; i<_tuples.size();++i)
		{
			if(_tuples.get(i).getValue(attributeName).equals(variety))
				++count;
		}
		
		return count;
	}
	
	private int getCountWRTTarget(String attributeName,String variety,String targetVariety)
	{
		int count = 0;
		for(int i = 0; i<_tuples.size();++i)
		{
			if(_tuples.get(i).getValue(attributeName).equals(variety)  && _tuples.get(i).getValue(_targetAttribute.getName()).equals(targetVariety))
				++count;
		}
		
		return count;
	}
	
	private double getEC()
	{
		double ec = 0.0;
		ArrayList<String> targetVariety = _targetAttribute.getVariety();
		ArrayList<Double> probs = new ArrayList<Double>();
		int totalTupleCount  = _tuples.size();
		for(int i = 0; i< targetVariety.size();++i)
		{
			int countTV = countMatchingThisVariety(_targetAttribute.getName(),targetVariety.get(i));
			probs.add(countTV*1.0/totalTupleCount);
		}
		ec = entropy(probs);
		return ec;
	}
	
	public Attribute getAtrribute()
	{
		return _attribute;
	}
	
	public String getVariety()
	{
		return _variety;
	}
	
	public ArrayList<Tuple> getTuples()
	{
		return _tuples;
	}
	
	public ArrayList<Tree> getChildren()
	{
		return _children;
	}
	
	public Tree getParent()
	{
		return _parent;
	}
}

