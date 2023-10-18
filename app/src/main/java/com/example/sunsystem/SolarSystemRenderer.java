package com.example.sunsystem;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.SystemClock;

class SolarSystemRenderer implements GLSurfaceView.Renderer
{
    private float mAngle;

    private Planet m_Earth;
    private Planet m_Sun;
    private Planet m_Moon;
    private float[] m_Eyeposition = { 0.0f, 0.0f, 0.0f };
    private boolean mTranslucentBackground;
    public final static int SS_SUNLIGHT = GL10.GL_LIGHT0;
    public final static int SS_FILLLIGHT1 = GL10.GL_LIGHT1;
    public final static int SS_FILLLIGHT2 = GL10.GL_LIGHT2;
    public final static int X_VALUE	= 0;
    public final static int Y_VALUE = 1;
    public final static int Z_VALUE = 2;
    private float angle = 0.0f;

    public Context myContext;

    public SolarSystemRenderer(Context context) {
        this.myContext = context;
    }

    private void initGeometry(GL10 gl) {
        int resid;
        m_Eyeposition[X_VALUE] = 0.0f;
        m_Eyeposition[Y_VALUE] = 0.0f;
        m_Eyeposition[Z_VALUE] = 10.0f;
        resid =  R.drawable.earth_light;//1
        m_Earth = new Planet(50, 50, .5f, 1.0f, gl, myContext, true, resid);
        m_Earth.setPosition(0.0f, 0.0f, -2.0f);
        m_Sun = new Planet(50, 50, 1.0f, 1.0f, gl, myContext, false, 0);
        m_Sun.setPosition(0.0f, 0.0f, 0.0f);
        int moonResId = R.drawable.moon_texture;
        m_Moon = new Planet(50, 50, 0.1f, 1.0f, gl, myContext, true, moonResId);
        m_Moon.setPosition(2.5f, 0.0f, 0.0f);
    }

