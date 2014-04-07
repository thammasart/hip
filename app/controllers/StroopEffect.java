package controllers;

import models.*;
import play.*;
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
    //แสดงหน้าการทดลอง
    @Security.Authenticated(Secured.class)
    public static Result experiment(long trialId, int questionNo){

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
        if(questionNo < Trial.TOTAL_QUESTION){
            return redirect(routes.StroopEffect.experiment(trialId, questionNo));
        }
        return redirect(routes.StroopEffect.report(user.username, trialId));
    }

    //แสดงหน้าผลลัพธ์การทดลอง
    @Security.Authenticated(Secured.class)
    public static Result report(String username, Long trialId){
        if(username.equals("") || trialId == 0){
            return redirect(controllers.routes.StroopEffect.info());
        }
        User user = User.find.byId(username);
        Trial trial = Trial.find.byId(trialId);
        List<Answer> answers = Answer.findInvolving(user, trial.quizzes);
        double totalUsedTime = Answer.calculateTotalUsedTime(answers);
        int score = Answer.calculateTotalScore(answers);
        return ok(report.render(score,totalUsedTime,trial.quizzes.size(), "Report", user));
    }
    //ตรวจสอบว่าผู้ใช้ทำการทดลองหรือยัง
    @Security.Authenticated(Secured.class)
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
    }

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

        return redirect(routes.Admin.index());
    }

}
