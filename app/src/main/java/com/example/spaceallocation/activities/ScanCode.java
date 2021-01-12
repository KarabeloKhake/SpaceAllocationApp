package com.example.spaceallocation.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.example.spaceallocation.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class ScanCode extends AppCompatActivity {

    ActionBar actionBar;
    private SurfaceView svScanner;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    //This class provides methods to play DTMF tones
    private ToneGenerator toneGenerator;
    private TextView tvCode;
    String sBarcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_code_activity);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("\t\t\t\t\t\t\tScan User Code");

        toneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC,     100);
        svScanner = findViewById(R.id.svScanner);
        tvCode = findViewById(R.id.tvCode);

        initialiseDetectorsAndSources();
    } //end onCreate()

    //Custom Methods
    private void initialiseDetectorsAndSources() {
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        svScanner.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if(ActivityCompat.checkSelfPermission(ScanCode.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(svScanner.getHolder());
                    } //end if
                    else {
                        ActivityCompat.requestPermissions(ScanCode.this, new
                                        String[]{Manifest.permission.CAMERA},
                                REQUEST_CAMERA_PERMISSION);
                    } //end else
                } //end try
                catch (IOException e) {
                    e.printStackTrace();
                } //end catch()
            } //end surfaceCreated()
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            } //end surfaceChanged()
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            } //end surfaceDestroyed()
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                // Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            } //end release()

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final Intent intent = new Intent();
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if(barcodes.size() != 0) {
                    tvCode.post(new Runnable() {

                        @Override
                        public void run() {
                            barcodes.valueAt(0);
                            tvCode.removeCallbacks(null);
                            sBarcode = barcodes.valueAt(0).email.address;
//                            tvCode.setText(sBarcode);
                            intent.putExtra("userCode", sBarcode);
                            toneGenerator.startTone(ToneGenerator.TONE_CDMA_PIP, 150);

                            //send the code back to the login activity
                            setResult(RESULT_OK, intent);
                        } //end run()
                    });
                } //end if
            } //end receiveDetections()
        });
    } //end initialiseDetectorsAndSources()
} //end class ScanCode
