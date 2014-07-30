package models.stroopEffect;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

import javax.persistence.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table (name="stroop_quiz")
public class Quiz extends Model{
    @Id
    public long id;

    @ManyToOne
    @JsonBackReference
    public Trial trial;
    @ManyToOne
    @JsonManagedReference
    public Question question;
    @OneToMany(cascade=CascadeType.REMOVE)
    @JsonBackReference
    public List<Answer> answers = new ArrayList<Answer>();

    public static Quiz create(Trial trial,Question question){
        Quiz quiz = new Quiz();
        quiz.trial = trial;
        quiz.question = question;
        return quiz;
    }

    public void switchRandomQuestion(){
        Random random = new Random();
        if(!question.isMatch()){
            List<Question> questions = Question.findAllMatchQuestion(this.trial.questionType);
            int index = random.nextInt(questions.size());
            question = questions.get(index);
        }else{
            List<Question> questions = Question.findAllNotMatchQuestion(this.trial.questionType);
            int index = random.nextInt(questions.size());
            question = questions.get(index);
        }
        update();
    }

    public void randomToNewQuestion(List<Question> questions){
        Random random = new Random();
        int index = random.nextInt(questions.size());
        question = questions.get(index);
        update();
    }

    @SuppressWarnings("unchecked")
    public static Finder<Long,Quiz> find = new Finder(Long.class,Quiz.class);

}
