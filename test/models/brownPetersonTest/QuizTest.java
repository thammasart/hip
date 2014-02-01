import models.*;
import models.brownPeterson.*;
import org.junit.*;
import static org.junit.Assert.*;
import play.test.WithApplication;
import static play.test.Helpers.*;
import java.util.List;


public class QuizTest extends WithApplication{
	@Before
	public void setUp(){
		start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
	}

	@Test
	public void shouldCreateAndNotNull(){
		new Trial().save();
		new Question("hello","world","!!!").save();
		Trial trial = Trial.findById(1);
		Question question = Question.findQuestionById(1);
		assertNotNull(Quiz.create(200, 7, trial, question));
	}

	@Test
	public void shouldCreateAndReferCorectly(){
		new Trial().save();
		new Question("hello","world","!!!").save();
		Trial trial = Trial.findById(1);
		Question question = Question.findQuestionById(1);
		Quiz quiz = Quiz.create(200, 7, trial, question);

		assertEquals(200, quiz.initCountdown);
		assertEquals(7, quiz.flashTime);
		assertEquals(trial.id, quiz.trial.id);
		assertEquals(question.id, quiz.question.id);
	}

	@Test
	public void shouldFindInvolvingByTrialAndNotNull(){
		new Trial().save();
		new Question("hello","world","!!!").save();
		Trial trial = Trial.findById(1);
		Question question = Question.findQuestionById(1);
		Quiz.create(200, 7, trial, question).save();
		Quiz.create(100, 5, trial, question).save();
		Quiz.create(80, 6, trial, question).save();

		assertNotNull(Quiz.findInvolving(trial));
	}

	@Test
	public void shouldFindInvolvingByTrialAndAttributeCorrectly(){
		new Trial().save();
		new Question("hello","world","!!!").save();
		Trial trial = Trial.findById(1);
		Question question = Question.findQuestionById(1);
		Quiz.create(200, 7, trial, question).save();
		Quiz.create(100, 5, trial, question).save();
		Quiz.create(80, 6, trial, question).save();

		List<Quiz> quizzes = Quiz.findInvolving(trial);
		assertEquals(200, quizzes.get(0).initCountdown);
		assertEquals(100, quizzes.get(1).initCountdown);
		assertEquals(80, quizzes.get(2).initCountdown);
	}
}