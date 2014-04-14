package models.unitTest;

import models.magicNumber7.*;
import models.*;

import play.test.WithApplication;
import org.junit.*;
import static org.junit.Assert.*;
import static play.test.Helpers.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;

public class MagicNumber7UnitTest extends WithApplication{
	@Before
    public void setUp(){
        start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
        Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, -1);
		Date lastYearDate = calendar.getTime();
		calendar.add(Calendar.YEAR, +2);
		Date nextYearDate = calendar.getTime();
		new ExperimentSchedule("Experiment 1", 1, lastYearDate,nextYearDate, ExperimentType.MAGICNUMBER7).save();
		new User("123", "123").save();
    }

    @Test
    public void createQuestionAndNotNull(){
    	assertNotNull(new Question("ABCDE", QuestionType.ENGLISH));
    }
}