package models.stroopEffect;

import play.db.ebean.Model;

import javax.persistence.*;
import models.User;

@Entity
@Table (name="stroop_answer")
public class Answer extends Model{

    @Id
    public long answer_id;
    @Column(length=20)
    public String answer;
    public double used_time;

    @ManyToOne
    public User user;
    @ManyToOne
    public Quiz quiz;

    public Answer(String answer,double usedTime){
        this.answer = answer;
        this.used_time = usedTime;
    }

    public static Answer create(String answer, double usedTime,User user, Quiz quiz){
        Answer newAnswer = new Answer(answer,usedTime);
        newAnswer.user = user;
        newAnswer.quiz = quiz;
        return newAnswer;
    }

    public static Finder<Long,Answer> find = new Finder(Long.class,Answer.class);

}
