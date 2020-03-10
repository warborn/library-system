package classes;


import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ivan
 */
public class Helper {
    
    public static boolean validateTextFields(JTextField ... fields)
    {
        for(JTextField field : fields)
        {
            if(field.getText().equals(""))
            {
                return false;
            }
        }
        return true;
    }
    
    public static void clearTextFields(JTextField ... fields)
    {
        for(JTextField field : fields)
        {
            field.setText("");
        }
    }
    
    public static void showEmptyFieldsMessage(JFrame frame)
    {
        JOptionPane.showMessageDialog(frame, "No se pueden dejar campos vacios.", 
                                      "Campos Vacios", JOptionPane.ERROR_MESSAGE);
    }
    
}
