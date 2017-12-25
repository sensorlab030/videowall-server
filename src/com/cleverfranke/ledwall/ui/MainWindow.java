package com.cleverfranke.ledwall.ui;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainWindow extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MainWindow() {
		// Initialize frame
		setTitle("Configuration panel");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		// Set size
		Dimension d = new Dimension(640, 480);
		setSize(d);
		setMinimumSize(d);
		setResizable(true);
		
		
		pack();
		setVisible(true);
	}

}
