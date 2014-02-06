package models.stroopEffect;

import models.ExperimentType;
import play.test.WithApplication;
import org.junit.*;
import static org.junit.Assert.*;
import static play.test.Helpers.*;

public class QuestionTest extends WithApplication{

    @Before
    public void setUp(){
        start(fakeApplication(inMemoryDatabase()));
    }

    @Test
    public void createQuesiton(){
        Question q = new Question();
        assertNotNull(q);
    }

    @Test
    public void createQuestionWithProperty(){
        Question q = new Question("WHITE","white");
        assertEquals("WHITE",q.color_word);
        assertEquals("white",q.ink_color);
    }

    @Test
    public void saveAndQueryQuestion(){
        new Question().save();
        assertNotNull(Question.find.byId( new Long(1) ));
    }

    @Test
    public void shouldBeFoundBy(){
        Quiz quiz = new Quiz();
        quiz.save();
    }
}
