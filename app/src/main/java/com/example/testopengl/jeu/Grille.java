package com.example.testopengl.jeu;

import com.example.testopengl.formes.Forme;

import java.util.ArrayList;
import java.util.List;

public class Grille {
    private List<Forme> liste_formes;
    private List<List<Forme>> grille;
    private final static int NB_LIG =3;
    private final static int NB_COL =3;

    public Grille(List<Forme> liste_formes) {
        this.liste_formes = liste_formes;
        this.grille = new ArrayList<>();

        for (int i = 0; i < NB_LIG; i++) {
            List<Forme> lig = new ArrayList<>();
            for (int j = 0; j < NB_COL; j++) {
                lig.add(liste_formes.get(j));
            }
            this.grille.add(lig);
            for (int j = 0; j < NB_COL; j++) {
                this.liste_formes.remove(j);
            }
        }

    }

    private void déplacementHaut(int lig, int col){
        for (int i = 0; i < NB_LIG; i++) {
            for (int j = 0; j < NB_COL; j++) {
            }
        }
    }

    private void déplacementBas(int lig, int col){

    }

    private void déplacementDroite(int lig, int col){

    }

    private void déplacementGauche(int lig, int col){

    }
}
