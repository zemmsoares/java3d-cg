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

public class Desk extends Group {
	public Desk(Appearance deskApp) {

		// Table top
		Primitive top = new Box(1.5f, 0.025f, 0.5f, Box.GENERATE_TEXTURE_COORDS | Box.GENERATE_NORMALS | Box.ENABLE_GEOMETRY_PICKING, deskApp);
		Transform3D tr = new Transform3D();
		tr.set(new Vector3f(-1f, 0.025f + 0.5f, 0f));
		TransformGroup tg = new TransformGroup(tr);
		tg.addChild(top);
		this.addChild(tg);
		
		Primitive leg = new Box(0.025f, 0.25f, 0.5f, Box.GENERATE_TEXTURE_COORDS | Box.GENERATE_NORMALS | Box.ENABLE_GEOMETRY_PICKING, deskApp);
		Transform3D tr1 = new Transform3D();
		tr1.set(new Vector3f(0.5f, 0.025f + 0.25f, 0f));
		TransformGroup tg1 = new TransformGroup(tr1);
		tg1.addChild(leg);
		this.addChild(tg1);
		
		leg = new Box(0.025f, 0.25f, 0.5f, Box.GENERATE_TEXTURE_COORDS | Box.GENERATE_NORMALS | Box.ENABLE_GEOMETRY_PICKING, deskApp);
		Transform3D tr11 = new Transform3D();
		tr11.set(new Vector3f(-2.47f, 0.025f + 0.25f, 0f));
		TransformGroup tg11 = new TransformGroup(tr11);
		tg11.addChild(leg);
		this.addChild(tg11);
		
	}
}
