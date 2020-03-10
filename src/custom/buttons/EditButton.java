package custom.buttons;


import classes.Author;
import classes.Book;
import classes.ClassType;
import classes.DBEntity;
import frames.JFrameListAuthors;
import frames.JFrameListBooks;
import frames.JFrameUpdateAuthor;
import frames.JFrameUpdateBook;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ivan
 */
public class EditButton extends CustomButton implements ActionListener {
    
    public EditButton(JFrame frame, ClassType type, int id, String content)
    {
        super(frame, type, id, content);
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
//        JOptionPane.showMessageDialog(null, "Has presionado el boton editar para: " + getID());
        
        DBEntity entity = null;
//        JFrameList frame = null;

        switch(getType())
        {
            case AUTHOR:
                entity = Author.findByID(getID());
//                frame = (JFrameListAuthors)getFrame();
                JFrameUpdateAuthor fa = new JFrameUpdateAuthor((JFrameListAuthors)getFrame(), getID());
                fa.setVisible(true);
                break;
            case BOOK:
                entity = Book.findByID(getID());
                JFrameUpdateBook fb = new JFrameUpdateBook((JFrameListBooks)getFrame(), getID());
                fb.setVisible(true);
                break;
            case ADMIN:
                break;
        }

    }
    
}
