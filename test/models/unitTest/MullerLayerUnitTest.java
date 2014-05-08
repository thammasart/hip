package models.unitTest;

import models.mullerLayer.*;
import models.ExperimentSchedule;
import models.ExperimentType;
import models.User;
import play.test.WithApplication;
import org.junit.*;
import static org.junit.Assert.*;
import static play.test.Helpers.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;

public class MullerLayerUnitTest extends WithApplication {

    @Before
    public void setUp(){
        start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        Date lastYearDate = calendar.getTime();
        calendar.add(Calendar.YEAR, +2);
        Date nextYearDate = calendar.getTime();
        new ExperimentSchedule("Experiment 1", 3, lastYearDate,
         		nextYearDate, ExperimentType.MULLERLAYER).save();
        new User("admin","admin").save();
    }

    @Test
    public void createQuestionShouldNotNull(){
    	Question q = new Question();
    	assertNotNull(q);
    }

    @Test
    public void createTrialShouldNotNull(){
        ExperimentSchedule exp = ExperimentSchedule.find.byId(1L);
        Trial t = new Trial(exp);
        assertNotNull(t);
        assertEquals(exp, t.schedule);
    }

}