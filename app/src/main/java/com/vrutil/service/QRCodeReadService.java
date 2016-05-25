/***********************************************************
 *
 * READ QRCODE BACKGROUND APPLICATION
 * VERSION 1.0
 */
package com.vrutil.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class QRCodeReadService extends Service implements TextureView.SurfaceTextureListener {

    //Log Tags
    private final String TAG = "LOG_FINALY";

    /*
    * Camera orientation
    * Default is BACK
    * */
    public static int CAMERA_FACING = CameraSource.CAMERA_FACING_BACK;

    /*
    * True to never stop the camera
    * False to stop the service with command
    * stopService(new Intent(MainActivity.this, RecorderService.class));
    * */
    public static Boolean SERVICE_NEVER_STOP = Boolean.FALSE;

    /*
    * Read time barcode
    * 0       = anytime
    * 1000    = 1 seconds
    * 1000*60 = 1 minute
    * */
    public static int READ_TIME = 0;

    /*
    * Create Manager
    * */
    private WindowManager mWindowManager;

    /*
    * Inflate Options
    * */
    public LayoutInflater minflater;

    /*
    * Virtual Texture
    * */
    public TextureView mTextureView;

    /*
    * Virtual Camera View
    * */
    SurfaceView cameraView;

    /*
    * Camera Source
    * */
    CameraSource cameraSource;

    /*
    * Barcode Detector
    * */
    BarcodeDetector barcodeDetector;

    @Override
    public void onCreate() {

        Log.i(TAG, "Start onCreate");
        super.onCreate();

        if(!isFrontCameraAvailable()){
            Log.i(TAG, "Cameras doenst exists FACING = " + CAMERA_FACING);
            return;
        }

        //Initialize components
        initializeBarDetector();

        //Creating virtual texture
        createTexture();

        //Starting detector
        startBarDetector();

        //Read barCode
        read();

    }

    private void initializeBarDetector() {
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setRequestedPreviewSize(640, 480)
                .setFacing(CAMERA_FACING)
                //.setFacing(CameraSource.CAMERA_FACING_FRONT)
                .build();
    }

    private void createTexture() {
        mTextureView   = new TextureView(this);
        minflater      = (LayoutInflater)getSystemService (LAYOUT_INFLATER_SERVICE);
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        FrameLayout mParentView = new FrameLayout(getApplicationContext());
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_TOAST,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                PixelFormat.TRANSLUCENT);

        mWindowManager.addView(mParentView, params);
    }

    public void startBarDetector() {
        try {
            cameraSource.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void read() {
        Log.i(TAG, "READ START");
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {}

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {

                    /**********************************/
                    /**********************************/
                    /**********************************/
                    /**********************************/
                    /*********TODA LOGICA AQUI*********/

                    Log.i(TAG, ""+barcodes.valueAt(0).displayValue);

                    /*********TODA LOGICA AQUI*********/
                    /**********************************/
                    /**********************************/
                    /**********************************/
                    /**********************************/

                }
                SystemClock.sleep(READ_TIME);
            }
        });
    }

    private boolean isFrontCameraAvailable() {
        int cameraCount = 0;
        boolean isFrontCameraAvailable = false;
        cameraCount = Camera.getNumberOfCameras();
        while (cameraCount > 0) {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            cameraCount--;
            Camera.getCameraInfo(cameraCount, cameraInfo);
            if (cameraInfo.facing == CAMERA_FACING) {
                isFrontCameraAvailable = true;
                break;
            }
        }
        return isFrontCameraAvailable;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        return START_STICKY_COMPATIBILITY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if( !SERVICE_NEVER_STOP ) {
            cameraSource.stop();
        }
        Log.i(TAG, "SERVICE STOPED");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "ON BIND SERVICE");
        return null;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Log.i(TAG, "onSurfaceTextureAvailable IS AVAILABLE");
        try {
            cameraSource.start(cameraView.getHolder());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {}

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {return false;}

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {}

}
