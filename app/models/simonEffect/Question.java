package models.simonEffect;

import play.db.ebean.*;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import play.db.ebean.Model.Finder;

@Entity
@Table (name = "simon_effect_question")
public class Question extends Model{

    public static final char[] alphabets = {'O', 'X'};
    public static final String[] colors = {"red", "green"};

    @Id
    public long id;
    public String color;
    public char alphabet;
    public String direction;
    @OneToMany(cascade=CascadeType.REMOVE,mappedBy="question")
    @JsonManagedReference
    public List<Quiz> quizzes;

    public Question(String color,char alphabet , String direction) {
    	this.color = color;
    	this.alphabet = alphabet;
    	this.direction = direction;
    }

	@SuppressWarnings("unchecked")
	public static Finder<Long, Question> find = new Finder(Long.class, Question.class);

    public Question() {

    }


    public static Question findQuestionByType(QuestionType questionType) {
        Random random = new Random();
        String alp = alphabets[random.nextInt(alphabets.length)] + "";
        String cl = "";
        if(questionType == QuestionType.ONEFEATURE){
            if(alp.equals("O")){
                cl = "green";
            }else if(alp.equals("X")){
                cl = "red";
            }
        }
        else if(questionType == QuestionType.TWOFEATURE){
            cl = colors[random.nextInt(colors.length)];
        }
        return find.where().eq("alphabet", alp).eq("color", cl).findUnique();
    }
}
