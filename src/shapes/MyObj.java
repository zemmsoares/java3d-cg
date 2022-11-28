package shapes;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Material;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Primitive;

public class MyObj extends BranchGroup {

	public MyObj(float size, Appearance screen2App, Appearance plasticApp) {
		
		// Transformation, 2 Rotationen:

		
		Transform3D tr1 = new Transform3D();
		tr1.set(new Vector3f(0f, 0.5f + 0.5f, -0.75f));
		tr1.setScale(0.5);
		
		// Quadro1 - Pintura	
		Primitive screen = new Box(0.28f, 0.48f, 0.010f,Box.GENERATE_TEXTURE_COORDS | Box.GENERATE_NORMALS | Box.ENABLE_GEOMETRY_PICKING, screen2App);
		tr1.setTranslation(new Vector3f(0.7f, 0f + 0.5f, -0.50f));
		
		Transform3D tr2 = new Transform3D();
		//tr2.rotY(Math.toRadians(270));
		tr1.mul(tr2);
		
		
		TransformGroup tg1 = new TransformGroup(tr1);
		this.addChild(tg1);
		

		tg1 = new TransformGroup(tr1);
		
		
		
		tg1.addChild(screen);
		this.addChild(tg1);
	}
}
