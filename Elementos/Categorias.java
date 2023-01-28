package es.umh.dadm.mistickets49428110y.Elementos;

import java.io.Serializable;

public class Categorias implements Serializable {

    private int id;
    private String texto;
    private String descripcion;
    private String descripcion_larga;
    private String imagen;
    private String direccion;

    public Categorias() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }




    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


    public String getDescripcionlarga() {
        return descripcion_larga;
    }

    public void setDescripcionlarga(String descripcion_larga) {
        this.descripcion_larga = descripcion_larga;
    }
    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }


}
