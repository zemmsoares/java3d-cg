package main;

import java.awt.event.KeyEvent;
import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupOnAWTEvent;
import javax.vecmath.Vector3f;

public class Animation extends Behavior {
	private TransformGroup objRotate;
	private Transform3D rotation = new Transform3D();
	private double angle = 0.0;

	public Animation(TransformGroup objRotate) {
		this.objRotate = objRotate;
	}


	// initialize the Behavior
	// set initial wakeup condition
	// called when behavior becomes live
	public void initialize() {
		this.wakeupOn(new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED));
	}

	//called by Java 3D when appropriate stimulus occurs
	public void processStimulus(Enumeration criteria) {
	//do what is necessary in response to stimulus
		angle += 0.1;
		rotation.rotY(angle);

		objRotate.setTransform(rotation);
		this.wakeupOn(new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED));
	}

} // end of class SimpleBehavior