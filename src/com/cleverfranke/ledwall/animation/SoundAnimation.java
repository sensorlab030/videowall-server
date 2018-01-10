package com.cleverfranke.ledwall.animation;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;

import java.awt.Rectangle;
import java.util.ArrayList;

import com.cleverfranke.util.FileSystem;
import com.cleverfranke.util.PColor;

import ddf.minim.*;
import ddf.minim.analysis.*;

public class SoundAnimation extends BaseCanvas3dAnimation {

	Minim minim;
	AudioPlayer song;
	FFT fft;
	BeatDetect beat;

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

	// Beat cube
	float eRadius;

	ArrayList<Shape> shapes = new ArrayList<>();

	// Lignes qui apparaissent sur les cot�s
	int nbMurs = 4;
	Mur[] murs;

	float width, height;

	public SoundAnimation(PApplet applet) {
		super(applet);

		// Charger la librairie minim
		minim = new Minim(applet);

		// Charger la chanson
		song = minim.loadFile(FileSystem.getApplicationPath("jonas_mix.mov"));

		// Cr�er l'objet FFT pour analyser la chanson
		fft = new FFT(song.bufferSize(), song.sampleRate());

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

//		for(int i = 0; i < 100; i++) {
//			shapes.add(new Shape((float) Math.random() * (float) width, (float) Math.random() * height, (float) i / 100f * Shape.Z_MAX));
//		}


		// Cr�er tous les objets
		// Cr�er les objets cubes
		for (int i = 0; i < nbCubes; i++) {
			cubes[i] = new Cube();
		}

		// Cr�er les objets murs
		// Murs gauches
		for (int i = 0; i < nbMurs; i += 4) {
			murs[i] = new Mur(0, height / 2, 40, height);
		}

		// Murs droits
		for (int i = 1; i < nbMurs; i += 4) {
			murs[i] = new Mur(width, height / 2, 40, height);
		}

		// Murs bas
		for (int i = 2; i < nbMurs; i += 4) {
			murs[i] = new Mur(width / 2, height, width, 40);
		}

		// Murs haut
		for (int i = 3; i < nbMurs; i += 4) {
			murs[i] = new Mur(width / 2, 0, width, 40);
		}

		// Beat detection radius
		eRadius = 20;
	}

	@Override
	public void isStarting() {
		song.play(0);
	}

	@Override
	protected void drawCanvasAnimationFrame(PGraphics g) {

		// Faire avancer la chanson. On draw() pour chaque "frame" de la chanson...
		fft.forward(song.mix);

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


		beat.detect(song.mix);

		// Couleur subtile de background
		g.beginDraw();
		g.background(scoreLow / 100, scoreMid / 100, scoreHi / 100);

		// Cube pour chaque bande de fr�quence
		for (int i = 0; i < nbCubes; i++) {
			// Valeur de la bande de fr�quence
			float bandValue = fft.getBand(i);

			// La couleur est repr�sent�e ainsi: rouge pour les basses, vert pour les sons
			// moyens et bleu pour les hautes.
			// L'opacit� est d�termin�e par le volume de la bande et le volume global.
			cubes[i].display(scoreLow, scoreMid, scoreHi, bandValue, scoreGlobal, g, beat.isOnset());
		}




		// Murs rectangles
		for (int i = 0; i < nbMurs; i++) {
			// On assigne � chaque mur une bande, et on lui envoie sa force.
			float intensity = fft.getBand(i % ((int) (fft.specSize() * specHi)));
			murs[i].display(scoreLow, scoreMid, scoreHi, intensity, scoreGlobal, g, beat.isOnset());
		}

		g.endDraw();
		g.image(g.get(), 0, 0);


	}

	class Shape {

		public static final float Z_MAX = 100f;
		public static final float Z_MIN = 10f;
		private PVector pos;

		public Shape(float x, float y, float z) {
			this.pos = new PVector(x, y, z);
		}

		public void draw(PGraphics g) {

			g.pushMatrix();

			float scaler = -0.2f;

			// @TODO apply color
			g.fill(255, 40);
			g.noStroke();
			g.ellipseMode(PConstants.CENTER);
			g.translate(pos.x, pos.y);
//			g.scale(scaler * pos.z);
			g.ellipse(0,  0, scaler * pos.z * 10, scaler * pos.z * 10);

			g.popMatrix();

			// Update z
			pos.z--;
			if (pos.z < Z_MIN) {
				pos.z = Z_MAX;
			}
		}


	}

	// Classe pour les cubes qui flottent dans l'espace
	class Cube {
		// Position Z de "spawn" et position Z maximale
		float startingZ = -2000;
		float maxZ = 0;

