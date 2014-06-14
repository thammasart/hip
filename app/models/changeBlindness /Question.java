package models.changeBlindness;

import play.db.ebean.Model;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table (name="change_blindness_question")
public class Question extends Model{
    @Id
    public long id;
    public String pathOfPic1;
    public String pathOfPic2;
    public int positionOfChangeX;
    public int positionOfChangeY;
    @OneToMany
    public List<Quiz> quizzes = new ArrayList<Quiz>();

    public Question(){
        this.pathOfPic1 = "";
        this.pathOfPic2 = "";
        this.positionOfChangeX = 0;
        this.positionOfChangeY = 0;
    }

    public Question(String pathOfPic1, String pathOfPic2, int positionOfChangeX, int positionOfChangeY){
        this.pathOfPic1 = pathOfPic1;
        this.pathOfPic2 = pathOfPic2;
        this.positionOfChangeX = positionOfChangeX;
        this.positionOfChangeY = positionOfChangeY;
    }

    @SuppressWarnings("unchecked")
    public static Finder<Long, Question> find = new Finder(Long.class, Question.class);

}
