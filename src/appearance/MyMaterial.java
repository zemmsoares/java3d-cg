package appearance;

import javax.media.j3d.Material;

public class MyMaterial extends Material {
	public final static int PLASTIC = 0;
	public final static int ORANGE = 1;
	public final static int WHITE = 2;
	public final static int WALL = 3;

	public MyMaterial(int type) {
		switch (type) {
		case PLASTIC:
			this.setAmbientColor(0.0f, 0.0f, 0.0f);
			this.setDiffuseColor(0.0f, 0.0f, 0.0f);
			this.setSpecularColor(0.0f, 0.0f, 0.0f);
			this.setShininess(25.6f);
			break;
		case ORANGE:
			this.setAmbientColor(0.255f, 0.127f, 0.8f);
			this.setDiffuseColor(0.255f, 0.127f, 0.8f);
			this.setSpecularColor(0.255f, 0.127f, 0.8f);
			this.setShininess(25.6f);
			break;
		case WHITE:
			this.setAmbientColor(255f,255f,255f);
			this.setDiffuseColor(255f,255f,255f);
			this.setSpecularColor(255f,255f,255f);
			this.setShininess(25.6f);
			break;
		case WALL:
			this.setAmbientColor(0.255f, 0.127f, 0.8f);
			this.setDiffuseColor(0.255f, 0.127f, 0.8f);
			this.setSpecularColor(0.255f, 0.127f, 0.8f);
			this.setShininess(25.6f);
			break;
			
		}
	}

}
