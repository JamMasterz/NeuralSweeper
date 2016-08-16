package com.jam.neural.view;

import java.awt.BorderLayout;

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

//TODO: When num of datapoints get bigger than smth, average the stuff. Can run avg on avgs.
public class FitnessGraph extends JFrame{
	private static final long serialVersionUID = 1L;
	
	private XYSeries avgSeries;
	private XYSeries bestSeries;
	private int gen = 0;

	public FitnessGraph() {
		super("Fitness Graph");
        this.avgSeries = new XYSeries("Average fitness", false);
        this.bestSeries = new XYSeries("Best fitness", false);
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
	
	public void addFitness(int avg, int best){
		avgSeries.add(gen, avg);
		bestSeries.add(gen++, best);
	}
}
