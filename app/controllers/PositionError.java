package controllers;

import models.ExperimentSchedule;
import models.User;
import models.positionError.*;

import play.*;
import play.mvc.*;
import play.data.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import views.html.positionError.*;
import views.html.iframe.*;
import java.util.Date;

public class PositionError extends Controller{
    private static final Form<Answer> answerForm = Form.form(Answer.class);
    @Security.Authenticated(Secured.class)
    public static Result info(){
        User user = User.find.byId(request().username());
        return ok(info.render(user));
    }
    //แสดงกรอบในหน้าข้อมูลการทดลอง
    @Security.Authenticated(Secured.class)
    public static Result infoIframe(){
        return ok(position_error_iframe.render());
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
        return ok(position_error_proc_iframe.render());
    }
    @Security.Authenticated(Secured.class)
    public static Result demoPage(){
        Question question = new Question("ABCDEFGHIJ",QuestionType.ENGLISH);
        Quiz quiz = new Quiz(10,question,null);
        return ok(demo.render(quiz,0.5,1.0));
    }
    @Security.Authenticated(Secured.class)
    public static Result demoReport(){
        Form<Answer> boundForm = answerForm.bindFromRequest();
        User user = User.find.byId(session().get("username"));
        Answer answer = boundForm.get();
        int score = 0;
        if (answer.isCorrect)
            score++;
        return ok(demoReport.render(score,answer.usedTime,1,"Demo Report",user));
    }

    //แสดงหน้าการทดลอง
    @Security.Authenticated(Secured.class)
    public static Result experiment(long trialId,int questionNo){
        return ok(exp.render(Trial.find.byId(trialId), questionNo));
    }

    @Security.Authenticated(Secured.class)
    public static Result saveAnswer(long trialId, int questionNo){

        Form<Answer> boundForm = answerForm.bindFromRequest();
        User user = User.find.byId(session().get("username"));
        Trial trial = Trial.find.byId(trialId);

        if(boundForm.hasErrors()){
            flash("error", "please correct the form above.");
            return badRequest(views.html.home.render(user));
        }

        Answer answer = boundForm.get();
        answer.user = user;
        answer.quiz = trial.quizzes.get(questionNo);
        answer.save();

        questionNo++;
        if(questionNo < 3){
            return redirect(routes.PositionError.experiment(trialId, questionNo));
        }
        return redirect(routes.PositionError.report(user.username, trialId));
    }

    //แสดงหน้าผลลัพธ์การทดลอง
    @Security.Authenticated(Secured.class)
    public static Result report(String username, long trialId){
        if(username.equals("") || trialId == 0){
            return redirect(controllers.routes.AttentionBlink.info());
        }

        User user = User.find.byId(username);
        Trial trial = Trial.find.byId(trialId);
        List<Answer> answers = Answer.findInvolving(user, trial.quizzes);
        double totalUsedTime = Answer.calculateTotalUsedTime(answers);
        int score = Answer.calculateTotalScore(answers);
        return ok(report.render(score,totalUsedTime,trial.quizzes.size(), "Report", user));
    }

    //ตรวจสอบว่าผู้ใช้ทำการทดลองหรือยัง
    @Security.Authenticated(Secured.class)
    public static Result checkUserTakeRepeatExperiment() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        if(user == null) {
            return redirect(routes.Application.index());
        }

        if(models.TimeLog.isRepeatTrial(user, 1, ExperimentSchedule.find.byId(8L))) {
            flash("repeat", "คุณเคยทำการทดลองนี้แล้ว หากต้องการทำต่อโปรดติดต่อผู้ดูแลระบบ");
            return ok(proc.render(user));
        }
        models.TimeLog.create(new Date(), user, 1,ExperimentSchedule.find.byId(8L)).save();
        return redirect(routes.PositionError.experiment(1,0));
    }

}
