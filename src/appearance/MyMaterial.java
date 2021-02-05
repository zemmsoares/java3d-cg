package appearance;

import javax.media.j3d.Material;

public class MyMaterial extends Material {
	public final static int BRASS = 0;
	public final static int BRONZE = 1;
	public final static int PLASTIC = 2;
	public final static int ORANGE = 3;

	public MyMaterial(int type) {
		switch (type) {
		case BRASS:
			this.setAmbientColor(0.329412f, 0.223529f, 0.027451f);
			this.setDiffuseColor(0.790392f, 0.568627f, 0.113725f);
			this.setSpecularColor(0.992157f, 0.941176f, 0.807843f);
			this.setShininess(27.8974f);
			break;
		case BRONZE:
			this.setAmbientColor(0.2125f, 0.1275f, 0.054f);
			this.setDiffuseColor(0.714f, 0.4284f, 0.18144f);
			this.setSpecularColor(0.393548f, 0.271906f, 0.166721f);
			this.setShininess(25.6f);
			break;
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
			
		}
	}

}
