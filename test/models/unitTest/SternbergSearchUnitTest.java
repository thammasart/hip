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
    	int length = 5;
    	double blinkTime = 0.05;
    	int oneCharIsCorrect = 2;
    	int oneCharIsInCorrect = 2;
    	int twoCharIsCorrect = 2;
    	int twoCharIsInCorrect = 2;
    	QuestionType questionType = QuestionType.NUMBER;
    	Trial trial = Trial.create(ex, length, blinkTime, oneCharIsCorrect, oneCharIsInCorrect, twoCharIsCorrect, twoCharIsInCorrect, questionType);
    	assertNotNull(trial);
    	assertEquals(ex, trial.schedule);
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
    	Trial.create(ex, length, blinkTime, oneCharIsCorrect, oneCharIsInCorrect, twoCharIsCorrect, twoCharIsInCorrect, questionType).save();
    	Trial trial = Trial.find.byId(1L);
    	assertNotNull(trial);
    	assertEquals(ex, trial.schedule);
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
    	new Question("12345", QuestionType.NUMBER).save();
    	Question question = Question.find.byId(1L);
    	assertNotNull(question);
    	assertEquals("12345", question.memorySet);
    	assertEquals(QuestionType.NUMBER, question.questionType);
    }

    @Test
    public void queryQuestionShouldCorrect(){
    	new Question("12345", QuestionType.NUMBER).save();
    	Question question = Question.find.byId(1L);
    	assertNotNull(question);
    	assertEquals("12345", question.memorySet);
    	assertEquals(QuestionType.NUMBER, question.questionType);
    }

    @Test
    public void createQuizAndNotNull(){
    	ExperimentSchedule ex = ExperimentSchedule.find.byId(1L);
    	new Question("12345", QuestionType.NUMBER).save();
    	Question question = Question.find.byId(1L);
    	Trial.create(ex, 6, 0.05, 2, 2, 2, 2, QuestionType.NUMBER).save();
    	Trial trial = Trial.find.byId(1L);
    	Quiz quiz = new Quiz(trial, question);
    	assertNotNull(quiz);
    	assertEquals(trial, quiz.trial);
    	assertEquals(question, quiz.question);
    }

    @Test
    public void createQuizWithParameter(){
    	ExperimentSchedule ex = ExperimentSchedule.find.byId(1L);
    	new Question("12345", QuestionType.NUMBER).save();
    	Question question = Question.find.byId(1L);
    	Trial.create(ex, 6, 0.05, 2, 2, 2, 2, QuestionType.NUMBER).save();
    	Trial trial = Trial.find.byId(1L);
    	Quiz quiz = Quiz.create(trial, question, "5", true);
    	assertNotNull(quiz);
    	assertEquals(trial, quiz.trial);
    	assertEquals(question, quiz.question);
        assertEquals("5", quiz.questionChar);
    	assertTrue(quiz.isTrue);
    }

    @Test
    public void queryQuizShouldCorrect(){
    	ExperimentSchedule ex = ExperimentSchedule.find.byId(1L);
    	new Question("12345", QuestionType.NUMBER).save();
    	Question question = Question.find.byId(1L);
    	Trial.create(ex, 6, 0.05, 2, 2, 2, 2, QuestionType.NUMBER).save();
    	Trial trial = Trial.find.byId(1L);
    	Quiz.create(trial, question, "5", true).save();
    	Quiz quiz = Quiz.find.byId(1L);
    	assertNotNull(quiz);
    	assertEquals(trial, quiz.trial);
    	assertEquals(question, quiz.question);
        assertEquals("5", quiz.questionChar);
    	assertTrue(quiz.isTrue);
    }

    @Test
    public void createAnswerShouldNotNull(){
    	User user = User.find.byId("123");
    	ExperimentSchedule ex = ExperimentSchedule.find.byId(1L);
    	new Question("12345", QuestionType.NUMBER).save();
    	Question question = Question.find.byId(1L);
    	Trial.create(ex, 6, 0.05, 2, 2, 2, 2, QuestionType.NUMBER).save();
    	Trial trial = Trial.find.byId(1L);
    	Quiz.create(trial, question, "5", true).save();
    	Quiz quiz = Quiz.find.byId(1L);
    	Answer answer = new Answer(user, quiz);
    	assertNotNull(answer);
    	assertEquals(user, answer.user);
    	assertEquals(quiz, answer.quiz);
    }

    @Test
    public void createAnswerWithParameter(){
    	User user = User.find.byId("123");
    	ExperimentSchedule ex = ExperimentSchedule.find.byId(1L);
    	new Question("12345", QuestionType.NUMBER).save();
    	Question question = Question.find.byId(1L);
    	Trial.create(ex, 6, 0.05, 2, 2, 2, 2, QuestionType.NUMBER).save();
    	Trial trial = Trial.find.byId(1L);
    	Quiz.create(trial, question, "5", true).save();
    	Quiz quiz = Quiz.find.byId(1L);
    	Answer answer = Answer.create(user, quiz, true, 4.85, true);
    	assertNotNull(answer);
    	assertEquals(user, answer.user);
    	assertEquals(quiz, answer.quiz);
    	assertTrue(answer.answer);
    	assertEquals(4.85, answer.usedTime, 0.001);
    	assertTrue(answer.isCorrect);
    }

    @Test
    public void queryAnswerShouldCorrect(){
    	User user = User.find.byId("123");
    	ExperimentSchedule ex = ExperimentSchedule.find.byId(1L);
    	new Question("12345", QuestionType.NUMBER).save();
    	Question question = Question.find.byId(1L);
    	Trial.create(ex, 6, 0.05, 2, 2, 2, 2, QuestionType.NUMBER).save();
    	Trial trial = Trial.find.byId(1L);
    	Quiz.create(trial, question, "5", true).save();
    	Quiz quiz = Quiz.find.byId(1L);
    	Answer.create(user, quiz, true, 4.85, true).save();
    	Answer answer = Answer.find.byId(1L);
    	assertNotNull(answer);
    	assertEquals(user, answer.user);
    	assertEquals(quiz, answer.quiz);
    	assertTrue(answer.answer);
    	assertEquals(4.85, answer.usedTime, 0.001);
    	assertTrue(answer.isCorrect);
    }
}