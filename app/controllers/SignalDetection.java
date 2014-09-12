package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.ExperimentSchedule;
import models.TimeLog;
import models.signalDetection.*;
import play.*;
import play.libs.Json;
import play.mvc.*;
import play.data.*;

import models.User;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import views.html.signalDetection.*;
import views.html.iframe.*;
import java.util.Date;

public class SignalDetection extends Controller{

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
        return ok(signal_detection_iframe.render());
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
        return ok(signal_detection_proc_iframe.render());
    }

    //แสดงหน้าตัวอย่างการทดลอง
    @Security.Authenticated(Secured.class)
    public static Result demoPage(){
        Question question = new Question('Y','X');
        Quiz quiz = new Quiz(0.5,2,20);
        quiz.question = question;
        return ok(demo.render(quiz));
    }
    //แสดงหน้าผลลัพธ์ตัวอย่างการทดลอง
    @Security.Authenticated(Secured.class)
    public static Result reportDemo(){
        Form<Answer> boundForm = answerForm.bindFromRequest();
        User user = User.find.byId(session().get("username"));
        Answer answer = boundForm.get();
        int score = 0;
        if (answer.isCorrect)
            score++;
        return ok(demoReport.render(score,answer.usedTime,1, "Report", user));
    }

    //แสดงหน้าการทดลอง
    @Security.Authenticated(Secured.class)
    public static Result experiment(long trialId, int questionNo, boolean isPreview){
        return ok(exp.render(Trial.find.byId(trialId),questionNo, isPreview));
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
        answer.user = user;
        answer.quiz = trial.quizzes.get(questionNo);
        answer.save();

        questionNo++;
        if(questionNo < Trial.TOTAL_QUESTION){
            return redirect(routes.SignalDetection.experiment(trialId, questionNo, isPreview));
        }
        else if(!isPreview){
            TimeLog timeLog = TimeLog.findByUserAndTrialId(user, trialId,trial.schedule);
            timeLog.endTime = new Date();
            timeLog.update();
            Trial.find.byId(trialId).updateResult();
        }
        return redirect(routes.SignalDetection.report(user.username, trialId, isPreview));
    }

    //แสดงหน้าผลลัพธ์การทดลอง
    @Security.Authenticated(Secured.class)
    public static Result report(String username, long trialId, boolean isPreview){
        if(username.equals("") || trialId == 0){
            return redirect(controllers.routes.SignalDetection.info());
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
    public static Result saveTrials() {
        ObjectNode result = Json.newObject();
        JsonNode json;
        try {
            json = request().body().asJson();
            String jsonString = Json.stringify(json);
            ObjectMapper mapper = new ObjectMapper();
            List<Trial> trials = mapper.readValue(jsonString, new TypeReference<List<Trial>>(){});
            for(Trial obj : trials){
                Trial trial = Trial.find.byId(obj.id);
                List<Quiz> quizzes = obj.quizzes;
                for(Quiz temp : quizzes){
                    Quiz quiz = Quiz.find.byId(temp.id);
                    quiz.length = temp.length;
                    quiz.displayTime = temp.displayTime;
                    quiz.noOfTarget = temp.noOfTarget;
                    Question question = Question.find.byId(quiz.question.id);
                    question.noise = temp.question.noise;
                    question.target = temp.question.target;
                    question.update();
                    quiz.update();
                }

                trial.update();
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
