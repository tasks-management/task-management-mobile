package com.duongll.succotask.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;

import com.duongll.succotask.R;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class FinishDialogFragment extends DialogFragment {

    private final int REQUEST_IMAGE_CAPTURE = 1;
    private final int REQUEST_IMAGE_GALLERY = 2;

    private Button btnCapture, btnGallery;
    private ImageView imageView;
    private Bitmap bitmap;
    private OnSuccess onSuccessCallback;
    private DialogInterface.OnCancelListener cancelListener;

    public interface OnSuccess {
        void onSuccess(Bitmap bitmap);
        void onCancel(boolean cancelable);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }

    public FinishDialogFragment() {
        // Required empty public constructor
    }

    public void setOnSuccessCallback(OnSuccess onSuccessCallback) {
        this.onSuccessCallback = onSuccessCallback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.fragment_finish_dialog, null, false);
        imageView = dialogView.findViewById(R.id.imageSubmitted);
        btnCapture = dialogView.findViewById(R.id.btnCapture);
        btnGallery = dialogView.findViewById(R.id.btnUpload);
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(captureIntent, REQUEST_IMAGE_CAPTURE);
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, REQUEST_IMAGE_GALLERY);
            }
        });
        builder.setView(dialogView).setPositiveButton("Upload", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onSuccessCallback.onSuccess(bitmap);
            }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onSuccessCallback.onCancel(true);
            }
        });
        return builder.create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
        } else if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK){
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), data.getData());
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
