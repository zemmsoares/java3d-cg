package main;

import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.swing.*;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonsAction implements ActionListener {
	
    private Main main;

    ButtonsAction(Main main){
        this.main = main;
    }
	
	
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton bt = (JButton) e.getSource();
     
        if(bt == main.test){
        		System.out.print("test");
        }
    }
}
