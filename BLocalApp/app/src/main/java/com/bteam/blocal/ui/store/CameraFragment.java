package com.bteam.blocal.ui.store;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.bteam.blocal.BarcodeScannerAnalyzer;
import com.bteam.blocal.R;
import com.bteam.blocal.data.IOnCompleteCallback;
import com.bteam.blocal.utility.NavigationResult;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

//This activity and its layout was based on https://akhilbattula.medium.com/android-camerax-java-example-aeee884f9102
//There have been some changes as some functions aren't in use anymore
public class CameraFragment extends Fragment {
    public static final String BAR_CODE_RESULT = "bar_code_result";

    private int REQUEST_CODE_PERMISSIONS = 1001;
    private final String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.CAMERA};
    protected Toolbar tlb;
    private boolean scanFinished = false;

    private boolean allPermissionsGranted() {

        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(getActivity(), permission) !=
                    PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private Executor executor = Executors.newSingleThreadExecutor();
    private PreviewView mPreviewView;

    public CameraFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_camera_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPreviewView = view.findViewById(R.id.camera);
        tlb = view.findViewById(R.id.tlb_camera_view);

        tlb.setNavigationOnClickListener(l -> navigateBack());

        if (allPermissionsGranted()) {
            startCamera(); //start camera if permission has been granted by user
        } else {
            requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                navigateBack();
            }
        }
    }

    private void startCamera() {
        final ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider
                .getInstance(getContext());

        cameraProviderFuture.addListener(() -> {
            try {

                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);

            } catch (ExecutionException | InterruptedException e) {
                // No errors need to be handled for this Future.
                // This should never be reached.
            }
        }, ContextCompat.getMainExecutor(getContext()));
    }

    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {

        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .build();

        imageAnalysis.setAnalyzer(executor,
                new BarcodeScannerAnalyzer(new IOnCompleteCallback<String>() {
            @Override
            public void onError(Throwable err) {
                if(!scanFinished){
                    navigateBack();
                    imageAnalysis.clearAnalyzer();
                }
                scanFinished = true;


            }

            @Override
            public void onSuccess(String data) {
                if(!scanFinished){
                    barCodeScanned(data);
                    imageAnalysis.clearAnalyzer();
                }
                scanFinished = true;

            }
        }));
        preview.setSurfaceProvider(mPreviewView.getSurfaceProvider());
        Camera camera = cameraProvider.bindToLifecycle(getViewLifecycleOwner(),
                cameraSelector, preview, imageAnalysis);
    }

    protected void barCodeScanned(String data) {
        NavigationResult.setNavigationResult(this, BAR_CODE_RESULT, data);
        navigateBack();
    }

    private void navigateBack() {
        NavHostFragment.findNavController(this).navigateUp();
    }
}