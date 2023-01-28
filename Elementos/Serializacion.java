package es.umh.dadm.mistickets49428110y.Elementos;


import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class Serializacion {

    public static String SerializarCategoria(ArrayList<Categorias> listaCategorias){
        Gson gson = new Gson();
        String objJSONSerialized = gson.toJson(listaCategorias);
        return objJSONSerialized;
    }

    /**
     * Return null if listaCategoria is empty
     * @param listadoCategorias
     * @return listaCategorias
     */
    public static ArrayList<Categorias> DesSerializarCategoria (String listadoCategorias){
        ArrayList<Categorias> listaCategorias;
        Gson gson = new Gson();
        listaCategorias = gson.fromJson(listadoCategorias.split("\\n")[0],
                new TypeToken<List<Categorias>>(){}.getType());

        return listaCategorias;

    }

}
