package es.umh.dadm.mispelis74374423j.practica2.peliculas;

import android.graphics.Bitmap;
import android.media.Image;

public class Pelicula {

    private int id;
    private int id_usuario;
    private int id_plataforma;
    private Bitmap imagen_caratula;
    private String nombre_peli;
    private int duracion;
    private String genero;
    private int calificacion;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getId_plataforma() {
        return id_plataforma;
    }

    public void setId_plataforma(int id_plataforma) {
        this.id_plataforma = id_plataforma;
    }

    public Bitmap getImagen_caratula() {
        return imagen_caratula;
    }

    public void setImagen_caratula(Bitmap imagen_caratula) {
        this.imagen_caratula = imagen_caratula;
    }

    public String getNombre_peli() {
        return nombre_peli;
    }

    public void setNombre_peli(String nombre_peli) {
        this.nombre_peli = nombre_peli;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

}
