package com.maldo.instacloning;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class UploadActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_PICK = 2;
    ImageView ivUpload;
    Bitmap bitmap;
    EditText etCaption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        ivUpload = (ImageView) findViewById(R.id.iv_upload);
        etCaption = (EditText) findViewById(R.id.etCaption);
    }

    public void BtnUploadCamera(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
            } else {
                GetCamera();
            }
        } else {
            GetCamera();
        }
    }

    public void BtnGallery(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_IMAGE_PICK);
            } else {
                GetGallery();
            }
        } else {
            GetGallery();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                GetCamera();
            }
        } else if (requestCode == REQUEST_IMAGE_PICK) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                GetGallery();
            }
        }
    }

    public void GetCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void GetGallery() {
        Intent takeGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (takeGalleryIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeGalleryIntent, REQUEST_IMAGE_PICK);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK && data != null) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                bitmap = imageBitmap;
                ivUpload.setImageBitmap(imageBitmap);
            } else {
                Toast.makeText(this, "yah :(", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_IMAGE_PICK) {
            if (resultCode == RESULT_OK && data != null) {
                Uri SelectedImage = data.getData();
                try {
                    Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), SelectedImage);
                    bitmap = imageBitmap;
                    ivUpload.setImageBitmap(imageBitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(UploadActivity.this, "Image Shared!", Toast.LENGTH_SHORT).show();
                }
            }else {
                    Toast.makeText(UploadActivity.this, "Image could not be shared - please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    public void BtnUpload (View view){
        if ( bitmap == null){
            Toast.makeText(this, "Take gambar dulu la bro", Toast.LENGTH_SHORT).show();
            return;
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        ParseFile file = new ParseFile("image.png", byteArray);


        ParseObject obj = new ParseObject("Gambar");
        obj.put("username", ParseUser.getCurrentUser().getUsername());
        obj.put("image", file);
        obj.put("caption", etCaption.getText().toString());

        obj.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e ==  null){
                    Toast.makeText(UploadActivity.this, "uyeeah", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UploadActivity.this, "yaelaah  -" +e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

