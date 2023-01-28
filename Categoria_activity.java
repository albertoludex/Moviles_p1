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
import java.io.OutputStreamWriter;
import java.util.ArrayList;


import es.umh.dadm.mistickets49428110y.Elementos.Serializacion;
import es.umh.dadm.mistickets49428110y.Elementos.Categorias;


public class Categoria_activity extends AppCompatActivity {


    public static final String CATEGORIAS_TXT= "categorias.txt";

    ImageButton anadirCategoria;

    ArrayList<Categorias> arrayCategorias;
    Context context;
    
    ListView lvcategoria;

    Categoria_adapter categoriaAdapter;



    private final static String TAG="ServiciosActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);

        context = this;

        cargarCategorias();

        anadirCategoria= findViewById(R.id.add_categoria);

        anadirCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent categorias = new Intent(Categoria_activity.this, Categoria_add.class);
                startActivity(categorias);
                finish();



            }
        });

        //ACCEDER AL DETALLE DE UN CATEGORIA
        lvcategoria.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                Intent intent = new Intent(Categoria_activity.this, Categoria_detail.class);
                setResult(RESULT_OK, intent);


                intent.putExtra("detallesCategoria",arrayCategorias.get(position));


                startActivity(intent);
                finish();
            }
        });


        //ELIMINAR UNA CATEGORIA CONCRETA
        lvcategoria.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id) {


                final int posicion=pos;
                //AQUI SE IMPLEMENTA EL ALERTDIALOG
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Categoria_activity.this);

                dialogo1.setTitle(R.string.titulo_dialog);
                dialogo1.setMessage(R.string.cuerpo_dialog_categoria);

                dialogo1.setCancelable(false);



                //SI SE PULSA "SI", SE EJECUTAN LOS CHECKS, LAS ASIGNACIONES Y EL PASO A LA SIGUIENTE ACTIVITY
                dialogo1.setPositiveButton(R.string.dialog_si, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogo1, int id) {




                        ArrayList<Categorias> arrayAux = new ArrayList<Categorias>();

                        arrayAux=readFichero();



                        arrayAux.remove(posicion);

                        saveFichero(arrayAux);

                        categoriaAdapter.notifyDataSetChanged();
                        finish();
                        startActivity(getIntent());



                        //-------------------------------------------



                    }
                });


                //EN CASO DE SELECCIONAR "NO" EN EL DIALOGO, SE CANCELARA LA OPERACION
                //SE VOLVERA AL MENU A REVISAR LOS DATOS
                dialogo1.setNegativeButton("No", new DialogInterface.OnClickListener() {
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



    //CARGAR LOS CATEGORIAS DE LA BD E INTRODUCIRLOS EN EL LISTVIEW MEDIANTE EL ADAPTER
    private void cargarCategorias(){

        lvcategoria=  findViewById(R.id.ListViewCategorias);

        arrayCategorias = new ArrayList<Categorias>();




        Log.d(TAG,"VALOR: "+readFichero());


        arrayCategorias=readFichero();




        //Si no hay registros mostramos un aviso
        if (arrayCategorias==null || arrayCategorias.isEmpty())
        {
            Toast.makeText(getBaseContext(), R.string.no_registros, Toast.LENGTH_LONG).show();

        }else{



            readFichero();


            categoriaAdapter = new Categoria_adapter(this,readFichero());



            lvcategoria.setAdapter(categoriaAdapter);


        }



    }



    private ArrayList<Categorias> readFichero() {
        String cadena = "";
        ArrayList<Categorias> categorias= null;
        try {
            BufferedReader fin = new BufferedReader(
                    new InputStreamReader(openFileInput(CATEGORIAS_TXT)));

            cadena = fin.readLine();
            categorias=Serializacion.DesSerializarCategoria(cadena);

            fin.close();
        } catch (Exception ex) {
            Log.e("Ficheros", getResources().getString(R.string.Error_leer_memoria));
        }
        return categorias;
    }


    private void saveFichero (ArrayList<Categorias> arrayS){

        String cadena=Serializacion.SerializarCategoria(arrayS);
        try
        {
            OutputStreamWriter fout=
                    new OutputStreamWriter(
                            openFileOutput(CATEGORIAS_TXT,
                                    Context.MODE_PRIVATE));
            fout.write(cadena);
            fout.close();
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", getResources().getString(R.string.Error_escribir_memoria));
        }
    }






}
