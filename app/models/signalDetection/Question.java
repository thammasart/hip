package models.signalDetection;

import play.db.ebean.Model;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

@Entity
@Table (name="signal_detection_question")
public class Question extends Model{

    private static final String ENGLISH_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    @Id
    public long id;
    public char target;
    public char noise;

    @OneToOne(mappedBy = "question")
    private List<Quiz> quizzes = new ArrayList<Quiz>();

    public Question(){}

    public Question(char target,char noise){
        this.target = target;
        this.noise = noise;
    }

    public static Question create(){
        Random random = new Random();
        char target = ENGLISH_CASE.charAt(random.nextInt(ENGLISH_CASE.length()));
        char noise = ENGLISH_CASE.charAt(random.nextInt(ENGLISH_CASE.length()));
        return new Question(target, noise);
    }

    @SuppressWarnings("unchecked")
    public static Finder<Long, Question> find = new Finder(Long.class,Question.class);

}
