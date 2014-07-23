package models.visualSearch;

import com.fasterxml.jackson.annotation.JsonBackReference;
import play.db.ebean.Model;
import play.libs.Json;

import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

@Entity
@Table (name="visual_search_quiz")
public class Quiz extends Model{
    @Id
    public long id;
    public double positionXofTarget;
    public double positionYofTarget;
    public ShapeType target = ShapeType.CIRCLE_BLUE;
    public int squareBlue;
    public int squareGreen;
    public int squareRed;
    public int circleGreen;
    public int circleRed;
    public FrameSize frameSize;
    @OneToOne
    public Question question;
    @OneToMany(cascade=CascadeType.REMOVE)
    public List<Answer> answers = new ArrayList<Answer>();
    @OneToOne(cascade = CascadeType.ALL)
    @JsonBackReference
    public Trial trial;

    public Quiz(){}
    public Quiz(Trial trial){
        this.trial = trial;
    }



    public static List<Quiz> findInvolving(Trial trial){
        return Quiz.find.where().eq("trial", trial).findList();
    }

    public String toJson(){
        return Json.stringify(Json.toJson(this));
    }

    public static String toJsonArray(List<Quiz> quizzes){
        String result = "[";
        for(int i=0;i< quizzes.size();i++){
            result += quizzes.get(i).toJson();
            if(i < quizzes.size() - 1){
                result += ",";
            }
        }
        result += "]";
        return result;

    }

    @SuppressWarnings("unchecked")
    public static Finder<Long, Quiz> find = new Finder(Long.class, Quiz.class);

    public static Quiz create(Trial trial) {
        Quiz quiz = new Quiz(trial);
        quiz.frameSize = FrameSize.MEDIUM;
        quiz.circleGreen = 5;
        quiz.circleRed = 5;
        quiz.squareBlue = 5;
        quiz.squareGreen = 5;
        quiz.squareRed = 5;
        Question question = new Question();
        question.save();
        quiz.question = question;
        return quiz;
    }

}