		// Valeurs de positions
		float x, y, z;
		float rotX, rotY, rotZ;
		float sumRotX, sumRotY, sumRotZ;

		// Constructeur
		Cube() {

			// Faire apparaitre le cube � un endroit al�atoire
			x = (float) Math.random() * width;
			y = (float) Math.random() * height;
			z = (float) Math.random() * maxZ;

			// Donner au cube une rotation al�atoire
			rotX = (float) Math.random();
			rotY = (float) Math.random();
			rotZ = (float) Math.random();
		}

		void display(float scoreLow, float scoreMid, float scoreHi, float intensity, float scoreGlobal, PGraphics g, boolean beatIsOn) {
			// S�lection de la couleur, opacit� d�termin�e par l'intensit� (volume de la
			// bande)
			int displayColor = PColor.color(scoreLow * 0.67f, scoreMid * 0.67f, scoreHi * 0.67f, intensity * 5f);
			g.fill(displayColor);
//			if (beatIsOn) {
//				g.fill(displayColor, 255);
//			} else {
//				g.fill(PColor.color(255,255,255), 255);
//			}

			// Couleur lignes, elles disparaissent avec l'intensit� individuelle du cube
			g.stroke(0);
			g.strokeWeight(0);

			// Cr�ation d'une matrice de transformation pour effectuer des rotations,
			// agrandissements
			g.pushMatrix();

			// D�placement
			g.translate(x, y, z);

			// Calcul de la rotation en fonction de l'intensit� pour le cube
//			sumRotX += intensity * (rotX / 1000);
//			sumRotY += intensity * (rotY / 1000);
//			sumRotZ += intensity * (rotZ / 1000);
//
//			// Application de la rotation
//			g.rotateX(sumRotX);
//			g.rotateY(sumRotY);
//			g.rotateZ(sumRotZ);

			// Cr�ation de la boite, taille variable en fonction de l'intensit� pour le cube
			g.box(50 + (intensity / 2));

			// Application de la matrice
			g.popMatrix();

			// D�placement Z
			z += 1 + (intensity / 5) + (Math.pow((scoreGlobal / 150), 2));

			// Replacer la boite � l'arri�re lorsqu'elle n'est plus visible
			if (z >= maxZ) {
				x = (float) Math.random() * width;
				y = (float) Math.random() * height;
				z = startingZ;
			}
		}
	}

	// Classe pour afficher les lignes sur les cot�s
	class Mur {
		// Position minimale et maximale Z
		float startingZ = -1000;
		float maxZ = 50;

		// Valeurs de position
		float x, y, z;
		float sizeX, sizeY;

		// Constructeur
		Mur(float x, float y, float sizeX, float sizeY) {
			// Faire apparaitre la ligne � l'endroit sp�cifi�
			this.x = x;
			this.y = y;
			// Profondeur al�atoire
			this.z = PApplet.map((float) Math.random(), 0, 1, startingZ, maxZ);

			// On d�termine la taille car les murs au planchers ont une taille diff�rente
			// que ceux sur les c�t�s
			this.sizeX = sizeX;
			this.sizeY = sizeY;
		}

		// Fonction d'affichage
		void display(float scoreLow, float scoreMid, float scoreHi, float intensity, float scoreGlobal, PGraphics g, boolean beatIsOn) {
			// Couleur d�termin�e par les sons bas, moyens et �lev�
			// Opacit� d�termin� par le volume global
			int displayColor = PColor.color(scoreLow * 0.67f, scoreMid * 0.67f, scoreHi * 0.67f, scoreGlobal);

			if (beatIsOn) {
				g.fill(PColor.color(255, 0, 0));
			} else {
				g.fill(PColor.color(0, 0, 0));
			}
			// Faire disparaitre les lignes au loin pour donner une illusion de brouillard
//			g.fill(PColor.color(255, 0, 0), ((scoreGlobal - 5) / 1000) * (255 + (z / 25)));
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

//			// Deuxi�me bande, celle qui est toujours de la m�me taille
//			displayColor = PColor.color(scoreLow * 0.5f, scoreMid * 0.5f, scoreHi * 0.5f, scoreGlobal);
//			g.fill(displayColor, (scoreGlobal / 5000) * (255 + (z / 25)));
//
//			// Matrice de transformation
//			g.pushMatrix();
//
//			// D�placement
//			g.translate(x, y, z);
//
//			// Agrandissement
//			g.scale(sizeX, sizeY, 10);
//
//			// Cr�ation de la "boite"
//			g.box(1);
//			g.popMatrix();

			// D�placement Z
			z += (Math.pow((scoreGlobal / 150), 2));
			if (z >= maxZ) {
				z = startingZ;
			}
		}
	}
}
