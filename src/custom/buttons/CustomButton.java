package custom.buttons;

import classes.ClassType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
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
public class CustomButton extends JButton implements ActionListener{
    
    private int id;
    private String content;
    private JFrame frame;
    private ClassType type;
    
    public CustomButton(JFrame frame, ClassType type, int id, String content)
    {
        super(content);
        this.frame = frame;
        this.type = type;
        this.id = id;
        this.content = content;
    }
    
    public CustomButton()
    {
    }
    
    public int getID()
    {
        return id;
    }
    
    public void setID(int id)
    {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public ClassType getType() {
        return type;
    }

    public void setType(ClassType type) {
        this.type = type;
    }
    
    
    
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        JOptionPane.showMessageDialog(null, "Has presionado el boton editar para: " + getID());
    }
    
}
