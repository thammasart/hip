package models.unitTest;

import models.sternbergSearch.*;
import models.*;

import play.test.WithApplication;
import org.junit.*;
import static org.junit.Assert.*;
import static play.test.Helpers.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;

public class SternbergSearchUnitTest extends WithApplication{

    @Before
    public void setUp(){
        start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
        Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, -1);
		Date lastYearDate = calendar.getTime();
		calendar.add(Calendar.YEAR, +2);
		Date nextYearDate = calendar.getTime();
		new ExperimentSchedule("Experiment 1", 1, lastYearDate,nextYearDate, ExperimentType.STERNBERGSEARCH).save();
		new User("123", "123").save();
    }

    @Test
    public void createTrialAndNotNull(){
    	ExperimentSchedule ex = ExperimentSchedule.find.byId(1L);
    	Trial trial = new Trial(ex);
    	assertNotNull(trial);
    	assertEquals(ex, trial.schedule);
    }

    @Test
    public void createTrialWithParameter(){
    	ExperimentSchedule ex = ExperimentSchedule.find.byId(1L);
    	String memorySet = "123456";
    	int length = 6;
    	double blinkTime = 0.05;
    	int oneCharIsCorrect = 2;
    	int oneCharIsInCorrect = 2;
    	int twoCharIsCorrect = 2;
    	int twoCharIsInCorrect = 2;
    	QuestionType questionType = QuestionType.NUMBER;
    	Trial trial = Trial.create(ex, memorySet, length, blinkTime, oneCharIsCorrect, oneCharIsInCorrect, twoCharIsCorrect, twoCharIsInCorrect, questionType);
    	assertNotNull(trial);
    	assertEquals(ex, trial.schedule);
    	assertEquals(memorySet, trial.memorySet);
    	assertEquals(length, trial.length);
    	assertEquals(blinkTime, trial.blinkTime, 0.001);
    	assertEquals(oneCharIsCorrect, trial.oneCharIsCorrect);
    	assertEquals(oneCharIsInCorrect, trial.oneCharIsInCorrect);
    	assertEquals(twoCharIsCorrect, trial.twoCharIsCorrect);
    	assertEquals(twoCharIsInCorrect, trial.twoCharIsInCorrect);
    	assertEquals(questionType, trial.questionType);
    }

    @Test
    public void queryTrialShouldCorrect(){
    	ExperimentSchedule ex = ExperimentSchedule.find.byId(1L);
    	String memorySet = "123456";
    	int length = 6;
    	double blinkTime = 0.05;
    	int oneCharIsCorrect = 2;
    	int oneCharIsInCorrect = 2;
    	int twoCharIsCorrect = 2;
    	int twoCharIsInCorrect = 2;
    	QuestionType questionType = QuestionType.NUMBER;
    	Trial.create(ex, memorySet, length, blinkTime, oneCharIsCorrect, oneCharIsInCorrect, twoCharIsCorrect, twoCharIsInCorrect, questionType).save();
    	Trial trial = Trial.find.byId(1L);
    	assertNotNull(trial);
    	assertEquals(ex, trial.schedule);
    	assertEquals(memorySet, trial.memorySet);
    	assertEquals(length, trial.length);
    	assertEquals(blinkTime, trial.blinkTime, 0.001);
    	assertEquals(oneCharIsCorrect, trial.oneCharIsCorrect);
    	assertEquals(oneCharIsInCorrect, trial.oneCharIsInCorrect);
    	assertEquals(twoCharIsCorrect, trial.twoCharIsCorrect);
    	assertEquals(twoCharIsInCorrect, trial.twoCharIsInCorrect);
    	assertEquals(questionType, trial.questionType);
    }

    @Test
    public void createQuestionWithParameterAndNotNull(){
    	new Question("5", QuestionType.NUMBER).save();
    	Question question = Question.find.byId(1L);
    	assertNotNull(question);
    	assertEquals("5", question.question);
    	assertEquals(QuestionType.NUMBER, question.questionType);
    }

    @Test
    public void queryQuestionShouldCorrect(){
    	new Question("5", QuestionType.NUMBER).save();
    	Question question = Question.find.byId(1L);
    	assertNotNull(question);
    	assertEquals("5", question.question);
    	assertEquals(QuestionType.NUMBER, question.questionType);
    }

    @Test
    public void createQuizAndNotNull(){
    	ExperimentSchedule ex = ExperimentSchedule.find.byId(1L);
    	new Question("5", QuestionType.NUMBER).save();
    	Question question = Question.find.byId(1L);
    	Trial.create(ex, "123456", 6, 0.05, 2, 2, 2, 2, QuestionType.NUMBER).save();
    	Trial trial = Trial.find.byId(1L);
    	Quiz quiz = new Quiz(trial, question);
    	assertNotNull(quiz);
    	assertEquals(trial, quiz.trial);
    	assertEquals(question, quiz.question);
    }

    @Test
    public void createQuizWithParameter(){
    	ExperimentSchedule ex = ExperimentSchedule.find.byId(1L);
    	new Question("5", QuestionType.NUMBER).save();
    	Question question = Question.find.byId(1L);
    	Trial.create(ex, "123456", 6, 0.05, 2, 2, 2, 2, QuestionType.NUMBER).save();
    	Trial trial = Trial.find.byId(1L);
    	Quiz quiz = Quiz.create(trial, question, true);
    	assertNotNull(quiz);
    	assertEquals(trial, quiz.trial);
    	assertEquals(question, quiz.question);
    	assertTrue(quiz.isTrue);
    }
}