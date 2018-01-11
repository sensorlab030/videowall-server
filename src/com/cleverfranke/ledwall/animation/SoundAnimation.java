package com.cleverfranke.ledwall.animation;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

import java.awt.Rectangle;

import com.cleverfranke.ledwall.walldriver.WallGeometry;
import com.cleverfranke.util.PColor;
import ddf.minim.*;
import ddf.minim.analysis.*;

public class SoundAnimation extends BaseCanvas3dAnimation {

	Minim minim;
	AudioInput song;
//	AudioPlayer song;
	FFT fft;
	BeatDetect beat;
	WallGeometry wall = WallGeometry.getInstance();

	// Variables qui d�finissent les "zones" du spectre
	// Par exemple, pour les basses, on prend seulement les premi�res 4% du spectre
	// total
	float specLow = 0.03f; // 3%
	float specMid = 0.125f; // 12.5%
	float specHi = 0.20f; // 20%

	// Il reste donc 64% du spectre possible qui ne sera pas utilis�.
	// Ces valeurs sont g�n�ralement trop hautes pour l'oreille humaine de toute
	// facon.

	// Valeurs de score pour chaque zone
	float scoreLow = 0;
	float scoreMid = 0;
	float scoreHi = 0;

	// Valeur pr�c�dentes, pour adoucir la reduction
	float oldScoreLow = scoreLow;
	float oldScoreMid = scoreMid;
	float oldScoreHi = scoreHi;

	// Valeur d'adoucissement
	float scoreDecreaseRate = 25;

	// Cubes qui apparaissent dans l'espace
	int nbCubes;
	Cube[] cubes;

	// Lignes qui apparaissent sur les cot�s
	int nbMurs = 4;
	Mur[] murs;

	float width, height;
	int bgColor = PColor.color(0, 0, 0);
	int squareColor = PColor.color(0, 0, 0);

	// Beating shapes
	float radiusTopLeft = 0;
	float radiusBottomRight = 0;
	float radiusBottomLeft = 0;
	float radiusTopRight = 0;

	int[] beatingShapesColors = new int[4];
	boolean hasSetColors = false;

	// Roel's animation
	float diam, xpos, ypos;
	float hue;
	int color;
	int i = 0;

	int currentFrame = 0;

	public SoundAnimation(PApplet applet) {
		super(applet);
		// Charger la librairie minim
		minim = new Minim(applet);

		// Charger la chanson
//		song = minim.loadFile(FileSystem.getApplicationPath("jonas_mix.mov"));
		song = minim.getLineIn(Minim.MONO);
		// Cr�er l'objet FFT pour analyser la chanson
		
		if (song != null) {
			fft = new FFT(song.bufferSize(), song.sampleRate());
		}

		// New beat detection
		beat = new BeatDetect();

		// Un cube par bande de fr�quence
		nbCubes = 5; // (int) (fft.specSize() * specHi);
		cubes = new Cube[nbCubes];

		// Autant de murs qu'on veux
		murs = new Mur[nbMurs];

		Rectangle geom = getGeometry();

		width = geom.width;
		height = geom.height;

		diam = 0;
		xpos = width/2;
		ypos = height/2;
		color = PColor.hsb((float) Math.random(), 1f, 1f);
		
		
		// Cr�er tous les objets
		// Cr�er les objets cubes
		for (int i = 0; i < nbCubes; i++) {
			cubes[i] = new Cube();
		}

		// Cr�er les objets murs
		// Murs gauches
		for (int i = 0; i < nbMurs; i += 4) {
			murs[i] = new Mur(0, height / 2, 20, height, 5);
		}

		// Murs droits
		for (int i = 1; i < nbMurs; i += 4) {
			murs[i] = new Mur(width, height / 2, 20, height, 5);
		}

		// Murs bas
		for (int i = 2; i < nbMurs; i += 4) {
			murs[i] = new Mur(width / 2, height, width, 20, 5);
		}

		// Murs haut
		for (int i = 3; i < nbMurs; i += 4) {
			murs[i] = new Mur(width / 2, 0, width, 20, 5);
		}

	}

