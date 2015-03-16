package Main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import Algorithm.Attribute;
import Algorithm.Tree;
import Algorithm.Tuple;

class finalVarietyValue
{
	String Variety;
	double value;
}

public class Driver 
{
	
	public static void main(String args[])
	{
		String currentDirectory = System.getProperty("user.dir");
		if(args.length < 2)
		{
			System.out.println("Missing file names of tree.txt or test.txt or both");
			System.exit(0);
		}
		
		String treeFile = currentDirectory + "\\" + args[0];
		String testFile  = currentDirectory + "\\" + args[1];
		
		File tree = new File(treeFile);
		File test = new File(testFile);
		
		if(!tree.exists() )
		{
			System.out.println("Tree file not Present");
			System.exit(0);
		}
		if(!test.exists())
		{
			System.out.println("Test file not Present");
			System.exit(0);
		}
		
		ReadTreeFile readTreeFile = new ReadTreeFile();
		boolean status = readTreeFile.readTree(tree);
		if(!status)
		{
			System.out.println("Some error in reading tree file");
			System.out.println(0);
		}
		
		ArrayList<Tuple> tuples = ReadTreeFile._tuples.getTuples();
		//ArrayList<Attribute> attributes = ReadTreeFile._attributes;
		Attribute targetAttribute = new Attribute();
		ArrayList<Attribute> featureAttributes = new ArrayList<Attribute>();
		
		/*for(int i = 0; i< attributes.size();++i)
		{
			if(attributes.get(i).getName().equals(ReadTreeFile._targetAttribute) )
				targetAttribute= attributes.get(i);
			if(ReadTreeFile._featureAttributes.contains(attributes.get(i).getName()))
				featureAttributes.add(attributes.get(i));
		}*/
		
		
		for(int i = 0;i<ReadTreeFile._featureAttributes.size();++i)
		{
			featureAttributes.add(ReadTreeFile.getAttribute(ReadTreeFile._featureAttributes.get(i)));
		}
		targetAttribute = ReadTreeFile.getAttribute(ReadTreeFile._targetAttribute);
		
		Tree root = new Tree(tuples,targetAttribute,featureAttributes);
		
		
		// Tree Testing
		//testingTree(root);
		
		ReadTestFile rd = new ReadTestFile();
		rd.readTestFile(test,featureAttributes);
		
		/** The following function works for data without * Input. This is the bench mark for the *rred inputs
		//ProbsForInputData(root, rd.getTestItems(),targetAttribute);
		*/
		
		StarredInputHelper(root, rd.getTestItems(), targetAttribute);
		
		
		/*----------------------------------------------------------------------------
		 * Three fold cross validation
		 */
		
		for(int i = 0; i< 3;++i)
		{
			System.out.println("Crossfold "+(i+1));
			ArrayList<Tuple> trainTuples = new ArrayList<Tuple>();
			ArrayList<Tuple> testTuples = new ArrayList<Tuple>();
			for(int j = (i * 3),k=0; k < (2*tuples.size()/3);j = (j+1)%tuples.size(),++k)
			{
				trainTuples.add(tuples.get(j));
			}
			
			testTuples.addAll(tuples);
			testTuples.removeAll(trainTuples);
			
			ArrayList<Attribute> allAttributes = new ArrayList<Attribute>();
			allAttributes.addAll(ReadTreeFile._attributes);
			allAttributes.remove(targetAttribute);
			Tree trainedRoot = new Tree(trainTuples,targetAttribute,featureAttributes);
			
			//testingTree(trainedRoot);
			
			ArrayList<HashMap<String, String>> testTuplesData = getTestTuples(testTuples,targetAttribute,featureAttributes);
			StarredInputHelper(trainedRoot, testTuplesData, targetAttribute);
			System.out.println();
		}
		
		System.out.println("Done");
		
	}
	
