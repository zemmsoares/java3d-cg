package shapes;

import javax.media.j3d.GeometryArray;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.vecmath.Point2f;
import javax.vecmath.Point3f;

public class Wall {
	public Shape3D makeGround(Point3f rightUp, Point3f rightDown, Point3f leftDown, Point3f leftUp){
        Point3f[]  coords = new Point3f[8];
        for(int i = 0; i< 8; i++)
            coords[i] = new Point3f();

        Point2f[]  tex_coords = new Point2f[8];
        for(int i = 0; i< 8; i++)
            tex_coords[i] = new Point2f();

        coords[0] = rightUp;
        coords[1] = rightDown;
        coords[2] = leftDown;
        coords[3] = leftUp;

        coords[4] = rightDown;
        coords[5] = rightUp;
        coords[6] = leftUp;
        coords[7] = leftDown;

        tex_coords[0].x = 0.0f;
        tex_coords[0].y = 0.0f;

        tex_coords[1].x = 10.0f;
        tex_coords[1].y = 0.0f;

        tex_coords[2].x = 0.0f;
        tex_coords[2].y = 10.0f;

        tex_coords[3].x = 10.0f;
        tex_coords[3].y = 10.0f;

        tex_coords[4].x = 0.0f;
        tex_coords[4].y = 0.0f;

        tex_coords[5].x = 10.0f;
        tex_coords[5].y = 0.0f;

        tex_coords[6].x = 0.0f;
        tex_coords[6].y = -10.0f;

        tex_coords[7].x = 10.0f;
        tex_coords[7].y = -10.0f;

        QuadArray qa_ground = new QuadArray(8, GeometryArray.COORDINATES|
                GeometryArray.TEXTURE_COORDINATE_2);
        qa_ground.setCoordinates(0,coords);

        qa_ground.setTextureCoordinates(0, tex_coords);

        Shape3D ground = new Shape3D(qa_ground);

        return ground;
}
}