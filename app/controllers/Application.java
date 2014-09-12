package controllers;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import play.*;
import play.mvc.*;
import play.data.*;
import play.libs.Json;
import views.html.*;
import views.html.iframe.*;
import models.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Application extends Controller {


	public static Result index() {
        if(session().toString() != "{}"){
            return redirect(routes.Application.home());
        }
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
    
    @Security.Authenticated(Secured.class)
    @BodyParser.Of(BodyParser.Json.class)
    public static Result findExpResult(String expType){
        List<ExperimentSchedule> exps = null;
        ObjectNode result = Json.newObject();
        JsonNode json;
        try { 
                if(expType.equals("/attentionBlink")){
                    exps = ExperimentSchedule.find.where().eq("experimentType",ExperimentType.ATTENTIONBLINK).findList();
                    List<models.attentionBlink.Trial> trialList = new ArrayList<models.attentionBlink.Trial>();
                    for(ExperimentSchedule exp:exps){
                        trialList.addAll(models.attentionBlink.Trial.findInvolving(exp));
                    }
                    ObjectMapper mapper = new ObjectMapper();
                    String jsonArray = mapper.writeValueAsString(trialList);
                    json = Json.parse(jsonArray);
                    result.put("trials",json);

                    models.attentionBlink.QuestionType[] questionTypes = models.attentionBlink.QuestionType.values();
                    jsonArray = mapper.writeValueAsString(questionTypes);
                    json = Json.parse(jsonArray);
                    result.put("questionTypes",json);
                    return ok(result);
                }
                else  
                if(expType.equals("/simonEffect")){
                    exps = ExperimentSchedule.find.where().eq("experimentType",ExperimentType.SIMONEFFECT).findList();
                    List<models.simonEffect.Trial> trialList = new ArrayList<models.simonEffect.Trial>();
                    for(ExperimentSchedule exp:exps){
                        trialList.addAll(models.simonEffect.Trial.findInvolving(exp));
                    }
                    ObjectMapper mapper = new ObjectMapper();
                    String jsonArray = mapper.writeValueAsString(trialList);
                    json = Json.parse(jsonArray);
                    result.put("trials",json);

                    models.simonEffect.QuestionType[] questionTypes = models.simonEffect.QuestionType.values();
                    jsonArray = mapper.writeValueAsString(questionTypes);
                    json = Json.parse(jsonArray);
                    result.put("questionTypes",json);
                    return ok(result);
                }
                else  
                if(expType.equals("/stroopEffect")){
                    exps = ExperimentSchedule.find.where().eq("experimentType",ExperimentType.STROOPEFFECT).findList();
                    List<models.stroopEffect.Trial> trialList = new ArrayList<models.stroopEffect.Trial>();
                    for(ExperimentSchedule exp:exps){
                        trialList.addAll(models.stroopEffect.Trial.findInvolving(exp));
                    }
                    ObjectMapper mapper = new ObjectMapper();
                    String jsonArray = mapper.writeValueAsString(trialList);
                    json = Json.parse(jsonArray);
                    result.put("trials",json);

                    models.stroopEffect.QuestionType[] questionTypes = models.stroopEffect.QuestionType.values();
                    jsonArray = mapper.writeValueAsString(questionTypes);
                    json = Json.parse(jsonArray);
                    result.put("questionTypes",json);
                    return ok(result);
                }
                else  
                if(expType.equals("/visualSearch")){
                    exps = ExperimentSchedule.find.where().eq("experimentType",ExperimentType.VISUALSEARCH).findList();
                    List<models.visualSearch.Trial> trialList = new ArrayList<models.visualSearch.Trial>();
                    for(ExperimentSchedule exp:exps){
                        trialList.addAll(models.visualSearch.Trial.findInvolving(exp));
                    }
                    ObjectMapper mapper = new ObjectMapper();
                    String jsonArray = mapper.writeValueAsString(trialList);
                    json = Json.parse(jsonArray);
                    result.put("trials",json);

                    return ok(result);
                }
                else  
                if(expType.equals("/brownPeterson")){
                    exps = ExperimentSchedule.find.where().eq("experimentType",ExperimentType.BROWNPETERSON).findList();
                    List<models.brownPeterson.Trial> trialList = new ArrayList<models.brownPeterson.Trial>();
                    for(ExperimentSchedule exp:exps){
                        trialList.addAll(models.brownPeterson.Trial.findInvolving(exp));
                    }
                    ObjectMapper mapper = new ObjectMapper();
                    String jsonArray = mapper.writeValueAsString(trialList);
                    json = Json.parse(jsonArray);
                    result.put("trials",json);

                    models.brownPeterson.QuestionType[] questionTypes = models.brownPeterson.QuestionType.values();
                    jsonArray = mapper.writeValueAsString(questionTypes);
                    json = Json.parse(jsonArray);
                    result.put("questionTypes",json);
                    return ok(result);
                }
                else  
                if(expType.equals("/magicNumber7")){
                    exps = ExperimentSchedule.find.where().eq("experimentType",ExperimentType.MAGICNUMBER7).findList();
                    List<models.magicNumber7.Trial> trialList = new ArrayList<models.magicNumber7.Trial>();
                    for(ExperimentSchedule exp:exps){
                        trialList.addAll(models.magicNumber7.Trial.findInvolving(exp));
                    }
                    ObjectMapper mapper = new ObjectMapper();
                    String jsonArray = mapper.writeValueAsString(trialList);
                    json = Json.parse(jsonArray);
                    result.put("trials",json);

                    models.magicNumber7.QuestionType[] questionTypes = models.magicNumber7.QuestionType.values();
                    jsonArray = mapper.writeValueAsString(questionTypes);
                    json = Json.parse(jsonArray);
                    result.put("questionTypes",json);
                    return ok(result);
                }
                else  
                if(expType.equals("/positionError")){
                    exps = ExperimentSchedule.find.where().eq("experimentType",ExperimentType.POSITIONERROR).findList();
                    List<models.positionError.Trial> trialList = new ArrayList<models.positionError.Trial>();
                    for(ExperimentSchedule exp:exps){
                        trialList.addAll(models.positionError.Trial.findInvolving(exp));
                    }
                    ObjectMapper mapper = new ObjectMapper();
                    String jsonArray = mapper.writeValueAsString(trialList);
                    json = Json.parse(jsonArray);
                    result.put("trials",json);

                    models.positionError.QuestionType[] questionTypes = models.positionError.QuestionType.values();
                    jsonArray = mapper.writeValueAsString(questionTypes);
                    json = Json.parse(jsonArray);
                    result.put("questionTypes",json);
                    return ok(result);
                }
                else  
                if(expType.equals("/sternbergSearchExhaustive")){
                    exps = ExperimentSchedule.find.where().eq("experimentType",ExperimentType.STERNBERGSEARCH).findList();
                    List<models.sternbergSearch.Trial> trialList = new ArrayList<models.sternbergSearch.Trial>();
                    for(ExperimentSchedule exp:exps){
                        trialList.addAll(models.sternbergSearch.Trial.findInvolving(exp));
                    }
                    ObjectMapper mapper = new ObjectMapper();
                    String jsonArray = mapper.writeValueAsString(trialList);
                    json = Json.parse(jsonArray);
                    result.put("trials",json);

                    models.sternbergSearch.QuestionType[] questionTypes = models.sternbergSearch.QuestionType.values();
                    jsonArray = mapper.writeValueAsString(questionTypes);
                    json = Json.parse(jsonArray);
                    result.put("questionTypes",json);
                    return ok(result);
                }
                else  
                if(expType.equals("/sternbergSearchParallel")){
                    exps = ExperimentSchedule.find.where().eq("experimentType",ExperimentType.STERNBERGSEARCH).findList();
                    List<models.sternbergSearch.Trial> trialList = new ArrayList<models.sternbergSearch.Trial>();
                    for(ExperimentSchedule exp:exps){
                        trialList.addAll(models.sternbergSearch.Trial.findInvolving(exp));
                    }
                    ObjectMapper mapper = new ObjectMapper();
                    String jsonArray = mapper.writeValueAsString(trialList);
                    json = Json.parse(jsonArray);
                    result.put("trials",json);

                    models.sternbergSearch.QuestionType[] questionTypes = models.sternbergSearch.QuestionType.values();
                    jsonArray = mapper.writeValueAsString(questionTypes);
                    json = Json.parse(jsonArray);
                    result.put("questionTypes",json);
                    return ok(result);
                }
                else  
                if(expType.equals("/changeBlindness")){
                    exps = ExperimentSchedule.find.where().eq("experimentType",ExperimentType.CHANGEBLINDNESS).findList();
                    List<models.changeBlindness.Trial> trialList = new ArrayList<models.changeBlindness.Trial>();
                    for(ExperimentSchedule exp:exps){
                        trialList.addAll(models.changeBlindness.Trial.findInvolving(exp));
                    }
                    ObjectMapper mapper = new ObjectMapper();
                    String jsonArray = mapper.writeValueAsString(trialList);
                    json = Json.parse(jsonArray);
                    result.put("trials",json);

                    return ok(result);
                }
                else  
                if(expType.equals("/garnerInterference")){
                    exps = ExperimentSchedule.find.where().eq("experimentType",ExperimentType.GARNERINTERFERENCE).findList();
                    List<models.garnerInterference.Trial> trialList = new ArrayList<models.garnerInterference.Trial>();
                    for(ExperimentSchedule exp:exps){
                        trialList.addAll(models.garnerInterference.Trial.findInvolving(exp));
                    }
                    ObjectMapper mapper = new ObjectMapper();
                    String jsonArray = mapper.writeValueAsString(trialList);
                    json = Json.parse(jsonArray);
                    result.put("trials",json);

                    models.garnerInterference.QuestionType[] questionTypes = models.garnerInterference.QuestionType.values();
                    jsonArray = mapper.writeValueAsString(questionTypes);
                    json = Json.parse(jsonArray);
                    result.put("questionTypes",json);
                    return ok(result);
                }
                else  
                if(expType.equals("/mullerLayer")){
                    exps = ExperimentSchedule.find.where().eq("experimentType",ExperimentType.MULLERLAYER).findList();
                    List<models.mullerLayer.Trial> trialList = new ArrayList<models.mullerLayer.Trial>();
                    for(ExperimentSchedule exp:exps){
                        trialList.addAll(models.mullerLayer.Trial.findInvolving(exp));
                    }
                    ObjectMapper mapper = new ObjectMapper();
                    String jsonArray = mapper.writeValueAsString(trialList);
                    json = Json.parse(jsonArray);
                    result.put("trials",json);

                    return ok(result);
                }
                else  
                if(expType.equals("/signalDetection")){
                    exps = ExperimentSchedule.find.where().eq("experimentType",ExperimentType.SIGNALDETECTION).findList();
                    List<models.signalDetection.Trial> trialList = new ArrayList<models.signalDetection.Trial>();
                    for(ExperimentSchedule exp:exps){
                        trialList.addAll(models.signalDetection.Trial.findInvolving(exp));
                    }
                    ObjectMapper mapper = new ObjectMapper();
                    String jsonArray = mapper.writeValueAsString(trialList);
                    json = Json.parse(jsonArray);
                    result.put("trials",json);

                    return ok(result);
                }
        }catch (JsonProcessingException e) {
            result.put("message", e.getMessage());
            result.put("status", "error1");
        }catch(RuntimeException e){
            e.printStackTrace();
            result.put("message", e.getMessage());
            result.put("status", "error2");
        }catch(Exception e){
            result.put("message", e.getMessage());
            result.put("status", "error3");
        }
        return ok(result);
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
        if (expType.equals("BROWNPETERSON")){
            nextPage = redirect(routes.BrownPeterson.proc());
        }
        else if (expType.equals("STROOPEFFECT")){
            nextPage = redirect(routes.StroopEffect.proc());
        }
        else if (expType.equals("ATTENTIONBLINK")){
            nextPage = redirect(routes.AttentionBlink.proc());
        }
        else if (expType.equals("SIGNALDETECTION")){
            nextPage = redirect(routes.SignalDetection.proc());
        }
        else if (expType.equals("POSITIONERROR")){
            nextPage = redirect(routes.PositionError.proc());
        }
        else if (expType.equals("STERNBERGSEARCH")){
            nextPage = redirect(routes.SternbergSearch.proc());
        }
        else if (expType.equals("MAGICNUMBER7")){
            nextPage = redirect(routes.MagicNumber7.proc());
        }
        else if (expType.equals("SIMONEFFECT")){
            nextPage = redirect(routes.SimonEffect.proc());
        }
        else if (expType.equals("MULLERLAYER")){
            nextPage = redirect(routes.MullerLayer.proc());
        }
        else if (expType.equals("GARNERINTERFERENCE")){
            nextPage = redirect(routes.GarnerInterference.proc());
        }
        else if (expType.equals("VISUALSEARCH")){
            nextPage = redirect(routes.VisualSearch.proc());
        }
        else if (expType.equals("CHANGEBLINDNESS")){
            nextPage = redirect(routes.ChangeBlindness.proc());
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
            case BROWNPETERSON : nextPage = redirect(routes.BrownPeterson.experiment(trialId,0,false));break;
            case STROOPEFFECT : nextPage = redirect(routes.StroopEffect.experiment(trialId,0,false)); break;
            case ATTENTIONBLINK : nextPage = redirect(routes.AttentionBlink.experiment(trialId,0,false)); break;
            case SIGNALDETECTION : nextPage = redirect(routes.SignalDetection.experiment(trialId,0,false)); break;
            case POSITIONERROR : nextPage = redirect(routes.PositionError.experiment(trialId,0,false)); break;
            case STERNBERGSEARCH : nextPage = redirect(routes.SternbergSearch.experiment(trialId,0,false,false)); break;
            case MAGICNUMBER7 : nextPage = redirect(routes.MagicNumber7.experiment(trialId,0,false)); break;
            case SIMONEFFECT : nextPage = redirect(routes.SimonEffect.experiment(trialId,0,false)); break;
            case MULLERLAYER : nextPage = redirect(routes.MullerLayer.experiment(trialId,0,false)); break;
            case GARNERINTERFERENCE: nextPage = redirect(routes.GarnerInterference.experiment(trialId,0,false)); break;
            case VISUALSEARCH: nextPage = redirect(routes.VisualSearch.experiment(trialId,0,false)); break;
            case CHANGEBLINDNESS : nextPage = redirect(routes.ChangeBlindness.experiment(trialId,0,false)); break;
        }
        return nextPage;
    }

    public static Result chooseTrial(String experimentType){
        User user = User.find.byId(session().get("username"));

        return ok(views.html.trial.render(experimentType, user));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result saveExperiment(Long id, String name, Long startDate, Long expireDate) {
        ObjectNode result = Json.newObject();
        try {
            ExperimentSchedule exp = ExperimentSchedule.find.byId(id);
            if (!name.isEmpty()) {
                exp.name = name;
            }
            if (startDate != -1) {
                exp.startDate = LocalDate.fromDateFields(new Date(startDate)).toDateTimeAtStartOfDay().toDate();
            }
            if (expireDate != -1) {
                exp.expireDate = LocalDate.fromDateFields(new Date(expireDate)).toDateMidnight().toDate();
            }
            exp.update();
            result.put("message", "success");
            result.put("status", "ok");
        } catch (RuntimeException e) {
            result.put("message", e.getMessage());
            result.put("status", "error");
        } catch (Exception e) {
            result.put("message", e.getMessage());
            result.put("status", "error");
        }

        return ok(result);
    }
}
