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
}