package shapes;

import java.awt.Color;
import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.sun.j3d.utils.universe.SimpleUniverse;
import javax.media.j3d.*;
import javax.vecmath.*;

// An Egyptian pyramid
// Base divided into two triangles

public class PyramidExample {
	public static void main(String[] args) {
		SimpleUniverse universe = new SimpleUniverse();
		BranchGroup group = new BranchGroup();

		Point3f e = new Point3f(1.0f, 0.0f, 0.0f); // east
		Point3f s = new Point3f(0.0f, 0.0f, 1.0f); // south
		Point3f w = new Point3f(-1.0f, 0.0f, 0.0f); // west
		Point3f n = new Point3f(0.0f, 0.0f, -1.0f); // north
		Point3f t = new Point3f(0.0f, 0.721f, 0.0f); // top

		TriangleArray pyramidGeometry = new TriangleArray(18,
				TriangleArray.COORDINATES);
		pyramidGeometry.setCoordinate(0, e);
		pyramidGeometry.setCoordinate(1, t);
		pyramidGeometry.setCoordinate(2, s);

		pyramidGeometry.setCoordinate(3, s);
		pyramidGeometry.setCoordinate(4, t);
		pyramidGeometry.setCoordinate(5, w);

		pyramidGeometry.setCoordinate(6, w);
		pyramidGeometry.setCoordinate(7, t);
		pyramidGeometry.setCoordinate(8, n);

		pyramidGeometry.setCoordinate(9, n);
		pyramidGeometry.setCoordinate(10, t);
		pyramidGeometry.setCoordinate(11, e);

		pyramidGeometry.setCoordinate(12, e);
		pyramidGeometry.setCoordinate(13, s);
		pyramidGeometry.setCoordinate(14, w);

		pyramidGeometry.setCoordinate(15, w);
		pyramidGeometry.setCoordinate(16, n);
		pyramidGeometry.setCoordinate(17, e);
		GeometryInfo geometryInfo = new GeometryInfo(pyramidGeometry);
		NormalGenerator ng = new NormalGenerator();
		ng.generateNormals(geometryInfo);

		GeometryArray result = geometryInfo.getGeometryArray();
		
		// yellow appearance
		Appearance appearance = new Appearance();
		Color3f color = new Color3f(Color.yellow);
		Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
		Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
		Texture texture = new Texture2D();
		TextureAttributes texAttr = new TextureAttributes();
		texAttr.setTextureMode(TextureAttributes.MODULATE);
		texture.setBoundaryModeS(Texture.WRAP);
		texture.setBoundaryModeT(Texture.WRAP);
		texture.setBoundaryColor(new Color4f(0.0f, 1.0f, 0.0f, 0.0f));
		Material mat = new Material(color, black, color, white, 70f);
		appearance.setTextureAttributes(texAttr);
		appearance.setMaterial(mat);
		appearance.setTexture(texture);
		Shape3D shape = new Shape3D(result, appearance);
		group.addChild(shape);

		// above pyramid
		Vector3f viewTranslation = new Vector3f();
		viewTranslation.z = 3;
		viewTranslation.x = 0f;
		viewTranslation.y = .3f;
		Transform3D viewTransform = new Transform3D();
		viewTransform.setTranslation(viewTranslation);
		Transform3D rotation = new Transform3D();
		rotation.rotX(-Math.PI / 12.0d);
		rotation.mul(viewTransform);
		universe.getViewingPlatform().getViewPlatformTransform().setTransform(
				rotation);
		universe.getViewingPlatform().getViewPlatformTransform().getTransform(
				viewTransform);
		
		// lights
		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
				1000.0);
		Color3f light1Color = new Color3f(.7f, .7f, .7f);
		Vector3f light1Direction = new Vector3f(4.0f, -7.0f, -12.0f);
		DirectionalLight light1 = new DirectionalLight(light1Color, light1Direction);
		light1.setInfluencingBounds(bounds);
		group.addChild(light1);
		Color3f ambientColor = new Color3f(.4f, .4f, .4f);
		AmbientLight ambientLightNode = new AmbientLight(ambientColor);
		ambientLightNode.setInfluencingBounds(bounds);
		group.addChild(ambientLightNode);
		
		universe.addBranchGraph(group);
	}
}