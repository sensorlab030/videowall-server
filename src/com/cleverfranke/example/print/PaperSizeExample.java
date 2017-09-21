package com.cleverfranke.example.print;

import com.cleverfranke.util.FileSystem;
import com.cleverfranke.util.print.*;

import processing.core.PApplet;

public class PaperSizeExample extends PApplet {
	
	public void setup() {

		// Determine where to write the PDF file
		String pdfPath = FileSystem.getApplicationPath("output/papersize.pdf");
		System.out.println("Printing to " + pdfPath);

		// Create PDF object (A4 landscape)
		Pdf pdf  = Pdf.createPdf(this, PaperSize.A4, false, pdfPath);
		pdf.beginDraw();
		
		// Draw background color light grey
		pdf.background(230);
		
		// Draw red cross across canvas
		pdf.stroke(255, 0, 0);
		pdf.line(0, 0, PaperSize.A4.getLandscapeWidth(), PaperSize.A4.getLandscapeHeight());
		pdf.line(0, PaperSize.A4.getLandscapeHeight(), PaperSize.A4.getLandscapeWidth(), 0);
		
		// Finalize pdf
		pdf.dispose();
		
		// End application
		System.out.println("Pdf writing done");
		exit();
	}
	
	public static void main(String[] args) {
		PApplet.main(PaperSizeExample.class.getName());
	}
	
}
