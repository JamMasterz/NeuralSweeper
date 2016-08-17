package com.jam.statistics;

import java.util.ArrayList;

/**
 * This class stores and manages data points that implement the DataPoint interface 
 * When the points exceed a certain limit, they are averaged, making the data much more compact
 * Mathematically, the averaging process will get logarithmically less frequent ass the data points are put into this manager.
 * The averaged values have the first points X coordinate. Example, 100 points, (0,0), (1,1), (2,2), ..., (99, 99) are averaged, 
 * the average is (0, 49)
 */
//TODO: Pool the DataPoint instances
@SuppressWarnings("rawtypes")
public class StatisticsManager<U extends AveragableDataPoint> {
	public enum UpdateRequirement{
		NO_UPDATE, UPDATE_ONE_POINT, UPDATE_EVERYTHING;
	}
	
	private final ArrayList<U> points;
	private final ArrayList<U> unprocessed;
	
	private final int averageNum;
	private final int maxPoints;
	
	private int currentAverage = 0;
	
	/**
	 * @param maxPoints How many points the list can containn before it gets averaged
	 * @param averageNum How many points are used to computer averages
	 */
	public StatisticsManager(int maxPoints, int averageNum) {
		if (averageNum > maxPoints) throw new IllegalArgumentException("Points to average must be less or equal to the max number of points");
		if (maxPoints % averageNum != 0) throw new IllegalArgumentException("Max number of points must be evenly divisible by number of points to average");
		
		this.averageNum = averageNum;
		this.maxPoints = maxPoints;
		this.points = new ArrayList<U>();
		this.unprocessed = new ArrayList<U>(averageNum);
	}
	
	/**
	 * @param dataPoint Data point to add
	 * @return Whether the addition has modified the set of points. Its not required to happen
	 * since this class averages points. If there aren't enough points to calculate an average,
	 * the addition will not be visible. The value however, is stored.
	 */
	public UpdateRequirement put(U dataPoint){
		UpdateRequirement update = UpdateRequirement.NO_UPDATE;
		if (currentAverage == 0){
			//If its the first run, store it in the main list
			points.add(dataPoint);
			update = UpdateRequirement.UPDATE_ONE_POINT;
		} else {
			unprocessed.add(dataPoint);
			
			if (unprocessed.size() == currentAverage){
				//If we can compute a new average, do it
				points.add(averageUnprocessed());
				update = UpdateRequirement.UPDATE_ONE_POINT;
			}
		}
		
		if (points.size() == maxPoints){
			averageEverything();
			update = UpdateRequirement.UPDATE_EVERYTHING;
		}
		
		return update;
	}
	
	@SuppressWarnings("unchecked")
	private void averageEverything(){
		currentAverage = (currentAverage == 0) ? averageNum : currentAverage * averageNum;
		
		for (int i = 0; i < maxPoints / averageNum; i++) {
			for (int j = 1; j < averageNum; j++) {
				points.get(i).sumYValues(points.remove(i + 1));
			}
		}
		for (U p : points){
			p.multiplyYValues(1 / (double) averageNum);
		}
	}
	
	@SuppressWarnings("unchecked")
	private U averageUnprocessed(){
		//Add all the point values to the first point and divide by amount
		U first = unprocessed.remove(0);
		for (int i = 0; i < currentAverage - 1; i++) {
			first.sumYValues(unprocessed.remove(0));
		}
		first.multiplyYValues(1 / (double) currentAverage);
		
		return first;
	}
	
	public ArrayList<U> getData(){
		return points;
	}
	
	public U getDataPoint(int index){
		return points.get(index);
	}
}
