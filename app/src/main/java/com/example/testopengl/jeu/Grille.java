package com.example.testopengl.jeu;

import com.example.testopengl.formes.Forme;

import java.util.ArrayList;
import java.util.List;

public class Grille {
    private List<Forme> grille;
    private final int nbLignes;
    private final int nbColonnes;

    /**
     * La grille est initialisée dans son état final
     * @param largeurGrille, un int qui représente le nombre de colonne
     * @param hauteurGrille, un int qui représente le nombre de ligne
     * @param liste_formes, une liste de forme
     */
    public Grille(int largeurGrille, int hauteurGrille, ArrayList<Forme> liste_formes) {
        this.nbLignes = hauteurGrille;
        this.nbColonnes = largeurGrille; // si on a toujours des grilles carrées, on pourrait retirer le paramètre hauteurGrille
        this.grille = liste_formes; // la liste des formes doit être de longueur (nombre de cases - 1)
        this.grille.add(null); // la dernière case (en bas à droite) est vide
    }

    /**
     * Récupérer la forme présente aux coordonnées lig et col
     * @param lig, un int qui représente la ligne
     * @param col, un int qui représente la colonne
     * @return la forme à la position lig, col
     */
    private Forme getFormeParLigneColonne(int lig, int col) {
        return this.grille.get(lig * nbColonnes + col);
    }

    /**
     * Changer la forme présente aux coordonnées lig et col
     * @param lig, un int qui représente la ligne
     * @param col, un int qui représente la colonne
     * @param forme, une forme à mettre dans la grille
     */
    private void setFormeParLigneColonne(int lig, int col, Forme forme) {
        this.grille.set(lig * nbLignes + col, forme);
    }

    // retourne false si c'est impossible de déplacer cette case,
    // true sinon (et déplace la case dans ce cas là)
    public boolean deplacement(int ligne, int colonne) {
        // pour pouvoir déplacer une forme vers le haut,
        // il faut qu'on ne soit pas dans la ligne la plus haute
        // et qu'il n'y ait rien dans la case au dessus
        if (ligne != 0 && getFormeParLigneColonne(ligne - 1, colonne) == null) {
            this.deplacementHaut(ligne, colonne);
            return true;
        } else if (ligne != nbLignes - 1 && getFormeParLigneColonne(ligne + 1, colonne) == null) {
            this.deplacementBas(ligne, colonne);
            return true;
        } else if (colonne != nbColonnes - 1 && getFormeParLigneColonne(colonne + 1, ligne) == null) {
            this.deplacementDroite(ligne, colonne);
            return true;
        } else if (colonne != 0 && getFormeParLigneColonne(colonne - 1, ligne) == null) {
            this.deplacementGauche(ligne, colonne);
            return true;
        }
        return false;
    }

    /**
     * Permet de déplacer une forme à la position lig, col vers le haut
     * @param lig, un int qui représente la lignes
     * @param col, un int qui représente la colonnes
     */
    private void deplacementHaut(int lig, int col){
        Forme formeADeplacer = this.getFormeParLigneColonne(lig, col);
        this.setFormeParLigneColonne(lig-1, col, formeADeplacer); // on met dans la forme dans la case au-dessus
        this.setFormeParLigneColonne(lig, col, null); // et on la retire de la case actuelle

    }

    /**
     * Permet de déplacer une forme à la position lig, col vers le bas
     * @param lig, un int qui représente la lignes
     * @param col, un int qui représente la colonnes
     */
    private void deplacementBas(int lig, int col){
        Forme formeADeplacer = this.getFormeParLigneColonne(lig, col);
        this.setFormeParLigneColonne(lig+1, col, formeADeplacer);
        this.setFormeParLigneColonne(lig, col, null);
    }

    /**
     * Permet de déplacer une forme à la position lig, col vers le droite
     * @param lig, un int qui représente la lignes
     * @param col, un int qui représente la colonnes
     */
    private void deplacementDroite(int lig, int col){
        Forme formeADeplacer = this.getFormeParLigneColonne(lig, col);
        this.setFormeParLigneColonne(lig, col+1, formeADeplacer);
        this.setFormeParLigneColonne(lig, col, null);
    }

    /**
     * Permet de déplacer une forme à la position lig, col vers le gauche
     * @param lig, un int qui représente la lignes
     * @param col, un int qui représente la colonnes
     */
    private void deplacementGauche(int lig, int col){
        Forme formeADeplacer = this.getFormeParLigneColonne(lig, col);
        this.setFormeParLigneColonne(lig, col-1, formeADeplacer);
        this.setFormeParLigneColonne(lig, col, null);
    }

    /**
     * Récupérer la grille
     * @return la grille courante
     */
    public List<Forme> getGrille() {
        return grille;
    }

    /**
     * Mélange la grille avec des déplacements autorisés
     */
    public void deplacementAleatoire() {
        int i = 0;
        for (Forme forme : this.grille) {
            if (forme == null) {
                deplacement(i/3, i%3);
                break;
            }
        }
    }

    public void dessinerFormes(float[] scratch){
        for (Forme forme: this.grille ) {
            if (forme != null) {
                forme.draw(scratch);
            }
        }
    }
}
