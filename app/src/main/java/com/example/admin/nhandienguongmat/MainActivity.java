package com.example.admin.nhandienguongmat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.Toast;
import java.util.regex.*;


public class MainActivity extends AppCompatActivity {
    ImageView imvSang;
    Button btnNhanDien;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Init();

        final Bitmap mybitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.sang);
        imvSang.setImageBitmap(mybitmap);

        final Paint rectPain = new Paint();
        rectPain.setStrokeWidth(3);// độ dầy của đường viền
        rectPain.setColor(Color.WHITE);// định dạng color vòng nhận diện
        rectPain.setStyle(Paint.Style.STROKE);// đường viền bao quanh vùng nhận diện ko để làm mất face
        // STROKE là 1 đường viền bao quanh vùng được chọn

        final Bitmap tempbitmap = Bitmap.createBitmap(mybitmap.getWidth(),mybitmap.getHeight(),Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(tempbitmap);
        canvas.drawBitmap(mybitmap,0,0,null);

        btnNhanDien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               FaceDetector faceDetector = new FaceDetector.Builder(getApplicationContext())
                       .setTrackingEnabled(false)
                       .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                       .setMode(FaceDetector.FAST_MODE)
                       .build();

               if (!faceDetector.isOperational()){// nếu faceDetector không hoạt động
                   Toast.makeText(MainActivity.this, "Face Detector cout not be set up on your device", Toast.LENGTH_SHORT).show();
                   return;
               }
                Frame frame = new Frame.Builder().setBitmap(mybitmap).build();
                SparseArray<Face> sparseArray = faceDetector.detect(frame);
                for (int i =0;i < sparseArray.size();i++){
                    Face face = sparseArray.get(i);

                    float x1 = face.getPosition().x;
                    float y1 = face.getPosition().y;
                    float x2 = x1 + face.getWidth();
                    float y2 = y1 +face.getHeight();

                    RectF rectF = new RectF(x1,y1,x2,y2);
                    canvas.drawRoundRect(rectF,2,2,rectPain);
                }

                imvSang.setImageDrawable( new BitmapDrawable(getResources(),tempbitmap));
            }
        });
    }

    private void Init() {
        imvSang= (ImageView) findViewById(R.id.imvSang);
        btnNhanDien= (Button) findViewById(R.id.btnNhanDien);
    }
}
