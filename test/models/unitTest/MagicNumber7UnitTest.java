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
    	Quiz quiz = Quiz.create(trial, question, 0.5, 2);
    	assertNotNull(quiz);
    	assertEquals(trial, quiz.trial);
    	assertEquals(question, quiz.question);
    	assertEquals(0.5, quiz.displayTime, 0.001);
    	assertEquals(2, quiz.chunkSize);
    }

    @Test
    public void queryQuizShouldCorrect(){
    	ExperimentSchedule ex = ExperimentSchedule.find.byId(1L);
    	Trial.create(ex, 5, QuestionType.ENGLISH).save();
    	Trial trial = Trial.find.byId(1L);
    	new Question("ABCDE", QuestionType.ENGLISH).save();
    	Question question = Question.find.byId(1L);
    	Quiz.create(trial, question, 0.5, 2).save();
    	Quiz quiz = Quiz.find.byId(1L);
    	assertNotNull(quiz);
    	assertEquals(trial, quiz.trial);
    	assertEquals(question, quiz.question);
    	assertEquals(0.5, quiz.displayTime, 0.001);
    	assertEquals(2, quiz.chunkSize);
    }

    @Test
    public void createAnswerShouldNotNull(){
    	ExperimentSchedule ex = ExperimentSchedule.find.byId(1L);
    	Trial.create(ex, 5, QuestionType.ENGLISH).save();
    	new Question("ABCDE", QuestionType.ENGLISH).save();
    	Quiz.create(Trial.find.byId(1L), Question.find.byId(1L), 0.5, 0).save();
    	Answer answer = new Answer(User.find.byId("123"), Quiz.find.byId(1L));
    	assertNotNull(answer);
    	assertEquals(User.find.byId("123"), answer.user);
    	assertEquals(Quiz.find.byId(1L), answer.quiz);
    }

    @Test
    public void createAnswerWithParameterShouldNotNull(){
    	ExperimentSchedule ex = ExperimentSchedule.find.byId(1L);
    	Trial.create(ex, 5, QuestionType.ENGLISH).save();
    	new Question("ABCDE", QuestionType.ENGLISH).save();
    	Quiz.create(Trial.find.byId(1L), Question.find.byId(1L), 0.5, 0).save();
    	Answer answer = Answer.create(User.find.byId("123"), Quiz.find.byId(1L), "ABCDE", 5.55, true);
    	assertNotNull(answer);
    	assertEquals(User.find.byId("123"), answer.user);
    	assertEquals(Quiz.find.byId(1L), answer.quiz);
    	assertEquals("ABCDE", answer.answer);
    	assertEquals(5.55, answer.usedTime, 0.001);
    	assertTrue(answer.isCorrect);
    }

    @Test
    public void QueryAnswerShouldNotNull(){
    	ExperimentSchedule ex = ExperimentSchedule.find.byId(1L);
    	Trial.create(ex, 5, QuestionType.ENGLISH).save();
    	new Question("ABCDE", QuestionType.ENGLISH).save();
    	Quiz.create(Trial.find.byId(1L), Question.find.byId(1L), 0.5, 0).save();
    	Answer.create(User.find.byId("123"), Quiz.find.byId(1L), "ABCDE", 5.55, true).save();
    	Answer answer = Answer.find.byId(1L);
    	assertNotNull(answer);
    	assertEquals(User.find.byId("123"), answer.user);
    	assertEquals(Quiz.find.byId(1L), answer.quiz);
    	assertEquals("ABCDE", answer.answer);
    	assertEquals(5.55, answer.usedTime, 0.001);
    	assertTrue(answer.isCorrect);
    }

    @Test
    public void getListAnswerFromUserAndTrialShouldCorrect(){
    	ExperimentSchedule ex = ExperimentSchedule.find.byId(1L);
    	Trial.create(ex, 5, QuestionType.ENGLISH).save();
    	new Question("ABCDE", QuestionType.ENGLISH).save();
    	new Question("ZXCVB", QuestionType.ENGLISH).save();
    	new Question("QWERT", QuestionType.ENGLISH).save();
    	Quiz.create(Trial.find.byId(1L), Question.find.byId(1L), 0.5, 0).save();
    	Quiz.create(Trial.find.byId(1L), Question.find.byId(2L), 0.3, 2).save();
    	Quiz.create(Trial.find.byId(1L), Question.find.byId(3L), 0.1, 3).save();
    	Answer.create(User.find.byId("123"), Quiz.find.byId(1L), "ABCDE", 5.55, true).save();
    	Answer.create(User.find.byId("123"), Quiz.find.byId(2L), "MNBVC", 6.66, false).save();
    	Answer.create(User.find.byId("123"), Quiz.find.byId(3L), "QWERT", 7.77, true).save();
    	List<Answer> answers = new ArrayList<Answer>();
    	answers = Answer.findInvolving(User.find.byId("123"), Trial.find.byId(1L).quizzes);
    	assertNotNull(answers);
    	assertEquals(Answer.find.byId(1L), answers.get(0));
    	assertEquals(Answer.find.byId(2L), answers.get(1));
    	assertEquals(Answer.find.byId(3L), answers.get(2));
    }

    @Test
    public void calculateScoreAndUsedTimeShouldCorrect(){
    	ExperimentSchedule ex = ExperimentSchedule.find.byId(1L);
    	Trial.create(ex, 5, QuestionType.ENGLISH).save();
    	new Question("ABCDE", QuestionType.ENGLISH).save();
    	new Question("ZXCVB", QuestionType.ENGLISH).save();
    	new Question("QWERT", QuestionType.ENGLISH).save();
    	Quiz.create(Trial.find.byId(1L), Question.find.byId(1L), 0.5, 0).save();
    	Quiz.create(Trial.find.byId(1L), Question.find.byId(2L), 0.3, 2).save();
    	Quiz.create(Trial.find.byId(1L), Question.find.byId(3L), 0.1, 3).save();
    	Answer.create(User.find.byId("123"), Quiz.find.byId(1L), "ABCDE", 5.55, true).save();
    	Answer.create(User.find.byId("123"), Quiz.find.byId(2L), "MNBVC", 6.66, false).save();
    	Answer.create(User.find.byId("123"), Quiz.find.byId(3L), "QWERT", 7.77, true).save();
    	List<Answer> answers = new ArrayList<Answer>();
    	answers = Answer.findInvolving(User.find.byId("123"), Trial.find.byId(1L).quizzes);
    	assertEquals(2, Answer.calculateTotalScore(answers));
    	assertEquals(19.98, Answer.calculateTotalUsedTime(answers), 0.001);
    }
}