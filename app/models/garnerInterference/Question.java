package models.garnerInterference;

import play.db.ebean.Model;
import javax.persistence.*;

@Entity
@Table (name="garner_interference_question")
public class Question extends Model{
    @Id
    public long id;
    public String colorPic;
    public String sizePic;
    public String colorQuestion;
    public String sizeQuestion;
    public boolean colorMatch;
    public boolean sizeMatch;
    
    public Question(){
    	
    }

    @SuppressWarnings("unchecked")
    public static Finder<Long, Question> find = new Finder(Long.class, Question.class);

    public static Question create(QuestionType questionType, boolean isNotFake) {
        Question question = new Question();
        switch (questionType){
            case COLOR:createSimpleColorQuestion(question,isNotFake);break;
            case SIZE:createSimpleSizeQuestion(question, isNotFake);break;
            case BOTH:createSimpleBiDirectionQuestion(question, isNotFake);break;
        }
        return question;
    }

    private static void createSimpleBiDirectionQuestion(Question question, boolean isNotFake) {
        question.sizePic = "big";
        question.sizeQuestion = isNotFake ? "big" : "small";
        question.sizeMatch = isNotFake ? true : false;
        question.colorPic = "dark";
    }

    private static void createSimpleSizeQuestion(Question question, boolean isNotFake) {
        question.sizePic = "big";
        question.sizeQuestion = isNotFake ? "big" : "small";
        question.sizeMatch = isNotFake ? true : false;
        question.colorPic = "dark";
        question.colorQuestion = isNotFake ? "dark" : "light";
        question.colorMatch = isNotFake ? true : false;
    }

    private static void createSimpleColorQuestion(Question question,boolean isNotFake) {
        question.colorPic = "dark";
        question.colorQuestion = isNotFake ? "dark" : "light";
        question.colorMatch = isNotFake ? true : false;
        question.sizePic = "big";
    }
}
