package ut2.pd7;
import java.util.Collection;
import java.util.TreeSet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ernesto
 */
public interface IArbolTrie {

    TreeSet<TAbonado> buscarTelefonos(String pais, String area);

    void insertar(TAbonado unAbonado);
    
}
