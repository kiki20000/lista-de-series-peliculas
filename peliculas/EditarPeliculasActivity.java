package es.umh.dadm.mispelis74374423j.practica2.peliculas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import es.umh.dadm.mispelis74374423j.R;
import es.umh.dadm.mispelis74374423j.practica2.UsuariosSQLiteHelper;
import es.umh.dadm.mispelis74374423j.practica2.plataformas.Platafor;

public class EditarPeliculasActivity extends AppCompatActivity {

    private EditText titulo, plataforma, duracion, genero, calificacion;
    private Button guardar, foto, galeria;
    private int id_plataforma, id_usuario, id_pelicula, comprobacion;
    private UsuariosSQLiteHelper usuBD;
    private Pelicula peli1;
    private ImageView caratula;
    private static final String nameBD = "BDUsuarios";
    private static final int VERSION = UsuariosSQLiteHelper.VERSION;
    private static final String identificador = "identificador_usuario";
    private static final String plataformador = "id_plataforma";
    private static final String peliformador = "id_pelicula";
    private static final String dato = "data";
    private static final String imagen = "image/";

    /**
     * Declaraciones de los componentes y llamadas a los metodos segun el boton
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_peliculas);

        titulo = (EditText) findViewById(R.id.et_titulo_editar);
        plataforma = (EditText) findViewById(R.id.et_nombreplataforma_editar);
        duracion = (EditText) findViewById(R.id.et_duracion_editar);
        genero = (EditText) findViewById(R.id.et_genero_editar);
        calificacion = (EditText) findViewById(R.id.et_calificacion_editar);
        caratula = (ImageView) findViewById(R.id.im_editar_pelicula);
        guardar = (Button) findViewById(R.id.guardar_editar);
        foto = (Button) findViewById(R.id.btn_foto_editar_peliculas);
        galeria = (Button) findViewById(R.id.btn_galeria_editar_peliculas);

        id_plataforma = getIntent().getIntExtra(plataformador, -1);
        id_usuario = getIntent().getIntExtra(identificador, -1);
        id_pelicula = getIntent().getIntExtra(peliformador, -1);

        usuBD = new UsuariosSQLiteHelper(this, nameBD, null, VERSION);

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrircamara();
            }
        });

        galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarGaleria();
            }
        });


        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarCampos()) {
                    Intent volver = new Intent(EditarPeliculasActivity.this, PeliculasActivity.class);
                    volver.putExtra(identificador, id_usuario);
                    startActivity(volver);
                }
            }
        });
    }

    /**
     * intento de abrir la camara
     */
    private void abrircamara(){

        Intent camara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camara, 1);

    }

    /**
     * intento de abrir la galeria
     */
    private void cargarGaleria(){
        Intent galeria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galeria.setType(imagen);
        startActivityForResult(galeria, 2);
    }

    /**
     * metodo para que guarde el resultado tanto de la galeria como de la camara
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String cancel = getString(R.string.cancel);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imagencaratula = (Bitmap) extras.get(dato);
            caratula.setImageBitmap(imagencaratula);
        }
        if(requestCode == 2 && resultCode == RESULT_OK){
            Uri path = data.getData();
            caratula.setImageURI(path);
        }
        if(resultCode == RESULT_CANCELED){
            Toast.makeText(this, cancel, Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * metodo que hace las comprobaciones pertinentes y si toddo es correcto actualiza la pelicula
     * @return
     */
    private boolean validarCampos() {

        String rellene = getString(R.string.rellene);
        String introduzca = getString(R.string.introduzca);
        String la_plataforma = getString(R.string.la_plataforma);

        String titulos = titulo.getText().toString();
        String plataformas = plataforma.getText().toString();
        String duraciones = duracion.getText().toString();
        String generos = genero.getText().toString();
        String calificaciones = calificacion.getText().toString();

        int calificaciones1 = Integer.parseInt(calificaciones);
        int duraciones1 = Integer.parseInt(duraciones);

        if (titulos.isEmpty() || plataformas.isEmpty() || duraciones.isEmpty() || generos.isEmpty() || calificaciones.isEmpty()) {

            Toast.makeText(this, rellene, Toast.LENGTH_SHORT).show();
            return false;
        } else {

            if (calificaciones1 < 0 || calificaciones1 > 5) {

                Toast.makeText(this, introduzca, Toast.LENGTH_SHORT).show();
                return false;
            } else {

                comprobacion = usuBD.consultarBDPeliculas(id_usuario, plataformas);

                if (comprobacion == -1){
                    Toast.makeText(this, la_plataforma, Toast.LENGTH_SHORT).show();
                    return false;
                }
                else {

                    peli1 = new Pelicula();
                    peli1.setId_usuario(id_usuario);
                    peli1.setId_plataforma(comprobacion);
                    peli1.setNombre_peli(titulos);
                    peli1.setDuracion(duraciones1);
                    peli1.setGenero(generos);
                    peli1.setCalificacion(calificaciones1);

                    Bitmap bitmap;

                    if(null!=caratula.getDrawable())
                    {
                        caratula.invalidate();
                        BitmapDrawable drawable = (BitmapDrawable) caratula.getDrawable();
                        bitmap = drawable.getBitmap();
                        peli1.setImagen_caratula(bitmap);
                    }

                    usuBD.modificarPeliculas(id_pelicula, peli1);

                    return true;
                }
            }
        }
    }
}