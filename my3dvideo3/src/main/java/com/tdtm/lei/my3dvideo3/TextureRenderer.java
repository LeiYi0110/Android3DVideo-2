package com.tdtm.lei.my3dvideo3;

/**
 * Created by lei on 6/12/16.
 */
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class TextureRenderer {

    private int mProgram;
    private int mTexSamplerHandle;
    private int mTexCoordHandle;
    private int mPosCoordHandle;

    private FloatBuffer mTexVertices;
    private FloatBuffer mPosVertices;

    private int mViewWidth;
    private int mViewHeight;

    private int mTexWidth;
    private int mTexHeight;

    private static final String VERTEX_SHADER =
            "attribute vec4 a_position;\n" +
                    "attribute vec2 a_texcoord;\n" +
                    "varying vec2 v_texcoord;\n" +
                    "void main() {\n" +
                    "  gl_Position = a_position;\n" +
                    "  v_texcoord = a_texcoord;\n" +
                    "}\n";

    private static final String FRAGMENT_SHADER =
            "precision mediump float;\n" +
                    "uniform sampler2D tex_sampler;\n" +
                    "varying vec2 v_texcoord;\n" +
                    "void main() {\n" +
                    "  gl_FragColor = texture2D(tex_sampler, v_texcoord);\n" +
                    "}\n";


    /*
    private static final float[] TEX_VERTICES = {
        0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f
    };
    */


    /*
    private  float[] TEX_VERTICES = {
            0.0f, 1.0f, 0.5f, 1.0f, 0.0f, 0.0f, 0.5f, 0.0f
    };
    */

    private  float[] TEX_VERTICES;

    /*
    private static final float[] POS_VERTICES = {
        -1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f
    };
    */

    private float[] POS_VERTICES;

    //private static final int FLOAT_SIZE_BYTES = 4;
    private static final int FLOAT_SIZE_BYTES = 4;

    private  int partCount = 1920*2;

    private void initArray()
    {


        //partCount = 8;//1920*2;//1000000;
        //static  SceneVertex vertices[1920*2*4];
        TEX_VERTICES = new float[partCount*8];
        POS_VERTICES = new float[partCount*8];
        //SceneVertex *p = vertices;

        float vertexInterval = 4.0f/partCount;
        //NSLog(@"%f",vertexInterval);
        float startVertexPos = -1.0f;


        float textureInterval = 1.0f/partCount;
        //NSLog(@"%f",textureInterval);
        float startTexturePos = 0.0f;


        for(int i=0; i < partCount/2; i++)
        {
            //for(int j = 0; j < 8; j++)
            int j = 8*i;
            POS_VERTICES[j] = startVertexPos + i*vertexInterval ;

            POS_VERTICES[j + 1] = -1.0f;
            //p++;
            POS_VERTICES[j + 2] = startVertexPos + i*vertexInterval;
            //p++;
            POS_VERTICES[j + 3]  = 1.0f;
            //p++;

            POS_VERTICES[j + 4] = startVertexPos + (i + 1)*vertexInterval ;
            //p++;
            POS_VERTICES[j + 5] = -1.0f;
            //p++;
            POS_VERTICES[j + 6] = startVertexPos + (i + 1)*vertexInterval;
            //p++;
            POS_VERTICES[j + 7] = 1.0f;//normalizedSamplingSize.height;
            //p++;
        }
        float offset = 35*vertexInterval;//self.offsetNum*vertexInterval;//35*vertexInterval;//27*vertexInterval;//21*vertexInterval;

        for(int i=0; i < partCount/2; i++)
        {
            int j = partCount/2*8 + 8*i;
            POS_VERTICES[j] = startVertexPos + i*vertexInterval + offset;

            POS_VERTICES[j + 1] = -1.0f;
            //p++;
            POS_VERTICES[j + 2] = startVertexPos + i*vertexInterval + offset;
            //p++;
            POS_VERTICES[j + 3]  = 1.0f;
            //p++;

            POS_VERTICES[j + 4] = startVertexPos + (i + 1)*vertexInterval + offset;
            //p++;
            POS_VERTICES[j + 5] = -1.0f;
            //p++;
            POS_VERTICES[j + 6] = startVertexPos + (i + 1)*vertexInterval + offset;
            //p++;
            POS_VERTICES[j + 7] = 1.0f;

        }

        for(int i = 0; i < partCount/2; i++)
        {
            int j = 8*i;
            TEX_VERTICES[j] = startTexturePos + i*textureInterval;
            //q++;
            TEX_VERTICES[j + 1] = 1.0f;
            //q++;
            TEX_VERTICES[j + 2] = startTexturePos + i*textureInterval;
            //q++;
            TEX_VERTICES[j + 3] = 0.0f;
            //q++;

            TEX_VERTICES[j + 4] = startTexturePos + (i + 1)*textureInterval;
            //q++;
            TEX_VERTICES[j + 5] = 1.0f;
            //q++;
            TEX_VERTICES[j + 6] = startTexturePos + (i + 1)*textureInterval;
            //q++;
            TEX_VERTICES[j + 7] = 0.0f;
            //q++;
        }

        startTexturePos = 0.5f;
        for(int i = 0; i < partCount/2; i++)
        {
            int j = partCount/2*8 + 8*i;
            TEX_VERTICES[j] = startTexturePos + (i + 1)*textureInterval;
            //q++;
            TEX_VERTICES[j + 1] = 1.0f;
            //q++;
            TEX_VERTICES[j + 2] = startTexturePos + (i + 1)*textureInterval;
            //q++;
            TEX_VERTICES[j + 3] = 0.0f;
            //q++;

            TEX_VERTICES[j + 4] = startTexturePos + (i + 2)*textureInterval;
            //q++;
            TEX_VERTICES[j + 5] = 1.0f;
            //q++;
            TEX_VERTICES[j + 6] = startTexturePos + (i + 2)*textureInterval;
            //q++;
            TEX_VERTICES[j + 7] = 0.0f;
        }









    }

    public void init() {

        initArray();
        // Create program
        mProgram = GLToolbox.createProgram(VERTEX_SHADER, FRAGMENT_SHADER);

        // Bind attributes and uniforms
        mTexSamplerHandle = GLES20.glGetUniformLocation(mProgram,
                "tex_sampler");
        mTexCoordHandle = GLES20.glGetAttribLocation(mProgram, "a_texcoord");
        mPosCoordHandle = GLES20.glGetAttribLocation(mProgram, "a_position");

        // Setup coordinate buffers
        mTexVertices = ByteBuffer.allocateDirect(
                TEX_VERTICES.length * FLOAT_SIZE_BYTES)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTexVertices.put(TEX_VERTICES).position(0);
        mPosVertices = ByteBuffer.allocateDirect(
                POS_VERTICES.length * FLOAT_SIZE_BYTES)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mPosVertices.put(POS_VERTICES).position(0);
    }

    public void tearDown() {
        GLES20.glDeleteProgram(mProgram);
    }

    public void updateTextureSize(int texWidth, int texHeight) {
        mTexWidth = texWidth;
        mTexHeight = texHeight;
        //computeOutputVertices();
    }

    public void updateViewSize(int viewWidth, int viewHeight) {
        mViewWidth = viewWidth;
        mViewHeight = viewHeight;
        //computeOutputVertices();
    }

    public void renderTexture(int texId) {
        // Bind default FBO
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

        // Use our shader program
        GLES20.glUseProgram(mProgram);
        GLToolbox.checkGlError("glUseProgram");

        // Set viewport
        GLES20.glViewport(0, 0, mViewWidth, mViewHeight);
        GLToolbox.checkGlError("glViewport");

        // Disable blending
        GLES20.glDisable(GLES20.GL_BLEND);

        // Set the vertex attributes
        GLES20.glVertexAttribPointer(mTexCoordHandle, 2, GLES20.GL_FLOAT, false,
                0, mTexVertices);
        GLES20.glEnableVertexAttribArray(mTexCoordHandle);
        GLES20.glVertexAttribPointer(mPosCoordHandle, 2, GLES20.GL_FLOAT, false,
                0, mPosVertices);
        GLES20.glEnableVertexAttribArray(mPosCoordHandle);
        GLToolbox.checkGlError("vertex attribute setup");

        // Set the input texture
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLToolbox.checkGlError("glActiveTexture");
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
        GLToolbox.checkGlError("glBindTexture");
        GLES20.glUniform1i(mTexSamplerHandle, 0);

        // Draw
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        //GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        for (int i = 0; i < partCount; i = i + 2) {
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 4*i, 4);
        }
    }

    private void computeOutputVertices() {
        if (mPosVertices != null) {
            float imgAspectRatio = mTexWidth / (float)mTexHeight;
            float viewAspectRatio = mViewWidth / (float)mViewHeight;
            float relativeAspectRatio = viewAspectRatio / imgAspectRatio;
            float x0, y0, x1, y1;
            if (relativeAspectRatio > 1.0f) {
                x0 = -1.0f / relativeAspectRatio;
                y0 = -1.0f;
                x1 = 1.0f / relativeAspectRatio;
                y1 = 1.0f;
            } else {
                x0 = -1.0f;
                y0 = -relativeAspectRatio;
                x1 = 1.0f;
                y1 = relativeAspectRatio;
            }
            float[] coords = new float[] { x0, y0, x1, y0, x0, y1, x1, y1 };
            mPosVertices.put(coords).position(0);
        }
    }

}
