package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.media.j3d.Alpha;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.AudioDevice;
import javax.media.j3d.Background;
import javax.media.j3d.BackgroundSound;
import javax.media.j3d.Behavior;
import javax.media.j3d.Billboard;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.DistanceLOD;
import javax.media.j3d.Font3D;
import javax.media.j3d.FontExtrusion;
import javax.media.j3d.Geometry;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.GeometryUpdater;
import javax.media.j3d.ImageComponent;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.IndexedQuadArray;
import javax.media.j3d.IndexedTriangleArray;
import javax.media.j3d.LineArray;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.Material;
import javax.media.j3d.MediaContainer;
import javax.media.j3d.Morph;
import javax.media.j3d.PhysicalBody;
import javax.media.j3d.PhysicalEnvironment;
import javax.media.j3d.PointLight;
import javax.media.j3d.PointSound;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.QuadArray;
import javax.media.j3d.RenderingAttributes;
import javax.media.j3d.RotPosPathInterpolator;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Screen3D;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Sound;
import javax.media.j3d.Switch;
import javax.media.j3d.TexCoordGeneration;
import javax.media.j3d.Text3D;
import javax.media.j3d.Texture;
import javax.media.j3d.Texture2D;
import javax.media.j3d.TextureCubeMap;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.media.j3d.View;
import javax.media.j3d.ViewPlatform;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Point4d;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4d;
import javax.vecmath.Vector4f;

import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;

import com.sun.j3d.utils.behaviors.mouse.MouseBehavior;

import java.awt.*;
import javax.swing.*;

import com.sun.j3d.audioengines.javasound.JavaSoundMixer;
import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.picking.PickCanvas;
import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.picking.PickTool;
import com.sun.j3d.utils.universe.SimpleUniverse;

import main.MorphingBehavior;
import appearance.MyMaterial;
import appearance.TextureAppearance;
import main.Fountain;
import main.Fountain.UpdateWaterBehavior;
import main.Fountain.WaterUpdater;
//
import shapes.Axes;
import shapes.BoxStand;
import shapes.Floor;
import shapes.GeometryInfoExample;
import shapes.Monitors;
import shapes.MyObj;
import shapes.Pc;
import shapes.PolygonArray;
import shapes.myCube;
import shapes.tLamp;

import shapes.Desk;
import shapes.Primitives;
import shapes.QuadroData;

public class Main extends Frame implements MouseListener {

	SimpleUniverse su;
	BoundingSphere bounds = new BoundingSphere(); // Bounds of the scene
	PointLight pLight = null; // Point light of the scene
	PickCanvas pc = null; // PickCanvas to perform picking
	JButton test, test2, test3;
	BufferedImage[] images = new BufferedImage[3];

	private Canvas3D cv1;
	private Canvas3D offScreenCanvas;
	private View view;

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
		// Canvas / GUI
		////////////////////////////////////////////////////////////////////////////

		// Create first canvas for the first view
		GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();
		cv1 = new Canvas3D(gc);
		cv1.addMouseListener(this); // Add a mouse listener to the canvas cv1 to get the mouse events

		setLayout(new BorderLayout());
		add(cv1);

		JPanel p = new JPanel();
		p.setPreferredSize(new Dimension(130, 200));
		p.setLayout(new FlowLayout());

		test = new JButton("Reset View");
		test.addActionListener(new ButtonsAction(this));
		p.add(test);

		test2 = new JButton("Save");
		test2.addActionListener(new ButtonsAction(this));

		add("East", p);
		pack();

		su = new SimpleUniverse(cv1, 2);
		su.getViewingPlatform().setNominalViewingTransform();

		// Create the content branch of the scene graph
		// BranchGroup bg = createSceneGraph();
		// The first TransformGroup of the view is passed and it will be controlled by
		// the rotator
		// to rotate the view around the Y axis
		BranchGroup bg = createSceneGraph(su.getViewingPlatform().getMultiTransformGroup().getTransformGroup(0));
		bg.compile();
		su.addBranchGraph(bg); // Add the content branch to the simple universe

		// Create a PickCanvas foe the first view (cv1)
		pc = new PickCanvas(cv1, bg);
		pc.setMode(PickTool.GEOMETRY);

		// sound3D

		AudioDevice audioDev = new JavaSoundMixer(su.getViewer().getPhysicalEnvironment());
		audioDev.initialize();

		////////////////////////////////////////////////////////////////////////////
		// OrbitBehavior
		////////////////////////////////////////////////////////////////////////////

		OrbitBehavior orbit = new OrbitBehavior(cv1);
		orbit.setSchedulingBounds(bounds);
		su.getViewingPlatform().setViewPlatformBehavior(orbit);

