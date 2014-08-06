package models.stroopEffect;

import play.db.ebean.Model;

import javax.persistence.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

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

    @OneToMany(cascade=CascadeType.REMOVE, mappedBy = "question")
    private List<Quiz> quizzes;

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

        String colorWord = "";

        if(this.questionType == QuestionType.ENGLISH){
            colorWord = this.colorWord;
        }else if(this.questionType == QuestionType.THAI){
            colorWord = findEnglishColorWord(this.colorWord);
        }
        return colorWord.equalsIgnoreCase(inkColor);
    }

    public String findEnglishColorWord(String colorWordTH){
        final String BLACK_TH = "ดำ";
        final String BLUE_TH = "น้ำเงิน";
        final String RED_TH = "แดง";
        final String GREEN_TH = "เขียว";
        final String PURPLE_TH = "ม่วง";
        final String YELLOW_TH = "เหลือง";
        final String BLACK = "black";
        final String BLUE = "blue";
        final String RED = "red";
        final String GREEN = "green";
        final String PURPLE = "purple";
        final String YELLOW = "yellow";
        String colorWord = "";
        if (colorWordTH.equals(BLACK_TH)){
            colorWord = BLACK;
        }
        else if(colorWordTH.equals(BLUE_TH)){
            colorWord = BLUE;
        }
        else if(colorWordTH.equals(RED_TH)){
            colorWord = RED;
        }
        else if(colorWordTH.equals(GREEN_TH)){
            colorWord = GREEN;
        }
        else if(colorWordTH.equals(PURPLE_TH)){
            colorWord = PURPLE;
        }
        else if(colorWordTH.equals(YELLOW_TH)){
            colorWord = YELLOW;
        }
        return colorWord;
    }

    public static List<Question> findAllMatchQuestion(QuestionType questionType){
        List<Question> questions = find.where().eq("questionType", questionType).findList();
        List<Question> matchQuestions = new ArrayList<Question>();
        for(Question question : questions){
            if(question.isMatch()){
                matchQuestions.add(question);
            }
        }
        return matchQuestions;
    }
    public static List<Question> findAllNotMatchQuestion(QuestionType questionType){
        List<Question> questions = find.where().eq("questionType", questionType).findList();
        List<Question> notMatchQuestions = new ArrayList<Question>();
        for(Question question : questions){
            if(!question.isMatch()){
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
