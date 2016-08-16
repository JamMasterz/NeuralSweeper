package com.jam.statistics;

public class FitnessDatapoint implements AveragableDataPoint<Integer, Integer>{
	public static final int ID_AVERAGE = 0;
	public static final int ID_BEST = 1;
	
	protected int generation;
	protected int average, best;
	
	public FitnessDatapoint(int x, int avg, int best){
		this.generation = x;
		this.average = avg;
		this.best = best;
	}

	@Override
	public Integer getX() {
		return generation;
	}

	@Override
	public Integer getY(int id) {
		if (id == ID_AVERAGE){
			return average;
		} else if (id == ID_BEST){
			return best;
		} else {
			return null;
		}
	}

	@Override
	public int getNumYValues() {
		return 2; //Average and best
	}

	/*
	@Override
	public void setY(int id, Integer value) {
		if (id == ID_AVERAGE){
			average = value.intValue();
		} else if (id == ID_BEST){
			best = value.intValue();
		} else {
			throw new ArrayIndexOutOfBoundsException("No Y-value with id: " + id);
		}
	}
	*/

	@Override
	public AveragableDataPoint<Integer, Integer> sumYValues(AveragableDataPoint<Integer, Integer> p) {
		FitnessDatapoint p1 = (FitnessDatapoint) p;
		this.average += p1.average;
		this.best += p1.best;
				
		return this;
	}

	@Override
	public AveragableDataPoint<Integer, Integer> multiplyYValues(double factor) {
		this.average = (int) (this.average * factor);
		this.best = (int) (this.best * factor);
		
		return this;
	}
}
