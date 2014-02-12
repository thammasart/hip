package controllers;

import models.brownPeterson.*;
import models.*;
import play.*;
import play.mvc.*;
import play.data.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import views.html.brownPeterson.*;
import views.html.iframe.*;
import java.util.Date;

public class BrownPeterson extends Controller {
    private static final Form<Answer> answerForm = Form.form(Answer.class);
    @Security.Authenticated(Secured.class)
    public static Result info(){
        User user = User.find.byId(request().username());
        return ok(info.render(user));
    }

    @Security.Authenticated(Secured.class)
    public static Result infoIframe(){
        return ok(brown_peterson_iframe.render());
    }
    
    @Security.Authenticated(Secured.class)
    public static Result proc(){
        User user = User.find.byId(request().username());
        return ok(proc.render(user));
    }
    
    @Security.Authenticated(Secured.class)
    public static Result procIframe(){
        return ok(brown_peterson_proc_iframe.render());
    }

    @Security.Authenticated(Secured.class)
    public static Result experiment(long trialId, int questionNo){

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
        if(questionNo < Trial.TOTAL_QUESTION){
            return redirect(routes.BrownPeterson.experiment(trialId, questionNo));
        }
        return redirect(routes.BrownPeterson.report(user.username, trialId));
    }
    
    @Security.Authenticated(Secured.class)
    public static Result report(String username, Long trialId){
        if(username.equals("") || trialId == 0){
            return redirect(controllers.routes.BrownPeterson.info());
        }
        User user = User.find.where().eq("username", username).findUnique();
        Trial trial = Trial.find.where().eq("id", trialId).findUnique();
        List<Quiz> quizzes = Quiz.findInvolving(trial);
        List<Answer> answers = Answer.findInvolving(user, quizzes);

        double totalUsedTime = Answer.calculateTotalUsedTime(answers);  
        int score = Answer.calculateTotalScore(answers);
        return ok(report.render(score,totalUsedTime,quizzes.size(), "Report", user));
    }

    @Security.Authenticated(Secured.class)
    public static Result checkUserTakeRepeatExperiment() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        if(user == null) {
            return redirect(routes.Application.index());
        }
        if(TimeLog.isRepeatTrial(user, Trial.find.byId(new Long(1)))) {
            flash("repeat", "คุณเคยทำการทดลองนี้แล้ว หากต้องการทำต่อโปรดติดต่อผู้ดูแลระบบ");
            return ok(proc.render(user));
        }
        TimeLog.create(new Date(), user, Trial.find.byId(new Long(1))).save();
        return redirect(routes.BrownPeterson.experiment(new Long(1), 0));
    }
}
