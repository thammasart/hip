package models.unitTest;

import models.magicNumber7.*;
import models.*;

import play.test.WithApplication;
import org.junit.*;
import static org.junit.Assert.*;
import static play.test.Helpers.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;

public class MagicNumber7UnitTest extends WithApplication{
	@Before
    public void setUp(){
        start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
        Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, -1);
		Date lastYearDate = calendar.getTime();
		calendar.add(Calendar.YEAR, +2);
		Date nextYearDate = calendar.getTime();
		new ExperimentSchedule("Experiment 1", 1, lastYearDate,nextYearDate, ExperimentType.MAGICNUMBER7).save();
		new User("123", "123").save();
    }

    @Test
    public void createQuestionWithParameterShouldNotNull(){
    	assertNotNull(new Question("ABCDE", QuestionType.ENGLISH));
    }

    @Test
    public void queryQuestionShouldCorrect(){
    	new Question("ABCDE", QuestionType.ENGLISH).save();
    	Question question = Question.find.byId(1L);
    	assertNotNull(question);
    	assertEquals("ABCDE", question.memorySet);
    	assertEquals(QuestionType.ENGLISH, question.questionType);
    }

    @Test
    public void createTrialShouldNotNull(){
    	ExperimentSchedule ex = ExperimentSchedule.find.byId(1L);
    	Trial trial = new Trial(ex);
    	assertNotNull(trial);
    	assertEquals(ex, trial.schedule);
    }

    @Test
    public void createTrialWithParameterShouldCorrect(){
    	ExperimentSchedule ex = ExperimentSchedule.find.byId(1L);
    	Trial trial = Trial.create(ex, 5, QuestionType.ENGLISH);
    	assertNotNull(trial);
    	assertEquals(ex, trial.schedule);
    	assertEquals(5, trial.length);
    	assertEquals(QuestionType.ENGLISH, trial.questionType);
    }

    @Test
    public void queryTrialShouldCorrect(){
    	ExperimentSchedule ex = ExperimentSchedule.find.byId(1L);
    	Trial.create(ex, 5, QuestionType.ENGLISH).save();
    	Trial trial = Trial.find.byId(1L);
    	assertNotNull(trial);
    	assertEquals(ex, trial.schedule);
    	assertEquals(5, trial.length);
    	assertEquals(QuestionType.ENGLISH, trial.questionType);
    }

    @Test
    public void createQuizShouldNotNull(){
    	ExperimentSchedule ex = ExperimentSchedule.find.byId(1L);
    	Trial.create(ex, 5, QuestionType.ENGLISH).save();
    	Trial trial = Trial.find.byId(1L);
    	new Question("ABCDE", QuestionType.ENGLISH).save();
    	Question question = Question.find.byId(1L);
    	Quiz quiz = new Quiz(trial, question);
    	assertNotNull(quiz);
    	assertEquals(trial, quiz.trial);
    	assertEquals(question, quiz.question);
    }

    @Test
    public void createQuizWithParameterShouldCorrect(){
    	ExperimentSchedule ex = ExperimentSchedule.find.byId(1L);
    	Trial.create(ex, 5, QuestionType.ENGLISH).save();
    	Trial trial = Trial.find.byId(1L);
    	new Question("ABCDE", QuestionType.ENGLISH).save();
    	Question question = Question.find.byId(1L);
    	Quiz quiz = Quiz.create(trial, question, 0.5, 2, true);
    	assertNotNull(quiz);
    	assertEquals(trial, quiz.trial);
    	assertEquals(question, quiz.question);
    	assertEquals(0.5, quiz.displayTime, 0.001);
    	assertEquals(2, quiz.chunkSize);
    	assertTrue(quiz.isCorrect);
    }

    @Test
    public void queryQuizShouldCorrect(){
    	ExperimentSchedule ex = ExperimentSchedule.find.byId(1L);
    	Trial.create(ex, 5, QuestionType.ENGLISH).save();
    	Trial trial = Trial.find.byId(1L);
    	new Question("ABCDE", QuestionType.ENGLISH).save();
    	Question question = Question.find.byId(1L);
    	Quiz.create(trial, question, 0.5, 2, true).save();
    	Quiz quiz = Quiz.find.byId(1L);
    	assertNotNull(quiz);
    	assertEquals(trial, quiz.trial);
    	assertEquals(question, quiz.question);
    	assertEquals(0.5, quiz.displayTime, 0.001);
    	assertEquals(2, quiz.chunkSize);
    	assertTrue(quiz.isCorrect);
    }
}