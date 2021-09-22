package com.bteam.blocal.utility;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bteam.blocal.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.FileNotFoundException;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class ImageSelector {
    public static final int TAKE_PICUTRE_REQUEST_CODE = 45;
    public static final int PICK_PICTURE_REQUEST_CODE = 46;
    public static final int MY_CAMERA_REQUEST_CODE = 100;

    public static Bitmap onActitityResultImageHandler(ContentResolver contentResolver,
                                                      int requestCode, int resultCode,
                                                      @Nullable Intent data) {
        if (resultCode != RESULT_CANCELED) {
            Bitmap image = null;
            switch (requestCode) {
                case TAKE_PICUTRE_REQUEST_CODE:
                    //TODO: This is good for thumbnails but it's not good for full size images
                    image = (Bitmap) data.getExtras().get("data");
                    break;
                case PICK_PICTURE_REQUEST_CODE:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        if (selectedImage != null) {
                            try {
                                image = BitmapFactory.decodeStream(contentResolver
                                        .openInputStream(selectedImage));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
            }
            return image;
        }
        return null;
    }

    public static void requestImage(Fragment fragment) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(fragment.getContext());
        builder.setTitle(R.string.title_change_photo)
                .setCancelable(true)
                .setNegativeButton(R.string.lbl_cancel, (dialogInterface, i) -> dialogInterface.dismiss());
        builder.setItems(R.array.array_choose_photo, (dialogInterface, i) -> {
            switch (i) {
                case 0:
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    try {
                        if (ContextCompat.checkSelfPermission(fragment.getContext(),
                                Manifest.permission.CAMERA)
                                == PackageManager.PERMISSION_DENIED){
                            ActivityCompat.requestPermissions(fragment.getActivity(),
                                    new String[] {Manifest.permission.CAMERA},
                                    MY_CAMERA_REQUEST_CODE);
                            dialogInterface.dismiss();
                        }
                        else{
                            fragment.startActivityForResult(takePicture, TAKE_PICUTRE_REQUEST_CODE);
                        }

                    } catch (ActivityNotFoundException err) {
                        //TODO: Display to user error
                    }

                    break;
                case 1:
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    fragment.startActivityForResult(pickPhoto, PICK_PICTURE_REQUEST_CODE);
                    break;
            }
        });

        builder.show();
    }

    public static boolean handleRequestPermission(Fragment fragment, int requestCode,
                                                  @NonNull String[] permissions,
                                                  @NonNull int[] grantResults){
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                fragment.startActivityForResult(takePicture, TAKE_PICUTRE_REQUEST_CODE);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}
