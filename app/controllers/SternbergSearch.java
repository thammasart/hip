package controllers;

import play.*;
import play.mvc.*;
import play.data.*;

import models.User;
import models.sternbergSearch.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import views.html.sternbergSearch.*;
import views.html.iframe.*;
import java.util.Date;

public class SternbergSearch extends Controller{

    //แสดงหน้าข้อมูลการทดลอง
    @Security.Authenticated(Secured.class)
    public static Result info(){
        User user = User.find.byId(request().username());
        return ok(info.render(user));
    }

    //แสดงกรอบในหน้าข้อมูลการทดลอง
    @Security.Authenticated(Secured.class)
    public static Result infoIframe(){
        return ok(sternberg_search_iframe.render());
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
        return ok(sternberg_search_proc_iframe.render());
    }

    //แสดงหน้าตัวอย่างการทดลอง
    @Security.Authenticated(Secured.class)
    public static Result demoPage(){
        return ok(demo.render());
    }

    //แสดงหน้าผลลัพธ์ตัวอย่างการทดลอง
    @Security.Authenticated(Secured.class)
    public static Result demoReport(){

        DynamicForm reportData = Form.form().bindFromRequest();
        User user = User.find.byId(session().get("username"));
        double time = Double.parseDouble(reportData.get("time"));
        int score = Integer.parseInt(reportData.get("score"));

        return ok(demoReport.render(score,time,2,"Demo Report",user));
    }

    //แสดงหน้าการทดลอง
    @Security.Authenticated(Secured.class)
    public static Result experiment(long trialId,int questionNo){
        //return ok(exp.render(Trial.find.byId(trialId), questionNo));
        return TODO;
    }

    @Security.Authenticated(Secured.class)
    public static Result saveAnswer(long trialId, int questionNo){
        /*
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
        if(questionNo < trial.numberOfQuiz){
            return redirect(routes.AttentionBlink.experiment(trialId, questionNo));
        }
        return redirect(routes.AttentionBlink.report(user.username, trialId));
        */
        return TODO;
    }

    //แสดงหน้าผลลัพธ์การทดลอง
    @Security.Authenticated(Secured.class)
    public static Result report(String username, long trialId){
        /*
        if(username.equals("") || trialId == 0){
            return redirect(controllers.routes.AttentionBlink.info());
        }

        User user = User.find.byId(username);
        Trial trial = Trial.find.byId(trialId);
        List<Answer> answers = Answer.findInvolving(user, trial.quizzes);
        double totalUsedTime = Answer.calculateTotalUsedTime(answers);
        int score = Answer.calculateTotalScore(answers);
        return ok(report.render(score,totalUsedTime,trial.quizzes.size(), "Report", user));
        */
        return TODO;
    }

    //ตรวจสอบว่าผู้ใช้ทำการทดลองหรือยัง
    @Security.Authenticated(Secured.class)
    public static Result checkUserTakeRepeatExperiment() {

        User user = User.find.where().eq("username", session().get("username")).findUnique();
        if(user == null) {
            return redirect(routes.Application.index());
        }
/*
        if(models.TimeLog.isRepeatTrial(user, 1, ExperimentSchedule.find.byId(11L))) {
            flash("repeat", "คุณเคยทำการทดลองนี้แล้ว หากต้องการทำต่อโปรดติดต่อผู้ดูแลระบบ");
            return ok(proc.render(user));
        }
        models.TimeLog.create(new Date(), user, 1,ExperimentSchedule.find.byId(11L)).save();
        return redirect(routes.AttentionBlink.experiment(19,0));

 */
        return TODO;
    }

}
