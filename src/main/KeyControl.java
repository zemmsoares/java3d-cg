package main;

import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.Node;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupCondition;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnAWTEvent;
import javax.media.j3d.WakeupOnCollisionEntry;
import javax.media.j3d.WakeupOnCollisionExit;
import javax.media.j3d.WakeupOr;
import javax.vecmath.Vector3f;

public class KeyControl extends Behavior {
	private TransformGroup moveTg = null;
	private Node node = null;
	private boolean collision = false;

	private WakeupCondition wakeupCondition = null;

	private int lastKey = 0;

	public KeyControl(TransformGroup moveTg, Node node) {
		// The constructor is used to pass to the behavior the objects that it needs.
		this.moveTg = moveTg;
		this.node = node;
	}

	@Override
	public void initialize() {
		WakeupCriterion[] keyEvents = new WakeupCriterion[4];
		keyEvents[0] = new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED);
		keyEvents[1] = new WakeupOnAWTEvent(KeyEvent.KEY_RELEASED);
		keyEvents[2] = new WakeupOnCollisionEntry(node, WakeupOnCollisionEntry.USE_GEOMETRY);
		keyEvents[3] = new WakeupOnCollisionExit(node, WakeupOnCollisionExit.USE_GEOMETRY);

		// The wakeup condition is a combination of 4 wakeup criterion
		wakeupCondition = new WakeupOr(keyEvents);
		wakeupOn(wakeupCondition);
	}

	@Override
	public void processStimulus(Enumeration criteria) {
		WakeupCriterion wakeupCriterion;
		AWTEvent[] events;

		while (criteria.hasMoreElements()) {
			// Get each wakeup criterion
			wakeupCriterion = (WakeupCriterion) criteria.nextElement();

			// Find its type and process it
			if (wakeupCriterion instanceof WakeupOnAWTEvent) {
				events = ((WakeupOnAWTEvent) wakeupCriterion).getAWTEvent();

				for (int i = 0; i < events.length; i++) {
					if (events[i].getID() == KeyEvent.KEY_PRESSED) {
						keyPressed((KeyEvent) events[i]);
					} else if (events[i].getID() == KeyEvent.KEY_RELEASED) {
						// not implementes in this example
					}

				}
			} else if (wakeupCriterion instanceof WakeupOnCollisionEntry) {
				collision = true;
				System.out.println("WakeupOnCollisionEntry");
			} else if (wakeupCriterion instanceof WakeupOnCollisionExit) {
				collision = false;
				System.out.println("WakeupOnCollisionExit");
			}
		}

		wakeupOn(wakeupCondition);
	}

	private void keyPressed(KeyEvent event) {
		int keyCode = event.getKeyCode();

		switch (keyCode) {

		case KeyEvent.VK_HOME:
			if (!collision || (collision && lastKey != KeyEvent.VK_HOME))
				// if (!collision)
				doRotationY(Math.toRadians(1.0));
			break;
		case KeyEvent.VK_INSERT:
			if (!collision || (collision && lastKey != KeyEvent.VK_INSERT))
				// if (!collision)
				doRotationY(Math.toRadians(-1.0));
			break;
		case KeyEvent.VK_DELETE:
			if (!collision || (collision && lastKey != KeyEvent.VK_DELETE))
				// if (!collision)
				doTranslation(new Vector3f(0f, 0f, -0.01f));
			break;
		case KeyEvent.VK_END:
			if (!collision || (collision && lastKey != KeyEvent.VK_END))
				// if (!collision)
				doTranslation(new Vector3f(0f, 0f, 0.01f));
			break;
		}
		lastKey = keyCode;
	}

	private void doRotationY(double t) {
		// Standard code to add a transformation to the actual transformation of a
		// TransformGroup
		
		// Create the new transformation to add
		Transform3D newTr = new Transform3D();
		newTr.rotY(t);

		// Get old transformation of the TransformGroup
		Transform3D oldTr = new Transform3D();
		moveTg.getTransform(oldTr);

		// Add the new transformation by multiplying the transformations
		oldTr.mul(newTr);

		// Set the new transformation
		moveTg.setTransform(oldTr);
	}

	private void doTranslation(Vector3f v) {
		Transform3D newTr = new Transform3D();
		newTr.setTranslation(v);

		Transform3D oldTr = new Transform3D();
		moveTg.getTransform(oldTr);

		oldTr.mul(newTr);

		moveTg.setTransform(oldTr);
	}
}
