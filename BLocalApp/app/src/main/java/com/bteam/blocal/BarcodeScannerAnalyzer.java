package com.bteam.blocal;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.Image;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import com.bteam.blocal.data.IOnCompleteCallback;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

//Basis from https://developers.google.com/ml-kit/vision/barcode-scanning/android
public class BarcodeScannerAnalyzer implements ImageAnalysis.Analyzer {

    private static final String TAG = "BARCODE_SCANNER";
    public final BarcodeScanner scanner;
    private final IOnCompleteCallback<String> callback;

    public BarcodeScannerAnalyzer(IOnCompleteCallback<String> callback) {
        this.callback = callback;
        //Initialisation of scanner: standards used in Denmark, Europe are EAN and UPC
        BarcodeScannerOptions options =
                new BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(
                                Barcode.FORMAT_EAN_13,
                                Barcode.FORMAT_EAN_8,
                                Barcode.FORMAT_UPC_A,
                                Barcode.FORMAT_UPC_E)
                        .build();

        scanner = BarcodeScanning.getClient(options);
    }

    @Override
    public void analyze(@NonNull ImageProxy image) {
        @SuppressLint("UnsafeExperimentalUsageError") Image mediaImg = image.getImage();
        if (mediaImg != null) {
            InputImage img = InputImage.fromMediaImage(mediaImg,
                    image.getImageInfo().getRotationDegrees());

            scanner.process(img)
                    .addOnSuccessListener(barcodes -> {
                        for (Barcode barcode: barcodes) {
                            Rect bounds = barcode.getBoundingBox();
                            Point[] corners = barcode.getCornerPoints();

                            String rawValue = barcode.getRawValue();

                            int valueType = barcode.getValueType();

                            switch (valueType) {
                                case Barcode.FORMAT_EAN_8:
                                case Barcode.FORMAT_EAN_13:
                                case Barcode.FORMAT_UPC_A:
                                case Barcode.FORMAT_UPC_E:
                                case Barcode.TYPE_PRODUCT:
                                    callback.onSuccess(barcode.getRawValue());
                                    break;
                            }
                        }
                    })
                    .addOnCompleteListener(results -> image.close());
        }
    }
}
