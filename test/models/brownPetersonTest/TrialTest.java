import play.test.WithApplication;
import static play.test.Helpers.*;
import org.junit.*;
import static org.junit.Assert.*;
import models.brownPeterson.*;
import models.*;
import java.util.Date;
import java.util.List;


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
		Date date = new Date();
		new ExperimentSchedule("Experiment 1", 5, new Date(date.getYear()-1, 0, 1), 
			new Date(date.getYear()+1, 0, 31), ExperimentType.BROWNPETERSON).save();
		ExperimentSchedule ex = ExperimentSchedule.find.byId(new Long(1));
		assertNotNull(Trial.create(ex));
	}

	@Test
	public void trialShouldBeFindById() {
		new Trial().save();
		assertEquals(1, Trial.findById(1).id);
	}

	@Test
	public void shoudBeFindByTrialsInvolving(){
		Date date = new Date();
		new ExperimentSchedule("Experiment 1", 5, new Date(date.getYear()-1, 0, 1), 
			new Date(date.getYear()+1, 0, 31), ExperimentType.BROWNPETERSON).save();
		ExperimentSchedule ex = ExperimentSchedule.find.byId(new Long(1));
		Trial.create(ex).save();
		Trial.create(ex).save();
		Trial.create(ex).save();

		assertEquals(3, Trial.findInvolving(ex).size());
	}

	@Test
	public void shoudBeFindByTrialInvolvingAndReferCorrectly(){
		Date date = new Date();
		new ExperimentSchedule("Experiment 1", 5, new Date(date.getYear()-1, 0, 1), 
			new Date(date.getYear()+1, 0, 31), ExperimentType.BROWNPETERSON).save();
		ExperimentSchedule ex = ExperimentSchedule.find.byId(new Long(1));
		Trial.create(ex).save();
		Trial.create(ex).save();
		Trial.create(ex).save();

		List<Trial> trials = Trial.findInvolving(ex);
		assertEquals(1, trials.get(0).id);
		assertEquals(2, trials.get(1).id);
		assertEquals(3, trials.get(2).id);
	}

	
}
