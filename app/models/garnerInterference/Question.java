package models.garnerInterference;

import play.db.ebean.Model;
import javax.persistence.*;
import java.util.Random;

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

    public static final String[] SIZES = {"big", "small"};
    public static final String[] COLORS = {"dark", "light"};

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
        question.sizePic = generateSizePic();
        question.sizeQuestion = isNotFake ? question.sizePic : calculateSizeFake(question.sizePic);
        question.sizeMatch = isNotFake ? true : false;
        question.colorPic = generateColorPic();
        question.colorQuestion = isNotFake ? question.colorPic : calculateColorFake(question.colorPic);
        question.colorMatch = isNotFake ? true : false;
    }

    private static String calculateSizeFake(String sizePic) {
        if(sizePic.equals(SIZES[0]))
            return SIZES[1];
        return SIZES[0];
    }



    private static void createSimpleSizeQuestion(Question question, boolean isNotFake) {
        question.sizePic = generateSizePic();
        question.sizeQuestion = isNotFake ? question.sizePic : calculateSizeFake(question.sizePic);
        question.sizeMatch = isNotFake ? true : false;
        question.colorPic = generateColorPic();
    }

    private static String calculateColorFake(String colorPic) {
        return colorPic.equals(COLORS[0]) ? COLORS[1] : COLORS[0];
    }

    private static String generateColorPic() {
        return COLORS[new Random().nextInt(COLORS.length)];
    }

    private static String generateSizePic() {
        return SIZES[new Random().nextInt(SIZES.length)];
    }

    private static void createSimpleColorQuestion(Question question,boolean isNotFake) {
        question.colorPic = generateColorPic();
        question.colorQuestion = isNotFake ? question.colorPic : calculateColorFake(question.colorPic);
        question.colorMatch = isNotFake ? true : false;
        question.sizePic = generateSizePic();
    }
}
