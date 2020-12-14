package com.example.doanlaptrinha_nhom4;

public class Question {
    public String question;

    public String answera;
    public String answerb;
    public String answerc;
    public String answerd;
    public String answer;


    public Question() {

    }

    public Question(String question, String answerA, String answerB, String answerC, String answerD, String rightAnswer) {
        this.question = question;
        this.answera = answerA;
        this.answerb = answerB;
        this.answerc = answerC;
        this.answerd = answerD;
        this.answer = rightAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswera() {
        return answera;
    }

    public void setAnswera(String answera) {
        this.answera = answera;
    }

    public String getAnswerb() {
        return answerb;
    }

    public void setAnswerb(String answerb) {
        this.answerb = answerb;
    }

    public String getAnswerc() {
        return answerc;
    }

    public void setAnswerc(String answerc) {
        this.answerc = answerc;
    }

    public String getAnswerd() {
        return answerd;
    }

    public void setAnswerd(String answerd) {
        this.answerd = answerd;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
  }
}