    private void initLighting(GL10 gl) {
        float[] sunPos = { 0.0f, 0.0f, 0.0f, 1.0f };
        float[] posFill1 = { -15.0f, 15.0f, 0.0f, 1.0f };
        float[] posFill2 = { -10.0f, -4.0f, 1.0f, 1.0f };

        float[] white = { 1.0f, 1.0f, 1.0f, 1.0f };
        float[] dimblue = { 0.0f, 0.0f, .2f, 1.0f };

        float[] cyan = { 0.0f, 1.0f, 1.0f, 1.0f };
        float[] red = { 1.0f, 1.0f, 0.0f, 1.0f };
        float[] yellow = { 1.0f, 0.0f, 0.0f, 1.0f };
        float[] dimmagenta = { .75f, 0.0f, .25f, 1.0f };

        float[] dimcyan = { 0.0f, .5f, .5f, 1.0f };

        //lights go here

        gl.glLightfv(SS_SUNLIGHT, GL10.GL_POSITION, makeFloatBuffer(sunPos));
        gl.glLightfv(SS_SUNLIGHT, GL10.GL_DIFFUSE, makeFloatBuffer(red));
        gl.glLightfv(SS_SUNLIGHT, GL10.GL_SPECULAR, makeFloatBuffer(yellow));

        gl.glLightfv(SS_FILLLIGHT1, GL10.GL_POSITION, makeFloatBuffer(posFill1));
        gl.glLightfv(SS_FILLLIGHT1, GL10.GL_DIFFUSE, makeFloatBuffer(dimblue));
        gl.glLightfv(SS_FILLLIGHT1, GL10.GL_SPECULAR, makeFloatBuffer(dimcyan));

        gl.glLightfv(SS_FILLLIGHT2, GL10.GL_POSITION, makeFloatBuffer(posFill2));
        gl.glLightfv(SS_FILLLIGHT2, GL10.GL_SPECULAR, makeFloatBuffer(dimmagenta));
        gl.glLightfv(SS_FILLLIGHT2, GL10.GL_DIFFUSE, makeFloatBuffer(dimblue));

        //materials go here
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, makeFloatBuffer(cyan));
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, makeFloatBuffer(white));

        gl.glMaterialf(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, 25);

        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glLightModelf(GL10.GL_LIGHT_MODEL_TWO_SIDE, 1.0f);

        gl.glEnable(GL10.GL_LIGHTING);
        gl.glEnable(SS_SUNLIGHT);
        gl.glEnable(SS_FILLLIGHT1);
        gl.glEnable(SS_FILLLIGHT2);
    }

    private void initSetClipping(GL10 gl) {
        // TODO Auto-generated method stub
    }

    protected static FloatBuffer makeFloatBuffer(float[] arr) {
        ByteBuffer bb = ByteBuffer.allocateDirect(arr.length*4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(arr);
        fb.position(0);
        return fb;
    }

    public void executePlanet(Planet mPlanet, GL10 gl) {
        gl.glPushMatrix();
        gl.glTranslatef(mPlanet.m_Pos[0], mPlanet.m_Pos[1], mPlanet.m_Pos[2]);
        mPlanet.draw(gl);
        gl.glPopMatrix();
    }

    public void onDrawFrame(GL10 gl) {
        float[] paleYellow = {1.0f, 1.0f, 0.3f, 1.0f};
        float[] white = {1.0f, 1.0f, 1.0f, 1.0f};
        float[] cyan = {0.0f, 1.0f, 1.0f, 1.0f};
        float[] black = {0.0f, 0.0f, 0.0f, 0.0f};
        float[] sunPos = {0.0f, 0.0f, 0.0f, 1.0f};

        // Очищаем экран и устанавливаем параметры вида
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();

        float[] sunDiffuseColor = {1.0f, 1.0f, 0.0f, 1.0f}; // Красный цвет
        float[] sunSpecularColor = {1.0f, 1.0f, 0.0f, 1.0f}; // Красный цвет

        gl.glLightfv(SS_SUNLIGHT, GL10.GL_POSITION, makeFloatBuffer(sunPos));
        gl.glLightfv(SS_SUNLIGHT, GL10.GL_DIFFUSE, makeFloatBuffer(sunDiffuseColor));
        gl.glLightfv(SS_SUNLIGHT, GL10.GL_SPECULAR, makeFloatBuffer(sunSpecularColor));

        // Устанавливаем позицию камеры (наблюдателя)
        gl.glTranslatef(0.0f, 0.0f, -50.0f);

        // Вращаем Землю вокруг Солнца
        float earthOrbitRadius = 10.0f; // Радиус орбиты Земли вокруг Солнца
        float earthOrbitSpeed = 0.5f;   // Скорость обращения Земли вокруг Солнца (в градусах за кадр)
        mAngle += earthOrbitSpeed;      // Увеличиваем угол для следующего кадра
        float earthX = (float) (earthOrbitRadius * Math.cos(Math.toRadians(mAngle)));
        float earthZ = (float) (earthOrbitRadius * Math.sin(Math.toRadians(mAngle)));

        // Вращаем Луну вокруг Земли
        float moonOrbitRadius = 1.0f;  // Радиус орбиты Луны вокруг Земли
        float moonAngle = mAngle * 2;   // Луна вращается быстрее, поэтому увеличиваем скорость

        float moonX = earthX + (float) (moonOrbitRadius * Math.cos(Math.toRadians(moonAngle)));
        float moonZ = earthZ + (float) (moonOrbitRadius * Math.sin(Math.toRadians(moonAngle)));

        // Рисуем Солнце
        gl.glPushMatrix();
        executePlanet(m_Sun, gl);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_EMISSION, makeFloatBuffer(paleYellow));
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, makeFloatBuffer(black));
        gl.glPopMatrix();

        // Рисуем Землю
        gl.glPushMatrix();
        gl.glTranslatef(earthX, 0.0f, earthZ);
        gl.glRotatef(0.5f, 0.5f, 1.0f, 0.0f);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, makeFloatBuffer(black));
        executePlanet(m_Earth, gl);
        gl.glPopMatrix();

        // Рисуем Луну
        gl.glPushMatrix();
        gl.glRotatef(0.5f, 0.5f, 1.0f, 0.0f);
        gl.glTranslatef(moonX, 0.0f, moonZ);
        executePlanet(m_Moon, gl);
        gl.glPopMatrix();
    }


    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
        float aspectRatio;
        float zNear = .1f;
        float zFar = 1000;
        float fieldOfView = 30.0f/57.3f;
        float size;
        gl.glEnable(GL10.GL_NORMALIZE);
        aspectRatio = (float)width / (float)height;
        gl.glMatrixMode(GL10.GL_PROJECTION);
        size = zNear * (float)(Math.tan((double)(fieldOfView/2.0f)));
        gl.glFrustumf(-size, size, -size /aspectRatio,
                size /aspectRatio, zNear, zFar);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        initGeometry(gl);
        initLighting(gl);
        initSetClipping(gl);
        gl.glDisable(GL10.GL_DITHER);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,
                GL10.GL_FASTEST);
        gl.glCullFace(GL10.GL_FRONT);
        if (!mTranslucentBackground) {
            gl.glClearColor(0, 0, 0, 0);
        }
        else {
            gl.glClearColor(1,1,1,1);
        }
        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glEnable(GL10.GL_DEPTH_TEST);
    }

}