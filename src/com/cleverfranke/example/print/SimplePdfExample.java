package com.cleverfranke.example.print;

import com.cleverfranke.util.FileSystem;
import com.cleverfranke.util.print.*;

import processing.core.PApplet;

public class SimplePdfExample extends PApplet {
	
	public void setup() { 

		// Determine where to write the PDF file
		String pdfPath = FileSystem.getApplicationPath("output/simplepdf.pdf");
		System.out.println("Printing to " + pdfPath);

		// Create PDF object (100x100 points)
		Pdf pdf  = new Pdf(this, 100, 100, pdfPath);
		pdf.beginDraw();
		
		// Draw background color light grey
		pdf.background(230);
		
		// Draw red cross across canvas
		pdf.stroke(255, 0, 0);
		pdf.line(0, 0, 100, 100);
		pdf.line(0, 100, 100, 0);

		// Finalize pdf
		pdf.dispose();
		
		// End application
		System.out.println("Pdf writing done");
		exit();
	}
	
	public static void main(String[] args) {
		PApplet.main(SimplePdfExample.class.getName());
	}
	
}
