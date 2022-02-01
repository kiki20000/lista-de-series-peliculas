package es.umh.dadm.mispelis74374423j.practica2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import es.umh.dadm.mispelis74374423j.practica1.Usuario;
import es.umh.dadm.mispelis74374423j.practica2.peliculas.Pelicula;
import es.umh.dadm.mispelis74374423j.practica2.plataformas.EditarPlataformaActivity;
import es.umh.dadm.mispelis74374423j.practica2.plataformas.Platafor;

public class UsuariosSQLiteHelper extends SQLiteOpenHelper {

    Cursor cursor;
    SQLiteDatabase db;

    // Aqui creamos la tabla de usuarios
    private static final String sqlCreate = "CREATE TABLE Usuarios (_id integer primary key autoincrement, email text not null, nombre text not null, password text not null, pregunta_seguridad text not null, respuesta_seguridad text not null, apellidos text, fecha text, interes1 boolean, interes2 boolean, interes3 boolean, interes4 boolean, interes5 boolean )";
    private static final String sqlCreate2 = "CREATE TABLE Plataformas (_id integer primary key autoincrement, id_usu integer not null, nombre text not null, url text not null, acceso text not null, password_plataforma text not null, imagen BLOB)";
    private static final String sqlCreate3 = "CREATE TABLE Peliculas (_id integer primary key autoincrement, id_usu integer not null, id_plataforma integer not null, titulo text not null, duracion int not null, genero text not null, calificacion int not null, imagen BLOB)";

    private static final String nameBD = "BDUsuarios";
    public static final int VERSION = 7;

    /////// NOMBRE TABLAS ////////
    private String tabla1 = "Usuarios";
    private String tabla2 = "Plataformas";
    private String tabla3 = "Peliculas";

   //////// NOMBRE REGISTROS /////////
   private String email = "email";
   private String nombre = "nombre";
   private String password = "password";
   private String pregunta_seguridad = "pregunta_seguridad";
   private String respuesta_seguridad = "respuesta_seguridad";
   private String id_usu = "id_usu";
   private String url = "url";
   private String acceso = "acceso";
   private String password_plataforma = "password_plataforma";
   private String id_plataforma = "id_plataforma";
   private String titulo = "titulo";
   private String duracion = "duracion";
   private String genero = "genero";
   private String calificacion = "calificacion";
   private String apellidos = "apellidos";
   private String fecha = "fecha";
   private String interes1 = "interes1";
   private String interes2 = "interes2";
   private String interes3 = "interes3";
   private String interes4 = "interes4";
   private String interes5 = "interes5";


   /////// CONDICIONES ///////
   private String id = "_id=";


