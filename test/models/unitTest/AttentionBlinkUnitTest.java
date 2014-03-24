package models.unitTest;

import models.attentionBlink.*;
import models.*;
import play.test.WithApplication;
import org.junit.*;
import static org.junit.Assert.*;
import static play.test.Helpers.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;

public class StroopEffectUnitTest extends WithApplication{

    @Before
    public void setUp(){
        start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
    }

    /*@Test
    public void createAnswer() {
    	Answer answer = new Answer();
    	assertNotNull(answer);
    }*/
}
