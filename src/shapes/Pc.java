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
	public Pc(Appearance plasticApp,Appearance whiteApp) {
		
				Primitive pc = new Box(0.2f, 0.3f, 0.4f, whiteApp);
				Transform3D tr = new Transform3D();
				tr.set(new Vector3f(0f, 0.025f + 0.9f, 0f));
				TransformGroup tg = new TransformGroup(tr);
				tg.addChild(pc);
				this.addChild(tg);
				
				pc = new Box(0.005f, 0.28f, 0.38f, plasticApp);
				tr.setTranslation(new Vector3f(-0.2f, 0.025f + 0.9f, 0f));
				tg = new TransformGroup(tr);
				tg.addChild(pc);
				this.addChild(tg);
				
				//legs
				
				pc = new Box(0.05f, 0.03f, 0.05f, whiteApp);
				tr.setTranslation(new Vector3f(-0.15f, 0.60f, 0.35f));
				tg = new TransformGroup(tr);
				tg.addChild(pc);
				this.addChild(tg);
				
				pc = new Box(0.05f, 0.03f, 0.05f, whiteApp);
				tr.setTranslation(new Vector3f(0.15f, 0.60f, 0.35f));
				tg = new TransformGroup(tr);
				tg.addChild(pc);
				this.addChild(tg);
				
				pc = new Box(0.05f, 0.03f, 0.05f, whiteApp);
				tr.setTranslation(new Vector3f(0.15f, 0.60f, -0.35f));
				tg = new TransformGroup(tr);
				tg.addChild(pc);
				this.addChild(tg);

				pc = new Box(0.05f, 0.03f, 0.05f, whiteApp);
				tr.setTranslation(new Vector3f(-0.15f, 0.60f, -0.35f));
				tg = new TransformGroup(tr);
				tg.addChild(pc);
				this.addChild(tg);
	}
}
