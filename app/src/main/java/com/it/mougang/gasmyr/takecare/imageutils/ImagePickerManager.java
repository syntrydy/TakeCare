package com.it.mougang.gasmyr.takecare.imageutils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by gasmyr.mougang on 12/12/16.
 */

public class ImagePickerManager extends PickerManager {

    public ImagePickerManager(Activity activity) {
        super(activity);
    }

    protected void sendToExternalApp( ){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");

        activity.startActivityForResult(Intent.createChooser(intent, "Select one image..."),
                REQUEST_CODE_SELECT_IMAGE);
    }

    @Override
    public void setUri(Uri uri)
    {
        mProcessingPhotoUri = uri;
    }

}