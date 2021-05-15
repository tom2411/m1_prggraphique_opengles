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
package com.example.testopengl.formes;

import android.opengl.GLES30;
import android.util.Log;

import com.example.testopengl.MyGLRenderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

//import android.opengl.GLES20;


//Dessiner un carré

public class Triangle implements Forme{
/* Le vertex shader avec la définition de gl_Position et les variables utiles au fragment shader
 */
    private final String vertexShaderCode =
        "#version 300 es\n"+
                "uniform mat4 uMVPMatrix;\n"+
            "in vec3 vPosition;\n" +
                "in vec4 vCouleur;\n"+
                "out vec4 Couleur;\n"+
                "out vec3 Position;\n"+
            "void main() {\n" +
                "Position = vPosition;\n"+
            "gl_Position = uMVPMatrix * vec4(vPosition,1.0);\n" +
                "Couleur = vCouleur;\n"+
            "}\n";

    private final String fragmentShaderCode =
            "#version 300 es\n"+
            "precision mediump float;\n" + // pour définir la taille d'un float
            "in vec4 Couleur;\n"+
            "in vec3 Position;\n"+
            "out vec4 fragColor;\n"+
            "void main() {\n" +// transformation du carré en rond
            /*"float x = Position.x;\n"+
            "float y = Position.y;\n"+
            "float test = x*x+y*y;\n"+
            "if (test>1.0) \n"+
                "discard;\n"+*/
            "fragColor = Couleur;\n" +
            "}\n"
            ;

    /* les déclarations pour l'équivalent des VBO */

    private FloatBuffer vertexBuffer; // Pour le buffer des coordonnées des sommets du carré
    private ShortBuffer indiceBuffer; // Pour le buffer des indices
    private FloatBuffer colorBuffer; // Pour le buffer des couleurs des sommets

    /* les déclarations pour les shaders
    Identifiant du programme et pour les variables attribute ou uniform
     */
    private int IdProgram; // identifiant du programme pour lier les shaders
    private int IdPosition; // idendifiant (location) pour transmettre les coordonnées au vertex shader
    private int IdCouleur; // identifiant (location) pour transmettre les couleurs
    private int IdMVPMatrix; // identifiant (location) pour transmettre la matrice PxVxM

    static final int COORDS_PER_VERTEX = 3; // nombre de coordonnées par vertex
    static final int COULEURS_PER_VERTEX = 4; // nombre de composantes couleur par vertex

    int []linkStatus = {0};

    /* Attention au repère au niveau écran (x est inversé)
     Le tableau des coordonnées des sommets
     Oui ce n'est pas joli avec 1.0 en dur ....
     */

    // tableau qui sert de mémoire à la forme pour quelle utilise toujours le repère
    // qui part de 0/ milieu de l'ecran
     float initTriangleCoords[] = {
            0.0f,   1.0f, 0.0f,
            -1.0f,  -1.0f, 0.0f,
            1.0f,  -1.0f, 0.0f
    };

     float triangleCoords[] = {
            0.0f,   1.0f, 0.0f,
            -1.0f,  -1.0f, 0.0f,
            1.0f,  -1.0f, 0.0f
            };
    // Le tableau des couleurs
     float triangleColors[] = {
             1.0f,  0.0f, 0.0f, 1.0f,
             1.0f,  0.0f, 0.0f, 1.0f,
             1.0f,  0.0f, 0.0f, 1.0f,
             };

    // Le carré est dessiné avec 2 triangles
    private final short Indices[] = {0, 1, 2};

    private final int vertexStride = COORDS_PER_VERTEX * 4; // le pas entre 2 sommets : 4 bytes per vertex

    private final int couleurStride = COULEURS_PER_VERTEX * 4; // le pas entre 2 couleurs

    private final float Position[] = {0.0f,0.0f};

    public Triangle(float[] Pos) {

        // positionnnement de la forme en fonction du paramètre du constructeur et de sa position initial
        // avec le repère du milieu l'écran
        Position[0] = Pos[0];
        Position[1] = Pos[1];
        for (int i = 0; i < triangleCoords.length-1; i+=3) {
            triangleCoords[i] = initTriangleCoords[i] + Position[0];
            triangleCoords[i+1] = initTriangleCoords[i+1] + Position[1];
        }



    }


    public void set_position(float[] pos) {
        for (int i = 0; i < triangleCoords.length-1; i+=3) {
            triangleCoords[i] = initTriangleCoords[i] + pos[0];
            triangleCoords[i+1] = initTriangleCoords[i+1] + pos[1];
        }
        Log.d("deplacement", "pos[0]= "+pos[0]+" ,pos[1]= "+pos[1]);
        Position[0]=pos[0];
        Position[1]=pos[1];
    }

    @Override
    public float[] get_position() {
            return this.Position;
    }

    /* La fonction Display */
    public void draw(float[] mvpMatrix) {
        // initialisation du buffer pour les vertex (4 bytes par float)
        ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(triangleCoords);
        vertexBuffer.position(0);


        // initialisation du buffer pour les couleurs (4 bytes par float)
        ByteBuffer bc = ByteBuffer.allocateDirect(triangleColors.length * 4);
        bc.order(ByteOrder.nativeOrder());
        colorBuffer = bc.asFloatBuffer();
        colorBuffer.put(triangleColors);
        colorBuffer.position(0);

        // initialisation du buffer des indices
        ByteBuffer dlb = ByteBuffer.allocateDirect(Indices.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        indiceBuffer = dlb.asShortBuffer();
        indiceBuffer.put(Indices);
        indiceBuffer.position(0);

        /* Chargement des shaders */
        int vertexShader = MyGLRenderer.loadShader(
                GLES30.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(
                GLES30.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        IdProgram = GLES30.glCreateProgram();             // create empty OpenGL Program
        GLES30.glAttachShader(IdProgram, vertexShader);   // add the vertex shader to program
        GLES30.glAttachShader(IdProgram, fragmentShader); // add the fragment shader to program
        GLES30.glLinkProgram(IdProgram);                  // create OpenGL program executables
        GLES30.glGetProgramiv(IdProgram, GLES30.GL_LINK_STATUS,linkStatus,0);

        // Add program to OpenGL environment
        GLES30.glUseProgram(IdProgram);

           // get handle to shape's transformation matrix
        IdMVPMatrix = GLES30.glGetUniformLocation(IdProgram, "uMVPMatrix");

        // Apply the projection and view transformation
        GLES30.glUniformMatrix4fv(IdMVPMatrix, 1, false, mvpMatrix, 0);


        // get handle to vertex shader's vPosition member et vCouleur member
        IdPosition = GLES30.glGetAttribLocation(IdProgram, "vPosition");
        IdCouleur = GLES30.glGetAttribLocation(IdProgram, "vCouleur");

        /* Activation des Buffers */
        GLES30.glEnableVertexAttribArray(IdPosition);
        GLES30.glEnableVertexAttribArray(IdCouleur);

        /* Lecture des Buffers */
        GLES30.glVertexAttribPointer(
                IdPosition, COORDS_PER_VERTEX,
                GLES30.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        GLES30.glVertexAttribPointer(
                IdCouleur, COULEURS_PER_VERTEX,
                GLES30.GL_FLOAT, false,
                couleurStride, colorBuffer);




        // Draw the square
        GLES30.glDrawElements(
                GLES30.GL_TRIANGLES, Indices.length,
                GLES30.GL_UNSIGNED_SHORT, indiceBuffer);


        // Disable vertex array
        GLES30.glDisableVertexAttribArray(IdPosition);
        GLES30.glDisableVertexAttribArray(IdCouleur);

    }

}
