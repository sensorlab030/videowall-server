package com.cleverfranke.ledwall.animation;

import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.cleverfranke.util.Settings;

import ddf.minim.AudioInput;
import ddf.minim.Minim;
import de.looksgood.ani.Ani;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.serial.Serial;

public class OpeningAnimation extends BaseCanvasAnimation {
	
	private static final int LED_COUNT = 50;
	private static final int LED_LIMITED_RANGE = 40;
	
	// Settings
	private float soundSmoothingRatio = 0f;
	private float soundAmplification = 1;
	private boolean outputEnabled = false;
	private boolean triggerEnabled = false;
	
	// Current state
	private float rawVolume;		// Current raw sound intensity [0, 1]
	private float amplifiedVolume; 	// Current amplified sound intensity
	private int outputVolume = 0;	// Current mapped output in number of leds [0, 50]
	private boolean triggered = false;
	
	// UI and sound analysis objects
	private Minim minim;
	private AudioInput in;
	private AudioUI ui;
	private Serial serialPort;
	
	private ArrayList<AnimationCircle> circles = new ArrayList<>();
	
	public OpeningAnimation(PApplet applet) {
		super(applet);
		ui = new AudioUI();
		
		try {
			serialPort = new Serial(applet, Settings.getValue("soundstickComPort"));
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	protected void drawCanvasAnimationFrame(PGraphics g) {
		
		g.background(0);
		
		// Calculate volumes 
		if (in != null) {
			rawVolume = soundSmoothingRatio * rawVolume + (1.f - soundSmoothingRatio) * in.mix.level();
			amplifiedVolume = Math.min(rawVolume * soundAmplification, 1);
		}
		
		if (triggered) {
			
			drawOpeningAnimationFrame(g);
			outputVolume = 50;
		
		} else {
			
			if (outputEnabled) {
				if (triggerEnabled) {
					// Actual output, enable full range
					outputVolume = (int) PApplet.map(amplifiedVolume, 0f, 1f, 0f, (float) LED_COUNT);
				} else {
					// Fake output, enable decreased range
					outputVolume = (int) PApplet.map(amplifiedVolume, 0f, 1f, 0f, (float) LED_LIMITED_RANGE);	
				}
			} else {
				// Output disabled
				outputVolume = 0;
			}
			
			// Trigger
			if (amplifiedVolume > .9f) {
				trigger();
			}
			
		} 
		
		// Set UI values
		ui.setSoundMonitors(rawVolume, amplifiedVolume, outputVolume);
		
		// Send output volume to Arduino
		if (serialPort != null) {
			serialPort.write(outputVolume & (0xff));
		}
		
	}
	
	private void drawOpeningAnimationFrame(PGraphics g)  {
		g.pushMatrix();

		g.ellipseMode(PConstants.CENTER);
		g.translate(g.width / 2, g.height);
		
		for (AnimationCircle c : circles) {
			c.draw(g);
		}
		
		g.popMatrix();
	}
	
	public void trigger() {
		
		if (!triggerEnabled) {
			return;
		}
		
		// Set UI triggered
		ui.setTriggered();
		
		// Create circles
		for(int i = 0; i < 3; i++) {
			circles.add(new AnimationCircle((float) i * AnimationCircle.DELAY_OFFSET));
		}
		
		// Set triggered flag
		triggered = true;
		
	}
	
	@Override
	public void isStarting() { 
		minim = new Minim(applet);
		in = minim.getLineIn(Minim.MONO);
		if (in != null) {
			in.disableMonitoring();
		}
		
		// Reset all
		triggered = false;
		circles.clear();
		ui.reset();
		
		// Show UI
		ui.setVisible(true);
		
	}
	
	@Override
	public void isStopping() {
		if (in != null) {
			in.close();
			in = null;
		}
		minim.stop();
		minim.dispose();
		minim = null;
		
		ui.setVisible(false);
	}
	
	
	@SuppressWarnings("serial")
	private class AudioUI extends JFrame implements KeyListener {
		
		private JProgressBar rawVolumeMonitor;
		private JProgressBar amplifiedVolumeMonitor;
		private JProgressBar outputVolumeMonitor;
		
		private JSlider amplificationSlider;
		private JSlider smoothingSlider;
		
		private JCheckBox outputEnabledCheckbox;
		private JCheckBox triggerEnabledCheckbox;
		
		private JLabel warningLabel = new JLabel("Safe", JLabel.CENTER);
		
		public AudioUI() {
			
			// Initialize frame
			setTitle("Opening UI");
			setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			setResizable(true);
			addKeyListener(this);
			setFocusable(true);
			
			// Init content pane layout
			Container contentPane = getContentPane();
			contentPane.setLayout(new GridBagLayout());
			
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.insets = new Insets(10, 10, 10, 10);
			
			// Raw volume
			JLabel rawVolumeLabel = new JLabel("Raw volume", JLabel.RIGHT);
			c.gridx = 0;
			c.gridy = 0;
			contentPane.add(rawVolumeLabel, c);
			
			rawVolumeMonitor = new JProgressBar(0, 255);
			rawVolumeMonitor.setValue((int) rawVolume);
			c.gridx = 1;
			c.gridy = 0;
			contentPane.add(rawVolumeMonitor, c);
			
			// Amplified volume
			JLabel amplifiedVolumeLabel = new JLabel("Amplified volume", JLabel.RIGHT);
			c.gridx = 0;
			c.gridy = 1;
			contentPane.add(amplifiedVolumeLabel, c);
			
			amplifiedVolumeMonitor = new JProgressBar(0, 255);
			amplifiedVolumeMonitor.setValue((int) amplifiedVolume);
			c.gridx = 1;
			c.gridy = 1;
			contentPane.add(amplifiedVolumeMonitor, c);
			
			// Output volume (leds)
			JLabel outputVolumeLabel = new JLabel("Output volume (LEDS)", JLabel.RIGHT);
			c.gridx = 0;
			c.gridy = 2;
			contentPane.add(outputVolumeLabel, c);
			
			outputVolumeMonitor = new JProgressBar(0, LED_COUNT);
			outputVolumeMonitor.setValue(outputVolume);
			c.gridx = 1;
			c.gridy = 2;
			contentPane.add(outputVolumeMonitor, c);
			
			// Separator
			c.gridx = 0;
			c.gridy = 3;
			c.gridwidth = 2;
			contentPane.add(new JSeparator(), c);
			
			// Amplification
			JLabel amplificationSliderLabel = new JLabel("Amplification", JLabel.RIGHT);
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridwidth = 1;
			c.gridx = 0;
			c.gridy = 4;
			contentPane.add(amplificationSliderLabel, c);
			
			amplificationSlider = new JSlider(1, 40);
			amplificationSlider.setValue((int) soundAmplification);
			amplificationSlider.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					soundAmplification = (float) amplificationSlider.getValue();
				}
			});
			
			c.gridx = 1;
			c.gridy = 4;
			contentPane.add(amplificationSlider, c);
			
			// Smoothing
			JLabel smootingSliderLabel = new JLabel("Smoothing", JLabel.RIGHT);
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 0;
			c.gridy = 5;
			contentPane.add(smootingSliderLabel, c);
			
			smoothingSlider = new JSlider(0, 100);
			smoothingSlider.setValue((int) (soundSmoothingRatio * (float) smoothingSlider.getMaximum()));
			smoothingSlider.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					soundSmoothingRatio = (float) smoothingSlider.getValue() / (float) smoothingSlider.getMaximum();
				}
			});
			c.gridx = 1;
			c.gridy = 5;
			contentPane.add(smoothingSlider, c);
			
			// Output toggle
			JLabel outputToggleLabel = new JLabel("Output enabled (A)", JLabel.RIGHT);
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 0;
			c.gridy = 6;
			contentPane.add(outputToggleLabel, c);
			
			outputEnabledCheckbox = new JCheckBox();
			outputEnabledCheckbox.setSelected(outputEnabled);
			outputEnabledCheckbox.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					outputEnabled = outputEnabledCheckbox.isSelected();
				}
			});
			c.gridx = 1;
			c.gridy = 6;
			contentPane.add(outputEnabledCheckbox, c);
			
			// Trigger toggle
			JLabel triggerToggleLabel = new JLabel("Trigger enabled (S)", JLabel.RIGHT);
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 0;
			c.gridy = 7;
			contentPane.add(triggerToggleLabel, c);
			
			triggerEnabledCheckbox = new JCheckBox();
			triggerEnabledCheckbox.setSelected(triggerEnabled);
			triggerEnabledCheckbox.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					triggerEnabled = triggerEnabledCheckbox.isSelected();
					warningLabel.setText(triggerEnabled ? "LIVE!" : "Safe");
				}
			});
			c.gridx = 1;
			c.gridy = 7;
			contentPane.add(triggerEnabledCheckbox, c);
			
			// Separator
			c.gridx = 0;
			c.gridy = 8;
			c.gridwidth = 2;
			contentPane.add(new JSeparator(), c);
			
			// LIVE 
			warningLabel.setSize(400, 100);
			warningLabel.setFont(new Font(warningLabel.getFont().getName(), Font.PLAIN, 40));
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridwidth = 2;
			c.gridx = 0;
			c.gridy = 9;
			contentPane.add(warningLabel, c);
			
			pack();
		}
		
		public void setSoundMonitors(float rawVolume, float amplifiedVolume, int outputVolume) {
			rawVolumeMonitor.setValue(Math.round(rawVolume * rawVolumeMonitor.getMaximum()));
			amplifiedVolumeMonitor.setValue(Math.round(amplifiedVolume * rawVolumeMonitor.getMaximum()));
			outputVolumeMonitor.setValue(outputVolume);
		}

		public void reset() {
			outputEnabledCheckbox.setSelected(false);
			triggerEnabledCheckbox.setSelected(false);
			warningLabel.setText("Safe");
		}
		
		public void setTriggered() {
			warningLabel.setText("TRIGGERED");
		}
		
		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_A:
					outputEnabledCheckbox.setSelected(!outputEnabledCheckbox.isSelected());
					break;
				case KeyEvent.VK_S:
					triggerEnabledCheckbox.setSelected(!triggerEnabledCheckbox.isSelected());
					break;
				case KeyEvent.VK_SPACE:
					trigger();
					break;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {}

		@Override
		public void keyTyped(KeyEvent e) {}
		
	}
	
	private class AnimationCircle {

		public final static float DELAY_OFFSET = .5f;
		
		private final static float TARGET_SIZE = 500;
		private final static float ANIM_DURATION = 2.5f;
		private final static float START_STROKEWEIGHT = 10f;
		private final static float TARGET_STROKEWEIGHT = 40f;

		private Ani sizeAanim;
		private Ani strokeAnim;
		
		private float strokeWeight;
		private float size;
		
		public AnimationCircle(float delay) {
			
			sizeAanim = new Ani(this, ANIM_DURATION, delay, "size", TARGET_SIZE);
			sizeAanim.setEasing(Ani.CUBIC_OUT);
			sizeAanim.repeat();
			
			strokeAnim = new Ani(this, ANIM_DURATION, delay, "strokeWeight", TARGET_STROKEWEIGHT);
			strokeAnim.setBegin(START_STROKEWEIGHT);
			strokeAnim.setEasing(Ani.CUBIC_OUT);
			strokeAnim.repeat();
			
		}
		
		public void draw(PGraphics g) {
			g.noFill();
			g.strokeWeight(strokeWeight);
			g.stroke(255, 255, 255);
			g.ellipse(0, 0, size, size);
		}
		
	}
	
}
