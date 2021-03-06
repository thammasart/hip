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
    	Question question = new Question(letter, set, true, QuestionType.NUMBER);
    	assertNotNull(question);
        assertEquals(letter, question.letter);
        assertEquals(set, question.set);
        assertTrue(question.correctAnswer);
        assertEquals(QuestionType.NUMBER, question.questionType);
    }

    @Test
    public void questionQueryShouldCorrect() {
        String set = "1234567891234";
        String letter = "56";
    	new Question(letter, set, true, QuestionType.NUMBER).save();
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
    	Trial trial = new Trial(QuestionType.NUMBER);
    	assertNotNull(trial);
        assertEquals(QuestionType.NUMBER,trial.questionType);
        
    }
    @Test
    public void setNumberOfQuizInTrial(){
        
        Trial t =  new Trial(QuestionType.NUMBER);        
        t.numberOfQuiz = 3;
        t.save();
        assertEquals(3,Trial.find.byId(1L).numberOfQuiz);
    }

    @Test
    public void trialQueryShouldCorrect(){
        new Trial(QuestionType.NUMBER).save();        
        assertNotNull(Trial.find.byId(1L));
    }

    @Test
    public void linkTrialWithExperimentSchedule(){
	Calendar calendar = Calendar.getInstance();
	calendar.add(Calendar.YEAR, -1);

	Date lastYearDate = calendar.getTime();
	calendar.add(Calendar.YEAR, +2);
	Date nextYearDate = calendar.getTime();

	new ExperimentSchedule("Experiment 1", 5, lastYearDate,nextYearDate, ExperimentType.ATTENTIONBLINK).save();
        ExperimentSchedule ex = ExperimentSchedule.find.byId(1L);
        
        Trial trial = Trial.create(ex, QuestionType.NUMBER, 3);
        trial.save();

	assertNotNull(trial);
        assertEquals(ex.id, Trial.find.byId(1L).schedule.id);
    }
}
