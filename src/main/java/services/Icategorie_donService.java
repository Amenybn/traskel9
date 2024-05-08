/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temcategorie_done file, choose Tools | Temcategorie_dones
 * and open the temcategorie_done in the editor.
 */
package com.example.demo4.services;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author asus
 */
public interface Icategorie_donService<T> {
    
       public void ajoutercategorie_don(T t) throws SQLException;
    public void modifiercategorie_don(T t) throws SQLException;
    public void supprimercategorie_don(T t) throws SQLException;
    public List<T> recuperercategorie_don() throws SQLException;
    
}
