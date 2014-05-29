package models.unitTest;

import models.ExperimentSchedule;
import models.ExperimentType;
import models.TimeLog;
import models.User;
import models.simonEffect.*;
import play.test.WithApplication;
import org.junit.*;
import static org.junit.Assert.*;
import static play.test.Helpers.*;
import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;

public class SimonEffectUnitTest extends WithApplication {

    @Before
    public void setUp(){
    	Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, -1);
		Date lastYearDate = calendar.getTime();
		calendar.add(Calendar.YEAR, +2);
		Date nextYearDate = calendar.getTime();
        start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
        new ExperimentSchedule("testExperiment 1", 1, lastYearDate,nextYearDate, ExperimentType.SIMONEFFECT).save();
        new User("123", "123").save();
    }

    @Test
    public void createQuestionNotNull(){
        assertNotNull(new Question());
    }

    @Test
    public void createQuestionWithParameter(){
        Question q = new Question("red",'O',"up");
        assertEquals("red",q.color);
        assertEquals('O',q.alphabet);
        assertEquals("up",q.direction);
    }

    @Test
    public void createQuestionAndSaveAndRetrieveComplete(){
        new Question("red",'O',"up").save();
        assertEquals(1,Question.find.findRowCount());

        Question q = Question.find.byId(1L);
        assertEquals("red",q.color);
        assertEquals('O',q.alphabet);
        assertEquals("up",q.direction);
    }

    @Test
    public void createTrialNotNull(){
        assertNotNull(new Trial());
    }

    @Test
    public void createTrialWithParameter(){
    	ExperimentSchedule exp = ExperimentSchedule.find.byId(1L);
    	Trial trial = new Trial(exp, QuestionType.TWOFEATURE, 0.5);

        assertEquals(exp,trial.schedule);
        assertEquals(0.5,trial.blinkTime, 0.001);
    }

    @Test
    public void createTrialAndSaveAndRetrieveComplete(){
        ExperimentSchedule exp = ExperimentSchedule.find.byId(1L);
    	new Trial(exp, QuestionType.TWOFEATURE, 0.5).save();
        assertEquals(1,Trial.find.findRowCount());

        Trial trial = Trial.find.byId(1L);
        assertEquals(exp,trial.schedule);
        assertEquals(0.5,trial.blinkTime, 0.001);
    }

    @Test
    public void createQuizNotNull(){
        assertNotNull(new Quiz());
    }

    @Test
    public void createQuizWithParameter(){
    	new Question("red",'O',"up").save();
        Question q = Question.find.byId(1L);
        ExperimentSchedule exp = ExperimentSchedule.find.byId(1L);
    	new Trial(exp, QuestionType.TWOFEATURE, 0.5).save();
    	Trial trial = Trial.find.byId(1L);
        Quiz quiz = new Quiz(trial, q, "down");
        assertEquals(trial, quiz.trial);
        assertEquals(q, quiz.question);
        assertEquals("down", quiz.position);
    }

    @Test
    public void createQuizAndSaveAndRetrieveComplete(){
        new Question("red",'O',"up").save();
        Question q = Question.find.byId(1L);
        ExperimentSchedule exp = ExperimentSchedule.find.byId(1L);
    	new Trial(exp, QuestionType.TWOFEATURE, 0.5).save();
    	Trial trial = Trial.find.byId(1L);
        new Quiz(trial, q, "down").save();
        assertEquals(1,Quiz.find.findRowCount());

        Quiz quiz = Quiz.find.byId(1L);
        assertEquals(trial, quiz.trial);
        assertEquals(q, quiz.question);
        assertEquals("down", quiz.position);
    }

    @Test
    public void createQuizAndTrialShouldFindQuizzesCorrectly(){
    	new Question("red",'O',"up").save();
    	new Question("red",'X',"right").save();
    	new Question("geen",'O',"left").save();
    	new Question("geen",'X',"down").save();
        Question q1 = Question.find.byId(1L);
        Question q2 = Question.find.byId(2L);
        Question q3 = Question.find.byId(3L);
        Question q4 = Question.find.byId(4L);
        ExperimentSchedule exp = ExperimentSchedule.find.byId(1L);
    	new Trial(exp, QuestionType.TWOFEATURE, 0.5).save();
    	Trial trial = Trial.find.byId(1L);
        new Quiz(trial, q1, "down").save();
        new Quiz(trial, q2, "up").save();
        new Quiz(trial, q3, "left").save();
        new Quiz(trial, q4, "right").save();
        new Quiz(trial, q4, "up").save();

        Question question = Question.find.byId(4L);
        assertEquals(2,question.quizzes.size());
        assertEquals(Quiz.find.byId(4L), question.quizzes.get(0));
        assertEquals(Quiz.find.byId(5L), question.quizzes.get(1));
        trial = Trial.find.byId(1L);
        assertEquals(5,trial.quizzes.size());
        assertEquals(Quiz.find.byId(1L), trial.quizzes.get(0));
        assertEquals(Quiz.find.byId(2L), trial.quizzes.get(1));
        assertEquals(Quiz.find.byId(3L), trial.quizzes.get(2));
        assertEquals(Quiz.find.byId(4L), trial.quizzes.get(3));
        assertEquals(Quiz.find.byId(5L), trial.quizzes.get(4));
    }

    @Test
    public void createAnswerNotNull(){
        assertNotNull(new Question());
    }

    @Test
    public void createAnswerWithParameter(){
    	new Question("red",'O',"up").save();
        Question q1 = Question.find.byId(1L);
        ExperimentSchedule exp = ExperimentSchedule.find.byId(1L);
    	new Trial(exp, QuestionType.TWOFEATURE, 0.5).save();
    	Trial trial = Trial.find.byId(1L);
        new Quiz(trial, q1, "down").save();
        Quiz quiz = Quiz.find.byId(1L);
		User user = User.find.byId("123");
		Answer ans = new Answer(user, quiz, "up", 0.9);
		assertEquals(user, ans.user);
		assertEquals(quiz, ans.quiz);
		assertEquals("up", ans.answer);
		assertEquals(0.9, ans.usedTime, 0.01);
		assertEquals(true, ans.isCorrect);
    }

    @Test
    public void createAnswerAndSaveAndRetrieveComplete(){
        new Question("red",'O',"up").save();
        Question q1 = Question.find.byId(1L);
        ExperimentSchedule exp = ExperimentSchedule.find.byId(1L);
    	new Trial(exp, QuestionType.TWOFEATURE, 0.5).save();
    	Trial trial = Trial.find.byId(1L);
        new Quiz(trial, q1, "down").save();
        Quiz quiz = Quiz.find.byId(1L);
		User user = User.find.byId("123");
		new Answer(user, quiz, "up", 0.9).save();
		Answer ans = Answer.find.byId(1L);

		assertEquals(user, ans.user);
		assertEquals(quiz, ans.quiz);
		assertEquals("up", ans.answer);
		assertEquals(0.9, ans.usedTime, 0.01);;
		assertEquals(true, ans.isCorrect);
    }

    @Test
    public void calculateScoreShouldCorrect(){
    	new Question("red",'O',"up").save();
    	new Question("red",'X',"right").save();
    	new Question("geen",'O',"left").save();
    	new Question("geen",'X',"down").save();
        Question q1 = Question.find.byId(1L);
        Question q2 = Question.find.byId(2L);
        Question q3 = Question.find.byId(3L);
        Question q4 = Question.find.byId(4L);
        ExperimentSchedule exp = ExperimentSchedule.find.byId(1L);
        new Trial(exp, QuestionType.TWOFEATURE, 0.5).save();
    	Trial trial = Trial.find.byId(1L);
        new Quiz(trial, q1, "down").save();
        new Quiz(trial, q2, "up").save();
        new Quiz(trial, q3, "left").save();
        new Quiz(trial, q4, "right").save();
        new Quiz(trial, q4, "up").save();
        User user = User.find.byId("123");
		new Answer(user, Quiz.find.byId(1L), "down", 0.9).save();
		new Answer(user, Quiz.find.byId(2L), "up", 0.5).save();
		new Answer(user, Quiz.find.byId(3L), "left", 0.1).save();
		new Answer(user, Quiz.find.byId(4L), "right", 0.2).save();
		new Answer(user, Quiz.find.byId(5L), "down", 1.6).save();

		List<Answer> ans = Answer.find.where().eq("user_username", "123").findList();
		assertEquals(2,Answer.calculateTotalScore(ans));
    }

    @Test
    public void calculateUsedTimeShouldCorrect(){
    	new Question("red",'O',"up").save();
    	new Question("red",'X',"right").save();
    	new Question("geen",'O',"left").save();
    	new Question("geen",'X',"down").save();
        Question q1 = Question.find.byId(1L);
        Question q2 = Question.find.byId(2L);
        Question q3 = Question.find.byId(3L);
        Question q4 = Question.find.byId(4L);
        ExperimentSchedule exp = ExperimentSchedule.find.byId(1L);
        new Trial(exp, QuestionType.TWOFEATURE, 0.5).save();
    	Trial trial = Trial.find.byId(1L);
        new Quiz(trial, q1, "down").save();
        new Quiz(trial, q2, "up").save();
        new Quiz(trial, q3, "left").save();
        new Quiz(trial, q4, "right").save();
        new Quiz(trial, q4, "up").save();
        User user = User.find.byId("123");
		new Answer(user, Quiz.find.byId(1L), "down", 0.9).save();
		new Answer(user, Quiz.find.byId(2L), "up", 0.5).save();
		new Answer(user, Quiz.find.byId(3L), "left", 0.1).save();
		new Answer(user, Quiz.find.byId(4L), "right", 0.2).save();
		new Answer(user, Quiz.find.byId(5L), "down", 1.6).save();

		List<Answer> ans = Answer.find.where().eq("user_username", "123").findList();
		assertEquals(3.3,Answer.calculateTotalUsedTime(ans),0.001);
    }
}