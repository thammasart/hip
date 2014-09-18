package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.ExperimentSchedule;
import models.TimeLog;
import models.User;
import models.mullerLayer.*;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.iframe.muller_layer_iframe;
import views.html.iframe.muller_layer_proc_iframe;
import views.html.mullerLayer.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import models.attentionBlink.*;

public class MullerLayer extends Controller {
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
        return ok(muller_layer_iframe.render());
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
        return ok(muller_layer_proc_iframe.render()); // not done yet
    }
    @Security.Authenticated(Secured.class)
    public static Result demoPage(){
        return ok(demo.render());
    }

    //แสดงหน้า preview
    public static Result preview(long trialId){
        User user = User.find.byId(session().get("username"));
        List<Answer> answers = Answer.find.where().eq("user",user).findList();
        for(Answer ans:answers){
            if(ans.quiz.trial.id == trialId)
                ans.delete();
        }
        return redirect(routes.MullerLayer.experiment(trialId, 0, true));
    }
    
    //แสดงหน้าการทดลอง
    @Security.Authenticated(Secured.class)
    public static Result experiment(long trialId,int questionNo, boolean isPreview){
        Trial t = Trial.find.byId(trialId);
        List<LineType> lt = new ArrayList<LineType>();
        int noOfDeleteLines = 5-t.quizzes.get(questionNo).noOfChoice;
        lt.add(t.quizzes.get(questionNo).question.line1);
        lt.add(t.quizzes.get(questionNo).question.line2);
        lt.add(t.quizzes.get(questionNo).question.line3);
        lt.add(t.quizzes.get(questionNo).question.line4);
        lt.add(t.quizzes.get(questionNo).question.line5);

        for(int i = 0 ; i <noOfDeleteLines;i++){
            lt.remove(i);
        }
        return ok(exp.render(Trial.find.byId(trialId), questionNo,lt, isPreview));
    }


    @Security.Authenticated(Secured.class)
    public static Result demoReport(){
        DynamicForm reportData = Form.form().bindFromRequest();
        User user = User.find.byId(session().get("username"));
        double time = Double.parseDouble(reportData.get("time"));
        int score = Integer.parseInt(reportData.get("score"));
        return ok(demoReport.render(score,time,1,"Demo Report",user));
    }
    @Security.Authenticated(Secured.class)
    public static Result saveAnswer(long trialId, int questionNo, boolean isPreview){
        Form<Answer> boundForm = answerForm.bindFromRequest();
        User user = User.find.byId(session().get("username"));
        Trial trial = Trial.find.byId(trialId);

        DynamicForm boundRequest = Form.form().bindFromRequest("choice");
        int choice = Integer.parseInt(boundRequest.data().get("choice"));

        if(boundForm.hasErrors()){
            flash("error", "please correct the form above.");
            return badRequest(views.html.home.render(user));
        }

        Answer answer = boundForm.get();
        if(trial.quizzes.get(questionNo).differChoice == choice){
            answer.isCorrect = true;
        }
        answer.user = user;
        answer.quiz = trial.quizzes.get(questionNo);
        answer.save();

        questionNo++;
        if(questionNo < trial.quizzes.size()){
            return redirect(routes.MullerLayer.experiment(trialId, questionNo, isPreview));
        }
        else if(!isPreview){
            TimeLog timeLog = TimeLog.findByUserAndTrialId(user, trialId,trial.schedule);
            timeLog.endTime = new Date();
            timeLog.update();
            Trial.find.byId(trialId).updateResult();
        }
        return redirect(routes.MullerLayer.report(user.username, trialId, isPreview));
    }
    
    //แสดงหน้าผลลัพธ์การทดลอง
    @Security.Authenticated(Secured.class)
    public static Result report(String username, long trialId, boolean isPreview){
        if(username.equals("") || trialId == 0){
            return redirect(controllers.routes.AttentionBlink.info());
        }
        User user = User.find.byId(username);
        Trial trial = Trial.find.byId(trialId);
        List<Answer> answers = Answer.findInvolving(user, trial.quizzes);
        double totalUsedTime = Answer.calculateTotalUsedTime(answers);
        int score = Answer.calculateTotalScore(answers);
        if(isPreview){
            return ok(reportPreview.render(score,totalUsedTime,trial.quizzes.size(), "Report", user));
        }
        else{
            return ok(report.render(score,totalUsedTime,trial.quizzes.size(), "Report", user));
        }
    }


    @BodyParser.Of(BodyParser.Json.class)
    public static Result initial(long id) {
        ObjectNode result = Json.newObject();
        JsonNode json;
        try {
            ExperimentSchedule exp = ExperimentSchedule.find.byId(id);
            List<Trial> trials = Trial.findInvolving(exp);
            ObjectMapper mapper = new ObjectMapper();
            String jsonArray = mapper.writeValueAsString(trials);
            json = Json.parse(jsonArray);
            result.put("message", "success");
            result.put("status", "ok");
            result.put("trials", json);
        }catch (JsonProcessingException e) {
            result.put("message", e.getMessage());
            result.put("status", "error");
        }catch(RuntimeException e){
            result.put("message", e.getMessage());
            result.put("status", "error");
        }catch(Exception e){
            result.put("message", e.getMessage());
            result.put("status", "error");
        }

        return ok(result);
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result findQuestion(long trialId, int questionNo) {
        ObjectNode result = Json.newObject();
        JsonNode json;
        try {
            Trial trial = Trial.find.byId(trialId);
            Quiz quiz = trial.quizzes.get(questionNo);
            quiz.differChoice = 0;
            json = Json.toJson(quiz);
            result.put("message", "success");
            result.put("status", "ok");
            result.put("quiz", json);
        }catch(RuntimeException e){
            result.put("message", e.getMessage());
            result.put("status", "error");
        }catch(Exception e){
            result.put("message", e.getMessage());
            result.put("status", "error");
        }

        return ok(result);
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result saveAdmin() {
        ObjectNode result = Json.newObject();
        JsonNode json;
        try {
            json = request().body().asJson();
            String jsonString = Json.stringify(json);
            ObjectMapper mapper = new ObjectMapper();
            List<Trial> trials = mapper.readValue(jsonString, new TypeReference<List<Trial>>(){});
            for(Trial trial : trials){
                Trial t = Trial.find.byId(trial.id);
                t.displayTime = trial.displayTime;
                for(Quiz go : trial.quizzes){
                    Quiz quiz = Quiz.find.byId(go.id);
                    quiz.differChoice = go.differChoice;
                    quiz.differLength = go.differLength;
                    quiz.isPositive = go.isPositive;
                    quiz.lengthType = go.lengthType;
                    quiz.noOfChoice = go.noOfChoice;
                    Question question = Question.find.byId(go.question.id);
                    question.line1 = go.question.line1;
                    question.line2 = go.question.line2;
                    question.line3 = go.question.line3;
                    question.line4 = go.question.line4;
                    question.line5 = go.question.line5;
                    question.update();
                    quiz.update();
                }
                t.update();
            }


            result.put("message", "success");
            result.put("status", "ok");
        }catch (JsonProcessingException e) {
            result.put("message", e.getMessage());
            result.put("status", "error");
        }catch(RuntimeException e){
            result.put("message", e.getMessage());
            result.put("status", "error");
        }catch(Exception e){
            result.put("message", e.getMessage());
            result.put("status", "error");
        }

        return ok(result);
    }
}
