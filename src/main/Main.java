package main;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.util.Enumeration;
import java.util.Random;

import javax.media.j3d.Alpha;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.Behavior;
import javax.media.j3d.Billboard;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Font3D;
import javax.media.j3d.FontExtrusion;
import javax.media.j3d.Geometry;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.GeometryUpdater;
import javax.media.j3d.IndexedQuadArray;
import javax.media.j3d.LineArray;
import javax.media.j3d.Material;
import javax.media.j3d.PhysicalBody;
import javax.media.j3d.PhysicalEnvironment;
import javax.media.j3d.PointLight;
import javax.media.j3d.RotPosPathInterpolator;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Text3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.media.j3d.ViewPlatform;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Quat4f;
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
import main.Fountain;
import main.Fountain.UpdateWaterBehavior;
import main.Fountain.WaterUpdater;
import shapes.Axes;
import shapes.Floor;
import shapes.Monitors;
import shapes.MyObj;
import shapes.Pc;
import shapes.myCube;
import shapes.tLamp;
import shapes.Desk;

public class Main extends Frame implements MouseListener {
	
	BoundingSphere bounds = new BoundingSphere(); // Bounds of the scene
	PointLight pLight = null; // Point light of the scene
	PickCanvas pc = null; // PickCanvas to perform picking
	JButton test;
	SimpleUniverse su;
	public boolean objColl = false;

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
		
        ////////////////////////////////////////////////////////////////////////////
        // 							Canvas / GUI
        ////////////////////////////////////////////////////////////////////////////
		
		// Create first canvas for the first view
		GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();
		Canvas3D cv1 = new Canvas3D(gc);
		cv1.addMouseListener(this); // Add a mouse listener to the canvas cv1 to get the mouse events	
		
		setLayout(new BorderLayout());
		add(cv1);
	    
        JPanel p = new JPanel();
        p.setPreferredSize(new Dimension(130, 200));
        p.setLayout(new FlowLayout());
        
        test = new JButton("Reset View");
        test.addActionListener(new ButtonsAction(this));
        p.add(test);
             
        add("East", p);
        pack();
		
		su = new SimpleUniverse(cv1, 2); 
		su.getViewingPlatform().setNominalViewingTransform();

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
		tr20.setTranslation(new Vector3f(-0.80f, 0.30f, 0f));
		TransformGroup tg20 = new TransformGroup(tr20);
		tg20.addChild(cube);
		root.addChild(tg20);
		
		
        ////////////////////////////////////////////////////////////////////////////
        // 							Fontain
        ////////////////////////////////////////////////////////////////////////////
		
        // Create the transform group node and initialize it to the
        // identity. Add it to the root of the subgraph.

        TransformGroup objSpin = new TransformGroup();
        objSpin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        
        Transform3D tr21 = new Transform3D();
        tr21.setTranslation(new Vector3f(0.75f, 0.35f, 0.2f));
        tr21.setScale(0.2f);
        TransformGroup tg21 = new TransformGroup(tr21);
        tg21.addChild(objSpin);
        root.addChild(tg21);

        // a bounding sphere specifies a region a behavior is active
        // create a sphere centered at the origin with radius of 1
        BoundingSphere bounds = new BoundingSphere();

        Fountain fountain = new Fountain();
        objSpin.addChild(fountain);

        Behavior waterBehavior = fountain.getWaterBehavior();
        waterBehavior.setSchedulingBounds(bounds);
        root.addChild(waterBehavior);
	
		
		
        ////////////////////////////////////////////////////////////////////////////
        // 							Billboard
        ////////////////////////////////////////////////////////////////////////////
        
		TransformGroup bbTg = new TransformGroup();
		bbTg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		root.addChild(bbTg);

		Billboard bb = new Billboard(bbTg, Billboard.ROTATE_ABOUT_POINT, new Point3f(0f, 0f, 2.0f));
		bb.setSchedulingBounds(bounds);
		bbTg.addChild(bb);

		Transform3D tr12 = new Transform3D();
		tr12.setTranslation(new Vector3f(0.0f, 0.0f, 0f));
		TransformGroup tg12 = new TransformGroup(tr12);
		ColorCube imagePanel = new ColorCube(0.1f);

		tg12.addChild(imagePanel);
		bbTg.addChild(tg12);

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
		
		
        ////////////////////////////////////////////////////////////////////////////
        // 							Moving Object 
        ////////////////////////////////////////////////////////////////////////////
		
