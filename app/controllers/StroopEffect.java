package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.User;
import models.TimeLog;
import models.ExperimentSchedule;
import models.stroopEffect.*;
import play.*;
import play.libs.Json;
import play.mvc.*;
import play.data.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import views.html.stroopEffect.*;
import views.html.iframe.*;
import java.util.Date;

import models.stroopEffect.*;

public class StroopEffect extends Controller {
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
        return ok(stroop_effect_iframe.render());
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
        return ok(stroop_effect_proc_iframe.render());
    }

    @Security.Authenticated(Secured.class)
    public static Result demoPage(){
        Trial trial = new Trial(2,QuestionType.ENGLISH);
        Question question = new Question("Blue","Red");
        Quiz quiz = Quiz.create(trial,question);
        trial.quizzes = new ArrayList<Quiz>();
        trial.quizzes.add(quiz);
        return ok(demo.render(trial,0));
    }

    //แสดงหน้า preview
    public static Result preview(long trialId){
        User user = User.find.byId(session().get("username"));
        List<Answer> answers = Answer.find.where().eq("user",user).findList();
        for(Answer ans:answers){
            if(ans.quiz.trial.id == trialId)
                ans.delete();
        }
        return redirect(routes.StroopEffect.experiment(trialId, 0, true));
    }

    //แสดงหน้าการทดลอง
    @Security.Authenticated(Secured.class)
    public static Result experiment(long trialId, int questionNo, boolean isPreview){

        return ok(exp.render(Trial.find.byId(trialId), questionNo, isPreview));
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
            return redirect(routes.StroopEffect.experiment(trialId, questionNo, isPreview));
        }
        else if(!isPreview){
            TimeLog timeLog = TimeLog.findByUserAndTrialId(user, trialId,trial.schedule);
            timeLog.endTime = new Date();
            timeLog.update();
            Trial.find.byId(trialId).updateResult();
        }
        //trial.updateResult();
        //trial.update();
        return redirect(routes.StroopEffect.report(user.username, trialId, isPreview));
    }

    //แสดงหน้าผลลัพธ์การทดลอง
    @Security.Authenticated(Secured.class)
    public static Result report(String username, Long trialId, boolean isPreview){
        if(username.equals("") || trialId == 0){
            return redirect(controllers.routes.StroopEffect.info());
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

    @Security.Authenticated(Secured.class)
    public static Result demoReport(){
        Form<Answer> boundForm = answerForm.bindFromRequest();
        User user = User.find.byId(session().get("username"));
        Answer answer = boundForm.get();
        double time = answer.usedTime;
        int score = 0;
        if (answer.answer.equalsIgnoreCase("Red"))
            score++;
        return ok(demoReport.render(score,time,1,"Demo Report",user));
    }

    //ตรวจสอบว่าผู้ใช้ทำการทดลองหรือยัง
/*  @Security.Authenticated(Secured.class)
    public static Result checkUserTakeRepeatExperiment() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        if(user == null) {
            return redirect(routes.Application.index());
        }
        if(models.stroopEffect.TimeLog.isRepeatTrial(user, Trial.find.byId(new Long(2)))) {
            flash("repeat", "คุณเคยทำการทดลองนี้แล้ว หากต้องการทำต่อโปรดติดต่อผู้ดูแลระบบ");
            return ok(proc.render(user));
        }
        models.stroopEffect.TimeLog.create(new Date(), user, Trial.find.byId(new Long(2))).save();
        return redirect(routes.StroopEffect.experiment(new Long(2),0));
    }  */

    @Security.Authenticated(Secured.class)
    public static Result installQuestion(){
        final String[] colors = { "black", "blue", "red", "green", "purple", "yellow"};
        final String[] colorsTH = { "ดำ", "น้ำเงิน", "แดง", "เขียว", "ม่วง", "เหลือง"};
        for(int i=0; i < colors.length; i++){
            for(int j=0;j < colors.length; j++){
                Question question = new Question(colors[i], colors[j]);
                question.questionType = QuestionType.ENGLISH;
                question.save();
            }
        }

        for(int i = 0; i < colorsTH.length; i++){
            for(int j=0;j < colors.length; j++){
                Question question = new Question(colorsTH[i], colors[j]);
                question.questionType = QuestionType.THAI;
                question.save();
            }
        }

        return redirect(routes.InitialController.index());
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
            result.put("trials", json);
            List<Question> questions = Question.find.all();
            jsonArray = mapper.writeValueAsString(questions);
            json = Json.parse(jsonArray);
            result.put("questions", json);
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
                    Question question = Question.find.byId(temp.question.id);
                    quiz.question = question;
                    quiz.update();
                }
                trial.questionType = obj.questionType;
                trial.appearTime = obj.appearTime;

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
