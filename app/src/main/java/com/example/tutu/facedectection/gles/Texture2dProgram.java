/**
 * TuSDKFilterEngine
 * Texture2dProgram.java
 *
 * @author  Yanlin
 * @Date  May 23, 2017 8:53:22 PM
 * @Copright (c) 2016 tusdk.com. All rights reserved.
 *
 */
package com.example.tutu.facedectection.gles;

import android.annotation.TargetApi;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.os.Build;

import java.nio.FloatBuffer;

/**
 * 
 * 参考了 Grafika 项目中的相关代码 
 * 
 * @author Yanlin
 *
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
public class Texture2dProgram 
{
    public enum ProgramType 
    {
    	/**
    	 * 显示普通的 2D 纹理
    	 */
        TEXTURE_2D, 
        /**
         * 显示 SurfaceTexture 采集得到的 OES 纹理
         */
        TEXTURE_EXT
    }
    
    // Simple vertex shader, used for all programs.
    private static final String VERTEX_SHADER =
            "attribute vec4 aPosition;\n" +
            "attribute vec4 aTextureCoord;\n" +
            "varying vec2 vTextureCoord;\n" +
            "void main() {\n" +
            "    gl_Position =  aPosition;\n" +
            "    vTextureCoord =  aTextureCoord.xy;\n" +
            "}\n";

    // Simple fragment shader for use with "normal" 2D textures.
    private static final String FRAGMENT_SHADER_2D =
            "precision mediump float;\n" +
            "varying vec2 vTextureCoord;\n" +
            "uniform sampler2D sTexture;\n" +
            "void main() {\n" +
            "    gl_FragColor = texture2D(sTexture, vTextureCoord);\n" +
            "}\n";

    // Simple fragment shader for use with external 2D textures (e.g. what we get from
    // SurfaceTexture).
    private static final String FRAGMENT_SHADER_EXT =
            "#extension GL_OES_EGL_image_external : require\n" +
            "precision mediump float;\n" +
            "varying vec2 vTextureCoord;\n" +
            "uniform samplerExternalOES sTexture;\n" +
            "void main() {\n" +
            "    gl_FragColor = texture2D(sTexture, vTextureCoord);\n" +
            "}\n";

    
    // 顶点坐标
	private static final float imageVertices[] = { 
			-1.0f, -1.0f, 
			 1.0f, -1.0f, 
			-1.0f,  1.0f, 
			 1.0f,  1.0f
	};
	
	// 纹理坐标，原始朝向
    private static final float textureCoordinates[] = {
    		0.0f, 0.0f, 
    		1.0f, 0.0f, 
    		0.0f, 1.0f, 
    		1.0f, 1.0f
    };

    private ProgramType mProgramType;

    // Handles to the GL program and various components of it.
    private int mProgramHandle;
    private int maPositionLoc;
    private int maTextureCoordLoc;
    
    private int maTextureUniformLoc;

    private int mTextureTarget;
    
    private FloatBuffer vertexBuffer;
    private FloatBuffer textureCoordBuffer;

    /**
     * Prepares the program in the current EGL context.
     */
    public Texture2dProgram(ProgramType programType) 
    {
        mProgramType = programType;

        switch (programType) {
            case TEXTURE_2D:
                mTextureTarget = GLES20.GL_TEXTURE_2D;
                mProgramHandle = GLUtils.createProgram(VERTEX_SHADER, FRAGMENT_SHADER_2D);
                break;
            case TEXTURE_EXT:
                mTextureTarget = GLES11Ext.GL_TEXTURE_EXTERNAL_OES;
                mProgramHandle = GLUtils.createProgram(VERTEX_SHADER, FRAGMENT_SHADER_EXT);
                break;
            default:
                throw new RuntimeException("Unhandled type " + programType);
        }
        if (mProgramHandle == 0) {
            throw new RuntimeException("Unable to create program");
        }

        // get locations of attributes and uniforms
        maPositionLoc = GLES20.glGetAttribLocation(mProgramHandle, "aPosition");
        maTextureCoordLoc = GLES20.glGetAttribLocation(mProgramHandle, "aTextureCoord");
        maTextureUniformLoc =  GLES20.glGetUniformLocation(mProgramHandle, "sTexture");
        
        GLES20.glUseProgram(mProgramHandle);
        GLES20.glEnableVertexAttribArray(maPositionLoc);
        GLES20.glEnableVertexAttribArray(maTextureCoordLoc);
        
        vertexBuffer = GLUtils.createFloatBuffer(imageVertices);
        textureCoordBuffer = GLUtils.createFloatBuffer(textureCoordinates);
        
        GLUtils.checkGlError("create Progrom");
    }

    /**
     * Releases the program.
     * <p>
     * The appropriate EGL context must be current (i.e. the one that was used to create
     * the program).
     */
    public void release() 
    {
        GLES20.glDeleteProgram(mProgramHandle);
        mProgramHandle = -1;
    }

    /**
     * Returns the program type.
     */
    public ProgramType getProgramType() 
    {
        return mProgramType;
    }

    /**
     * 绘制图片
     * 
     * @param textureId 纹理
     */
    public void draw(int textureId) 
    { 
//    	TLog.d("draw rotation: %d - %d", rotation.getDegree(), rotation.getFlag());
    	GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
		GLES20.glClearColor(0, 0, 0, 1.0f);
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		
    	// Select the program.
        GLES20.glUseProgram(mProgramHandle);
//        TuSDKGLUtils.checkGlError("glUseProgram");

        // Set the texture.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE2);
        GLES20.glBindTexture(mTextureTarget, textureId);
        GLES20.glUniform1i(maTextureUniformLoc, 2);
        
        // Connect vertexBuffer to "aPosition".
        GLES20.glVertexAttribPointer(maPositionLoc, 2,
            GLES20.GL_FLOAT, false, 0, vertexBuffer);
 
        // Connect texBuffer to "aTextureCoord".
        GLES20.glVertexAttribPointer(maTextureCoordLoc, 2,
                GLES20.GL_FLOAT, false, 0, textureCoordBuffer);

        // Draw the rect.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        GLES20.glBindTexture(mTextureTarget, 0);
        GLES20.glUseProgram(0);
    }
}
