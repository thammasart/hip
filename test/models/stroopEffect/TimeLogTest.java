package models.stroopEffect;

import models.User;
import play.test.WithApplication;
import org.junit.*;
import static org.junit.Assert.*;
import static play.test.Helpers.*;

import java.util.Date;

public class TimeLogTest extends WithApplication{

    @Before
    public void setUp(){
        start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
    }

    @Test
    public void createTimeLogSuccess(){
        TimeLog timeLog = new TimeLog();
        Date start = new Date(new Date().getYear()-1,1,1);
        Date finish = new Date(new Date().getYear()+1,1,2);
        TimeLog timeLog1 = new TimeLog(start,finish);
        timeLog1.save();

        assertNotNull(timeLog);
        assertNotNull(TimeLog.find.byId(new Long(1)));
        assertEquals(start, timeLog1.start_time);
        assertEquals(finish,timeLog1.end_time);
    }

    @Test
    public void connect_user_and_trial() {
        User u = new User("adisak", "abc123");
        u.save();
        Trial t = new Trial();
        Date start = new Date(new Date().getYear()-1,1,1);
        Date finish = new Date(new Date().getYear()+1,1,2);
        TimeLog timeLog = TimeLog.create(start,finish, u, t);

        assertEquals(u.username, timeLog.user.username);
        assertEquals(t.trial_id, timeLog.trial.trial_id);
    }
}