	@Override
	public void isStarting() {
//		song.play(0);
	}
	
	public void isStopping() {
		if (song != null) {
			song = null;
		}
	}

	@Override
	protected void drawCanvasAnimationFrame(PGraphics g) {
		if (song == null) {
			return;
		}
		
		currentFrame++;
		
		beat.detect(song.mix);
		// Faire avancer la chanson. On draw() pour chaque "frame" de la chanson...
		fft.forward(song.mix);

		if (currentFrame < 100) draw3DWallsAndCubes(g, false, false);
		if (currentFrame >= 100 && currentFrame < 200) draw3DWallsAndCubes(g, true, false);
		if (currentFrame == 200) hasSetColors = false;
		if (currentFrame >= 200 && currentFrame < 300) drawBeatingShapes(g);
		if (currentFrame >= 300 && currentFrame < 400) draw3DWallsAndCubes(g, true, true);
		if (currentFrame >= 400 && currentFrame < 500) draw3DWallsAndCubes(g, true, false);
		if (currentFrame == 500) hasSetColors = false;
		if (currentFrame >= 500 && currentFrame < 600) drawBeatingShapes(g);
		if (currentFrame >= 600) currentFrame = 0;

	}


	void draw3DWallsAndCubes(PGraphics g, boolean invertColor, boolean invertShape) {
		// Calcul des "scores" (puissance) pour trois cat�gories de son
		// D'abord, sauvgarder les anciennes valeurs
		oldScoreLow = scoreLow;
		oldScoreMid = scoreMid;
		oldScoreHi = scoreHi;

		// R�initialiser les valeurs
		scoreLow = 0;
		scoreMid = 0;
		scoreHi = 0;

		// Calculer les nouveaux "scores"
		for (int i = 0; i < fft.specSize() * specLow; i++) {
			scoreLow += fft.getBand(i);
		}

		for (int i = (int) (fft.specSize() * specLow); i < fft.specSize() * specMid; i++) {
			scoreMid += fft.getBand(i);
		}

		for (int i = (int) (fft.specSize() * specMid); i < fft.specSize() * specHi; i++) {
			scoreHi += fft.getBand(i);
		}

		// Faire ralentir la descente.
		if (oldScoreLow > scoreLow) {
			scoreLow = oldScoreLow - scoreDecreaseRate;
		}

		if (oldScoreMid > scoreMid) {
			scoreMid = oldScoreMid - scoreDecreaseRate;
		}

		if (oldScoreHi > scoreHi) {
			scoreHi = oldScoreHi - scoreDecreaseRate;
		}

		// Volume pour toutes les fr�quences � ce moment, avec les sons plus haut plus
		// importants.
		// Cela permet � l'animation d'aller plus vite pour les sons plus aigus, qu'on
		// remarque plus
		float scoreGlobal = 0.66f * scoreLow + 0.8f * scoreMid + 1f * scoreHi;

		// Couleur subtile de background
		g.beginDraw();
		if (beat.isOnset()) {
			if (invertColor) {
				bgColor = PColor.color((int)(Math.random()*255), (int)(Math.random()*255),(int)(Math.random()*255));
				squareColor = PColor.color(0,0,0);
			} else {
				bgColor = PColor.color(0,0,0);
				squareColor = PColor.color((int)(Math.random()*255), (int)(Math.random()*255),(int)(Math.random()*255));
			}
		}

		g.background(bgColor);

		// Cube pour chaque bande de fr�quence
		for (int i = 0; i < nbCubes; i++) {
			// Valeur de la bande de fr�quence
			float bandValue = fft.getBand(i);

			// La couleur est repr�sent�e ainsi: rouge pour les basses, vert pour les sons
			// moyens et bleu pour les hautes.
			// L'opacit� est d�termin�e par le volume de la bande et le volume global.
			cubes[i].display(scoreLow, scoreMid, scoreHi, bandValue, scoreGlobal, g, invertShape);
		}


		int wallColor[] = {(int)Math.round(Math.random()),(int)Math.round(Math.random()),(int)Math.round(Math.random())};

		// Murs rectangles
		for (int i = 0; i < nbMurs; i++) {
			// On assigne � chaque mur une bande, et on lui envoie sa force.
			float intensity = fft.getBand(i % ((int) (fft.specSize() * specHi)));
			murs[i].display(scoreLow, scoreMid, scoreHi, intensity, scoreGlobal, g, beat.isOnset(), wallColor);
		}


		g.endDraw();
		g.image(g.get(), 0, 0);
	}

