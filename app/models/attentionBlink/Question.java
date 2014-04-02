package models.attentionBlink;

import play.db.ebean.*;
import javax.persistence.*;
import java.util.Random;

@Entity
@Table (name = "attention_blink_question")
public class Question extends Model{

    private static final String ENGLISH_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBER_CASE = "0123456789";
    private static final String THAI_CASE = "กขคฆงจฉชฌญฎฐฒณดถทธนบผพภมยรลวศษสหฬอ";

	@Id
	public long id;
    @Column(nullable=false)
	public String letter;
    @Column(name="question_set",nullable=false, length=20)
    public String set;
    public boolean correctAnswer;
    public QuestionType questionType;

	public Question(String letter, String set, boolean correctAnswer, QuestionType questionType) {
        this.letter = letter;
        this.set = set;
        this.correctAnswer = correctAnswer;
        this.questionType = questionType; 
	}

    public static Question generateQuestion(QuestionType questionType, int length, int numberOfTarget, boolean isCorrect){
        StringBuffer setbuffer = new StringBuffer();
        String typeString = getQuestionTypeCase(questionType);

        Random random = new Random();
        StringBuffer letterBuffer = new StringBuffer();
        for(int i = 0; i < numberOfTarget; i++){
            letterBuffer.append(typeString.charAt(random.nextInt(typeString.length())));
        }

        for(int i =0; i < length; i++){
            setbuffer.append(typeString.charAt(random.nextInt(typeString.length())));
        }

        if(isCorrect){
            int index = random.nextInt(setbuffer.length() - numberOfTarget - 1);
            for(int i = 0; i < numberOfTarget; i++){
                setbuffer.setCharAt(index + i, letterBuffer.charAt(i));
            }
        }else{
            while(setbuffer.toString().contains(letterBuffer)){
                StringBuffer newLetter = new StringBuffer();
                for(int i = 0; i < numberOfTarget; i++){
                    newLetter.append(typeString.charAt(random.nextInt(typeString.length())));
                }
                String temp = setbuffer.toString();
                temp = temp.replace(letterBuffer, newLetter);
                setbuffer = new StringBuffer(temp);
            }
        }

        Question question = new Question(letterBuffer.toString(), setbuffer.toString(), isCorrect, questionType);
        question.save();
        return question;
    }

    public void changeQuestionSetByCorrectAnswer(){
        String typeString = getQuestionTypeCase(questionType);
        Random random = new Random();
        if(this.correctAnswer){
            StringBuffer setbuffer = new StringBuffer(this.set);
            int index = random.nextInt(setbuffer.length() - this.letter.length() - 1);
            for(int i = 0; i < this.letter.length(); i++){
                setbuffer.setCharAt(index + i, this.letter.charAt(i));
            }
            this.set = setbuffer.toString();
        }else{
            while(this.set.contains(this.letter)){
                StringBuffer letterBuffer = new StringBuffer();
                for(int i = 0; i < this.letter.length(); i++){
                    letterBuffer.append(typeString.charAt(random.nextInt(typeString.length())));
                }
                this.set = this.set.replace(this.letter, letterBuffer);
            }
        }

        this.update();
    }

    private static String getQuestionTypeCase(QuestionType questionType){
        String typeString = ENGLISH_CASE;
        switch(questionType){
            case ENGLISH : typeString = ENGLISH_CASE;break;
            case THAI : typeString = THAI_CASE;break;
            case NUMBER : typeString = NUMBER_CASE;break;
        }
        return typeString;
    }
        
    @SuppressWarnings("unchecked")
    public static Finder<Long, Question> find = new Finder(Long.class, Question.class);
    
    
    
}
