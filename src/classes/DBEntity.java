/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package classes;

/**
 *
 * @author Ivan
 */
public abstract class DBEntity {
    
    public abstract boolean insert();
    public abstract boolean update();
    public abstract boolean save();
    public abstract boolean delete();
}
