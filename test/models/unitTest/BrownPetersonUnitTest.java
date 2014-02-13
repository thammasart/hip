package models.unitTest;

import models.brownPeterson.*;
import models.*;
import play.test.WithApplication;
import org.junit.*;
import static org.junit.Assert.*;
import static play.test.Helpers.*;
import java.util.List;
import java.util.Date;
import java.util.Calendar;


public class BrownPetersonUnitTest extends WithApplication {
	@Before
	public void setUp(){
		start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
		
	}
	
	@Test
	public void answer_should_create_and_not_null(){
		new Trial().save();
		new Question("hello","world","!!!").save();
		Trial trial = Trial.findById(1);
		Question question = Question.findQuestionById(1);
		Quiz.create(200, 7, trial, question).save();
		Quiz quiz = Quiz.find.byId(new Long(1));
		new User("123","Secret").save();
		User user = User.find.byId("123");

		assertNotNull(new Answer("first word", "second word", "third word", 13.9,"TDM", user, quiz));
	}

	@Test
	public void answer_attribute_should_be_correct(){
		new Trial().save();
		new Question("hello","world","!!!").save();
		Trial trial = Trial.findById(1);
		Question question = Question.findQuestionById(1);
		Quiz.create(200, 7, trial, question).save();
		Quiz quiz = Quiz.find.byId(new Long(1));
		new User("123","Secret").save();
		User user = User.find.byId("123");
		
		Answer answer = new Answer("first word", "second word", "third word", 13.9,"TDM", user, quiz);

		assertEquals("first word", answer.firstWord);
		assertEquals("second word", answer.secondWord);
		assertEquals("third word", answer.thirdWord);

		assertEquals("123", answer.user.username);
		assertEquals(200, answer.quiz.initCountdown);
	}

	@Test
	public void should_create_question(){
        Question q = new Question("hello","world","!!!");
        assertNotNull(q);
    }

    @Test
    public void should_save_and_query_question(){
    	new Question("hello","world","!!!").save();
    	assertEquals(1,Question.find.findRowCount());
    }
    @Test
    public void should_save_and_query_four_question(){
    	new Question("hello","world","!!!").save();
    	new Question("hello","world","!!!").save();
    	new Question("hello","world","!!!").save();
    	new Question("hello","world","!!!").save();
    	assertEquals(4,Question.find.findRowCount());

    }

    @Test
    public void should_recieve_question_correctly(){
    	new Question("hello","world","!!!").save();
    	Question q = Question.find.where().eq("id", new Long(1)).findUnique();
    	assertEquals("hello",q.firstWord);
    	assertEquals("world",q.secondWord);
    	assertEquals("!!!",q.thirdWord);
    }

    @Test
    public void should_recieve_question_by_Id(){
    	new Question("hello","world","!!!").save();
        Question q = Question.findQuestionById(1);
    	assertEquals("hello",q.firstWord);
    	assertEquals("world",q.secondWord);
    	assertEquals("!!!",q.thirdWord);
    }    

    @Test
    public void recieve_Question_List_By_Three(){
    	new Question("hello1","world1","!!!1").save();
    	new Question("hello2","world2","!!!2").save();
    	new Question("hello3","world3","!!!3").save();
    	new Question("hello4","world4","!!!4").save();
    	new Question("hello5","world5","!!!5").save();

    	List<Question> questionList = Question.getQuestionListBy(3);
    	assertEquals(3,questionList.size());
    }

    @Test
    public void recieve_Question_List_By_Five(){
    	new Question("hello1","world1","!!!1").save();
    	new Question("hello2","world2","!!!2").save();
    	new Question("hello3","world3","!!!3").save();
    	new Question("hello4","world4","!!!4").save();
    	new Question("hello5","world5","!!!5").save();

    	List<Question> questionList = Question.getQuestionListBy(5);
    	assertEquals(5,questionList.size());
    }

    @Test
    public void recieve_Question_By_Amount_Three_Not_Null(){
     	new Question("hello1","world1","!!!1").save();
    	new Question("hello2","world2","!!!2").save();
    	new Question("hello3","world3","!!!3").save();
    	new Question("hello4","world4","!!!4").save();
    	new Question("hello5","world5","!!!5").save();
    	
     	List<Question> questionList = Question.getQuestionListBy(3);
   		assertNotNull(questionList.get(0));
   		assertNotNull(questionList.get(1));
   		assertNotNull(questionList.get(2));
    }

