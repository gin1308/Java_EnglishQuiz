package com.example.doanlaptrinha_nhom4;

public class Score implements Comparable<Score>{
    public String name;
    public Integer score;
    public Score()
    {}
    public Score(String name,Integer score)
    {
        this.name=name;
        this.score=score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    @Override
    public String toString(){
        return "Name: " + name + " -" +" Score: "+ score;
    }
    @Override
    public int compareTo(Score score) {
        if(score.score==this.score)
        {
            return  this.getName().compareTo(score.getName());//so sánh theo bảng chữ cái từ a->z
        }
        return score.getScore()-this.getScore();
    }
}