		// OBJECT TO MOVE
		Appearance objApp = new Appearance();
		objApp.setMaterial(new MyMaterial(MyMaterial.RED));
		MyObj obj = new MyObj(0.025f, objApp);

		// TransformGroup to position the object
		Transform3D tr = new Transform3D();
		tr.setTranslation(new Vector3f(-0.32f, 0.28f, 0f));
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
		TextureAppearance deskApp = new TextureAppearance("images/wood.jpg", false, this); 

		Desk table = new Desk(deskApp);
		
		Transform3D desk = new Transform3D();
		desk.setScale(0.5f);
		desk.setTranslation(new Vector3f(0.5f, 0f, 0f));
		TransformGroup tDesk = new TransformGroup(desk);
		tDesk.addChild(table);
		spin.addChild(tDesk);
		

		// Monitors
		TextureAppearance screen1App = new TextureAppearance("images/screen.jpg", false, this); 
		TextureAppearance screen2App = new TextureAppearance("images/screen2.jpg", false, this); 
		Appearance plasticApp = new Appearance();
		Appearance GreyplasticApp = new Appearance();
		
		MyMaterial plastic = new MyMaterial(MyMaterial.PLASTIC);
		plasticApp.setMaterial(plastic);
		
		MyMaterial greyplastic = new MyMaterial(MyMaterial.WHITE_PLASTIC);
		GreyplasticApp.setMaterial(greyplastic);
		
		Monitors monitor = new Monitors(screen1App,screen2App, plasticApp, GreyplasticApp);
		
		Transform3D monitors = new Transform3D();
		monitors.setScale(0.5f);
		monitors.setTranslation(new Vector3f(0.5f, 0f, 0f));
		TransformGroup tMonitors = new TransformGroup(monitors);
		tMonitors.addChild(monitor);
		spin.addChild(tMonitors);
		
		// PC
		TextureAppearance caseApp = new TextureAppearance("images/case.jpg", false, this); 
		Appearance redApp = new Appearance();
		MyMaterial red = new MyMaterial(MyMaterial.RED);
		redApp.setMaterial(red);
		
		Pc computer = new Pc(plasticApp, redApp, caseApp);
		
		Transform3D comp = new Transform3D();
		comp.setScale(0.5f);
		comp.setTranslation(new Vector3f(0.5f, 0f, 0f));
		TransformGroup tComp = new TransformGroup(comp);
		tComp.addChild(computer);

		
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
		listenerGroup.addChild(tComp);
		
        ////////////////////////////////////////////////////////////////////////////
        // 							3D TEXT
        ////////////////////////////////////////////////////////////////////////////
		
		Appearance text3dap = new Appearance();
		text3dap.setMaterial(new Material());
		Font3D font = new Font3D(new Font("SansSerif", Font.PLAIN, 1), new FontExtrusion());
		Text3D text = new Text3D(font, "IPG");
		Shape3D shape3dtext = new Shape3D(text, text3dap);

		Transform3D textt = new Transform3D();
		textt.setScale(0.1);
		textt.setTranslation(new Vector3f(0.1f, 0.28f, 0f));
		TransformGroup ttext = new TransformGroup(textt);
		root.addChild(ttext);
		ttext.addChild(shape3dtext);
		
		// The tg that is parent of the table, must have permissions to be part of the
		// picking result and to read and write its geometric transformation
		ttext.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
		ttext.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		ttext.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
        ////////////////////////////////////////////////////////////////////////////
        // 							Geometry w/ Geometryinfo
        ////////////////////////////////////////////////////////////////////////////
		
		Appearance orangeApp = new Appearance();
		MyMaterial orange = new MyMaterial(MyMaterial.ORANGE);
		orangeApp.setMaterial(orange);
		
		Appearance ap = new Appearance();
		ap.setMaterial(new Material());
		Shape3D shape = new tLamp();
		shape.setAppearance(orangeApp);
		
		Transform3D geome = new Transform3D();
		geome.setScale(0.15);
		geome.setTranslation(new Vector3f(0f, 0.85f, 0f));
		TransformGroup tGeom = new TransformGroup(geome);
		spin.addChild(tGeom);
		tGeom.addChild(shape);
	
		
        ////////////////////////////////////////////////////////////////////////////
        // 							Walls
        ////////////////////////////////////////////////////////////////////////////
		
