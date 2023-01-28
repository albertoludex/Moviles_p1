package es.umh.dadm.mistickets49428110y;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import es.umh.dadm.mistickets49428110y.Elementos.Tickets;

public class TicketsSQLiteHelper extends SQLiteOpenHelper {


    Cursor cursor;
    SQLiteDatabase db;
    String[] campos = new String[] {"_id", "texto", "descripcion","descripcion_larga","imagen_ticket", "precio", "proveedor", "fecha_ticket", "categoria"};

    private static final String sqlCreate = "CREATE TABLE Tickets (_id integer primary key autoincrement, texto text not null, descripcion text not null, descripcion_larga text not null, imagen_ticket text, precio int not null, proveedor text not null, fecha_ticket int, categoria text)";


    private static final String nombreBD = "DBTicket";

    private static final String TAG = "TicketSQLiteHelper";


    public TicketsSQLiteHelper(Context contexto, SQLiteDatabase.CursorFactory factory, int version) {

        super(contexto, nombreBD, factory, version);

    }


    public void updateTickets(Tickets ticket){


        db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("texto", ticket.getTexto());
        contentValues.put("descripcion",ticket.getDescipcion());
        contentValues.put("descripcion_larga",ticket.getDescipcionlarga());
        contentValues.put("imagen_ticket",ticket.getImagen());
        contentValues.put("precio", ticket.getPrecio());
        contentValues.put("proveedor",ticket.getProveedor());
        contentValues.put("fecha_ticket",ticket.getFecha());
        contentValues.put("categoria",ticket.getCategoria());

        db.update("Tickets",contentValues, "_id=?", new String[] {Integer.toString(ticket.getId())});

    }


    public void  eliminarTicket(String id){


        db = getWritableDatabase();
        db.delete("Tickets", "_id=?", new String[] {id});

    }



    public String ultimo_id(){




        String selectQuery = "SELECT MAX(_id) AS id FROM Tickets";

        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();

        @SuppressLint("Range") int maxid = cursor.getInt(cursor.getColumnIndex("id"));

        maxid=maxid+1;

        String maximo= String.valueOf(maxid);

        return maximo;

    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(sqlCreate);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {

        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS Tickets");

        //Se crea la nueva versión de la tabla
        db.execSQL(sqlCreate);


    }

    /* Método para obtener todos los tickets de la bd */
    public ArrayList<Tickets> obtenerTickets(){

        //Abrimos la base de datos 'bdtickets' en modo escritura
        db = getWritableDatabase();

        cursor = db.query("Tickets", campos, "", null, null, null, null);

        ArrayList<Tickets> arrayTickets = new ArrayList<Tickets>();
        Tickets ticket;
        if (cursor.moveToFirst()) {
            do {
                ticket = obtenerValores();

                arrayTickets.add(ticket);


            } while(cursor.moveToNext());
        }
        return arrayTickets;

    }


    public ArrayList<Tickets> TicketsDeCategorias(String categoria){

        String[] args = new String[] {categoria};

        db = getWritableDatabase();

        cursor = db.rawQuery("SELECT * FROM Tickets WHERE categoria=? ",args);

        ArrayList<Tickets> arrayTickets = new ArrayList<Tickets>();
        Tickets ticket;
        if (cursor.moveToFirst()) {
            do {
                ticket = obtenerValores();

                arrayTickets.add(ticket);


            } while(cursor.moveToNext());
        }

        return arrayTickets;

    }



    public void agregarTicket(Tickets ticket)
    {
        //Alternativa 1: método sqlExec()
        //String sql = "INSERT INTO Peliculas (_id,genero, ...) VALUES ('" + id + "','" + genero + "') ";
        //db.execSQL(sql);

        //Alternativa 2: método insert()
        ContentValues nuevoRegistro = asignarValores(ticket);

        db = getWritableDatabase();
        db.insert("Tickets", null, nuevoRegistro);

    }



    /*
     * Método para obtener valores del cursor y devolver un objeto ticket
     * */
    private Tickets obtenerValores(){

        Tickets ticket = new Tickets();
        ticket.setId(cursor.getInt(0));
        ticket.setTexto(cursor.getString(1));
        ticket.setDescipcion(cursor.getString(2));
        ticket.setDescipcionlarga(cursor.getString(3));
        ticket.setImagen(cursor.getString(4));
        ticket.setPrecio(cursor.getInt(5));
        ticket.setProveedor(cursor.getString(6));
        ticket.setFecha(cursor.getInt(7));
        ticket.setCategoria(cursor.getString(8));




        return ticket;
    }





    /*
     * Método para asignar valores al registro a actualizar o a inser1tar
     * */
    private ContentValues asignarValores(Tickets ticket){
        ContentValues nuevoRegistro = new ContentValues();
        nuevoRegistro.put("texto",ticket.getTexto());
        nuevoRegistro.put("descripcion",ticket.getDescipcion());
        nuevoRegistro.put("descripcion_larga",ticket.getDescipcionlarga());
        nuevoRegistro.put("imagen_ticket",ticket.getImagen());
        nuevoRegistro.put("precio",ticket.getPrecio());
        nuevoRegistro.put("proveedor",ticket.getProveedor());
        nuevoRegistro.put("fecha_ticket",ticket.getFecha());
        nuevoRegistro.put("categoria",ticket.getCategoria());
        return nuevoRegistro;
    }


}
