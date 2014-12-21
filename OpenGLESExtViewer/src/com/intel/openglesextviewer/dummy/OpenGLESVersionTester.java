package com.intel.openglesextviewer.dummy;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.opengl.GLES10;

import android.util.Log;

public class OpenGLESVersionTester {
	final private static String LOG_TAG = "OpenGLESVersionTester";
	public static class OpenGLESVersion {
		public int majorVersion = 0;
		public int minorVersion = 0;
		public String toString() {
			return String.format("%d.%d", majorVersion, minorVersion);
		}
	}
	/*
	public static class TestResult {
		public OpenGLESVersion version = new OpenGLESVersion();
		public boolean supported = false;
		public String[] allExts = new String[0];
		public String[] intelExts = new String[0];
	};
	*/
	public static OpenGLESVersion getMaxVersionSupported(Activity activity) {
		OpenGLESVersion version = new OpenGLESVersion();
	    // refer to: http://stackoverflow.com/questions/9198293/is-there-a-way-to-check-if-android-device-supports-opengl-es-2-0	
		
        final ActivityManager activityManager = (ActivityManager)activity.getSystemService(Context.ACTIVITY_SERVICE);
        final int glesFullVersion = activityManager.getDeviceConfigurationInfo().reqGlEsVersion;
        
        version.majorVersion = (glesFullVersion & 0xFFFF0000) >> 16;
        version.minorVersion = glesFullVersion & 0x0000FFFF;		
		return version;
	}
	/*
	// TODO: use it to replace TestGLSurfaceView
	public static TestResult test(int majorVersion, int minorVersion) {
        TestResult result = new TestResult();
		
        final int EGL_CONTEXT_MAJOR_VERSION 	= 0x3098;
        final int EGL_CONTEXT_MINOR_VERSION		= 0x30FB;
        
        result.version.majorVersion = majorVersion;
        result.version.minorVersion = minorVersion;
        
        EGL10 egl = (EGL10) EGLContext.getEGL();       
        EGLDisplay display = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);

        int[] version = new int[2];
        egl.eglInitialize(display, version);
        Log.i(LOG_TAG, String.format("EGL version: %d.%d", version[0], version[1]));

        int[] configAttribs =
        {
            EGL10.EGL_RED_SIZE, 4,
            EGL10.EGL_GREEN_SIZE, 4,
            EGL10.EGL_BLUE_SIZE, 4,
            EGL10.EGL_RENDERABLE_TYPE, 4,
            EGL10.EGL_NONE
        };
        EGLConfig[] configs = new EGLConfig[10];
        int[] num_config = new int[1];
        egl.eglChooseConfig(display, configAttribs, configs, 10, num_config);
        
        if (0 < num_config[0]) {
            int[] attribs = {EGL_CONTEXT_MAJOR_VERSION, majorVersion, EGL_CONTEXT_MINOR_VERSION , minorVersion, EGL10.EGL_NONE };

            Log.i(LOG_TAG, String.format("Creating context(%d.%d) ...", majorVersion, minorVersion));
            egl.eglCreateContext(display, configs[0], EGL10.EGL_NO_CONTEXT, attribs);
            int error = egl.eglGetError();
            if (error == EGL10.EGL_SUCCESS) {
            	result.supported = true;
            	result.allExts = GLES10.glGetString(GLES10.GL_EXTENSIONS).split(" ");
            	
        		ArrayList<String> intelExts = new ArrayList<String>();
        		for (int i=0; i<result.allExts.length; ++i) {
        			String ext = result.allExts[i];
        			if (-1 < ext.toLowerCase().indexOf("intel")) {
        				intelExts.add(ext);
        			}
        		}
        		result.intelExts = new String[intelExts.size()];
        		for (int i=0; i<result.intelExts.length; ++i) {
        			result.intelExts[i] = intelExts.get(i).toString();
        		}
            	Log.i(LOG_TAG, String.format("Intel extensions(%d):", result.intelExts.length));
        		for (int i=0; i<result.intelExts.length; ++i) {
        			Log.i(LOG_TAG, result.intelExts[i]);
        		}
        		Log.i(LOG_TAG, String.format("All extensions(%d):", result.allExts.length));
        		for (int i=0; i<result.allExts.length; ++i) {
        			Log.i(LOG_TAG, result.allExts[i]);
        		}
            }
            else {
                Log.e(LOG_TAG, String.format("Failed to create context: EGL error: 0x%x", error));            	
            }            
        }
        egl.eglTerminate(display);
        return result;
	}	
	*/
}
