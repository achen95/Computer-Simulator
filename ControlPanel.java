package project;

import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class ControlPanel implements Observer {
	private ViewsOrganizer view;
	private JButton stepButton = new JButton("Step");
	private JButton clearButton = new JButton("Clear");
	private JButton runButton = new JButton("Run/Pause");
	private JButton reloadButton = new JButton("Reload");
	
	public ControlPanel(ViewsOrganizer gui) {
		view = gui;
		gui.addObserver(this);
	}
	
	public JComponent createControlDisplay() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 0));
		stepButton.addActionListener(e -> view.step());
		panel.add(stepButton);
		clearButton.addActionListener(e -> view.clearJob());
		panel.add(clearButton);
		runButton.addActionListener(e -> view.toggleAutoStep());
		panel.add(runButton);
		reloadButton.addActionListener(e -> view.reload());
		panel.add(reloadButton);
		
		JSlider slider = new JSlider(5,1000);
		slider.addChangeListener(e -> view.setPeriod(slider.getValue()));
		panel.add(slider);
		
		return panel;
	}
	
	public void update(Observable arg0, Object arg1) {
		runButton.setEnabled(view.getCurrentState().getRunPauseActive());
		clearButton.setEnabled(view.getCurrentState().getClearActive());
		reloadButton.setEnabled(view.getCurrentState().getReloadActive());
		stepButton.setEnabled(view.getCurrentState().getStepActive());
	}
}
