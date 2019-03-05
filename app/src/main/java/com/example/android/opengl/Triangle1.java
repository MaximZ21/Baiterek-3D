/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.opengl;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * A two-dimensional triangle for use as a drawn object in OpenGL ES 2.0.
 */
public class Triangle1 {

    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    // The matrix must be included as a modifier of gl_Position.
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private final FloatBuffer vertexBuffer;
    private final int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static float triangleCoords[];


    private int vertexCount = 0;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    float color[] = { 1f, 0.84313725f, 0f, 0.0f };

    /**
     * Sets up the drawing object data for use in an OpenGL ES context.
     */
    public Triangle1() {
        float ShiftY = 0.7f;
        double DEG = Math.PI/180;
        double mRaduis = 0.35f;
        double mStep = 15;
        double dTheta = mStep * DEG;
        double dPhi = dTheta;
        int points = 0;
        FloatBuffer sphereVertex;
        sphereVertex = FloatBuffer.allocate(40000);
        double phi;
        phi = -(Math.PI);
        for( phi = phi; phi <= Math.PI; phi+=dPhi) {
            System.out.println(points);
            //for each stage calculating the slices
            for (double theta = 0.0; theta <= (Math.PI * 2); theta += dTheta) {

                sphereVertex.put((float) (mRaduis * Math.sin(phi) * Math.cos(theta)));//1
                sphereVertex.put((float) (mRaduis * Math.cos(phi))+ShiftY);//3
                sphereVertex.put((float) (mRaduis * Math.sin(phi) * Math.sin(theta)));//2
                points++;
                sphereVertex.put((float) (mRaduis * Math.sin(phi) * Math.cos(theta + 0.02)));//1
                sphereVertex.put((float) (mRaduis * Math.cos(phi))+ShiftY);//3
                sphereVertex.put((float) (mRaduis * Math.sin(phi) * Math.sin(theta + 0.02)));//2
                points++;

                sphereVertex.put((float) (mRaduis * Math.sin(phi + 0.4) * Math.cos(theta)));//1
                sphereVertex.put((float) (mRaduis * Math.cos(phi + 0.4))+ShiftY);//3
                sphereVertex.put((float) (mRaduis * Math.sin(phi + 0.4) * Math.sin(theta)));//2

                points++;
            }
        }

        phi = -(Math.PI);
        for( phi = phi; phi <= Math.PI; phi+=dPhi) {
            System.out.println(points);
            //for each stage calculating the slices
            for (double theta = 0.0; theta <= (Math.PI * 2); theta += dTheta) {
                sphereVertex.put((float) (mRaduis * Math.sin(phi) * Math.cos(theta)));//1
                sphereVertex.put((float) (mRaduis * Math.cos(phi))+ShiftY);//3
                sphereVertex.put((float) (mRaduis * Math.sin(phi) * Math.sin(theta)));//2
                points++;
                sphereVertex.put((float) (mRaduis * Math.sin(phi+0.02) * Math.cos(theta)));//1
                sphereVertex.put((float) (mRaduis * Math.cos(phi+0.02))+ShiftY);//3
                sphereVertex.put((float) (mRaduis * Math.sin(phi+0.02) * Math.sin(theta)));//2
                points++;
                sphereVertex.put((float) (mRaduis * Math.sin(phi) * Math.cos(theta+0.4)));//1
                sphereVertex.put((float) (mRaduis * Math.cos(phi))+ShiftY);//3
                sphereVertex.put((float) (mRaduis * Math.sin(phi) * Math.sin(theta+0.4)));//2

                points++;
            }
        }






//        phi = -(Math.PI);
//        for( phi = phi; phi <= Math.PI; phi+=dPhi) {
//            //for each stage calculating the slices
//            for(double theta = 0.0; theta <= (Math.PI*2); theta+=dTheta) {
//                sphereVertex.put((float) (mRaduis * Math.cos(phi)) );
//                sphereVertex.put((float) (mRaduis * Math.sin(phi) * Math.cos(theta)) );
//                sphereVertex.put((float) (mRaduis * Math.sin(phi) * Math.sin(theta)) );
//
//
//                points++;
//
//            }
//        }
        int var = 37;
        vertexCount = points;
        sphereVertex.position(0);
        triangleCoords = sphereVertex.array();
        System.out.println(Math.PI*2/dTheta);
//        for(int i = 0 ; i < points*3; i++){
//            float tmp = triangleCoords[i];
//            float tmp2 = triangleCoords[i+1];
//            float tmp3 = triangleCoords[i+2];
//            triangleCoords[i] = triangleCoords[i+var];
//            triangleCoords[i+1] = triangleCoords[i+1+var];
//            triangleCoords[i+2] = triangleCoords[i+2+var];
//            triangleCoords[i+var] = tmp;
//            triangleCoords[i+1+var] = tmp2;
//            triangleCoords[i+2+var] = tmp3;
//            i+=9;
//        }
        System.out.println(points);


        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                triangleCoords.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(triangleCoords);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);

        // prepare shaders and OpenGL program
        int vertexShader = MyGLRenderer.loadShader(
                GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(
                GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables

    }

    /**
     * Encapsulates the OpenGL ES instructions for drawing this shape.
     *
     * @param mvpMatrix - The Model View Project matrix in which to draw
     * this shape.
     */
    public void draw(float[] mvpMatrix) {
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(
                mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
       mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

//        int m_color = GLES20 . glGetAttribLocation ( mProgram , " myVertexColor ");
//        GLES20 . glEnableVertexAttribArray ( m_color );
//        vertexBuffer . position (3);
//        GLES20 . glVertexAttribPointer (
//                m_color , 4,
//                GLES20 . GL_FLOAT , false ,
//                vertexStride , vertexBuffer );
//        vertexBuffer . position (0);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        MyGLRenderer.checkGlError("glGetUniformLocation");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        MyGLRenderer.checkGlError("glUniformMatrix4fv");

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

}
