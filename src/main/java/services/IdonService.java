/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo4.services;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author asus
 */
public interface IdonService<T> {
    
       public void ajouterdon(T t) throws SQLException;
    public void modifierdon(T t) throws SQLException;
    public void supprimerdon(T t) throws SQLException;
    public List<T> recupererdon() throws SQLException;
    
}
