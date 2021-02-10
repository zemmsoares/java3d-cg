package shapes;

import javax.vecmath.*;
import java.awt.*;
import java.awt.event.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;

public class tLamp extends Shape3D{
   public tLamp() {
	  
		Point3f e = new Point3f(1.0f, 0.0f, 0.0f); // east
		Point3f s = new Point3f(0.0f, 0.0f, 1.0f); // south
		Point3f w = new Point3f(-1.0f, 0.0f, 0.0f); // west
		Point3f n = new Point3f(0.0f, 0.0f, -1.0f); // north
		Point3f t = new Point3f(0.0f, 0.721f, 0.0f); // top

		TriangleArray tLampGeometry = new TriangleArray(18,
				TriangleArray.COORDINATES);
		tLampGeometry.setCoordinate(0, e);
		tLampGeometry.setCoordinate(1, t);
		tLampGeometry.setCoordinate(2, s);

		tLampGeometry.setCoordinate(3, s);
		tLampGeometry.setCoordinate(4, t);
		tLampGeometry.setCoordinate(5, w);

		tLampGeometry.setCoordinate(6, w);
		tLampGeometry.setCoordinate(7, t);
		tLampGeometry.setCoordinate(8, n);

		tLampGeometry.setCoordinate(9, n);
		tLampGeometry.setCoordinate(10, t);
		tLampGeometry.setCoordinate(11, e);

		tLampGeometry.setCoordinate(12, e);
		tLampGeometry.setCoordinate(13, s);
		tLampGeometry.setCoordinate(14, w);

		tLampGeometry.setCoordinate(15, w);
		tLampGeometry.setCoordinate(16, n);
		tLampGeometry.setCoordinate(17, e);
		
		GeometryInfo geometryInfo = new GeometryInfo(tLampGeometry);
		NormalGenerator ng = new NormalGenerator();
		ng.generateNormals(geometryInfo);

		this.setGeometry(geometryInfo.getGeometryArray());
	}
	      
  }

