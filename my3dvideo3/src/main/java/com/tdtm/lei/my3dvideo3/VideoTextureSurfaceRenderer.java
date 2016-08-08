package com.tdtm.lei.my3dvideo3;

/**
 * Created by lei on 5/13/16.
 */
import android.content.Context;
import android.graphics.*;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;
import android.view.WindowManager;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class VideoTextureSurfaceRenderer extends TextureSurfaceRenderer implements
        SurfaceTexture.OnFrameAvailableListener
{

    public static final String TAG = VideoTextureSurfaceRenderer.class.getSimpleName();

    public int offsetValue = 3;

    /**
     *
     */
    private  float squareSize = 1.0f;
    private  float squareCoords[] = {
            -squareSize,  squareSize,   // top left
            -squareSize, -squareSize,   // bottom left
            squareSize, -squareSize,    // bottom right
            squareSize,  squareSize }; // top right

    private static short drawOrder[] = { 0, 1, 2, 0, 2, 3};

    private Context context;

    // Texture to be shown in backgrund
    private FloatBuffer textureBuffer;

    /*
    private float textureCoords[] = {
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f };
            */



    private float textureCoords[] = {
            0.0f, 1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 1.0f, 1.0f
    };


    private int[] textures = new int[1];

    private int shaderProgram;
    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;

    private SurfaceTexture videoTexture;
    private float[] videoTextureTransform;
    private boolean frameAvailable = false;

    int textureParamHandle;
    int textureCoordinateHandle;
    int positionHandle;
    int textureTranformHandle;

    private static final int FLOAT_SIZE_BYTES = 4;


    public VideoTextureSurfaceRenderer(Context context, SurfaceTexture texture, int width, int height)
    {
        super(texture, width, height);
        this.context = context;
        videoTextureTransform = new float[16];
        //initArray();
    }

    private void setupGraphics()
    {
        final String vertexShader = RawResourceReader.readTextFileFromRawResource(context, R.raw.vetext_sharder);
        final String fragmentShader = RawResourceReader.readTextFileFromRawResource(context, R.raw.fragment_sharder);

        final int vertexShaderHandle = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, vertexShader);
        final int fragmentShaderHandle = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);
        shaderProgram = ShaderHelper.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle,
                new String[]{"texture","vPosition","vTexCoordinate","textureTransform"});

        GLES20.glUseProgram(shaderProgram);
        textureParamHandle = GLES20.glGetUniformLocation(shaderProgram, "texture");
        textureCoordinateHandle = GLES20.glGetAttribLocation(shaderProgram, "vTexCoordinate");
        positionHandle = GLES20.glGetAttribLocation(shaderProgram, "vPosition");
        textureTranformHandle = GLES20.glGetUniformLocation(shaderProgram, "textureTransform");
    }

    private void setupVertexBuffer()
    {
        /*
        // Draw list buffer
        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);
        */

        // Initialize the texture holder
        ByteBuffer bb = ByteBuffer.allocateDirect(squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());

        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);
    }

    private void setupTexture()
    {
        ByteBuffer texturebb = ByteBuffer.allocateDirect(textureCoords.length * 4);
        texturebb.order(ByteOrder.nativeOrder());

        textureBuffer = texturebb.asFloatBuffer();
        textureBuffer.put(textureCoords);
        textureBuffer.position(0);

        // Generate the actual texture
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glGenTextures(1, textures, 0);
        checkGlError("Texture generate");

        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textures[0]);
        checkGlError("Texture bind");

        videoTexture = new SurfaceTexture(textures[0]);
        videoTexture.setOnFrameAvailableListener(this);
    }

    @Override
    protected boolean draw()
    {
        //initGLComponents();
        synchronized (this)
        {
            if (frameAvailable)
            {
                videoTexture.updateTexImage();
                videoTexture.getTransformMatrix(videoTextureTransform);
                frameAvailable = false;
            }
            else
            {
                return false;
            }

        }
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glViewport(0, 0, width, height);

        //initGLComponents();


        this.drawTexture();

        return true;
    }

    private void drawTexture() {
        // Draw texture

        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, 2, GLES20.GL_FLOAT, false, 0, vertexBuffer);

        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textures[0]);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glUniform1i(textureParamHandle, 0);

        GLES20.glEnableVertexAttribArray(textureCoordinateHandle);
        GLES20.glVertexAttribPointer(textureCoordinateHandle, 2, GLES20.GL_FLOAT, false, 0, textureBuffer);

        GLES20.glUniformMatrix4fv(textureTranformHandle, 1, false, videoTextureTransform, 0);

        //GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        //GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        for (int i = 0; i < partCount; i = i + 2) {
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 4*i, 4);
        }


        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(textureCoordinateHandle);
    }


    @Override
    protected void initGLComponents()
    {

        initArray();
        setupVertexBuffer();
        setupTexture();

        setupGraphics();

    }

    @Override
    protected void deinitGLComponents()
    {
        GLES20.glDeleteTextures(1, textures, 0);
        GLES20.glDeleteProgram(shaderProgram);
        videoTexture.release();
        videoTexture.setOnFrameAvailableListener(null);
    }


    public void checkGlError(String op)
    {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e("SurfaceTest", op + ": glError " + GLUtils.getEGLErrorString(error));
        }
    }

    @Override
    public SurfaceTexture getVideoTexture()
    {
        return videoTexture;
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture)
    {
        synchronized (this)
        {
            frameAvailable = true;
        }
    }

    private  int partCount = 1920*2;

    private void initArray()
    {



        //partCount = 8;//1920*2;//1000000;
        //static  SceneVertex vertices[1920*2*4];
        textureCoords = new float[partCount*8];
        squareCoords = new float[partCount*8];
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
            squareCoords[j] = startVertexPos + i*vertexInterval ;

            squareCoords[j + 1] = 1.0f;//-1.0f;
            //p++;
            squareCoords[j + 2] = startVertexPos + i*vertexInterval;
            //p++;
            squareCoords[j + 3]  = -1.0f;//1.0f;
            //p++;

            squareCoords[j + 4] = startVertexPos + (i + 1)*vertexInterval ;
            //p++;
            squareCoords[j + 5] = 1.0f;//-1.0f;
            //p++;
            squareCoords[j + 6] = startVertexPos + (i + 1)*vertexInterval;
            //p++;
            squareCoords[j + 7] = -1.0f;//1.0f;//normalizedSamplingSize.height;
            //p++;
        }
        float offset = offsetValue*vertexInterval;//self.offsetNum*vertexInterval;//35*vertexInterval;//27*vertexInterval;//21*vertexInterval;

        for(int i=0; i < partCount/2; i++)
        {
            int j = partCount/2*8 + 8*i;
            squareCoords[j] = startVertexPos + i*vertexInterval + offset;

            squareCoords[j + 1] = 1.0f;//-1.0f;
            //p++;
            squareCoords[j + 2] = startVertexPos + i*vertexInterval + offset;
            //p++;
            squareCoords[j + 3]  = -1.0f;//1.0f;
            //p++;

            squareCoords[j + 4] = startVertexPos + (i + 1)*vertexInterval + offset;
            //p++;
            squareCoords[j + 5] = 1.0f;//-1.0f;
            //p++;
            squareCoords[j + 6] = startVertexPos + (i + 1)*vertexInterval + offset;
            //p++;
            squareCoords[j + 7] = -1.0f;//1.0f;

        }

        for(int i = 0; i < partCount/2; i++)
        {
            int j = 8*i;
            textureCoords[j] = startTexturePos + i*textureInterval;
            //q++;
            textureCoords[j + 1] = 1.0f;
            //q++;
            textureCoords[j + 2] = startTexturePos + i*textureInterval;
            //q++;
            textureCoords[j + 3] = 0.0f;
            //q++;

            textureCoords[j + 4] = startTexturePos + (i + 1)*textureInterval;
            //q++;
            textureCoords[j + 5] = 1.0f;
            //q++;
            textureCoords[j + 6] = startTexturePos + (i + 1)*textureInterval;
            //q++;
            textureCoords[j + 7] = 0.0f;
            //q++;
        }

        startTexturePos = 0.5f;
        for(int i = 0; i < partCount/2; i++)
        {
            int j = partCount/2*8 + 8*i;
            textureCoords[j] = startTexturePos + (i + 1)*textureInterval;
            //q++;
            textureCoords[j + 1] = 1.0f;
            //q++;
            textureCoords[j + 2] = startTexturePos + (i + 1)*textureInterval;
            //q++;
            textureCoords[j + 3] = 0.0f;
            //q++;

            textureCoords[j + 4] = startTexturePos + (i + 2)*textureInterval;
            //q++;
            textureCoords[j + 5] = 1.0f;
            //q++;
            textureCoords[j + 6] = startTexturePos + (i + 2)*textureInterval;
            //q++;
            textureCoords[j + 7] = 0.0f;
        }









    }
}
