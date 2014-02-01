package controllers;

import models.brownPeterson.Question;
import models.brownPeterson.Quiz;
import models.brownPeterson.Answer;
import models.brownPeterson.Trial;
import models.User;
import models.ExperimentSchedule;
import play.*;
import play.mvc.*;
import play.data.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import views.html.brownPeterson.*;
import views.html.iframe.*;

public class BrownPeterson extends Controller {
    public static List<Answer> answerList = new ArrayList<Answer>();
    public static List<Question> questions = null;
    private static List<ExperimentSchedule> currentEx = ExperimentSchedule.getAllWorkingExperiment();
    public static int questionNumber = 0;

        public static Result task(){
            return ok(brown_peterson_info.render());
        }

        public static Result renderShortTermMemoryBrownPetersonTaskIframe(){
            return ok(brown_peterson_iframe.render());
        }
        public static Result renderShortTermMemoryBrownPetersonTaskProc(){
            return ok(brown_peterson_proc.render());
        }
        public static Result renderShortTermMemoryBrownPetersonTaskProcIframe(){
            return ok(brown_peterson_proc_iframe.render());
        }
        public static Result taskExperiment(long trialId){
            List<Quiz> quizzes = Quiz.find.where().eq("trial_id", trialId).findList();
            User user = User.find.where().eq("username", session().get("username")).findUnique();
            questions = Question.findInvolving(quizzes);

            Form<Answer> filledForm = Form.form(Answer.class);
            Answer answer = filledForm.bindFromRequest().get();
            if (questionNumber > 0){
                answerList.add(answer);
            }
            if (questionNumber+1 <= questions.size())
                return ok(brown_peterson_exp.render(questions.get(questionNumber),quizzes.get(questionNumber++).initCountdown, trialId));
            else{
                List<Answer> answerListTemp = new ArrayList<Answer>(answerList);
                for(int i = 0; i < answerListTemp.size(); i++){
                    answerListTemp.get(i).user = user;
                    answerListTemp.get(i).quiz = quizzes.get(i);
                    answerListTemp.get(i).save();
                }
                questionNumber = 0;
                answerList.clear();
                return redirect(routes.BrownPeterson.report(user.username, trialId));
            }
        }
        public static Result report(String username, Long trialId){
            if(username.equals("") || trialId == 0){
                return redirect(controllers.routes.BrownPeterson.task());
            }
            User user = User.find.where().eq("username", username).findUnique();
            Trial trial = Trial.find.where().eq("id", trialId).findUnique();
            List<Quiz> quizzes = Quiz.findInvolving(trial);
            List<Question> questions = Question.findInvolving(quizzes);
            List<Answer> answers = Answer.findInvolving(user, quizzes);

            double totalUsedTime = Answer.calculateTotalUsedTime(answers);  
            int score = Answer.calculateTotalScore(answers);
            return ok(report.render(score,totalUsedTime,quizzes.size(), "Report"));
        }

}
