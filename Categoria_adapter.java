package es.umh.dadm.mistickets49428110y;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;


import es.umh.dadm.mistickets49428110y.Elementos.Categorias;

public class Categoria_adapter extends BaseAdapter {


    private Context SContext;

    ArrayList<Categorias> arrayCategorias;

    private final String TAG="CategoriaAdapter";


    public Categoria_adapter(Context context, ArrayList<Categorias> array){

        super();
        SContext= context;
        arrayCategorias = array;


    }



    @Override
    public int getCount() {
        return arrayCategorias.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        // inflate the layout for each item of listView
        LayoutInflater infalter = (LayoutInflater) SContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = infalter.inflate(R.layout.activity_categoria_element, null);

        // get the reference of textViews
        TextView textViewTexto = (TextView) view.findViewById(R.id.nombre_categoria_list);




        ImageView imageViewTicket = (ImageView) view.findViewById(R.id.img_categoria_list);
        imageViewTicket.setRotation(90);



        // Set the Sender number and smsBody to respective TextViews

        textViewTexto.setText(arrayCategorias.get(position).getTexto());





        Log.d(TAG,"Nombre img adapter: " +arrayCategorias.get(position).getDireccion());

        try {
            File f=new File(arrayCategorias.get(position).getImagen(), arrayCategorias.get(position).getDireccion());
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            imageViewTicket.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }





        return view;
    }
}

