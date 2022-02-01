package es.umh.dadm.mispelis74374423j.practica2.plataformas;

import android.graphics.Bitmap;

public class Platafor {

    private int id;
    private int id_usu;
    private Bitmap imagen;
    private String nombre;
    private String url;
    private String acceso;
    private String password_plataforma;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_usu() {
        return id_usu;
    }

    public void setId_usu(int id_usu) {
        this.id_usu = id_usu;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAcceso() {
        return acceso;
    }

    public void setAcceso(String acceso) {
        this.acceso = acceso;
    }

    public String getPassword_plataforma() {
        return password_plataforma;
    }

    public void setPassword_plataforma(String password_plataforma) {
        this.password_plataforma = password_plataforma;
    }

}
