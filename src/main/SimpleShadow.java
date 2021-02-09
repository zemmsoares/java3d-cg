package main;

import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TriangleArray;
import javax.swing.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimpleShadow extends Shape3D {
	
		SimpleShadow(GeometryArray geom, Vector3f direction, Color3f col,
				float height) {
			int vCount = geom.getVertexCount();
			TriangleArray poly = new TriangleArray(vCount,
					GeometryArray.COORDINATES | GeometryArray.COLOR_3);
			int v;
			Point3f vertex = new Point3f();
			Point3f shadow = new Point3f();
			for (v = 0; v < vCount; v++) {
				geom.getCoordinate(v, vertex);
				System.out.println(vertex.y - height);
			shadow.set(vertex.x - (vertex.y - height), height + 0.0001f,
						vertex.z - (vertex.y - height));
				poly.setCoordinate(v, shadow);
				poly.setColor(v, col);
			}
			this.setGeometry(poly);
		}
}