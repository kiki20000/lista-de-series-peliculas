package es.umh.dadm.mispelis74374423j.practica2.peliculas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import es.umh.dadm.mispelis74374423j.ListaAdaptadoraPeliculas;
import es.umh.dadm.mispelis74374423j.R;
import es.umh.dadm.mispelis74374423j.practica2.ListaAdaptadora;
import es.umh.dadm.mispelis74374423j.practica2.UsuariosSQLiteHelper;
import es.umh.dadm.mispelis74374423j.practica2.plataformas.PantallaPlataformaActivity;
import es.umh.dadm.mispelis74374423j.practica2.plataformas.Platafor;
import es.umh.dadm.mispelis74374423j.practica2.plataformas.PlataformasActivity;
import es.umh.dadm.mispelis74374423j.practica2.plataformas.anyadir_plataformaActivity;

public class PeliculasActivity extends AppCompatActivity {

    private int id_usuario;
    private ListView listadopeliculas;
    ListaAdaptadoraPeliculas listaAdaptadoraPeliculas;
    Context context;
    ArrayList<Pelicula> arrayPelis;
    private UsuariosSQLiteHelper usuBD;
    private static final String FICHERO = "Peliculas.txt";
    private static final String TAG = "Peliculas";
    private static final String nameBD = "BDUsuarios";
    private static final int VERSION = UsuariosSQLiteHelper.VERSION;
    private static final String identificador = "identificador_usuario";
    private static final String plataformador = "id_plataforma";
    private static final String peliformador = "id_pelicula";

