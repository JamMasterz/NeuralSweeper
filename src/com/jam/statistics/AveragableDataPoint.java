package com.jam.statistics;

public interface AveragableDataPoint<T, V>{
	T getX();
	V getY(int id);
	int getNumYValues();
	//void setY(int id, V value);
	AveragableDataPoint<T, V> sumYValues(AveragableDataPoint<T, V> p);
	AveragableDataPoint<T, V> multiplyYValues(double factor);
}
