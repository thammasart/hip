package models.attentionBlink;

import play.db.ebean.Model;
import javax.persistence.*;

@Entity
@Table (name="attention_blink_quiz")
public class Quiz extends Model{
    @Id
    public long id;

    @ManyToOne
    public Trial trial;
    @ManyToOne
    public Question question;
    @OneToMany
    public List<Answer> answers;

    public static Quiz create(Trial trial, Question question){
        Quiz quiz = new Quiz();
        quiz.trial = trial;
        quiz.question = question;
        return quiz;
    }

    @SuppressWarnings("unchecked")
    public static Finder<Long,Quiz> find = new Finder(Long.class,Quiz.class);

}
