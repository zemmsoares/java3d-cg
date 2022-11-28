package shapes;

import javax.media.j3d.Appearance;
import javax.media.j3d.Group;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Primitive;

public class Monitors extends Group {
	public Monitors(Appearance screen1App, Appearance screen2App, Appearance screen3App, Appearance plasticApp) {

		/*
		// Monitor Horizontal
		Primitive top = new Box(0.5f, 0.3f, 0.020f, plasticApp);
		Transform3D tr = new Transform3D();
		tr.set(new Vector3f(-1f, 0.025f + 0.9f, -0.3f));
		TransformGroup tg = new TransformGroup(tr);
		tg.addChild(top);
		this.addChild(tg);
		*/
		
		//QUADRO 1
		// Monitor Vertical
		Primitive top = new Box(0.3f, 0.5f, 0.020f, plasticApp);
		Transform3D tr1 = new Transform3D();
		tr1.set(new Vector3f(-1.9f, 0.025f + 1.1f, -0.3f));
		TransformGroup tg1 = new TransformGroup(tr1);
		tg1.addChild(top);
		this.addChild(tg1);
		
		//QUADRO 2
		Primitive quadroDois = new Box(0.3f, 0.5f, 0.020f, plasticApp);
		Transform3D tr2 = new Transform3D();
		tr2.set(new Vector3f(-1.2f, 0.025f + 1.1f, -0.3f));
		TransformGroup tg2 = new TransformGroup(tr2);
		tg2.addChild(quadroDois);
		this.addChild(tg2);
		
		//QUADRO 3
		Primitive quadroTres = new Box(0.3f, 0.5f, 0.020f, plasticApp);
		Transform3D tr3 = new Transform3D();
		tr3.set(new Vector3f(-0.5f, 0.025f + 1.1f, -0.3f));
		TransformGroup tg3 = new TransformGroup(tr3);
		tg3.addChild(quadroTres);
		this.addChild(tg3);
		

		// Quadro1 - Pintura	
		Primitive screen = new Box(0.28f, 0.48f, 0.010f,Box.GENERATE_TEXTURE_COORDS | Box.GENERATE_NORMALS | Box.ENABLE_GEOMETRY_PICKING,screen1App);
		tr1.setTranslation(new Vector3f(-1.9f, 0.025f + 1.1f, -0.287f));
		tg1 = new TransformGroup(tr1);
		tg1.addChild(screen);
		this.addChild(tg1);
		
		// Quadro2 - Pintura	
		Primitive screen2 = new Box(0.28f, 0.48f, 0.010f,Box.GENERATE_TEXTURE_COORDS | Box.GENERATE_NORMALS | Box.ENABLE_GEOMETRY_PICKING,screen2App);
		tr2.setTranslation(new Vector3f(-1.2f, 0.025f + 1.1f, -0.287f));
		tg2 = new TransformGroup(tr2);
		tg2.addChild(screen2);
		this.addChild(tg2);
		
		// Quadro2 - Pintura	
		Primitive screen3 = new Box(0.28f, 0.48f, 0.010f,Box.GENERATE_TEXTURE_COORDS | Box.GENERATE_NORMALS | Box.ENABLE_GEOMETRY_PICKING,screen3App);
		tr3.setTranslation(new Vector3f(-0.5f, 0.025f + 1.1f, -0.287f));
		tg3 = new TransformGroup(tr3);
		tg3.addChild(screen3);
		this.addChild(tg3);
		
		
		/*
		// stand main monitor
		Primitive Stand = new Cylinder(0.04f, 0.5f,GreyplasticApp);
		tr.setTranslation(new Vector3f(-1f, -0.1f + 0.9f, -0.325f));
		tg = new TransformGroup(tr);
		tg.addChild(Stand);
		this.addChild(tg);
		
		Stand = new Box(0.2f, 0.02f, 0.05f, Box.GENERATE_TEXTURE_COORDS | Box.GENERATE_NORMALS | Box.ENABLE_GEOMETRY_PICKING, GreyplasticApp);
		tr.setTranslation(new Vector3f(-1f, 0.55f, -0.325f));
		tg = new TransformGroup(tr);
		tg.addChild(Stand);
		this.addChild(tg);
		
	
		// stand vertical monitor
		Stand = new Cylinder(0.04f, 0.5f);
		tr.setTranslation(new Vector3f(-1.9f, -0.1f + 0.9f, -0.325f));
		tg = new TransformGroup(tr);
		tg.addChild(Stand);
		this.addChild(tg);
		
		Stand = new Box(0.2f, 0.02f, 0.05f, Box.GENERATE_TEXTURE_COORDS | Box.GENERATE_NORMALS | Box.ENABLE_GEOMETRY_PICKING, GreyplasticApp);
		tr.setTranslation(new Vector3f(-1.9f, 0.55f, -0.325f));
		tg = new TransformGroup(tr);
		tg.addChild(Stand);
		this.addChild(tg);
		*/
		
		
	}
}
