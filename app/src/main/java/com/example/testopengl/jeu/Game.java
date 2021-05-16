package com.example.testopengl.jeu;

import com.example.testopengl.formes.Forme;
import com.example.testopengl.formes.Losange;
import com.example.testopengl.formes.Plateau;
import com.example.testopengl.formes.Square;
import com.example.testopengl.formes.Triangle;

import java.util.ArrayList;
import java.util.Arrays;

public class Game {

    private Plateau mPlateau;

    private Losange losange1;
    private Losange losange2;
    private Losange losange3;

    private Square carre1;
    private Square carre2;
    private Square carre3;

    private Triangle triangle1;
    private Triangle triangle2;

    private Grille mGrille;

    private Grille grilleNonMelangee;

    private float[] mPlateauPosition = {0.0f, 0.0f};

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

    private ArrayList<float[]> cases;

    public Game() {
        /* on va définir une classe Square pour dessiner des carrés */
        mPlateau = new Plateau(mPlateauPosition);

        this.cases = new ArrayList<>(Arrays.asList(case1, case2, case3, case4, case5, case6, case7, case8, case9));

        this.losange1 = new Losange(case1, 1f, 0f, 0f);
        this.losange2 = new Losange(case2, 0f, 1f, 0f);
        this.losange3 = new Losange(case3, 0f, 0f, 1f);

        this.carre1 = new Square(case4, 1f, 0f, 0f);
        this.carre2 = new Square(case5, 0f, 1f, 0f);
        this.carre3 = new Square(case6, 0f, 0f, 1f);

        this.triangle1 = new Triangle(case7, 1f, 0f, 0f);
        this.triangle2 = new Triangle(case8, 0f, 1f, 0f);

        ArrayList<Forme> liste_forme = new ArrayList<>(Arrays.asList(losange1,losange2,losange3,carre1,carre2,carre3,triangle1,triangle2,null));
        this.mGrille = new Grille(3,3, liste_forme);

        for (int i = 0; i < 9; i++) {
            if (mGrille.getGrille().get(i) != null) {
                mGrille.getGrille().get(i).set_position(this.cases.get(i));
            }
        }

        this.grilleNonMelangee = (Grille) this.mGrille.clone();
    }

    public Grille mGrille() {
        return this.mGrille;
    }

    public Plateau getmPlateau() {
        return mPlateau;
    }

    public void dessinerJeu(float[] scratch) {
        mPlateau.draw(scratch);
        mGrille.dessinerFormes(scratch);
    }

    public float[] getmPlateauPosition() {
        return mPlateauPosition;
    }

    public boolean isGrilleResolue() {
        return (this.mGrille.equals(this.grilleNonMelangee));
    }


}
