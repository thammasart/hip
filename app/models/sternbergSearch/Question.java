package models.sternbergSearch;

import play.db.ebean.*;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import play.db.ebean.Model.Finder;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table (name = "sternberg_search_question")
public class Question extends Model{

    private static final String ENGLISH_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBER_CASE = "0123456789";
    private static final String THAI_CASE = "กขคฆงจฉชฌญฎฐฒณดถทธนบผพภมยรลวศษสหฬอ";
    @Id
    public long id;
    public String memorySet;
    public QuestionType questionType;
    @OneToMany(mappedBy = "question")
    private List<Quiz> quizzes;

    public Question(){}
    public Question(String memorySet, QuestionType questionType) {
    	this.memorySet = memorySet;
    	this.questionType = questionType;
    }

	@SuppressWarnings("unchecked")
	public static Finder<Long, Question> find = new Finder(Long.class, Question.class);

    public static Question generateQuestion(QuestionType type, int length) {
        Question question = new Question();
        question.questionType = type;
        question.generateMemorySet(length);
        return question;
    }

    private void generateMemorySet(int length) {
        StringBuffer memorySetBuffer = new StringBuffer();

        for(int i = 0; i < length; i++){
            memorySetBuffer.append(generateRandomChar());
        }

        this.memorySet = memorySetBuffer.toString();
    }

    private char generateRandomChar() {
        Random random = new Random();
        char c = 'a';
        switch (questionType){
            case ENGLISH: c = ENGLISH_CASE.charAt(random.nextInt(ENGLISH_CASE.length()));break;
            case THAI: c = THAI_CASE.charAt(random.nextInt(THAI_CASE.length()));break;
            case NUMBER: c = NUMBER_CASE.charAt(random.nextInt(NUMBER_CASE.length()));break;
        }

        return c;
    }
}
