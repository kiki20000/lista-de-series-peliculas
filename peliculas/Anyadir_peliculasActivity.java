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

public class Anyadir_peliculasActivity extends AppCompatActivity {

    private EditText et_titulo, et_duracion, et_genero, et_calificacion, et_plataforma;
    private Button guardar, foto, galeria;
    private Pelicula peli1;
    private UsuariosSQLiteHelper usuBD;
    private int id_usuario, comprobacion;
    private ImageView caratula;
    private static final String nameBD = "BDUsuarios";
    private static final int VERSION = UsuariosSQLiteHelper.VERSION;
    private static final String identificador = "identificador_usuario";
    private static final String dato = "data";
    private static final String imagen = "image/";

    /**
     * Funcion que asigna los valores previamente declarados, tamboien contiene los listeners de los botones
     * registrar y entrar, y tambien del TV de recordar la contraseña
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anyadir_peliculas);

        et_titulo = (EditText) findViewById(R.id.Titulo_pelicula);
        et_duracion = (EditText) findViewById(R.id.duracion);
        et_genero = (EditText) findViewById(R.id.Genero);
        et_calificacion = (EditText) findViewById(R.id.calificacion);
        et_plataforma = (EditText) findViewById(R.id.plataforma);
        caratula = (ImageView) findViewById(R.id.imagenpeli);
        guardar = (Button) findViewById(R.id.guardar);
        foto = (Button) findViewById(R.id.btn_foto);
        galeria = (Button) findViewById(R.id.btn_galeria);

        usuBD = new UsuariosSQLiteHelper(this, nameBD, null, VERSION);
        id_usuario = getIntent().getIntExtra(identificador, -1);

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
                validarcampos();
            }
        });

    }

    /**
     * metodo para intentar abrir la camara
     */
    private void abrircamara(){

        Intent camara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camara, 1);

    }

    /**
     * metodo para intentar abrir la galeria
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
     * metodo para agregar una pelicula si toddo esta correcto
     */
    public void validarcampos(){

        String obli = getString(R.string.obli);
        String valorar = getString(R.string.valorar);
        String noexisteplataforma = getString(R.string.noexisteplataforma);
        String anyadirbn = getString(R.string.anyadirbn);
        String ERROR = getString(R.string.ERROR);

        String titulo = et_titulo.getText().toString();
        String plataforma = et_plataforma.getText().toString();
        String duracion = et_duracion.getText().toString();
        String genero = et_genero.getText().toString();
        String calificacion = et_calificacion.getText().toString();

        int calificacion1 = Integer.parseInt(calificacion);
        int duracion1 = Integer.parseInt(duracion);

        if(titulo.isEmpty() || plataforma.isEmpty() || duracion.isEmpty() || genero.isEmpty() || calificacion.isEmpty()){

            Toast.makeText(this, obli, Toast.LENGTH_SHORT).show();
        }
        else {

            if(calificacion1 < 0 || calificacion1 > 5) {
                Toast.makeText(this, valorar, Toast.LENGTH_SHORT).show();
            }
            else {
                comprobacion = usuBD.consultarBDPeliculas(id_usuario, plataforma);

                if (comprobacion == -1){
                    Toast.makeText(this, noexisteplataforma, Toast.LENGTH_SHORT).show();
                }
                else{

                    peli1 = new Pelicula();
                    peli1.setId_usuario(id_usuario);
                    peli1.setId_plataforma(comprobacion);
                    peli1.setNombre_peli(titulo);
                    peli1.setDuracion(duracion1);
                    peli1.setGenero(genero);
                    peli1.setCalificacion(calificacion1);

                    Bitmap bitmap;

                    if(null!=caratula.getDrawable())
                    {
                        caratula.invalidate();
                        BitmapDrawable drawable = (BitmapDrawable) caratula.getDrawable();
                        bitmap = drawable.getBitmap();
                    }else{
                        Toast.makeText(this, ERROR, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    peli1.setImagen_caratula(bitmap);


                    usuBD.agregarpeliculas(peli1);
                    Toast.makeText(this, anyadirbn, Toast.LENGTH_SHORT).show();

                    Intent volver = new Intent(Anyadir_peliculasActivity.this, PeliculasActivity.class);
                    volver.putExtra(identificador, id_usuario);
                    startActivity(volver);
                }
            }

        }

    }


}