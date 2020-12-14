package com.example.doanlaptrinha_nhom4;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Result extends AppCompatActivity {
    TextView txtResult;
    EditText txtName;
    Button btnSaveScore;
    ImageButton btnRestart;
    String level;
    Score newScore;
    Boolean checkSound;
    String test="alo";
    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result2);
        txtResult=(TextView)findViewById(R.id.TxtResult);
        txtName=(EditText)findViewById(R.id.TxtName);
        btnSaveScore=(Button)findViewById(R.id.BtnSave);
        newScore=new Score();

        mediaPlayer = MediaPlayer.create(this,R.raw.audienceclapping);


        ReceiveData();
        CheckSoundOnCreate();

        btnSaveScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtName.getText().toString().trim().equals(""))
                {
                    Toast.makeText(Result.this,"Enter your name",Toast.LENGTH_SHORT).show();
                }
                else {
                    if(!isConnected(Result.this)){
                        Toast.makeText(Result.this,"NetWork Error",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        WriteData();
                        Toast.makeText(Result.this,"Save Score Successful!",Toast.LENGTH_SHORT).show();
                        txtName.setEnabled(false);
                        btnSaveScore.setEnabled(false);
                    }
                }
            }
        });

        btnRestart=(ImageButton)findViewById(R.id.Restart);
        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent restart=new Intent(Result.this,MainActivity.class);
                startActivity(restart);
                mediaPlayer.stop();
            }
        });
    }

    private boolean isConnected(Result result) {
        ConnectivityManager connectivityManager =  (ConnectivityManager) result.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiCon=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobbilConn=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if((wifiCon!=null&&wifiCon.isConnected())||(mobbilConn!=null&&mobbilConn.isConnected())){
            return true;
        }
        else {
            return false;
        }
    }

    public void WriteData()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("HeightScore").child(level);
        String name=txtName.getText().toString().trim();
        Integer score=Integer.parseInt(txtResult.getText().toString().trim());
        newScore.setName(name);
        newScore.setScore(score);
        myRef.push().setValue(newScore);
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Exit").setMessage("Are you sure you want to exit");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        }).setNegativeButton("No",null).show();
    }

    public void CheckSoundOnCreate(){
        if(checkSound){
            mediaPlayer.start();
            mediaPlayer.setLooping(true);
        }
        SaveSound();
    }

    public void ReceiveData(){
        Intent callerIntent=getIntent();
        Bundle packageFromCaller= callerIntent.getBundleExtra("ResultPackage");
        txtResult.setText(String.valueOf(packageFromCaller.getInt("Score")));
        level=packageFromCaller.getString("Level");
        checkSound=packageFromCaller.getBoolean("CheckSpeaker");
    }

    public void SaveSound(){
        SharedPreferences sharedPreferences=getSharedPreferences("Sound", MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putBoolean("CheckSound",checkSound);
        editor.apply();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.release();
    }
}