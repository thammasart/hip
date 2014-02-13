package models.stroopEffect;

import models.User;
import play.test.WithApplication;
import org.junit.*;
import static org.junit.Assert.*;
import static play.test.Helpers.*;

import java.util.Date;
import java.util.Calendar;

public class TimeLogTest extends WithApplication{

    @Before
    public void setUp(){
        start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
    }

    @Test
    public void createTimeLogSuccess(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        Date startDate = calendar.getTime();
        calendar.add(Calendar.YEAR, +2);
        Date finishDate = calendar.getTime();
        TimeLog timeLog = new TimeLog();
        TimeLog timeLog1 = new TimeLog(startDate,finishDate);
        timeLog1.save();

        assertNotNull(timeLog);
        assertNotNull(TimeLog.find.byId(1L));
        assertEquals(startDate, timeLog1.startTime);
        assertEquals(finishDate,timeLog1.endTime);
    }

    @Test
    public void connect_user_and_trial() {
        User u = new User("adisak", "abc123");
        u.save();
        Trial t = new Trial();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        Date startDate = calendar.getTime();
        calendar.add(Calendar.YEAR, +2);
        Date finishDate = calendar.getTime();
        TimeLog timeLog = TimeLog.create(startDate,finishDate, u, t);

        assertEquals(u.username, timeLog.user.username);
        assertEquals(t.id, timeLog.trial.id);
    }
}
