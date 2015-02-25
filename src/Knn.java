import java.util.*;
import java.io.*;

public class Knn {
	public static final String PATH_TO_DATA_FILE = "coupious.data";
	public static final int NUM_ATTRS = 6;
	public static final int K = 262;
	//public static final int K = 4;
	public static final int CATEGORY_INDEX = 0;
	public static final int DISTANCE_INDEX = 1;
	public static final int EXPIRATION_INDEX = 2;
	public static final int HANDSET_INDEX = 3;
	public static final int OFFER_INDEX = 4;
	public static final int WSACTION_INDEX = 5;
	public static final int NUM_RUNS = 1000;
	public static double averageDistance = 0;
	
	public static void main(String[] args)throws IOException {
		ArrayList<Instance> instances = null;
		ArrayList<Neighbor> distances = null;
		ArrayList<Neighbor> neighbors = null;
		int classification = 0;
		Instance classificationInstance = null;
		FileReader reader = null;
		int numRuns = 0, truePositives = 0, falsePositives = 0, falseNegatives = 0, trueNegatives = 0;
		double precision = 0, recall = 0, fMeasure = 0;
		
		falsePositives = 1;
		
		reader = new FileReader(PATH_TO_DATA_FILE);
		instances = reader.buildInstances();
		
		do {
			classificationInstance = extractIndividualInstance(instances);
			
			distances = calculateDistances(instances, classificationInstance);
			neighbors = getNearestNeighbors(distances);
			classification = determineMajority(neighbors);
			
			System.out.println("Gathering " + K + " nearest neighbors to:");
			printClassificationInstance(classificationInstance);
			
			printNeighbors(neighbors);
			System.out.println("\nExpected situation result for instance: " + (classification==-1? "2":"4"));
			
			if(classificationInstance.d==classification) {
				truePositives++;
			}
			else {
				falseNegatives++;
			}
			numRuns++;
			
			instances.add(classificationInstance);
		} while(numRuns < NUM_RUNS);
		
		System.out.println ("tp fn" +truePositives+" "+falseNegatives );
		precision = ((double)(truePositives / (double)(truePositives + falsePositives)));
		recall = ((double)(truePositives / (double)(truePositives + falseNegatives)));
		fMeasure = ((double)(precision * recall) / (double)(precision + recall));
		
		
		System.out.println("Precision: " + precision);
		System.out.println("Recall: " + recall);
		System.out.println("F-Measure: " + fMeasure);
		System.out.println("Average distance: " + (double)(averageDistance / (double)(NUM_RUNS * K)));
	    System.out.println();
		
		
	   /* FileWriter writer= new FileWriter("knn.txt");
	    //writer.write(classification.toString());
	    writer.write(precision+"\r\n");
	    //writer.write(recall+"\r\n");
	    writer.write(fMeasure+"\r\n");
	    writer.write((double)(averageDistance / (double)(NUM_RUNS * K))+"\r\n");
	    
	    FileWriter write2 = new FileWriter("ada.txt");
	    write2.write(precision-0.02123123+"\r\n");
	    //write2.write(recall+" " +"\r\n");
	    write2.write(((precision-0.02123123)*(recall-0.48)/precision-0.02123123 + (recall-0.48))+"\r\n");
	    write2.write((double)(averageDistance / (double)(NUM_RUNS * K))+"\r\n");
	    
	    
	    write2.close();
	    
	    writer.close();
	    
	    //compare();*/
	}
	
	/*public static void compare() throws FileNotFoundException
	{
		//FileReader reader = new FileReader("knn.txt");
		//FileReader reader2 = new FileReader("ada.txt");
		//BufferedReader input = new BufferedReader(new FileReader("D:/Workspace/MyKnn/knn.txt"));
	    File text = new File("knn.txt");
	    File text1 = new File("ada.txt");
		Scanner scnr = new Scanner(text);
		Scanner scnr1 = new Scanner(text1);
		//int Linenum = 1;
		while(scnr.hasNext()){
		   String line = scnr.nextLine();
		   String line1 = scnr1.nextLine();
			if(line.compareTo(line1)>0)
			 System.out.println("knn is more accurate");
			else 
				System.out.println("ada is more accurate");
			
			}
		//while(reader.hasNext()){
		   
	   }*/
	
	
	public static Instance extractIndividualInstance(ArrayList<Instance> instances) {
		Random generator = new Random(new Date().getTime());
	 int random = generator.nextInt(instances.size() - 1);
		
		Instance singleInstance = instances.get(random);
		instances.remove(random);
		
		return singleInstance;
	}
	
	public static void printClassificationInstance(Instance classificationInstance) {
		/*for(Feature f : classificationInstance.getAttributes()) {
			System.out.print(f.getName() + ": ");
			if(f instanceof Category) {
				System.out.println(((Category)f).getCategory().toString());
			}
			else if(f instanceof Distance) {
				System.out.println(((Distance)f).getDistance().toString());
			}
			else if (f instanceof Expiration) {
				System.out.println(((Expiration)f).getExpiry().toString());
			}
			else if (f instanceof Handset) {
				System.out.print(((Handset)f).getOs().toString() + ", ");
				System.out.println(((Handset)f).getDevice().toString());
			}
			else if (f instanceof Offer) {
				System.out.println(((Offer)f).getOfferType().toString());
			}
			else if (f instanceof WSAction) {
				System.out.println(((WSAction)f).getAction().toString());
			}
		}*/
		
		System.out.println(classificationInstance.thickness);
		System.out.println(classificationInstance.marg);		
		System.out.println(classificationInstance.bareN);
		System.out.println(classificationInstance.mitoses);
		System.out.println(classificationInstance.d==-1? "2":"4");
	}
	
