package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import views.html.*;
import views.html.iframe.*;
import models.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Application extends Controller {


	public static Result index() {
        return ok(index.render(Form.form(UserForm.class)));
    }

    @Security.Authenticated(Secured.class)
	public static Result home(){
        User user = User.find.byId(request().username());
        return ok(home.render(user));
    }

    @Security.Authenticated(Secured.class)
	public static Result selectExperiment(){
        User user = User.find.byId(request().username());
        return ok(selectExperiment.render(user));
    }

    @Security.Authenticated(Secured.class)
    public static Result about() {
        User user = User.find.byId(request().username());
        return ok(about.render(user));
    }

    @Security.Authenticated(Secured.class)
    public static Result showResult(){
        User user = User.find.byId(request().username());
        List<Double> scores = new ArrayList<Double>();
        scores.add(models.brownPeterson.Answer.calculateAverageScore());
        return ok(allResult.render(user,scores));
    }

    public static Result logout() {
        session().clear();
        flash("success", "You've been logged out");
        return redirect(routes.Application.index());
    }

	public static Result authenticate() {
        Form<UserForm> userForm = Form.form(UserForm.class).bindFromRequest();
        if(userForm.hasErrors()) {
            flash("unauthenticate", "Incorrect Username/Password.");
            return badRequest(index.render(userForm));
        }
        else {
            session().clear();
            session("username", userForm.get().username);
            return redirect(routes.Application.home());
        }
    }

    @Security.Authenticated(Secured.class)
    public static Result checkBackFormTrial(String expType) {
        User user = User.find.byId(session().get("username"));
        if(user == null) {
            return redirect(routes.Application.index());
        }
        Result nextPage = TODO;
        switch(expType){
            case "BROWNPETERSON" : nextPage = redirect(routes.BrownPeterson.proc());break;
            case "STROOPEFFECT" : nextPage = redirect(routes.StroopEffect.proc()); break;
            case "ATTENTIONBLINK" : nextPage = redirect(routes.AttentionBlink.proc()); break;
            case "SIGNALDETECTION" : nextPage = redirect(routes.SignalDetection.proc()); break;
            case "POSITIONERROR" : nextPage = redirect(routes.PositionError.proc()); break;
            case "STERNBERGSEARCH" : nextPage = redirect(routes.SternbergSearch.proc()); break;
            case "MAGICNUMBER7" : nextPage = redirect(routes.MagicNumber7.proc()); break;
            case "SIMONEFFECT" : nextPage = redirect(routes.SimonEffect.proc()); break;
            case "MULLERLAYER" : nextPage = redirect(routes.MullerLayer.proc()); break;
            case "GARNERINTERFERENCE" : nextPage = redirect(routes.GarnerInterference.proc()); break;
            case "VISUALSEARCH" : nextPage = redirect(routes.VisualSearch.proc()); break;
        }

        return nextPage;
    }

    @Security.Authenticated(Secured.class)
    public static Result checkUserTakeRepeatExperiment(long trialId, long expId) {
        User user = User.find.byId(session().get("username"));
        ExperimentSchedule exp = ExperimentSchedule.find.byId(expId);
        if(user == null) {
            return redirect(routes.Application.index());
        }
        if(TimeLog.isRepeatTrial(user, trialId, exp)) {
            flash("repeat", "คุณเคยทำการทดลองนี้แล้ว หากต้องการทำอีกครั้งโปรดติดต่อผู้ดูแลระบบ");
            return ok(views.html.trial.render(exp.experimentType.toString() ,user));
        }
        TimeLog.create(new Date(), user, trialId, exp).save();
        
        Result nextPage = badRequest(views.html.trial.render(exp.experimentType.toString() , user));

        switch(exp.experimentType){
            case BROWNPETERSON : nextPage = redirect(routes.BrownPeterson.experiment(trialId,0));break;
            case STROOPEFFECT : nextPage = redirect(routes.StroopEffect.experiment(trialId,0)); break;
            case ATTENTIONBLINK : nextPage = redirect(routes.AttentionBlink.experiment(trialId,0)); break;
            case SIGNALDETECTION : nextPage = redirect(routes.SignalDetection.experiment(trialId,0)); break;
            case POSITIONERROR : nextPage = redirect(routes.PositionError.experiment(trialId,0)); break;
            case STERNBERGSEARCH : nextPage = redirect(routes.SternbergSearch.experiment(trialId,0,false)); break;
            case MAGICNUMBER7 : nextPage = redirect(routes.MagicNumber7.experiment(trialId,0)); break;
            case SIMONEFFECT : nextPage = redirect(routes.SimonEffect.experiment(trialId,0)); break;
            case MULLERLAYER : nextPage = redirect(routes.MullerLayer.experiment(trialId,0)); break;
            case GARNERINTERFERENCE: nextPage = redirect(routes.GarnerInterference.experiment(trialId,0)); break;
            case VISUALSEARCH: nextPage = redirect(routes.VisualSearch.experiment(trialId,0)); break;
        }
        return nextPage;
    }

    public static Result chooseTrial(String experimentType){
        User user = User.find.byId(session().get("username"));

        return ok(views.html.trial.render(experimentType, user));
    }

}
