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

public class Pc extends Group {
	public Pc(Appearance pc2App, Appearance plasticApp) {

		// Monitor Horizontal
		Primitive top = new Box(0.5f, 0.3f, 0.020f, plasticApp);
		Transform3D tr = new Transform3D();
		tr.set(new Vector3f(-1f, 0.025f + 0.9f, -0.3f));
		TransformGroup tg = new TransformGroup(tr);
		tg.addChild(top);
		this.addChild(tg);
		
		// Monitor Vertical
		top = new Box(0.3f, 0.5f, 0.020f, plasticApp);
		Transform3D tr1 = new Transform3D();
		tr1.set(new Vector3f(-1.9f, 0.025f + 1.1f, -0.3f));
		TransformGroup tg1 = new TransformGroup(tr1);
		tg1.addChild(top);
		this.addChild(tg1);
		
		// Screen
		Primitive screen = new Box(0.48f, 0.28f, 0.010f,Box.GENERATE_TEXTURE_COORDS | Box.GENERATE_NORMALS | Box.ENABLE_GEOMETRY_PICKING,pc2App);
		tr.setTranslation(new Vector3f(-1f, 0.025f + 0.9f, -0.28f));
		tg = new TransformGroup(tr);
		tg.addChild(screen);
		this.addChild(tg);
		
		// Screen Vertical		
		screen = new Box(0.28f, 0.48f, 0.010f,Box.GENERATE_TEXTURE_COORDS | Box.GENERATE_NORMALS | Box.ENABLE_GEOMETRY_PICKING,pc2App);
		tr.setTranslation(new Vector3f(-1.9f, 0.025f + 1.1f, -0.28f));
		tg = new TransformGroup(tr);
		tg.addChild(screen);
		this.addChild(tg);
		
		
	}
}
