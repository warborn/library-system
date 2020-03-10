package custom.buttons;


import classes.Author;
import classes.Book;
import classes.ClassType;
import classes.DBEntity;
import frames.JFrameList;
import frames.JFrameListAuthors;
import frames.JFrameListBooks;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ivan
 */

public class DeleteButton extends CustomButton implements ActionListener {
    
    public DeleteButton(JFrame frame, ClassType type, int id, String content)
    {
        super(frame, type, id, content);
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
//        JOptionPane.showMessageDialog(null, "Has presionado el boton eliminar para: " + getID());
        
        DBEntity entity = null;
        JFrameList frame = null;

        switch(getType())
        {
            case AUTHOR:
                entity = Author.findByID(getID());
                frame = (JFrameListAuthors)getFrame();
                break;
            case BOOK:
                entity = Book.findByID(getID());
                frame = (JFrameListBooks)getFrame();
                break;
            case ADMIN:
                break;
        }

        if(entity.delete())
        {
            JOptionPane.showMessageDialog(null, "Se ha eliminado con exito!");
            frame.loadTableContent();
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Hubo un error al eliminar.");
        }
    }
    
}
