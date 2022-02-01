package es.umh.dadm.mispelis74374423j.practica2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import es.umh.dadm.mispelis74374423j.R;
import es.umh.dadm.mispelis74374423j.practica2.peliculas.PeliculasActivity;
import es.umh.dadm.mispelis74374423j.practica2.plataformas.PlataformasActivity;

public class Inicio extends AppCompatActivity {

    private TextView plataformas, pelicul;
    private ImageView imagen_plataforma, imagen_peliculas;
    private int id_usuario;
    private static final String identificador = "identificador_usuario";

    /**
     * unimos los componentes y hacemos llamadas a los respectivos metodos
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        plataformas = (TextView) findViewById(R.id.Plataformas);
        pelicul = (TextView) findViewById(R.id.Peliculas);
        imagen_plataforma = (ImageView) findViewById(R.id.imageView2);
        imagen_peliculas = (ImageView) findViewById(R.id.imageView3);

        id_usuario = getIntent().getIntExtra(identificador, -1);

        plataformas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent plataformas = new Intent(Inicio.this, PlataformasActivity.class);
                plataformas.putExtra(identificador, id_usuario);
                startActivity(plataformas);

            }
        });

        imagen_plataforma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent plataformas2 = new Intent(Inicio.this, PlataformasActivity.class);
                plataformas2.putExtra(identificador, id_usuario);
                startActivity(plataformas2);

            }
        });

        pelicul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent peli = new Intent(Inicio.this, PeliculasActivity.class);
                peli.putExtra(identificador, id_usuario);
                startActivity(peli);

            }
        });

        imagen_peliculas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent peli = new Intent(Inicio.this, PeliculasActivity.class);
                peli.putExtra(identificador, id_usuario);
                startActivity(peli);

            }
        });

    }
}