    @Test
    public void recieve_Question_By_Amount_By_five_Not_Null(){
     	new Question("hello1","world1","!!!1").save();
    	new Question("hello2","world2","!!!2").save();
    	new Question("hello3","world3","!!!3").save();
    	new Question("hello4","world4","!!!4").save();
    	new Question("hello5","world5","!!!5").save();
    	
     	List<Question> questionList = Question.getQuestionListBy(5);
   		assertNotNull(questionList.get(0));
   		assertNotNull(questionList.get(1));
   		assertNotNull(questionList.get(2));
   		assertNotNull(questionList.get(3));
   		assertNotNull(questionList.get(4));
    }

    @Test
    public void question_Member_In_List_Should_Not_Same() {
    	new Question("hello1","world1","!!!1").save();
    	new Question("hello2","world2","!!!2").save();
    	new Question("hello3","world3","!!!3").save();
    	new Question("hello4","world4","!!!4").save();
    	new Question("hello5","world5","!!!5").save();

    	List<Question> questionList = Question.getQuestionListBy(3);
    	assertNotEquals(questionList.get(0).id, questionList.get(1).id);
    	assertNotEquals(questionList.get(0).id, questionList.get(2).id);
    	assertNotEquals(questionList.get(2).id, questionList.get(1).id);

    }

    @Test
    public void question_list_should_not_amount_Overflow() {
    	new Question("hello1","world1","!!!1").save();
    	new Question("hello2","world2","!!!2").save();
    	new Question("hello3","world3","!!!3").save();
    	new Question("hello4","world4","!!!4").save();
    	new Question("hello5","world5","!!!5").save();

    	List<Question> questionList = Question.getQuestionListBy(10);

    	assertNull(questionList);
    }

    @Test
	public void quiz_should_Create_And_Not_Null(){
		new Trial().save();
		new Question("hello","world","!!!").save();
		Trial trial = Trial.findById(1);
		Question question = Question.findQuestionById(1);
		assertNotNull(Quiz.create(200, 7, trial, question));
	}

	@Test
	public void quiz_should_Create_And_Refer_Corectly(){
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
	public void quiz_should_Find_Involving_By_Trial_And_Not_Null(){
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
	public void quiz_should_Find_Involving_By_Trial_And_Attribute_Correctly(){
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
	@Test
	public void timelog_should_create_And_Not_Null(){
		new Trial().save();
		Trial trial = Trial.findById(1);
		new User("123","Secret").save();
		User user = User.find.byId("123");

		assertNotNull(TimeLog.create(new Date(), new Date(), user, trial));
	}

	@Test
	public void trialShouldCreateAndNotNull(){
		assertNotNull(new Trial());
	}

	@Test
	public void trigramShouldBeWordByDefault() {
		assertEquals("word", new Trial().trigramType);
	}
	@Test
	public void trigramShouldBeEnglishByDefault(){
		assertEquals("english", new Trial().trigramLanguage);
	}

	@Test
	public void trialShouldBeCreate() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, -1);
		Date lastYearDate = calendar.getTime();
		calendar.add(Calendar.YEAR, +2);
		Date nextYearDate = calendar.getTime();
		new ExperimentSchedule("Experiment 1", 5, lastYearDate, 
			nextYearDate, ExperimentType.BROWNPETERSON).save();
		ExperimentSchedule ex = ExperimentSchedule.find.byId(1L);
		assertNotNull(Trial.create(ex));
	}

	@Test
	public void trialShouldBeFindById() {
		new Trial().save();
		assertEquals(1, Trial.findById(1).id);
	}

	@Test
	public void trial_shoud_Be_Find_By_Trials_Involving(){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, -1);
		Date lastYearDate = calendar.getTime();
		calendar.add(Calendar.YEAR, +2);
		Date nextYearDate = calendar.getTime();
		new ExperimentSchedule("Experiment 1", 5, lastYearDate, 
			nextYearDate, ExperimentType.BROWNPETERSON).save();
		ExperimentSchedule ex = ExperimentSchedule.find.byId(1L);
		Trial.create(ex).save();
		Trial.create(ex).save();
		Trial.create(ex).save();

		assertEquals(3, Trial.findInvolving(ex).size());
	}

	@Test
	public void trial_shoud_Be_Find_By_Trial_Involving_And_Refer_Correctly(){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, -1);
		Date lastYearDate = calendar.getTime();
		calendar.add(Calendar.YEAR, +2);
		Date nextYearDate = calendar.getTime();
		new ExperimentSchedule("Experiment 1", 5, lastYearDate, 
			nextYearDate, ExperimentType.BROWNPETERSON).save();
		ExperimentSchedule ex = ExperimentSchedule.find.byId(1L);
		Trial.create(ex).save();
		Trial.create(ex).save();
		Trial.create(ex).save();

		List<Trial> trials = Trial.findInvolving(ex);
		assertEquals(1, trials.get(0).id);
		assertEquals(2, trials.get(1).id);
		assertEquals(3, trials.get(2).id);
	}
}
