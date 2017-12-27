package com.cleverfranke.ledwall.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import com.cleverfranke.ledwall.animation.VideoAnimation;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {
	
	private DefaultListModel<AnimationModelEntry> animationModel;
	private JList<AnimationModelEntry> animationList;
	
	public MainWindow() {
		initAnimationList();
		
		// Initialize frame
		setTitle("Configuration panel");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		// Set size
		Dimension d = new Dimension(640, 480);
		setSize(d);
		setMinimumSize(d);
		setResizable(true);
		
		animationList = new JList<AnimationModelEntry>(animationModel);
		animationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		animationList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (renderer instanceof JLabel && value instanceof AnimationModelEntry) {
                    // Here value will be of the Type 'CD'
                    ((JLabel) renderer).setText(((AnimationModelEntry) value).label);
                }
                return renderer;
            }
        });
		
		JScrollPane listScroller = new JScrollPane(animationList);
		listScroller.setPreferredSize(new Dimension(250, 80));
		
		
		
//		final JPanel root = new JPanel(new GridLayout(1, 1));
//		root.add(listScroller);
		getContentPane().add(listScroller);
		
		pack();
		setVisible(true);
	}
	
	private void initAnimationList() {
		
		animationModel = new DefaultListModel<AnimationModelEntry>();
				
		// Add animations
		animationModel.addElement(new AnimationModelEntry("beachball", "Beach Ball"));
		animationModel.addElement(new AnimationModelEntry("linewave", "Line Wave"));
		animationModel.addElement(new AnimationModelEntry("spectrumanalyzer", "Spectrum Analyzer"));
		
		// Add videos
		for (File f : VideoAnimation.getVideoFileList()) {
			String id = "video_" + f.getName();
			String label = f.getName() + " (Video)";
			animationModel.addElement(new AnimationModelEntry(id, label));
		}
		
	}
	
}
