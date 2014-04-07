package models.stroopEffect;

import play.db.ebean.Model;

import javax.persistence.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import play.db.ebean.Model.Finder;

@Entity
@Table (name="stroop_engword_question")
public class Question extends Model{

    public static final String[] colors = { "black", "blue", "red", "green", "purple", "yellow"};
    public static final String[] colorsTH = { "ดำ", "น้ำเงิน", "แดง", "เขียว", "ม่วง", "เหลือง"};

    @Id
    public long id;
    @Column(nullable=false, length=20)
    public String colorWord;
    @Column(nullable=false, length=20)
    public String inkColor;

    public QuestionType questionType = QuestionType.ENGLISH;

    @OneToMany
    public List<Quiz> quizzes;

    public Question(){
        colorWord = "Black";
        inkColor = "black";
    }

    public Question(String colorWord, String inkColor){
        this.colorWord = colorWord;
        this.inkColor = inkColor;
    }

    public static List<Question> findInvoving(List<Quiz> quizzes){
        List<Question> questions = new ArrayList<Question>();
        for(Quiz quiz : quizzes){
            questions.add(quiz.question);
        }
        return questions;
    }

    public boolean isMatch(){
        return colorWord.equalsIgnoreCase(inkColor);
    }

    public static List<Question> findAllMatchQuestion(QuestionType questionType){
        List<Question> questions = find.where().eq("questionType", questionType).findList();
        List<Question> matchQuestions = new ArrayList<Question>();
        for(Question question : questions){
            if(question.colorWord.equalsIgnoreCase(question.inkColor)){
                matchQuestions.add(question);
            }
        }
        return matchQuestions;
    }
    public static List<Question> findAllNotMatchQuestion(QuestionType questionType){
        List<Question> questions = find.where().eq("questionType", questionType).findList();
        List<Question> notMatchQuestions = new ArrayList<Question>();
        for(Question question : questions){
            if((!question.colorWord.equalsIgnoreCase(question.inkColor))){
                notMatchQuestions.add(question);
            }
        }
        return notMatchQuestions;
    }

    public static Question randomNewQuestion(List<Question> questions){
        Random random = new Random();
        int index = random.nextInt(questions.size());
        return questions.get(index);
    }

    @SuppressWarnings("unchecked")
    public static Finder<Long, Question> find = new Finder(Long.class,Question.class);

}
