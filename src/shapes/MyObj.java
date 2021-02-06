package shapes;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Material;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Cone;

public class MyObj extends BranchGroup {

	public MyObj(float size, Appearance app) {

		Box obj1 = new Box(size, size, size, Box.GENERATE_NORMALS | Box.GENERATE_TEXTURE_COORDS, app);
		Transform3D tr = new Transform3D();
		tr.setTranslation(new Vector3f(0f, size + 0.001f, 0f));
		TransformGroup tg = new TransformGroup(tr);
		tg.addChild(obj1);
		this.addChild(tg);

	}
}
