package com.example.doanlaptrinha_nhom4;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    Button btnStart, btnScore, btnHowToPlay ,btnExit;
    Spinner spinner;
    String array[] = {"5 QUESTION", "6 QUESTION", "7 QUESTION", "8 QUESTION", "9 QUESTION", "10 QUESTION"};
    ImageView btnSpeaker;
    Boolean checkSpeaker=true;
    String test="";
    int pos;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //tạo âm thanh
        mediaPlayer = MediaPlayer.create(this, R.raw.maintheme);

        spinner = (Spinner) findViewById(R.id.spinerNoQuestion);
        CreateSpiner();

        //Nút chuyển sang Activity Level
        btnStart = (Button) findViewById(R.id.BtnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                Intent start = new Intent(MainActivity.this, Level.class);
                //Đưa dữ liệu số câu hỏi vào bundle
                switch (pos) {
                    case 0:
                        //Đưa dữ liệu vào Bundle
                        bundle.putInt("NoQ", 5);
                        break;
                    case 1:
                        //Đưa dữ liệu vào Bundle
                        bundle.putInt("NoQ", 6);
                        break;
                    case 2:
                        //Đưa dữ liệu vào Bundle
                        bundle.putInt("NoQ", 7);
                        break;
                    case 3:
                        //Đưa dữ liệu vào Bundle
                        bundle.putInt("NoQ", 8);
                        break;
                    case 4:
                        //Đưa dữ liệu vào Bundle
                        bundle.putInt("NoQ", 9);
                        break;
                    case 5:
                        //Đưa dữ liệu vào Bundle
                        bundle.putInt("NoQ", 10);
                        break;
                }
                bundle.putBoolean("Speaker", checkSpeaker);
                start.putExtra("QuestionPackage", bundle);
                startActivity(start);
                mediaPlayer.stop();
            }
        });
        //Nút chuyển sang Activity HighScore
        btnScore = (Button) findViewById(R.id.BtnScore);
        btnScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                Intent highscore = new Intent(MainActivity.this, HighScore.class);
                bundle.putBoolean("CheckSound", checkSpeaker);
                highscore.putExtra("SoundHighScore", bundle);
                startActivity(highscore);
                mediaPlayer.stop();
            }
        });

        //Nút chuyển sang Hướng dẫn chơi
        btnHowToPlay=(Button)findViewById(R.id.BtnHowToPlay);
        btnHowToPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent howPlay=new Intent(MainActivity.this,HowToPlay.class);
                startActivity(howPlay);
            }
        });


        //Nút thoát chương trình
        btnExit = (Button) findViewById(R.id.BtnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });

        LoadSound(checkSpeaker);
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
                LoadSound(checkSpeaker);
            }

        });

    }

    //Tạo Spinner
    public void CreateSpiner() {
        //Gán mảng Arr (data source) vào Adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.itemspinshow, array);
        //Thiết lập cách hiển thị dữ liệu trong spinner
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        //Thiết lập adapter cho spinner
        spinner.setAdapter(adapter);
        //Bắt sự kiện khi lựa chọn cho spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pos = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                pos = 0;
            }
        });
    }

    public void SaveSound(){
        SharedPreferences sharedPreferences=getSharedPreferences("Sound", MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putBoolean("CheckSound",checkSpeaker);
        editor.apply();
    }

    public void LoadSound(boolean check){
        SharedPreferences sharedPreferences=getSharedPreferences("Sound",MODE_PRIVATE);
        if(sharedPreferences!=null){
            checkSpeaker=sharedPreferences.getBoolean("CheckSound",check);
        }else {
            checkSpeaker=sharedPreferences.getBoolean("CheckSound",true);
        }
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

    //tắt nhạc khi thoát App
    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.release();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPreferences=getSharedPreferences("Sound", MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit().clear();
        editor.apply();
    }
}