package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import play.data.DynamicForm;
import views.html.*;
import models.*;
import views.html.admin.*;
import java.util.List;
import java.util.ArrayList;
import models.brownPeterson.*;

import java.util.List;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.lang.Exception;


public class Admin extends Controller {
    private static final Form<ExperimentSchedule> expForm = Form.form(ExperimentSchedule.class);

    //แสดงผลหน้า home ของ admin
    @Security.Authenticated(Secured.class)
    public static Result index() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        List<User> userList = User.find.all();

        if(user == null){
            return redirect(routes.Application.index());
        }

        if(user.status != UserRole.ADMIN) {
            return redirect(routes.Application.home());
        }
        return ok(index_admin.render(User.getAllUser()));
        
    }
    //แสดงผลหน้าข้อมูลของ user ทั้งหมด
    @Security.Authenticated(Secured.class)
    public static Result renderUserInfo() {
        List<User> userList = User.find.all();
        return ok(user_info.render(User.getAllUser()));
    }

    //ทำการเพิ่ม user account ใหม่เข้าไปในระบบ
    @Security.Authenticated(Secured.class)
    public static Result saveUser() {
        DynamicForm  stringForm = Form.form().bindFromRequest();
        String userString = stringForm.get("users");

        if(userString.contains(" "))return TODO;
        String[] result = userString.split("\r\n");

        boolean userExist = false;
        List<String> userExistName = new ArrayList<String>();
        for(int i=0 ; i <result.length;i++){
            List<User> users = User.find.where().eq("username",result[i]).findList();
            if (users.size() == 0){
                User temp = new User(result[i],result[i],stringForm.get("section"),stringForm.get("semester"),stringForm.get("academicYear"),stringForm.get("department"),stringForm.get("faculty"));
                temp.save();
            }
            else{
                userExist = true;
                userExistName.add(result[i]);
            }
        }
        if (userExist){
            String errorText = "Already has user: ";
            for (String i : userExistName)
                errorText = errorText + i + " ";
            flash("userExisted", errorText);
        }
        else
            flash("savedSuccess","Add Success");

        return ok(user_info.render(User.getAllUser()));
    }
    
    //แสดงผลหน้า experiment set ทั้งหมดในระบบ
    @Security.Authenticated(Secured.class)
    public static Result displayExperimentList() {
        List<ExperimentSchedule> expList = ExperimentSchedule.find.all();
        return ok(views.html.admin.experiment.main.render(expList));
    }

    //แสดงหน้าเพิ่มชุดการทดลอง
    @Security.Authenticated(Secured.class)
    public static Result addExperiment() {
        return ok(views.html.admin.experiment.add.render(expForm));
    }

    //
    @Security.Authenticated(Secured.class)
    public static Result saveExperiment() {
        Form<ExperimentSchedule> boundForm = expForm.bindFromRequest();

        if(boundForm.hasErrors()){
            flash("error", "please correct the form above.");
            return badRequest(views.html.admin.experiment.add.render(expForm));
        }

        ExperimentSchedule exp = boundForm.get();
        exp.save();
        flash("success","Successfully");
        exp.generateTrials();
        return redirect(routes.Admin.displayExperimentList());
    }

    @Security.Authenticated(Secured.class)
    public static Result displayParameter(long id){
        final ExperimentSchedule exp = ExperimentSchedule.find.byId(id);
        if(exp == null){
            return notFound("This Experiment does not exist.");
        }
        return ok(views.html.admin.experiment.edit.render(exp));
    }

    @Security.Authenticated(Secured.class)
    public static Result saveBrownPetersonParameter(long expId){
        DynamicForm requestData = Form.form().bindFromRequest();
        ExperimentSchedule exp = ExperimentSchedule.find.byId(expId);
        
        exp.name = requestData.get("name");
        try{
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            exp.startDate = dateFormat.parse(requestData.get("startDate"));
            exp.expireDate = dateFormat.parse(requestData.get("expireDate"));
        } catch (ParseException e){
            flash("date_error", "กรุณากรอกข้อมูลช่วงเวลาการทำทดลองให้ถูกต้อง");
            return badRequest(views.html.admin.experiment.edit.render(exp));
        }
        exp.update();
        /* Create Trials for Experiment */

        List<Trial> trials = Trial.findInvolving(exp);
        for(Trial trial : trials){
            for(Quiz quiz : Quiz.findInvolving(trial)){
                quiz.initCountdown = Integer.parseInt(requestData.get("initCountdown-" + quiz.id));
                quiz.flashTime = Integer.parseInt(requestData.get("flashTime-" + quiz.id));
                quiz.update();
            }
            trial.trigramType = requestData.get("trigramType-" + trial.id);
            trial.trigramLanguage = requestData.get("trigramLanguage-" + trial.id);
            trial.update();
        }
        // end create Trial
        flash("success", "update success.");
        return ok(views.html.admin.experiment.edit.render(exp));
    }

    public static Result displayBrownPetersonQuestionList(){
        return ok(views.html.admin.experiment.displayQuestions.render());
    }

    public static Result addBrownPetersonQuestion(){
        return ok(views.html.admin.experiment.addQuestion.render());
    }

    public static Result saveBrownPetersonQuestion(){
        DynamicForm  questionForm = Form.form().bindFromRequest();
        String tempQuestion = questionForm.get("questions");

        String[] questions = tempQuestion.split("\\r?\\n");
        int counter = 0;

        for(String question : questions){
            try{
                String[] words = question.split(",");
                new models.brownPeterson.Question(words[0],words[1],words[2]).save();
            }catch(Exception e){
                String warning = "เพิ่มคำถามสำเร็จ " + counter + " คำถาม" +
                                " พบปัญหาที่บรรทัดที่ " + (counter + 1) +
                                " [" + questions[counter] + "]" +
                                " โปรดลองใหม่อีกครั้ง";
                flash("error", warning);
                return badRequest(views.html.admin.experiment.addQuestion.render());
            }
            counter++;
        }

        flash("success", "update success.");
        
        return ok(views.html.admin.experiment.displayQuestions.render());
    }

    public static Result displayExperimentResult(long expId){
        return ok(views.html.admin.experiment.result.render(ExperimentSchedule.find.byId(expId)));
    }
}
