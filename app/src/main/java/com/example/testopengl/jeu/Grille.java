package com.example.testopengl.jeu;

import com.example.testopengl.formes.Forme;

import java.util.ArrayList;
import java.util.List;

public class Grille {
    private List<Forme> grille;
    private final int nbLignes;
    private final int nbColonnes;

    public Grille(int largeurGrille, int hauteurGrille, ArrayList<Forme> liste_formes) {
        this.nbLignes = hauteurGrille;
        this.nbColonnes = largeurGrille; // si on a toujours des grilles carrées, on pourrait retirer le paramètre hauteurGrille
        this.grille = liste_formes;
    }

    // récupérer la forme présente aux coordonnées lig et col
    private Forme get(int lig, int col) {
        return this.grille.get(lig * nbColonnes + col);
    }

    // changer la forme présente aux coordonnées lig et col
    private void set(int lig, int col, Forme forme) {
        this.grille.set(lig * nbLignes + col, forme);
    }

    // retourne false si c'est impossible de déplacer cette case,
    // true sinon (et déplace la case dans ce cas là)
    public boolean deplacement(int ligne, int colonne) {
        // pour pouvoir déplacer une forme vers le haut,
        // il faut qu'on ne soit pas dans la ligne la plus haute
        // et qu'il n'y ait rien dans la case au dessus
        if (ligne != 0 && get(ligne - 1, colonne) == null) {
            this.deplacementHaut(ligne, colonne);
            return true;
        } else if (ligne != nbLignes - 1 && get(ligne + 1, colonne) == null) {
            this.deplacementBas(ligne, colonne);
            return true;
        } else if (colonne != nbColonnes - 1 && get(colonne + 1, ligne) == null) {
            this.deplacementDroite(ligne, colonne);
            return true;
        } else if (colonne != 0 && get(colonne - 1, ligne) == null) {
            this.deplacementGauche(ligne, colonne);
            return true;
        }
        return false;
    }

    private void deplacementHaut(int lig, int col){
        this.set(lig-1, col, this.get(lig, col)); // on met dans la forme dans la case au-dessus
        this.set(lig, col, null); // et on la retire de la case actuelle

    }

    private void deplacementBas(int lig, int col){
        this.set(lig+1, col, this.get(lig, col));
        this.set(lig, col, null);
    }

    private void deplacementDroite(int lig, int col){
        this.set(lig, col+1, this.get(lig, col));
        this.set(lig, col, null);
    }

    private void deplacementGauche(int lig, int col){
        this.set(lig, col-1, this.get(lig, col));
        this.set(lig, col, null);
    }
}
