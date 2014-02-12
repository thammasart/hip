package models.brownPetersonTest;



import play.test.WithApplication;
import static play.test.Helpers.*;
import org.junit.*;
import static org.junit.Assert.*;
import models.brownPeterson.*;
import models.*;
import java.util.Date;
import java.util.List;
import java.util.Calendar;

public class TrialTest extends WithApplication {
	@Before
	public void setUp() {
		start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
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
	public void shoudBeFindByTrialsInvolving(){
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
	public void shoudBeFindByTrialInvolvingAndReferCorrectly(){
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
