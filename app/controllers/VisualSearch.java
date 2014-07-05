package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.ExperimentSchedule;

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
        switch (Trial.find.byId(trialId).frameSize){
            case SMALLER :
                frameWidth = "300px";
                frameHeight = "200px";
                top = "300px";
                left = "270px";
                break;
            case SMALL :
                frameWidth = "500px";
                frameHeight = "300px";
                top = "240px";
                left = "160px";
                break;
            case MEDIUM :
                frameWidth = "600px";
                frameHeight = "400px";
                top = "190px";
                left = "120px";
                break;
            case BIG :
                frameWidth = "800px";
                frameHeight = "500px";
                top = "142px";
                left = "30px";
                break;
            case EXTRA :
                frameWidth = "1000px";
                frameHeight = "500px";
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

        String json = "[{top:3,left:5},{top:19,left:20}]";

        return ok(exp.render(Trial.find.byId(trialId),questionNo,frameWidth,frameHeight,top,left,json));
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
        List<Quiz> quizzes = Quiz.findInvolving(trial);
        answer.user = user;
        answer.quiz = quizzes.get(questionNo);
        answer.save();

        questionNo++;
        if(questionNo < quizzes.size()){
            return redirect(routes.VisualSearch.experiment(trialId, questionNo));
        }
        return redirect(routes.VisualSearch.report(user.username, trialId));
    }

    //แสดงหน้าผลลัพธ์การทดลอง
    @Security.Authenticated(Secured.class)
    public static Result report(String username, long trialId){
        if(username.equals("") || trialId == 0){
            return redirect(controllers.routes.VisualSearch.info());
        }

        User user = User.find.byId(username);
        Trial trial = Trial.find.byId(trialId);
        List<Quiz> quizzes = Quiz.findInvolving(trial);
        List<Answer> answers = Answer.findInvolving(user, quizzes);
        double totalUsedTime = Answer.calculateTotalUsedTime(answers);
        int score = Answer.calculateTotalScore(answers);
        return ok(report.render(score,totalUsedTime,quizzes.size(), "Report", user));
    }

}
