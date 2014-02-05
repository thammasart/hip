import org.junit.*;
import java.util.Date;
import static org.junit.Assert.*;
import play.test.WithApplication;
import static play.test.Helpers.*;
import models.brownPeterson.*;
import models.*;
import java.util.List;
import com.avaje.ebean.*;
import play.libs.Yaml;



public class BrownPetersonTest extends WithApplication {
	@Before
	public void setUp() {
		start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
		Ebean.save((List) Yaml.load("test-data.yml"));
	}

	@Test
	public void experimentSchedule_Should_Have_1_row(){

		assertEquals(1, ExperimentSchedule.find.findRowCount());
	}
	@Test
	public void questions_should_have_9_Rows(){
		assertEquals(12, Question.find.findRowCount());
	}
	@Test
	public void users_should_have_1_Rows(){
		assertEquals(1, User.find.findRowCount());
	}

	@Test
	public void trials_should_have_1_row(){
		assertEquals(3, Trial.find.findRowCount());
	}
	@Test
	public void quizzes_should_have_3_row(){
		assertEquals(9, Quiz.find.findRowCount());
	}
	@Test
	public void timelog_should_have_3_row(){
		assertEquals(3, TimeLog.find.findRowCount());
	}
	@Test
	public void answers_should_have_9_rows(){
		assertEquals(9, Answer.find.findRowCount());
	}

	@Test
	public void retrieve_all_answers_success(){
		List<Answer> answers = Answer.find.all();
		assertEquals("q1w1", answers.get(0).firstWord);
		assertEquals("q2w2", answers.get(1).secondWord);
		assertEquals("q3w3", answers.get(2).thirdWord);
	}

	@Test
	public void retrieve_answers_involving_by_user_success(){
		User user = User.find.where().eq("username", "s550").findUnique();
		List<Answer> answers = Answer.find.where().eq("user_username", user.username).findList();
		assertEquals(9, answers.size());
	}
	@Test
	public void retrieve_Quizzes_involving_by_trial_success(){
		Trial trial = Trial.find.where().eq("id", 1).findUnique();
		List<Quiz> quizzes = Quiz.find.where().eq("trial_id", trial.id).findList();
		assertEquals(3, quizzes.size());
	}

	@Test
	public void check_user_used_to_take_the_trial_by_timelog_success(){
		User user = User.find.where().eq("username", "s550").findUnique();
		Trial trial = Trial.find.where().eq("id", 1).findUnique();
		assertTrue(TimeLog.isRepeatTrial(user, trial));
	}
	@Test
	public void check_user_used_to_take_the_trial_by_timelog_fail(){
		User user = User.find.where().eq("username", "s551").findUnique();
		Trial trial = Trial.find.where().eq("id", 1).findUnique();
		assertFalse(TimeLog.isRepeatTrial(user, trial));
	}
}