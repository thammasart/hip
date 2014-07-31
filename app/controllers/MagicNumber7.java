package controllers;

import models.magicNumber7.Answer;
import models.magicNumber7.Question;
import models.magicNumber7.Quiz;
import models.magicNumber7.Trial;
import models.signalDetection.*;
import play.*;
import play.mvc.*;
import play.data.*;

import models.User;
import models.ExperimentSchedule;
import models.TimeLog;
import models.magicNumber7.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import views.html.magicNumber7.*;
import views.html.iframe.*;
import java.util.Date;

public class MagicNumber7 extends Controller{
    private static final Form<Answer> answerForm = Form.form(Answer.class);
    //แสดงหน้าข้อมูลการทดลอง
    @Security.Authenticated(Secured.class)
    public static Result info(){
        User user = User.find.byId(request().username());
        return ok(info.render(user));
    }
    //แสดงกรอบในหน้าข้อมูลการทดลอง
    @Security.Authenticated(Secured.class)
    public static Result infoIframe(){
        return ok(magic_number7_iframe.render());
    }
    //แสดงหน้าขั้นตอนการทดลอง
    @Security.Authenticated(Secured.class)
    public static Result proc(){
        User user = User.find.byId(request().username());
        return ok(proc.render(user));
    }
    //แสดงกรอบในหน้าขั้นตอนการทดลอง
    @Security.Authenticated(Secured.class)
    public static Result procIframe(){
        return ok(magic_number7_proc_iframe.render());
    }

    @Security.Authenticated(Secured.class)
    public static Result demoPage(){
        Trial trial = new Trial(null);
        Question question = new Question("ABOPQAF",null);
        Quiz quiz = Quiz.create(trial,question,2,0,7);
        trial.quizzes = new ArrayList<Quiz>();
        trial.quizzes.add(quiz);
        return ok(demo.render(trial, 0));
    }
    //แสดงหน้าการทดลอง
    @Security.Authenticated(Secured.class)
    public static Result experiment(long trialId,int questionNo){
        return ok(exp.render(Trial.find.byId(trialId), questionNo));
    }

    @Security.Authenticated(Secured.class)
    public static Result saveAnswer(long trialId, int questionNo){
        DynamicForm reportData = Form.form().bindFromRequest();
        double time = Double.parseDouble(reportData.get("usedTime"));
        int score = Integer.parseInt(reportData.get("score"));
        String answer = reportData.get("answer");

        User user = User.find.byId(session().get("username"));
        Trial trial = Trial.find.byId(trialId);

        if(reportData.hasErrors()){
            flash("error", "please correct the form above.");
            return badRequest(views.html.home.render(user));
        }

        Answer ans = Answer.create(user,trial.quizzes.get(questionNo),answer,time,score);
        ans.save();

        questionNo++;
        if(questionNo < trial.numberOfQuiz){
            return redirect(routes.MagicNumber7.experiment(trialId, questionNo));
        }
        TimeLog timeLog = TimeLog.findByUserAndTrialId(user, trialId,trial.schedule);
        timeLog.endTime = new Date();
        timeLog.update();
        return redirect(routes.MagicNumber7.report(user.username, trialId));
    }

    //แสดงหน้าผลลัพธ์การทดลอง
    @Security.Authenticated(Secured.class)
    public static Result report(String username, long trialId){
        if(username.equals("") || trialId == 0){
            return redirect(controllers.routes.MagicNumber7.info());
        }
        User user = User.find.byId(username);
        Trial trial = Trial.find.byId(trialId);
        List<Answer> answers = Answer.findInvolving(user, trial.quizzes);
        double totalUsedTime = Answer.calculateTotalUsedTime(answers);
        return ok(report.render(answers,totalUsedTime, "Report", user));
    }

    @Security.Authenticated(Secured.class)
    public static Result demoReport(int length){
        DynamicForm reportData = Form.form().bindFromRequest();
        User user = User.find.byId(session().get("username"));
        double time = Double.parseDouble(reportData.get("usedTime"));
        int score = Integer.parseInt(reportData.get("score"));
        return ok(demoReport.render(score,time,length,"Demo Report",user));
    }

}
