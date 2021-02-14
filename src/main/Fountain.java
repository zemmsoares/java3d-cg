package main;

/*
 *      @(#)ParticleApp.java 1.0 01/06/11
 *
 * Copyright (c) 1996-2001 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 */

/*
 * Getting Started with the Java 3D API
 * written in Java 3D
 *
 * ParticleApp.java demonstrates changing BY_REFERENCE geometry.
 */

import java.applet.Applet;
import java.util.Enumeration;
import java.util.Random;

import javax.media.j3d.Appearance;
import javax.media.j3d.Behavior;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Geometry;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.GeometryUpdater;
import javax.media.j3d.IndexedQuadArray;
import javax.media.j3d.LineArray;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TriangleFanArray;
import javax.media.j3d.WakeupOnElapsedFrames;

import main.Fountain.UpdateWaterBehavior;
import main.Fountain.WaterUpdater;

public class Fountain extends BranchGroup{

    protected GeometryUpdater geometryUpdater = new WaterUpdater();
    protected float baseElevation = -0.45f;
    protected LineArray waterLines = null;


    class UpdateWaterBehavior extends Behavior{

      WakeupOnElapsedFrames w = null;

      public UpdateWaterBehavior(){
        w = new WakeupOnElapsedFrames(0);
      }

      public void initialize(){
        wakeupOn(w);
      }

      public void processStimulus(Enumeration critiria){
        waterLines.updateData(geometryUpdater);
        wakeupOn(w);
      } // end processStimulus

    } // end class UpdateWaterBehavior

    public class WaterUpdater implements GeometryUpdater{

      Random random;
      GeometryArray geometryArray;
      float[] vertices;
      int N, i;

      public WaterUpdater(){
        random = new Random();
      }

      public void updateData(Geometry geometry){

        geometryArray = (GeometryArray)geometry;
        vertices = geometryArray.getCoordRefFloat();
        N = geometryArray.getValidVertexCount();

        for(i = 0; i < N; i+=2){   
          if(vertices[i*3+1] > baseElevation){  // if y = 0
              vertices[i*3+0] += vertices[i*3+0] - vertices[i*3+3];      //x1=2x1-x2
              vertices[i*3+1] += vertices[i*3+1] - vertices[i*3+4]-0.01f; //y1=2y1-y2-c
              vertices[i*3+2] += vertices[i*3+2] - vertices[i*3+5];      //z1=2z1-z2
              vertices[i*3+3] = (vertices[i*3+0]+vertices[i*3+3])/2;   //x2 = old x1
              vertices[i*3+4] = (vertices[i*3+1]+vertices[i*3+4]+0.01f)/2;//y2 = old y2
              vertices[i*3+5] = (vertices[i*3+2]+vertices[i*3+5])/2;   //z2 = old z1
              if(vertices[i*3+1] < baseElevation){  // if y < 0
                vertices[i*3+0] = 0.0f; //x1
                vertices[i*3+1] = baseElevation; //y1
                vertices[i*3+2] = 0.0f; //z1
                vertices[i*3+3] = 0.0f; //x2
                vertices[i*3+4] = baseElevation; //y2
                vertices[i*3+5] = 0.0f; //z2
              }
          } else { // randomly start a drop
             if(random.nextFloat() > 0.8){
               vertices[i*3+0] = 0.03f*(random.nextFloat()-0.5f); //x1
               vertices[i*3+1] = 0.14f*random.nextFloat()+baseElevation; //y1
               vertices[i*3+2] = 0.03f*(random.nextFloat()-0.5f); //z1
            } // end if
          } // end if-else
        } // end for loop
      } // end updateData(Geometry)
    } // end of class WaterUpdater

    public Behavior getWaterBehavior(){
      return new UpdateWaterBehavior();
    }

    public GeometryUpdater getWaterUpdater(){
       return geometryUpdater;
    }

////////////////////////////////////////////
//
    // create fountain subgraph
//
    public Fountain() {

        Appearance waterAppear;

        waterAppear = createWaterAppearance();
        this.addChild(new Shape3D(createWaterGeometry(),waterAppear));
        //this.addChild(new Shape3D(createFountainBaseGeometry(),waterAppear));

        GeometryUpdater geometryUpdater = new WaterUpdater();
    } // end of Fountain constructor 