	void drawBeatingShapes(PGraphics g) {

		if (!hasSetColors) {
			beatingShapesColors = setBeatingShapesColors();
			hasSetColors = true;
		}

		g.beginDraw();
		g.rectMode(PConstants.RADIUS);
		g.background(PColor.color(0,0,0));

		if ( beat.isOnset() ) {
			radiusTopLeft = (int) (Math.random() * (200 - 10) + 10);
			radiusBottomRight = (int) (Math.random() * (100 - 10) + 10);
			radiusTopRight = (int) (Math.random() * (20 - 5) + 5);
			radiusBottomLeft = (int) (Math.random() * (50 - 10) + 10);
		}

		g.lights();
		g.fill(beatingShapesColors[0]);
		g.rect(width/2 - radiusTopLeft, height/2 - radiusTopLeft, radiusTopLeft, radiusTopLeft);
		g.fill(beatingShapesColors[1]);
		g.rect(width/2 + radiusBottomRight, height/2 + radiusBottomRight, radiusBottomRight, radiusBottomRight);
		g.fill(beatingShapesColors[3]);
		g.rect(width/2 + radiusTopRight, height/2 - radiusTopRight, radiusTopRight, radiusTopRight);
		g.fill(beatingShapesColors[2]);
		g.rect(width/2 - radiusBottomLeft, height/2 + radiusBottomLeft, radiusBottomLeft, radiusBottomLeft);


		radiusTopLeft *= 0.7;
		if ( radiusTopLeft < 5 ) radiusTopLeft = 5;

		radiusBottomRight *= 0.7;
		if ( radiusBottomRight < 5 ) radiusBottomRight = 5;

		radiusTopRight *= 0.7;
		if ( radiusTopRight < 5 ) radiusTopRight = 5;

		radiusBottomLeft *= 0.7;
		if ( radiusBottomLeft < 5 ) radiusBottomLeft = 5;

		g.endDraw();
		g.image(g.get(), 0, 0);
	}

	int[] setBeatingShapesColors() {
		float h0 = (float) Math.random();
		float h1 = (float) Math.random();
		float h2 = h0 + .5f % 1f;
		float h3 = h1 + .5f % 1f;

		beatingShapesColors[0] = PColor.hsb(h0, 1f, 1f);
		beatingShapesColors[1] = PColor.hsb(h1, 1f, 1f);
		beatingShapesColors[2] = PColor.hsb(h2, 1f, 1f);
		beatingShapesColors[3] = PColor.hsb(h3, 1f, 1f);
		return beatingShapesColors;
	}

	void drawRoelAnimation(PGraphics g, int option) {
		g.noStroke();
		g.rectMode(PConstants.CENTER);

		g.fill(color);
		switch (option) {
			case 1:
				g.rect(xpos, height/2, diam, diam);
			break;
			case 2: g.rect(xpos*2, height/2, diam, height);
			break;
			case 3: g.rect(width/2, ypos, width, diam);
			break;
		}

		//scaling en moving
		diam = diam+100;
		xpos = xpos + 20;

//		if (beat.isOnset()) {
//			pompi = Color.HSBtoRGB(PApplet.map(h, 0, 360, 0, 1), 1f, 1f);
//		}

		if (diam > width) {
		  diam = 1;
		  // contrasting color change
		  hue = (hue + .495f) % 1f;
		}
	}


