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

public class BoxStand extends Group {
	public BoxStand(Appearance screen1App) {

		
		// Monitor Horizontal
		Primitive top = new Box(0.2f, 0.3f, 0.2f, screen1App);
		Transform3D tr = new Transform3D();
		tr.set(new Vector3f(1.3f, 0.3f, 0.0f));
		TransformGroup tg = new TransformGroup(tr);
		tg.addChild(top);
		this.addChild(tg);
		
		// Monitor Horizontal
		Primitive top2 = new Box(0.2f, 0.3f, 0.2f, screen1App);
		Transform3D tr2 = new Transform3D();
		tr2.set(new Vector3f(1.3f, 0.3f, 0.5f));
		TransformGroup tg2 = new TransformGroup(tr2);
		tg2.addChild(top2);
		this.addChild(tg2);
		
		// Monitor Horizontal
		Primitive top3 = new Box(0.2f, 0.3f, 0.2f, screen1App);
		Transform3D tr3 = new Transform3D();
		tr3.set(new Vector3f(1.3f, 0.3f, 1.0f));
		TransformGroup tg3 = new TransformGroup(tr3);
		tg3.addChild(top3);
		this.addChild(tg3);
		
		// Monitor Horizontal
		Primitive top4 = new Box(0.2f, 0.3f, 0.2f, screen1App);
		Transform3D tr4 = new Transform3D();
		tr4.set(new Vector3f(1.3f, 0.3f, 1.5f));
		TransformGroup tg4 = new TransformGroup(tr4);
		tg4.addChild(top4);
		this.addChild(tg4);
		
	}
}
