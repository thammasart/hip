package models.attentionBlink;

import play.db.ebean.Model;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table (name="sttention_blink_question")
public class Question extends Model{
    @Id
    public long id;
    public String questionLetter;
    public String charactorSet;
    public String correctAnswer;
    public String charactorType;

    @OneToMany
    public List<Quiz> quizzes;

    public Question(){
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
