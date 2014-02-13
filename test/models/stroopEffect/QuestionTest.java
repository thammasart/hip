package models.stroopEffect;

import play.test.WithApplication;
import org.junit.*;
import static org.junit.Assert.*;
import static play.test.Helpers.*;

import java.util.List;
import java.util.ArrayList;

public class QuestionTest extends WithApplication{

    @Before
    public void setUp(){
        start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
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
        assertEquals("white",q.inkColor);
    }

    @Test
    public void saveAndQueryQuestion(){
        new Question().save();
        assertNotNull(Question.find.byId( new Long(1) ));
    }

    @Test
    public void shouldBeFoundByQuiz(){
        Quiz quiz = new Quiz();
        Question q = new Question("Black","Yellow");
        quiz.question = q;
        q.save();
        quiz.save();
        List<Quiz> quizList = new ArrayList<Quiz>();
        quizList.add(quiz);
        List<Question> questionList = Question.findInvoving(quizList);
        assertEquals(q.id,questionList.get(0).question_id);
    }
}
