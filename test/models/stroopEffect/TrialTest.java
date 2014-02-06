package models.stroopEffect;

import play.test.WithApplication;
import org.junit.*;
import static org.junit.Assert.*;
import static play.test.Helpers.*;
import java.util.Date;
import models.stroopEffect.*;
import models.*;

public class TrialTest extends WithApplication{

    @Before
    public void setUp(){
        start(fakeApplication(inMemoryDatabase()));
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
        ExperimentSchedule exs = new ExperimentSchedule("Name",3,new Date(114,1,10),new Date(114,1,20), ExperimentType.STROOPEFFECT);
        exs.save();
        Trial.create(exs).save();
        Trial.create(exs).save();
        Trial.create(exs).save();
        assertEquals(3, Trial.findInvolving(exs).size());
    }

}
