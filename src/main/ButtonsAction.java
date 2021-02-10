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
                Transform3D teste = new Transform3D();
                teste.set(new Vector3f(0.0f,0.5f,3.0f));
                main.getSimpleU().getViewingPlatform().getViewPlatformTransform().setTransform(teste);
               // System.out.print(main.getSimpleU());
        }else {
            System.out.println("Test");
        }
        
    }
}