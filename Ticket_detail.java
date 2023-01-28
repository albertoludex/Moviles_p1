package es.umh.dadm.mistickets49428110y;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import es.umh.dadm.mistickets49428110y.Elementos.Tickets;

public class Ticket_detail extends AppCompatActivity {


    TextView texto;
    TextView precio;
    TextView categoria;
    TextView proveedor;
    TextView descripcion;
    TextView descripcion_larga;

    TextView fecha;

    Tickets ticket;

    ImageView imagen;
    Button boton;


    private static final String TAG = "DetallesTicket";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);


        Intent intent = getIntent();
        ticket = (Tickets) intent.getSerializableExtra("detallesTicket");





        texto= findViewById(R.id.nombre_detalle);
        precio = findViewById(R.id.precio_detalle);
        categoria = findViewById(R.id.servicio_detalle);
        proveedor = findViewById(R.id.proveedor_detalle);
        descripcion = findViewById(R.id.descripcion_detalle);
        descripcion_larga = findViewById(R.id.descripcion_largo_detalle);

        fecha = findViewById(R.id.fecha_detalle);
        imagen = findViewById(R.id.imagen_gasto_detalle);
        boton = findViewById(R.id.editar_boton);



        texto.setText(ticket.getTexto());
        precio.setText(String.valueOf(ticket.getPrecio()));
        categoria.setText(ticket.getCategoria());
        descripcion.setText(ticket.getDescipcion());
        descripcion_larga.setText(ticket.getDescipcionlarga());
        proveedor.setText(ticket.getProveedor());
        fecha.setText(convertir_fecha(ticket.getFecha()));



        try {

            File f = new File(ticket.getImagen());
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            imagen.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }


        //BOTON PARA PASAR A EDITAR EL TICKET
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                Intent editar_ticket = new Intent(Ticket_detail.this, Ticket_modificar.class);
                editar_ticket.putExtra("modificarTicket", ticket );
                startActivity(editar_ticket);
                finish();



            }
        });




    }




    //TRASNFORMAR NUMERO(BD) A FECHA(PARA EL TextView)
    private String convertir_fecha(int fecha){


        String fecha_string = String.valueOf(fecha);
        String ano= fecha_string.substring(0,4);
        String mes= fecha_string.substring(4,6);
        String dia= fecha_string.substring(6,8);

        String fecha_correcta = dia + "/" + mes + "/" + ano;

        return fecha_correcta;
    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

        Intent intent = new Intent(Ticket_detail.this, Ticket_activity.class);
        setResult(RESULT_OK, intent);
        startActivity(intent);
        super.onBackPressed();
    }





}