	// Classe pour les cubes qui flottent dans l'espace
	class Cube {
		// Position Z de "spawn" et position Z maximale
		float startingZ = -1000;
		float maxZ = -50;

		// Valeurs de positions
		float x, y, z;

		// Constructeur
		Cube() {

			// Faire apparaitre le cube � un endroit al�atoire
			x = (float) Math.random() * width;
			y = (float) Math.random() * height;
			z = (float) Math.random() * maxZ;
		}

		void display(float scoreLow, float scoreMid, float scoreHi, float intensity, float scoreGlobal, PGraphics g, boolean invertShape) {
			// S�lection de la couleur, opacit� d�termin�e par l'intensit� (volume de la
			// bande)
			g.fill(squareColor);

			// Couleur lignes, elles disparaissent avec l'intensit� individuelle du cube
			g.stroke(0);
			g.strokeWeight(0);

			// Cr�ation d'une matrice de transformation pour effectuer des rotations,
			// agrandissements
			g.pushMatrix();

			// D�placement
			g.translate(x, y, z);

			// Cr�ation de la boite, taille variable en fonction de l'intensit� pour le cube
			if (invertShape) {
				g.lights();
				g.sphere(20 + (intensity / 2));
			} else {
				g.box(50 + (intensity / 2));
			}


			// Application de la matrice
			g.popMatrix();

			// D�placement Z
			z += 1 + (intensity / 5) + (Math.pow((scoreGlobal / 150), 2));

			// Replacer la boite � l'arri�re lorsqu'elle n'est plus visible
			if (z >= maxZ) {
				x = (float) Math.random() * width;
				y = (float) Math.random() * height;
				z = (float) (Math.random() * startingZ + startingZ/2);
			}
		}
	}

	// Classe pour afficher les lignes sur les cot�s
	class Mur {
		// Position minimale et maximale Z
		float startingZ = -1000;
		float maxZ = 100;

		// Valeurs de position
		float x, y, z;
		float sizeX, sizeY;

		int colorDuration;
		boolean stayColored;

		// Constructeur
		Mur(float x, float y, float sizeX, float sizeY, int colorDuration) {
			// Faire apparaitre la ligne � l'endroit sp�cifi�
			this.x = x;
			this.y = y;
			// Profondeur al�atoire
			this.z = PApplet.map((float) Math.random(), 0, 1, startingZ, maxZ);

			// On d�termine la taille car les murs au planchers ont une taille diff�rente
			// que ceux sur les c�t�s
			this.sizeX = sizeX;
			this.sizeY = sizeY;
			this.colorDuration = colorDuration;
			this.stayColored = false;
		}

		void setColorDuration(int colorDuration) {
			this.colorDuration = colorDuration;
		}

		// Fonction d'affichage
		void display(float scoreLow, float scoreMid, float scoreHi, float intensity, float scoreGlobal, PGraphics g, boolean beatIsOn, int[] rand) {
			// Couleur d�termin�e par les sons bas, moyens et �lev�
			if (beatIsOn) {
				this.stayColored = true;
			}

			if (this.stayColored) {
				this.setColorDuration(this.colorDuration - 1);
			}

			if (this.colorDuration > 0) {
				g.fill(PColor.color(rand[0] * 255, rand[1] * 255, rand[2] * 255));
			} else if (this.colorDuration == 0) {
				this.stayColored = false;
				this.setColorDuration(5);
				g.fill(bgColor);
			} else {
				g.fill(bgColor);
			}

			g.noStroke();

			// Premi�re bande, celle qui bouge en fonction de la force
			// Matrice de transformation
			g.pushMatrix();

			// D�placement
			g.translate(x, y, z);

			// Agrandissement
			if (intensity > 100)
				intensity = 100;
			g.scale(sizeX * (intensity / 100), sizeY * (intensity / 100), 20);

			// Cr�ation de la "boite"
			g.box(1);
			g.popMatrix();

			// D�placement Z
			z += (Math.pow((scoreGlobal / 150), 2));
			if (z >= maxZ) {
				z = startingZ;
			}
		}
	}
}
