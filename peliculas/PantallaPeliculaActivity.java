package es.umh.dadm.mispelis74374423j.practica2.peliculas;

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
import es.umh.dadm.mispelis74374423j.practica2.plataformas.EditarPlataformaActivity;
import es.umh.dadm.mispelis74374423j.practica2.plataformas.PantallaPlataformaActivity;

public class PantallaPeliculaActivity extends AppCompatActivity {

    private TextView titulo, duracion, genero, calificacion;
    private int id_platafo, id_usuario, id_pelicula;
    private ImageView img_recuperar;
    private UsuariosSQLiteHelper usuBD;
    private static final String nameBD = "BDUsuarios";
    private static final int VERSION = UsuariosSQLiteHelper.VERSION;
    private static final String identificador = "identificador_usuario";
    private static final String plataformador = "id_plataforma";
    private static final String peliformador = "id_pelicula";

    /**
     * Aqui declaramos todos los componentes y los linkamos con las variables, tambn hacemos las llamadas
     * a los metodos correspondientes
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_pelicula);

        titulo = (TextView) findViewById(R.id.tv_titulo);
        duracion = (TextView) findViewById(R.id.tv_duracion);
        genero = (TextView) findViewById(R.id.tv_genero);
        calificacion = (TextView) findViewById(R.id.tv_calificacion);
        img_recuperar = (ImageView) findViewById(R.id.imagen_recuperar);

        usuBD = new UsuariosSQLiteHelper(this, nameBD, null, VERSION);

        id_platafo = Integer.parseInt(getIntent().getStringExtra(plataformador));
        id_usuario = getIntent().getIntExtra(identificador, -1);
        id_pelicula = Integer.parseInt(getIntent().getStringExtra(peliformador));

        Pelicula peli1 = usuBD.traemeloAquiPelicula(id_pelicula);

        titulo.setText(peli1.getNombre_peli());
        duracion.setText(Integer.toString(peli1.getDuracion()));
        genero.setText(peli1.getGenero());
        calificacion.setText(Integer.toString(peli1.getCalificacion()));
        img_recuperar.setImageBitmap(peli1.getImagen_caratula());

    }

    /**
     * desplegar el menu
     * @param menu
     * @return
     */
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.editar_peliculas, menu);
        return true;
    }

    /**
     * composicion del menu
     * @param item
     * @return
     */
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();

        if (id == R.id.Editar_peliculas){
            Intent editar = new Intent(PantallaPeliculaActivity.this, EditarPeliculasActivity.class);
            editar.putExtra(plataformador,id_platafo);
            editar.putExtra(identificador,id_usuario);
            editar.putExtra(peliformador,id_pelicula);
            PantallaPeliculaActivity.this.startActivity(editar);

        }
        return super.onOptionsItemSelected(item);
    }


}