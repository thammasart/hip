package models.unitTest;

import models.ExperimentSchedule;
import models.ExperimentType;
import models.TimeLog;
import models.User;
import models.signalDetection.*;
import play.test.WithApplication;
import org.junit.*;
import static org.junit.Assert.*;
import static play.test.Helpers.*;
import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;



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
        new Question('8','B').save();
        Question q = Question.find.byId(1L);
        new Trial().save();
        Trial trial = Trial.find.byId(1L);
        q.save();
        Quiz.create(1.0,2,15,trial,q).save();
        Quiz.create(0.7,2,15,trial,q).save();
        Quiz.create(0.5,1,15,trial,q).save();

        List<Quiz> quizzes = Question.find.byId(1L).quizzes;

        assertEquals(3,quizzes.size());
        assertEquals(1.0,quizzes.get(0).displayTime,0.001);
        assertEquals(0.7,quizzes.get(1).displayTime,0.001);
        assertEquals(0.5,quizzes.get(2).displayTime,0.001);

    }

    @Test
    public void createQuizNotNull(){
        assertNotNull(new Quiz());
    }

    @Test
    public void createQuizWithParameter(){
        Quiz q = new Quiz(0.5,2,15);
        assertEquals(0.5,q.displayTime,0.001);
        assertEquals(2,q.noOfTarget);
    }

    @Test
    public void createQuizAndSaveComplete(){
        new Quiz(2,2,15).save();
        assertEquals(1,Quiz.find.findRowCount());
    }

    @Test
    public void saveAndRetrieveQuizComplete(){
        new Quiz(0.5,2,15).save();
        Quiz q = Quiz.find.byId(1L);
        assertEquals(0.5,q.displayTime,0.001);
        assertEquals(2,q.noOfTarget);
    }


    @Test
    public void quizCanAccessToQuestion(){
        Quiz quiz = new Quiz(5,2,15);
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
        new Trial().save();
        Quiz.create(0.5,2,15,Trial.find.byId(1L),q).save();
        Quiz quiz = Quiz.find.byId(1L);

        assertNotNull(quiz);
        assertEquals(0.5,quiz.displayTime,0.001);
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

    @Test 
    public void answerShouldCreateAndNotNull(){
        new Trial().save();
        new Question('x','y').save();
        Trial trial = Trial.find.byId(1L);
        Question question = Question.find.byId(1L);
        Quiz.create(200, 7, 15, trial, question).save();
        Quiz quiz = Quiz.find.byId(new Long(1));
        new User("123","Secret").save();
        User user = User.find.byId("123");

        assertNotNull(Answer.create(true, 13.9, user, quiz));
    }

    @Test 
    public void answerShouldCreateResultIsCorrect(){
        new Trial().save();
        new Question('x','y').save();
        Trial trial = Trial.find.byId(1L);
        Question question = Question.find.byId(1L);
        Quiz.create(0.7, 7, 15, trial, question).save();
        Quiz.create(1.0, 0, 15, trial, question).save();
        Quiz quiz = Quiz.find.byId(1L);
        Quiz quiz2 = Quiz.find.byId(2L);
        new User("123","Secret").save();
        User user = User.find.byId("123");
        Answer answer =  Answer.create(true, 13.9, user, quiz);
        Answer answer2 =  Answer.create(true, 13.9, user, quiz2);
        assertEquals(true,answer.isCorrect);
        assertEquals(false,answer2.isCorrect);
    }

    @Test
    public void calculateTotalScoreShouldCorrect(){
        new Trial().save();
        new Question('x','y').save();
        Trial trial = Trial.find.byId(1L);
        Question question = Question.find.byId(1L);
        Quiz.create(0.7, 7, 15, trial, question).save();
        Quiz.create(0.85, 0, 15, trial, question).save();
        Quiz.create(1.0, 0, 15, trial, question).save();
        Quiz quiz = Quiz.find.byId(1L);
        Quiz quiz2 = Quiz.find.byId(2L);
        Quiz quiz3 = Quiz.find.byId(3L);
        new User("123","Secret").save();
        User user = User.find.byId("123");
        Answer answer =  Answer.create(true, 13.9, user, quiz);
        Answer answer2 =  Answer.create(true, 2.9, user, quiz2);
        Answer answer3 =  Answer.create(false, 0.9, user, quiz3);
        List<Answer> answers = new ArrayList<Answer>();
        answers.add(answer);
        answers.add(answer2);

        assertEquals(1,Answer.calculateTotalScore(answers));

        answers.add(answer3);
        assertEquals(2,Answer.calculateTotalScore(answers));
    }

    @Test
    public void calculateTotalUsedTimeShouldCorrect(){
        new Trial().save();
        new Question('x','y').save();
        Trial trial = Trial.find.byId(1L);
        Question question = Question.find.byId(1L);
        Quiz.create(0.7, 7, 15, trial, question).save();
        Quiz.create(0.85, 0, 15, trial, question).save();
        Quiz.create(1.0, 0, 15, trial, question).save();
        Quiz quiz = Quiz.find.byId(1L);
        Quiz quiz2 = Quiz.find.byId(2L);
        Quiz quiz3 = Quiz.find.byId(3L);
        new User("123","Secret").save();
        User user = User.find.byId("123");
        Answer answer =  Answer.create(true, 13.9, user, quiz);
        Answer answer2 =  Answer.create(true, 2.9, user, quiz2);
        Answer answer3 =  Answer.create(false, 0.9, user, quiz3);
        List<Answer> answers = new ArrayList<Answer>();
        answers.add(answer);
        answers.add(answer2);

        assertEquals(16.8,Answer.calculateTotalUsedTime(answers),0.001);

        answers.add(answer3);
        assertEquals(17.7,Answer.calculateTotalUsedTime(answers),0.001);
    }

    @Test
    public void createAnswerAndSaveComplete(){
        new Trial().save();
        new Question('x','y').save();
        Trial trial = Trial.find.byId(1L);
        Question question = Question.find.byId(1L);
        Quiz.create(0.7, 7, 15, trial, question).save();
        Quiz quiz = Quiz.find.byId(1L);

        new User("123","Secret").save();
        User user = User.find.byId("123");
        Answer.create(true, 13.9, user, quiz).save();
        Answer answer =  Answer.find.byId(1L);

        assertNotNull(answer);
        assertEquals(user,answer.user);
        assertEquals(quiz,answer.quiz);
        assertEquals(13.9,answer.usedTime,0.001);
        assertEquals(true,answer.answer);
    }

    @Test
    public void getAnswerFromUserAndTrialComplete(){
        new Trial().save();
        new Question('x','y').save();
        Trial trial = Trial.find.byId(1L);
        Question question = Question.find.byId(1L);
        Quiz.create(0.7, 7, 15, trial, question).save();
        Quiz quiz = Quiz.find.byId(1L);

        new User("123","Secret").save();
        User user = User.find.byId("123");
        Answer.create(true, 13.9, user, quiz).save();


        List<Answer> answers = Answer.findInvolving(user,trial.quizzes);
        Answer answer =  answers.get(0);
        assertEquals(1,answers.size());
        assertEquals(user,answer.user);
        assertEquals(quiz,answer.quiz);
        assertEquals(13.9,answer.usedTime,0.001);
        assertEquals(true,answer.answer);

    }

}
