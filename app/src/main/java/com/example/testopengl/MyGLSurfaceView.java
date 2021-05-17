/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.testopengl;

import android.content.Context;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* La classe MyGLSurfaceView avec en particulier la gestion des événements
  et la création de l'objet renderer */

/* On va dessiner un carré qui peut se déplacer grace à une translation via l'écran tactile */

public class MyGLSurfaceView extends GLSurfaceView {

    /* Un attribut : le renderer (GLSurfaceView.Renderer est une interface générique disponible) */
    /* MyGLRenderer va implémenter les méthodes de cette interface */

    private final MyGLRenderer mRenderer;

    public MyGLSurfaceView(Context context) {
        super(context);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        // Création d'un context OpenGLES 2.0
        setEGLContextClientVersion(3);

        // Création du renderer qui va être lié au conteneur View créé
        mRenderer = new MyGLRenderer();
        setRenderer(mRenderer);

        // Option pour indiquer qu'on redessine uniquement si les données changent
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    /* Comment interpréter les événements sur l'écran tactile */
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // Les coordonnées du point touché sur l'écran
        float x = e.getX();
        float y = e.getY();

        // la taille de l'écran en pixels
        float screen_x = getWidth();
        float screen_y = getHeight();

        // Des messages si nécessaires */
        Log.d("message", "x"+Float.toString(x));
        Log.d("message", "y"+Float.toString(y));
        Log.d("message", "screen_x="+Float.toString(screen_x));
        Log.d("message", "screen_y="+Float.toString(screen_y));


        /* accès aux paramètres du rendu (cf MyGLRenderer.java)
        soit la position courante du centre du carré
         */
        float[] pos = mRenderer.getPosition();

        /* Conversion des coordonnées pixel en coordonnées OpenGL
        Attention l'axe x est inversé par rapport à OpenGLSL
        On suppose que l'écran correspond à un carré d'arête 2 centré en 0
         */

        float get_width=getWidth()/100.0f;
        float get_heigth=getHeight()/100.0f;

        float x_opengl = 2*get_width*x/getWidth() - get_width;
        float y_opengl = -2*get_heigth*y/getHeight() + get_heigth;

        Log.d("message","x_opengl="+Float.toString(x_opengl));
        Log.d("message","y_opengl="+Float.toString(y_opengl));

        /* Le carré représenté a une arête de 2 (oui il va falloir changer cette valeur en dur !!)
        /* On teste si le point touché appartient au carré ou pas car on ne doit le déplacer que si ce point est dans le carré
        */

        // Réprésentation des hit box des cases en OpenGl
        boolean test_case1 = ((x_opengl > pos[0]-11) && (x_opengl < pos[0]-3.5) && (y_opengl > pos[1]+3.25) && (y_opengl < pos[1]+9.75));
        boolean test_case2 = ((x_opengl > pos[0]-3.5) && (x_opengl < pos[0]+3.5) && (y_opengl > pos[1]+3.25) && (y_opengl < pos[1]+9.75));
        boolean test_case3 = ((x_opengl > pos[0]+3.5) && (x_opengl < pos[0]+11) && (y_opengl > pos[1]+3.25) && (y_opengl < pos[1]+9.75));

        boolean test_case4 = ((x_opengl > pos[0]-11) && (x_opengl < pos[0]-3.5) && (y_opengl > pos[1]-3.25) && (y_opengl < pos[1]+3.25));
        boolean test_case5 = ((x_opengl > pos[0]-3.5) && (x_opengl < pos[0]+3.5) && (y_opengl > pos[1]-3.25) && (y_opengl < pos[1]+3.25));
        boolean test_case6 = ((x_opengl > pos[0]+3.5) && (x_opengl < pos[0]+11) && (y_opengl > pos[1]-3.25) && (y_opengl < pos[1]+3.25));

        boolean test_case7 = ((x_opengl > pos[0]-11) && (x_opengl < pos[0]-3.5) && (y_opengl < pos[1]-3.25) && (y_opengl > pos[1]-9.75));
        boolean test_case8 = ((x_opengl > pos[0]-3.5) && (x_opengl < pos[0]+3.5) && (y_opengl < pos[1]-3.25) && (y_opengl > pos[1]-9.75));
        boolean test_case9 = ((x_opengl > pos[0]+3.5) && (x_opengl < pos[0]+11) && (y_opengl < pos[1]-3.25) && (y_opengl > pos[1]-9.75));

        Log.d("message","pos[0]= "+pos[0]+" ,pos[1]= "+pos[1]);

        // liste des cases avec les coordonnées OpenGl
        List<Boolean> liste_case = new ArrayList<>(Arrays.asList(test_case1,test_case2,test_case3,test_case4,test_case5,test_case6,test_case7,test_case8,test_case9));

        // Si le jeu est mélangé et que l'on a toucher une case
        if (this.mRenderer.getGame().isMelange() && (test_case1 || test_case2 || test_case3 || test_case4 || test_case5 || test_case6 || test_case7 || test_case8 || test_case9)) {
           switch (e.getAction()) {
                case MotionEvent.ACTION_UP:
                    // pour chaque case
                    for (int i = 0; i < liste_case.size(); i++) {
                        // Si la case i à été touché et que un déplacement est possible
                        if (liste_case.get(i) && this.mRenderer.getGame().mGrille().deplacementPossible(i/3, i%3)) {
                            MediaPlayer media = MediaPlayer.create(getContext(), R.raw.move);
                            media.start();
                            // echanger la forme avec la case vide
                            mRenderer.getGame().mGrille().deplacement(i/3, i%3);
                            requestRender(); // mise à jour de l'affichage
                            break;
                        // si la case à été touché mais qu'il n'y a pas de déplacement possible
                        } else if (liste_case.get(i) && !this.mRenderer.getGame().mGrille().deplacementPossible(i/3, i%3)){
                            // afficher un Toast
                            Toast.makeText(getContext(), "Il est impossible de déplacer cette forme !", Toast.LENGTH_SHORT).show();
                            // faire clignoter le fond en orange 2 fois
                            clignotement(2);
                            break;
                        }
                    }
                    // Si le jeu est résolue
                    if (this.mRenderer.getGame().isGrilleResolue()) {
                        MediaPlayer media = MediaPlayer.create(getContext(), R.raw.caught_a_pokemon);
                        media.start();


                        Log.d("Game", "La partie est terminée");
                        // afficher un toast
                        Toast.makeText(getContext(), "Félicitations ! \nCliquez n'importe où pour rejouer", Toast.LENGTH_LONG).show();
                        this.mRenderer.getGame().setMelange(false); // préparer la variable mélange pour la prochaine partie
                    }

            }
        }
        // Si le jeu n'est pas mélangé
        else if (!this.mRenderer.getGame().isMelange()){
            // si on touche l'écran
            if (e.getAction() == MotionEvent.ACTION_UP){
                Log.d("Avant mélange", ""+mRenderer.getGame().mGrille().getGrille());
                // on fait des déplacements possible x fois
                mRenderer.getGame().mGrille().melangerGrille(9);
                Log.d("Après mélange", ""+mRenderer.getGame().mGrille().getGrille());
                this.mRenderer.getGame().setMelange(true); // on met à jour le boolean de mélange
                Toast.makeText(getContext(), "La grille est mélangée, bon courage !", Toast.LENGTH_SHORT).show();
                requestRender();// on met à jour l'affichage
            }
        }
        return true;
    }

    /**
     * Permet de faire clignoter le fond
     * @param nbClignotement, un int qui dit combien de fois clignoter
     */
    public void clignotement(int nbClignotement){
        // pour nbClignotement de fois
        for (int i = 0; i < nbClignotement; i++) {
            try {
                // passer le fond en orange
                this.mRenderer.getGame().setPlateauColors(.87f,.43f,.08f);
                requestRender(); // mettre à jour l'affichage
                Thread.sleep(250); // attendre 250 ms
                // passer le fond en blanc
                this.mRenderer.getGame().setPlateauColors(0.93f,0.93f,0.82f);
                requestRender(); // mettre à jour l'affichage
                Thread.sleep(250); // attendre 250 ms
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }
    }

}