		// create off screen canvas
		view = su.getViewer().getView();
		offScreenCanvas = new Canvas3D(gc, true);
		Screen3D sOn = cv1.getScreen3D();
		Screen3D sOff = offScreenCanvas.getScreen3D();
		Dimension dim = sOn.getSize();
		sOff.setSize(dim);
		sOff.setPhysicalScreenWidth(sOn.getPhysicalScreenWidth());
		sOff.setPhysicalScreenHeight(sOn.getPhysicalScreenHeight());
		Point loc = cv1.getLocationOnScreen();
		offScreenCanvas.setOffScreenLocation(loc);
		// button
		Button test3 = new Button("Save image");
		p.add(test3);
		test3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				BufferedImage bi = capture();
				save(bi);
			}
		});

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
		// Primitives
		////////////////////////////////////////////////////////////////////////////

		Appearance BlackPlasticApp = new Appearance();
		MyMaterial blackplastic = new MyMaterial(MyMaterial.BLACK_PLASTIC);
		BlackPlasticApp.setMaterial(blackplastic);

		Primitives primitive = new Primitives(BlackPlasticApp);

		Transform3D primitives = new Transform3D();
		primitives.setScale(0.5f);
		primitives.setTranslation(new Vector3f(0.0f, 0.30f, -0.80f));
		TransformGroup tPrimitives = new TransformGroup(primitives);
		tPrimitives.addChild(primitive);
		root.addChild(tPrimitives);

		////////////////////////////////////////////////////////////////////////////
		// SHAPE IndexedLineArray
		////////////////////////////////////////////////////////////////////////////

		Appearance apPolygn = new Appearance();
		apPolygn.setMaterial(new Material());
		Shape3D cube = new myCube();
		cube.setAppearance(apPolygn);

		Transform3D trcube = new Transform3D();
		trcube.setScale(0.15);
		trcube.setTranslation(new Vector3f(-0.5f, 0.3f, -0.85f));
		TransformGroup tgcube = new TransformGroup(trcube);
		tgcube.addChild(cube);
		root.addChild(tgcube);

		////////////////////////////////////////////////////////////////////////////
		// Polygn Shades / Transparency / FLAT / NICEST / GOURAUD
		////////////////////////////////////////////////////////////////////////////

		Appearance apGouraud = new Appearance();

		// SHADE GOURAD
		Color3f colour = new Color3f(Color.orange);
		Appearance appearanceGouraud = new Appearance();
		appearanceGouraud.setColoringAttributes(new ColoringAttributes(colour, ColoringAttributes.SHADE_GOURAUD));
		Material m = new Material();
		m.setAmbientColor(colour);
		m.setEmissiveColor(0.255f, 0f, 0f);
		m.setDiffuseColor(colour);
		m.setSpecularColor(1f, 1f, 1f);
		m.setShininess(0f);
		appearanceGouraud.setMaterial(m);

		// transparent
		int tMode = TransparencyAttributes.BLENDED;
		float tValue = 0.6f;
		TransparencyAttributes ta = new TransparencyAttributes(tMode, tValue);
		Appearance appearanceTransparente = new Appearance();
		appearanceTransparente.setTransparencyAttributes(ta);
		Material m2 = new Material();
		m2.setAmbientColor(colour);
		m2.setEmissiveColor(0.255f, 0f, 0f);
		m2.setDiffuseColor(colour);
		m2.setSpecularColor(1f, 1f, 1f);
		m2.setShininess(0f);
		appearanceTransparente.setMaterial(m2);

		// flat
		Appearance appearanceFlat = new Appearance();
		ColoringAttributes ca2 = new ColoringAttributes();
		ca2.setShadeModel(ColoringAttributes.SHADE_FLAT);
		appearanceFlat.setColoringAttributes(ca2);
		appearanceFlat.setMaterial(null);
		RenderingAttributes ra2 = new RenderingAttributes();
		ra2.setIgnoreVertexColors(false);
		appearanceFlat.setRenderingAttributes(ra2);
		Material m3 = new Material();
		m3.setAmbientColor(colour);
		m3.setEmissiveColor(0.255f, 0f, 0f);
		m3.setDiffuseColor(colour);
		m3.setSpecularColor(1f, 1f, 1f);
		m3.setShininess(0f);
		appearanceFlat.setMaterial(m3);

		// Lines
		Appearance appearanceLines = new Appearance();
		appearanceLines.setPolygonAttributes(
				new PolygonAttributes(PolygonAttributes.POLYGON_LINE, PolygonAttributes.CULL_BACK, 0));
		appearanceLines.setLineAttributes(new LineAttributes(3, LineAttributes.PATTERN_DASH, false));
		Material m4 = new Material();
		m4.setAmbientColor(colour);
		m4.setEmissiveColor(0.255f, 0f, 0f);
		m4.setDiffuseColor(colour);
		m4.setSpecularColor(1f, 1f, 1f);
		m4.setShininess(0f);
		appearanceLines.setMaterial(m3);

		ColorOptions colorOptions = new ColorOptions(BlackPlasticApp, appearanceGouraud, appearanceTransparente,
				appearanceFlat, appearanceLines);
		root.addChild(colorOptions);

		////////////////////////////////////////////////////////////////////////////
		// Animation Behaviour
		////////////////////////////////////////////////////////////////////////////

		TransformGroup objRotate = new TransformGroup();
		objRotate.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		root.addChild(objRotate);
		objRotate.addChild(new ColorCube(0.1));
		Animation myRotationBehavior = new Animation(objRotate);
		myRotationBehavior.setSchedulingBounds(new BoundingSphere());
		root.addChild(myRotationBehavior);

		////////////////////////////////////////////////////////////////////////////
		// TEXT INFOS
		////////////////////////////////////////////////////////////////////////////

		TextInfo textinfo = new TextInfo();
		root.addChild(textinfo);

		////////////////////////////////////////////////////////////////////////////
		// Custom Geometry
		////////////////////////////////////////////////////////////////////////////

		Appearance apMirror = createTextureAppearanceMirror();

		TexCoordGeneration tcg = new TexCoordGeneration();
		tcg.setGenMode(TexCoordGeneration.REFLECTION_MAP);
		tcg.setFormat(TexCoordGeneration.TEXTURE_COORDINATE_3);
		tcg.setPlaneR(new Vector4f(2, 0, 0, 0));
		tcg.setPlaneS(new Vector4f(0, 2, 0, 0));
		tcg.setPlaneT(new Vector4f(0, 0, 2, 0));
		apMirror.setTexCoordGeneration(tcg);

		PolygonArray polygon = new PolygonArray();
		polygon.setAppearance(apMirror);

		Transform3D trMyCustomShape = new Transform3D();
		trMyCustomShape.setScale(0.15f);
		trMyCustomShape.setTranslation(new Vector3f(0.40f, 0.0f, -0.85f));
		TransformGroup tgMyCustomShape = new TransformGroup(trMyCustomShape);
		tgMyCustomShape.addChild(polygon);
		root.addChild(tgMyCustomShape);

		////////////////////////////////////////////////////////////////////////////
		// Fontain
		////////////////////////////////////////////////////////////////////////////

		// Create the transform group node and initialize it to the
		// identity. Add it to the root of the subgraph.

		TransformGroup objSpin = new TransformGroup();
		objSpin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D trFountain = new Transform3D();
		trFountain.setTranslation(new Vector3f(-0.6f, 0.35f, -0.80f));
		trFountain.setScale(0.2f);
		TransformGroup tgFountain = new TransformGroup(trFountain);
		tgFountain.addChild(objSpin);
		root.addChild(tgFountain);

		// a bounding sphere specifies a region a behavior is active
		// create a sphere centered at the origin with radius of 1
		BoundingSphere bounds = new BoundingSphere();

		Fountain fountain = new Fountain();
		objSpin.addChild(fountain);

		Behavior waterBehavior = fountain.getWaterBehavior();
		waterBehavior.setSchedulingBounds(bounds);
		root.addChild(waterBehavior);

		////////////////////////////////////////////////////////////////////////////
		// Axes Helper
		////////////////////////////////////////////////////////////////////////////
		/*
		 * // Axes root.addChild(new Axes(new Color3f(Color.RED), 3, 0.5f));
		 */
		// Floor
		//Color3f floorc = new Color3f(new Vector3f(-0.5f, 0.3f, -0.85f));
		root.addChild(new Floor(1, -1, 1, new Color3f(new Vector3f(0.083f, 0.083f, 0.083f)), new Color3f(Color.WHITE), true));
		
		

		TransformGroup spin = new TransformGroup();
		spin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		root.addChild(spin);

		// To rotate the view around the Y axis, the rotator controls the tgView and the
		// rotator is a child of the root.
		Alpha alpha = new Alpha(-1, 30000);
		RotationInterpolator rotator = new RotationInterpolator(alpha, tgView);
		rotator.setSchedulingBounds(bounds);

		////////////////////////////////////////////////////////////////////////////
		// Moving Object
		////////////////////////////////////////////////////////////////////////////

		Appearance objApp = new Appearance();
		objApp.setMaterial(new MyMaterial(MyMaterial.RED));
		ColorCube obj = new ColorCube(0.05f);

		Transform3D trObj = new Transform3D();
		trObj.setTranslation(new Vector3f(-0.28f, 0.05f, 0f));
		TransformGroup tgObj = new TransformGroup(trObj);
		tgObj.addChild(obj);

		// TransformGroup to move the object
		TransformGroup moveTg = new TransformGroup();
		moveTg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		moveTg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		moveTg.addChild(tgObj);
		root.addChild(moveTg);

		// Behavior to move the object.
		KeyControl kc = new KeyControl(moveTg, obj);
		kc.setSchedulingBounds(bounds);
		root.addChild(kc);

		////////////////////////////////////////////////////////////////////////////
		// Paintings / Table
		////////////////////////////////////////////////////////////////////////////

		// Table Texture
		TextureAppearance deskApp = new TextureAppearance("images/wood.jpg", false, this);

		Desk table = new Desk(deskApp);

		Transform3D desk = new Transform3D();
		desk.setScale(0.5f);
		desk.setTranslation(new Vector3f(0.5f, 0f, -0.80f));
		TransformGroup tDesk = new TransformGroup(desk);
		tDesk.addChild(table);
		spin.addChild(tDesk);

		// Paintings
		TextureAppearance screen1App = new TextureAppearance("images/screen1.jpg", false, this);
		TextureAppearance screen2App = new TextureAppearance("images/screen2.jpg", false, this);
		TextureAppearance screen3App = new TextureAppearance("images/screen3.jpg", false, this);
		TextureAppearance screen4App = new TextureAppearance("images/screen4.jpg", false, this);
		Appearance plasticApp = new Appearance();
		Appearance GreyplasticApp = new Appearance();

		MyMaterial plastic = new MyMaterial(MyMaterial.PLASTIC);
		plasticApp.setMaterial(plastic);

		MyMaterial greyplastic = new MyMaterial(MyMaterial.WHITE_PLASTIC);
		GreyplasticApp.setMaterial(greyplastic);

		Monitors monitor = new Monitors(screen1App, screen2App, screen3App, plasticApp);

		Transform3D monitors = new Transform3D();
		monitors.setScale(0.5f);
		monitors.setTranslation(new Vector3f(0.5f, 0f, -0.75f));
		TransformGroup tMonitors = new TransformGroup(monitors);
		tMonitors.addChild(monitor);
		spin.addChild(tMonitors);

		////////////////////////////////////////////////////////////////////////////
		// Box Stands
		////////////////////////////////////////////////////////////////////////////

		BoxStand boxstand = new BoxStand(screen1App);

		Transform3D boxstands = new Transform3D();
		boxstands.setScale(0.5f);
		boxstands.setTranslation(new Vector3f(0.0f, 0.0f, 0.0f));
		TransformGroup tBoxstands = new TransformGroup(boxstands);
		tBoxstands.addChild(boxstand);
		spin.addChild(tBoxstands);

		
		////////////////////////////////////////////////////////////////////////////
		// quadrodata
		////////////////////////////////////////////////////////////////////////////
		
		Appearance AppQuadro = new Appearance();
		MyMaterial quadroDataM = new MyMaterial(MyMaterial.ORANGE);
		AppQuadro.setMaterial(quadroDataM);

		Shape3D quadrodata = new Shape3D(createGeometry(), AppQuadro);

		Transform3D trquadrodatas = new Transform3D();
		
		trquadrodatas.rotX(Math.toRadians(90));
		trquadrodatas.setScale(0.065f);
	    trquadrodatas.setTranslation(new Vector3f(0.58f, 0.56f, -0.85f));
		TransformGroup tgquadroDatas = new TransformGroup(trquadrodatas);
		tgquadroDatas.addChild(quadrodata);
		root.addChild(tgquadroDatas);

		////////////////////////////////////////////////////////////////////////////
		// Moving PC
		////////////////////////////////////////////////////////////////////////////

		TextureAppearance caseApp = new TextureAppearance("images/case.jpg", false, this);
		Appearance redApp = new Appearance();
		MyMaterial red = new MyMaterial(MyMaterial.RED);
		redApp.setMaterial(red);

		Pc computer = new Pc(plasticApp, redApp, caseApp);

		Transform3D comp = new Transform3D();
		comp.setScale(0.2f);
		comp.setTranslation(new Vector3f(0f, 0f, 0f));
		TransformGroup tComp = new TransformGroup(comp);
		tComp.addChild(computer);

		////////////////////////////////////////////////////////////////////////////
		// KeyNavigatorBehavior
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
		// 3D TEXT
		////////////////////////////////////////////////////////////////////////////

		Appearance text3dap = new Appearance();
		text3dap.setMaterial(new Material());
		Font3D font = new Font3D(new Font("SansSerif", Font.PLAIN, 1), new FontExtrusion());
		Text3D text = new Text3D(font, "2022");
		Shape3D shape3dtext = new Shape3D(text, text3dap);

		Transform3D textt = new Transform3D();
		textt.setScale(0.1);
		textt.setTranslation(new Vector3f(-0.25f, 0.28f, -0.85f));
		TransformGroup ttext = new TransformGroup(textt);
		root.addChild(ttext);
		ttext.addChild(shape3dtext);

		// The tg that is parent of the table, must have permissions to be part of the
		// picking result and to read and write its geometric transformation
		ttext.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
		ttext.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		ttext.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		////////////////////////////////////////////////////////////////////////////
		// Geometry w/ Geometryinfo
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
		// LOD
		////////////////////////////////////////////////////////////////////////////

		Transform3D trm1 = new Transform3D();
		trm1.setTranslation(new Vector3f(0.65f, 0.4f, 0.75f));

		TransformGroup objTrans = new TransformGroup(trm1);
		root.addChild(objTrans);

		Switch sw = new Switch(0);
		sw.setCapability(Switch.ALLOW_SWITCH_READ);
		sw.setCapability(Switch.ALLOW_SWITCH_WRITE);
		objTrans.addChild(sw);

		// 3 levels of view
		loadImages();
		Appearance apLod = createAppearance(0);
		sw.addChild(new Sphere(0.1f, Primitive.GENERATE_TEXTURE_COORDS, 40, apLod));
		apLod = createAppearance(1);
		sw.addChild(new Sphere(0.1f, Primitive.GENERATE_TEXTURE_COORDS, 20, apLod));
		apLod = createAppearance(2);
		sw.addChild(new Sphere(0.1f, Primitive.GENERATE_TEXTURE_COORDS, 10, apLod));

		// the DistanceLOD behavior
		float[] distances = new float[2];
		distances[0] = 2.5f;
		distances[1] = 5.0f;

		DistanceLOD lod = new DistanceLOD(distances);
		lod.addSwitch(sw);

		BoundingSphere boundsLod = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 5.0);
		lod.setSchedulingBounds(boundsLod);
		objTrans.addChild(lod);

		////////////////////////////////////////////////////////////////////////////
		// Walls
		////////////////////////////////////////////////////////////////////////////

		TransformGroup wallSideTg = new TransformGroup();
		Shape3D wallSide = new shapes.Wall().makeGround(new Point3f(1.0f, 1.0f, 1.0f), new Point3f(1.0f, 0f, 1.0f),
				new Point3f(1.0f, 0f, -1.0f), new Point3f(1.0f, 1.0f, -1.0f));

		// BackWall
		Transform3D tWallB = new Transform3D();
		tWallB.rotY(Math.PI / 2);
		TransformGroup wallBTg = new TransformGroup(tWallB);
		Shape3D wallB = new shapes.Wall().makeGround(new Point3f(1.0f, 1.0f, 1.0f), new Point3f(1.0f, 0f, 1.0f),
				new Point3f(1.0f, 0f, -1.0f), new Point3f(1.0f, 1.0f, -1.0f));
		// wallB.setAppearance(BlackPlasticApp);
		// wallB.setAppearanceOverrideEnable(true);
		wallB.setAppearance(BlackPlasticApp);
		wallSide.setAppearance(BlackPlasticApp);

		wallSideTg.addChild(wallSide);
		root.addChild(wallSideTg);

		wallBTg.addChild(wallB);
		root.addChild(wallBTg);

		////////////////////////////////////////////////////////////////////////////
		// Background
		////////////////////////////////////////////////////////////////////////////

		// Background background = new Background(new Color3f(Color.BLACK));
		// background.setApplicationBounds(bounds);
		// root.addChild(background);

		// Background
		java.net.URL urlBg = getClass().getClassLoader().getResource("images/bg.jpg");
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(urlBg);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		ImageComponent2D image = new ImageComponent2D(ImageComponent2D.FORMAT_RGB, bi);
		Background background = new Background(image);
		background.setApplicationBounds(bounds);
		root.addChild(background);

		////////////////////////////////////////////////////////////////////////////
		// Sound
		////////////////////////////////////////////////////////////////////////////

		PointSound sound = new PointSound();
		java.net.URL url = this.getClass().getClassLoader().getResource("images/sound.au");
		MediaContainer mc = new MediaContainer(url);
		sound.setSoundData(mc);
		sound.setLoop(Sound.INFINITE_LOOPS);
		sound.setInitialGain(1f);
		sound.setEnable(true);
		float[] distancesSound = { 1f, 20f };
		float[] gains = { 1f, 0.001f };
		sound.setDistanceGain(distancesSound, gains);
		BoundingSphere soundBounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
		sound.setSchedulingBounds(soundBounds);
		root.addChild(sound);

		////////////////////////////////////////////////////////////////////////////
		// Lights
		////////////////////////////////////////////////////////////////////////////

		AmbientLight aLight = new AmbientLight(true, new Color3f(Color.WHITE));
		aLight.setInfluencingBounds(bounds);
		root.addChild(aLight);

		pLight = new PointLight(new Color3f(Color.white), new Point3f(3f, 3f, 3f), new Point3f(1f, 0f, 0f));
		pLight.setCapability(PointLight.ALLOW_STATE_READ);
		pLight.setCapability(PointLight.ALLOW_STATE_WRITE);
		pLight.setInfluencingBounds(bounds);
		root.addChild(pLight);
		
		//Light1
		PointLight pLight2 = new PointLight(new Color3f(Color.white), new Point3f(0.40f, 0.1f, 0.70f), new Point3f(1f, 0f, 1f));
		pLight2.setCapability(PointLight.ALLOW_STATE_READ);
		pLight2.setCapability(PointLight.ALLOW_STATE_WRITE);
		pLight2.setInfluencingBounds(bounds);
		root.addChild(pLight2);

		/////////////////////////////////////////////////////////////////////////////
		//// ANIMATION / Interpolator
		////////////////////////////////////////////////////////////////////////////

		// Object
		Appearance myObjApp = new Appearance();
		myObjApp.setMaterial(new MyMaterial(MyMaterial.RED));
		MyObj myObj = new MyObj(1f, screen4App, plasticApp);

		// TransformGroup to move the object
		TransformGroup moveTg1 = new TransformGroup();
		moveTg1.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		moveTg1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		root.addChild(moveTg1);
		moveTg1.addChild(myObj);

		// Interpolator
		Point3f[] positions = new Point3f[5]; // Array of positions that thefine the path

		positions[0] = new Point3f(0.10f, -0.10f, 0f);
		positions[1] = new Point3f(0.10f, 0.10f, 0f);
		positions[2] = new Point3f(0.10f, 0.10f, 0f);
		positions[3] = new Point3f(0.10f, -0.10f, 0f);
		positions[4] = new Point3f(0.10f, -0.10f, 0f);

		Quat4f[] quats = new Quat4f[5]; // Array of quaternions that define the orientation betwen postions
		Quat4f q = new Quat4f();
		for (int i = 0; i < quats.length; i++)
			quats[i] = new Quat4f();

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
		RotPosPathInterpolator interpolator = new RotPosPathInterpolator(alpha1, moveTg1, tr60, knots, quats,
				positions);
		interpolator.setSchedulingBounds(bounds);
		moveTg1.addChild(interpolator);

		Appearance appear = new Appearance();

		appear.setPolygonAttributes(
				new PolygonAttributes(PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_BACK, 0));
		appear.setLineAttributes(new LineAttributes(3, LineAttributes.PATTERN_SOLID, false));

		/////////////////////////////////////////////////////////////////////////////
		//// MORPHING
		////////////////////////////////////////////////////////////////////////////

		GeometryArray[] geoms = new GeometryArray[4];
		geoms[0] = morph1();
		geoms[1] = morph2();
		geoms[2] = morph3();
		geoms[3] = morph4();

		Morph morph = new Morph(geoms, appear);
		morph.setCapability(Morph.ALLOW_WEIGHTS_READ);
		morph.setCapability(Morph.ALLOW_WEIGHTS_WRITE);
		Transform3D trm = new Transform3D();
		trm.setTranslation(new Vector3f(0.65f, 0.4f, 0.50f));
		trm.setScale(0.1);
		TransformGroup tgm = new TransformGroup(trm);
		tgm.addChild(morph);
		root.addChild(tgm);
		// Behavior node
		Alpha alpha2 = new Alpha(-1, Alpha.INCREASING_ENABLE, 0, 0, 2000, 0, 0, 2000, 0, 0);
		MorphingBehavior mb = new MorphingBehavior(morph, alpha2);
		BoundingSphere bounds2 = new BoundingSphere();
		mb.setSchedulingBounds(bounds2);
		root.addChild(mb);

		////////////////////////////////////////////////////////////////////////////
		// Billboard LOAD TEXTURE
		////////////////////////////////////////////////////////////////////////////

		// Transformation, 2 Rotationen:
		Transform3D billboardtr = new Transform3D();
		billboardtr.rotY(Math.PI / 2);

		// drehung.rotZ(Math.PI / 2);
		Transform3D billboardtr2 = new Transform3D();
		billboardtr2.rotX(Math.PI / -2);
		billboardtr2.setScale(0.08);

		// drehung2.setTranslation(new Vector3f(0.0f,0.4f, 0.65f));
		billboardtr.mul(billboardtr2);
		TransformGroup objBillboard = new TransformGroup(billboardtr);
		// Loader
		ObjectFile file = new ObjectFile(ObjectFile.RESIZE);
		Scene scene = null;
		try {
			scene = file.load(ClassLoader.getSystemResource("vaso.obj"));

		} catch (Exception e) {
		}
		objBillboard.addChild(scene.getSceneGroup());

		////////////////////////////////////////////////////////////////////////////
		// Billboard
		////////////////////////////////////////////////////////////////////////////

		TransformGroup bbTg = new TransformGroup();
		bbTg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		root.addChild(bbTg);

		Billboard bb = new Billboard(bbTg, Billboard.ROTATE_ABOUT_POINT, new Point3f(0.65f, 0.4f, 0.25f));
		bb.setSchedulingBounds(bounds);
		bbTg.addChild(bb);

		Transform3D tr12 = new Transform3D();
		tr12.setTranslation(new Vector3f(0.65f, 0.4f, 0.25f));
		TransformGroup tg12 = new TransformGroup(tr12);
		ColorCube imagePanel = new ColorCube(0.05f);

		tg12.addChild(objBillboard);
		bbTg.addChild(tg12);

		////////////////////////////////////////////////////////////////////////////
		// Shadow
		////////////////////////////////////////////////////////////////////////////

		TransformGroup spinball = new TransformGroup();
		spinball.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		root.addChild(spinball);
		// object
		Appearance apPlaneta = createTextureAppearance();
		Sphere sphere22 = new Sphere(0.10f, Primitive.GENERATE_TEXTURE_COORDS, 50, apPlaneta);

		Transform3D tr00 = new Transform3D();
		tr00.set(new Vector3f(0.65f, 0.42f, 0.0f));
		TransformGroup tg00 = new TransformGroup(tr00);
		tg00.addChild(sphere22);

		spinball.addChild(tg00);

		// rotator
		Alpha alpha22 = new Alpha(-1, 10000);
		RotationInterpolator rotator22 = new RotationInterpolator(alpha22, spinball);
		rotator22.setAxisOfRotation(tr00);
		rotator22.setSchedulingBounds(bounds);
		spinball.addChild(rotator22);

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

	// MORPH
	GeometryArray morph1() {
		IndexedTriangleArray iaa = new IndexedTriangleArray(6, GeometryArray.COORDINATES, 24);
		int phi = 1;
		Point3f[] coords = new Point3f[6];
		coords[0] = new Point3f(0, 1, 0);
		coords[1] = new Point3f(0.5f, 0, 0);
		coords[2] = new Point3f(0, 0, 0.5f);
		coords[3] = new Point3f(0, -1, 0);
		coords[4] = new Point3f(-0.5f, 0, 0);
		coords[5] = new Point3f(0, 0, -0.5f);

		iaa.setCoordinates(0, coords);
		int[] index = { 0, 2, 1, 0, 1, 5, 0, 5, 4, 0, 4, 2, 3, 1, 2, 3, 2, 4, 3, 4, 5, 3, 5, 1 };

		iaa.setCoordinateIndices(0, index);

		GeometryInfo geom = new GeometryInfo(iaa);
		NormalGenerator ng = new NormalGenerator();
		ng.generateNormals(geom);
		return geom.getGeometryArray();
	}

	GeometryArray morph2() {
		IndexedTriangleArray iaa = new IndexedTriangleArray(6, GeometryArray.COORDINATES, 24);
		int phi = 1;
		Point3f[] coords = new Point3f[6];
		coords[0] = new Point3f(0, 0.5f, 0);
		coords[1] = new Point3f(0.3f, 0, 0);
		coords[2] = new Point3f(0, 0, 0.3f);
		coords[3] = new Point3f(0, -0.5f, 0);
		coords[4] = new Point3f(-0.3f, 0, 0);
		coords[5] = new Point3f(0, 0, -0.3f);

		iaa.setCoordinates(0, coords);
		int[] index = { 0, 2, 1, 0, 1, 5, 0, 5, 4, 0, 4, 2, 3, 1, 2, 3, 2, 4, 3, 4, 5, 3, 5, 1 };

		iaa.setCoordinateIndices(0, index);

		GeometryInfo geom = new GeometryInfo(iaa);
		NormalGenerator ng = new NormalGenerator();
		ng.generateNormals(geom);
		return geom.getGeometryArray();
	}

	GeometryArray morph3() {
		IndexedTriangleArray iaa = new IndexedTriangleArray(6, GeometryArray.COORDINATES, 24);
		int phi = 1;
		Point3f[] coords = new Point3f[6];
		coords[0] = new Point3f(0, 0.5f, 0);
		coords[1] = new Point3f(0.2f, 0, 0);
		coords[2] = new Point3f(0, 0, 0.2f);
		coords[3] = new Point3f(0, -0.5f, 0);
		coords[4] = new Point3f(-0.2f, 0, 0);
		coords[5] = new Point3f(0, 0, -0.2f);

		iaa.setCoordinates(0, coords);
		int[] index = { 0, 2, 1, 0, 1, 5, 0, 5, 4, 0, 4, 2, 3, 1, 2, 3, 2, 4, 3, 4, 5, 3, 5, 1 };

		iaa.setCoordinateIndices(0, index);

		GeometryInfo geom = new GeometryInfo(iaa);
		NormalGenerator ng = new NormalGenerator();
		ng.generateNormals(geom);
		return geom.getGeometryArray();
	}

	GeometryArray morph4() {
		IndexedTriangleArray iaa = new IndexedTriangleArray(6, GeometryArray.COORDINATES, 24);
		int phi = 1;
		Point3f[] coords = new Point3f[6];
		coords[0] = new Point3f(0, 1, 0);
		coords[1] = new Point3f(0.5f, 0, 0);
		coords[2] = new Point3f(0, 0, 0.5f);
		coords[3] = new Point3f(0, -1, 0);
		coords[4] = new Point3f(-0.5f, 0, 0);
		coords[5] = new Point3f(0, 0, -0.5f);

		iaa.setCoordinates(0, coords);
		int[] index = { 0, 2, 1, 0, 1, 5, 0, 5, 4, 0, 4, 2, 3, 1, 2, 3, 2, 4, 3, 4, 5, 3, 5, 1 };

		iaa.setCoordinateIndices(0, index);

		GeometryInfo geom = new GeometryInfo(iaa);
		NormalGenerator ng = new NormalGenerator();
		ng.generateNormals(geom);
		return geom.getGeometryArray();
	}

	public BufferedImage capture() {
		// render off screen image
		Dimension dim = cv1.getSize();
		view.stopView();
		view.addCanvas3D(offScreenCanvas);
		BufferedImage bImage = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_RGB);
		ImageComponent2D buffer = new ImageComponent2D(ImageComponent.FORMAT_RGB, bImage);
		offScreenCanvas.setOffScreenBuffer(buffer);
		view.startView();
		offScreenCanvas.renderOffScreenBuffer();
		offScreenCanvas.waitForOffScreenRendering();
		bImage = offScreenCanvas.getOffScreenBuffer().getImage();
		view.removeCanvas3D(offScreenCanvas);
		return bImage;
	}

	public void save(BufferedImage bImage) {
		// save image to file
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("."));
		if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			File oFile = chooser.getSelectedFile();
			try {
				ImageIO.write(bImage, "jpeg", oFile);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	// SHADOW
	Appearance createTextureAppearance() {
		Appearance ap = new Appearance();
		URL filename = getClass().getClassLoader().getResource("images/ball.jpg");
		TextureLoader loader = new TextureLoader(filename, this);
		ImageComponent2D image = loader.getImage();
		if (image == null) {
			System.out.println("can't find texture file.");
		}

		Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA, image.getWidth(), image.getHeight());
		texture.setImage(0, image);
		texture.setEnable(true);
		texture.setMagFilter(Texture.BASE_LEVEL_LINEAR);
		texture.setMinFilter(Texture.BASE_LEVEL_LINEAR);
		ap.setTexture(texture);
		return ap;
	}

	// LOD

	void loadImages() {
		URL filename = getClass().getClassLoader().getResource("images/ball.jpg");
		try {
			images[0] = ImageIO.read(filename);
			AffineTransform xform = AffineTransform.getScaleInstance(0.5, 0.5);
			AffineTransformOp scaleOp = new AffineTransformOp(xform, null);
			for (int i = 1; i < 3; i++) {
				images[i] = scaleOp.filter(images[i - 1], null);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	Appearance createAppearance(int i) {
		Appearance appear = new Appearance();
		ImageComponent2D image = new ImageComponent2D(ImageComponent2D.FORMAT_RGB, images[i]);
		Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA, image.getWidth(), image.getHeight());
		texture.setImage(0, image);
		texture.setEnable(true);
		texture.setMagFilter(Texture.BASE_LEVEL_LINEAR);
		texture.setMinFilter(Texture.BASE_LEVEL_LINEAR);
		appear.setTexture(texture);
		return appear;
	}

	Appearance createTextureAppearanceMirror() {
		Appearance apMirror = new Appearance();
	    URL filename = 
	            getClass().getClassLoader().getResource("images/mirror.jpg");
	        TextureLoader loader = new TextureLoader(filename, this);
	        ImageComponent2D image1 = loader.getImage();


	        TextureCubeMap texture = new TextureCubeMap(Texture.BASE_LEVEL, Texture.RGBA,
	        	    image1.getWidth());
	        	    texture.setImage(0, TextureCubeMap.NEGATIVE_X, image1);
	        	    texture.setImage(0, TextureCubeMap.NEGATIVE_Y, image1);
	        	    texture.setImage(0, TextureCubeMap.NEGATIVE_Z, image1);
	        	    texture.setImage(0, TextureCubeMap.POSITIVE_X, image1);
	        	    texture.setImage(0, TextureCubeMap.POSITIVE_Y, image1);
	        	    texture.setImage(0, TextureCubeMap.POSITIVE_Z, image1);

		texture.setEnable(true);
		texture.setMagFilter(Texture.BASE_LEVEL_LINEAR);
		texture.setMinFilter(Texture.BASE_LEVEL_LINEAR);
		apMirror.setTexture(texture);

		TexCoordGeneration tcg = new TexCoordGeneration(TexCoordGeneration.OBJECT_LINEAR,
				TexCoordGeneration.TEXTURE_COORDINATE_3);
		tcg.setPlaneR(new Vector4f(1, 0, 0, 0));
		tcg.setPlaneS(new Vector4f(0, 1, 0, 0));
		tcg.setPlaneT(new Vector4f(0, 0, 1, 0));
		apMirror.setTexCoordGeneration(tcg);
		apMirror.setCapability(Appearance.ALLOW_TEXGEN_WRITE);
		return apMirror;
	}
	
	 private Geometry createGeometry() {
		    int m = 40;
		    int n = 40;
		    Point3f[] pts = new Point3f[m*n];
		    int idx = 0;
		    for (int i = 0; i < m; i++) {
		      for (int j = 0; j < n; j++) {
		        float x = (i - m/2)*0.1f;
		        float z = (j - n/2)*0.2f;
		        float y = 2.3f * (float)(Math.cos(x*x)*z)/
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
