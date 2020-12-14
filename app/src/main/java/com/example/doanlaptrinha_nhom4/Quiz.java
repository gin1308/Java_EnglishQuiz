package com.example.doanlaptrinha_nhom4;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;



public class Quiz extends AppCompatActivity {

    public MediaPlayer mediaPlayer;

    RadioGroup RG;
    RadioButton RbA,RbB,RbC,RbD;
    ImageButton btnAnswer;
    TextView txtQuestion,txtScore,txtNoQuestion,txtTime;
    ImageView btnSpeaker;
    Boolean checkSpeaker;
    Random rd=new Random();
    int pos=0;
    int score=0;
    int noQuestion=1;
    int totalQuestion;
    ArrayList<Question> lquestion;
    Question question;
    String level;
    String answer="";
    CountDownTimer countDownTimer;
    long timeLeft=20000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        mediaPlayer = MediaPlayer.create(this,R.raw.minutetowinit);

        //Ánh xạ
        txtQuestion=(TextView)findViewById(R.id.TxTQuestion);
        txtScore=(TextView)findViewById(R.id.txtScore);
        txtNoQuestion=(TextView)findViewById(R.id.TxtNumberQuestion);
        txtTime=(TextView)findViewById(R.id.TxTTime);
        RG=(RadioGroup)findViewById(R.id.RadioGroupAnswer);
        RbA=(RadioButton)findViewById(R.id.RbA);
        RbB=(RadioButton)findViewById(R.id.RbB);
        RbC=(RadioButton)findViewById(R.id.RbC);
        RbD=(RadioButton)findViewById(R.id.RbD);
        btnAnswer=(ImageButton)findViewById(R.id.BtAnswer);
        lquestion=new ArrayList<>();
        question=new Question();

        ReceiveData();
        ReadFirebase();
        StartTime();

        //kiểm tra câu trả lời đúng
        btnAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int idCheck=RG.getCheckedRadioButtonId();
                //kiểm tra theo id của radiobutton,
                // nếu text của radiobutton trùng khớp với đáp án thì điểm và số câu hỏi sẽ tăng lên 1,
                // nếu không chỉ có số câu hỏi tăng
                //nếu không chọn câu trả lời mà chọn nút answer thì số câu hỏi sẽ tăng lên 1
                switch (idCheck)
                {
                    case R.id.RbA:
                        if(RbA.getText().equals(answer))
                        {
                            score++;
                            noQuestion++;
                        }
                        else {
                            noQuestion++;
                        }
                        break;
                    case R.id.RbB:
                        if(RbB.getText().equals(answer))
                        {
                            score++;
                            noQuestion++;
                        }
                        else {
                            noQuestion++;
                        }
                        break;
                    case R.id.RbC:
                        if(RbC.getText().equals(answer))
                        {
                            score++;
                            noQuestion++;
                        }
                        else {
                            noQuestion++;
                        }
                        break;
                    case R.id.RbD:
                        if(RbD.getText().equals(answer))
                        {
                            score++;
                            noQuestion++;
                        }
                        else {
                            noQuestion++;
                        }
                        break;
                    default:
                        noQuestion++;
                        break;
                }

                
                if(noQuestion>totalQuestion)
                {
                    DataTransfer();
                    countDownTimer.cancel();
                }
                else{
                    if(lquestion.size()==0)//Khi hết câu hổi trong danh sách sẽ đọc lại data
                    {
                        noQuestion = 1;
                        ReadFirebase();//lấy lại dữ liệu từ firebase
                        Toast.makeText(Quiz.this, "NETWORK ERROR", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Display();
                    }
                }
            }
        });

        btnSpeaker=(ImageView)findViewById(R.id.bgSpeaker);
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
            }
        });
    }

    public void ReadFirebase()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Question");
        myRef.child(level).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds:dataSnapshot.getChildren()){
                        Question question=ds.getValue(Question.class);
                        lquestion.add(question);
                    }
                    Display();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void Display()
    {
        //random question
        pos = rd.nextInt(lquestion.size());

        //gắn dữ liệu
        txtQuestion.setText(lquestion.get(pos).getQuestion());
        answer = lquestion.get(pos).getAnswer();
        String answerA = lquestion.get(pos).getAnswera();
        String answerB = lquestion.get(pos).getAnswerb();
        String answerC = lquestion.get(pos).getAnswerc();
        String answerD = lquestion.get(pos).getAnswerd();

        //random vị trí câu trả lời
        ArrayList<String> rdAnswer = new ArrayList<>();
        rdAnswer.add(answerA);
        rdAnswer.add(answerB);
        rdAnswer.add(answerC);
        rdAnswer.add(answerD);
        Collections.shuffle(rdAnswer);

        //hiện câu trả lời
        RbA.setText(rdAnswer.get(0));
        RbB.setText(rdAnswer.get(1));
        RbC.setText(rdAnswer.get(2));
        RbD.setText(rdAnswer.get(3));

        txtScore.setText("" + score + "");
        txtNoQuestion.setText("" + noQuestion + "/" + totalQuestion);
        lquestion.remove(pos);
        RG.clearCheck();
    }

    public void StartTime()
    {
        if(totalQuestion==5)
        {
            timeLeft=61000;
        }
        else if (totalQuestion==6)
        {
            timeLeft=71000;
        }
        else if(totalQuestion==7)
        {
            timeLeft=81000;
        }
        else if(totalQuestion==8)
        {
            timeLeft=91000;
        }
        else if(totalQuestion==9)
        {
            timeLeft=110000;
        }
        else if(totalQuestion==10)
        {
            timeLeft=120000;
        }
        countDownTimer=new CountDownTimer(timeLeft,1000) {
            @Override
            public void onTick(long l) {
                timeLeft=l;
                txtTime.setText(String.valueOf(timeLeft/1000));
            }
            @Override
            public void onFinish() {
                timeLeft=0;
                DataTransfer();
            }
        }.start();
    }

    public void CheckSoundOnCreate(){
        if(checkSpeaker){
            btnSpeaker.setImageResource(R.drawable.speaker);
            mediaPlayer.start();
        }
        else {
            btnSpeaker.setImageResource(R.drawable.speakermute);
        }
    }

    public void ReceiveData(){
        Intent callerIntent=getIntent();
        Bundle packageFromCaller= callerIntent.getBundleExtra("MyPackage");
        level=packageFromCaller.getString("Level");
        totalQuestion=packageFromCaller.getInt("NoQ");
        checkSpeaker=packageFromCaller.getBoolean("CheckSpeaker");
        Log.e("Note","Level: "+level);
        Log.e("Note","No.Q"+String.valueOf(totalQuestion));
    }

    public void DataTransfer()// chuyển dữ liệu từ quiz sang result
    {
        Intent result = new Intent(Quiz.this,Result.class);
        Bundle bundle = new Bundle();
        bundle.putInt("Score",score);
        bundle.putString("Level",level);
        bundle.putBoolean("CheckSpeaker",checkSpeaker);
        result.putExtra("ResultPackage",bundle);
        startActivity(result);
        mediaPlayer.stop();
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

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.release();
    }
}