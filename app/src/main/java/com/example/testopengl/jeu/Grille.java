package com.example.testopengl.jeu;

import android.util.Log;

import com.example.testopengl.formes.Forme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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

        // TODO : faire en sorte que le mélange se fasse pas au début
        for (int i = 0; i < 500; i++) {
            this.deplacementAleatoire();
        }
        Log.d("grille melangee", this.grille.toString());
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

    /**
     * Permet de savoir si une forme est deplaçable vers une autre.
     * @param ligne, un int qui représente la ligne de destination de la forme
     * @param colonne, un int qui représente la colonne de destination de la forme
     * @return un boolean.
     * Retourne false si c'est impossible de déplacer cette case,
     * * true sinon (et déplace la case dans ce cas là)
     */
    public boolean deplacementPossible(int ligne, int colonne) {
        // pour pouvoir déplacer une forme vers le haut,
        // il faut qu'on ne soit pas dans la ligne la plus haute
        // et qu'il n'y ait rien dans la case au dessus
        if (ligne != 0 && getFormeParLigneColonne(ligne - 1, colonne) == null) {
            //this.deplacementHaut(ligne, colonne);
            return true;
        } else if (ligne != nbLignes - 1 && getFormeParLigneColonne(ligne + 1, colonne) == null) {
            //this.deplacementBas(ligne, colonne);
            return true;
        } else if (colonne != nbColonnes - 1 && getFormeParLigneColonne(colonne + 1, ligne) == null) {
            //this.deplacementDroite(ligne, colonne);
            return true;
        } else if (colonne != 0 && getFormeParLigneColonne(colonne - 1, ligne) == null) {
            //this.deplacementGauche(ligne, colonne);
            return true;
        }
        return false;
    }

    public ArrayList<Integer> getDeplacementsPossiblesCaseVide(int ligne, int colonne) {
        ArrayList<Integer> deplacementsPossibles = new ArrayList<>();
        if (ligne != 0) { // on peut déplacer le vide vers le haut
            deplacementsPossibles.add(1);
        }
        if (ligne != nbLignes - 1) { // on peut déplacer le vide vers le bas
            deplacementsPossibles.add(2);
        }
        if (colonne != nbColonnes - 1) { // on peut déplacer le vide vers la droite
            deplacementsPossibles.add(3);
        }
        if (colonne != 0) { // on peut déplacer le vide vers la gauche
            deplacementsPossibles.add(4);
        }
        return deplacementsPossibles;
    }

    /**
     * Permet de savoir si une forme est deplaçable vers une autre.
     * @param ligne, un int qui représente la ligne de destination de la forme
     * @param colonne, un int qui représente la colonne de destination de la forme
     * @return un boolean.
     * Retourne false si c'est impossible de déplacer cette case,
     * * true sinon (et déplace la case dans ce cas là)
     */
    public void deplacement(int ligne, int colonne) {
        // pour pouvoir déplacer une forme vers le haut,
        // il faut qu'on ne soit pas dans la ligne la plus haute
        // et qu'il n'y ait rien dans la case au dessus
        if (ligne != 0 && getFormeParLigneColonne(ligne - 1, colonne) == null) {
            this.deplacementHaut(ligne, colonne);
        } else if (ligne != nbLignes - 1 && getFormeParLigneColonne(ligne + 1, colonne) == null) {
            this.deplacementBas(ligne, colonne);
        } else if (colonne != nbColonnes - 1 && getFormeParLigneColonne(colonne + 1, ligne) == null) {
            this.deplacementDroite(ligne, colonne);
        } else if (colonne != 0 && getFormeParLigneColonne(colonne - 1, ligne) == null) {
            this.deplacementGauche(ligne, colonne);
        }
    }

    /**
     * Permet de déplacer une forme à la position lig, col vers le haut
     * @param lig, un int qui représente la lignes
     * @param col, un int qui représente la colonnes
     */
    private void deplacementHaut(int lig, int col){
        Forme formeSource = this.getFormeParLigneColonne(lig, col);
        Forme formeDestination = this.getFormeParLigneColonne(lig-1, col);
        this.setFormeParLigneColonne(lig, col, formeDestination); // et on la retire de la case actuelle
        this.setFormeParLigneColonne(lig-1, col, formeSource); // on met dans la forme dans la case au-dessus

    }

    /**
     * Permet de déplacer une forme à la position lig, col vers le bas
     * @param lig, un int qui représente la lignes
     * @param col, un int qui représente la colonnes
     */
    private void deplacementBas(int lig, int col){
        Forme formeSource = this.getFormeParLigneColonne(lig, col);
        Forme formeDestination = this.getFormeParLigneColonne(lig+1, col);
        this.setFormeParLigneColonne(lig, col, formeDestination);
        this.setFormeParLigneColonne(lig+1, col, formeSource);
    }

    /**
     * Permet de déplacer une forme à la position lig, col vers le droite
     * @param lig, un int qui représente la lignes
     * @param col, un int qui représente la colonnes
     */
    private void deplacementDroite(int lig, int col){
        Forme formeSource = this.getFormeParLigneColonne(lig, col);
        Forme formeDestination = this.getFormeParLigneColonne(lig, col+1);
        this.setFormeParLigneColonne(lig, col, formeDestination);
        this.setFormeParLigneColonne(lig, col+1, formeSource);
    }

    /**
     * Permet de déplacer une forme à la position lig, col vers le gauche
     * @param lig, un int qui représente la lignes
     * @param col, un int qui représente la colonnes
     */
    private void deplacementGauche(int lig, int col){
        Forme formeSource = this.getFormeParLigneColonne(lig, col);
        Forme formeDestination = this.getFormeParLigneColonne(lig, col-1);
        this.setFormeParLigneColonne(lig, col, formeDestination);
        this.setFormeParLigneColonne(lig, col-1, formeSource);
    }

    /**
     * Récupérer la grille
     * @return la grille courante
     */
    public List<Forme> getGrille() {
        return this.grille;
    }

    /**
     * Mélange la grille avec des déplacements autorisés
     */
    public void deplacementAleatoire() {
        int i = 0;
        for (Forme forme : this.grille) {
            if (forme == null) {
                int ligne = i/3;
                int colonne = i%3;
                ArrayList<Integer> deplacementsPossibles = this.getDeplacementsPossiblesCaseVide(ligne, colonne);
//                Log.d("deplacementsPossibles", deplacementsPossibles.toString());
                Random rand = new Random();
                int deplacementChoisi = deplacementsPossibles.get(rand.nextInt(deplacementsPossibles.size()));
                switch (deplacementChoisi) {
                    case 1:
                        this.deplacementHaut(ligne, colonne);
                        break;
                    case 2:
                        this.deplacementBas(ligne, colonne);
                        break;
                    case 3:
                        this.deplacementDroite(ligne, colonne);
                        break;
                    case 4:
                        this.deplacementGauche(ligne, colonne);
                        break;
                }
                break;
            } else {
                i++;
            }
        }
    }

    /**
     * Permet de dessiner toutes formes qui sont dans la liste que contient la grille
     * @param scratch
     */
    public void dessinerFormes(float[] scratch){
        for (Forme forme: this.grille ) {
            if (forme != null) {
                forme.draw(scratch);
            }
        }
        for (Forme forme: this.grille ) {
            if (forme != null) {
                Log.d("deplacement", "dessinerFormes: " + Arrays.toString(forme.get_position()));
            }
        }
        Log.d("deplacement", "dessinerFormes: "+ this.grille);
    }
}
