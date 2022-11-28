package shapes;

import javax.media.j3d.Appearance;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Group;
import javax.media.j3d.IndexedTriangleArray;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TriangleArray;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Primitive;

public class Minibox extends Group{
	  public Minibox(Appearance BlackPlasticApp) {
			// Monitor Horizontal
			Primitive top = new Box(0.2f, 0.3f, 0.2f, BlackPlasticApp);
			Transform3D tr = new Transform3D();
			tr.set(new Vector3f(0.0f, 0.3f, 0.0f));
			TransformGroup tg = new TransformGroup(tr);
			tg.addChild(top);
			this.addChild(tg);
		  }
}


