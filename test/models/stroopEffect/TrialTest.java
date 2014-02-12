package models.stroopEffect;

import play.test.WithApplication;
import org.junit.*;
import static org.junit.Assert.*;
import static play.test.Helpers.*;
import java.util.Date;
import java.util.Calendar;
import models.stroopEffect.*;
import models.*;

public class TrialTest extends WithApplication{

    @Before
    public void setUp(){
        start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
    }

    @Test
    public void createTrial(){
        Trial trial = new Trial();
        assertNotNull(trial);
    }

    @Test
    public void createTrailWithProperty(){
        Trial trial = new Trial(10,QuestionType.THAI,true);
        assertEquals(10,trial.appear_time);
    }

    @Test
    public void saveAndQueryTrial(){
        new Trial().save();
        assertNotNull(Trial.findById(1));
    }

    @Test
    public void shouldBeFoundByExperimentSchedule(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        Date lastYearDate = calendar.getTime();
        calendar.add(Calendar.YEAR, +2);
        Date nextYearDate = calendar.getTime();
        ExperimentSchedule exs = new ExperimentSchedule("Name",3,lastYearDate,nextYearDate, ExperimentType.STROOPEFFECT);
        exs.save();
        Trial.create(exs).save();
        Trial.create(exs).save();
        Trial.create(exs).save();
        assertEquals(3, Trial.findInvolving(exs).size());
    }

}
