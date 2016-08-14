package com.jam.neural.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import javax.swing.border.TitledBorder;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = -2928806346426112279L;
	protected final ButtonGroup evolutionRadios = new ButtonGroup();
	protected final ButtonGroup gameDifficultyRadios = new ButtonGroup();
	protected JSpinner tpsSpinner, threadsSpinner, generationsSpinner, hiddenLayersSpinner, nodesHiddenLayerSpinner, specimensSpinner;
	protected JSpinner gamesVertSpinner, gamesHorSpinner, gameScaleSpinner;
	protected JButton attachGUIButton, startButton, stopButton, showGraphsButton;
	protected JLabel indicatorLabel, generationLabel;
	protected JPanel taskPanel;

	/**
	 * Create the panel.
	 */
	public MainFrame(JPanel taskPanel) {
		setTitle("Minesweeper Neural Network");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		setSize(1000, 450);
		
		JPanel normalEvoPanel = new JPanel();
		normalEvoPanel.setBorder(new TitledBorder(null, "Normal Evolution", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		normalEvoPanel.setBounds(10, 37, 318, 205);
		getContentPane().add(normalEvoPanel);
		SpringLayout sl_normalEvoPanel = new SpringLayout();
		normalEvoPanel.setLayout(sl_normalEvoPanel);
		
		JLabel lblTickssecond = new JLabel("Ticks/Second :");
		sl_normalEvoPanel.putConstraint(SpringLayout.WEST, lblTickssecond, 10, SpringLayout.WEST, normalEvoPanel);
		normalEvoPanel.add(lblTickssecond);
		
		tpsSpinner = new JSpinner();
		tpsSpinner.setModel(new SpinnerNumberModel(2, 1, 10, 1));
		sl_normalEvoPanel.putConstraint(SpringLayout.NORTH, tpsSpinner, 10, SpringLayout.NORTH, normalEvoPanel);
		sl_normalEvoPanel.putConstraint(SpringLayout.NORTH, lblTickssecond, 3, SpringLayout.NORTH, tpsSpinner);
		sl_normalEvoPanel.putConstraint(SpringLayout.WEST, tpsSpinner, -86, SpringLayout.EAST, normalEvoPanel);
		sl_normalEvoPanel.putConstraint(SpringLayout.EAST, tpsSpinner, -10, SpringLayout.EAST, normalEvoPanel);
		normalEvoPanel.add(tpsSpinner);
		
		JLabel lblAttach = new JLabel("Attach GUI");
		sl_normalEvoPanel.putConstraint(SpringLayout.WEST, lblAttach, 10, SpringLayout.WEST, normalEvoPanel);
		normalEvoPanel.add(lblAttach);
		
		attachGUIButton = new JButton("Attach");
		sl_normalEvoPanel.putConstraint(SpringLayout.NORTH, lblAttach, 4, SpringLayout.NORTH, attachGUIButton);
		sl_normalEvoPanel.putConstraint(SpringLayout.NORTH, attachGUIButton, 6, SpringLayout.SOUTH, tpsSpinner);
		sl_normalEvoPanel.putConstraint(SpringLayout.WEST, attachGUIButton, 0, SpringLayout.WEST, tpsSpinner);
		sl_normalEvoPanel.putConstraint(SpringLayout.EAST, attachGUIButton, 0, SpringLayout.EAST, tpsSpinner);
		normalEvoPanel.add(attachGUIButton);
		
		JPanel panel_2 = new JPanel();
		sl_normalEvoPanel.putConstraint(SpringLayout.NORTH, panel_2, 6, SpringLayout.SOUTH, lblAttach);
		sl_normalEvoPanel.putConstraint(SpringLayout.WEST, panel_2, 0, SpringLayout.WEST, lblTickssecond);
		sl_normalEvoPanel.putConstraint(SpringLayout.SOUTH, panel_2, 98, SpringLayout.SOUTH, attachGUIButton);
		sl_normalEvoPanel.putConstraint(SpringLayout.EAST, panel_2, 0, SpringLayout.EAST, tpsSpinner);
		panel_2.setBorder(new TitledBorder(null, "GUI Settings", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		normalEvoPanel.add(panel_2);
		SpringLayout sl_panel_2 = new SpringLayout();
		panel_2.setLayout(sl_panel_2);
		
		JLabel lblGamesVertically = new JLabel("Games vertically");
		sl_panel_2.putConstraint(SpringLayout.WEST, lblGamesVertically, 10, SpringLayout.WEST, panel_2);
		panel_2.add(lblGamesVertically);
		
		gamesVertSpinner = new JSpinner();
		gamesVertSpinner.setModel(new SpinnerNumberModel(3, 1, 30, 1));
		sl_panel_2.putConstraint(SpringLayout.NORTH, lblGamesVertically, 3, SpringLayout.NORTH, gamesVertSpinner);
		sl_panel_2.putConstraint(SpringLayout.NORTH, gamesVertSpinner, 0, SpringLayout.NORTH, panel_2);
		sl_panel_2.putConstraint(SpringLayout.WEST, gamesVertSpinner, -88, SpringLayout.EAST, panel_2);
		sl_panel_2.putConstraint(SpringLayout.EAST, gamesVertSpinner, -10, SpringLayout.EAST, panel_2);
		panel_2.add(gamesVertSpinner);
		
		JLabel lblGamesHorizontally = new JLabel("Games horizontally");
		sl_panel_2.putConstraint(SpringLayout.WEST, lblGamesHorizontally, 0, SpringLayout.WEST, lblGamesVertically);
		panel_2.add(lblGamesHorizontally);
		
		gamesHorSpinner = new JSpinner();
		gamesHorSpinner.setModel(new SpinnerNumberModel(7, 1, 60, 1));
		sl_panel_2.putConstraint(SpringLayout.NORTH, lblGamesHorizontally, 3, SpringLayout.NORTH, gamesHorSpinner);
		sl_panel_2.putConstraint(SpringLayout.NORTH, gamesHorSpinner, 6, SpringLayout.SOUTH, gamesVertSpinner);
		sl_panel_2.putConstraint(SpringLayout.WEST, gamesHorSpinner, 0, SpringLayout.WEST, gamesVertSpinner);
		sl_panel_2.putConstraint(SpringLayout.EAST, gamesHorSpinner, 0, SpringLayout.EAST, gamesVertSpinner);
		panel_2.add(gamesHorSpinner);
		
		JLabel lblNewLabel = new JLabel("Game scale");
		sl_panel_2.putConstraint(SpringLayout.WEST, lblNewLabel, 0, SpringLayout.WEST, lblGamesVertically);
		panel_2.add(lblNewLabel);
		
		gameScaleSpinner = new JSpinner();
		gameScaleSpinner.setModel(new SpinnerListModel(new String[] {"1.5", "1.4", "1.3", "1.2", "1.1", "1.0", "0.9", "0.8", "0.7", "0.6", "0.5", "0.4"}));
		sl_panel_2.putConstraint(SpringLayout.NORTH, lblNewLabel, 3, SpringLayout.NORTH, gameScaleSpinner);
		sl_panel_2.putConstraint(SpringLayout.NORTH, gameScaleSpinner, 8, SpringLayout.SOUTH, gamesHorSpinner);
		sl_panel_2.putConstraint(SpringLayout.WEST, gameScaleSpinner, 0, SpringLayout.WEST, gamesVertSpinner);
		sl_panel_2.putConstraint(SpringLayout.EAST, gameScaleSpinner, 0, SpringLayout.EAST, gamesVertSpinner);
		panel_2.add(gameScaleSpinner);
		
		JPanel acceleratedEvoPanel = new JPanel();
		acceleratedEvoPanel.setBorder(new TitledBorder(null, "Accelerated Evolution", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		acceleratedEvoPanel.setBounds(338, 37, 318, 205);
		getContentPane().add(acceleratedEvoPanel);
		SpringLayout sl_AcceleratedEvoPanel = new SpringLayout();
		acceleratedEvoPanel.setLayout(sl_AcceleratedEvoPanel);
		
		JLabel lblThreadsSupportedBy = new JLabel("Threads supported by current machine: ");
		sl_AcceleratedEvoPanel.putConstraint(SpringLayout.NORTH, lblThreadsSupportedBy, 10, SpringLayout.NORTH, acceleratedEvoPanel);
		sl_AcceleratedEvoPanel.putConstraint(SpringLayout.WEST, lblThreadsSupportedBy, 10, SpringLayout.WEST, acceleratedEvoPanel);
		acceleratedEvoPanel.add(lblThreadsSupportedBy);
		
		JLabel lblThreads = new JLabel(Integer.toString(Runtime.getRuntime().availableProcessors()));
		sl_AcceleratedEvoPanel.putConstraint(SpringLayout.NORTH, lblThreads, 0, SpringLayout.NORTH, lblThreadsSupportedBy);
		sl_AcceleratedEvoPanel.putConstraint(SpringLayout.WEST, lblThreads, 6, SpringLayout.EAST, lblThreadsSupportedBy);
		acceleratedEvoPanel.add(lblThreads);
		
		JSeparator separator = new JSeparator();
		sl_AcceleratedEvoPanel.putConstraint(SpringLayout.NORTH, separator, 6, SpringLayout.SOUTH, lblThreadsSupportedBy);
		sl_AcceleratedEvoPanel.putConstraint(SpringLayout.WEST, separator, 0, SpringLayout.WEST, lblThreadsSupportedBy);
		sl_AcceleratedEvoPanel.putConstraint(SpringLayout.SOUTH, separator, 8, SpringLayout.SOUTH, lblThreadsSupportedBy);
		sl_AcceleratedEvoPanel.putConstraint(SpringLayout.EAST, separator, 296, SpringLayout.WEST, acceleratedEvoPanel);
		acceleratedEvoPanel.add(separator);
		
		JLabel lblThreadsToUse = new JLabel("Threads to use :");
		sl_AcceleratedEvoPanel.putConstraint(SpringLayout.WEST, lblThreadsToUse, 0, SpringLayout.WEST, lblThreadsSupportedBy);
		acceleratedEvoPanel.add(lblThreadsToUse);
		
		threadsSpinner = new JSpinner();
		threadsSpinner.setModel(new SpinnerNumberModel(1, 1, Runtime.getRuntime().availableProcessors(), 1));
		sl_AcceleratedEvoPanel.putConstraint(SpringLayout.NORTH, lblThreadsToUse, 3, SpringLayout.NORTH, threadsSpinner);
		sl_AcceleratedEvoPanel.putConstraint(SpringLayout.NORTH, threadsSpinner, 6, SpringLayout.SOUTH, separator);
		sl_AcceleratedEvoPanel.putConstraint(SpringLayout.WEST, threadsSpinner, 0, SpringLayout.WEST, lblThreads);
		sl_AcceleratedEvoPanel.putConstraint(SpringLayout.EAST, threadsSpinner, 0, SpringLayout.EAST, separator);
		acceleratedEvoPanel.add(threadsSpinner);
		
		JLabel lblGenerationsToRun = new JLabel("Generations to run :");
		sl_AcceleratedEvoPanel.putConstraint(SpringLayout.WEST, lblGenerationsToRun, 10, SpringLayout.WEST, acceleratedEvoPanel);
		acceleratedEvoPanel.add(lblGenerationsToRun);
		
		generationsSpinner = new JSpinner();
		generationsSpinner.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
		sl_AcceleratedEvoPanel.putConstraint(SpringLayout.NORTH, lblGenerationsToRun, 3, SpringLayout.NORTH, generationsSpinner);
		sl_AcceleratedEvoPanel.putConstraint(SpringLayout.NORTH, generationsSpinner, 6, SpringLayout.SOUTH, threadsSpinner);
		sl_AcceleratedEvoPanel.putConstraint(SpringLayout.WEST, generationsSpinner, 0, SpringLayout.WEST, lblThreads);
		sl_AcceleratedEvoPanel.putConstraint(SpringLayout.EAST, generationsSpinner, 0, SpringLayout.EAST, separator);
		acceleratedEvoPanel.add(generationsSpinner);
		
		JRadioButton radioNormal = new JRadioButton("Normal Evolution");
		radioNormal.setSelected(true);
		radioNormal.setBounds(10, 7, 148, 23);
		radioNormal.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				switch (e.getStateChange()){
					case ItemEvent.SELECTED:
						setEnabledPanel(normalEvoPanel, true);
						break;
					case ItemEvent.DESELECTED:
						setEnabledPanel(normalEvoPanel, false);
						break;
				}
			}
		});
		JRadioButton radioAccelerated = new JRadioButton("Accelerated Evolution");
		radioAccelerated.setBounds(160, 7, 168, 23);
		radioAccelerated.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				switch (e.getStateChange()){
				case ItemEvent.SELECTED:
					setEnabledPanel(acceleratedEvoPanel, true);
					break;
				case ItemEvent.DESELECTED:
					setEnabledPanel(acceleratedEvoPanel, false);
					break;
				}
			}
		});
		
		evolutionRadios.add(radioNormal);
		evolutionRadios.add(radioAccelerated);
		
		getContentPane().add(radioNormal);
		getContentPane().add(radioAccelerated);

		setEnabledPanel(acceleratedEvoPanel, false);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "General Settings", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(666, 37, 318, 205);
		getContentPane().add(panel);
		SpringLayout sl_panel = new SpringLayout();
		panel.setLayout(sl_panel);
		
		JLabel lblHiddenNeuralLayers = new JLabel("Hidden Neural Layers :");
		sl_panel.putConstraint(SpringLayout.WEST, lblHiddenNeuralLayers, 10, SpringLayout.WEST, panel);
		panel.add(lblHiddenNeuralLayers);
		
		hiddenLayersSpinner = new JSpinner();
		sl_panel.putConstraint(SpringLayout.NORTH, hiddenLayersSpinner, 10, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, hiddenLayersSpinner, 98, SpringLayout.EAST, lblHiddenNeuralLayers);
		sl_panel.putConstraint(SpringLayout.EAST, hiddenLayersSpinner, -10, SpringLayout.EAST, panel);
		hiddenLayersSpinner.setModel(new SpinnerNumberModel(5, 0, 50, 1));
		sl_panel.putConstraint(SpringLayout.NORTH, lblHiddenNeuralLayers, 3, SpringLayout.NORTH, hiddenLayersSpinner);
		panel.add(hiddenLayersSpinner);
		
		JLabel lblNodesPerHidden = new JLabel("Nodes per Hidden Neural Layer :");
		sl_panel.putConstraint(SpringLayout.WEST, lblNodesPerHidden, 10, SpringLayout.WEST, panel);
		panel.add(lblNodesPerHidden);
		
		nodesHiddenLayerSpinner = new JSpinner();
		sl_panel.putConstraint(SpringLayout.WEST, nodesHiddenLayerSpinner, 51, SpringLayout.EAST, lblNodesPerHidden);
		sl_panel.putConstraint(SpringLayout.EAST, nodesHiddenLayerSpinner, -10, SpringLayout.EAST, panel);
		nodesHiddenLayerSpinner.setModel(new SpinnerNumberModel(10, 1, 50, 1));
		sl_panel.putConstraint(SpringLayout.NORTH, lblNodesPerHidden, 3, SpringLayout.NORTH, nodesHiddenLayerSpinner);
		sl_panel.putConstraint(SpringLayout.NORTH, nodesHiddenLayerSpinner, 6, SpringLayout.SOUTH, hiddenLayersSpinner);
		panel.add(nodesHiddenLayerSpinner);
		
		JLabel lblNumberOfSpecimens = new JLabel("Number of specimens");
		sl_panel.putConstraint(SpringLayout.WEST, lblNumberOfSpecimens, 10, SpringLayout.WEST, panel);
		panel.add(lblNumberOfSpecimens);
		
		specimensSpinner = new JSpinner();
		sl_panel.putConstraint(SpringLayout.WEST, specimensSpinner, 105, SpringLayout.EAST, lblNumberOfSpecimens);
		sl_panel.putConstraint(SpringLayout.EAST, specimensSpinner, -10, SpringLayout.EAST, panel);
		specimensSpinner.setModel(new SpinnerNumberModel(20, 2, 100, 1));
		sl_panel.putConstraint(SpringLayout.NORTH, lblNumberOfSpecimens, 3, SpringLayout.NORTH, specimensSpinner);
		sl_panel.putConstraint(SpringLayout.NORTH, specimensSpinner, 6, SpringLayout.SOUTH, nodesHiddenLayerSpinner);
		panel.add(specimensSpinner);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Controls", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(10, 253, 318, 157);
		getContentPane().add(panel_1);
		SpringLayout sl_panel_1 = new SpringLayout();
		panel_1.setLayout(sl_panel_1);
		
		startButton = new JButton("Start");
		sl_panel_1.putConstraint(SpringLayout.NORTH, startButton, 10, SpringLayout.NORTH, panel_1);
		sl_panel_1.putConstraint(SpringLayout.WEST, startButton, 10, SpringLayout.WEST, panel_1);
		panel_1.add(startButton);
		
		stopButton = new JButton("Stop");
		sl_panel_1.putConstraint(SpringLayout.NORTH, stopButton, 0, SpringLayout.NORTH, startButton);
		sl_panel_1.putConstraint(SpringLayout.WEST, stopButton, 6, SpringLayout.EAST, startButton);
		panel_1.add(stopButton);
		
		showGraphsButton = new JButton("Show Graphs");
		sl_panel_1.putConstraint(SpringLayout.NORTH, showGraphsButton, 0, SpringLayout.NORTH, startButton);
		sl_panel_1.putConstraint(SpringLayout.WEST, showGraphsButton, 6, SpringLayout.EAST, stopButton);
		panel_1.add(showGraphsButton);
		
		JLabel lblRunning = new JLabel("Running ");
		sl_panel_1.putConstraint(SpringLayout.NORTH, lblRunning, 22, SpringLayout.SOUTH, startButton);
		sl_panel_1.putConstraint(SpringLayout.WEST, lblRunning, 0, SpringLayout.WEST, startButton);
		panel_1.add(lblRunning);
		
		indicatorLabel = new JLabel("\u2022");
		sl_panel_1.putConstraint(SpringLayout.NORTH, indicatorLabel, 16, SpringLayout.SOUTH, stopButton);
		sl_panel_1.putConstraint(SpringLayout.WEST, indicatorLabel, 17, SpringLayout.EAST, lblRunning);
		sl_panel_1.putConstraint(SpringLayout.SOUTH, indicatorLabel, 39, SpringLayout.SOUTH, stopButton);
		sl_panel_1.putConstraint(SpringLayout.EAST, indicatorLabel, 29, SpringLayout.EAST, lblRunning);
		indicatorLabel.setFont(new Font("Tahoma", Font.PLAIN, 27));
		indicatorLabel.setForeground(Color.RED);
		panel_1.add(indicatorLabel);
		
		JLabel lblGeneration = new JLabel("Generation :");
		sl_panel_1.putConstraint(SpringLayout.NORTH, lblGeneration, 6, SpringLayout.SOUTH, lblRunning);
		sl_panel_1.putConstraint(SpringLayout.WEST, lblGeneration, 0, SpringLayout.WEST, startButton);
		panel_1.add(lblGeneration);
		
		generationLabel = new JLabel("0");
		sl_panel_1.putConstraint(SpringLayout.WEST, generationLabel, 0, SpringLayout.WEST, stopButton);
		sl_panel_1.putConstraint(SpringLayout.SOUTH, generationLabel, 0, SpringLayout.SOUTH, lblGeneration);
		panel_1.add(generationLabel);
		
		taskPanel.setBounds(338, 254, 646, 156);
		getContentPane().add(taskPanel);
		setVisible(true);
	}
	
	private void setEnabledPanel(JPanel panel, boolean enabled){
		//System.out.println("Setting " + ((TitledBorder) panel.getBorder()).getTitle() + ", " + enabled);
		for (Component c : panel.getComponents()){
			if (c instanceof JPanel){
				JPanel cont = (JPanel) c;
				setEnabledPanel(cont, enabled);
			} else {
				c.setEnabled(enabled);
			}
		}
	}
	
	public void setRunningIndicator(boolean running){
		indicatorLabel.setForeground((running) ? Color.GREEN : Color.RED);
	}
	
	public void setGenerationNumber(int generation){
		generationLabel.setText(Integer.toString(generation));
	}
	
	public void bumpGenerationNumber(){
		generationLabel.setText(Integer.toString(Integer.parseInt(generationLabel.getText()) + 1)); 
	}
	
	public int getTPS(){
		return (int) tpsSpinner.getValue();
	}
	
	public int getNumThreads(){
		return (int) threadsSpinner.getValue();
	}
	
	public int getGensToRun(){
		return (int) generationsSpinner.getValue();
	}
	
	public int getNumSpecimens(){
		return (int) specimensSpinner.getValue();
	}
	
	public int getNumHiddenLayers(){
		return (int) hiddenLayersSpinner.getValue();
	}
	
	public int getNodesPerLayer(){
		return (int) nodesHiddenLayerSpinner.getValue();
	}
	
	public int getGamesVertically(){
		return (int) gamesVertSpinner.getValue();
	}
	
	public int getGamesHorizontally(){
		return (int) gamesHorSpinner.getValue();
	}
	
	public float getGamesScale(){
		return Float.parseFloat((String) gameScaleSpinner.getValue());
	}
	
	public void setStartActionListener(ActionListener listener){
		startButton.addActionListener(listener);
	}
	
	public void setStopActionListener(ActionListener listener){
		stopButton.addActionListener(listener);
	}
	
	public void setShowGraphsActionListener(ActionListener listener){
		showGraphsButton.addActionListener(listener);
	}
	
	public void setAttachActionListener(ActionListener listener){
		attachGUIButton.addActionListener(listener);
	}
}