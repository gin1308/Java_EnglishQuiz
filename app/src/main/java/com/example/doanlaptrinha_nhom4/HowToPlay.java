package com.example.doanlaptrinha_nhom4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ViewFlipper;

public class HowToPlay extends AppCompatActivity {

    ImageButton btnBack, btnNext, btnPre;
    ViewFlipper viewFlipper;
    int[] arrImage={R.drawable.guide_main, R.drawable.guide_level, R.drawable.guide_quiz, R.drawable.guide_score, R.drawable.guide_highscore};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_play);

        viewFlipper=(ViewFlipper)findViewById(R.id.viewFlipper);
        btnBack=(ImageButton)findViewById(R.id.btnback);
        btnNext=(ImageButton)findViewById(R.id.btnNext);
        btnPre=(ImageButton)findViewById(R.id.btnPre);

        for (int i=0;i<arrImage.length;i++){
            ImageView imageView=new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(arrImage[i]);
            viewFlipper.addView(imageView);
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(HowToPlay.this,MainActivity.class);
                startActivity(back);
            }
        });


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(viewFlipper.getDisplayedChild()==arrImage.length-1){
                    viewFlipper.stopFlipping();
                }
                else {
                    viewFlipper.showNext();
                }
            }
        });

        btnPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(viewFlipper.getDisplayedChild()==0){
                    viewFlipper.stopFlipping();
                }
                else {
                    viewFlipper.showPrevious();
                }
            }
        });
    }
}