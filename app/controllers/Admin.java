package controllers;

import org.joda.time.LocalDate;
import play.mvc.*;
import play.data.*;
import play.data.DynamicForm;
import models.*;
import com.avaje.ebean.*;
import views.html.admin.*;
import java.util.List;
import java.util.ArrayList;

import java.util.List;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.Date;

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
       // List<User> userList = User.find.all(); // I think it useless : who wrote it plz take care .
        return ok(user_info.render(User.getAllUser(),User.find.byId(session().get("username"))));
    }

    //Method ไว้ดึง Object AnswerResult ทั้งหมด
    public static List<AnswerResult> getAllResult(){
        List<AnswerResult> answers = new ArrayList<AnswerResult>();

        answers.addAll(models.attentionBlink.Answer.find.all());
        answers.addAll(models.brownPeterson.Answer.find.all());
        answers.addAll(models.changeBlindness.Answer.find.all());
        answers.addAll(models.garnerInterference.Answer.find.all());
        answers.addAll(models.magicNumber7.Answer.find.all());
        answers.addAll(models.mullerLayer.Answer.find.all());
        answers.addAll(models.positionError.Answer.find.all());
        answers.addAll(models.signalDetection.Answer.find.all());
        answers.addAll(models.simonEffect.Answer.find.all());
        answers.addAll(models.sternbergSearch.Answer.find.all());
        answers.addAll(models.stroopEffect.Answer.find.all());
        answers.addAll(models.visualSearch.Answer.find.all());

        return answers;
    }

    //Method ไว้ดึง Object AnswerResult จาก User ที่กำหนด
    public static List<AnswerResult> getResultByUser(List<User> users){

        List<AnswerResult> answers = new ArrayList<AnswerResult>();

        for (int i =0;i< users.size() ;i++){
            answers.addAll(models.attentionBlink.Answer.find.where().eq("user" ,users.get(i)).findList());
            answers.addAll(models.brownPeterson.Answer.find.where().eq("user", users.get(i)).findList());
            answers.addAll(models.changeBlindness.Answer.find.where().eq("user", users.get(i)).findList());
            answers.addAll(models.garnerInterference.Answer.find.where().eq("user", users.get(i)).findList());
            answers.addAll(models.magicNumber7.Answer.find.where().eq("user", users.get(i)).findList());
            answers.addAll(models.mullerLayer.Answer.find.where().eq("user", users.get(i)).findList());
            answers.addAll(models.positionError.Answer.find.where().eq("user" ,users.get(i)).findList());
            answers.addAll(models.signalDetection.Answer.find.where().eq("user" ,users.get(i)).findList());
            answers.addAll(models.simonEffect.Answer.find.where().eq("user" ,users.get(i)).findList());
            answers.addAll(models.sternbergSearch.Answer.find.where().eq("user" ,users.get(i)).findList());
            answers.addAll(models.stroopEffect.Answer.find.where().eq("user" ,users.get(i)).findList());
            answers.addAll(models.visualSearch.Answer.find.where().eq("user" ,users.get(i)).findList());
        }

        return answers;
    }

    //Method ไว้ดึง Object AnswerResult จาก Schedule ที่กำหนด
    public static List<AnswerResult> getResultByExp(List<ExperimentSchedule> exps,String type){

        List<AnswerResult> answers = new ArrayList<AnswerResult>();
        int size = exps.size();
        for (int i =0;i< size ;i++){

            if (type.equalsIgnoreCase("Attention Blink")){
                List<models.attentionBlink.Trial> trials = exps.get(i).findattentionTrials();
                int trialSize = trials.size();
                for (int j=0;j<trialSize;j++){
                    List<models.attentionBlink.Quiz> quizzes = trials.get(j).quizzes;
                    for (int k=0;k<quizzes.size();k++)
                        answers.addAll(models.attentionBlink.Answer.find.where().eq("quiz" ,quizzes.get(k)).findList());
                }
            }
            else if (type.equalsIgnoreCase("Brown Peterson")){
                List<models.brownPeterson.Trial> trials = exps.get(i).findbrowntrials();
                int trialSize = trials.size();
                for (int j=0;j<trialSize;j++){
                    List<models.brownPeterson.Quiz> quizzes = trials.get(j).quizzes;
                    for (int k=0;k<quizzes.size();k++)
                        answers.addAll(models.brownPeterson.Answer.find.where().eq("quiz" ,quizzes.get(k)).findList());
                }
            }
            else if (type.equalsIgnoreCase("Change Blindness")){
                List<models.changeBlindness.Trial> trials = exps.get(i).findchangeBlindnesstrials();
                int trialSize = trials.size();
                for (int j=0;j<trialSize;j++){
                    List<models.changeBlindness.Quiz> quizzes = trials.get(j).quizzes;
                    for (int k=0;k<quizzes.size();k++)
                        answers.addAll(models.changeBlindness.Answer.find.where().eq("quiz" ,quizzes.get(k)).findList());
                }
            }
            else if (type.equalsIgnoreCase("Garner Interference")){
                List<models.garnerInterference.Trial> trials = exps.get(i).findgarnertrials();
                int trialSize = trials.size();
                for (int j=0;j<trialSize;j++){
                    List<models.garnerInterference.Quiz> quizzes = trials.get(j).quizzes;
                    for (int k=0;k<quizzes.size();k++)
                        answers.addAll(models.garnerInterference.Answer.find.where().eq("quiz" ,quizzes.get(k)).findList());
                }
            }
            else if (type.equalsIgnoreCase("Magic Number 7")){
                List<models.magicNumber7.Trial> trials = exps.get(i).findmagic7trials();
                int trialSize = trials.size();
                for (int j=0;j<trialSize;j++){
                    List<models.magicNumber7.Quiz> quizzes = trials.get(j).quizzes;
                    for (int k=0;k<quizzes.size();k++)
                        answers.addAll(models.magicNumber7.Answer.find.where().eq("quiz" ,quizzes.get(k)).findList());
                }
            }
            else if (type.equalsIgnoreCase("Muller Layer")){
                List<models.mullerLayer.Trial> trials = exps.get(i).findmullertrials();
                int trialSize = trials.size();
                for (int j=0;j<trialSize;j++){
                    List<models.mullerLayer.Quiz> quizzes = trials.get(j).quizzes;
                    for (int k=0;k<quizzes.size();k++)
                        answers.addAll(models.mullerLayer.Answer.find.where().eq("quiz" ,quizzes.get(k)).findList());
                }
            }
            else if (type.equalsIgnoreCase("Position Error")){
                List<models.positionError.Trial> trials = exps.get(i).findpositionErrortrials();
                int trialSize = trials.size();
                for (int j=0;j<trialSize;j++){
                    List<models.positionError.Quiz> quizzes = trials.get(j).quizzes;
                    for (int k=0;k<quizzes.size();k++)
                        answers.addAll(models.positionError.Answer.find.where().eq("quiz" ,quizzes.get(k)).findList());
                }
            }
            else if (type.equalsIgnoreCase("Signal Detection")){
                List<models.signalDetection.Trial> trials = exps.get(i).findsignaltrials();
                int trialSize = trials.size();
                for (int j=0;j<trialSize;j++){
                    List<models.signalDetection.Quiz> quizzes = trials.get(j).quizzes;
                    for (int k=0;k<quizzes.size();k++)
                        answers.addAll(models.signalDetection.Answer.find.where().eq("quiz" ,quizzes.get(k)).findList());
                }
            }
            else if (type.equalsIgnoreCase("Simon Effect")){
                List<models.simonEffect.Trial> trials = exps.get(i).findsimontrials();
                int trialSize = trials.size();
                for (int j=0;j<trialSize;j++){
                    List<models.simonEffect.Quiz> quizzes = trials.get(j).quizzes;
                    for (int k=0;k<quizzes.size();k++)
                        answers.addAll(models.simonEffect.Answer.find.where().eq("quiz" ,quizzes.get(k)).findList());
                }
            }
            else if (type.equalsIgnoreCase("Sternberg Search")){
                List<models.sternbergSearch.Trial> trials = exps.get(i).findsternbergSearchtrials();
                int trialSize = trials.size();
                for (int j=0;j<trialSize;j++){
                    List<models.sternbergSearch.Quiz> quizzes = trials.get(j).quizzes;
                    for (int k=0;k<quizzes.size();k++)
                        answers.addAll(models.sternbergSearch.Answer.find.where().eq("quiz" ,quizzes.get(k)).findList());
                }
            }
            else if (type.equalsIgnoreCase("Stroop Effect")){
                List<models.stroopEffect.Trial> trials = exps.get(i).findstroopTrials();
                int trialSize = trials.size();
                for (int j=0;j<trialSize;j++){
                    List<models.stroopEffect.Quiz> quizzes = trials.get(j).quizzes;
                    for (int k=0;k<quizzes.size();k++)
                        answers.addAll(models.stroopEffect.Answer.find.where().eq("quiz" ,quizzes.get(k)).findList());
                }
            }
            else if (type.equalsIgnoreCase("Visual Search")){
                List<models.visualSearch.Trial> trials = exps.get(i).findvisualSearchTrials();
                int trialSize = trials.size();
                for (int j=0;j<trialSize;j++){
                    answers.addAll(models.visualSearch.Answer.find.where().eq("quiz" ,trials.get(j).quiz).findList());
                }
            }

        }

        return answers;
    }

    //แสดงหน้าเพิ่ม user
    @Security.Authenticated(Secured.class)
    public static Result RenderAdminExperimentResult() {
        return ok(experiment_result.render(getAllResult()));
    }
    @Security.Authenticated(Secured.class)
    public static Result RenderAdminFindResult() {
        return ok(find_result.render(User.getAllUser()));
    }

    public static Result resultByExp(){
        DynamicForm  stringForm = Form.form().bindFromRequest();
        String name = stringForm.get("scheduleName");
        String typeString = stringForm.get("expType");
        String startString = stringForm.get("startDate");
        String expireString = stringForm.get("expireDate");
        ExperimentType expType = ExperimentType.ATTENTIONBLINK;
        if (typeString.equalsIgnoreCase("Attention Blink")){
            expType = ExperimentType.ATTENTIONBLINK;
        }
        else if (typeString.equalsIgnoreCase("Brown Peterson")){
            expType = ExperimentType.BROWNPETERSON;
        }
        else if (typeString.equalsIgnoreCase("Change Blindness")){
            expType = ExperimentType.CHANGEBLINDNESS;
        }
        else if (typeString.equalsIgnoreCase("Garner Interference")){
            expType = ExperimentType.GARNERINTERFERENCE;
        }
        else if (typeString.equalsIgnoreCase("Magic Number 7")){
            expType = ExperimentType.MAGICNUMBER7;
        }
        else if (typeString.equalsIgnoreCase("Muller Layer")){
            expType = ExperimentType.MULLERLAYER;
        }
        else if (typeString.equalsIgnoreCase("Position Error")){
            expType = ExperimentType.POSITIONERROR;
        }
        else if (typeString.equalsIgnoreCase("Signal Detection")){
            expType = ExperimentType.SIGNALDETECTION;
        }
        else if (typeString.equalsIgnoreCase("Simon Effect")){
            expType = ExperimentType.SIMONEFFECT;
        }
        else if (typeString.equalsIgnoreCase("Sternberg Search")){
            expType = ExperimentType.STERNBERGSEARCH;
        }
        else if (typeString.equalsIgnoreCase("Stroop Effect")){
            expType = ExperimentType.STROOPEFFECT;
        }
        else if (typeString.equalsIgnoreCase("Visual Search")){
            expType = ExperimentType.VISUALSEARCH;
        }


        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date startDate;
        Date expireDate;
        List<ExperimentSchedule> expList;
        try {
            startDate = df.parse(startString);
            expireDate = df.parse(expireString);
            expireDate = new Date(expireDate.getTime() + 86400000 - 1);
            if (typeString.equalsIgnoreCase("All Type")){
                expList = ExperimentSchedule.find.where().icontains("name", name)
                        .ge("startDate", startDate)
                        .le("expireDate", expireDate)
                        .findList();
            }
            else{
                expList = ExperimentSchedule.find.where().icontains("name", name)
                        .eq("experimentType",expType)
                        .ge("startDate", startDate)
                        .le("expireDate", expireDate)
                        .findList();
            }
        } catch (ParseException e) {
            if (typeString.equalsIgnoreCase("All Type")){
                expList = ExperimentSchedule.find.where().icontains("name", name)
                        .findList();
            }
            else{
                expList = ExperimentSchedule.find.where().icontains("name", name)
                        .eq("experimentType",expType)
                        .findList();
            }
        }
        //flash("savedSuccess","Filter Search Successfully!");
        //return ok(views.html.admin.experiment.main.render(expList));
        return ok(experiment_result.render(getResultByExp(expList,typeString)));
    }

    public static Result resultByUser(){
        DynamicForm stringForm = Form.form().bindFromRequest();
        String username = stringForm.get("username");

        User temp = new User(username,"");

        List<User> users = new ArrayList<User>();
        //if (!username.equals("")){
        //    users.add(User.find.where().eq("username",username).findUnique());
        //    return ok(experiment_result.render(getResultByUser(users)));
        //}else{
        boolean isFirst = true;
        String oql ="  find  user ";
        String firstName = stringForm.get("firstname");
        String lastName = stringForm.get("lastname");
        String email = stringForm.get("eMail");
        String gender = stringForm.get("gender");
        if (gender == null)
            gender = "";
        String status = stringForm.get("status");
        UserRole role = null;
        if (status.equals("Admin")){
            role = UserRole.ADMIN;
        }else if (status.equals("Student")){
            role = UserRole.STUDENT;
        }else if (status.equals("Guest")){
            role = UserRole.GUEST;
        }else if (status.equals("TA")){
            role = UserRole.TA;
        }
        String section = stringForm.get("section");
        String semester = stringForm.get("semester");
        String academicYear = stringForm.get("academicYear");
        String faculty = stringForm.get("faculty");
        String department = stringForm.get("department");

        if (stringForm.get("birthDate").equals("")){

            if (role == null){
                if (stringForm.get("year").equals("")){
                    users = User.find.where().icontains("username", username)
                            .icontains("firstName", firstName)
                            .icontains("lastName", lastName)
                            .istartsWith("gender", gender)
                            .icontains("eMail", email)
                            .icontains("section", section)
                            .icontains("semester", semester)
                            .icontains("academicYear", academicYear)
                            .icontains("faculty", faculty)
                            .icontains("department", department)
                            .findList();
                }else{
                    int year = Integer.valueOf(stringForm.get("year")).intValue();
                    users = User.find.where().icontains("username", username)
                            .icontains("firstName", firstName)
                            .icontains("lastName",lastName)
                            .istartsWith("gender", gender)
                            .icontains("eMail", email)
                            .eq("year",year)
                            .icontains("section",section)
                            .icontains("semester",semester)
                            .icontains("academicYear",academicYear)
                            .icontains("faculty",faculty)
                            .icontains("department",department)
                            .findList();
                }
            }else{
                if (stringForm.get("year").equals("")){
                    users = User.find.where().icontains("username", username)
                            .icontains("firstName", firstName)
                            .icontains("lastName", lastName)
                            .eq("status",role)
                            .istartsWith("gender", gender)
                            .icontains("eMail", email)
                            .icontains("section", section)
                            .icontains("semester", semester)
                            .icontains("academicYear", academicYear)
                            .icontains("faculty", faculty)
                            .icontains("department", department)
                            .findList();
                }else{
                    int year = Integer.valueOf(stringForm.get("year")).intValue();
                    users = User.find.where().icontains("username", username)
                            .icontains("firstName", firstName)
                            .icontains("lastName",lastName)
                            .eq("status",role)
                            .istartsWith("gender", gender)
                            .icontains("eMail", email)
                            .eq("year",year)
                            .icontains("section",section)
                            .icontains("semester",semester)
                            .icontains("academicYear",academicYear)
                            .icontains("faculty",faculty)
                            .icontains("department",department)
                            .findList();
                }
            }
        }
        else{
            Date dob;
            try{
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                dob = dateFormat.parse(stringForm.get("birthDate"));
            }catch (ParseException e){
                dob = new Date();
            }
            if (role == null){
                if (stringForm.get("year").equals("")){
                    users = User.find.where().icontains("username", username)
                            .icontains("firstName", firstName)
                            .icontains("lastName", lastName)
                            .istartsWith("gender", gender)
                            .icontains("eMail", email)
                            .eq("birthDate",dob)
                            .icontains("section", section)
                            .icontains("semester", semester)
                            .icontains("academicYear", academicYear)
                            .icontains("faculty", faculty)
                            .icontains("department", department)
                            .findList();
                }else{
                    int year = Integer.valueOf(stringForm.get("year")).intValue();
                    users = User.find.where().icontains("username", username)
                            .icontains("firstName", firstName)
                            .icontains("lastName",lastName)
                            .istartsWith("gender", gender)
                            .icontains("eMail", email)
                            .eq("birthDate", dob)
                            .eq("year", year)
                            .icontains("section",section)
                            .icontains("semester",semester)
                            .icontains("academicYear",academicYear)
                            .icontains("faculty",faculty)
                            .icontains("department",department)
                            .findList();
                }
            }else{
                if (stringForm.get("year").equals("")){
                    users = User.find.where().icontains("username", username)
                            .icontains("firstName", firstName)
                            .icontains("lastName", lastName)
                            .eq("status", role)
                            .istartsWith("gender", gender)
                            .icontains("eMail", email)
                            .eq("birthDate", dob)
                            .icontains("section", section)
                            .icontains("semester", semester)
                            .icontains("academicYear", academicYear)
                            .icontains("faculty", faculty)
                            .icontains("department", department)
                            .findList();
                }else{
                    int year = Integer.valueOf(stringForm.get("year")).intValue();
                    users = User.find.where().icontains("username", username)
                            .icontains("firstName", firstName)
                            .icontains("lastName",lastName)
                            .eq("status", role)
                            .istartsWith("gender", gender)
                            .icontains("eMail", email)
                            .eq("birthDate", dob)
                            .eq("year", year)
                            .icontains("section",section)
                            .icontains("semester",semester)
                            .icontains("academicYear",academicYear)
                            .icontains("faculty",faculty)
                            .icontains("department",department)
                            .findList();
                }
            }
        }
        return ok(experiment_result.render(getResultByUser(users)));
        //}
    }

    @Security.Authenticated(Secured.class)
    public static Result RenderAdminFindResultPost(int type) {
        if (type == 1)
            return resultByExp();
        else if (type == 2)
            return resultByUser();
        else return redirect(routes.Admin.RenderAdminFindResult());
    }

    //ทำการเพิ่ม user account ใหม่เข้าไปในระบบ
    @Security.Authenticated(Secured.class)
    public static Result addUser(String userNames){
       return ok(add_user.render(User.getAllUser(),userNames));
    }

    //แสดงหน้าแก้ไข user
    @Security.Authenticated(Secured.class)
    public static Result editUser(String userName){
        return ok(edit_user.render(User.find.where().eq("username",userName).findUnique()));
    }

    //ทำการแก้ไข user account ในระบบ
    public static Result type_editUser() {
        DynamicForm stringForm = Form.form().bindFromRequest();
        boolean userNotFound = false;
        User oldUser = User.find.where().eq("username",stringForm.get("usrName")).findUnique();
        if (oldUser != null){
            Form<User> userForm = Form.form(User.class).bindFromRequest();
            User newUser = userForm.get();
            oldUser.firstName = newUser.firstName;
            oldUser.lastName = newUser.lastName;
            oldUser.gender = newUser.gender;
            oldUser.birthDate = newUser.birthDate;
            oldUser.year = newUser.year;
            oldUser.eMail = newUser.eMail;
            oldUser.section = newUser.section;
            oldUser.semester = newUser.semester;
            oldUser.academicYear = newUser.academicYear;
            oldUser.faculty = newUser.faculty;
            oldUser.department = newUser.department;

            //่้try catch
            oldUser.update();
        }
        else{
            userNotFound = true;
        }

        if (userNotFound){
            String errorText = "No such a user in the database. ";
            flash("nullUser", errorText);
        }
        else
            flash("savedSuccess","Edit user successfully !");

        return ok(user_info.render(User.getAllUser(),User.find.byId(session().get("username"))));
    }

    //ทำการเพิ่ม user account ใหม่เข้าไปในระบบ
    private static Result type_addUser(){
        DynamicForm  stringForm = Form.form().bindFromRequest();
        String userString = stringForm.get("users");
        boolean nullUser = false;
        boolean whiteSpace = false;
        if (userString.isEmpty() || userString.trim().isEmpty()){
            nullUser = true;
        }
        if (userString.contains(" ")){
            whiteSpace = true;
        }

        if (!nullUser && !whiteSpace){
            String[] result = userString.split("\r\n");

            boolean userExist = false;

            List<String> userExistName = new ArrayList<String>();

            for(int i=0 ; i <result.length;i++){
                List<User> users = User.find.where().eq("username",result[i]).findList();
                if (users.size() == 0){
                    User temp = new User(result[i],result[i]);
                    temp.year = Integer.parseInt(stringForm.get("year"));
                    temp.gender = "";
                    temp.section = stringForm.get("section");
                    temp.semester = stringForm.get("semester");
                    temp.academicYear = stringForm.get("academicYear");
                    temp.department = stringForm.get("department");
                    temp.faculty = stringForm.get("faculty");

                    String userStatus = stringForm.get("userStatus");
                    if (userStatus.equalsIgnoreCase("ADMIN")){
                        temp.status = UserRole.ADMIN;
                    }
                    else if ((userStatus.equalsIgnoreCase("STUDENT"))){
                        temp.status = UserRole.STUDENT;
                    }
                    else if ((userStatus.equalsIgnoreCase("TA"))){
                        temp.status = UserRole.TA;
                    }
                    else if ((userStatus.equalsIgnoreCase("DUMMY"))){
                        temp.status = UserRole.DUMMY;
                    }
                    else if ((userStatus.equalsIgnoreCase("SPECIAL"))){
                        temp.status = UserRole.SPECIAL;
                    }
                    else{
                        temp.status = UserRole.DUMMY;
                    }

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
                flash("nullUser", errorText);
                return redirect(routes.Admin.addUser(userString));
            }
            else
                flash("savedSuccess","Add new user(s) successfully !");
            return ok(user_info.render(User.getAllUser(),User.find.byId(session().get("username"))));
        }
        else {
            if (nullUser)
                flash("nullUser", "Please enter a username");
            else
                flash("nullUser", "Please do not use a white space in a username");
            return redirect(routes.Admin.addUser(userString));
        }
    }
    //ทำการลบ user account ออกจากระบบ
    private static Result type_deleteUser(){
        DynamicForm  stringForm = Form.form().bindFromRequest();
        String userString = stringForm.get("deleteRow");
        String[] result = userString.split(":::::");
        User currentUser = User.find.byId(session().get("username"));
        String warning = "";
        if (result.length == 1 && result[0].equals("")){
            flash("userExisted","No records selected!");
            return ok(user_info.render(User.getAllUser(),currentUser));
        }
        for(int i=0 ; i <result.length;i++){
            User user = User.find.where().eq("username",result[i]).findUnique();
            if (!currentUser.username.equals(user.username)){
                user.status = UserRole.DELETED;
                user.save();
            }
            else
                warning = " but except your user.";
        }
        flash("savedSuccess","Delete user(s) successfully !"+ warning);
        return ok(user_info.render(User.getAllUser(),currentUser));
    }

    //แสดงหน้าUserหลังจาก เพิ่ม ลบ แก้ไข user แล้ว
    @Security.Authenticated(Secured.class)
    public static Result saveUser(int mode) {
        if (mode == 0)
            return type_addUser();
        else if (mode == 1)
            return type_editUser();
        else if (mode == 2){
            return type_deleteUser();
        }
        else
            return redirect(routes.Admin.renderUserInfo());

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

    //แสดงหน้าค้นหาการทดลอง
    @Security.Authenticated(Secured.class)
    public static Result findExperiment() {
        return ok(views.html.admin.experiment.findExp.render());
    }

    //เพิ่มExperiment แล้วแสดงผล
    public static Result type_addExperiment(){
        Form<ExperimentSchedule> boundForm = expForm.bindFromRequest();

        if(boundForm.hasErrors()){
            flash("error", "Please input experiment schedule name.");
            return badRequest(views.html.admin.experiment.add.render(expForm));
        }

        ExperimentSchedule exp = boundForm.get();
        exp.startDate = LocalDate.fromDateFields(exp.startDate).toDateTimeAtStartOfDay().toDate();
        exp.expireDate = LocalDate.fromDateFields(exp.expireDate).toDateMidnight().toDate();
        exp.status = ScheduleStatus.CLOSE;
        exp.save();
        flash("savedSuccess","Experiment Schedule added!");
        exp.generateTrials();
        return redirect(routes.Admin.displayExperimentList());
    }

    //กรองExperiment แล้วแสดงผล
    public static Result type_filterExperiment(){
        DynamicForm  stringForm = Form.form().bindFromRequest();
        String name = stringForm.get("scheduleName");
        String typeString = stringForm.get("expType");
        String status = stringForm.get("expStatus");
        String startString = stringForm.get("startDate");
        String expireString = stringForm.get("expireDate");
        ExperimentType expType = ExperimentType.ATTENTIONBLINK;
        if (typeString.equalsIgnoreCase("Attention Blink")){
            expType = ExperimentType.ATTENTIONBLINK;
        }
        else if (typeString.equalsIgnoreCase("Brown Peterson")){
            expType = ExperimentType.BROWNPETERSON;
        }
        else if (typeString.equalsIgnoreCase("Change Blindness")){
            expType = ExperimentType.CHANGEBLINDNESS;
        }
        else if (typeString.equalsIgnoreCase("Garner Interference")){
            expType = ExperimentType.GARNERINTERFERENCE;
        }
        else if (typeString.equalsIgnoreCase("Magic Number 7")){
            expType = ExperimentType.MAGICNUMBER7;
        }
        else if (typeString.equalsIgnoreCase("Muller Layer")){
            expType = ExperimentType.MULLERLAYER;
        }
        else if (typeString.equalsIgnoreCase("Position Error")){
            expType = ExperimentType.POSITIONERROR;
        }
        else if (typeString.equalsIgnoreCase("Signal Detection")){
            expType = ExperimentType.SIGNALDETECTION;
        }
        else if (typeString.equalsIgnoreCase("Simon Effect")){
            expType = ExperimentType.SIMONEFFECT;
        }
        else if (typeString.equalsIgnoreCase("Sternberg Search")){
            expType = ExperimentType.STERNBERGSEARCH;
        }
        else if (typeString.equalsIgnoreCase("Stroop Effect")){
            expType = ExperimentType.STROOPEFFECT;
        }
        else if (typeString.equalsIgnoreCase("Visual Search")){
            expType = ExperimentType.VISUALSEARCH;
        }


        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date startDate;
        Date expireDate;
        List<ExperimentSchedule> expList;

        try {
            startDate = df.parse(startString);
            expireDate = df.parse(expireString);
            expireDate = new Date(expireDate.getTime() + 86400000 - 1);
           ScheduleStatus sstatus;
            if (status.equalsIgnoreCase("Opened")){
                sstatus = ScheduleStatus.OPEN;
            }else if (status.equalsIgnoreCase("Closed")){
                sstatus = ScheduleStatus.CLOSE;
            }else{
                sstatus = ScheduleStatus.DISABLED;
            }
            if (status.equalsIgnoreCase("All")){
                if (typeString.equalsIgnoreCase("All Type")){
                    expList = ExperimentSchedule.find.where().icontains("name", name)
                            .ge("startDate", startDate)
                            .le("expireDate", expireDate)
                            .findList();
                }
                else{
                    expList = ExperimentSchedule.find.where().icontains("name", name)
                            .eq("experimentType",expType)
                            .ge("startDate", startDate)
                            .le("expireDate", expireDate)
                            .findList();
                }
            }
            else {
                if (typeString.equalsIgnoreCase("All Type")){
                    expList = ExperimentSchedule.find.where().icontains("name", name)
                            .ge("startDate", startDate)
                            .le("expireDate", expireDate)
                            .eq("status", sstatus)
                            .findList();
                }
                else{
                    expList = ExperimentSchedule.find.where().icontains("name", name)
                            .eq("experimentType",expType)
                            .ge("startDate", startDate)
                            .le("expireDate", expireDate)
                            .eq("status", sstatus)
                            .findList();
                }
            }
        } catch (ParseException e) {
            ScheduleStatus sstatus;
            if (status.equalsIgnoreCase("Opened")){
                sstatus = ScheduleStatus.OPEN;
            }else if (status.equalsIgnoreCase("Closed")){
                sstatus = ScheduleStatus.CLOSE;
            }else{
                sstatus = ScheduleStatus.DISABLED;
            }
            if (status.equalsIgnoreCase("All")){
                if (typeString.equalsIgnoreCase("All Type")){
                    expList = ExperimentSchedule.find.where().icontains("name", name)
                            .findList();
                }
                else{
                    expList = ExperimentSchedule.find.where().icontains("name", name)
                            .eq("experimentType",expType)
                            .findList();
                }
            }
            else {
                if (typeString.equalsIgnoreCase("All Type")){
                    expList = ExperimentSchedule.find.where().icontains("name", name)
                            .eq("status", sstatus)
                            .findList();
                }
                else{
                    expList = ExperimentSchedule.find.where().icontains("name", name)
                            .eq("experimentType",expType)
                            .eq("status", sstatus)
                            .findList();
                }
            }
        }
        flash("savedSuccess","Filter Search Successfully!");
        return ok(views.html.admin.experiment.main.render(expList));
    }
      
    public static Result type_deleteExperiment(){
        DynamicForm  stringForm = Form.form().bindFromRequest();
        String userString = stringForm.get("deleteRow");
        String[] result = userString.split(":::::");
        if (result.length == 1 && result[0].equals("")){
            flash("savedFail","No records selected!");
            return redirect(routes.Admin.displayExperimentList());
        }
        for(int i=0 ; i <result.length;i++){
            long expId = Long.parseLong(result[i]);
            ExperimentSchedule exp = ExperimentSchedule.find.byId(expId);
            exp.deleteExpAndRelative();
        }
        flash("savedSuccess","Delete schedule(s) successfully !");
        return redirect(routes.Admin.displayExperimentList());
    }
    //แสดงผลหน้า experiment set หลังจาก เพิ่ม ลบ ค้นหา แล้ว
    @Security.Authenticated(Secured.class)
    public static Result saveExperiment(int mode) {
        if (mode == 0){
            return type_addExperiment();
        }
        else if (mode == 1)
            return type_filterExperiment();
        else if (mode == 2)
            return type_deleteExperiment();
        else
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
        
        ExperimentSchedule exp = ExperimentSchedule.find.byId(expId);
        DynamicForm requestData = Form.form().bindFromRequest();
        
        exp.name = requestData.get("name");
        try{
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            exp.startDate = dateFormat.parse(requestData.get("startDate"));
            exp.expireDate = dateFormat.parse(requestData.get("expireDate"));
        } catch (ParseException e){
            flash("date_error", "กรุณากรอกข้อมูลช่วงเวลาการทำทดลองให้ถูกต้อง");
            return badRequest(views.html.admin.experiment.edit.render(exp));
        }
        exp.update();
        /* Create Trials for Experiment */

        List<models.brownPeterson.Trial> trials = models.brownPeterson.Trial.findInvolving(exp);
        for(models.brownPeterson.Trial trial : trials){
            for(models.brownPeterson.Quiz quiz : models.brownPeterson.Quiz.findInvolving(trial)){
                quiz.initCountdown = Integer.parseInt(requestData.get("initCountdown_" + quiz.id));
                quiz.flashTime = Integer.parseInt(requestData.get("flashTime_" + quiz.id));
                quiz.update();
            }
            String trigramType = requestData.get("trigramType-" + trial.id);
            String trigramLanguage = requestData.get("trigramLanguage-" + trial.id);
            if(!(trigramType.equals(trial.trigramType) && trigramLanguage.equals(trial.trigramLanguage))) {
                trial.trigramType = trigramType;
                trial.trigramLanguage = trigramLanguage;
                trial.randomNewQuestions();
            }
            
            trial.update();
        }
        // end create Trial
        flash("success", "update success.");
        return ok(views.html.admin.experiment.edit.render(exp));
    }

    public static Result displayBrownPetersonQuestionList(long id){
        return ok(views.html.admin.experiment.displayQuestions.render(id));
    }

    public static Result addBrownPetersonQuestion(long id){
        return ok(views.html.admin.experiment.addQuestion.render(id));
    }

    public static Result saveBrownPetersonQuestion(long id){
        DynamicForm  questionForm = Form.form().bindFromRequest();
        String tempQuestion = questionForm.get("questions");
        String trigramType = questionForm.get("trigramType");
        String trigramLanguage = questionForm.get("trigramLanguage");

        String[] questions = tempQuestion.split("\\r?\\n");
        int counter = 0;

        for(String question : questions){
            try{
                String[] words = null;
                if(question.contains(",")){
                    words = question.split(",");
                }else{
                    words = question.split("\\s+");
                }   
                models.brownPeterson.Question.create(words[0],words[1],words[2], trigramType, trigramLanguage).save();
            }catch(Exception e){
                String warning = "เพิ่มคำถามสำเร็จ " + counter + " คำถาม" +
                                " พบปัญหาที่บรรทัดที่ " + (counter + 1) +
                                " [" + questions[counter] + "]" +
                                " โปรดลองใหม่อีกครั้ง";
                flash("error", warning);
                return badRequest(views.html.admin.experiment.addQuestion.render(id));
            }
            counter++;
        }

        flash("success", "update success.");
        
        return ok(views.html.admin.experiment.displayQuestions.render(8L));
    }

    public static Result randomBrownPetersonQuestion(long expId, long quizId){
        models.brownPeterson.Quiz quiz = models.brownPeterson.Quiz.find.byId(quizId);
        quiz.randomToNewQuestion();
        flash("success", "เปลี่ยนแปลงคำถามเรียบร้อยแล้ว");
        return redirect(routes.Admin.displayParameter(expId));
    }

    public static Result displayExperimentResult(long expId){
        return ok(views.html.admin.experiment.result.render(ExperimentSchedule.find.byId(expId)));
    }

    public static Result switchStroopEffectQuestion(long expId, long quizId){
        models.stroopEffect.Quiz quiz = models.stroopEffect.Quiz.find.byId(quizId);
        quiz.switchRandomQuestion();
        flash("success", "เปลี่ยนแปลงคำถามเรียบร้อยแล้ว");
        return redirect(routes.Admin.displayParameter(expId));
    }

    public static Result saveStroopEffectParameter(long expId){
        ExperimentSchedule exp = ExperimentSchedule.find.byId(expId);
        DynamicForm  requestData = Form.form().bindFromRequest();

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
        
        List<models.stroopEffect.Trial> trials = models.stroopEffect.Trial.findInvolving(exp);
        for(models.stroopEffect.Trial trial : trials ){
            String questionType = requestData.get("questionType-" + trial.id);
            if(!trial.questionType.toString().equals(questionType)){
                trial.toQuestionTYpe(questionType);
                trial.randomNewQuestions();
            }

            for(models.stroopEffect.Quiz quiz : trial.quizzes){
                String isMatchString = requestData.get("isMatch-" + quiz.id);
                boolean isMatch = true;
                if(isMatchString == null){
                    isMatch = false;
                }else{
                    isMatch = true;
                }

                if(isMatch != quiz.question.isMatch()){
                    quiz.switchRandomQuestion();
                }
            }
        }

        flash("success", "update success.");
        return ok(views.html.admin.experiment.edit.render(exp));
    }

    public static Result saveSignalDetectionParameter(long expId){
        ExperimentSchedule exp = ExperimentSchedule.find.byId(expId);
        DynamicForm  requestData = Form.form().bindFromRequest();

        exp.name = requestData.get("name");
        try{
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            exp.startDate = dateFormat.parse(requestData.get("startDate"));
            exp.expireDate = dateFormat.parse(requestData.get("expireDate"));
        } catch (ParseException e){
            flash("date_error", "กรุณากรอกข้อมูลช่วงเวลาการทำทดลองให้ถูกต้อง");
            return badRequest(views.html.admin.experiment.edit.render(exp));
        }
        exp.update();

        List<models.signalDetection.Trial> trials = models.signalDetection.Trial.findInvolving(exp);

        for(models.signalDetection.Trial trial : trials){
            for(models.signalDetection.Quiz quiz : trial.quizzes){
                
                String targetString = requestData.get("target_" + quiz.id);
                String noiseString = requestData.get("noise_" + quiz.id);
                String lengthString = requestData.get("length_" + quiz.id);
                String noOfTargetString = requestData.get("noOfTarget_" + quiz.id);
                String displayTimeString = requestData.get("displayTime_" + quiz.id);

                char target = targetString.charAt(0);
                char noise = noiseString.charAt(0);
                int length = Integer.parseInt(lengthString);
                int noOfTarget = Integer.parseInt(noOfTargetString);
                double displayTime = Double.parseDouble(displayTimeString);
                quiz.question.target = target;
                quiz.question.noise = noise;
                quiz.length = length;
                quiz.noOfTarget = noOfTarget;
                quiz.displayTime = displayTime;
                quiz.question.update();
                quiz.update();
                
            }
        }

        flash("success", "update success.");
        return ok(views.html.admin.experiment.edit.render(exp));
    }
    public static Result saveSimonEffectParameter(long expId){
        ExperimentSchedule exp = ExperimentSchedule.find.byId(expId);
        DynamicForm  requestData = Form.form().bindFromRequest();

        exp.name = requestData.get("name");
        try{
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            exp.startDate = dateFormat.parse(requestData.get("startDate"));
            exp.expireDate = dateFormat.parse(requestData.get("expireDate"));
        } catch (ParseException e){
            flash("date_error", "กรุณากรอกข้อมูลช่วงเวลาการทำทดลองให้ถูกต้อง");
            return badRequest(views.html.admin.experiment.edit.render(exp));
        }
        exp.update();

        List<models.simonEffect.Trial> trials = models.simonEffect.Trial.findInvolving(exp);

        for(models.simonEffect.Trial trial : trials){

            String blinkTimeString = requestData.get("blinkTime_" + trial.id);
            String questionTypeString = requestData.get("questionType_" + trial.id);

            double blinkTime = Double.parseDouble(blinkTimeString);
            models.simonEffect.QuestionType questionType = null;
            if(questionTypeString.equals("ONEFEATURE"))
                questionType = models.simonEffect.QuestionType.ONEFEATURE;
            else if(questionTypeString.equals("TWOFEATURE"))
                questionType = models.simonEffect.QuestionType.TWOFEATURE;

            trial.blinkTime = blinkTime;
            trial.questionType = questionType;

            for(models.simonEffect.Quiz quiz : trial.quizzes){
                quiz.question = models.simonEffect.Question.findQuestionByType(questionType);
                quiz.position = models.simonEffect.Quiz.randomPosition();
                quiz.update();
            }
            trial.update();
        }

        flash("success", "update success.");
        return ok(views.html.admin.experiment.edit.render(exp));
    }


}