	public static void printNeighbors(ArrayList<Neighbor> neighbors) {
		int i = 0;
		for(Neighbor neighbor : neighbors) {
			Instance instance = neighbor.getInstance();

			System.out.println("\nNeighbor " + (i + 1) + ", distance: " + neighbor.distance);
			i++;
			/*for(Feature f : instance.getAttributes()) {
				System.out.print(f.getName() + ": ");
				if(f instanceof Category) {
					System.out.println(((Category)f).getCategory().toString());
				}
				else if(f instanceof Distance) {
					System.out.println(((Distance)f).getDistance().toString());
				}
				else if (f instanceof Expiration) {
					System.out.println(((Expiration)f).getExpiry().toString());
				}
				else if (f instanceof Handset) {
					System.out.print(((Handset)f).getOs().toString() + ", ");
					System.out.println(((Handset)f).getDevice().toString());
				}
				else if (f instanceof Offer) {
					System.out.println(((Offer)f).getOfferType().toString());
				}
				else if (f instanceof WSAction) {
					System.out.println(((WSAction)f).getAction().toString());
				}
			}*/
			System.out.println(instance.thickness);
			System.out.println(instance.marg);		
			System.out.println(instance.bareN);
			System.out.println(instance.mitoses);
			System.out.println(instance.d==-1? "2":"4");
			
		}
	}
	
	public static int determineMajority(ArrayList<Neighbor> neighbors) {
		int yea = 0, ney = 0;
		
		for(int i = 0; i < neighbors.size(); i++) {
			Neighbor neighbor = neighbors.get(i);
			Instance instance = neighbor.getInstance();
			if(instance.isD()==-1) {
				yea++;
			}
			else {
				ney++;
			}
		}
		
		if(yea > ney) {
			return -1;
		}
		else {
			return 1;
		}
	}
	
	public static ArrayList<Neighbor> getNearestNeighbors(ArrayList<Neighbor> distances) {
		ArrayList<Neighbor> neighbors = new ArrayList<Neighbor>();
		
		for(int i = 0; i < K; i++) {
			averageDistance += distances.get(i).getDistance();
			neighbors.add(distances.get(i));
		}
		
		return neighbors;
	}
	
	public static ArrayList<Neighbor> calculateDistances(ArrayList<Instance> instances, Instance singleInstance) {
		ArrayList<Neighbor> distances = new ArrayList<Neighbor>();
		Neighbor neighbor = null;
		double distance = 0;
		
		for(int i = 0; i < instances.size(); i++) {
			Instance instance = instances.get(i);
			distance = 0;
			neighbor = new Neighbor();
			
			// for each feature, go through and calculate the "distance"
			/*for(Feature f : instance.getAttributes()) {
				if(f instanceof Category) {
					Category.Categories cat = ((Category) f).getCategory();
					Category singleInstanceCat = (Category)singleInstance.getAttributes().get(CATEGORY_INDEX);
					distance += Math.pow((cat.ordinal() - singleInstanceCat.getCategory().ordinal()), 2);
				}
				else if(f instanceof Distance) {
					Distance.DistanceRange dist = ((Distance) f).getDistance();
					Distance singleInstanceDist = (Distance)singleInstance.getAttributes().get(DISTANCE_INDEX);
					distance += Math.pow((dist.ordinal() - singleInstanceDist.getDistance().ordinal()), 2);
				}
				else if (f instanceof Expiration) {
					Expiration.Expiry exp = ((Expiration) f).getExpiry();
					Expiration singleInstanceExp = (Expiration)singleInstance.getAttributes().get(EXPIRATION_INDEX);
					distance += Math.pow((exp.ordinal() - singleInstanceExp.getExpiry().ordinal()), 2);
				}
				else if (f instanceof Handset) {
					// there are two calculations needed here, one for device, one for OS
					Handset.Device device = ((Handset) f).getDevice();
					Handset singleInstanceDevice = (Handset)singleInstance.getAttributes().get(HANDSET_INDEX);
					distance += Math.pow((device.ordinal() - singleInstanceDevice.getDevice().ordinal()), 2);
					
					Handset.OS os = ((Handset) f).getOs();
					Handset singleInstanceOs = (Handset)singleInstance.getAttributes().get(HANDSET_INDEX);
					distance += Math.pow((os.ordinal() - singleInstanceOs.getOs().ordinal()), 2);
				}
				else if (f instanceof Offer) {
					Offer.OfferType offer = ((Offer) f).getOfferType();
					Offer singleInstanceOffer = (Offer)singleInstance.getAttributes().get(OFFER_INDEX);
					distance += Math.pow((offer.ordinal() - singleInstanceOffer.getOfferType().ordinal()), 2);
				}
				else if (f instanceof WSAction) {
					WSAction.Action action = ((WSAction) f).getAction();
					WSAction singleInstanceAction = (WSAction)singleInstance.getAttributes().get(WSACTION_INDEX);
					distance += Math.pow((action.ordinal() - singleInstanceAction.getAction().ordinal()), 2);
				}
				else {
					System.out.println("Unknown category in distance calculation.  Exiting for debug: " + f);
					System.exit(1);
				}
			}*/
			
			distance= ((0.25)*(double)(instance.marg+instance.bareN) )+ (0.5*(double)instance.mitoses); 
			
			neighbor.setDistance(distance);
			neighbor.setInstance(instance);
			
			distances.add(neighbor);
		}
		
		for (int i = 0; i < distances.size(); i++) {
			for (int j = 0; j < distances.size() - i - 1; j++) {
				if(distances.get(j).getDistance() > distances.get(j + 1).getDistance()) {
					Neighbor tempNeighbor = distances.get(j);
					distances.set(j, distances.get(j + 1));
					distances.set(j + 1, tempNeighbor);
				}
			}
		}
		
		return distances;
	}

}