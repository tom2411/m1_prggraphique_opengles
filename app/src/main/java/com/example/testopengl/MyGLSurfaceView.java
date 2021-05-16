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
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

import com.example.testopengl.formes.Forme;

import java.util.List;

/* La classe MyGLSurfaceView avec en particulier la gestion des événements
  et la création de l'objet renderer

*/


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

    private boolean isMelange = false;

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

        boolean test_square = ((x_opengl < pos[0]+1.0) && (x_opengl > pos[0]-1.0) && (y_opengl < pos[1]+1.0) && (y_opengl > pos[1]-1.0));
        boolean test_plateau = ((x_opengl < pos[0]+10.0) && (x_opengl > pos[0]-10.0) && (y_opengl < pos[1]+10.0) && (y_opengl > pos[1]-10.0));

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
        /*Log.d("message","test_square="+Boolean.toString(test_square));
        Log.d("message","condition="+Boolean.toString(condition));*/

        if (isMelange && (test_case1 || test_case2 || test_case3 || test_case4 || test_case5 || test_case6 || test_case7 || test_case8 || test_case9)) {
           switch (e.getAction()) {
                case MotionEvent.ACTION_UP:
                    List<Forme> liste_forme = this.mRenderer.getGame().mGrille().getGrille();
                    int lig = -1;
                    int col = -1;
                    for (int i = 0; i < liste_forme.size(); i++) {
                        if (liste_forme.get(i) == null){
                            lig = i / 3;
                            col = i % 3;
                        }
                    }
                    Log.d("deplacement","case vide : lig: "+lig+" ,col: "+col );
                    if (isMelange && test_case1 && this.mRenderer.getGame().mGrille().deplacementPossible(0,0)){
                        Log.d("deplacement", "deplacement disponible : "+this.mRenderer.getGame().mGrille().deplacementPossible(0,0));
                        Log.d("deplacement", "case1");
                        mRenderer.getGame().mGrille().deplacement(0,0);
                        requestRender();
                    }
                    if (isMelange && test_case2 && this.mRenderer.getGame().mGrille().deplacementPossible(0,1)){
                        Log.d("deplacement", "deplacement disponible : "+this.mRenderer.getGame().mGrille().deplacementPossible(0,1));
                        Log.d("deplacement", "case2");
                        mRenderer.getGame().mGrille().deplacement(0,1);
                        requestRender();
                    }
                    if (isMelange && test_case3 && this.mRenderer.getGame().mGrille().deplacementPossible(0,2)){
                        Log.d("deplacement", "deplacement disponible : "+this.mRenderer.getGame().mGrille().deplacementPossible(0,2));
                        Log.d("deplacement", "case3");
                        mRenderer.getGame().mGrille().deplacement(0,2);
                        requestRender();
                    }
                    if ( isMelange && test_case4 && this.mRenderer.getGame().mGrille().deplacementPossible(1,0)){
                        Log.d("deplacement", "deplacement disponible : "+this.mRenderer.getGame().mGrille().deplacementPossible(1,0));
                        Log.d("deplacement", "case4");
                        mRenderer.getGame().mGrille().deplacement(1,0);
                        requestRender();
                    }
                    if (isMelange && test_case5 && this.mRenderer.getGame().mGrille().deplacementPossible(1,1)){
                        Log.d("deplacement", "deplacement disponible : "+this.mRenderer.getGame().mGrille().deplacementPossible(1,1));
                        Log.d("deplacement", "case5");
                        mRenderer.getGame().mGrille().deplacement(1,1);
                        requestRender();
                    }
                    if (isMelange && test_case6 && this.mRenderer.getGame().mGrille().deplacementPossible(1,2)){
                        Log.d("deplacement", "deplacement disponible : "+this.mRenderer.getGame().mGrille().deplacementPossible(1,2));
                        Log.d("deplacement", "case6");
                        mRenderer.getGame().mGrille().deplacement(1,2);
                        requestRender();
                    }
                    if (isMelange && test_case7 && this.mRenderer.getGame().mGrille().deplacementPossible(2,0)){
                        Log.d("deplacement", "deplacement disponible : "+this.mRenderer.getGame().mGrille().deplacementPossible(2,0));
                        Log.d("deplacement", "case7");
                        mRenderer.getGame().mGrille().deplacement(2,0);
                        requestRender();
                    }
                    if (isMelange && test_case8 && this.mRenderer.getGame().mGrille().deplacementPossible(2,1)){
                        Log.d("deplacement", "deplacement disponible : "+this.mRenderer.getGame().mGrille().deplacementPossible(2,1));
                        Log.d("deplacement", "case8");
                        mRenderer.getGame().mGrille().deplacement(2,1);
                        requestRender();
                    }
                    if (isMelange && test_case9 && this.mRenderer.getGame().mGrille().deplacementPossible(2,2)){
                        Log.d("deplacement", "deplacement disponible : "+this.mRenderer.getGame().mGrille().deplacementPossible(2,2));
                        Log.d("deplacement", "case9");
                        mRenderer.getGame().mGrille().deplacement(2,2);
                        requestRender();
                    }
                    Log.d("grille résolue ?", ""+this.mRenderer.getGame().isGrilleResolue());
            }
        }
        if (!isMelange){
            if (e.getAction() == MotionEvent.ACTION_UP){
                Log.d("Avant mélange", ""+mRenderer.getGame().mGrille().getGrille());
                mRenderer.getGame().mGrille().melangerGrille(9);
                Log.d("Après mélange", ""+mRenderer.getGame().mGrille().getGrille());
                isMelange = true;
                requestRender();
            }

        }

        return true;
    }

}
