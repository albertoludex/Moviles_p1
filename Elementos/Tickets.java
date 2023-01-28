package es.umh.dadm.mistickets49428110y.Elementos;

import java.io.Serializable;

public class Tickets implements Serializable {


    private int id;
    private String texto;
    private int precio;
    private String categoria;
    private String proveedor;
    private String descripcion;
    private String descripcion_larga;
    private String imagen;
    private int fecha;


    public Tickets() {

    }


    public Tickets(String texto, String descipcion, String descipcion_larga, String imagen) {
        this.texto = texto;
        this.descripcion_larga=descipcion_larga;
        this.descripcion = descipcion;
        this.imagen = imagen;
    }

    public Tickets(String texto, String descipcion, String descipcion_larga) {
        this.texto = texto;
        this.descripcion_larga=descipcion_larga;
        this.descripcion = descipcion;
    }


    public int getFecha() {
        return fecha;
    }

    public void setFecha(int fecha) {
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getDescipcion() {
        return descripcion;
    }

    public void setDescipcion(String descipcion) {
        this.descripcion = descipcion;
    }

    public String getDescipcionlarga() {
        return descripcion_larga;
    }

    public void setDescipcionlarga(String descipcion_larga) {
        this.descripcion_larga = descipcion_larga;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }


}
