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


public class Admin extends Controller {
    private static final Form<ExperimentSchedule> expForm = Form.form(ExperimentSchedule.class);
    private static final Form<Trial> trialForm = Form.form(Trial.class);

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

    public static Result renderUserInfo() {
        List<User> userList = User.find.all();
        return ok(user_info.render(User.getAllUser()));
    }

    public static Result saveUser() {
        DynamicForm  stringForm = Form.form().bindFromRequest();
        String userString = stringForm.get("users");
        StringTokenizer stz = new StringTokenizer(userString,"\n");
        List<User> userList = new ArrayList<User>();

        while(stz.hasMoreTokens()) { 
            String username = stz.nextToken();
            User usr = new User(username,username);
            usr.save();
            userList.add(usr);
        }
        return ok(user_info.render(userList));
    }

    public static Result experiment() {
        List<ExperimentSchedule> expList = ExperimentSchedule.find.all();
        return ok(views.html.admin.experiment.main.render(expList));
    }

    public static Result addExperiment() {
        return ok(views.html.admin.experiment.add.render(expForm));
    }

    public static Result saveExperiment() {
        Form<ExperimentSchedule> boundForm = expForm.bindFromRequest();
        if(boundForm.hasErrors()){
            flash("error", "please correct the form above.");
            return badRequest(views.html.admin.experiment.add.render(expForm));
        }
        ExperimentSchedule exp = boundForm.get();
        exp.save();
        flash("success","Successfully");
        for(int i = 0; i < exp.noOfTrial; i++){
            Trial trial = Trial.create(exp);
            trial.save();
            List<Question> questions = Question.getQuestionListBy(3); // 3 is number of quiz in trial.
            for(int j = 0; j < 3; j++){
                Quiz.create(100, 5, trial, questions.get(0)).save();
            }
        }
        return redirect(routes.Admin.experiment());
    }

    public static Result updateExperiment(long id) {
        Form<ExperimentSchedule> boundForm = expForm.bindFromRequest();
        ExperimentSchedule exp = ExperimentSchedule.find.byId(id);
        List<Trial> trials = Trial.findInvolving(exp);
        if(boundForm.hasErrors()){
            flash("error", "please correct the form above.");
            return badRequest(views.html.admin.experiment.edit.render(exp, trials));
        }

        boundForm.get().update(id);
        exp = ExperimentSchedule.find.byId(id);
        flash("success", "update success.");
        return ok(views.html.admin.experiment.edit.render(exp, trials));
    }

    public static Result parameter(long id){
        final ExperimentSchedule exp = ExperimentSchedule.find.byId(id);
        if(exp == null){
            return notFound("This Experiment does not exist.");
        }
        List<Trial> trials = Trial.findInvolving(exp);
        return ok(views.html.admin.experiment.edit.render(exp, trials));
    }
}
