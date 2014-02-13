package models.stroopEffect;

import play.db.ebean.Model;

import javax.persistence.*;

import java.util.List;
import java.util.ArrayList;

@Entity
@Table (name="stroop_quiz")
public class Quiz extends Model{
    @Id
    public long id;

    @ManyToOne
    public Trial trial;
    @ManyToOne
    public Question question;
    @OneToMany
    public List<Answer> answers;

    public static Quiz create(Trial trial,Question question){
        Quiz quiz = new Quiz();
        quiz.trial = trial;
        quiz.question = question;
        return quiz;
    }

    @SuppressWarnings("unchecked")
    public static Finder<Long,Quiz> find = new Finder(Long.class,Quiz.class);

}
