package nightout.app.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import nightout.app.R;

public class PictureActivity extends AppCompatActivity {

    public static final int GALLERY_REQUEST_CODE = 100;
    public static final int CAMERA_REQUEST_CODE = 200;
    public static final float PREFERRED_WIDTH = 300;
    public static final float PREFERRED_HEIGHT = 250;

    private ImageView selectedImageView;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        this.selectedImageView = findViewById(R.id.ivPicture);
        Button btnCamera = findViewById(R.id.btnCamera);
        Button btnGallery = findViewById(R.id.btnGallery);
        Button btnSaveImage = findViewById(R.id.btnSaveImage);

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select picture"), GALLERY_REQUEST_CODE);
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(intentCamera.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intentCamera, CAMERA_REQUEST_CODE);
                }
            }
        });

        btnSaveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String image = bitmapToString(resizeBitmap(bitmap));
                Intent i = new Intent();
                i.putExtra("image", image);

                setResult(RESULT_OK, i);

                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                Uri selectedImage = data.getData();
                InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                bitmap = BitmapFactory.decodeStream(imageStream);
                selectedImageView.setImageBitmap(bitmap);


            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        if(requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data != null ? data.getExtras() : null;
            Bitmap image = (Bitmap)extras.get("data");
            selectedImageView.setImageBitmap(image);
        }
    }

    private static String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static Bitmap resizeBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = PREFERRED_WIDTH / width;
        float scaleHeight = PREFERRED_HEIGHT / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        bitmap.recycle();
        return resizedBitmap;
    }
}