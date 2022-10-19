package com.example.laundryapp.Activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laundryapp.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class Tab_Home extends Fragment {
    private SurfaceView scv;
    private BarcodeDetector barcode;
    private CameraSource camera;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    //play DTMF tone
    private ToneGenerator tone;
    private TextView tv;
    private String data;

    public Tab_Home() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_tab__home,container, false);
        tone = new ToneGenerator(AudioManager.STREAM_MUSIC,100);
        scv = v.findViewById(R.id.sc_view);
        tv = v.findViewById(R.id.tx_code);

        return v;

    }
    private void initialiseDetector(){
        barcode = new BarcodeDetector.Builder(getContext()).setBarcodeFormats(Barcode.ALL_FORMATS).build();
        camera = new CameraSource.Builder(getContext(), barcode).setRequestedPreviewSize(1920, 1080).setAutoFocusEnabled(true).build();
        scv.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                try{
                    if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                        camera.start(scv.getHolder());
                    }else{
                        ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }
                }catch (IOException e){
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Unable to launch camera", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getContext(), "barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
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
                                    tv.setText(data);
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

    }

    @Override
    public void onResume() {
        super.onResume();
        initialiseDetector();
    }
}