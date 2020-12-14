package com.example.doanlaptrinha_nhom4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class HighScore extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    TextView easyScore,normalScore,hardScore;
    ImageButton btnBack;
    Boolean checkSound;
    ArrayList<Score> listEasyScore,listNormalScore,listHardScore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);
        easyScore=(TextView)findViewById(R.id.TxtHighscoreEasy);
        normalScore=(TextView)findViewById(R.id.TxtHighscoreNormal);
        hardScore=(TextView)findViewById(R.id.TxtHighscoreHard);
        btnBack=(ImageButton)findViewById(R.id.BtnBack);
        listEasyScore=new ArrayList<Score>();
        listNormalScore=new ArrayList<Score>();
        listHardScore=new ArrayList<Score>();

        mediaPlayer = MediaPlayer.create(this, R.raw.result);

        ReceiveData();
        CheckSoundOnCreate();
        ReadFirebase();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back=new Intent(HighScore.this,MainActivity.class);
                startActivity(back);
                mediaPlayer.stop();
            }
        });
    }

    public void ReadFirebase()//Lấy High Score từ firebase
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("HeightScore");

        //So sánh điểm chế độ dễ
        myRef.child("Easy").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    Score easyScore=ds.getValue(Score.class);
                    listEasyScore.add(easyScore);
                    Log.e("Getdata", String.valueOf(listEasyScore.size()));
                }
                Collections.sort(listEasyScore);

                //check sort
                for(Score sc:listEasyScore)
                {
                    Log.e("Sort",sc.toString());
                }
                easyScore.setText(listEasyScore.get(0).toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //So sánh điểm chế độ trung bình
        myRef.child("Normal").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    Score normalScore=ds.getValue(Score.class);
                    listNormalScore.add(normalScore);
                    Log.e("Getdata", String.valueOf(listNormalScore.size()));
                }
                Collections.sort(listNormalScore);

                //check sort
                for(Score sc:listNormalScore)
                {
                    Log.e("Sort",sc.toString());
                }
                normalScore.setText(listNormalScore.get(0).toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //So sánh điểm chế độ khó
        myRef.child("Hard").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    Score hardScore=ds.getValue(Score.class);
                    listHardScore.add(hardScore);
                    Log.e("Getdata", String.valueOf(listHardScore.size()));
                }
                Collections.sort(listHardScore);

                //check sort
                for(Score sc:listHardScore)
                {
                    Log.e("Sort",sc.toString());
                }
                hardScore.setText(listHardScore.get(0).toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void CheckSoundOnCreate(){
        if(checkSound){
            mediaPlayer.start();
        }
    }
    public void ReceiveData(){
        Intent callerIntent=getIntent();
        Bundle packageFromCaller= callerIntent.getBundleExtra("SoundHighScore");
        checkSound=packageFromCaller.getBoolean("CheckSound");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.release();
    }
}