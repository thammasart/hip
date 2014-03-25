package models.unitTest;

import models.ExperimentSchedule;
import models.ExperimentType;
import models.TimeLog;
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
        Question q = Question.find.byId(1L);
        assertEquals('x',q.target);
        assertEquals('y',q.noise);
    }

    @Test
    public void questionShouldFindQuizzesCorrectly(){

        Question q = new Question('8','B');
        q.save();
        Quiz.create(10,2,q).save();
        Quiz.create(7,2,q).save();
        Quiz.create(5,1,q).save();

        List<Quiz> quizzes = Question.find.byId(1L).quizzes;

        assertEquals(3,quizzes.size());
        assertEquals(10,quizzes.get(0).displayTime);
        assertEquals(7,quizzes.get(1).displayTime);
        assertEquals(5,quizzes.get(2).displayTime);

    }

    @Test
    public void createQuizNotNull(){
        assertNotNull(new Quiz());
    }

    @Test
    public void createQuizWithParameter(){
        Quiz q = new Quiz(5,2);
        assertEquals(5,q.displayTime);
        assertEquals(2,q.noOfTarget);
    }

    @Test
    public void createQuizAndSaveComplete(){
        new Quiz(2,2).save();
        assertEquals(1,Quiz.find.findRowCount());
    }

    @Test
    public void saveAndRetrieveQuizComplete(){
        new Quiz(5,2).save();
        Quiz q = Quiz.find.byId(1L);
        assertEquals(5,q.displayTime);
        assertEquals(2,q.noOfTarget);
    }


    @Test
    public void quizCanAccessToQuestion(){
        Quiz quiz = new Quiz(5,2);
        Question q = new Question('x','y');
        quiz.question = q;
        q.save();
        quiz.save();
        Quiz quiz2 = Quiz.find.byId(1L);
        Question question = quiz2.question;
        assertEquals(q,question);
        assertEquals('x',question.target);
        assertEquals('y',question.noise);
    }

    @Test
    public void createQuizByMethodCreate(){
        Question q = new Question('x','y');
        q.save();
        Quiz.create(5,2,q).save();
        Quiz quiz = Quiz.find.byId(1L);

        assertEquals(5,quiz.displayTime);
        assertEquals(2,quiz.noOfTarget);
        assertEquals(q,quiz.question);
    }

    @Test
    public void createTrialNotNull(){
        assertNotNull(new Trial());
    }

    @Test
    public void createTrialWithParameter(){
        Trial t = new Trial();
        t.id = 1;
        assertEquals(1,t.id);
    }

    @Test
    public void createTrialAndSaveComplete(){
        new Trial().save();
        assertEquals(1, Trial.find.findRowCount());
    }

    @Test
    public void saveAndRetrieveTrialComplete(){
        new Trial().save();
        Trial t = Trial.find.byId(1L);
        assertEquals(1,t.id);
    }

    @Test
    public void createTrialWithExperimentSchedule(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        Date lastYearDate = calendar.getTime();
        calendar.add(Calendar.YEAR, +2);
        Date nextYearDate = calendar.getTime();
        new ExperimentSchedule("Experiment 1", 5, lastYearDate,
                nextYearDate, ExperimentType.SIGNALDETECTION).save();
        ExperimentSchedule ex = ExperimentSchedule.find.byId(1L);
        Trial t = Trial.create(ex);
        assertNotNull(t);
        assertEquals(ex.name,t.schedule.name);
    }

    @Test
    public void trialShoudBeFindByTrialsInvolving(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        Date lastYearDate = calendar.getTime();
        calendar.add(Calendar.YEAR, +2);
        Date nextYearDate = calendar.getTime();
        new ExperimentSchedule("Experiment 1", 5, lastYearDate,
                nextYearDate, ExperimentType.SIGNALDETECTION).save();
        ExperimentSchedule ex = ExperimentSchedule.find.byId(1L);
        Trial.create(ex).save();
        Trial.create(ex).save();
        Trial.create(ex).save();
        assertEquals(3, Trial.findInvolving(ex).size());
    }

    @Test
    public void trialShoudBeFindByTrialsInvolvingAndReferCorrectly(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        Date lastYearDate = calendar.getTime();
        calendar.add(Calendar.YEAR, +2);
        Date nextYearDate = calendar.getTime();
        new ExperimentSchedule("Experiment 1", 5, lastYearDate,
                nextYearDate, ExperimentType.SIGNALDETECTION).save();
        ExperimentSchedule ex = ExperimentSchedule.find.byId(1L);
        Trial.create(ex).save();
        Trial.create(ex).save();
        Trial.create(ex).save();
        List<Trial> trials = Trial.findInvolving(ex);
        assertEquals(1, trials.get(0).id);
        assertEquals(2, trials.get(1).id);
        assertEquals(3, trials.get(2).id);
    }

    @Test
    public void createTimeLogWithTrialSuccess(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        Date startDate = calendar.getTime();
        calendar.add(Calendar.YEAR, +2);
        Date finishDate = calendar.getTime();
        new Trial().save();
        Trial t = Trial.find.byId(1L);
        TimeLog timeLog = new TimeLog(startDate,finishDate);
        timeLog.trialId = t.id;
        timeLog.save();

        assertEquals(t.id,timeLog.trialId);
    }

}
