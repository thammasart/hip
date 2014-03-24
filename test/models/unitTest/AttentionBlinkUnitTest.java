package models.unitTest;

import models.attentionBlink.*;
import models.*;
import play.test.WithApplication;
import org.junit.*;
import static org.junit.Assert.*;
import static play.test.Helpers.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;

public class AttentionBlinkUnitTest extends WithApplication{

    @Before
    public void setUp(){
        start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
    }

    @Test
    public void createAnswerWithParameter(){
        Answer answer = new Answer(true,5.0);
        assertNotNull(answer);
        assertEquals(true,answer.answer);
        assertEquals(5.0,answer.usedTime,0.2); 

        Answer answer2 = new Answer(false,5.0);
        assertNotNull(answer2);
        assertEquals(false,answer2.answer);
        assertEquals(5.0,answer2.usedTime,0.2); 
    }

    @Test
    public void answerQueryShouldCorrect(){
        new Answer(true,5.0).save();
        assertNotNull(Answer.find.byId(1L));
    }

    @Test
    public void createQuestion() {
        String set = "1234567891234";
        String letter = "56";
    	Question question = new Question(letter, set, "91", QuestionType.NUMBER);
    	assertNotNull(question);
        assertEquals(letter, question.letter);
        assertEquals(set, question.set);
        assertEquals("91", question.correctAnswer);
        assertEquals(QuestionType.NUMBER, question.questionType);
    }

    @Test
    public void questionQueryShouldCorrect() {
        String set = "1234567891234";
        String letter = "56";
    	new Question(letter, set, "91", QuestionType.NUMBER).save();
        assertNotNull(Question.find.byId(1L));
    }

    @Test
    public void createQuiz() {
    	Quiz quiz = new Quiz();
    	assertNotNull(quiz);
    }

    @Test
    public void quizQueryShouldCorrect(){
        new Quiz().save();
        assertNotNull( Quiz.find.byId(1L));
    }

    @Test
    public void createTrial() {
    	Trial trial = new Trial(10,0.5,QuestionType.NUMBER);
    	assertNotNull(trial);
        assertEquals(10,trial.length);
        assertEquals(0.5,trial.blinkTime,0.2);
        assertEquals(QuestionType.NUMBER,trial.questionType);
        
    }

    @Test
    public void trialQueryShouldCorrect(){
        new Trial(10,0.5,QuestionType.NUMBER).save();        
        assertNotNull(Trial.find.byId(1L));
    }
}
