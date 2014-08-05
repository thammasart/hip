package models.changeBlindness;

import com.fasterxml.jackson.annotation.JsonBackReference;
import play.db.ebean.Model;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

@Entity
@Table (name="change_blindness_quiz")
public class Quiz extends Model{
    @Id
    public long id;
    @ManyToOne
    @JsonBackReference
    public Trial trial;
    @ManyToOne
    public Question question;
    @OneToMany(cascade=CascadeType.REMOVE, mappedBy = "quiz")
    private List<Answer> answers = new ArrayList<Answer>();

    public Quiz(){}

    public Quiz(Trial trial, Question question){
    	this.trial = trial;
    	this.question = question;
    }

    public List<Answer> findAnswers(){
        return answers;
    }

    @SuppressWarnings("unchecked")
    public static Finder<Long, Quiz> find = new Finder(Long.class, Quiz.class);

    public static Quiz create(Trial trial, List<Question> questions) {
        Quiz quiz = new Quiz();
        quiz.trial = trial;
        Question question = questions.get(new Random().nextInt(questions.size()));
        quiz.question = question;
        questions.remove(question);
        if(questions.size() == 0)
            questions.addAll(Question.find.all());
        return quiz;
    }
}
