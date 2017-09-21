package com.cleverfranke.example.print;

import com.cleverfranke.util.FileSystem;
import com.cleverfranke.util.print.*;

import processing.core.PApplet;

public class BlendModesExample extends PApplet {
	
	public void setup() {

		// Determine where to write the PDF file
		String pdfPath = FileSystem.getApplicationPath("output/blendmodes.pdf");
		System.out.println("Printing to " + pdfPath);

		// Create PDF object (100x100 points)
		Pdf pdf  = new Pdf(this, 100, 100, pdfPath);
		pdf.beginDraw();
		pdf.noStroke();
		
		// Set blend mode to multiply. Available blend modes are:
		// - Burn
		// - Dodge
		// - Compatible
		// - Darken
		// - Difference
		// - Exclusion
		// - HardLight
		// - Lighten
		// - Multiply
		// - Normal
		// - Overlay
		// - Screen
		// - SoftLight
		pdf.setFillBlendMode(BlendMode.Multiply, 0.5f);
		
		// Draw magenta circle
		pdf.fill(255, 0, 255);
		pdf.ellipse(25, 50, 80, 80);
		
		// Draw overlapping cyan circle
		pdf.fill(0, 255, 255);
		pdf.ellipse(75, 50, 80, 80);
		
		// Reset blend mode
		pdf.setFillBlendMode(BlendMode.Normal);
		
		// Draw small white circle in the center
		pdf.fill(255);
		pdf.ellipse(50, 50, 30, 30);
		
		// Finalize pdf
		pdf.dispose();
		
		// End application
		System.out.println("Pdf writing done");
		exit();
	}
	
	public static void main(String[] args) {
		PApplet.main(BlendModesExample.class.getName());
	}
}
