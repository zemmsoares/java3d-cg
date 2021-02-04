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

public class Table extends Group {
	public Table(Appearance app) {

		// Table top
		Primitive top = new Box(0.5f, 0.05f, 0.5f, Cylinder.GENERATE_NORMALS | Cylinder.ENABLE_GEOMETRY_PICKING, app); // For the box, 0.5 means a width of 1
		Transform3D tr = new Transform3D();
		tr.set(new Vector3f(0f, 0.05f + 0.5f, 0f));
		TransformGroup tg = new TransformGroup(tr);
		tg.addChild(top);
		this.addChild(tg);
		
		// Legs
		Primitive leg = new Cylinder(0.05f, 0.5f, app);  // For the cylinder, 0.5 means a height of 0.5 
		tr.setTranslation(new Vector3d(0.4, 0.25, 0.4));
		tg = new TransformGroup(tr);
		tg.addChild(leg);
		this.addChild(tg);

		// The other 3 legs can easily by positioned considering symmetries from the first leg 
		leg = new Cylinder(0.05f, 0.5f, app);  
		tr.setTranslation(new Vector3d(-0.4, 0.25, 0.4));
		tg = new TransformGroup(tr);
		tg.addChild(leg);
		this.addChild(tg);
		
		leg = new Cylinder(0.05f, 0.5f, app);  
		tr.setTranslation(new Vector3d(0.4, 0.25, -0.4));
		tg = new TransformGroup(tr);
		tg.addChild(leg);
		this.addChild(tg);

		leg = new Cylinder(0.05f, 0.5f, app);  
		tr.setTranslation(new Vector3d(-0.4, 0.25, -0.4));
		tg = new TransformGroup(tr);
		tg.addChild(leg);
		this.addChild(tg);
	}
}
