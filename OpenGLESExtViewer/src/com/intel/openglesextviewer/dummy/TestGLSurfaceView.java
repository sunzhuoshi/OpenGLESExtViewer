package com.intel.openglesextviewer.dummy;

/*
 * This is a modified version of the GL2JNIView.java file distributed by The 
 * Android Open Source Project

 * Copyright (C) 2008-2009 The Android Open Source Project 
 * Copyright (C) 2012 ARM Limited

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
  
 * http://www.apache.org/licenses/LICENSE-2.0
  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.ArrayList;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.opengles.GL10;


public class TestGLSurfaceView extends GLSurfaceView 
{		
    private static String LOG_TAG = "TestGLSurfaceView";

    private int redSize = 5;
    private int greenSize = 6;
    private int blueSize = 5;
    private int alphaSize = 0;
    private int numberOfSamples = 4;
    private int depthSize = 16;

    static private int testGLESMajorVersion = 3;
    static private int testGLESMinorVersion = 1;
    static private ResultHandler testResultHandler = null;
    
    public int glesMajorVersion;
    public int glesMinorVersion;
    public boolean supported = false;
    public Object[] extensions; 
    public Object[] intelExtensions;
    private ResultHandler mResultHandler;
    public static void setTestParams(int majorVersion, int minorVersion)
    {
    	testGLESMajorVersion = majorVersion;
    	testGLESMinorVersion = minorVersion;
    }
    public static void setResultHandler(ResultHandler handler)
    {
    	testResultHandler = handler;
    }
    public TestGLSurfaceView(Context context, AttributeSet attrs)
    {
        super(context);
        
    	supported = true;        
    	extensions = new String[0];        
    	intelExtensions = new String[0];
        /* Initialize this view */
        setEGLContextFactory(new TheEGLContextFactory());
        setEGLConfigChooser(new TheConfigChooser());
        setRenderer(new TheRenderer());
    }
    int getNumberOfSamples() 
    {
    	return numberOfSamples;
    }
    int getDepthSize() 
    {
    	return depthSize;
    }
    public interface ResultHandler 
    {
    	public void handleResult(TestGLSurfaceView testView);
    }
    protected class TheRenderer implements GLSurfaceView.Renderer 
    {
        public void onDrawFrame(GL10 gl) 
        {
        }

        public void onSurfaceChanged(GL10 gl, int width, int height) 
        {
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) 
        {
        	Log.i(LOG_TAG, String.format("OpenGLES (%d.%d), supported: %b", glesMajorVersion, glesMinorVersion, supported));        	
        	if (supported) {
        		extensions = gl.glGetString(GL10.GL_EXTENSIONS).split(" ");
        		ArrayList<String> intelExts = new ArrayList<String>();
        		for (int i=0; i<extensions.length; ++i) {
        			String ext = extensions[i].toString();
        			if (-1 < ext.toLowerCase().indexOf("intel")) {
        				intelExts.add(ext);
        			}
        		}
        		intelExtensions = intelExts.toArray();
        		Log.i(LOG_TAG, String.format("Intel extensions(%d):", intelExtensions.length));
        		for (int i=0; i<intelExtensions.length; ++i) {
        			Log.i(LOG_TAG, intelExtensions[i].toString());
        		}
        		Log.i(LOG_TAG, String.format("All extensions(%d):", extensions.length));
        		for (int i=0; i<extensions.length; ++i) {
        			Log.i(LOG_TAG, extensions[i].toString());
        		}
        	}
        	if (null != mResultHandler) {
        		mResultHandler.handleResult(TestGLSurfaceView.this);
        	}
        }
    }
    /*
     * An implementation of "GLSurfaceView.EGLContextFactory"
     * for customizing the eglCreateContext and
     * eglDestroyContext calls.
     */
    protected class TheEGLContextFactory implements GLSurfaceView.EGLContextFactory 
    {

        public EGLContext createContext(EGL10 egl, EGLDisplay display, EGLConfig config) 
        {
            /* From the Khronos definitions */
            final int EGL_CONTEXT_CLIENT_VERSION = 0x3098;
            final int EGL_CONTEXT_MINOR_VERSION_KHR = 0x30FB;
            
            int error;

            glesMajorVersion = testGLESMajorVersion;
            glesMinorVersion = testGLESMinorVersion;
            mResultHandler = testResultHandler;            
            
            while ((error = egl.eglGetError()) != EGL10.EGL_SUCCESS) 
            {
                Log.e(LOG_TAG, String.format("Before TheEGLContextFactory.createContext(): EGL error: 0x%x", error));
            }

            /* Use the value of glesVersion */
            Log.i(LOG_TAG, String.format("Creating OpenGLES context(%d.%d) ...", glesMajorVersion, glesMinorVersion));
            int[] attribs = {EGL_CONTEXT_CLIENT_VERSION, glesMajorVersion, EGL_CONTEXT_MINOR_VERSION_KHR , glesMinorVersion, EGL10.EGL_NONE };

            EGLContext context = egl.eglCreateContext(display, config, EGL10.EGL_NO_CONTEXT, attribs);
            while ((error = egl.eglGetError()) != EGL10.EGL_SUCCESS)
            {
            	supported = false;
                Log.e(LOG_TAG, String.format("After TheEGLContextFactory.createContext(): EGL error: 0x%x", error));
                // fall back context
                Log.i(LOG_TAG,  "Use fallback EGL context");
                int[] fallBackAttribs = {EGL_CONTEXT_CLIENT_VERSION, 2, EGL_CONTEXT_MINOR_VERSION_KHR , 0, EGL10.EGL_NONE };
                context = egl.eglCreateContext(display, config, EGL10.EGL_NO_CONTEXT, fallBackAttribs);                
            }
            return context;
        }

        public void destroyContext(EGL10 egl, EGLDisplay display, EGLContext context) 
        {
            /* Allow the derived classes to destroy their resources*/
            egl.eglDestroyContext(display, context);
        }
    }

    /*
     * An implementation of "GLSurfaceView.EGLConfigChooser"
     * for choosing an EGLConfig configuration from a list of
     * potential configurations.
     */
    protected class TheConfigChooser implements GLSurfaceView.EGLConfigChooser 
    {
        protected int getConfigAttrib(EGL10 egl, EGLDisplay display, EGLConfig config, int attribute, int defaultValue) 
        {
            int[] ret = new int[1];
            
            if (egl.eglGetConfigAttrib(display, config, attribute, ret)) 
            {
                return ret[0];
            }
            
            return defaultValue;
        }

        public EGLConfig SelectAnEGLConfig(EGL10 egl, EGLDisplay display, EGLConfig[] configs) 
        {

            for(EGLConfig cfg : configs) 
            {
                int r = getConfigAttrib(egl, display, cfg, EGL10.EGL_RED_SIZE, 0);
                int g = getConfigAttrib(egl, display, cfg, EGL10.EGL_GREEN_SIZE, 0);
                int b = getConfigAttrib(egl, display, cfg, EGL10.EGL_BLUE_SIZE, 0);
                int a = getConfigAttrib(egl, display, cfg, EGL10.EGL_ALPHA_SIZE, 0);

                if (r == redSize && g == greenSize && b == blueSize && a == alphaSize)
                {
                    return cfg;
                }
            }
            
            return null;
        }

        public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) 
        {
            /* From the Khronos definitions */
            final int EGL_OPENGL_ES2_BIT = 4;

            int[] attribs =
            {
                EGL10.EGL_RED_SIZE, redSize,
                EGL10.EGL_GREEN_SIZE, greenSize,
                EGL10.EGL_BLUE_SIZE, blueSize,
                EGL10.EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT,
                EGL10.EGL_SAMPLES, getNumberOfSamples(),
                EGL10.EGL_DEPTH_SIZE, getDepthSize(),
                EGL10.EGL_NONE
            };
            int[] num_config = new int[1];
            egl.eglChooseConfig(display, attribs, null, 0, num_config);

            int numConfigs = num_config[0];

            if (numConfigs <= 0)
            {
                Log.e(LOG_TAG, "No EGL configs were found.");
                return null;
            }

            EGLConfig[] configs = new EGLConfig[numConfigs];
            egl.eglChooseConfig(display, attribs, configs, numConfigs, num_config);

            return SelectAnEGLConfig(egl, display, configs);
        }
    }
}
