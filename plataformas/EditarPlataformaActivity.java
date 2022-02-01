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

public class EditarPlataformaActivity extends AppCompatActivity {

    private EditText nombre_plataforma, nombre_usuario, nombre_url, nombre_password;
    private Button btn_guardar, foto, galeria;
    private int id_plataform, id_usuario;
    private UsuariosSQLiteHelper usuBD;
    private Platafor plata1;
    private ImageView caratula;
    private static final String nameBD = "BDUsuarios";
    private static final int VERSION = UsuariosSQLiteHelper.VERSION;
    private static final String identificador = "identificador_usuario";
    private static final String plataformador = "id_plataforma";
    private static final String dato = "data";
    private static final String imagen = "image/";

    /**
     * metodo para unir los componentes y hacer las llamadas a los otros metodos
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_plataforma);

        nombre_plataforma = (EditText) findViewById(R.id.et_nombre_plataforma);
        nombre_url = (EditText) findViewById(R.id.et_url_plataforma);
        nombre_usuario = (EditText) findViewById(R.id.et_usuario_plataformas);
        nombre_password = (EditText) findViewById(R.id.et_password_plataforma);
        caratula = (ImageView) findViewById(R.id.im_plataforma_editar);
        btn_guardar = (Button) findViewById(R.id.Guardar_editar);
        foto = (Button) findViewById(R.id.btn_plataforma_editar_foto);
        galeria = (Button) findViewById(R.id.btn_plataforma_editar_galeria);

        id_plataform = getIntent().getIntExtra(plataformador, -1);
        id_usuario = getIntent().getIntExtra(identificador, -1);
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

        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarCampos()){

                    String actualizacion = getString(R.string.actualizacion);

                    Toast.makeText(EditarPlataformaActivity.this, actualizacion, Toast.LENGTH_SHORT).show();
                    Intent regreso = new Intent(EditarPlataformaActivity.this, PlataformasActivity.class);
                    regreso.putExtra(plataformador,id_plataform);
                    regreso.putExtra(identificador,id_usuario);
                    startActivity(regreso);
                }
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
     * metodo para abrir la galeria
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
     * metodo para hacer las comproaciones pertinentes y si corresponde actualizar la plataforma
     * @return
     */
    private boolean validarCampos(){

        String rellene = getString(R.string.rellene);

        String plataforma = nombre_plataforma.getText().toString();
        String url = nombre_url.getText().toString();
        String usuario = nombre_usuario.getText().toString();
        String password = nombre_password.getText().toString();

        if(plataforma.isEmpty() || url.isEmpty() || usuario.isEmpty() || password.isEmpty()){

            Toast.makeText(this, rellene, Toast.LENGTH_SHORT).show();
            return false;

        }
        else{

            plata1 = new Platafor();
            plata1.setId_usu(id_usuario);
            plata1.setNombre(plataforma);
            plata1.setUrl(url);
            plata1.setAcceso(usuario);
            plata1.setPassword_plataforma(password);

            Bitmap bitmap;

            if(null!=caratula.getDrawable())
            {
                caratula.invalidate();
                BitmapDrawable drawable = (BitmapDrawable) caratula.getDrawable();
                bitmap = drawable.getBitmap();
                plata1.setImagen(bitmap);
            }

            usuBD.modificarPlataformas(id_plataform, plata1);

            return true;
        }
    }
}