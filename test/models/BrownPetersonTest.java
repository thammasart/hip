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
	public void experimentSchedule_Should_Have_2_row(){

		assertEquals(2, ExperimentSchedule.find.findRowCount());
	}
	@Test
	public void questions_should_have_9_Rows(){
		assertEquals(12, Question.find.findRowCount());
	}
	@Test
	public void users_should_have_3_Rows(){
		assertEquals(3, User.find.findRowCount());
	}

	@Test
	public void trials_should_have_2_row(){
		assertEquals(6, Trial.find.findRowCount());
	}
	@Test
	public void quizzes_should_have_3_row(){
		assertEquals(18, Quiz.find.findRowCount());
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
		assertEquals("q3w2", answers.get(2).thirdWord);
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
		Trial trial = Trial.find.where().eq("id", 2).findUnique();
		assertTrue(TimeLog.isRepeatTrial(user, trial));
	}
	@Test
	public void check_user_used_to_take_the_trial_by_timelog_fail(){
		User user = User.find.where().eq("username", "s551").findUnique();
		Trial trial = Trial.find.where().eq("id", 1).findUnique();
		assertFalse(TimeLog.isRepeatTrial(user, trial));
	}

	@Test
	public void check_user_can_take_the_exp_complete_3_trial() {
		/*
		User user = User.find.where().eq("username", "s551").findUnique();
		List<ExperimentSchedule> exps = ExperimentSchedule.getWorkingExperimentsByType(ExperimentType.BROWNPETERSON);
		assertTrue(TimeLog.canTakeExperiment(user, exps));*/
	}

	@Test
	public void check_user_cant_take_the_exp_complete_3_trial() {
		/*
		User user = User.find.where().eq("username", "s550").findUnique();
		List<ExperimentSchedule> exps = ExperimentSchedule.getWorkingExperimentsByType(ExperimentType.BROWNPETERSON);
		assertFalse(TimeLog.canTakeExperiment(user, exps));
		*/
	}

        @Test
        public void trial_should_have_3_quiz(){
            Trial trial = Trial.find.byId(1L); 
            assertNotNull(trial);
            assertEquals(3,trial.quizzes.size());
        }
        
        @Test
        public void trial_can_reference_to_question() {
            Trial trial = Trial.find.byId(1L);
            assertEquals("q1w1", trial.quizzes.get(0).question.firstWord);
        }
}
