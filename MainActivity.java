package es.umh.dadm.mistickets49428110y;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {


    private ImageButton Tickets;
    private ImageButton Categorias;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Remove title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        //getSupportActionBar().hide(); //hide the title bar

        //Remove notification bar
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //set content view AFTER ABOVE sequence (to avoid crash)
        this.setContentView(R.layout.activity_main);

        Tickets =  findViewById(R.id.tickets);
        Categorias =  findViewById(R.id.categorias);


        //BOTON PARA ACCEDER  CATEGORIA
        Categorias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent categorias = new Intent(MainActivity.this, Categoria_activity.class);
                startActivity(categorias);



            }
        });

        //BOTON PARA ACCEDER  TICKETS
        Tickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent categorias = new Intent(MainActivity.this, Ticket_activity.class);
                startActivity(categorias);



            }
        });












    }










}


