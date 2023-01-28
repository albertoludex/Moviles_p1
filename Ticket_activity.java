package es.umh.dadm.mistickets49428110y;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import es.umh.dadm.mistickets49428110y.Elementos.Categorias;
import es.umh.dadm.mistickets49428110y.Elementos.Serializacion;
import es.umh.dadm.mistickets49428110y.Elementos.Tickets;





public class Ticket_activity extends AppCompatActivity {


    final TicketsSQLiteHelper usdbh = new TicketsSQLiteHelper(this, null, 1);


    ListView lvTickets;
    Ticket_adapter ticketAdapter;
    Context context;

    public static final String CATEGORIAS_TXT= "categorias.txt";

    private static final String TAG = "TicketsActivity";





    private ImageButton anadirTicket;

    ArrayList<Tickets> arrayTicket;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets);

        context = this;



        cargarTickets();

        anadirTicket= findViewById(R.id.anadirGastoButton);



        //BOTON PARA AÑADIR TICKET, PRIMERO SE COMPRUEBA QUE HAY CATEGORIA DISPONIBLES, SINO NO SE PUEDE
        //PROCEDER A AÑADIR UN TICKET
        anadirTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(readFichero()==null ||  readFichero().isEmpty()){

                    Toast.makeText(getBaseContext(),R.string.alerta_anadir_ticket, Toast.LENGTH_LONG).show();

                }else {

                    Intent categoria = new Intent(Ticket_activity.this, Ticket_add.class);
                    startActivity(categoria);
                    finish();

                }

            }
        });

        //BOTON PARA VER LOS DETALLES DE UN TICKET
        lvTickets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent intent = new Intent(Ticket_activity.this, Ticket_detail.class);
                setResult(RESULT_OK, intent);

                intent.putExtra("detallesTicket", arrayTicket.get(position));

                startActivity(intent);
                finish();



            }
        });

        //BOTON PARA ELIMINAR UN TICKET CONCRETO
        lvTickets.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> av, View v, final int pos, long id) {


                final int posicion=pos;





                //AQUI SE IMPLEMENTA EL ALERTDIALOG



                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Ticket_activity.this);

                dialogo1.setTitle(R.string.titulo_dialog);
                dialogo1.setMessage(R.string.cuerpo_dialog_ticket);

                dialogo1.setCancelable(false);



                //SI SE PULSA "SI", SE EJECUTA LA FUNCION DE ELIMINAR DE LA BD Y SE RRECARGA LA ACTIVITY
                dialogo1.setPositiveButton(R.string.dialog_si, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogo1, int id) {



                        // TODO: Get the position of the item clicked.
                        //       Delete it from your collection eg.ArrayList.
                        //       Call notifydatasetChanged so that it will refresh
                        //       the views displaying updated list.


                        String id_string= String.valueOf(arrayTicket.get(posicion).getId());




                        usdbh.eliminarTicket(id_string);
                        ticketAdapter.notifyDataSetChanged();
                        finish();
                        startActivity(getIntent());



                    }
                });


                //EN CASO DE SELECCIONAR "NO" EN EL DIALOGO, SE CANCELARA LA OPERACION
                dialogo1.setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog1, int id) {

                        dialog1.cancel();

                    }
                });

                dialogo1.show();


                return true;

            }


        });





    }



    //CARGAR LOS TICKET DE LA BD E INTRODUCIRLOS EN EL LISTVIEW MEDIANTE EL ADAPTER
    private void cargarTickets()
    {
        lvTickets= findViewById(R.id.ListViewTickets);






        /***************************************/




        arrayTicket=usdbh.obtenerTickets();


        // Create the Adapter
        ticketAdapter = new Ticket_adapter(this,arrayTicket);

        // Set The Adapter to ListView

        lvTickets.setAdapter(ticketAdapter);




        //Si no hay registros mostramos un aviso
        if (arrayTicket.isEmpty())
        {
            Toast.makeText(getBaseContext(), R.string.no_registros, Toast.LENGTH_LONG).show();
        }
    }



    private ArrayList<Categorias> readFichero() {
        String cadena = "";
        ArrayList<Categorias> categorias= null;
        try {
            BufferedReader fin = new BufferedReader(
                    new InputStreamReader(openFileInput(CATEGORIAS_TXT)));

            cadena = fin.readLine();
            categorias= Serializacion.DesSerializarCategoria(cadena);

            fin.close();
        } catch (Exception ex) {
            Log.e("Ficheros", getResources().getString(R.string.Error_leer_memoria));
        }
        return categorias;
    }







}
