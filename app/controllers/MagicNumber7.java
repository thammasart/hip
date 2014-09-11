package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.magicNumber7.Answer;
import models.magicNumber7.Question;
import models.magicNumber7.Quiz;
import models.magicNumber7.Trial;
import models.signalDetection.*;
import play.*;
import play.libs.Json;
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
    public static Result experiment(long trialId,int questionNo, boolean isPreview){
        return ok(exp.render(Trial.find.byId(trialId), questionNo, isPreview));
    }

    @Security.Authenticated(Secured.class)
    public static Result saveAnswer(long trialId, int questionNo, boolean isPreview){
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
            return redirect(routes.MagicNumber7.experiment(trialId, questionNo, isPreview));
        }
        TimeLog timeLog = TimeLog.findByUserAndTrialId(user, trialId,trial.schedule);
        timeLog.endTime = new Date();
        timeLog.update();
        Trial.find.byId(trialId).updateResult();
        return redirect(routes.MagicNumber7.report(user.username, trialId, isPreview));
    }

    //แสดงหน้าผลลัพธ์การทดลอง
    @Security.Authenticated(Secured.class)
    public static Result report(String username, long trialId, boolean isPreview){
        if(username.equals("") || trialId == 0){
            return redirect(controllers.routes.MagicNumber7.info());
        }
        User user = User.find.byId(username);
        Trial trial = Trial.find.byId(trialId);
        List<Answer> answers = Answer.findInvolving(user, trial.quizzes);
        double totalUsedTime = Answer.calculateTotalUsedTime(answers);
        if(isPreview){
            return ok(report.render(answers,totalUsedTime, "Report", user));
        }
        else{
            return ok(report.render(answers,totalUsedTime, "Report", user));
        }
    }

    @Security.Authenticated(Secured.class)
    public static Result demoReport(int length){
        DynamicForm reportData = Form.form().bindFromRequest();
        User user = User.find.byId(session().get("username"));
        double time = Double.parseDouble(reportData.get("usedTime"));
        int score = Integer.parseInt(reportData.get("score"));
        return ok(demoReport.render(score,time,length,"Demo Report",user));
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
                    quiz.chunkSize = temp.chunkSize;
                    quiz.displayTime = temp.displayTime;
                    quiz.length = temp.length;
                    Question question = Question.find.byId(quiz.question.id);
                    question.memorySet = temp.question.memorySet;
                    question.questionType = temp.question.questionType;
                    question.update();
                    quiz.update();

                }
                trial.questionType = obj.questionType;
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
