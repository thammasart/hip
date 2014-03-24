package models.unitTest;

import models.signalDetection.*;
import play.test.WithApplication;
import org.junit.*;
import static org.junit.Assert.*;
import static play.test.Helpers.*;
import java.util.List;
import java.util.Date;
import java.util.Calendar;

public class SignalDetectionUnitTest extends WithApplication {

    @Before
    public void setUp(){
        start(fakeApplication(inMemoryDatabase(), fakeGlobal()));

    }

    @Test
    public void createQuestionNotNull(){
        assertNotNull(new Question());
    }

    @Test
    public void createQuestionWithParameter(){
        Question q = new Question('x','y');
        assertEquals('x',q.target);
        assertEquals('y',q.noise);
    }

    @Test
    public void createQuestionAndSaveComplete(){
        new Question('x','y').save();
        assertEquals(1,Question.find.findRowCount());
    }

    @Test
    public void saveAndRetrieveQuestionComplete(){
        new Question('x','y').save();
        Question q = Question.find.where().eq("id", new Long(1)).findUnique();
        assertEquals('x',q.target);
        assertEquals('y',q.noise);
    }

}
