package shapes;

import javax.media.j3d.Appearance;
import javax.media.j3d.IndexedQuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.vecmath.Point3f;

import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;

class Lampshade extends Shape3D {
	public Lampshade(int n, Appearance app) {
		// n = number of slices
		int totalVertices = 4 * n; // Total number of unique vertices.
		int totalIndices = 4 * 4 * n; // Total number of indices.

		// The geometry is constructed with quadrilaterals.
		// The IndexedQuadArray allows to not have to repeat vertex coordinates,
		IndexedQuadArray qa = new IndexedQuadArray(totalVertices, IndexedQuadArray.COORDINATES, totalIndices);

		// Transformation to generate the unique vertices
		Transform3D tr = new Transform3D();
		tr.rotY(2 * Math.PI / n); // Rotation around Y axis.

		// The 4 initial vertices that define a slice.
		Point3f[] pts = { new Point3f(1, 0, 0), new Point3f(1.1f, 0, 0), new Point3f(1.1f - 0.5f, 0.7f, 0f),
				new Point3f(1f - 0.5f, 0.7f, 0f) };

		// Generate all the unique vertices from the 4 initial vertices
		int index = 0;
		for (int j = 0; j < n; j++) { // for each slice
			for (int i = 0; i < 4; i++) { // for each vertex of the slice
				// Add the 'i' vertex to the geometry.
				qa.setCoordinate(index++, pts[i]);

				// After the vertex 'i' is added, the vertex is rotated around Y to
				// generate the correspondent next one.
				tr.transform(pts[i]);
			}
		}

		// Initial set of indexes to define the faces.
		int a = 0;
		int b = 1;
		int c = 2;
		int d = 3;

		// Construct 4 faces using the actual 4 vertices and the next 4.
		index = 0;
		int e, f, g, h; // the next 4 vertices.
		for (int j = 0; j < n; j++) {

			// Define the next 4 vertices from the actual ones.
			// The division by totalVertices makes the indices in the range [0,
			// totalVertices[.
			e = (a + 4) % totalVertices;
			f = (b + 4) % totalVertices;
			g = (c + 4) % totalVertices;
			h = (d + 4) % totalVertices;

			// Back face
			qa.setCoordinateIndex(index++, a);
			qa.setCoordinateIndex(index++, d);
			qa.setCoordinateIndex(index++, h);
			qa.setCoordinateIndex(index++, e);

			// Front face
			qa.setCoordinateIndex(index++, b);
			qa.setCoordinateIndex(index++, f);
			qa.setCoordinateIndex(index++, g);
			qa.setCoordinateIndex(index++, c);

			// Top face
			qa.setCoordinateIndex(index++, c);
			qa.setCoordinateIndex(index++, g);
			qa.setCoordinateIndex(index++, h);
			qa.setCoordinateIndex(index++, d);

			// Bottom face
			qa.setCoordinateIndex(index++, a);
			qa.setCoordinateIndex(index++, e);
			qa.setCoordinateIndex(index++, f);
			qa.setCoordinateIndex(index++, b);

			// Update the indices, the last 4 become the new 4.
			a = e;
			b = f;
			c = g;
			d = h;
		}

		// Transform the IndexQuadArray geometry into GeometryInfo to be able to
		// generate
		// the normals.
		GeometryInfo gi = new GeometryInfo(qa);
		NormalGenerator ng = new NormalGenerator();
		ng.generateNormals(gi);

		this.setGeometry(gi.getGeometryArray());
		this.setAppearance(app);
	}
}