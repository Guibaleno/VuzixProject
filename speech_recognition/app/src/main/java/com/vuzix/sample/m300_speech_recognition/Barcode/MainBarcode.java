package com.vuzix.sample.m300_speech_recognition.Barcode;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.vuzix.sample.m300_speech_recognition.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Barcode scanner sample code.
 *
 * This class gets the camera up and running.  The barcode interactions are in FindBarcode.java
 *  *
 * Position barcode in view frame and in focus.  Take picture with any key. If results are found
 * a toast with result text will show.
 */

public class MainBarcode extends Activity {
    private final String LOG_TAG = "BarcodeFromImage";
    public final String CUSTOM_SDK_INTENT = "com.vuzix.sample.m300_voicecontrolwithsdk.CustomIntent";
    private TextureView mTextureView;
    private CameraDevice mCameraDevice;
    private CameraCaptureSession mCameraCaptureSessions;
    private CaptureRequest.Builder mCaptureRequestBuilder;

    private Handler mBackgroundHandler;
    private Handler mUiThreadHandler;

    BarcodeFinder mBarcodeProcessor;

    private boolean mTakingPicture;   // Prevents multiple requests at one time

    private final static int TAKE_PICTURE_COMPLETED = 1001;
    private static final int REQUEST_PERMISSIONS = 2222; // unique to this application

    /**
     * Registers the UI handlers and threads, and creates the barcode scanner object
     *
     * @param savedInstanceState - ignored by us
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        // surface listeners - the only purpose is to open the camera when the preview surface becomes available
        mTextureView = (TextureView) findViewById(R.id.texture);
        mTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                openCamera();  // Open the camera whenever hte surface becomes available
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                // No action
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                // Note: Calling closeCamera() here causes a race condition. Use onPause()
                return true;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
                // no action
            }
        });

        // Handler for intercepting TAKE_PICTURE_COMPLETED back on the UI thread
        mUiThreadHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case TAKE_PICTURE_COMPLETED:
                        onPictureComplete();
                        break;
                    default:
                        super.handleMessage(msg);
                        break;
                }
            }
        };

        // Create a background thread
        HandlerThread mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());

        // Create the class that will handle the image and process for barcodes
        mBarcodeProcessor = new BarcodeFinder(this);
    }

    /**
     * Close the camera when we pause
     *
     * Note: the surface listener opens it again when we resume, so we don't need an onResume()
     */
    @Override
    protected void onPause() {
        closeCamera();
        super.onPause();
    }

    /**
     * Handles any physical button press to take the picture and evaluate for a barcode
     * @param keycode The keycode that is pressed/released
     * @param ignoredEvent - not used
     * @return True if handled, false otherwise
     */
    @Override
    public boolean onKeyDown(int keycode, KeyEvent ignoredEvent) {
        switch (keycode) {
            case KeyEvent.KEYCODE_DPAD_RIGHT:
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_ENTER:
                takeStillPicture();
                return true;
            case KeyEvent.KEYCODE_BACK:
                finish();
        }
        return super.onKeyDown(keycode, ignoredEvent);
    }

    /**
     * Called on the UI thread when the image is completely processed.  Re-starts the live preview
     */
    private void onPictureComplete() {
        mTakingPicture = false;
        createCameraPreview();
    }


