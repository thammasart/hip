package models.stroopEffect;

import play.db.ebean.Model;

import javax.persistence.*;

import java.util.List;
import java.util.ArrayList;

@Entity
@Table (name="stroop_engword_question")
public class Question extends Model{
    @Id
    public long id;
    @Column(nullable=false, length=20)
    public String colorWord;
    @Column(nullable=false, length=20)
    public String inkColor;

    @OneToMany
    public List<Quiz> quizzes;

    public Question(){
        colorWord = "Black";
        inkColor = "black";
    }

    public Question(String colorWord, String inkColor){
        colorWord = colorWord;
        this.inkColor = inkColor;
    }

    public static List<Question> findInvoving(List<Quiz> quizzes){
        List<Question> questions = new ArrayList<Question>();
        for(Quiz quiz : quizzes){
            questions.add(quiz.question);
        }
        return questions;
    }

    @SuppressWarnings("unchecked")
    public static Finder<Long, Question> find = new Finder(Long.class,Question.class);

}
