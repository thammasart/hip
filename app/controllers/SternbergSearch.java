package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.TimeLog;
import play.*;
import play.libs.Json;
import play.mvc.*;
import play.data.*;

import models.User;
import models.ExperimentSchedule;
import models.sternbergSearch.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import views.html.sternbergSearch.*;
import views.html.iframe.*;
import java.util.Date;

public class SternbergSearch extends Controller{

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

    //แสดงหน้า preview
    public static Result preview(long trialId){
        User user = User.find.byId(session().get("username"));
        List<Answer> answers = Answer.find.where().eq("user",user).findList();
        for(Answer ans:answers){
            if(ans.quiz.trial.id == trialId)
                ans.delete();
        }
        return redirect(routes.SternbergSearch.experiment(trialId, 0, false, true));
    }

    //แสดงหน้าการทดลอง
    @Security.Authenticated(Secured.class)
    public static Result experiment(long trialId,int questionNo,boolean isShowQuestion, boolean isPreview){
        return ok(exp.render(Trial.find.byId(trialId),questionNo,isShowQuestion,isPreview));
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
        if(questionNo < trial.oneCharIsCorrect + trial.oneCharIsInCorrect + trial.twoCharIsCorrect + trial.twoCharIsInCorrect){
            return redirect(routes.SternbergSearch.experiment(trialId, questionNo, true,isPreview));
        }
        else if(!isPreview){
            TimeLog timeLog = TimeLog.findByUserAndTrialId(user, trialId,trial.schedule);
            timeLog.endTime = new Date();
            timeLog.update();
            Trial.find.byId(trialId).updateResult();
        }
        return redirect(routes.SternbergSearch.report(user.username, trialId, isPreview));
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
                Question q = obj.quizzes.get(0).question;
                Question question = Question.find.byId(q.id);
                question.memorySet = q.memorySet;
                question.questionType = q.questionType;
                question.update();
                List<Quiz> unUseQuiz = new ArrayList<Quiz>(trial.quizzes);

                trial.quizzes = new ArrayList<Quiz>();
                List<Quiz> quizzes = obj.quizzes;
                for(Quiz temp : quizzes){
                    Quiz quiz = new Quiz(trial, question);
                    quiz.isTrue = temp.isTrue;
                    quiz.questionChar = temp.questionChar;
                    quiz.save();
                }
                trial.questionType = obj.questionType;
                trial.length = obj.length;
                trial.oneCharIsCorrect = obj.oneCharIsCorrect;
                trial.oneCharIsInCorrect = obj.oneCharIsInCorrect;
                trial.twoCharIsCorrect = obj.twoCharIsCorrect;
                trial.twoCharIsInCorrect = obj.twoCharIsInCorrect;
                trial.update();

                for(Quiz go : unUseQuiz){
                    Quiz quiz = Quiz.find.byId(go.id);
                    quiz.delete();
                }
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
