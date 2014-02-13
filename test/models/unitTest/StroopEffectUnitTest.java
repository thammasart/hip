package models.unitTest;

import models.stroopEffect.*;
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

    @Test
    public void createAnswer(){
        Answer answer = new Answer("BLACK",10);
        assertNotNull(answer);
    }

    @Test
    public void createAnswerWithProperty(){
        Answer answer = new Answer("BLACK",10.0);
        assertEquals("BLACK",answer.answer);
    }

    @Test
    public void answer_save_And_Query(){
        new Answer("BLACK",10.0).save();
        assertNotNull(Answer.find.byId(new Long(1)));
    }

    @Test
    public void createAndConnectWithQuizAndUser(){
        Quiz q = new Quiz();
        q.save();
        new User("s550","1234").save();
        User u = User.find.where().eq("username","s550").findUnique();
        Answer ans = Answer.create("BLACK",10,u,q);
        ans.save();
        assertEquals(q.id,ans.quiz.id);
        assertEquals(u.username,ans.user.username);
    }

    @Test
    public void createQuestionWithProperty(){
        Question q = new Question("WHITE","white");
        assertEquals("WHITE",q.colorWord);
        assertEquals("white",q.inkColor);
    }

    @Test
    public void saveAndQueryQuestion(){
        new Question().save();
        assertNotNull(Question.find.byId( new Long(1) ));
    }

    @Test
    public void question_should_Be_Found_By_Quiz(){
        Quiz quiz = new Quiz();
        Question q = new Question("Black","Yellow");
        quiz.question = q;
        q.save();
        quiz.save();
        List<Quiz> quizList = new ArrayList<Quiz>();
        quizList.add(quiz);
        List<Question> questionList = Question.findInvoving(quizList);
        assertEquals(q.id,questionList.get(0).id);
    }
    @Test
    public void createQuiz(){
        Quiz q = new Quiz();
        assertNotNull(q);
    }

    @Test
    public void quiz_should_save_And_Query(){
        new Quiz().save();
        assertNotNull(Quiz.find.byId(new Long(1)));
    }

    @Test
    public void quiz_create_and_connect_relation_success() {
        Trial t = new Trial();
        t.save();
        Question q = new Question();
        q.save();
        Quiz quiz = Quiz.create(t,q);
        quiz.save();
        assertEquals(t.id,quiz.trial.id);
        assertEquals(q.id,quiz.question.id);
    }
    @Test
    public void createTrial(){
        Trial trial = new Trial();
        assertNotNull(trial);
    }

    @Test
    public void createTrailWithProperty(){
        Trial trial = new Trial(10,QuestionType.THAI,true);
        assertEquals(10,trial.appearTime);
    }

    @Test
    public void saveAndQueryTrial(){
        new Trial().save();
        assertNotNull(Trial.findById(1));
    }

    @Test
    public void trial_should_Be_Found_By_ExperimentSchedule(){
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
