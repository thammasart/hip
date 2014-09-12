package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.ExperimentSchedule;

import models.TimeLog;
import play.*;
import play.libs.Json;
import play.mvc.*;
import play.data.*;

import models.User;
import models.visualSearch.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import views.html.visualSearch.*;
import views.html.iframe.*;
import java.util.Date;

public class VisualSearch extends Controller{

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
        return ok(visual_search_iframe.render());
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
        return ok(visual_search_proc_iframe.render());
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
        double time = Double.parseDouble(reportData.get("usedTime"));
        int score = Integer.parseInt(reportData.get("score"));
        return ok(demoReport.render(score,time,1,"Demo Report",user));
    }

    //แสดงหน้าการทดลอง
    @Security.Authenticated(Secured.class)
    public static Result experiment(long trialId, int questionNo){
        String frameWidth = "800px";
        String frameHeight = "500px";
        String top = "142px";
        String left = "30px";
        switch (Trial.find.byId(trialId).quiz.frameSize){
            case SMALLER :
                frameWidth = Trial.SMALLER_WIDTH + "px";
                frameHeight = Trial.SMALLER_HEIGHT + "px";
                top = "300px";
                left = "270px";
                break;
            case SMALL :
                frameWidth = Trial.SMALL_WIDTH + "px";
                frameHeight = Trial.SMALL_HEIGHT + "px";
                top = "240px";
                left = "160px";
                break;
            case MEDIUM :
                frameWidth = Trial.MEDIUM_WIDTH + "px";
                frameHeight = Trial.MEDIUM_HEIGHT + "px";
                top = "190px";
                left = "120px";
                break;
            case BIG :
                frameWidth = Trial.BIG_WIDTH + "px";
                frameHeight = Trial.BIG_HEIGHT + "px";
                top = "142px";
                left = "30px";
                break;
            case EXTRA :
                frameWidth = Trial.EXTRA_WIDTH + "px";
                frameHeight = Trial.EXTRA_HEIGHT + "px";
                top = "142px";
                left = "-70px";
                break;
            default :
                frameWidth = "800px";
                frameHeight = "500px";
                top = "142px";
                left = "30px";
                break;
        }

        return ok(exp.render(Trial.find.byId(trialId),questionNo,frameWidth,frameHeight,top,left));
    }


    @Security.Authenticated(Secured.class)
    public static Result saveAnswer(long trialId, int questionNo, boolean isPreview){
        Form<Answer> boundForm = answerForm.bindFromRequest();
        User user = User.find.byId(session().get("username"));
        Trial trial = Trial.find.byId(trialId);

        if(boundForm.hasErrors()){
            flash("error", "please correct the form above.");
            return badRequest(views.html.home.render(user));
        }

        Answer answer = boundForm.get();
        List<Quiz> quizzes = Quiz.findInvolving(trial);
        answer.user = user;
        answer.quiz = quizzes.get(questionNo);
        answer.save();

        questionNo++;
        if(questionNo < quizzes.size()){
            return redirect(routes.VisualSearch.experiment(trialId, questionNo, isPreview));
        }
        TimeLog timeLog = TimeLog.findByUserAndTrialId(user, trialId,trial.schedule);
        timeLog.endTime = new Date();
        timeLog.update();
        Trial.find.byId(trialId).updateResult();
        return redirect(routes.VisualSearch.report(user.username, trialId, isPreview));
    }

    //แสดงหน้าผลลัพธ์การทดลอง
    @Security.Authenticated(Secured.class)
    public static Result report(String username, long trialId, boolean isPreview){
        if(username.equals("") || trialId == 0){
            return redirect(controllers.routes.VisualSearch.info());
        }

        User user = User.find.byId(username);
        Trial trial = Trial.find.byId(trialId);
        List<Quiz> quizzes = Quiz.findInvolving(trial);
        List<Answer> answers = Answer.findInvolving(user, quizzes);
        double totalUsedTime = Answer.calculateTotalUsedTime(answers);
        int score = Answer.calculateTotalScore(answers);
        if(isPreview){
            return ok(reportPreview.render(score,totalUsedTime,quizzes.size(), "Report", user));
        }
        else{
            return ok(report.render(score,totalUsedTime,quizzes.size(), "Report", user));
        }
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result init(long trialId) {
        Trial trial = Trial.findById(trialId);
        ObjectNode result = Json.newObject();
        try{
            JsonNode json = Json.toJson(trial);
            result.put("message", "success");
            result.put("status", "ok");
            result.put("trial", json);
        } catch(RuntimeException e){
            result.put("message", e.getMessage());
            result.put("status", "error");
        }catch(Exception e){
            result.put("message", e.getMessage());
            result.put("status", "error");
        }
        return ok(result);
    }
}
