package com.cleverfranke.util.print;

import com.lowagie.text.pdf.PdfGState;
import com.lowagie.text.pdf.PdfName;

public enum BlendMode {
	Burn(PdfGState.BM_COLORBURN),
	Dodge(PdfGState.BM_COLORDODGE),
	Compatible(PdfGState.BM_COMPATIBLE),
	Darken(PdfGState.BM_DARKEN),
	Difference(PdfGState.BM_DIFFERENCE),
	Exclusion(PdfGState.BM_EXCLUSION),
	HardLight(PdfGState.BM_HARDLIGHT),
	Lighten(PdfGState.BM_LIGHTEN),
	Multiply(PdfGState.BM_MULTIPLY),
	Normal(PdfGState.BM_NORMAL),
	Overlay(PdfGState.BM_OVERLAY),
	Screen(PdfGState.BM_SCREEN),
	SoftLight(PdfGState.BM_SOFTLIGHT);
	
	private final PdfName iTextBlendMode;
	
	private BlendMode(PdfName iTextBlendMode) {
		this.iTextBlendMode = iTextBlendMode;
	}
	
	public PdfName getITextBlendMode() {
		return iTextBlendMode;
	}
	
}