package com.jam.neural.view;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.jam.statistics.FitnessDatapoint;
import com.jam.statistics.StatisticsManager.UpdateRequirement;

public class FitnessGraph extends JFrame{
	private static final long serialVersionUID = 1L;
	
	private ArrayList<FitnessDatapoint> data;
	
	private XYSeries avgSeries;
	private XYSeries bestSeries;

	public FitnessGraph(ArrayList<FitnessDatapoint> data) {
		super("Fitness Graph");
		if (data == null) throw new IllegalArgumentException("Data must not be null");
        this.avgSeries = new XYSeries("Average fitness", false);
        this.bestSeries = new XYSeries("Best fitness", false);
        
        this.data = data;
        for (FitnessDatapoint p : data){
        	addData(p);
        }
        
        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(avgSeries);
        dataset.addSeries(bestSeries);
        final JFreeChart chart = createChart(dataset);

        final ChartPanel chartPanel = new ChartPanel(chart);

        final JPanel content = new JPanel(new BorderLayout());
        content.add(chartPanel);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        setContentPane(content);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        
        pack();
        setVisible(true);
	}
	
	private JFreeChart createChart(final XYDataset dataset) {
        final JFreeChart result = ChartFactory.createXYLineChart("Fitness of generations", "Generation", "Fitness", dataset);
        final XYPlot plot = result.getXYPlot();
        ValueAxis axis = plot.getDomainAxis();
        axis.setLowerBound(0);
        axis.setAutoRange(true);
        axis = plot.getRangeAxis();
        axis.setLowerBound(0);
        axis.setAutoRange(true);
        
        return result;
    }
	
	public void update(UpdateRequirement update){
		switch (update){
			case NO_UPDATE:
				break;
			case UPDATE_ONE_POINT:
				addData(data.get(data.size() - 1));
				break;
			case UPDATE_EVERYTHING:
				avgSeries.clear();
				bestSeries.clear();
				for (FitnessDatapoint p : data){
					addData(p);
				}
				break;
		}
	}
	
	private void addData(FitnessDatapoint p){
		avgSeries.add(p.getX(), p.getY(FitnessDatapoint.ID_AVERAGE));
    	bestSeries.add(p.getX(), p.getY(FitnessDatapoint.ID_BEST));
	}
}