    Geometry createFountainGeometry(){

        // cross section of wall of fountatin geometry
        //               ____
        //   x2,y2,z2-->|    |<--x3,y3,z3
        //              |    |
        //   x1,y1,z1-->|    |<--x4,y4,z4.....center of fountain (y=0)
        //

        int N = 40;                   // number of perimeter verts
        final int I = N*4*3;
        float[] coord = new float[N*3];
        int[] indices = new int[N*4];
        IndexedQuadArray fountainQuads = null;

        fountainQuads = new IndexedQuadArray(N*4,
          IndexedQuadArray.COORDINATES, I );

        double a;
        int v;
        for(v = 0; v < N; v++){
            a=v*2.0*Math.PI/(N-2);
            coord[v*3+0] = (float)(0.7*Math.sin(a)); //x1
            coord[v*3+1] = baseElevation;            //y1
            coord[v*3+2] = (float)(0.7*Math.cos(a)); //z1
        }
        fountainQuads.setCoordinates(0,coord);

        for(v = 0; v < N; v++){
            //x2 = x1
            coord[v*3+1] += 0.1f; //y2
            //z2 = z1
        }
        fountainQuads.setCoordinates(N,coord);

        for(v = 0; v < N; v++){
            coord[v*3]   *= 0.9f;  //x3
            //y3 = y2
            coord[v*3+2] *= 0.9f;  //z3
        }
        fountainQuads.setCoordinates(2*N,coord);

        for(v = 0; v < N; v++){
            //x4 = x3
            coord[v*3+1] = baseElevation;    //y4 = y1
            //z4 = z3
        }
        fountainQuads.setCoordinates(3*N,coord);

        for(v = 0; v < N-1; v++){
            indices[v*4]   = v;
            indices[v*4+1] = (v+1);
            indices[v*4+2] = (v+1)+N;
            indices[v*4+3] = v+N;
    }

        v = N-1;
        indices[v*4]   = v;
        indices[v*4+1] = 0;
        indices[v*4+2] = N;
        indices[v*4+3] = v+N;

        fountainQuads.setCoordinateIndices(0,indices);

        int i;
        for(i = 1; i < 3; i++){
          for(v = 0; v < 4*N-1; v++) indices[v]+=N;
          fountainQuads.setCoordinateIndices(i*N*4,indices);
        }

        return fountainQuads;

    } // end of createFountainGeometry
    	    
  

    Geometry createWaterGeometry(){

        int N = 500;                   // number of 'drops'

        waterLines = new LineArray(N*2,
          LineArray.COORDINATES | LineArray.BY_REFERENCE);

        waterLines.setCapability(GeometryArray.ALLOW_REF_DATA_READ);
        waterLines.setCapability(GeometryArray.ALLOW_REF_DATA_WRITE);
        waterLines.setCapability(GeometryArray.ALLOW_COUNT_READ);

        float[] coordinates = new float[N*3*2];

        int p;
        for(p = 0; p < N; p+=2){        // for each particle
              coordinates[p*3+0] = 0.0f;
              coordinates[p*3+1] = baseElevation;
              coordinates[p*3+2] = 0.0f;
              coordinates[p*3+3] = 0.0f;
              coordinates[p*3+4] = baseElevation;
              coordinates[p*3+5] = 0.0f;
        }
        waterLines.setCoordRefFloat(coordinates);
        // the following statements would be redundant
        // waterLines.setInitialCoordIndex(0);
        // waterLines.setValidVertexCount(N*2);

        return waterLines;

    } // end of createWaterGeometry

    // create Appearance for Fountain
    //
    // this method creates the default Appearance for the
    // Fountain 



    Appearance createWaterAppearance(){

        Appearance fountainAppear = new Appearance();
        fountainAppear.setColoringAttributes(
             new ColoringAttributes(0.255f, 0.127f, 0.8f,ColoringAttributes.SHADE_FLAT));

        LineAttributes lineAttributes = new LineAttributes();
        lineAttributes.setLineAntialiasingEnable(true);

        // fountainAppear.setLineAttributes(lineAttributes);

        return fountainAppear;
    }

} // end of class Fountain
