package com.example.laundryapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laundryapp.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.lang.reflect.Parameter;
import java.security.Policy;

public class Scanner extends AppCompatActivity {

    private SurfaceView scv;
    private BarcodeDetector barcode;
    private CameraSource camera;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    //play DTMF tone
    private ToneGenerator tone;
    private TextView tv;
    private String data;
    private ImageView flash;
    private Camera kamera;
    private boolean isOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        tone = new ToneGenerator(AudioManager.STREAM_MUSIC,100);
        scv = findViewById(R.id.sc_view);
        tv = findViewById(R.id.tx_code);
        flash = findViewById(R.id.flash);

    }
    private void initialiseDetector(){
        barcode = new BarcodeDetector.Builder(getApplicationContext()).setBarcodeFormats(Barcode.ALL_FORMATS).build();
        camera = new CameraSource.Builder(getApplicationContext(), barcode).setRequestedPreviewSize(1920, 1080).setAutoFocusEnabled(true).build();
        scv.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                try{
                    if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                        camera.start(scv.getHolder());
                    }else{
                        ActivityCompat.requestPermissions(Scanner.this, new String[] {Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }
                }catch (IOException e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Unable to launch camera", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder Holder, int format, int w, int h) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
                camera.stop();
            }
        });

        barcode.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                Toast.makeText(getApplicationContext(), "barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodeSparseArray = detections.getDetectedItems();
                if(barcodeSparseArray.size() !=0){
                    tv.post(new Runnable() {
                        @Override
                        public void run() {
                            if(barcodeSparseArray.valueAt(0).email != null){
                                tv.removeCallbacks(null);
                                data = barcodeSparseArray.valueAt(0).email.address;
                                tv.setText(data);
                                tone.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                            }else{
                                data = barcodeSparseArray.valueAt(0).displayValue;
                                char[] arr_data;
                                String data_f;
                                StringBuilder sb = new StringBuilder();
                                arr_data = data.toCharArray();
                                for(int i=0; i< arr_data.length;i++){
                                    if(arr_data[i] == '_'){
                                        sb.append('\n');
                                    }else{
                                        sb.append(arr_data[i]);
                                    }
                                }
                                data_f = sb.toString();
                                tv.setText(data_f);
                                tone.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        camera.release();
        kamera.release();
    }

    @Override
    public void onResume() {
        super.onResume();
        initialiseDetector();
        FlashOn();
    }
    public void FlashOn(){
        PackageManager pm = this.getPackageManager();

        // if device support camera?
        if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Log.e("err", "Device has no camera!");
            Toast.makeText(this, "Error, Device has no camera!", Toast.LENGTH_SHORT).show();
            return;
        }
        kamera = Camera.open();
        Camera.Parameters p = kamera.getParameters();
        flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOn){
                    p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    kamera.setParameters(p);
                    kamera.stopPreview();
                    flash.setImageResource(R.drawable.flash_off);
                    isOn= false;
                }else{
                    p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    kamera.setParameters(p);
                    kamera.startPreview();
                    flash.setImageResource(R.drawable.flash_on);
                    isOn = true;
                }

            }
        });
    }
}