    /**
     * Creates the camera preview after opening the camera, or when the photo preview times-out
     */
    protected synchronized void createCameraPreview() {
        try {
            SurfaceTexture texture = mTextureView.getSurfaceTexture();
            if ((null == texture) || (null == mCameraDevice)) {
                return;
            }
            Surface surface = new Surface(texture);
            mCaptureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mCaptureRequestBuilder.addTarget(surface);
            mCameraDevice.createCaptureSession(Collections.singletonList(surface), new CameraCaptureSession.StateCallback(){
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    if (null == mCameraDevice) return;
                    mCameraCaptureSessions = session;
                    try {
                        mCameraCaptureSessions.setRepeatingRequest(mCaptureRequestBuilder.build(), null, null);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(MainBarcode.this, "Configuration change", Toast.LENGTH_SHORT).show();
                }
            }, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the camera
     */
    private synchronized void openCamera() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            assert cameraManager != null;
            String mCameraId = cameraManager.getCameraIdList()[0];
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)  {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSIONS);
                return;
            }
            cameraManager.openCamera(mCameraId, new CameraDevice.StateCallback() {
                @Override
                public void onOpened(CameraDevice camera) {
                    mCameraDevice = camera;
                    createCameraPreview();
                }

                @Override
                public void onDisconnected(CameraDevice camera) {
                    mCameraDevice.close();
                }

                @Override
                public void onError(CameraDevice camera, int error) {
                    if (null != mCameraDevice) {
                        mCameraDevice.close();
                        mCameraDevice = null;
                    }
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
            mCameraDevice = null;
        } catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the camera
     */
    private synchronized void closeCamera() {
        if (mCameraCaptureSessions != null) {
            mCameraCaptureSessions.close();
            mCameraCaptureSessions = null;
        }
        if ( mCameraDevice != null ) {
            mCameraDevice.close();
            mCameraDevice = null;
        }
    }


    /**
     * Called from our button handlers to take a picture
     *
     * Registers handlers, then makes the capture request
     **/
    public void takeStillPicture() {
        if (null == mCameraDevice) {
            Log.e(LOG_TAG,"No camera device");
            return;
        }

        if (mTakingPicture) {
            return;
        }
        mTakingPicture = true;

        Log.d(LOG_TAG,"takeStillPicture()");
        SurfaceTexture texture = mTextureView.getSurfaceTexture();
        Surface surface = new Surface(texture);
        try {
            mCaptureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_ZERO_SHUTTER_LAG);
            mCaptureRequestBuilder.addTarget(surface);
            mCameraDevice.createCaptureSession(Collections.singletonList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    try {
                        mCaptureRequestBuilder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER, CameraMetadata.CONTROL_AE_PRECAPTURE_TRIGGER_START);
                        mCaptureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
                        session.capture(mCaptureRequestBuilder.build(), new CameraCaptureSession.CaptureCallback() {
                            @Override
                            public void onCaptureProgressed(CameraCaptureSession session, CaptureRequest request, CaptureResult partialResult) {
                                // No action
                            }
                            @Override
                            public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                                handleCaptureCompleted();
                            }
                        }, mBackgroundHandler);

                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {

                }
            }, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * This callback is invoked when the capture request is complete.
     *
     * Sets up another capture request to format the data in the preferred format and set a handler
     * for when the image conversion is done
     */
    private void handleCaptureCompleted(){
        try {
            Log.d(LOG_TAG,"handleCaptureCompleted()");
            final int imageWidth = 1920, imageHeight = 1080;
            List<Surface> outputSurfaces = new ArrayList<Surface>();
            SurfaceTexture texture = mTextureView.getSurfaceTexture();
            Surface surface = new Surface(texture);
            outputSurfaces.add(surface);
            ImageReader reader = ImageReader.newInstance(imageWidth, imageHeight, ImageFormat.JPEG, 1);
            outputSurfaces.add(reader.getSurface());

            mCaptureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            mCaptureRequestBuilder.addTarget(surface);
            mCaptureRequestBuilder.addTarget(reader.getSurface());

            // Create an image listener that run on our background thread and processes the image
            reader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    handleCameraImageOnWorkerThread(reader);
                }
            }, mBackgroundHandler);

            // Create a configuration session. This handler can use our UI thread (null)
            mCameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    mCameraCaptureSessions = session;
                    try {
                        mCaptureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, 0);
                        mCameraCaptureSessions.capture(mCaptureRequestBuilder.build(), null, null);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {
                    // No action
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the image data by calling our barcode engine helper class
     *
     * @param reader - The image reader
     */
    private void handleCameraImageOnWorkerThread(ImageReader reader){
        Log.d(LOG_TAG, "Processing barcode results");
        String dataToShow = mBarcodeProcessor.getBarcodeResults(reader);
        reader.close();

        if(dataToShow == null) {
            dataToShow = getResources().getString(R.string.no_barcode_in_image);
        }

        // Show the user
        Log.i(LOG_TAG, "Result: " + dataToShow );
        Toast.makeText(MainBarcode.this, dataToShow , Toast.LENGTH_LONG).show();

        Message msg = mUiThreadHandler.obtainMessage();
        msg.what = TAKE_PICTURE_COMPLETED;
        mUiThreadHandler.sendMessage(msg);
    }

    /**
     * Handle permissions response.  Either closes the app, or initializes the camera
     *
     * @param requestCode - unique value to identify the request
     * @param permissions - specific permission being granted/denied
     * @param grantResults - results for each permission
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if ( (requestCode == REQUEST_PERMISSIONS) && (grantResults.length > 0)) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(MainBarcode.this, getResources().getString(R.string.no_permission), Toast.LENGTH_LONG).show();
                finish();
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}