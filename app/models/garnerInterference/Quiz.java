package models.garnerInterference;

import com.fasterxml.jackson.annotation.JsonBackReference;
import play.db.ebean.Model;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table (name="garner_interference_quiz")
public class Quiz extends Model{
    @Id
    public long id;
    public QuestionType questionType = QuestionType.COLOR; // color or size or both


    @ManyToOne
    @JsonBackReference
    public Trial trial;
    @ManyToOne
    public Question question;
    @OneToMany(cascade=CascadeType.REMOVE)
    public List<Answer> answers = new ArrayList<Answer>();

    public Quiz(){}

    public Quiz(Trial trial, Question question){
    	this.trial = trial;
    	this.question = question;
    }


    @SuppressWarnings("unchecked")
    public static Finder<Long, Quiz> find = new Finder(Long.class, Quiz.class);

    public static Quiz create(Trial trial, QuestionType questionType, boolean isNotFake) {
        Quiz quiz = new Quiz();
        quiz.trial = trial;
        quiz.questionType = questionType;
        Question question = Question.create(questionType, isNotFake);
        question.save();
        quiz.question = question;
        return quiz;
    }
}
