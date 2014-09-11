package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.ExperimentSchedule;
import models.TimeLog;
import models.User;
import models.garnerInterference.*;

import models.garnerInterference.Answer;
import models.garnerInterference.Trial;
import play.*;
import play.libs.Json;
import play.mvc.*;
import play.data.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import views.html.garnerInerference.*;
import views.html.iframe.*;
import java.util.Date;

//import models.attentionBlink.*;

public class GarnerInterference extends Controller {
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
        return ok(garner_interference_iframe.render());
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
        return ok(garner_interference_proc_iframe.render()); // not done yet
    }
    @Security.Authenticated(Secured.class)
    public static Result demoPage(){
        return ok(demo.render());
    }

    @Security.Authenticated(Secured.class)
    public static Result demoReport(){
        DynamicForm reportData = Form.form().bindFromRequest();
        User user = User.find.byId(session().get("username"));
        double time = Double.parseDouble(reportData.get("time"));
        int score = Integer.parseInt(reportData.get("score"));
        return ok(demoReport.render(score,time,1,"Demo Report",user));
    }
    //แสดงหน้าการทดลอง
    @Security.Authenticated(Secured.class)
    public static Result experiment(long trialId,int questionNo){
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
        if(questionNo < trial.quizzes.size()){
            return redirect(routes.GarnerInterference.experiment(trialId, questionNo));
        }
        TimeLog timeLog = TimeLog.findByUserAndTrialId(user, trialId,trial.schedule);
        timeLog.endTime = new Date();
        timeLog.update();
        Trial.find.byId(trialId).updateResult();
        return redirect(routes.GarnerInterference.report(user.username, trialId));
    }
    
    //แสดงหน้าผลลัพธ์การทดลอง
    @Security.Authenticated(Secured.class)
    public static Result report(String username, long trialId){
        if(username.equals("") || trialId == 0){
            return redirect(controllers.routes.GarnerInterference.info());
        }
        User user = User.find.byId(username);
        Trial trial = Trial.find.byId(trialId);
        List<Answer> answers = Answer.findInvolving(user, trial.quizzes);
        double totalUsedTime = Answer.calculateTotalUsedTime(answers);
        int score = Answer.calculateTotalScore(answers);
        return ok(report.render(score,totalUsedTime,trial.quizzes.size(), "Report", user));
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
            List<Color> colors = Color.find.all();
            jsonArray = mapper.writeValueAsString(colors);
            json = Json.parse(jsonArray);
            result.put("colors", json);
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
            for(Trial temp : trials){
                Trial trial = Trial.find.byId(temp.id);
                trial.color = temp.color;
                trial.lengthBigSquare = temp.lengthBigSquare;
                trial.lengthSmallSquare = temp.lengthSmallSquare;
                trial.noOfBiDimensionQuestion = temp.noOfBiDimensionQuestion;
                trial.noOfColorQuestion = temp.noOfColorQuestion;
                trial.noOfSizeQuestion = temp.noOfSizeQuestion;
                trial.noOfFakeBiDimentsionQuestion = temp.noOfFakeBiDimentsionQuestion;
                trial.noOfFakeColorQuestion = temp.noOfFakeColorQuestion;
                trial.noOfFakeSizeQuestion = temp.noOfFakeSizeQuestion;
                trial.colorDark = Color.find.byId(temp.colorDark.id);
                trial.colorLight = Color.find.byId(temp.colorLight.id);
                System.out.println("quizzes size : " + trial.quizzes.size());
                List<Quiz> quizzes = trial.quizzes;
                trial.quizzes = new ArrayList<Quiz>();
                trial.update();
                for(Quiz obj : quizzes){
                    obj.delete();
                }

                for(Quiz quiz : temp.quizzes){
                    Question question = new Question();
                    question.colorMatch = quiz.question.colorMatch;
                    question.colorPic = quiz.question.colorPic;
                    question.colorQuestion = quiz.question.colorQuestion;
                    question.sizeMatch = quiz.question.sizeMatch;
                    question.sizePic = quiz.question.sizePic;
                    question.sizeQuestion = quiz.question.sizeQuestion;
                    question.save();
                    Quiz obj = new Quiz(trial, question);
                    obj.questionType = quiz.questionType;
                    obj.save();
                }
            }
            result.put("message", "success");
            result.put("status", "ok");
        }catch (JsonProcessingException e) {
            e.printStackTrace();
            result.put("message", e.getMessage());
            result.put("status", "error");
        }catch(RuntimeException e){
            e.printStackTrace();
            result.put("message", e.getMessage());
            result.put("status", "error");
        }catch(Exception e){
            e.printStackTrace();
            result.put("message", e.getMessage());
            result.put("status", "error");
        }

        return ok(result);
    }
}
