package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;

import javax.media.j3d.Alpha;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.Billboard;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Font3D;
import javax.media.j3d.FontExtrusion;
import javax.media.j3d.Material;
import javax.media.j3d.PhysicalBody;
import javax.media.j3d.PhysicalEnvironment;
import javax.media.j3d.PointLight;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Shape3D;
import javax.media.j3d.SpotLight;
import javax.media.j3d.Text3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.media.j3d.ViewPlatform;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import java.awt.*;
import javax.swing.*;

import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.picking.PickCanvas;
import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.picking.PickTool;
import com.sun.j3d.utils.universe.SimpleUniverse;

import appearance.MyMaterial;
import appearance.TextureAppearance;
import shapes.Axes;
import shapes.Floor;
import shapes.Monitors;
import shapes.MyObj;
import shapes.Pc;
import shapes.PyramidExample;
import shapes.myCube;
import shapes.Dodecahedron;
import shapes.Desk;

public class Main extends Frame implements MouseListener {

	BoundingSphere bounds = new BoundingSphere(); // Bounds of the scene
	PointLight pLight = null; // Point light of the scene
	PickCanvas pc = null; // PickCanvas to perform picking

	public static void main(String[] args) {
		Frame frame = new Main();
		frame.setPreferredSize(new Dimension(1200, 800));
		frame.setTitle("Java 3D CG");
		frame.pack();
		frame.setVisible(true);    
		
	}

