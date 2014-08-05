package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.brownPeterson.*;
import models.*;
import models.brownPeterson.Answer;
import models.brownPeterson.Quiz;
import models.brownPeterson.Trial;
import play.*;
import play.libs.Json;
import play.mvc.*;
import play.data.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import views.html.brownPeterson.*;
import views.html.iframe.*;
import java.util.Date;
import java.io.File;
import com.google.common.io.Files;
import com.google.common.base.Charsets;

public class BrownPeterson extends Controller {
    private static final Form<Answer> answerForm = Form.form(Answer.class);
    private static List<Answer> answers = new ArrayList<Answer>();

    @Security.Authenticated(Secured.class)
    public static Result info(){
        User user = User.find.byId(request().username());
        return ok(info.render(user));
    }

    @Security.Authenticated(Secured.class)
    public static Result infoIframe(){
        return ok(brown_peterson_iframe.render());
    }
    
    @Security.Authenticated(Secured.class)
    public static Result proc(){
        User user = User.find.byId(request().username());
        return ok(proc.render(user));
    }
    
    @Security.Authenticated(Secured.class)
    public static Result procIframe(){
        return ok(brown_peterson_proc_iframe.render());
    }

    @Security.Authenticated(Secured.class)
    public static Result demoPage(){
        Question question = new Question("BTP","PYM","KLI");
        Quiz quiz = new Quiz(123,5);
        quiz.question = question;
        return ok(demo.render(quiz));
    }

    @Security.Authenticated(Secured.class)
    public static Result reportDemo(String first,String second,String third){
        Form<Answer> boundForm = answerForm.bindFromRequest();
        User user = User.find.byId(session().get("username"));
        Answer answer = boundForm.get();
        Question question = new Question(first,second,third);
        Quiz quiz = new Quiz(123,5);
        quiz.question = question;
        answer.quiz = quiz;
        
        answer = new Answer(answer.firstWord,answer.secondWord,answer.thirdWord,answer.usedTime,answer.countdownResult,answer.user,answer.quiz);

        List<Answer> answers = new ArrayList<Answer>();
        answers.add(answer);
        int score = Answer.calculateTotalScore(answers);
        return ok(demoReport.render(score,answer.usedTime,1, "Report", user));
    }

    @Security.Authenticated(Secured.class)
    public static Result experiment(long trialId, int questionNo){
        return ok(exp.render(Trial.find.byId(trialId), questionNo));
    }

    @Security.Authenticated(Secured.class)
    public static Result saveAnswer(long trialId, int questionNo){
        Form<Answer> boundForm = answerForm.bindFromRequest(); 
        User user = User.find.byId(session().get("username"));
        Trial trial = Trial.find.byId(trialId);
        if(boundForm.hasErrors()) {
            flash("error", "please correct the form above.");
            return badRequest(views.html.home.render(user));
        }
        Answer answer = boundForm.get();
        answer.user = user;
        answer.quiz = trial.quizzes.get(questionNo);
        answer = new Answer(answer.firstWord,answer.secondWord,answer.thirdWord,answer.usedTime,answer.countdownResult,answer.user,answer.quiz);
        answers.add(answer);
        questionNo++;
        if(questionNo < Trial.TOTAL_QUESTION){
            return redirect(routes.BrownPeterson.experiment(trialId, questionNo));
        }
        for(Answer ans : answers){
            ans.save();
        }
        TimeLog timeLog = TimeLog.findByUserAndTrialId(user, trialId,trial.schedule);
        timeLog.endTime = new Date();
        timeLog.update();
        answers = new ArrayList<Answer>();
        return redirect(routes.BrownPeterson.report(user.username, trialId));
    }
    
    @Security.Authenticated(Secured.class)
    public static Result report(String username, Long trialId){
        if(username.equals("") || trialId == 0){
            return redirect(controllers.routes.BrownPeterson.info());
        }
        User user = User.find.where().eq("username", username).findUnique();
        Trial trial = Trial.find.where().eq("id", trialId).findUnique();
        List<Quiz> quizzes = Quiz.findInvolving(trial);
        List<Answer> answers = Answer.findInvolving(user, quizzes);
        double totalUsedTime = Answer.calculateTotalUsedTime(answers);  
        int score = Answer.calculateTotalScore(answers);
        return ok(report.render(score,totalUsedTime,trial.quizzes.size(), "Report", user));
    }

    @Security.Authenticated(Secured.class)
    public static Result installQuestion(){
        try{
            String THAI_WORD_PATH = "/public/archive/brown_peterson_thai_word_question.txt";
            String ENG_WORD_PATH = "/public/archive/brown_peterson_eng_word_question.txt";
            String ENG_NONSENSE_PATH = "/public/archive/brown_peterson_eng_nonsense_question.txt";
            String THAI_NONSENSE_PATH = "/public/archive/brown_peterson_thai_nonsense_question.txt";
            File thaiWordsFile = Play.application().getFile(THAI_WORD_PATH);
            File engNonsenseFile = Play.application().getFile(ENG_WORD_PATH);
            File engWordsFile = Play.application().getFile(ENG_NONSENSE_PATH);
            File thaiNonsenseFile = Play.application().getFile(THAI_NONSENSE_PATH);
            String thaiWords = Files.toString(thaiWordsFile, Charsets.UTF_8);
            String engWords = Files.toString(engWordsFile, Charsets.UTF_8);
            String thaiNonsense = Files.toString(thaiNonsenseFile, Charsets.UTF_8);
            String engNonesense = Files.toString(engNonsenseFile, Charsets.UTF_8);

            Question.generateQuestion(thaiWords, Trial.WORD, Trial.THAI);
            Question.generateQuestion(engWords, Trial.WORD, Trial.ENGLISH);
            Question.generateQuestion(thaiNonsense, Trial.NON_SENSE, Trial.THAI);
            Question.generateQuestion(engNonesense, Trial.NON_SENSE, Trial.ENGLISH);

        }catch(Exception e){
            System.out.println("exception error");
            return redirect(routes.InitialController.index());
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
            List<Question> wordEnglish = Question.find.where().eq("trigramType", Trial.WORD).eq("trigramLanguage", Trial.ENGLISH).findList();
            List<Question> wordThai = Question.find.where().eq("trigramType", Trial.WORD).eq("trigramLanguage", Trial.THAI).findList();
            List<Question> nonsenseThai = Question.find.where().eq("trigramType", Trial.NON_SENSE).eq("trigramLanguage", Trial.THAI).findList();
            List<Question> nonsenseEng = Question.find.where().eq("trigramType", Trial.NON_SENSE).eq("trigramLanguage", Trial.ENGLISH).findList();
            jsonArray = mapper.writeValueAsString(wordEnglish);
            json = Json.parse(jsonArray);
            result.put("wordEnglish", json);
            jsonArray = mapper.writeValueAsString(wordThai);
            json = Json.parse(jsonArray);
            result.put("wordThai", json);
            jsonArray = mapper.writeValueAsString(nonsenseThai);
            json = Json.parse(jsonArray);
            result.put("nonsenseThai", json);
            jsonArray = mapper.writeValueAsString(nonsenseEng);
            json = Json.parse(jsonArray);
            result.put("nonsenseEng", json);
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
                    quiz.flashTime = temp.flashTime;
                    quiz.initCountdown = temp.initCountdown;
                    quiz.question = Question.find.byId(temp.question.id);
                    quiz.update();
                }
                trial.trigramLanguage = obj.trigramLanguage;
                trial.trigramType = obj.trigramType;
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
