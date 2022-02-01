package es.umh.dadm.mispelis74374423j.practica2.plataformas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import es.umh.dadm.mispelis74374423j.R;
import es.umh.dadm.mispelis74374423j.practica2.UsuariosSQLiteHelper;

public class PantallaPlataformaActivity extends AppCompatActivity {

    private TextView nombre_plataforma, nombre_url, nombre_usu;
    private int id_platafo, id_usuario;
    private ImageView img_recuperar;
    private UsuariosSQLiteHelper usuBD;
    private static final String nameBD = "BDUsuarios";
    private static final int VERSION = UsuariosSQLiteHelper.VERSION;
    private static final String identificador = "identificador_usuario";
    private static final String plataformador = "id_plataforma";

    /**
     * metodo para unir los componentes y hacer las llamadas pertinentes a los metodos
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_plataforma);

        nombre_plataforma = (TextView) findViewById(R.id.tv_nombre_plataforma);
        nombre_url = (TextView) findViewById(R.id.tv_url);
        nombre_usu = (TextView) findViewById(R.id.tv_usu);
        img_recuperar = (ImageView) findViewById(R.id.imagen_plataforma);

        usuBD = new UsuariosSQLiteHelper(this, nameBD, null, VERSION);

        id_platafo = Integer.parseInt(getIntent().getStringExtra(plataformador));
        id_usuario = getIntent().getIntExtra(identificador, -1);

        Platafor plataforma1 = usuBD.traemeloAquiPlataforma(id_platafo);

        nombre_plataforma.setText(plataforma1.getNombre());
        nombre_url.setText(plataforma1.getUrl());
        nombre_usu.setText(plataforma1.getAcceso());
        img_recuperar.setImageBitmap(plataforma1.getImagen());

    }

    /**
     * abrir y cerrar el menu
     * @param menu
     * @return
     */
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.editarplataforma, menu);
        return true;
    }

    /**
     * muestra las opciones del menu
     * @param item
     * @return
     */
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();

        if (id == R.id.Editar_plataforma){
            Intent editar = new Intent(PantallaPlataformaActivity.this, EditarPlataformaActivity.class);
            editar.putExtra(plataformador,id_platafo);
            editar.putExtra(identificador,id_usuario);
            PantallaPlataformaActivity.this.startActivity(editar);
        }
        return super.onOptionsItemSelected(item);
    }



}