package es.umh.dadm.mispelis74374423j.practica2.plataformas;

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

public class anyadir_plataformaActivity extends AppCompatActivity {

    private EditText nombre_p, url_p, usu_p, password_p;
    private Button anyadir_p, foto, galeria;
    private UsuariosSQLiteHelper usuBD;
    private Platafor plata1;
    private int id_usuario, auxiliar;
    private ImageView caratula;
    private static final String nameBD = "BDUsuarios";
    private static final int VERSION = UsuariosSQLiteHelper.VERSION;
    private static final String identificador = "identificador_usuario";
    private static final String dato = "data";
    private static final String imagen = "image/";

    /**
     * metodo para unir los componentes, y hacer las llamadas pertinentes a los otros metodos
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anyadir_plataforma);

        nombre_p = (EditText) findViewById(R.id.nombrePlataforma);
        url_p = (EditText) findViewById(R.id.URLPlataforma);
        usu_p = (EditText) findViewById(R.id.UsuarioPlataforma);
        password_p = (EditText) findViewById(R.id.PasswordPlataforma);
        caratula = (ImageView) findViewById(R.id.im_plataforma_añadir);
        anyadir_p = (Button) findViewById(R.id.anyadirButton);
        foto = (Button) findViewById(R.id.btn_plataforma_foto_añadir);
        galeria = (Button) findViewById(R.id.btn_plataforma_galeria_añadir);

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

        anyadir_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarcampos();
            }
        });

    }

    /**
     * intentamos abrir la camara
     */
    private void abrircamara(){

        Intent camara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camara, 1);

    }

    /**
     * intentamos abrir la galeria
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
     * metodo que hace las comprobaciones pertinentes y si esta toddo bn nos agrega la plataforma
     */
    private void validarcampos(){

        String obli = getString(R.string.obli);
        String ya_registrada = getString(R.string.ya_registrada);
        String plata_anyadida = getString(R.string.plata_anyadida);
        String ERROR = getString(R.string.ERROR);

        String campo_nombre = nombre_p.getText().toString();
        String campo_url = url_p.getText().toString();
        String campo_usu = usu_p.getText().toString();
        String campo_pass = password_p.getText().toString();

        if(campo_nombre.isEmpty() || campo_url.isEmpty() || campo_usu.isEmpty() || campo_pass.isEmpty()){

            Toast.makeText(this, obli, Toast.LENGTH_SHORT).show();
        }
        else {

            plata1 = new Platafor();
            plata1.setId_usu(id_usuario);
            plata1.setNombre(campo_nombre);
            plata1.setUrl(campo_url);
            plata1.setAcceso(campo_usu);
            plata1.setPassword_plataforma(campo_pass);

            auxiliar = usuBD.consultarBDPlataforma(id_usuario,campo_nombre);

            if(auxiliar != -1){
                Toast.makeText(this, ya_registrada, Toast.LENGTH_SHORT).show();
            }
            else{

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

                plata1.setImagen(bitmap);

                usuBD.agregarplataforma(plata1);
                Toast.makeText(this, plata_anyadida, Toast.LENGTH_SHORT).show();
                Intent devolver = new Intent(anyadir_plataformaActivity.this, PlataformasActivity.class);
                devolver.putExtra(identificador,id_usuario);
                startActivity(devolver);
            }
        }

    }
}