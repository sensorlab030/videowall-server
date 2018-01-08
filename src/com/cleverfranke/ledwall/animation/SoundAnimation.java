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

	// Variables qui définissent les "zones" du spectre
	// Par exemple, pour les basses, on prend seulement les premières 4% du spectre
	// total
	float specLow = 0.03f; // 3%
	float specMid = 0.125f; // 12.5%
	float specHi = 0.20f; // 20%

	// Il reste donc 64% du spectre possible qui ne sera pas utilisé.
	// Ces valeurs sont généralement trop hautes pour l'oreille humaine de toute
	// facon.

	// Valeurs de score pour chaque zone
	float scoreLow = 0;
	float scoreMid = 0;
	float scoreHi = 0;

	// Valeur précédentes, pour adoucir la reduction
	float oldScoreLow = scoreLow;
	float oldScoreMid = scoreMid;
	float oldScoreHi = scoreHi;

	// Valeur d'adoucissement
	float scoreDecreaseRate = 25;

	// Cubes qui apparaissent dans l'espace
	int nbCubes;
	Cube[] cubes;
	
	ArrayList<Shape> shapes = new ArrayList<>();

	// Lignes qui apparaissent sur les cotés
	int nbMurs = 500;
	Mur[] murs;

	float width, height;

	public SoundAnimation(PApplet applet) {
		super(applet);

		// Charger la librairie minim
		minim = new Minim(applet);

		// Charger la chanson
		song = minim.loadFile(FileSystem.getApplicationPath("song.mp3"));

		// Créer l'objet FFT pour analyser la chanson
		fft = new FFT(song.bufferSize(), song.sampleRate());

		// Un cube par bande de fréquence
		nbCubes = (int) (fft.specSize() * specHi);
		cubes = new Cube[nbCubes];

		// Autant de murs qu'on veux
		murs = new Mur[nbMurs];

		Rectangle geom = getGeometry();

		width = geom.width;
		height = geom.height;
		
//		for(int i = 0; i < 100; i++) {
//			shapes.add(new Shape((float) Math.random() * (float) width, (float) Math.random() * height, (float) i / 100f * Shape.Z_MAX));
//		}
		

		// Créer tous les objets
		// Créer les objets cubes
		for (int i = 0; i < nbCubes; i++) {
			cubes[i] = new Cube();
		}

		// Créer les objets murs
		// Murs gauches
		for (int i = 0; i < nbMurs; i += 4) {
			murs[i] = new Mur(0, height / 2, 10, height);
		}

		// Murs droits
		for (int i = 1; i < nbMurs; i += 4) {
			murs[i] = new Mur(width, height / 2, 10, height);
		}

		// Murs bas
		for (int i = 2; i < nbMurs; i += 4) {
			murs[i] = new Mur(width / 2, height, width, 10);
		}

		// Murs haut
		for (int i = 3; i < nbMurs; i += 4) {
			murs[i] = new Mur(width / 2, 0, width, 10);
		}

	}

	@Override
	public void isStarting() {
		song.play(0);
	}

	@Override
	protected void drawCanvasAnimationFrame(PGraphics g) {
		
		// Faire avancer la chanson. On draw() pour chaque "frame" de la chanson...
		fft.forward(song.mix);

		// Calcul des "scores" (puissance) pour trois catégories de son
		// D'abord, sauvgarder les anciennes valeurs
		oldScoreLow = scoreLow;
		oldScoreMid = scoreMid;
		oldScoreHi = scoreHi;

		// Réinitialiser les valeurs
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

		// Volume pour toutes les fréquences à ce moment, avec les sons plus haut plus
		// importants.
		// Cela permet à l'animation d'aller plus vite pour les sons plus aigus, qu'on
		// remarque plus
		float scoreGlobal = 0.66f * scoreLow + 0.8f * scoreMid + 1f * scoreHi;

		// Couleur subtile de background
		g.beginDraw();
		g.background(scoreLow / 100, scoreMid / 100, scoreHi / 100);

		// Cube pour chaque bande de fréquence
		for (int i = 0; i < nbCubes; i++) {
			// Valeur de la bande de fréquence
			float bandValue = fft.getBand(i);

			// La couleur est représentée ainsi: rouge pour les basses, vert pour les sons
			// moyens et bleu pour les hautes.
			// L'opacité est déterminée par le volume de la bande et le volume global.
			cubes[i].display(scoreLow, scoreMid, scoreHi, bandValue, scoreGlobal, g);
		}

		// Murs lignes, ici il faut garder la valeur de la bande précédent et la
		// suivante pour les connecter ensemble
		float previousBandValue = fft.getBand(0);

		// Distance entre chaque point de ligne, négatif car sur la dimension z
		float dist = -25;

		// Multiplier la hauteur par cette constante
		float heightMult = 2;

		// Pour chaque bande
		for (int i = 1; i < fft.specSize(); i++) {
			// Valeur de la bande de fréquence, on multiplie les bandes plus loins pour
			// qu'elles soient plus visibles.
			float bandValue = fft.getBand(i) * (1 + (i / 50));

			// Selection de la couleur en fonction des forces des différents types de sons
			g.stroke(100 + scoreLow, 100 + scoreMid, 100 + scoreHi, 255 - i);
			g.strokeWeight(1 + (scoreGlobal / 100));

			// ligne inferieure gauche
			g.line(0, 
					height - (previousBandValue * heightMult), 
					dist * (i - 1), 
					0, height - (bandValue * heightMult), 
					dist * i);
			g.line((previousBandValue * heightMult), height, dist * (i - 1), (bandValue * heightMult), height,
					dist * i);
			g.line(0, height - (previousBandValue * heightMult), dist * (i - 1), (bandValue * heightMult), height,
					dist * i);

			// ligne superieure gauche
			g.line(0, (previousBandValue * heightMult), dist * (i - 1), 0, (bandValue * heightMult), dist * i);
			g.line((previousBandValue * heightMult), 0, dist * (i - 1), (bandValue * heightMult), 0, dist * i);
			g.line(0, (previousBandValue * heightMult), dist * (i - 1), (bandValue * heightMult), 0, dist * i);

			// ligne inferieure droite
			g.line(width, height - (previousBandValue * heightMult), dist * (i - 1), width,
					height - (bandValue * heightMult), dist * i);
			g.line(width - (previousBandValue * heightMult), height, dist * (i - 1), width - (bandValue * heightMult),
					height, dist * i);
			g.line(width, height - (previousBandValue * heightMult), dist * (i - 1), width - (bandValue * heightMult),
					height, dist * i);

			// ligne superieure droite
			g.line(width, (previousBandValue * heightMult), dist * (i - 1), width, (bandValue * heightMult), dist * i);
			g.line(width - (previousBandValue * heightMult), 0, dist * (i - 1), width - (bandValue * heightMult), 0,
					dist * i);
			g.line(width, (previousBandValue * heightMult), dist * (i - 1), width - (bandValue * heightMult), 0,
					dist * i);

			// Sauvegarder la valeur pour le prochain tour de boucle
			previousBandValue = bandValue;
		}

		// Murs rectangles
		for (int i = 0; i < nbMurs; i++) {
			// On assigne à chaque mur une bande, et on lui envoie sa force.
			float intensity = fft.getBand(i % ((int) (fft.specSize() * specHi)));
			murs[i].display(scoreLow, scoreMid, scoreHi, intensity, scoreGlobal, g);
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
		float startingZ = -10000;
		float maxZ = 1000;

		// Valeurs de positions
		float x, y, z;
		float rotX, rotY, rotZ;
		float sumRotX, sumRotY, sumRotZ;

		// Constructeur
		Cube() {

			// Faire apparaitre le cube à un endroit aléatoire
			x = (float) Math.random() * width;
			y = (float) Math.random() * height;
			z = (float) Math.random() * maxZ;

			// Donner au cube une rotation aléatoire
			rotX = (float) Math.random();
			rotY = (float) Math.random();
			rotZ = (float) Math.random();
		}

		void display(float scoreLow, float scoreMid, float scoreHi, float intensity, float scoreGlobal, PGraphics g) {
			// Sélection de la couleur, opacité déterminée par l'intensité (volume de la
			// bande)
			int displayColor = PColor.color(scoreLow * 0.67f, scoreMid * 0.67f, scoreHi * 0.67f, intensity * 5f);
			g.fill(displayColor, 255);

			// Couleur lignes, elles disparaissent avec l'intensité individuelle du cube
			int strokeColor = PColor.color(255, 150 - (20 * intensity));
			g.stroke(strokeColor);
			g.strokeWeight(1 + (scoreGlobal / 300));

			// Création d'une matrice de transformation pour effectuer des rotations,
			// agrandissements
			g.pushMatrix();

			// Déplacement
			g.translate(x, y, z);

			// Calcul de la rotation en fonction de l'intensité pour le cube
			sumRotX += intensity * (rotX / 1000);
			sumRotY += intensity * (rotY / 1000);
			sumRotZ += intensity * (rotZ / 1000);

			// Application de la rotation
			g.rotateX(sumRotX);
			g.rotateY(sumRotY);
			g.rotateZ(sumRotZ);

			// Création de la boite, taille variable en fonction de l'intensité pour le cube
			g.box(100 + (intensity / 2));

			// Application de la matrice
			g.popMatrix();

			// Déplacement Z
			z += (1 + (intensity / 5) + (Math.pow((scoreGlobal / 150), 2)));

			// Replacer la boite à l'arrière lorsqu'elle n'est plus visible
			if (z >= maxZ) {
				x = (float) Math.random() * width;
				y = (float) Math.random() * height;
				z = startingZ;
			}
		}
	}

	// Classe pour afficher les lignes sur les cotés
	class Mur {
		// Position minimale et maximale Z
		float startingZ = -10000;
		float maxZ = 50;

		// Valeurs de position
		float x, y, z;
		float sizeX, sizeY;

		// Constructeur
		Mur(float x, float y, float sizeX, float sizeY) {
			// Faire apparaitre la ligne à l'endroit spécifié
			this.x = x;
			this.y = y;
			// Profondeur aléatoire
			this.z = PApplet.map((float) Math.random(), 0, 1, startingZ, maxZ);

			// On détermine la taille car les murs au planchers ont une taille différente
			// que ceux sur les côtés
			this.sizeX = sizeX;
			this.sizeY = sizeY;
		}

		// Fonction d'affichage
		void display(float scoreLow, float scoreMid, float scoreHi, float intensity, float scoreGlobal, PGraphics g) {
			// Couleur déterminée par les sons bas, moyens et élevé
			// Opacité déterminé par le volume global
			int displayColor = PColor.color(scoreLow * 0.67f, scoreMid * 0.67f, scoreHi * 0.67f, scoreGlobal);

			// Faire disparaitre les lignes au loin pour donner une illusion de brouillard
			g.fill(displayColor, ((scoreGlobal - 5) / 1000) * (255 + (z / 25)));
			g.noStroke();

			// Première bande, celle qui bouge en fonction de la force
			// Matrice de transformation
			g.pushMatrix();

			// Déplacement
			g.translate(x, y, z);

			// Agrandissement
			if (intensity > 100)
				intensity = 100;
			g.scale(sizeX * (intensity / 100), sizeY * (intensity / 100), 20);

			// Création de la "boite"
			g.box(1);
			g.popMatrix();

			// Deuxième bande, celle qui est toujours de la même taille
			displayColor = PColor.color(scoreLow * 0.5f, scoreMid * 0.5f, scoreHi * 0.5f, scoreGlobal);
			g.fill(displayColor, (scoreGlobal / 5000) * (255 + (z / 25)));

			// Matrice de transformation
			g.pushMatrix();

			// Déplacement
			g.translate(x, y, z);

			// Agrandissement
			g.scale(sizeX, sizeY, 10);

			// Création de la "boite"
			g.box(1);
			g.popMatrix();

			// Déplacement Z
			z += (Math.pow((scoreGlobal / 150), 2));
			if (z >= maxZ) {
				z = startingZ;
			}
		}
	}
}