	public static ArrayList<HashMap<String, String>> getTestTuples(ArrayList<Tuple> testTuples,Attribute targetAttribute,ArrayList<Attribute> featureAttibutes)
	{
		ArrayList<HashMap<String, String>> testTuplesData = new ArrayList<HashMap<String, String>>();
		
		for(int i =0;i<testTuples.size();++i)
		{
			Tuple t = testTuples.get(i);
			HashMap<String, String> temp= new HashMap<String, String>(); 
			
			for(int j = 0; j<featureAttibutes.size();++j)
			{
				temp.put(featureAttibutes.get(j).getName(), t.getValue(featureAttibutes.get(j).getName()));
			}
			/*temp.putAll(t.getCompleteData());
			temp.remove(targetAttribute.getName());*/	
			testTuplesData.add(temp);
		}
		
		return testTuplesData;
	}
	
	public static void testingTree(Tree root)
	{
		class pair
		{
			public Tree t;
			int level;
		}

		pair p = new pair();
		p.t = root;
		p.level = 1;
		Queue<pair> pq = new LinkedList<pair>();
		
		pq.add(p);
		
		while(!pq.isEmpty())
		{
			pair pt = pq.poll();
			Tree temp = pt.t;
			System.out.println("Level = "+pt.level);
			if(temp.getAtrribute()!=null)
				System.out.print("Attribute = "+ temp.getAtrribute().getName());
			System.out.println("Node:  Variety = "+temp.getVariety()+"  Number of tuples = "+temp.getTuples().size());
		
			for(int i = 0; i<temp.getChildren().size();++i)
			{
				pair t = new pair();
				t.level = pt.level+1;
				t.t = temp.getChildren().get(i);
				pq.add(t);
			}
		}
	}
	
	public static void ProbsForInputData(Tree root,ArrayList<HashMap<String, String>> TestInput,Attribute targetAttribute)
	{		
		for(int i =0; i< TestInput.size();++i)
		{
			System.out.print("tuple: ");
			HashMap<String, String> map = TestInput.get(i);
			ArrayList<finalVarietyValue> probs = new ArrayList<finalVarietyValue>();
			
			Tree tempRoot = root;
			int count = map.size();
			while(tempRoot.getTuples().size() > 0 && count > 0)
			{
				Attribute pres = tempRoot.getAtrribute();
				String variety = null;
				if(map.containsKey(pres.getName()))
				{
					variety = map.get(pres.getName());
				}
				else
				{
					System.out.println("Wrong Input");
					return;
				}
				System.out.print(variety+" ");
				ArrayList<Tree> children = tempRoot.getChildren();
				for(int j=0;j<children.size();++j)
				{
					if(children.get(j).getVariety().trim().equals(variety))
					{
						tempRoot = children.get(j);
						break;
					}
				}
				count--;
			}
			
			if(tempRoot.getTuples().size() == 0)
			{
				while(tempRoot.getTuples().size() == 0)
					tempRoot = tempRoot.getParent();
			}
			
			int totalTuplesCount = tempRoot.getTuples().size();
			for(int j=0;j<targetAttribute.getVariety().size();++j)
			{
				int numberOfTuples = getTuplesCountMathingVariety(tempRoot.getTuples(),targetAttribute.getName(),targetAttribute.getVariety().get(j));
				finalVarietyValue f = new finalVarietyValue();
				f.Variety = targetAttribute.getVariety().get(j);
				f.value = numberOfTuples*1.0/totalTuplesCount;
				probs.add(f);
			}
			
			System.out.println();
			System.out.print("class: ");
			for(int j =0;j<probs.size();++j)
			{
				System.out.print(probs.get(j).Variety+" "+probs.get(j).value+" ");
			}
			System.out.println();
		}
	}
	
	
	public static int getTuplesCountMathingVariety(ArrayList<Tuple> tuples, String targetAttribute,String variety)
	{
		int count = 0;
		for(int i=0; i<tuples.size();++i)
		{
			Tuple t = tuples.get(i);
			if(t.getValue(targetAttribute).equals(variety))
				count++;
		}
		return count;
	}
	
