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

import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.example.testopengl.formes.Forme;
import com.example.testopengl.formes.Losange;
import com.example.testopengl.formes.Plateau;
import com.example.testopengl.formes.Square;
import com.example.testopengl.formes.Triangle;
import com.example.testopengl.jeu.Grille;

import java.util.ArrayList;
import java.util.Arrays;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/* MyGLRenderer implémente l'interface générique GLSurfaceView.Renderer */

public class MyGLRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "MyGLRenderer";

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

    // Les matrices habituelles Model/View/Projection

    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mModelMatrix = new float[16];

    private float[] mPlateauPosition = {0.0f, 0.0f};

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

    /* Première méthode équivalente à la fonction init en OpenGLSL */
    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        // la couleur du fond d'écran
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

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

        ArrayList<Forme> liste_forme = new ArrayList<>(Arrays.asList(losange1,losange2,losange3,carre1,carre2,carre3,triangle1,triangle2));
        mGrille = new Grille(3,3, liste_forme);

        for (int i = 0; i < 9; i++) {
            if (mGrille.getGrille().get(i) != null) {
                mGrille.getGrille().get(i).set_position(this.cases.get(i));
            }
        }

    }

    public Grille mGrille() {
        return this.mGrille;
    }

    public float[] mMVPMatrix(){
        return this.mMVPMatrix;
    }

    /* Deuxième méthode équivalente à la fonction Display */
    @Override
    public void onDrawFrame(GL10 unused) {
        float[] scratch = new float[16]; // pour stocker une matrice

        // glClear rien de nouveau on vide le buffer de couleur et de profondeur */
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);

        /* on utilise une classe Matrix (similaire à glm) pour définir nos matrices P, V et M*/

        /* Pour le moment on va utiliser une projection orthographique
           donc View = Identity
         */

        /*pour positionner la caméra mais ici on n'en a pas besoin*/

       // Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Matrix.setIdentityM(mViewMatrix,0);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        Matrix.setIdentityM(mModelMatrix,0);

        /* Pour définir une translation on donne les paramètres de la translation
        et la matrice (ici mModelMatrix) est multipliée par la translation correspondante
         */
        /*Matrix.translateM(mModelMatrix, 0, mPlateauPosition[0], mPlateauPosition[1], 0);
        Matrix.translateM(mModelMatrix, 0, case1[0], case1[1], 0);
        Matrix.translateM(mModelMatrix, 0, case2[0], case2[1], 0);
        Matrix.translateM(mModelMatrix, 0, case3[0], case3[1], 0);

        Matrix.translateM(mModelMatrix, 0, case4[0], case4[1], 0);
        Matrix.translateM(mModelMatrix, 0, case5[0], case5[1], 0);*/
        Matrix.translateM(mModelMatrix, 0, 0, 0, 0);

        /*Matrix.translateM(mModelMatrix, 0, case7[0], case7[1], 0);
        Matrix.translateM(mModelMatrix, 0, case8[0], case8[1], 0);*/


        /* scratch est la matrice PxVxM finale */
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mModelMatrix, 0);

        /* on appelle la méthode dessin du carré élémentaire */
        mPlateau.draw(scratch);
        mGrille.dessinerFormes(scratch);
    }

    /* équivalent au Reshape en OpenGLSL */
    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        /* ici on aurait pu se passer de cette méthode et déclarer
        la projection qu'à la création de la surface !!
         */
        int width_proj = width/100;
        int heigth_proj = height/100;
        GLES30.glViewport(0, 0, width, height);
        Matrix.orthoM(mProjectionMatrix, 0, -width_proj, width_proj, -heigth_proj, heigth_proj, -1.0f, 1.0f);

    }

    /* La gestion des shaders ... */
    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES30.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES30.GL_FRAGMENT_SHADER)
        int shader = GLES30.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES30.glShaderSource(shader, shaderCode);
        GLES30.glCompileShader(shader);

        return shader;
    }


    /* Les méthodes nécessaires à la manipulation de la position finale du carré */
    public void setPosition(float x, float y) {
        this.carre3.set_position(new float[]{x, y});
    }

    public float[] getPosition() {
        return mPlateauPosition;
    }

}
