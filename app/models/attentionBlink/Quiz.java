package models.attentionBlink;

import play.db.ebean.*;

import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

import play.db.ebean.Model.Finder;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "attention_blink_quiz")
public class Quiz extends Model {

    @Id
    public long id;
    public int length = 13;
    public int numberOfTarget = 2;
    public double blinkTime = 0.1;
    public boolean isCorrect = true;

    @ManyToOne
    @JsonBackReference
    public Trial trial;

    @OneToOne(cascade = CascadeType.REMOVE)
    public Question question;

    @OneToMany(cascade = CascadeType.REMOVE)
    private List<Answer> answers = new ArrayList<Answer>();

    public static Quiz create(Trial trial) {
        Quiz quiz = new Quiz();
        Question question = Question.generateQuestion(trial.questionType, quiz.length,
                quiz.numberOfTarget, quiz.isCorrect);
        question.save();
        quiz.question = question;
        quiz.trial = trial;
        return quiz;
    }

    public List<Answer> findAnswers() {
        return answers;
    }

    @SuppressWarnings("unchecked")
    public static Finder<Long, Quiz> find = new Finder(Long.class, Quiz.class);
}
