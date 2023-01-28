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

import es.umh.dadm.mistickets49428110y.Elementos.Categorias;

public class Categoria_detail extends AppCompatActivity {

    TextView nombre;
    TextView descripcion;
    TextView descripcion_larga;
    ImageView imagen_categoria;

    Button editar_categoria;
    Categorias categoria;
    Button ver_tickets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria_detail);


        nombre = findViewById(R.id.nombre_categoria_detail);
        descripcion = findViewById(R.id.descripcion_categoria_detail);
        descripcion_larga = findViewById(R.id.descripcion_categoria_larga_detail);
        imagen_categoria = findViewById(R.id.img_categoria_detail);
        editar_categoria = findViewById(R.id.editar_categoria_boton);
        ver_tickets = findViewById(R.id.ver_tickets);



        Intent intent = getIntent();

        categoria = (Categorias) intent.getSerializableExtra("detallesCategoria");



        nombre.setText(categoria.getTexto());
        descripcion.setText(categoria.getDescripcion());
        descripcion_larga.setText(categoria.getDescripcionlarga());

        try {
            File f=new File(categoria.getImagen(), categoria.getDireccion());
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            imagen_categoria.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }



        //BOTON PARA PASAR A EDITAR EL CATEGORIA
        editar_categoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent editar_categoria = new Intent(Categoria_detail.this, Categoria_modificar.class);
                editar_categoria.putExtra("modificarCategoria",categoria);
                startActivity(editar_categoria);
                finish();
            }
        });

        //BOTON PARA VER LOS TICKETS ASOCIADOS AL CATEGORIA
        ver_tickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent tickets_asociados = new Intent(Categoria_detail.this, Categoria_Listar_Tickets.class);
                tickets_asociados.putExtra("TicketsAsociados",categoria);
                startActivity(tickets_asociados);
                finish();


            }
        });


    }



    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

        Intent intent = new Intent(Categoria_detail.this, Categoria_activity.class);
        setResult(RESULT_OK, intent);
        startActivity(intent);
        super.onBackPressed();
    }

}