	protected void processWindowEvent(WindowEvent e) {
		super.processWindowEvent(e);
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			System.exit(0);
		}
	}

	public Main() {
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

        ////////////////////////////////////////////////////////////////////////////
        // 							OrbitBehavior
        ////////////////////////////////////////////////////////////////////////////
		
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
		
	
        ////////////////////////////////////////////////////////////////////////////
        // 							Custom Geometry
        ////////////////////////////////////////////////////////////////////////////
		
		myCube cube = new myCube();
		
		Transform3D tr20 = new Transform3D();
		tr20.setScale(0.2f);
		tr20.setTranslation(new Vector3f(0f, 0.5f, 0f));
		TransformGroup tg20 = new TransformGroup(tr20);
		tg20.addChild(cube);
		root.addChild(tg20);
		
		
        ////////////////////////////////////////////////////////////////////////////
        // 							Billboard
        ////////////////////////////////////////////////////////////////////////////
        
        Vector3f translate = new Vector3f();
        Transform3D T3D = new Transform3D();
        TransformGroup TGT = new TransformGroup();
        TransformGroup TGR = new TransformGroup(); 
        Billboard billboard = null;
        BoundingSphere bSphere = new BoundingSphere();
        
        translate.set(new Vector3f(1.0f, 1.0f, 0.0f));
        T3D.setTranslation(translate);
        
        // set up for billboard behavior
        TGR.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        billboard = new Billboard(TGR);
        billboard.setSchedulingBounds(bSphere);
        
        // assemble scene graph
        root.addChild(TGT);
        root.addChild(billboard);
        TGT.addChild(TGR);
        TGR.addChild(new ColorCube(0.1f));
        
        ////////////////////////////////////////////////////////////////////////////
        
		// Axes
		root.addChild(new Axes(new Color3f(Color.RED), 3, 0.5f));

		// Floor
		root.addChild(new Floor(10, -1, 1, new Color3f(Color.BLACK), new Color3f(Color.WHITE), true));
		
		
		TransformGroup spin = new TransformGroup();
		spin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		root.addChild(spin);
		
		// To rotate the view around the Y axis, the rotator controls the tgView and the rotator is a child of the root.
		Alpha alpha = new Alpha(-1, 30000);
		RotationInterpolator rotator = new RotationInterpolator(alpha, tgView);
		rotator.setSchedulingBounds(bounds);
		//root.addChild(rotator); ROTATE ANIMATION
		// spin.addChild(rotator);
		
		
        ////////////////////////////////////////////////////////////////////////////
        // 							Moving Object 
        ////////////////////////////////////////////////////////////////////////////
		
		// OBJECT TO MOVE
		Appearance objApp = new Appearance();
		objApp.setMaterial(new MyMaterial(MyMaterial.RED));
		// Box obj = new Box(0.2f, 0.2f, 0.2f, Primitive.GENERATE_NORMALS, objApp);
		MyObj obj = new MyObj(0.025f, objApp);

		// TransformGroup to position the object
		Transform3D tr = new Transform3D();
		tr.setTranslation(new Vector3f(0f, 0f, 0f));
		TransformGroup tg = new TransformGroup(tr);
		tg.addChild(obj);
		// tg.addChild(new Axes(new Color3f(Color.BLUE), 3, 0.5f));

		// TransformGroup to move the object
		TransformGroup moveTg = new TransformGroup();
		moveTg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		moveTg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		moveTg.addChild(tg);
		root.addChild(moveTg);
		
		// Behavior to move the object.
		KeyControl kc = new KeyControl(moveTg, obj);
		kc.setSchedulingBounds(bounds);
		root.addChild(kc);
		
		
        ////////////////////////////////////////////////////////////////////////////
        // 							DESK / MONITOR / PC
        ////////////////////////////////////////////////////////////////////////////
		
		// Desk
		TextureAppearance topApp = new TextureAppearance("images/wood3.jpg", false, this); 
		Appearance legApp = new Appearance();

		Desk table = new Desk(topApp, legApp);
		
		Transform3D tr1 = new Transform3D();
		tr1.setScale(0.5f);
		tr1.setTranslation(new Vector3f(0.5f, 0f, 0f));
		TransformGroup tg1 = new TransformGroup(tr1);
		tg1.addChild(table);
		// root.addChild(tg);
		spin.addChild(tg1);


		// Monitors
		TextureAppearance pc2App = new TextureAppearance("images/screen.jpg", false, this); 
		TextureAppearance pc3App = new TextureAppearance("images/screen2.jpg", false, this); 
		Appearance plasticApp = new Appearance();
		
		MyMaterial plastic = new MyMaterial(MyMaterial.PLASTIC);
		plasticApp.setMaterial(plastic);
		
		Monitors monitor = new Monitors(pc2App,pc3App, plasticApp);
		
		Transform3D tr11 = new Transform3D();
		tr11.setScale(0.5f);
		tr11.setTranslation(new Vector3f(0.5f, 0f, 0f));
		TransformGroup tg11 = new TransformGroup(tr11);
		tg11.addChild(monitor);
		spin.addChild(tg11);
		
		// PC
		Appearance redApp = new Appearance();
		MyMaterial red = new MyMaterial(MyMaterial.RED);
		redApp.setMaterial(red);
		
		Pc computer = new Pc(plasticApp, redApp);
		
		Transform3D tr2 = new Transform3D();
		tr2.setScale(0.5f);
		tr2.setTranslation(new Vector3f(0.5f, 0f, 0f));
		TransformGroup tg2 = new TransformGroup(tr2);
		tg2.addChild(computer);

		
        ////////////////////////////////////////////////////////////////////////////
        // 							KeyNavigatorBehavior
        ////////////////////////////////////////////////////////////////////////////
		
		TransformGroup listenerGroup = new TransformGroup();
        listenerGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        listenerGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        root.addChild(listenerGroup);

        KeyNavigatorBehavior behaviour = new KeyNavigatorBehavior(listenerGroup);
        behaviour.setSchedulingBounds(new BoundingSphere(new Point3d(), 2));

        listenerGroup.addChild(behaviour);
		listenerGroup.addChild(tg2);
		
        ////////////////////////////////////////////////////////////////////////////
        // 							3D TEXT
        ////////////////////////////////////////////////////////////////////////////
		
		Appearance text3dap = new Appearance();
		text3dap.setMaterial(new Material());
		Font3D font = new Font3D(new Font("SansSerif", Font.PLAIN, 1), new FontExtrusion());
		Text3D text = new Text3D(font, "IPG");
		Shape3D shape3dtext = new Shape3D(text, text3dap);

		Transform3D tr5 = new Transform3D();
		tr5.setScale(0.1);
		tr5.setTranslation(new Vector3f(0.1f, 0.28f, 0f));
		TransformGroup tg5 = new TransformGroup(tr5);
		root.addChild(tg5);
		tg5.addChild(shape3dtext);
		
		// The tg that is parent of the table, must have permissions to be part of the
		// picking result and to read and write its geometric transformation
		tg5.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
		tg5.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		tg5.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
        ////////////////////////////////////////////////////////////////////////////
        // 							Geometry
        ////////////////////////////////////////////////////////////////////////////
		
		Appearance orangeApp = new Appearance();
		MyMaterial orange = new MyMaterial(MyMaterial.ORANGE);
		orangeApp.setMaterial(orange);
		
		Appearance ap = new Appearance();
		ap.setMaterial(new Material());
		Shape3D shape = new Dodecahedron();
		shape.setAppearance(orangeApp);
		
		Transform3D tr4 = new Transform3D();
		tr4.setScale(0.05);
		tr4.setTranslation(new Vector3f(0f, 1f, 0f));
		TransformGroup tg4 = new TransformGroup(tr4);
		spin.addChild(tg4);
		tg4.addChild(shape);
	
		
        ////////////////////////////////////////////////////////////////////////////
        // 							Walls
        ////////////////////////////////////////////////////////////////////////////
		
		Appearance testeApp = new Appearance();
		MyMaterial wallback = new MyMaterial(MyMaterial.WALL);
		testeApp.setMaterial(wallback);
		
        TransformGroup wall1Tg = new TransformGroup();
        Shape3D wall1 = new shapes.Wall().makeGround(new Point3f(1.0f, 1.0f, 1.0f),
                new Point3f(1.0f, 0f, 1.0f), new Point3f(1.0f, 0f, -1.0f),
                new Point3f(1.0f, 1.0f, -1.0f));
        wall1.setAppearance(redApp);
        
        // BackWall
        Transform3D transformWall3 = new Transform3D();
        transformWall3.rotY(Math.PI/2);
        TransformGroup wall3Tg = new TransformGroup(transformWall3);
        Shape3D wall3 = new shapes.Wall().makeGround(new Point3f(1.0f, 1.0f, 1.0f),
                new Point3f(1.0f, 0f, 1.0f), new Point3f(1.0f, 0f, -1.0f),
                new Point3f(1.0f, 1.0f, -1.0f));
        wall3.setAppearance(redApp);
        
        wall1Tg.addChild(wall1);
        root.addChild(wall1Tg);
        
        wall3Tg.addChild(wall3);
        root.addChild(wall3Tg);

		// Background
		Background background = new Background(new Color3f(Color.BLACK));
		background.setApplicationBounds(bounds);
		root.addChild(background);

        ////////////////////////////////////////////////////////////////////////////
        // 							Lights
        ////////////////////////////////////////////////////////////////////////////
		
		AmbientLight aLight = new AmbientLight(true, new Color3f(Color.WHITE));
		aLight.setInfluencingBounds(bounds);
		root.addChild(aLight);

		pLight = new PointLight(new Color3f(Color.white), new Point3f(3f, 3f, 3f), new Point3f(1f, 0f, 0f));
		pLight.setCapability(PointLight.ALLOW_STATE_READ);
		pLight.setCapability(PointLight.ALLOW_STATE_WRITE);
		pLight.setInfluencingBounds(bounds);
		root.addChild(pLight);

		SpotLight sLight = new SpotLight(new Color3f(Color.red), new Point3f(0.5f, 1f, 0f), new Point3f(1f, 0f, 0f),
		new Vector3f(0f, -1f, 0f), (float) (Math.PI / 6.0), 0f);
		sLight.setInfluencingBounds(bounds);
		root.addChild(sLight);

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
				if (nodeS.toString().contains("Dodecahedron")) {
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
