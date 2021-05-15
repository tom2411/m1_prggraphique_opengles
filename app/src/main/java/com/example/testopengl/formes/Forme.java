package com.example.testopengl.formes;

public interface Forme {
    void draw(float[] mvpMatrix);
    void set_position(float[] pos);
    float[] get_position();
}
