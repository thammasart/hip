package models.stroopEffect;

import play.db.ebean.Model;

import javax.persistence.*;

import java.util.List;
import java.util.ArrayList;

@Entity
@Table (name="stroop_engword_question")
public class Question extends Model{
    @Id
    public long question_id;
    @Column(nullable=false, length=20)
    public String color_word;
    @Column(nullable=false, length=20)
    public String ink_color;

    @OneToMany
    public List<Quiz> quizzes;

    public Question(){
        color_word = "";
        ink_color = "black";
    }

    public Question(String colorWord, String inkColor){
        color_word = colorWord;
        ink_color = inkColor;
    }

    public static Finder<Long, Question> find = new Finder(Long.class,Question.class);

}