    /**
     * declaracion de los componentes y linkamos cada componente, tambn hacemos las llamadas pertinentes
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peliculas);

        usuBD = new UsuariosSQLiteHelper(this, nameBD, null, VERSION);

        id_usuario = getIntent().getIntExtra(identificador, -1);

        context = this;

        cargarPlataformas(id_usuario);

    }

    //Metodo para desplegar y ocultar el menu
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.menuanyadirpelis, menu);
        return true;
    }

    //metodo para asignar las funciones correspondientes a las opciones
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();

        if (id == R.id.añadirpelicula){
            Intent anyadir = new Intent(PeliculasActivity.this, Anyadir_peliculasActivity.class);
            anyadir.putExtra(identificador,id_usuario);
            PeliculasActivity.this.startActivity(anyadir);

        }
        else if( id == R.id.exportarpeliculas){

            if(puedoEscribirMemoriaExterna()){
                guardarTexto();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * con este metodo hacemos q se muestre el lv, dentro tenemos click corto y largo
     * @param id_usuario
     */
    private void cargarPlataformas(int id_usuario){

        String no_hay = getString(R.string.no_hay);

        listadopeliculas = (ListView) findViewById(R.id.lv2);

        // listadoplataformas.setOnItemClickListener(this);
        UsuariosSQLiteHelper sqLiteHelper = new UsuariosSQLiteHelper(this, nameBD, null, VERSION);

        arrayPelis = sqLiteHelper.obtenerPeliculas(id_usuario);

        //creamos el adaptador
        listaAdaptadoraPeliculas = new ListaAdaptadoraPeliculas(this, arrayPelis);

        listadopeliculas.setAdapter(listaAdaptadoraPeliculas);

        listadopeliculas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(PeliculasActivity.this, PantallaPeliculaActivity.class);
                TextView id_oculto_plataforma = (TextView)view.findViewById(R.id.id_platforma);
                TextView id_oculto_pelicula = (TextView)view.findViewById(R.id.id_pelicula);
                intent.putExtra(plataformador, id_oculto_plataforma.getText());
                intent.putExtra(peliformador, id_oculto_pelicula.getText());
                intent.putExtra(identificador, id_usuario);
                startActivity(intent);
            }
        });

        listadopeliculas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                String si = getString(R.string.si);
                String Borrado = getString(R.string.Borrado);
                String no = getString(R.string.no);
                String Borrar = getString(R.string.Borrar);
                String eliminar = getString(R.string.eliminar);

                TextView id_oculto_plataforma = (TextView)view.findViewById(R.id.id_platforma);
                TextView id_oculto_pelicula = (TextView)view.findViewById(R.id.id_pelicula);

                AlertDialog.Builder alert = new AlertDialog.Builder(PeliculasActivity.this);
                alert.setMessage(eliminar)
                        .setCancelable(false)
                        .setPositiveButton(si, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Borrar plataforma
                                usuBD.BorrarPeliculas(Integer.parseInt(id_oculto_pelicula.getText().toString()));
                                Toast.makeText(PeliculasActivity.this, Borrado, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(PeliculasActivity.this, PeliculasActivity.class);
                                intent.putExtra(identificador, id_usuario);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog titulo = alert.create();
                titulo.setTitle(Borrar);
                titulo.show();

                return false;
            }
        });


        if (arrayPelis.isEmpty()){
            Toast.makeText(getBaseContext(), no_hay, Toast.LENGTH_SHORT).show();
        }

    }

    private boolean puedoEscribirMemoriaExterna(){
        String state = Environment.getExternalStorageState();
        return (state.equals(Environment.MEDIA_MOUNTED)? true: false);
    }

    /**
     * metodo para exportar los ficheros
     */
    private void guardarTexto(){

        String no_hemos = getString(R.string.no_hemos);
        String guardatexto = getString(R.string.guardatexto);
        String guardarTextos = getString(R.string.guardarTextos);
        String FileNotFound = getString(R.string.FileNotFound);
        String IOException = getString(R.string.IOException);
        String enter = getString(R.string.enter);
        String id_plata = getString(R.string.id_plata);
        String id_usu = getString(R.string.id_usu);
        String id_pelicula = getString(R.string.id_pelicula);
        String Nombre_peli = getString(R.string.Nombre_peli);
        String Duracioncita = getString(R.string.Duracioncita);
        String Generocito = getString(R.string.Generocito);
        String Calificacioncita = getString(R.string.Calificacioncita);



        if (!puedoEscribirMemoriaExterna()){
            Toast.makeText(this, no_hemos , Toast.LENGTH_LONG).show();
            return;
        }
        File raiz = getExternalFilesDir(null);
        File fichero = new File(raiz, FICHERO);
        Log.d(TAG, guardatexto +raiz.getAbsolutePath());

        // Crear el fichero si no existía
        //boolean creado = false;
        if (!fichero.exists()){
            try {
                //creado = fichero.createNewFile();
                fichero.createNewFile();
            } catch (IOException e1){
                Log.e(TAG, guardarTextos);
                return;
            } catch (SecurityException e2){
                Log.e(TAG, guardarTextos);
                return;
            }
        }

        // Abrir el fichero para escritura
        BufferedWriter buf;
        try {
            buf = new BufferedWriter(new FileWriter(fichero.getAbsolutePath()));
        } catch (IOException e) {
            Log.e(TAG, FileNotFound);
            return;
        }

        // Obtener el texto del cuadro
        Pelicula peli1;
        CharSequence datos;

        for(int i=0; i<arrayPelis.size();i++)
        {
            peli1 = arrayPelis.get(i);

            // Escribimos en el fichero
            try{

                datos = String.valueOf(peli1.getId());

                buf.write(id_pelicula);
                buf.write(datos.toString());
                buf.write(enter);


                datos = String.valueOf(peli1.getId_usuario());

                buf.write(id_usu);
                buf.write(datos.toString());
                buf.write(enter);


                datos = String.valueOf(peli1.getId_plataforma());

                buf.write(id_plata);
                buf.write(datos.toString());
                buf.write(enter);

                buf.write(Nombre_peli);
                buf.write(peli1.getNombre_peli());
                buf.write(enter);


                datos = String.valueOf(peli1.getDuracion());

                buf.write(Duracioncita);
                buf.write(datos.toString());
                buf.write(enter);

                buf.write(Generocito);
                buf.write(peli1.getGenero());
                buf.write(enter);


                datos = String.valueOf(peli1.getCalificacion());

                buf.write(Calificacioncita);
                buf.write(datos.toString());
                buf.write(enter);
                buf.write(enter);

                buf.flush();

            } catch(IOException e){
                Log.e(TAG, IOException);
            }

        }

        try {

            buf.close();

        } catch (IOException e) {
            Log.e(TAG, IOException);
        }
    }
}