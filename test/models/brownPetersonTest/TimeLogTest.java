package models.brownPetersonTest;


import models.*;
import models.brownPeterson.*;
import org.junit.*;
import static org.junit.Assert.*;
import play.test.WithApplication;
import static play.test.Helpers.*;
import java.util.List;
import java.util.Date;


public class TimeLogTest extends WithApplication{
	@Before
	public void setUp(){
		start(fakeApplication(inMemoryDatabase(), fakeGlobal()));

	}

	@Test
	public void shouldCreateAndNotNull(){
		new Trial().save();
		Trial trial = Trial.findById(1);
		new User("123","Secret").save();
		User user = User.find.byId("123");

		assertNotNull(TimeLog.create(new Date(), new Date(), user, trial));
	}

	@Test
	public void getUsersTakeTrialInWorkingExperiments() {
		/*
		Date date = new Date();
		new User("s550", "1234").save();
		new ExperimentSchedule("Experiment 1", 5, new Date(date.getYear()-2, 0, 1), new Date(date.getYear()-1, 0, 31), ExperimentType.BROWNPETERSON).save();
		new ExperimentSchedule("Experiment 2", 3, new Date(date.getYear()-1, 0, 1), new Date(date.getYear()+1, 0, 31), ExperimentType.BROWNPETERSON).save();
		new ExperimentSchedule("Experiment 2", 3, new Date(date.getYear()+1, 0, 1), new Date(date.getYear()+1, 0, 31), ExperimentType.BROWNPETERSON).save();
		User user = User.find.byId("s550");
		ExperimentSchedule ex = ExperimentSchedule.find.byId(1L);
		ExperimentSchedule ex2 = ExperimentSchedule.find.byId(new Long(2));
		Trial.create(ex).save();
		Trial.create(ex).save();
		Trial.create(ex).save();
		Trial.create(ex2).save();
		Trial.create(ex2).save();
		Trial.create(ex2).save();
		TimeLog.create(new Date(date.getYear()-2, 0, 10), new Date(date.getYear()-2, 0, 10), user, Trial.find.byId(new Long(1)));
		TimeLog.create(new Date(date.getYear()-2, 0, 15), new Date(date.getYear()-2, 0, 15), user, Trial.find.byId(new Long(2)));
		TimeLog.create(new Date(date.getYear()-2, 0, 20), new Date(date.getYear()-2, 0, 20), user, Trial.find.byId(new Long(3)));
		TimeLog.create(new Date(date.getYear()-1, 0, 10), new Date(date.getYear()+1, 0, 10), user, Trial.find.byId(new Long(4)));
		TimeLog.create(new Date(date.getYear()-1, 0, 15), new Date(date.getYear()+1, 0, 15), user, Trial.find.byId(new Long(5)));
		TimeLog.create(new Date(date.getYear()-1, 0, 20), new Date(date.getYear()+1, 0, 20), user, Trial.find.byId(new Long(6)));
		assertEquals(3, TimeLog.getUsersTakeTrialFromWorkingExperiments(user).size());
		*/
	}
}