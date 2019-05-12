package com.example.ocrapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

public class SelectButtonHandler {

    public static final int PICK_IMAGE_CODE = 1;

    private final AppCompatActivity app;
    private final Button button;
    private final ImageView image;
    private final TextView console;

    public Uri imageUri;

    public SelectButtonHandler(final AppCompatActivity app, Button selectButton, ImageView textImage, TextView console) {
        this.app = app;
        this.button = selectButton;
        this.image = textImage;
        this.console = console;
        this.imageUri = null;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);

                app.startActivityForResult(gallery, PICK_IMAGE_CODE);
            }
        });
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public void changeImage(Intent data){
        imageUri = data.getData();

        try{
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(app.getContentResolver(), imageUri);
            image.setImageBitmap(bitmap);
            console.setTextColor(Color.RED);
            console.setText("Image uploaded successfully.");
        }

        catch(IOException e){
            e.printStackTrace();
            console.setTextColor(Color.RED);
            console.setText("An error occurred while trying to oprn the file. Please try again.");
        }



    }
}
