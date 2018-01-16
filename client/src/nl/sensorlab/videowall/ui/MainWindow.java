package nl.sensorlab.videowall.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nl.sensorlab.videowall.AnimationManager;
import nl.sensorlab.videowall.LedWallApplication;
import nl.sensorlab.videowall.AnimationManager.AnimationEntry;

@SuppressWarnings("serial")
public class MainWindow extends JFrame implements AnimationManager.AnimationEventListener {
	
	private DefaultListModel<AnimationManager.AnimationEntry> animationModel;
	private JList<AnimationManager.AnimationEntry> animationList;
	
	public MainWindow(LedWallApplication application) {
		
		// Initialize frame
		setTitle("Configuration panel");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		// Set size
		Dimension d = new Dimension(640, 480);
		setSize(d);
		setMinimumSize(d);
		setResizable(true);
		
		// Load model
		animationModel = new DefaultListModel<>();
		for (AnimationManager.AnimationEntry e : AnimationManager.getInstance().getAvailableAnimations()) {
			animationModel.addElement(e);
		}
		
		// Create list
		animationList = new JList<>(animationModel);
		animationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		animationList.setCellRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (renderer instanceof JLabel && value instanceof AnimationManager.AnimationEntry) {
					((JLabel) renderer).setText(((AnimationManager.AnimationEntry) value).getLabel());
				}
				return renderer;
			}
		});
		animationList.getSelectionModel().addListSelectionListener(new AnimationListSelectionHandler(animationList));
		
		// Subscribe to animation events
		AnimationManager.getInstance().addListener(this);

		// Add scroll pane for list
		JScrollPane listScroller = new JScrollPane(animationList);
		listScroller.setPreferredSize(new Dimension(250, 80));
		
		JSlider brighnessSlider = new JSlider(JSlider.VERTICAL, 0, 255, 255);
		brighnessSlider.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				application.setWallBrightness((int) source.getValue());
			}
		});
		
		// Add controls to content pane
		Container contentPane = getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.LINE_AXIS));
		contentPane.add(listScroller, BorderLayout.CENTER);
		contentPane.add(brighnessSlider, BorderLayout.PAGE_END);
		
		pack();
		open();
		
	}
	
	public void toggle() {
		if (!this.isVisible()) {
			open();
		} else {
			close();
		}
	}
	
	public void open () {
		this.setVisible(true);
	}
	
	public void close () {
		this.setVisible(false);
	}
	
	@Override
	public void onCurrentAnimationChanged(int index) {
		animationList.setSelectedIndex(index);
	}
	
	class AnimationListSelectionHandler implements ListSelectionListener {
		
		private JList<AnimationEntry> sourceList;
		
		public AnimationListSelectionHandler(JList<AnimationEntry> sourceList) {
			this.sourceList = sourceList;
		}
		
		public void valueChanged(ListSelectionEvent e) {
			if (!e.getValueIsAdjusting()) {
				AnimationManager.getInstance().startAnimation(sourceList.getSelectedIndex());
			}
		}
	}



}
