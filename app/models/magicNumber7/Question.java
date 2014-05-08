package models.magicNumber7;

import play.db.ebean.*;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import play.db.ebean.Model.Finder;

@Entity
@Table (name = "magic_number_7_question")
public class Question extends Model{

    private static final String ENGLISH_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBER_CASE = "0123456789";
    private static final String THAI_CASE = "กขคฆงจฉชฌญฎฐฒณดถทธนบผพภมยรลวศษสหฬอ";

    @Id
    public long id;
    public String memorySet;
    public QuestionType questionType;
    @OneToMany
    public List<Quiz> quizzes = new ArrayList<Quiz>();

    public Question(){}

    public Question(String memorySet, QuestionType questionType) {
    	this.memorySet = memorySet;
    	this.questionType = questionType;
    }

    public static Question generateQuestion(QuestionType type, int length){
        Question question = new Question();
        question.questionType = type;
        question.memorySet = question.generateMemorySet(length);
        return question;
    }

    private String generateMemorySet(int length){
        StringBuffer memorySetBuffer = new StringBuffer();

        for(int i = 0; i < length; i++){
            memorySetBuffer.append(generateRandomChar());
        }

        return memorySetBuffer.toString();
    }

    private char generateRandomChar(){
        Random random = new Random();
        char c = 'a';
        switch (questionType){
            case ENGLISH: c = ENGLISH_CASE.charAt(random.nextInt(ENGLISH_CASE.length()));break;
            case THAI: c = THAI_CASE.charAt(random.nextInt(THAI_CASE.length()));break;
            case NUMBER: c = NUMBER_CASE.charAt(random.nextInt(NUMBER_CASE.length()));break;
        }

        return c;
    }

	@SuppressWarnings("unchecked")
	public static Finder<Long, Question> find = new Finder(Long.class, Question.class);
}