package shapes;

import javax.media.j3d.Geometry;
import javax.media.j3d.Group;
import javax.vecmath.Point3f;

import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;

public class QuadroData extends Group{
	public Geometry createGeometry() {
	    int m = 40;
	    int n = 40;
	    Point3f[] pts = new Point3f[m*n];
	    int idx = 0;
	    for (int i = 0; i < m; i++) {
	      for (int j = 0; j < n; j++) {
	        float x = (i - m/2)*0.2f;
	        float z = (j - n/2)*0.2f;
	        float y = 2f * (float)(Math.cos(x*x) * Math.sin(z*z))/
	                 ((float)Math.exp(0.25*(x*x+z*z)))-1.0f;
	        
	        
	        pts[idx++] = new Point3f(x, y, z);
	      }
	    }

	    int[] coords = new int[2*n*(m-1)];
	    idx = 0;
	    for (int i = 1; i < m; i++) {
	      for (int j = 0; j < n; j++) {
	        coords[idx++] = i*n + j;
	        coords[idx++] = (i-1)*n + j;
	      }
	    }

	    int[] stripCounts = new int[m-1];
	    for (int i = 0; i < m-1; i++) stripCounts[i] = 2*n;

	    GeometryInfo gi = new GeometryInfo(GeometryInfo.TRIANGLE_STRIP_ARRAY);
	    gi.setCoordinates(pts);
	    gi.setCoordinateIndices(coords);
	    gi.setStripCounts(stripCounts);

	    NormalGenerator ng = new NormalGenerator();
	    ng.generateNormals(gi);

	    return gi.getGeometryArray();
	  }
}
