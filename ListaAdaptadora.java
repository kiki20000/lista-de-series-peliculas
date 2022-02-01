package es.umh.dadm.mispelis74374423j.practica2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import es.umh.dadm.mispelis74374423j.R;
import es.umh.dadm.mispelis74374423j.practica2.plataformas.Platafor;

public class ListaAdaptadora extends BaseAdapter {

    private Context context;
    ArrayList<Platafor> arrayPlataformas;

    public ListaAdaptadora(Context context1, ArrayList<Platafor> array){
        super();
        context = context1;
        arrayPlataformas = array;
    }

    @Override
    public int getCount() {

        //Devuelve el numero de plataformas en el cursor
        return arrayPlataformas.size();
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
     * aqui diseñamos lo que queremos q se vea en el listview
     * @param position
     * @param view
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view =inflater.inflate(R.layout.listado_elementos, null);

        TextView nombre_plataforma = (TextView)view.findViewById(R.id.tv_plataforma);
        TextView id_oculto = (TextView)view.findViewById(R.id.tv_id_oculto);
        ImageView imagen_plataforma = (ImageView)view.findViewById(R.id.imagen_listado_elemento);

        nombre_plataforma.setText(arrayPlataformas.get(position).getNombre());
        id_oculto.setText(Integer.toString(arrayPlataformas.get(position).getId()));
        imagen_plataforma.setImageBitmap(arrayPlataformas.get(position).getImagen());

        return view;
    }
}
