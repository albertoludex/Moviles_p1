package es.umh.dadm.mistickets49428110y;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import es.umh.dadm.mistickets49428110y.Elementos.Serializacion;
import es.umh.dadm.mistickets49428110y.Elementos.Categorias;

public class Categoria_add extends AppCompatActivity {


    public static final String CATEGORIAS_TXT= "categorias.txt";


    private final int MY_PERMISSIONS_REQUESTS = 0;

    private static final int CAPTURAR_IMAGEN = 1;
    private static final int GALERIA_IMAGEN = 2;

    Categorias categoria = new Categorias();

    private static final String TAG = "anadirCategoria";

    EditText nombre;
    EditText descripcion;
    EditText descripcion_larga;

    ImageView imagen_categoria;

    Button anadir_categoria;

    ArrayList<Categorias> arrayCategorias= new ArrayList<Categorias>();

    int id_actual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria_add);

        requestPermissions();


        nombre = findViewById(R.id.nombre_categoria_add);
        descripcion = findViewById(R.id.descripcion_categoria_add);
        descripcion_larga = findViewById(R.id.descripcion_categoria_larga_add);
        imagen_categoria = findViewById(R.id.img_categoria_add);
        anadir_categoria = findViewById(R.id.boton_categoria_add);



        if(readFichero()==null || readFichero().size()==0){


            id_actual=0;

        }else if(readFichero().size()==1){

            id_actual=1;

        }else{


            arrayCategorias= readFichero();

            int ultima_posicion=arrayCategorias.size()-1;

            id_actual=arrayCategorias.get(ultima_posicion).getId()+1;


        }

        //CAMBIAR IMAGEN CON ONCLICK
        imagen_categoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                alertImagen();




            }
        });


        //BOTON PARA AÑADIR EL CATEGORIA, PRIMERO SE COMPRUEBA QUE ESTÁN LOS CAMPOS RELLENADOS
        anadir_categoria.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


                if (nombre.getText().toString().isEmpty()) {
                    Toast toastexto = Toast.makeText(getBaseContext(), R.string.texto_requerido, Toast.LENGTH_LONG);
                    toastexto.show();
                }
                else if (descripcion.getText().toString().isEmpty()) {
                    Toast toastdes = Toast.makeText(getBaseContext(), R.string.descripcion_requerido, Toast.LENGTH_LONG);
                    toastdes.show();
                }
                else if (descripcion_larga.getText().toString().isEmpty()) {
                    Toast toastdes = Toast.makeText(getBaseContext(), R.string.descripcion_requerido, Toast.LENGTH_LONG);
                    toastdes.show();
                }else{

                    categoria.setId(id_actual);
                    categoria.setTexto(nombre.getText().toString());
                    categoria.setDescripcion(descripcion.getText().toString());
                    categoria.setDescripcionlarga(descripcion_larga.getText().toString());


                    if(categoria.getImagen()==null){


                        Bitmap bitmap = ((BitmapDrawable) imagen_categoria.getDrawable()).getBitmap();

                        saveToInternalStorage(bitmap);

                    }



                    if(readFichero()==null ){



                        arrayCategorias.add(categoria);
                        saveFichero(arrayCategorias);





                    }else{


                        arrayCategorias=readFichero();
                        arrayCategorias.add(categoria);

                        saveFichero(arrayCategorias);


                    }



                    Intent intent = new Intent(Categoria_add.this, Categoria_activity.class);
                    setResult(RESULT_OK, intent);

                    startActivity(intent);
                    finish();



                }





            }
        });







    }



    //REQUISITO DE PERMISOS
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUESTS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    finish();
                }
                break;
            }
            default:
                break;
        }
    }





    //REQUISITO DE PERMISOS (CÁMARA Y ACCESOS A MEMORIA)
    private void requestPermissions()
    {
        List<String> requiredPermissions = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requiredPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requiredPermissions.add(Manifest.permission.CAMERA);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requiredPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (!requiredPermissions.isEmpty())
            ActivityCompat.requestPermissions(this,
                    requiredPermissions.toArray(new String[]{}),
                    MY_PERMISSIONS_REQUESTS);
    }


    //CAPTURAR BITMAP CAMARA O GALERIA
    @Override
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
                    Toast.makeText(this, R.string.error, Toast.LENGTH_LONG).show();
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

        String n_imagen = id_actual+"S.jpg";

        categoria.setDireccion(n_imagen);


        File mypath=new File(directory,n_imagen);

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

        categoria.setImagen(directory.getAbsolutePath());
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

