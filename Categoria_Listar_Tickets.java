package es.umh.dadm.mistickets49428110y;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import es.umh.dadm.mistickets49428110y.Elementos.Tickets;
import es.umh.dadm.mistickets49428110y.Elementos.Categorias;

public class Categoria_Listar_Tickets extends AppCompatActivity {



    ListView lvTickets;
    Ticket_adapter ticketAdapter;
    Context context;

    private static final String TAG = "TicketsActivity";



    ArrayList<Tickets> arrayTickets;

    Categorias categoria;

    TextView nombre;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria_listar_tickets);


        context = this;

        nombre = findViewById(R.id.nombre_categoria_tickets);


        cargarTickets();




    }

    //CARGAR LOS TICKETS, SE ACCEDE A LA BASE DE DATOS Y SE OBTIENEN LOS TICKETS ASOCIADOS A UN
    //CATEGORIA CONCRETO
    private void cargarTickets()
    {
        lvTickets = findViewById(R.id.ListViewTickets_categorias);

        //Abrimos la base de datos  en modo escritura
        final TicketsSQLiteHelper usdbh = new TicketsSQLiteHelper(this, null, 1);


        Intent gastoCampo= getIntent();


        Bundle b = gastoCampo.getExtras();


        if(b!=null){

            categoria = (Categorias)gastoCampo.getSerializableExtra("TicketsAsociados");

            nombre.setText(categoria.getTexto());

        }

        /***************************************/

        arrayTickets=usdbh.TicketsDeCategorias(categoria.getTexto());


        // Create the Adapter
        ticketAdapter = new Ticket_adapter(this,arrayTickets);

        // Set The Adapter to ListView
        lvTickets.setAdapter(ticketAdapter);


        //Si no hay registros mostramos un aviso
        if (arrayTickets.isEmpty())
        {
            Toast.makeText(getBaseContext(), R.string.no_registros, Toast.LENGTH_LONG).show();
        }
    }




}





