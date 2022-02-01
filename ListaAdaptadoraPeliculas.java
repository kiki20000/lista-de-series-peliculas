package es.umh.dadm.mispelis74374423j;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import es.umh.dadm.mispelis74374423j.practica2.peliculas.Pelicula;
import es.umh.dadm.mispelis74374423j.practica2.plataformas.Platafor;

public class ListaAdaptadoraPeliculas extends BaseAdapter {
    private Context context;
    ArrayList<Pelicula> arrayPelis;

    public ListaAdaptadoraPeliculas(Context context1, ArrayList<Pelicula> array){
        super();
        context = context1;
        arrayPelis = array;
    }

    @Override
    public int getCount() {

        //Devuelve el numero de plataformas en el cursor
        return arrayPelis.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * metodo que sirve para mostrar lo que queremos en el lv
     * @param position
     * @param view
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view =inflater.inflate(R.layout.listado_peliculas, null);

        TextView titulo = (TextView)view.findViewById(R.id.titulo);
        TextView id_oculto_plata = (TextView)view.findViewById(R.id.id_platforma);
        TextView id_oculto_peli = (TextView)view.findViewById(R.id.id_pelicula);
        TextView genero = (TextView)view.findViewById(R.id.genero);
        ImageView imagen_peli = (ImageView)view.findViewById(R.id.imagen_listado_peliculas);

        titulo.setText(arrayPelis.get(position).getNombre_peli());
        id_oculto_plata.setText(Integer.toString(arrayPelis.get(position).getId_plataforma()));
        id_oculto_peli.setText(Integer.toString(arrayPelis.get(position).getId()));
        genero.setText(arrayPelis.get(position).getGenero());
        imagen_peli.setImageBitmap(arrayPelis.get(position).getImagen_caratula());

        return view;
    }
}
