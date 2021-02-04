package ex9_1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;

import javax.media.j3d.Alpha;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Material;
import javax.media.j3d.PhysicalBody;
import javax.media.j3d.PhysicalEnvironment;
import javax.media.j3d.PointLight;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Shape3D;
import javax.media.j3d.SpotLight;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.media.j3d.ViewPlatform;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.picking.PickCanvas;
import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.picking.PickTool;
import com.sun.j3d.utils.universe.SimpleUniverse;

import appearance.MyMaterial;
import appearance.TextureAppearance;
import shapes.Axes;
import shapes.Floor;
import shapes.FloorLamp;
import shapes.FloorLamp2;
import shapes.Table;
import shapes.Table2;

public class Ex9_1 extends Frame implements MouseListener {

	BoundingSphere bounds = new BoundingSphere(); // Bounds of the scene
	PointLight pLight = null; // Point light of the scene
	PickCanvas pc = null; // PickCanvas to perform picking

	public static void main(String[] args) {
		Frame frame = new Ex9_1();
		frame.setPreferredSize(new Dimension(800, 800));
		frame.setTitle("Simple 3D Scene");
		frame.pack();
		frame.setVisible(true);
	}

	// The Frame class doesn't have a
	// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	// A possible solution is to override the processWindowEvent method.
	protected void processWindowEvent(WindowEvent e) {
		super.processWindowEvent(e);
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			System.exit(0);
		}
	}

	public Ex9_1() {
		// Create first canvas for the first view
		GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();
		Canvas3D cv1 = new Canvas3D(gc);
		cv1.addMouseListener(this); // Add a mouse listener to the canvas cv1 to get the mouse events

		// Create second canvas for a second view
		//Canvas3D cv2 = new Canvas3D(gc);  // In this exercise we don't use the second canvas

		// Add both canvas to the frame
		setLayout(new GridLayout(1, 1)); // In this exercise we don't use the second canvas
		add(cv1);
		// add(cv2);

		// Create the a simple universe with a standard nominal view
		// This is the first view of the scene
		SimpleUniverse su = new SimpleUniverse(cv1, 2);  // The parameter 2 creates 2 TransformGroup nodes in the view branch  
		su.getViewingPlatform().setNominalViewingTransform();

		// In this exercise we don't use the second canvas
		// Create a second view and add it to the simple universe
		// BranchGroup bgView = createView(cv, new Point3d(2.7, 1, 0), new Point3d(0, 0,
		// 0), new Vector3d(0, 1, 0));
		//BranchGroup bgView = createView(cv2, new Point3d(0, 2.7, 0), new Point3d(0, 0, 0), new Vector3d(1, 0, 1));
		//su.addBranchGraph(bgView);

		// Create the content branch of the scene graph
		//  BranchGroup bg = createSceneGraph();
		// The first TransformGroup of the view is passed and it will be controlled by the rotator
		// to rotate the view around the Y axis
		BranchGroup bg = createSceneGraph(su.getViewingPlatform().
			      getMultiTransformGroup().getTransformGroup(0));   
		bg.compile();
		su.addBranchGraph(bg); // Add the content branch to the simple universe

		// Create a PickCanvas foe the first view (cv1)
		pc = new PickCanvas(cv1, bg);
		pc.setMode(PickTool.GEOMETRY);

		// Add a OrbitBehavior to control the fisrt view with the mouse
		OrbitBehavior orbit = new OrbitBehavior(cv1);
		orbit.setSchedulingBounds(bounds);
		su.getViewingPlatform().setViewPlatformBehavior(orbit);
	}

	private BranchGroup createView(Canvas3D cv, Point3d eye, Point3d center, Vector3d vup) {
		View view = new View();
		view.setProjectionPolicy(View.PERSPECTIVE_PROJECTION);
		ViewPlatform vp = new ViewPlatform();
		view.addCanvas3D(cv);
		view.attachViewPlatform(vp);
		view.setPhysicalBody(new PhysicalBody());
		view.setPhysicalEnvironment(new PhysicalEnvironment());
		Transform3D trans = new Transform3D();
		trans.lookAt(eye, center, vup);
		trans.invert();
		TransformGroup tg = new TransformGroup(trans);
		tg.addChild(vp);
		BranchGroup bgView = new BranchGroup();
		bgView.addChild(tg);
		return bgView;
	}

	private BranchGroup createSceneGraph(TransformGroup tgView) {
		BranchGroup root = new BranchGroup();

		// Axes
		root.addChild(new Axes(new Color3f(Color.RED), 3, 0.5f));

		// Floor
		root.addChild(new Floor(10, -1, 1, new Color3f(Color.DARK_GRAY), new Color3f(Color.WHITE), true));

		TransformGroup spin = new TransformGroup();
		spin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		root.addChild(spin);
		
		// To rotate the view around the Y axis, the rotator controls the tgView and the rotator is a child of the root.
		Alpha alpha = new Alpha(-1, 30000);
		RotationInterpolator rotator = new RotationInterpolator(alpha, tgView);
		rotator.setSchedulingBounds(bounds);
		root.addChild(rotator);
		// spin.addChild(rotator);

		// Table
		//Appearance app = new Appearance();
		//app.setMaterial(new Material());
		//
		
		// We create the TextureAppearance class to make it easy to create an appearance of the type texture  
		TextureAppearance topApp = new TextureAppearance("images/wood2.jpg", false, this); 
		Appearance legApp = new Appearance();
		/*
		Material brass = new Material();
		brass.setAmbientColor(0.329412f, 0.223529f, 0.027451f);
		brass.setDiffuseColor(0.790392f, 0.568627f, 0.113725f);
		brass.setSpecularColor(0.992157f, 0.941176f, 0.807843f);
		brass.setShininess(27.8974f);
		*/
		
		// We create the MyMaterial class to make it easy to create and configure a material object
		MyMaterial brass = new MyMaterial(MyMaterial.BRASS);
		legApp.setMaterial(brass);
		
		Table2 table = new Table2(topApp, legApp);
		
		Transform3D tr = new Transform3D();
		tr.setScale(0.5f);
		tr.setTranslation(new Vector3f(0.5f, 0f, 0f));
		TransformGroup tg = new TransformGroup(tr);
		// The tg that is parent of the table, must have permissions to be part of the
		// picking result and to read and write its geometric transformation
		tg.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		tg.addChild(table);
		// root.addChild(tg);
		spin.addChild(tg);

		// Lamp
		// FloorLamp floorLamp = new FloorLamp(app);

		//Appearance lampshadeApp = createTextureAppearance("images/lampshade-texture.jpg", true);
		
		TextureAppearance lampshadeApp  = new TextureAppearance("images/lampshade-texture.jpg", true, this); 
		Appearance lampApp = new Appearance();
		/*
		 Material bronze = new Material(); 
		 bronze.setAmbientColor(0.2125f, 0.1275f, 0.054f); 
		 bronze.setDiffuseColor(0.714f, 0.4284f, 0.18144f);
		 bronze.setSpecularColor(0.393548f, 0.271906f, 0.166721f);
		 bronze.setShininess(25.6f);
		 */
		MyMaterial bronze = new MyMaterial(MyMaterial.BRONZE);
		lampApp.setMaterial(bronze);

		FloorLamp2 floorLamp = new FloorLamp2(lampshadeApp, lampApp);

		tr = new Transform3D();
		tr.setScale(0.5f);
		tr.setTranslation(new Vector3f(-0.3f, 0f, 0f));
		tg = new TransformGroup(tr);
		tg.addChild(floorLamp);
		// root.addChild(tg);
		spin.addChild(tg);

		// Background
		Background background = new Background(new Color3f(Color.LIGHT_GRAY));
		background.setApplicationBounds(bounds);
		root.addChild(background);

		// Lights
		AmbientLight aLight = new AmbientLight(true, new Color3f(Color.WHITE));
		aLight.setInfluencingBounds(bounds);
		root.addChild(aLight);

		pLight = new PointLight(new Color3f(Color.YELLOW), new Point3f(3f, 3f, 3f), new Point3f(1f, 0f, 0f));
		// The pLight must have permissions to change its state
		pLight.setCapability(PointLight.ALLOW_STATE_READ);
		pLight.setCapability(PointLight.ALLOW_STATE_WRITE);
		pLight.setInfluencingBounds(bounds);
		root.addChild(pLight);

		SpotLight sLight = new SpotLight(new Color3f(Color.blue), new Point3f(0.5f, 1f, 0f), new Point3f(1f, 0f, 0f),
				new Vector3f(0f, -1f, 0f), (float) (Math.PI / 6.0), 0f);
		sLight.setInfluencingBounds(bounds);
		root.addChild(sLight);
		
		//root.addChild(new Spot(new Vector3f(0.5f, 1f, 0f), 0.05f, new Color3f(Color.RED)));

		return root;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		pc.setShapeLocation(e);
		PickResult result = pc.pickClosest();

		if (result != null) {
			// Verify it its is the table
			TransformGroup nodeTG = (TransformGroup) result.getNode(PickResult.TRANSFORM_GROUP);
			if (nodeTG != null) {
				// Get the actual geometric transformation of the nodeTG that os the parent of
				// the table
				Transform3D tr = new Transform3D();
				nodeTG.getTransform(tr);

				// Create a rotation and add it to the actual geometric transformation tr
				Transform3D rot = new Transform3D();
				rot.rotY(Math.PI / 8);
				tr.mul(rot);

				// Set the geometric transformation of the nodeTG with the new tr
				nodeTG.setTransform(tr);
			}

			// Verify if it is the Lampshade
			Shape3D nodeS = (Shape3D) result.getNode(PickResult.SHAPE3D);
			if (nodeS != null) {
				// System.out.println(nodeS.toString());
				if (nodeS.toString().contains("Lampshade")) {
					// System.out.println("X");
					if (pLight.getEnable())
						pLight.setEnable(false);
					else
						pLight.setEnable(true);
					return;
				}
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}
