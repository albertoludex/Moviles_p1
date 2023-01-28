package es.umh.dadm.mistickets49428110y;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import es.umh.dadm.mistickets49428110y.Elementos.Tickets;
import es.umh.dadm.mistickets49428110y.Elementos.Serializacion;
import es.umh.dadm.mistickets49428110y.Elementos.Categorias;

import static java.lang.Integer.parseInt;

public class Ticket_modificar extends AppCompatActivity {

    private final int MY_PERMISSIONS_REQUESTS = 0;
    private static final int CAPTURAR_IMAGEN = 1;
    private static final int GALERIA_IMAGEN = 2;
    Context context;

    public static final String CATEGORIAS_TXT= "categorias.txt";



    TicketsSQLiteHelper modificar = new TicketsSQLiteHelper(this,null,1);



    Tickets tickets= new Tickets();

    Tickets ticket_final = new Tickets();


    EditText nombre;
    EditText precio;
    EditText proveedor;
    EditText descripcion_larga;
    EditText descripcion;
    TextView fecha;
    Spinner eleccion_categoria;

    ImageView imagen;

    Button boton_modificar;

    Calendar calendario = Calendar.getInstance();
    private static final String TAG = "ModificarTicket";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_modf);

        context = this;

        Intent intent = getIntent();
        tickets = (Tickets) intent.getSerializableExtra("modificarTicket");

        nombre = findViewById(R.id.nombre_modificar);
        precio = findViewById(R.id.precio_modificar);

        proveedor = findViewById(R.id.proveedor_modificar);
        descripcion = findViewById(R.id.descripcion_modificar);
        descripcion_larga = findViewById(R.id.descripcion_larga_modf);
        fecha = findViewById(R.id.fecha_modificar);
        imagen = findViewById(R.id.img_ticket_modf);
        boton_modificar = findViewById(R.id.modificar_boton);
        eleccion_categoria = findViewById(R.id.servicio_gasto_modificar);

        nombre.setText(tickets.getTexto());
        precio.setText(String.valueOf(tickets.getPrecio()));

        proveedor.setText(tickets.getProveedor());
        descripcion.setText(tickets.getDescipcion());
        descripcion_larga.setText(tickets.getDescipcionlarga());
        fecha.setText(convertir_fecha(tickets.getFecha()));


        //-----------------------------------------


        ArrayList<Categorias> arrayServicios;

        arrayServicios=readFichero();



        String[] categorias = new String[arrayServicios.size()+1];



        int i;

        categorias[0]="";


        if (arrayServicios==null){


            Toast.makeText(this, R.string.no_categoria, Toast.LENGTH_LONG).show();

        }else{

            //Toast.makeText(this, "ENTRA AQUI", Toast.LENGTH_LONG).show();
            for(i=1;i<arrayServicios.size()+1;i++){

                categorias[i]= arrayServicios.get(i-1).getTexto().toString();

            }



        }




        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, categorias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eleccion_categoria.setAdapter(adapter);

        List<String> stringList = new ArrayList<String>(Arrays.asList(categorias));

        eleccion_categoria.setSelection(stringList.indexOf(tickets.getCategoria()));








        try {
            File f=new File(tickets.getImagen());
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            imagen.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }







        //CONSTRUIR CALENDARIO
        fecha.setOnClickListener(new View.OnClickListener() {
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

                new DatePickerDialog(Ticket_modificar.this, fecha ,
                        calendario.get(Calendar.YEAR),
                        calendario.get(Calendar.MONTH),
                        calendario.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });

        //CAMBIAR IMAGEN CON ONCLICK
        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertImagen();

            }
        });






        //BOTON MODIFICAR, ASIGNA LOS VALORES INTRODUCIDOS EN UN OBJETO TICKET, ACTUALIZA LA BASE DE DATOS
        //Y VUELVE A LOS DETALLES DEL TICKET
        boton_modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ticket_final.setId(tickets.getId());
                ticket_final.setTexto(nombre.getText().toString());
                ticket_final.setPrecio(parseInt(precio.getText().toString()));
                ticket_final.setProveedor(proveedor.getText().toString());
                ticket_final.setDescipcion(descripcion.getText().toString());
                ticket_final.setDescipcionlarga(descripcion_larga.getText().toString());
                Bitmap bitmap = ((BitmapDrawable) imagen.getDrawable()).getBitmap();
                saveToInternalStorage(bitmap);



                Log.d(TAG,"IDENTIFICADOR: "+tickets.getTexto());
                Toast toastdes = Toast.makeText(getBaseContext(), "HECHO:"+tickets.getTexto(), Toast.LENGTH_LONG);
                toastdes.show();

                String[] parts = fecha.getText().toString().split("/");

                String fecha_guardar = parts[2] + parts[1] + parts[0] ;

                ticket_final.setFecha(parseInt(fecha_guardar));
                ticket_final.setCategoria(eleccion_categoria.getSelectedItem().toString());


                modificar.obtenerTickets();


                modificar.updateTickets(ticket_final);

                Intent intent = new Intent(Ticket_modificar.this, Ticket_detail.class);
                //setResult(RESULT_OK, intent);


                intent.putExtra("detallesTicket",ticket_final);
                startActivity(intent);
                finish();

            }
        });





    }


    //CAPTURAR BITMAP CAMARA O GALERIA
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURAR_IMAGEN) {
            if (resultCode == RESULT_OK) {
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                imagen.setImageBitmap(bp);
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
                    imagen.setImageBitmap(selectedImage);
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




        File mypath=new File(tickets.getImagen());

        //File mypath=new File(directory,n_imagen);

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

        ticket_final.setImagen(mypath.getAbsolutePath());

    }

    //FUNCION PARA CONVERTIR LA FECHA
    private String convertir_fecha(int fecha){


        String fecha_string = String.valueOf(fecha);
        String año= fecha_string.substring(0,4);
        String mes= fecha_string.substring(4,6);
        String dia= fecha_string.substring(6,8);

        String fecha_correcta = dia + "/" + mes + "/" + año;

        return fecha_correcta;
    }



    //Actualizar el calendario
    private void actualizarInput() {
        String formatoDeFecha = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, new Locale("es","ES"));

        fecha.setText(sdf.format(calendario.getTime()));
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




    //SI PILSAS EL BOTON DE ATRAS TE ENVIARÁ A LOS DETALLES DEL TICKET
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

        Intent intent = new Intent(Ticket_modificar.this, Ticket_detail.class);
        intent.putExtra("detallesTicket", tickets);
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


}
