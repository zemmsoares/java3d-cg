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

public class Table2 extends Group {
	public Table2(Appearance topApp, Appearance legApp) {

		// Table top
		Primitive top = new Box(0.5f, 0.05f, 0.5f, Box.GENERATE_TEXTURE_COORDS | Box.GENERATE_NORMALS | Box.ENABLE_GEOMETRY_PICKING, topApp); // 0.5 significa 1 de largura
		Transform3D tr = new Transform3D();
		tr.set(new Vector3f(0f, 0.05f + 0.5f, 0f));
		TransformGroup tg = new TransformGroup(tr);
		tg.addChild(top);
		this.addChild(tg);
		
		// Legs
		Primitive leg = new Cylinder(0.05f, 0.5f, legApp);  // 0.5 significa 0.5 de altura
		tr.setTranslation(new Vector3d(0.4, 0.25, 0.4));
		tg = new TransformGroup(tr);
		tg.addChild(leg);
		this.addChild(tg);
		
		leg = new Cylinder(0.05f, 0.5f, legApp);  // 0.5 significa 0.5 de altura
		tr.setTranslation(new Vector3d(-0.4, 0.25, 0.4));
		tg = new TransformGroup(tr);
		tg.addChild(leg);
		this.addChild(tg);
		
		leg = new Cylinder(0.05f, 0.5f, legApp);  // 0.5 significa 0.5 de altura
		tr.setTranslation(new Vector3d(0.4, 0.25, -0.4));
		tg = new TransformGroup(tr);
		tg.addChild(leg);
		this.addChild(tg);

		leg = new Cylinder(0.05f, 0.5f, legApp);  // 0.5 significa 0.5 de altura
		tr.setTranslation(new Vector3d(-0.4, 0.25, -0.4));
		tg = new TransformGroup(tr);
		tg.addChild(leg);
		this.addChild(tg);
	}
}
