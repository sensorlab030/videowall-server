package com.cleverfranke.ledwall.ui;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.cleverfranke.ledwall.AnimationManager;
import com.cleverfranke.ledwall.AnimationManager.AnimationEntry;

@SuppressWarnings("serial")
public class MainWindow extends JFrame implements AnimationManager.AnimationEventListener {
	
	private DefaultListModel<AnimationManager.AnimationEntry> animationModel;
	private JList<AnimationManager.AnimationEntry> animationList;
	
	public MainWindow() {
		
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
		getContentPane().add(listScroller);
		
		pack();
		setVisible(true);
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
