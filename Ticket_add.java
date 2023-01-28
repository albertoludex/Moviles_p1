package es.umh.dadm.mistickets49428110y;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.Manifest;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import es.umh.dadm.mistickets49428110y.Elementos.Tickets;
import es.umh.dadm.mistickets49428110y.Elementos.Serializacion;
import es.umh.dadm.mistickets49428110y.Elementos.Categorias;

import static java.lang.Integer.parseInt;


public class Ticket_add extends AppCompatActivity {


    private final int MY_PERMISSIONS_REQUESTS = 0;

    private static final int CAPTURAR_IMAGEN = 1;
    private static final int GALERIA_IMAGEN = 2;

    Tickets ticket= new Tickets();

    public static final String CATEGORIAS_TXT= "categorias.txt";

    EditText texto;
    EditText descripcion;
    EditText descripcion_larga;

    ImageView imagen_ticket;

    EditText precio_ticket;
    EditText proveedor_ticket;
    TextView fecha_ticket;

    Spinner elegir_categorias;

    Calendar calendario = Calendar.getInstance();


    TicketsSQLiteHelper ultimo_ticket = new TicketsSQLiteHelper(this, null, 1);


    private static final String TAG = "anadirGasto";


    Context context;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_add);

        requestPermissions();

        imagen_ticket=  findViewById(R.id.imagen_ticket_anadir);

        texto =  findViewById(R.id.Nombre_anadir);
        descripcion =  findViewById(R.id.descripcion_anadir);
        descripcion_larga = findViewById(R.id.descripcion_larga_anadir);
        precio_ticket =  findViewById(R.id.Precio_anadir);
        proveedor_ticket =  findViewById(R.id.Proveedor_anadir);

        elegir_categorias = findViewById(R.id.categoria_gasto_anadir);

        fecha_ticket =  findViewById(R.id.fecha_anadir);

        Button anadir = findViewById(R.id.button);


        ArrayList<Categorias> arrayCategorias;

        arrayCategorias= readFichero();


        int i;


        //COMPROBAR QUE HAY CATGORIAS DISPONIBLES Y AÑADIR LOS CATEGORIAS AL SPINNER
        if (arrayCategorias==null){

            Toast.makeText(this, R.string.no_categoria, Toast.LENGTH_LONG).show();

        }else{

            String[] categorias = new String[arrayCategorias.size()+1];


            categorias[0]="";


            for(i=1;i<arrayCategorias.size()+1;i++){

                categorias[i]= arrayCategorias.get(i-1).getTexto().toString();

            }



            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, categorias);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            elegir_categorias.setAdapter(adapter);



        }








        //CAMBIAR IMAGEN CON ONCLICK
        imagen_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                alertImagen();

            }
        });





        //BOTON PARA AÑADIR EL GASTO, PRIMERO SE COMPRUEBA QUE ESTÁN LOS CAMPOS RELLENADOS
        anadir.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {



                if (texto.getText().toString().isEmpty()) {
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
                }
                else if (precio_ticket.getText().toString().isEmpty()){
                    Toast toasprecio = Toast.makeText(getBaseContext(), R.string.precio_requerido, Toast.LENGTH_LONG);
                    toasprecio.show();
                }else if (proveedor_ticket.getText().toString().isEmpty()) {
                    Toast toasprov = Toast.makeText(getBaseContext(), R.string.proveedor_requerido, Toast.LENGTH_LONG);
                    toasprov.show();
                }else if(elegir_categorias.getSelectedItemPosition()==0){

                    Toast.makeText(getBaseContext(),R.string.categoria_requerido, Toast.LENGTH_LONG).show();

                }
                else{



                    //CONVERTIR LA FECHA INTRODUCIDA A ENTERO, QUITANDO LAS BARRAS "/"
                    String[] parts = fecha_ticket.getText().toString().split("/");

                    String fecha_guardar = parts[2] + parts[1] + parts[0] ;



                    ticket.setTexto(texto.getText().toString());
                    ticket.setDescipcion(descripcion.getText().toString());
                    ticket.setDescipcionlarga(descripcion_larga.getText().toString());
                    ticket.setFecha(parseInt(fecha_guardar));


                    if(ticket.getImagen()==null){


                        Bitmap bitmap = ((BitmapDrawable) imagen_ticket.getDrawable()).getBitmap();

                        saveToInternalStorage(bitmap);




                    }

                    ticket.setPrecio(parseInt(precio_ticket.getText().toString()));
                    ticket.setProveedor(proveedor_ticket.getText().toString());
                    ticket.setCategoria(elegir_categorias.getSelectedItem().toString());
                    ultimo_ticket.agregarTicket(ticket);
                    Intent intent = new Intent(Ticket_add.this, Ticket_activity.class);
                    setResult(RESULT_OK, intent);


                    startActivity(intent);
                    finish();


                }
            }
        });



        //CONSTRUIR CALENDARIO
        fecha_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Para añadir la fecha al calendario
                DatePickerDialog.OnDateSetListener fecha = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        calendario.set(Calendar.YEAR, year);
                        calendario.set(Calendar.MONTH, monthOfYear);
                        calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        actualizarInput();
                    }

                };

                new DatePickerDialog(Ticket_add.this, fecha ,
                        calendario.get(Calendar.YEAR),
                        calendario.get(Calendar.MONTH),
                        calendario.get(Calendar.DAY_OF_MONTH))
                        .show();
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
                    // FIXME: Handle this case the user denied to grant the permissions
                    finish();
                }
                break;
            }
            default:
                // TODO: Take care of this case later
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
                imagen_ticket.setImageBitmap(bp);
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
                    imagen_ticket.setImageBitmap(selectedImage);
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
    //EL OBJETO DE TICKET QUE SE VA A ENVIAR
    private void saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir


        ultimo_ticket.obtenerTickets();

        String n_imagen = ultimo_ticket.ultimo_id()+".jpg";
        Log.d(TAG, "IMAGEN: "+n_imagen);
        Log.d(TAG, "IMAGEN: "+directory.getAbsolutePath());


        File mypath=new File(directory,n_imagen);

        Log.d(TAG,"IMAGEN RUTA: "+mypath.getAbsolutePath());

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

        ticket.setImagen(mypath.getAbsolutePath());
        // gasto.setImagen(directory.getAbsolutePath());

    }


    //Actualizar el calendario
    private void actualizarInput() {
        String formatoDeFecha = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, new Locale("es","ES"));

        fecha_ticket.setText(sdf.format(calendario.getTime()));
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






}
