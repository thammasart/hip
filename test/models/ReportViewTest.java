import org.junit.*;
import java.util.Date;
import static org.junit.Assert.*;
import play.test.WithApplication;
import static play.test.Helpers.*;
import models.brownPeterson.*;
import models.*;
import java.util.List;

public class ReportViewTest extends WithApplication{
    @Before
    public void setUp() {
        start(fakeApplication(inMemoryDatabase()));
    }

    @Test
    public void find_questions_involving_by_quizzes_success(){
        Trial trial = Trial.find.all().get(0);
        List<Quiz> quizzes = Quiz.findInvolving(trial);
        List<Question> questions = Question.findInvolving(quizzes);

        assertEquals(3, questions.size());
    }

    @Test
    public void find_answers_involving_by_quizzes_and_user_success(){
        User user = User.find.where().eq("username", "s550").findUnique();
        Trial trial = Trial.find.all().get(0);
        List<Quiz> quizzes = Quiz.findInvolving(trial);
        List<Answer> answers = Answer.findInvolving(user, quizzes);

        assertEquals(3, answers.size());
    }

    @Test
    public void calculate_totalUsedTime_from_answers_success() {
        User user = User.find.where().eq("username", "s550").findUnique();
        Trial trial = Trial.find.all().get(0);
        List<Quiz> quizzes = Quiz.findInvolving(trial);
        List<Answer> answers = Answer.findInvolving(user, quizzes);

        double totalUsedTime = Answer.calculateTotalUsedTime(answers);

        assertEquals(30, totalUsedTime, 0.001);
    }

    @Test
    public void calculate_score_from_answers_and_question_success() {
        User user = User.find.where().eq("username", "s550").findUnique();
        Trial trial = Trial.find.all().get(0);
        List<Quiz> quizzes = Quiz.findInvolving(trial);
        List<Answer> answers = Answer.findInvolving(user, quizzes);

        int totalScore = Answer.calculateTotalScore(answers);

        assertEquals(2, totalScore);
    }
}