package es.umh.dadm.mistickets49428110y;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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

import es.umh.dadm.mistickets49428110y.Elementos.Tickets;

public class Ticket_adapter extends BaseAdapter {


    private Context GContext;
    ArrayList<Tickets> arrayTickets;

    private static final String TAG = "TicketAdapter";

    public Ticket_adapter(Context context, ArrayList<Tickets> array){

        super();
        GContext = context;
        arrayTickets = array;


    }







    @Override
    public int getCount() {
        // return the number of records in cursor
        return arrayTickets.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    // getView method is called for each item of ListView
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        // inflate the layout for each item of listView
        LayoutInflater infalter = (LayoutInflater) GContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = infalter.inflate(R.layout.activity_ticket_element, null);


        // get the reference of textViews

        TextView textViewTexto =  view.findViewById(R.id.Nombre_list);
        TextView textViewPrecio =  view.findViewById(R.id.Precio_list);
        TextView textViewProveedor =  view.findViewById(R.id.proveedor_list);
        ImageView imageViewticket =  view.findViewById(R.id.imagen_ticket_list);
        imageViewticket.setRotation(90);


        // Set the Sender number and smsBody to respective TextViews

        textViewTexto.setText(arrayTickets.get(position).getTexto());
        textViewPrecio.setText(String.valueOf(arrayTickets.get(position).getPrecio()));
        textViewProveedor.setText((arrayTickets.get(position).getProveedor()));


        try {
            File f= new File(arrayTickets.get(position).getImagen());
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            imageViewticket.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }



        return view;
    }




}
