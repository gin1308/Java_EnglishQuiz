package com.example.doanlaptrinha_nhom4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class Level extends AppCompatActivity {

    Button btnEasy, btnNormal, btnHard;
    ImageButton btnBack;
    int NoQuestion;
    ImageView btnSpeaker;
    Boolean checkSpeaker;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level2);

        mediaPlayer = MediaPlayer.create(this, R.raw.chooselevel);

        ReceiveData();

        //Chọn chế độ dễ
        btnEasy = (Button) findViewById(R.id.BtnEasy);
        btnEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransferData((String) btnEasy.getText());
                mediaPlayer.stop();
            }
        });

        //chọn chế độ bình thường
        btnNormal = (Button) findViewById(R.id.BtnNormal);
        btnNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransferData((String) btnNormal.getText());
                mediaPlayer.stop();
            }
        });

        //chọn chế độ khó
        btnHard = (Button) findViewById(R.id.BtnHard);
        btnHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransferData((String) btnHard.getText());
                mediaPlayer.stop();
            }
        });

        //quay lại main activity
        btnBack = (ImageButton) findViewById(R.id.BtnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                Intent mainmenu = new Intent(Level.this, MainActivity.class);
                bundle.putBoolean("CheckSpeaker", checkSpeaker);
                mainmenu.putExtra("MainSound", bundle);
                startActivity(mainmenu);
                mediaPlayer.stop();
            }
        });

        //Chỉnh hình ảnh khi nhấn nút Speaker
        btnSpeaker = (ImageView) findViewById(R.id.bgSpeaker);
        CheckSoundOnCreate();
        btnSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSpeaker) {
                    btnSpeaker.setImageResource(R.drawable.speakermute);
                    checkSpeaker = !checkSpeaker;
                    mediaPlayer.pause();
                } else {
                    btnSpeaker.setImageResource(R.drawable.speaker);
                    checkSpeaker = !checkSpeaker;
                    mediaPlayer.start();
                }
                SaveSound();
            }
        });
    }

    //Kiểm tra âm thanh khi vào activity
    public void CheckSoundOnCreate() {
        if (checkSpeaker) {
            btnSpeaker.setImageResource(R.drawable.speaker);
            mediaPlayer.start();
        } else {
            btnSpeaker.setImageResource(R.drawable.speakermute);
        }
    }

    //nhận dữ liệu từ main activity
    public void ReceiveData() {
        Intent callerIntent = getIntent();
        Bundle packageFromCaller = callerIntent.getBundleExtra("QuestionPackage");
        NoQuestion = packageFromCaller.getInt("NoQ");
        checkSpeaker = packageFromCaller.getBoolean("Speaker");
        Log.e("Note", "No.Q" + NoQuestion);
    }

    //chuyển dữ liệu sang Quiz activity
    public void TransferData(String data) {
        Intent quiz = new Intent(Level.this, Quiz.class);
        //Log.e("Note","Txt: "+btnEasy.getText());
        Bundle bundle = new Bundle();
        bundle.putString("Level", data);
        bundle.putInt("NoQ", NoQuestion);
        bundle.putBoolean("CheckSpeaker", checkSpeaker);
        quiz.putExtra("MyPackage", bundle);
        startActivity(quiz);
    }

    public void SaveSound(){
        SharedPreferences sharedPreferences=getSharedPreferences("Sound", MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putBoolean("CheckSound",checkSpeaker);
        editor.apply();
    }

    //tắt nhạc khi thoát App
    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.release();
    }
}