		Appearance testeApp = new Appearance();
		MyMaterial wallback = new MyMaterial(MyMaterial.WALL);
		testeApp.setMaterial(wallback);
		
        TransformGroup wallSideTg = new TransformGroup();
        Shape3D wallSide = new shapes.Wall().makeGround(new Point3f(1.0f, 1.0f, 1.0f),
                new Point3f(1.0f, 0f, 1.0f), new Point3f(1.0f, 0f, -1.0f),
                new Point3f(1.0f, 1.0f, -1.0f));
        wallSide.setAppearance(redApp);
        
        // BackWall
        Transform3D tWallB = new Transform3D();
        tWallB.rotY(Math.PI/2);
        TransformGroup wallBTg = new TransformGroup(tWallB);
        Shape3D wallB = new shapes.Wall().makeGround(new Point3f(1.0f, 1.0f, 1.0f),
                new Point3f(1.0f, 0f, 1.0f), new Point3f(1.0f, 0f, -1.0f),
                new Point3f(1.0f, 1.0f, -1.0f));
        wallB.setAppearance(redApp);
        
        wallSideTg.addChild(wallSide);
        root.addChild(wallSideTg);
        
        wallBTg.addChild(wallB);
        root.addChild(wallBTg);

        ////////////////////////////////////////////////////////////////////////////
        // 							Background
        ////////////////////////////////////////////////////////////////////////////
        
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
				
		/////////////////////////////////////////////////////////////////////////////
		////						ANIMATION
		////////////////////////////////////////////////////////////////////////////
		
		// Object
		Appearance myObjApp = new Appearance();
		myObjApp.setMaterial(new MyMaterial(MyMaterial.ORANGE));
		MyObj myObj = new MyObj(0.05f, myObjApp);

		// TransformGroup to move the object
		TransformGroup moveTg1 = new TransformGroup();
		moveTg1.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		moveTg1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		root.addChild(moveTg1);
		moveTg1.addChild(myObj);

		// Interpolator
		Point3f[] positions = new Point3f[5]; // Array of positions that thefine the path

		positions[0] = new Point3f(-0.50f, 0f, 0.50f);
		positions[1] = new Point3f(0.50f, 0f, 0.50f);
		positions[2] = new Point3f(0.50f, 0f, 0.50f);
		positions[3] = new Point3f(-0.50f, 0f, 0.50f);
		positions[4] = new Point3f(-0.50f, 0f, 0.50f);


		Quat4f[] quats = new Quat4f[5]; // Array of quaternions that define the orientation betwen postions
		Quat4f q = new Quat4f();
		for (int i = 0; i < quats.length; i++)
			quats[i] = new Quat4f();

		// Create the orientation as a AxisAngle4f object and convert it to a quaternion
		q.set(new AxisAngle4f(0f, 1f, 0f, (float) Math.toRadians(0)));
		quats[0].add(q);

		q.set(new AxisAngle4f(0f, 1f, 0f, (float) Math.toRadians(0)));
		quats[1].add(q);
		q.set(new AxisAngle4f(0f, 1f, 0f, (float) Math.toRadians(-90)));
		quats[2].add(q);


		float knots[] = new float[5]; 

		knots[0] = 0f;
		knots[1] = 0.25f;
		knots[2] = 0.50f;
		knots[3] = 0.75f;
		knots[4] = 1.0f;


		// Alpha alpha = new Alpha(-1, 10000);
		Alpha alpha1 = new Alpha(-1, 0, 2500, 10000, 0, 0); // Alpha with phaseDelayDuration

		Transform3D tr60 = new Transform3D();
		// tr.rotX(Math.toRadians(45));
		RotPosPathInterpolator interpolator = new RotPosPathInterpolator(alpha1, moveTg1, tr60, knots, quats, positions);
		interpolator.setSchedulingBounds(bounds);
		moveTg1.addChild(interpolator);

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
				rot.rotY(Math.PI / 1);
				tr.mul(rot);
				nodeTG.setTransform(tr);
			}

			// Verify if it is the Lampshade
			Shape3D nodeS = (Shape3D) result.getNode(PickResult.SHAPE3D);
			if (nodeS != null) {
				// System.out.println(nodeS.toString());
				if (nodeS.toString().contains("tLamp")) {
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

	public SimpleUniverse getSimpleU() {
		return su;
	}
	

}
