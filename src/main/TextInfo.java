package main;

import java.awt.Font;

import javax.media.j3d.Appearance;
import javax.media.j3d.Font3D;
import javax.media.j3d.FontExtrusion;
import javax.media.j3d.Group;
import javax.media.j3d.Material;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Text3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

public class TextInfo extends Group{
	public TextInfo() {
		
		Appearance text3dap = new Appearance();
		text3dap.setMaterial(new Material());
		Font3D font = new Font3D(new Font("SansSerif", Font.ITALIC, 1), new FontExtrusion());
		

		//LOD
		Text3D text = new Text3D(font, "LOD");
		Shape3D shape3dtext = new Shape3D(text, text3dap);
		Transform3D t1 = new Transform3D();
		t1.setScale(0.03);
		t1.setTranslation(new Vector3f(0.55f, 0.1f, 0.72f));
		
		Transform3D t2 = new Transform3D();
		t2.rotY(Math.toRadians(270));
		t1.mul(t2);
		TransformGroup tf = new TransformGroup(t1);
		tf.addChild(shape3dtext);
		this.addChild(tf);
		
		//MORPH
		Text3D textBillboard = new Text3D(font, "Morph");
		Shape3D shape3dtext2 = new Shape3D(textBillboard, text3dap);
		Transform3D ttextBillboard = new Transform3D();
		ttextBillboard.setScale(0.03);
		ttextBillboard.setTranslation(new Vector3f(0.55f, 0.1f, 0.45f));
		
		Transform3D text2 = new Transform3D();
		text2.rotY(Math.toRadians(270));
		ttextBillboard.mul(text2);
		TransformGroup tftextBillboard = new TransformGroup(ttextBillboard);
		tftextBillboard.addChild(shape3dtext2);
		this.addChild(tftextBillboard);
	
		//BILLBOARD
		Text3D textMorph = new Text3D(font, "Billboard");
		Shape3D shape3dtext3 = new Shape3D(textMorph, text3dap);
		Transform3D ttextMorph = new Transform3D();
		ttextMorph.setScale(0.03);
		ttextMorph.setTranslation(new Vector3f(0.55f, 0.1f, 0.20f));
		
		Transform3D text3 = new Transform3D();
		text3.rotY(Math.toRadians(270));
		ttextMorph.mul(text3);
		TransformGroup tftextMorph = new TransformGroup(ttextMorph);
		tftextMorph.addChild(shape3dtext3);
		this.addChild(tftextMorph);
		
		//Texture
		Text3D textMorph2 = new Text3D(font, "Texture");
		Shape3D shape3dtext32 = new Shape3D(textMorph2, text3dap);
		Transform3D ttextMorph2 = new Transform3D();
		ttextMorph2.setScale(0.03);
		ttextMorph2.setTranslation(new Vector3f(0.55f, 0.1f, -0.05f));
		
		Transform3D text32 = new Transform3D();
		text32.rotY(Math.toRadians(270));
		ttextMorph2.mul(text32);
		TransformGroup tftextMorph2 = new TransformGroup(ttextMorph2);
		tftextMorph2.addChild(shape3dtext32);
		this.addChild(tftextMorph2);
		
		//Texture
		Text3D textMorph3 = new Text3D(font, "Paintings by _miles28_");
		Shape3D shape3dtext33 = new Shape3D(textMorph3, text3dap);
		Transform3D ttextMorph3 = new Transform3D();
		ttextMorph3.setScale(0.03);
		ttextMorph3.setTranslation(new Vector3f(-0.25f, 0.28f, -0.78f));
		
		Transform3D text33 = new Transform3D();
		//text33.rotY(Math.toRadians(270));
		ttextMorph2.mul(text33);
		TransformGroup tftextMorph3 = new TransformGroup(ttextMorph3);
		tftextMorph3.addChild(shape3dtext33);
		this.addChild(tftextMorph3);
	}


	
}


//textt.rotX(Math.toRadians(90));

