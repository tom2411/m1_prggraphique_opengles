package com.example.testopengl.jeu;

import android.util.Log;

import com.example.testopengl.formes.Forme;
import com.example.testopengl.formes.Losange;
import com.example.testopengl.formes.Plateau;
import com.example.testopengl.formes.Square;
import com.example.testopengl.formes.Triangle;

import java.util.ArrayList;
import java.util.Arrays;

public class Game {

    // les formes affichées
    private Plateau mPlateau;

    private Losange losange1;
    private Losange losange2;
    private Losange losange3;

    private Square carre1;
    private Square carre2;
    private Square carre3;

    private Triangle triangle1;
    private Triangle triangle2;

    // la grille où évolue nos formes
    private Grille mGrille;

    // clone de la grille de départ pour avoir un état final
    private Grille grilleNonMelangee;

    // Position centrale de notre plateau et de notre repère
    private float[] mPlateauPosition = {0.0f, 0.0f};

    // est ce que le plateau est mélangé ?
    private boolean isMelange = false;

    // Coordonnées des cases de la matrice pour le placement des formes
    // à la création de la grille
    private float[] case1 = {-7.0f,6.5f};
    private float[] case2 = {0.0f, 6.5f};
    private float[] case3 = {7.0f, 6.5f};

    private float[] case4 = {-7.0f, 0.0f};
    private float[] case5 = {0.0f, 0.0f};
    private float[] case6 = {7.0f, 0.0f};

    private float[] case7 = {-7.0f, -6.5f};
    private float[] case8 = {0.0f, -6.5f};
    private float[] case9 = {7.0f, -6.5f};

    // liste des représentations des cases de la grilles en OpenGL
    private ArrayList<float[]> cases;

    public Game() {
        // Création d'un plateau avec un fond blanc
        mPlateau = new Plateau(mPlateauPosition,0.93f,0.93f,0.82f);

        // ajout des cases à la liste
        this.cases = new ArrayList<>(Arrays.asList(case1, case2, case3, case4, case5, case6, case7, case8, case9));

        // création des formes avec leurs couleurs et leurs places en OpenGL
        this.losange1 = new Losange(case1, 1f, 0f, 0f);
        this.losange2 = new Losange(case2, 0f, 1f, 0f);
        this.losange3 = new Losange(case3, 0f, 0f, 1f);

        this.carre1 = new Square(case4, 1f, 0f, 0f);
        this.carre2 = new Square(case5, 0f, 1f, 0f);
        this.carre3 = new Square(case6, 0f, 0f, 1f);

        this.triangle1 = new Triangle(case7, 1f, 0f, 0f);
        this.triangle2 = new Triangle(case8, 0f, 1f, 0f);

        // Ajout des formes à la grille
        ArrayList<Forme> liste_forme = new ArrayList<>(Arrays.asList(losange1,losange2,losange3,carre1,carre2,carre3,triangle1,triangle2,null));
        // création de la grille
        this.mGrille = new Grille(3,3, liste_forme);

        // positionnement des formes en OpenGL (affichage)
        for (int i = 0; i < 9; i++) {
            if (mGrille.getGrille().get(i) != null) {
                mGrille.getGrille().get(i).set_position(this.cases.get(i));
            }
        }

        // clone de la grille pour avoir un état final
        this.grilleNonMelangee = (Grille) this.mGrille.clone();
    }

    /**
     * Permet de récupérer l'objet Grille
     * @return une Grille
     */
    public Grille mGrille() {
        return this.mGrille;
    }

    /**
     * Permet de récupérer la forme plateau
     * @return un Plateau
     */
    public Plateau getmPlateau() {
        return mPlateau;
    }

    /**
     * Permet de dessiner tous les objets de la grille
     * @param scratch, la matrice MPV
     */
    public void dessinerJeu(float[] scratch) {
        mPlateau.draw(scratch);
        Log.d("deplacement", "dessinerJeu: "+ Arrays.toString(mPlateau.getPlateauColors()));
        mGrille.dessinerFormes(scratch);
    }

    /**
     * Permet de récupérer la position de l'objet Plateau
     * @return un tableau de float qui repérente les coordonnées du plateau
     */
    public float[] getmPlateauPosition() {
        return mPlateauPosition;
    }

    /**
     * Permet de changer la couleur du plateau de la grille
     * @param red, l'intensité du rouge pour un pixel
     * @param green, l'intensité du vert pour un pixel
     * @param blue, l'intensité du bleu pour un pixel
     */
    public void setPlateauColors(float red, float green, float blue) {
        mPlateau.setPlateauColors(red, green, blue);
    }

    /**
     * Permet de savoir si la grille est résolue ou non
     * @return un boolean qui est à true si la grille est résolue et false sinon
     */
    public boolean isGrilleResolue() {
        return (this.mGrille.equals(this.grilleNonMelangee));
    }

    /**
     * Permet de savoir si la grille est mélangé
     * @return un boolean, true si mélangé et false sinon
     */
    public boolean isMelange() {
        return isMelange;
    }

    /**
     * permet de changer la variable de mélange
     * @param melange, un boolean
     */
    public void setMelange(boolean melange) {
        isMelange = melange;
    }
}
