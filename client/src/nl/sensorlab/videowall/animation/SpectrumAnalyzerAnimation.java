package nl.sensorlab.videowall.animation;

import ddf.minim.AudioInput;
import ddf.minim.Minim;
import ddf.minim.analysis.FFT;
import processing.core.PApplet;
import processing.core.PGraphics;

public class SpectrumAnalyzerAnimation extends BaseCanvasAnimation {

	private Minim minim;
	private AudioInput in;
	private FFT fft;
	
	private float[] prevVals;
	private int relevantBandCount;
	
	public SpectrumAnalyzerAnimation(PApplet applet) {
		super(applet, DEFAULT_SCALE, CANVAS_MODE_2D);
		minim = new Minim(this);
		
		in = minim.getLineIn();
		if (in == null) {
			System.err.println("No Audio input found");
			return;
		}

		fft = new FFT(in.bufferSize(), in.sampleRate());
		fft.logAverages(200, 10);

		relevantBandCount = fft.specSize() / 2;
		prevVals = new float[relevantBandCount];

	}

	@Override
	protected void drawCanvasAnimationFrame(PGraphics g) {
		
		if (in == null) {
			g.background(0);
			return;
		}
		
		// perform a forward FFT on the samples in input buffer
		fft.forward(in.mix);
		
		g.background(0);
		g.translate(0, g.height / 2);
		
		// Waveform
		g.noStroke();
		g.fill(0, 0, 255);
		float amplitude = 200;
		float offset = g.height / 3;
		for (int i = 0; i < in.bufferSize() - 1; i++) {

			float lVal = in.left.get(i) * amplitude;
			float rVal = in.right.get(i) * amplitude;

			g.ellipse(i, -offset + lVal, 2, 2);
			g.ellipse(i, offset + rVal, 2, 2);
			
		}

		float barWidth = (float) g.width / (float) relevantBandCount;

		// Spectrum
		g.noStroke();
		g.fill(255, 0, 0, 100);
		for (int i = 0; i < relevantBandCount; i++) {
			float rawVal = dB(fft.getBand(i));

			float avgVal = (prevVals[i] > 0) ? 0.2f * rawVal + 0.8f * prevVals[i] : rawVal;

			float barHeight = PApplet.map(avgVal, 0, 100, 10, g.height * 2);
			g.rect(i * barWidth, 0, barWidth, -barHeight);
			g.rect(i * barWidth, 0, barWidth, barHeight);

			prevVals[i] = avgVal;
		}

	}
	
	public void isStarting() {
		if (in != null) {
			in.enableMonitoring();
		}
	}
	
	public void isStopping() {
		if (in != null) {
			in.disableMonitoring();
		}
	}
	
	static float dB(float x) {
		if (x == 0) {
			return 0;
		} else {
			return 10 * (float) Math.log10(x);
		}
	}

}
