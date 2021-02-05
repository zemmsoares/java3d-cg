package shapes;


import javax.media.j3d.Appearance;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;


public class Floor extends Shape3D {
	
	public Floor(int divisions, float min, float max, Color3f color1, Color3f color2, boolean solid) {
		// Range in X
		int m = divisions;
		float a = min;
		float b = max;
		float divX = (b - a) / m;

		// Range in Z
		int n = divisions;
		float c = min;
		float d = max;
		float divZ = (d - c) / m;

		// Total of vertices
		int totalPts = m * n * 4;

		Point3f[] pts = new Point3f[totalPts];  // Array of vertices
		Color3f[] col = new Color3f[totalPts];  // Array of colors of vertices
		
		
		int idx = 0; // Auxiliary index
		boolean invert = true;  // Flag to invert the color 
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				
				// Vertex 0
				float x = a + i * divX;
				float z = c + j * divZ;
				float y = 0f;
				pts[idx] = new Point3f(x, y, z);
				col[idx] = (invert ? color1 : color2);
				idx++;

				// Vertex 1
				x = a + i * divX;
				z = c + (j + 1) * divZ;
				pts[idx] = new Point3f(x, y, z);
				col[idx] = (invert ? color1 : color2);
				idx++;

				// Vertex 2
				x = a + (i + 1) * divX;
				z = c + (j + 1) * divZ;
				pts[idx] = new Point3f(x, y, z);
				col[idx] = (invert ? color1 : color2);
				idx++;

				// Vertex 3
				x = a + (i + 1) * divX;
				z = c + j * divZ;
				pts[idx] = new Point3f(x, y, z);
				col[idx] = (invert ? color1 : color2);
				idx++;

				invert = !invert;
			}
			invert = !invert;
		}

		// Define the geometry type
		QuadArray gi = new QuadArray(totalPts, QuadArray.COORDINATES
				| QuadArray.COLOR_3);
		
		gi.setCoordinates(0, pts); // Add coordinates of the vertices
		gi.setColors(0, col);  // Add colors of the vertices
		
		// Configure the appearance
		// The geometry is colored using the colors of the vertices.
		// But we configure the appearance with Polygon Attributes to 
		// render both sides of the geometry.
		Appearance ap = new Appearance();
		PolygonAttributes pa = new PolygonAttributes();
		if(solid)
		  pa.setPolygonMode(PolygonAttributes.POLYGON_FILL);
		else
			pa.setPolygonMode(PolygonAttributes.POLYGON_LINE);
		
		pa.setCullFace(PolygonAttributes.CULL_NONE);
		ap.setPolygonAttributes(pa);
		
		
		this.setGeometry(gi);   // Set the geometry
		this.setAppearance(ap); // Set the appearance 
	}

}