    public UsuariosSQLiteHelper(@Nullable Context context, @Nullable String nameBD, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, nameBD, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Se ejecuta la sentencia de SQL de creacion de la tabla
        db.execSQL(sqlCreate);
        db.execSQL(sqlCreate2);
        db.execSQL(sqlCreate3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Se elimina la version anterior de la tabla
        //db.execSQL("DROP TABLE IF EXISTS Usuarios");

        // Se crea la nueva version de la tabla
        String Seguridad = "ALTER TABLE Plataformas ADD imagen BLOB";

        db.execSQL(Seguridad);
       // db.execSQL(sqlCreate2);
       // db.execSQL(sqlCreate3);

    }


    ////////// USUARIOS //////////

    public void agregarusuario(Usuario usu1){

        // metodo de insert() a usuarios
        ContentValues nuevoRegistro = asignarValores(usu1);
        db = getWritableDatabase();
        db.insert(tabla1, null, nuevoRegistro);

    }

    /**
     * metodo para asignar valores a un usuario
     * @param usu1
     * @return
     */
    private ContentValues asignarValores(Usuario usu1){

        ContentValues nuevoRegistro = new ContentValues();

        nuevoRegistro.put(email, usu1.getEmail());
        nuevoRegistro.put(nombre, usu1.getNombre());
        nuevoRegistro.put(password, usu1.getPassword());
        nuevoRegistro.put(pregunta_seguridad, usu1.getPregunta_seguridad());
        nuevoRegistro.put(respuesta_seguridad, usu1.getRespuesta());
        nuevoRegistro.put(apellidos, usu1.getApellidos());
        nuevoRegistro.put(fecha, usu1.getDate());
        nuevoRegistro.put(interes1, usu1.getCheckBox());
        nuevoRegistro.put(interes2, usu1.getCheckBox2());
        nuevoRegistro.put(interes3, usu1.getCheckBox4());
        nuevoRegistro.put(interes4, usu1.getCheckBox5());
        nuevoRegistro.put(interes5, usu1.getCheckBox6());

        return nuevoRegistro;
    }

    /**
     * metodo para consultar si existe o no un usuario
     * @param campoemail
     * @return
     */
    public int consultarSoloMail(String campoemail){
        db = getReadableDatabase();
        cursor = db.rawQuery("SELECT _id FROM Usuarios WHERE email = '" +campoemail+ "' ", null);

        if (cursor.moveToFirst()){
            return cursor.getInt(0);
        }
        else{
            return -1;
        }
    }

    /**
     * metodo que sirve para que si un usuario ha metido bn sus credenciales lo dejemos pasar
     * @param campomail
     * @param campopass
     * @return
     */
    public int consultarBD(String campomail, String campopass){

        db = getReadableDatabase();
        cursor = db.rawQuery("SELECT _id FROM Usuarios WHERE email = '" +campomail+ "' AND password = '" +campopass+"' ", null);

        if (cursor.moveToFirst()){
            return cursor.getInt(0);
        }
        else{
            return -1;
        }
    }

    /**
     * metodo para que si un usuario nos dice su pregunta de seguridad ok le devolvamos su contraseña
     * @param id_usu
     * @param pregunta
     * @param respuesta
     * @return
     */
    public boolean comprobarSeguridad(int id_usu, String pregunta, String respuesta){

        db = getReadableDatabase();
        cursor = db.rawQuery("SELECT _id FROM Usuarios WHERE _id = '" +id_usu+ "' AND pregunta_seguridad = '" +pregunta+"' AND  respuesta_seguridad = '"+respuesta+"' ", null);

        if (cursor.moveToFirst()){
            return true;
        }
        else{
            return false;
        }
    }


    ////////// PLATAFORMAS //////////

    /**
     * metodo para agragar plataformas
     * @param plataforma1
     */
    public void agregarplataforma(Platafor plataforma1){
        ContentValues nuevoRegistro1 = asignarValoresPlataforma(plataforma1);
        db = getWritableDatabase();
        db.insert(tabla2, null, nuevoRegistro1);

    }

    /**
     * metodo para actualizar plataformas
     * @param id_plataforma
     * @param plataforma1
     */
    public void modificarPlataformas(int id_plataforma, Platafor plataforma1){
        ContentValues modificacionPlataforma = asignarValoresPlataforma(plataforma1);
        db = getWritableDatabase();
        db.update(tabla2, modificacionPlataforma, id +id_plataforma, null);
    }

    /**
     * metodo para borrar las plataformas
     * @param id_plataforma
     */
    public void BorrarPlataforma(int id_plataforma){

        db = getWritableDatabase();

        cursor = db.rawQuery("SELECT _id FROM Peliculas WHERE id_plataforma = '" +id_plataforma+ "' ", null);

        if (cursor.moveToFirst()){

            do {
                int i =  cursor.getInt(0);
                BorrarPeliculas(i);
            }while (cursor.moveToNext());

            db.delete(tabla2, id +id_plataforma, null);

        }
        else{

            db.delete(tabla2, id +id_plataforma, null);
        }
    }

    /**
     * metodo para consultar si existe la plataforma
     * @param usuario
     * @param nombre_plataforma
     * @return
     */
    public int consultarBDPlataforma(int usuario, String nombre_plataforma){

        db = getReadableDatabase();
        cursor = db.rawQuery("SELECT id_usu FROM Plataformas WHERE id_usu = '" +usuario+ "' AND nombre = '" +nombre_plataforma+"' ", null);

        if (cursor.moveToFirst()){
            return cursor.getInt(0);
        }
        else{
            return -1;
        }
    }

    /**
     * metodo para asignar valores a la plataforma
     * @param plataforma1
     * @return
     */
    private ContentValues asignarValoresPlataforma(Platafor plataforma1){

        ContentValues nuevoRegistro1 = new ContentValues();

        nuevoRegistro1.put(id_usu, plataforma1.getId_usu());
        nuevoRegistro1.put(nombre, plataforma1.getNombre());
        nuevoRegistro1.put(url, plataforma1.getUrl());
        nuevoRegistro1.put(acceso, plataforma1.getAcceso());
        nuevoRegistro1.put(password_plataforma, plataforma1.getPassword_plataforma());
        nuevoRegistro1.put("imagen", Convertidor.getBytes(plataforma1.getImagen()));

        return nuevoRegistro1;
    }

    /**
     * metodo que nos devuelve un array de peliculas que despues iran al lv
     * @param id_usuario
     * @return
     */
    public ArrayList<Platafor> obtenerplataformas(int id_usuario){
        db = getWritableDatabase();

        cursor = db.rawQuery("SELECT * FROM Plataformas WHERE id_usu = '" +id_usuario+ "' ORDER BY  nombre", null);

        ArrayList<Platafor> arrayPlataformas = new ArrayList<Platafor>();
        Platafor platafor1;
        if (cursor.moveToFirst()){
            do {
                platafor1 = obtenerValoresPlataforma();
                arrayPlataformas.add(platafor1);
            }while (cursor.moveToNext());
        }
        return arrayPlataformas;
    }

    /**
     * metodo para obtener los valores de una plataforma
     * @return
     */
    private Platafor obtenerValoresPlataforma(){

        Platafor platafor1 = new Platafor();
        platafor1.setId(cursor.getInt(0));
        platafor1.setId_usu(cursor.getInt(1));
        platafor1.setNombre(cursor.getString(2));
        platafor1.setUrl(cursor.getString(3));
        platafor1.setAcceso(cursor.getString(4));
        platafor1.setPassword_plataforma(cursor.getString(5));
        platafor1.setImagen(Convertidor.getImage(cursor.getBlob(6)));

        return platafor1;
    }

    /**
     * metodo para seleccionar una plataforma en concreto y llevar el objeto a donde se llama
     * @param id_plataforma
     * @return
     */
    public Platafor traemeloAquiPlataforma(int id_plataforma){

        Platafor plataforma;

        db = getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM Plataformas WHERE _id = '" +id_plataforma+ "' ", null);

        cursor.moveToFirst();
        plataforma = obtenerValoresPlataforma();

        return plataforma;
    }


    ////////// PELICULAS //////////

    /**
     * metodo para agregar una pelicula
     * @param peli
     */
    public void agregarpeliculas(Pelicula peli){

        // metodo de insert() a peliculas
        ContentValues nuevoRegistro2 = asignarValoresPelis(peli);
        db = getWritableDatabase();
        db.insert(tabla3, null, nuevoRegistro2);

    }

    /**
     * metodo para asignar valores a las peliculas
     * @param peli
     * @return
     */
    private ContentValues asignarValoresPelis(Pelicula peli){

        ContentValues nuevoRegistro2 = new ContentValues();

        nuevoRegistro2.put(id_usu, peli.getId_usuario());
        nuevoRegistro2.put(id_plataforma, peli.getId_plataforma());
        nuevoRegistro2.put(titulo, peli.getNombre_peli());
        nuevoRegistro2.put(duracion, peli.getDuracion());
        nuevoRegistro2.put(genero, peli.getGenero());
        nuevoRegistro2.put(calificacion, peli.getCalificacion());
        nuevoRegistro2.put("imagen", Convertidor.getBytes(peli.getImagen_caratula()));

        return nuevoRegistro2;
    }

    /**
     * metodo que nos devuelve un array de peliculas que se nos mostrara despues en el lv
     * @param id_usuario
     * @return
     */
    public ArrayList<Pelicula> obtenerPeliculas(int id_usuario){
        db = getWritableDatabase();

        cursor = db.rawQuery("SELECT * FROM Peliculas WHERE id_usu = '" +id_usuario+ "' ORDER BY  titulo", null);

        ArrayList<Pelicula> arrayPeliculas = new ArrayList<Pelicula>();
        Pelicula peli1;
        if (cursor.moveToFirst()){
            do {
                peli1 = obtenerValoresPeliculas();
                arrayPeliculas.add(peli1);
            }while (cursor.moveToNext());
        }
        return arrayPeliculas;
    }

    /**
     * metodo para obtener los valores de las peliculas
     * @return
     */
    private Pelicula obtenerValoresPeliculas(){

        Pelicula peli1 = new Pelicula();
        peli1.setId(cursor.getInt(0));
        peli1.setId_usuario(cursor.getInt(1));
        peli1.setId_plataforma(cursor.getInt(2));
        peli1.setNombre_peli(cursor.getString(3));
        peli1.setDuracion(cursor.getInt(4));
        peli1.setGenero(cursor.getString(5));
        peli1.setCalificacion(cursor.getInt(6));
        peli1.setImagen_caratula(Convertidor.getImage(cursor.getBlob(7)));

        return peli1;
    }

    /**
     * metodo para seleccionar una peli en concreto y llevar el objeto a donde se llama
     * @param id_pelicula
     * @return
     */
    public Pelicula traemeloAquiPelicula(int id_pelicula){

        Pelicula pelicula;

        db = getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM Peliculas WHERE _id = '" +id_pelicula+ "' ", null);

        cursor.moveToFirst();
        pelicula = obtenerValoresPeliculas();

        return pelicula;
    }

    /**
     * metodo para comprobar si existe una pelicula
     * @param usuario
     * @param nombre_plataforma
     * @return
     */
    public int consultarBDPeliculas(int usuario, String nombre_plataforma){

        db = getReadableDatabase();
        cursor = db.rawQuery("SELECT _id FROM Plataformas WHERE id_usu = '" +usuario+ "' AND nombre = '" +nombre_plataforma+"' ", null);

        if (cursor.moveToFirst()){
            return cursor.getInt(0);
        }
        else{
            return -1;
        }
    }

    /**
     * metodo para actualizar una plataforma
     * @param id_pelicula
     * @param peli1
     */
    public void modificarPeliculas(int id_pelicula, Pelicula peli1){
        ContentValues modificacionPeliculas = asignarValoresPelis(peli1);
        db = getWritableDatabase();
        db.update(tabla3, modificacionPeliculas, id +id_pelicula, null);
    }

    /**
     * metodo para borrar una pelicula
     * @param id_pelicula
     */
    public void BorrarPeliculas(int id_pelicula){

        db = getWritableDatabase();
        db.delete(tabla3, id +id_pelicula, null);

    }

}
