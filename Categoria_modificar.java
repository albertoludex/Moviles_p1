package es.umh.dadm.mistickets49428110y;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import es.umh.dadm.mistickets49428110y.Elementos.Serializacion;
import es.umh.dadm.mistickets49428110y.Elementos.Categorias;

public class Categoria_modificar extends AppCompatActivity {


    private final int MY_PERMISSIONS_REQUESTS = 0;
    private static final int CAPTURAR_IMAGEN = 1;
    private static final int GALERIA_IMAGEN = 2;
    Context context;
    public static final String CATEGORIAS_TXT= "categorias.txt";

    EditText nombre;
    EditText descripcion;
    EditText descripcion_larga;
    ImageView imagen_categoria;

    Button modificar_categoria;

    Categorias categoria= new Categorias();
    Categorias categoria_final = new Categorias();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria_modf);

        context = this;

        Intent intent= getIntent();
        categoria = (Categorias) intent.getSerializableExtra("modificarCategoria");


        nombre = findViewById(R.id.nombre_categoria_modf);
        descripcion = findViewById(R.id.descripcion_categoria_modf);
        descripcion_larga=findViewById(R.id.descripcion_categoria_larga_modf);
        imagen_categoria = findViewById(R.id.imagen_categoria_modf);

        modificar_categoria = findViewById(R.id.modificar_categoria_boton);

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


        //CAMBIAR IMAGEN CON ONCLICK
        imagen_categoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertImagen();

            }
        });




        //BOTON PARA EFECTUAR LA MODIFICACION DEL CATEGORIA, PRIMERO SE COMPRUEBA QUE ESTÁN LOS CAMPOS RELLENADOS
        modificar_categoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                categoria_final.setId(categoria.getId());
                categoria_final.setTexto(nombre.getText().toString());
                categoria_final.setDescripcion(descripcion.getText().toString());
                categoria_final.setDescripcionlarga(descripcion_larga.getText().toString());
                categoria_final.setDireccion(categoria.getDireccion());

                Bitmap bitmap = ((BitmapDrawable) imagen_categoria.getDrawable()).getBitmap();
                saveToInternalStorage(bitmap);

                //----------------------------------

                ArrayList<Categorias> arrayAux = new ArrayList<Categorias>();

                arrayAux=readFichero();


                int i;

                int posicion=0;

                for(i=0; i<arrayAux.size();i++){

                    if(arrayAux.get(i).getId()==categoria_final.getId()){

                        posicion=i;


                        break;

                    }
                }

                arrayAux.set(posicion,categoria_final);

                saveFichero(arrayAux);



                //---------------------------------


                Intent intent = new Intent(Categoria_modificar.this, Categoria_detail.class);
                intent.putExtra("detallesCategoria",categoria_final);
                startActivity(intent);
                finish();





            }
        });


    }


    //CAPTURAR BITMAP CAMARA O GALERIA
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURAR_IMAGEN) {
            if (resultCode == RESULT_OK) {
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                imagen_categoria.setImageBitmap(bp);
                saveToInternalStorage(bp);

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, R.string.cancelado, Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == GALERIA_IMAGEN) {

            if (resultCode == RESULT_OK) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    imagen_categoria.setImageBitmap(selectedImage);
                    saveToInternalStorage(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, R.string.cancelado, Toast.LENGTH_LONG).show();
                }

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, R.string.no_imagen, Toast.LENGTH_LONG).show();
            }


        }


    }


    //GUARDAR BITMAP RECIBIDO DE LA CAMARA O LA GALERIA. TAMBIEN GUARDA LA DIRECCION EN
    //EL OBJETO DE CATEGORIA QUE SE VA A ENVIAR
    private void saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir

        File mypath=new File(directory,categoria.getDireccion());

        categoria_final.setDireccion(categoria.getDireccion());

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        categoria_final.setImagen(directory.getAbsolutePath());

    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

        Intent intent = new Intent(Categoria_modificar.this, Categoria_detail.class);
        intent.putExtra("detallesCategoria", categoria);
        setResult(RESULT_OK, intent);
        startActivity(intent);

        super.onBackPressed();
    }

    //ALERT DIALOG QUE GESTIONA EL INICIO DE ACTIVIDADES CÁMARA Y GALERIA
    public void alertImagen() { //Botón seleccionar imagen
        final CharSequence[] options = {"Tomar foto","Elegir de la galería"}; //Opciones al seleccionar imagen
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Elige una opción"); //Cuadro diálogo
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int seleccion) {
                if(options[seleccion] == "Tomar foto"){

                    Intent cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cInt,CAPTURAR_IMAGEN);

                }else if(options[seleccion] == "Elegir de la galería"){
                    Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent,GALERIA_IMAGEN);
                }
            }
        });
        builder.show(); //Muestra el cuadro diálogo
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

