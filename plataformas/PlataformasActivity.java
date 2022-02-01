package es.umh.dadm.mispelis74374423j.practica2.plataformas;

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
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import es.umh.dadm.mispelis74374423j.R;
import es.umh.dadm.mispelis74374423j.practica1.MainActivity;
import es.umh.dadm.mispelis74374423j.practica1.RecuperarContrasenaActivity;
import es.umh.dadm.mispelis74374423j.practica2.ListaAdaptadora;
import es.umh.dadm.mispelis74374423j.practica2.UsuariosSQLiteHelper;
import es.umh.dadm.mispelis74374423j.practica2.peliculas.Pelicula;

public class PlataformasActivity extends AppCompatActivity {

    private int id_usuario;
    private ListView listadoplataformas;
    ListaAdaptadora listaAdaptadora;
    Context context;
    ArrayList<Platafor> arrayPlataformas;
    private UsuariosSQLiteHelper usuBD;
    private static final String FICHERORMA = "Plataformas.txt";
    private static final String TAGORMA = "Plataformas";
    private static final String nameBD = "BDUsuarios";
    private static final int VERSION = UsuariosSQLiteHelper.VERSION;
    private static final String identificador = "identificador_usuario";
    private static final String plataformador = "id_plataforma";

    /**
     * metodo para unir los componentes y hacer las llamadas a otros metodos pertinentes
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plataformas);

        usuBD = new UsuariosSQLiteHelper(this, nameBD, null, VERSION);

        id_usuario = getIntent().getIntExtra(identificador, -1);

        context = this;
        cargarPlataformas(id_usuario);

    }

    //Metodo para desplegar y ocultar el menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.plataformas, menu);
        return true;
    }

    //metodo para asignar las funciones correspondientes a las opciones
    public boolean onOptionsItemSelected(MenuItem item) {
        String Exportando = getString(R.string.Exportando);

        int id = item.getItemId();

        if (id == R.id.añadir) {
            Intent anyadir = new Intent(PlataformasActivity.this, anyadir_plataformaActivity.class);
            anyadir.putExtra(identificador, id_usuario);
            PlataformasActivity.this.startActivity(anyadir);
        } else if (id == R.id.exportar) {

            if (puedoEscribirMemoriaExterna()) {
                guardarTexto();
            }

            Toast.makeText(this, Exportando, Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * metodo que llama a lv y nos lo muestra
     * @param id_usuario
     */
    private void cargarPlataformas(int id_usuario) {

        String nope = getString(R.string.nope);

        listadoplataformas = (ListView) findViewById(R.id.lv1);

        // listadoplataformas.setOnItemClickListener(this);
        UsuariosSQLiteHelper sqLiteHelper = new UsuariosSQLiteHelper(this, nameBD, null, VERSION);

        arrayPlataformas = sqLiteHelper.obtenerplataformas(id_usuario);

        //creamos el adaptador
        listaAdaptadora = new ListaAdaptadora(this, arrayPlataformas);

        listadoplataformas.setAdapter(listaAdaptadora);


        listadoplataformas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(PlataformasActivity.this, PantallaPlataformaActivity.class);
                TextView id_oculto = (TextView) view.findViewById(R.id.tv_id_oculto);
                intent.putExtra(plataformador, id_oculto.getText());
                intent.putExtra(identificador, id_usuario);
                startActivity(intent);
            }
        });


        listadoplataformas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                String Deseo = getString(R.string.Deseo);
                String si = getString(R.string.si);
                String Borrado = getString(R.string.Borrado);
                String no = getString(R.string.no);
                String Borrar = getString(R.string.Borrar);

                TextView id_oculto = (TextView) view.findViewById(R.id.tv_id_oculto);

                AlertDialog.Builder alert = new AlertDialog.Builder(PlataformasActivity.this);
                alert.setMessage(Deseo)
                        .setCancelable(false)
                        .setPositiveButton(si, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Borrar plataforma
                                usuBD.BorrarPlataforma(Integer.parseInt(id_oculto.getText().toString()));
                                Toast.makeText(PlataformasActivity.this, Borrado, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(PlataformasActivity.this, PlataformasActivity.class);
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

        if (arrayPlataformas.isEmpty()) {
            Toast.makeText(getBaseContext(), nope, Toast.LENGTH_SHORT).show();
        }

    }

    private boolean puedoEscribirMemoriaExterna() {
        String state = Environment.getExternalStorageState();
        return (state.equals(Environment.MEDIA_MOUNTED) ? true : false);
    }

    /**
     * metodo para exportar los ficheros
     */
    private void guardarTexto() {

        String no_hemos = getString(R.string.no_hemos);
        String guardatexto = getString(R.string.guardatexto);
        String guardarTextos = getString(R.string.guardarTextos);
        String FileNotFound = getString(R.string.FileNotFound);
        String id_plata = getString(R.string.id_plata);
        String enter = getString(R.string.enter);
        String id_usu = getString(R.string.id_usu);
        String nompla = getString(R.string.nompla);
        String url = getString(R.string.url);
        String Acceso = getString(R.string.Acceso);
        String passplata = getString(R.string.passplata);
        String IOException = getString(R.string.IOException);

        if (!puedoEscribirMemoriaExterna()) {
            Toast.makeText(this, no_hemos, Toast.LENGTH_LONG).show();
            return;
        }
        File raiz = getExternalFilesDir(null);
        File fichero = new File(raiz, FICHERORMA);
        Log.d(TAGORMA, guardatexto + raiz.getAbsolutePath());

        // Crear el fichero si no existía
        //boolean creado = false;
        if (!fichero.exists()) {
            try {
                //creado = fichero.createNewFile();
                fichero.createNewFile();
            } catch (IOException e1) {
                Log.e(TAGORMA, guardarTextos);
                return;
            } catch (SecurityException e2) {
                Log.e(TAGORMA, guardarTextos);
                return;
            }
        }

        // Abrir el fichero para escritura
        BufferedWriter buf;
        try {
            buf = new BufferedWriter(new FileWriter(fichero.getAbsolutePath()));
        } catch (IOException e) {
            Log.e(TAGORMA, FileNotFound);
            return;
        }

        // Obtener el texto del cuadro
        Platafor plata1;
        CharSequence datos;

        for (int i = 0; i < arrayPlataformas.size(); i++) {
            plata1 = arrayPlataformas.get(i);

            // Escribimos en el fichero
            try {

                datos = String.valueOf(plata1.getId());

                buf.write(id_plata);
                buf.write(datos.toString());
                buf.write(enter);


                datos = String.valueOf(plata1.getId_usu());

                buf.write(id_usu);
                buf.write(datos.toString());
                buf.write(enter);


                buf.write(nompla);
                buf.write(plata1.getNombre());
                buf.write(enter);

                buf.write(url);
                buf.write(plata1.getUrl());
                buf.write(enter);


                buf.write(Acceso);
                buf.write(plata1.getAcceso());
                buf.write(enter);

                buf.write(passplata);
                buf.write(plata1.getPassword_plataforma());
                buf.write(enter);

                buf.write(enter);
                buf.write(enter);

                buf.flush();

            } catch (IOException e) {
                Log.e(TAGORMA, IOException);
            }

        }

        try {

            buf.close();

        } catch (IOException e) {
            Log.e(TAGORMA, IOException);
        }
    }
}