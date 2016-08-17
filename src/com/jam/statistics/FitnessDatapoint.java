package com.jam.statistics;

public class FitnessDatapoint implements AveragableDataPoint<Integer, Double>{
	public static final int ID_AVERAGE = 0;
	public static final int ID_BEST = 1;
	
	protected int generation;
	protected double average, best;
	
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
	public Double getY(int id) {
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

	@Override
	public AveragableDataPoint<Integer, Double> sumYValues(AveragableDataPoint<Integer, Double> p) {
		FitnessDatapoint p1 = (FitnessDatapoint) p;
		this.average += p1.average;
		this.best += p1.best;
				
		return this;
	}

	@Override
	public AveragableDataPoint<Integer, Double> multiplyYValues(double factor) {
		this.average = this.average * factor;
		this.best = this.best * factor;
		
		return this;
	}
}