	public static void StarredInputHelper(Tree root, ArrayList<HashMap<String, String>> TestInput,Attribute targetAttribute)
	{
		String OutputString = "";
		for(int i =0; i< TestInput.size();++i)
		{
			OutputString+= "tuple: ";
			HashMap<String, String> map = TestInput.get(i);
			for(int j = 0;j<map.size();++j)
			{
				OutputString+= map.values().toArray()[j]+" ";
			}
			ArrayList<finalVarietyValue> finalOutput = ProbsForInputDataStarred(root,map,targetAttribute);
			OutputString+="\n";
			
			for(int j =0;j<finalOutput.size();++j)
			{
				OutputString+=finalOutput.get(j).Variety+" "+finalOutput.get(j).value+" ";
			}
			OutputString+="\n";
		}

		System.out.println(OutputString);
	}
	
	public static ArrayList<finalVarietyValue> ProbsForInputDataStarred(Tree root,HashMap<String, String> map,Attribute targetAttribute)
	{
		HashMap<String, String> localMap = new HashMap<String, String>();
		localMap.putAll(map); 
		
		ArrayList<finalVarietyValue> probs = new ArrayList<finalVarietyValue>();			
		Tree tempRoot = root;
		int count = map.size();
		
		while(tempRoot.getTuples().size() > 0 && count > 0)
		{
			Attribute pres = tempRoot.getAtrribute();
			String variety = null;
			if(localMap.containsKey(pres.getName()))
			{
				variety = localMap.get(pres.getName());
				localMap.remove(pres.getName());
			}
			else
			{
				System.out.println("Wrong Input");
				System.exit(0);
			}
			
			ArrayList<Tree> children = tempRoot.getChildren();
			
			if(variety.contains("*"))
			{
				ArrayList<finalVarietyValue> summationProbs = new ArrayList<finalVarietyValue>();
				
				for(int j=0;j<children.size();++j)
				{
					
					 Tree tempChild = children.get(j);
					 ArrayList<finalVarietyValue> prob = ProbsForInputDataStarred(tempChild,localMap,targetAttribute);
					 AddEach(summationProbs,prob,tempChild.getTuples().size());
				}
				averageSummation(summationProbs,tempRoot.getTuples().size());
				return summationProbs;
			}
			else
			{
				for(int j=0;j<children.size();++j)
				{
					if(children.get(j).getVariety().trim().equals(variety.trim()))
					{
						tempRoot = children.get(j);
						break;
					}
				}
			}
			
			count--;
		}
		
		if(tempRoot.getTuples().size() == 0)
		{
			while(tempRoot.getTuples().size() == 0)
				tempRoot = tempRoot.getParent();
		}
		
		int totalTuplesCount = tempRoot.getTuples().size();
		for(int j=0;j<targetAttribute.getVariety().size();++j)
		{
			int numberOfTuples = getTuplesCountMathingVariety(tempRoot.getTuples(),targetAttribute.getName(),targetAttribute.getVariety().get(j));
			finalVarietyValue f = new finalVarietyValue();
			f.Variety = targetAttribute.getVariety().get(j);
			f.value = numberOfTuples*1.0/totalTuplesCount;
			probs.add(f);
		}
		
		return probs;
	}
	
	public static void AddEach(ArrayList<finalVarietyValue> summationProbs,ArrayList<finalVarietyValue> prob,int numberofTuples)
	{
		for(int i = 0; i< prob.size();++i)
		{
			finalVarietyValue fvvInSum = findInSummation(summationProbs,prob.get(i));
			fvvInSum.value += (prob.get(i).value * numberofTuples);
		}
	}
	
	public static finalVarietyValue findInSummation(ArrayList<finalVarietyValue> summationProbs, finalVarietyValue varieryvalue)
	{
		finalVarietyValue fvv = null; 
		for(int i = 0; i< summationProbs.size();++i)
		{
			if(summationProbs.get(i).Variety.equals(varieryvalue.Variety))
			{
				fvv = summationProbs.get(i);
			}
		}
		
		if(fvv == null)
		{
			fvv = new finalVarietyValue();
			fvv.Variety = varieryvalue.Variety;
			fvv.value = 0.0;
			summationProbs.add(fvv);
		}
		return fvv;
	}
	
	public static void averageSummation(ArrayList<finalVarietyValue> summationProbs,int totalTuples)
	{
		for(int i = 0; i< summationProbs.size();++i)
		{
			summationProbs.get(i).value = summationProbs.get(i).value/totalTuples;
		}
	}
}
