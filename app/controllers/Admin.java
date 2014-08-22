package controllers;

import org.joda.time.LocalDate;
import play.mvc.*;
import play.data.*;
import play.data.DynamicForm;
import models.*;
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

    //แสดงหน้าเพิ่ม user
    @Security.Authenticated(Secured.class)
    public static Result RenderAdminExperimentResult() {
        return ok(experiment_result.render(User.getAllUser()));
    }
    @Security.Authenticated(Secured.class)
    public static Result RenderAdminFindResult() {
        return ok(find_result.render(User.getAllUser()));
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

    public static Result displayBrownPetersonQuestionList(){
        return ok(views.html.admin.experiment.displayQuestions.render());
    }

    public static Result addBrownPetersonQuestion(){
        return ok(views.html.admin.experiment.addQuestion.render());
    }

    public static Result saveBrownPetersonQuestion(){
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
                return badRequest(views.html.admin.experiment.addQuestion.render());
            }
            counter++;
        }

        flash("success", "update success.");
        
        return ok(views.html.admin.experiment.displayQuestions.render());
    }

    public static Result randomBrownPetersonQuestion(long expId, long quizId){
        models.brownPeterson.Quiz quiz = models.brownPeterson.Quiz.find.byId(quizId);
        quiz.randomToNewQuestion();
        flash("success", "เปลี่ยนแปลงคำถามเรียบร้อยแล้ว");
        return redirect(routes.Admin.displayParameter(expId));
    }
    public static Result deleteBrownPetersonQuestion(long questionId){
        models.brownPeterson.Question question = models.brownPeterson.Question.find.byId(questionId);
        if(question.findQuizzes().size() > 0){
            flash("error", "ไม่สามารถลบได้ เนื่องจากถูกใช้งานอยู่");
            System.out.println(question.findQuizzes().size());
        }else{
            question.delete();
            flash("success", "ลบคำถามเรียบร้อยแล้ว");
        }
        return redirect(routes.Admin.displayBrownPetersonQuestionList());
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

    public static Result saveAttentionBlinkParameter(long expId){
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

        List<models.attentionBlink.Trial> trials = models.attentionBlink.Trial.findInvolving(exp);
        
        for(models.attentionBlink.Trial trial : trials){
            boolean isQuestionChange = false;
            String questionType = requestData.get("questionType_" + trial.id);
            if(!trial.questionType.toString().equals(questionType)){
                trial.changeQuestionType(questionType);
                isQuestionChange = true;
                trial.update();
            }

            for(models.attentionBlink.Quiz quiz : trial.quizzes){
                boolean isCorrectChange = false;
                String lengthStr = requestData.get("length_" + quiz.id);
                String numberOfTargetStr = requestData.get("numberOfTarget_" + quiz.id);
                String isCorrectStr = requestData.get("isCorrect_" + quiz.id);
                String blinkTimeStr = requestData.get("blinkTime_" + quiz.id);
                boolean isCorrect = true;
                if(isCorrectStr == null){
                    isCorrect = false;
                }else{
                    isCorrect = true;
                }

                if(isCorrect != quiz.isCorrect){
                    quiz.isCorrect = isCorrect;
                    quiz.question.correctAnswer = isCorrect;
                    isCorrectChange = true;
                }


                    
                try{
                    int length = Integer.parseInt(lengthStr);
                    int numberOfTarget = Integer.parseInt(numberOfTargetStr);
                    double blinkTime = Double.parseDouble(blinkTimeStr);
                    if(length != quiz.length){
                        quiz.length = length;
                        isQuestionChange = true;
                    }
                    if(numberOfTarget != quiz.numberOfTarget){
                        quiz.numberOfTarget = numberOfTarget;
                        isQuestionChange = true;
                    }
                    quiz.blinkTime = blinkTime;
                } catch(Exception e){
                    flash("error", "ข้อมูลไม่ถูกต้อง โปรดตรวจสอบอีกครั้ง");
                    return badRequest(views.html.admin.experiment.edit.render(exp));
                }
                if(isCorrectChange && !isQuestionChange){
                    quiz.question.changeQuestionSetByCorrectAnswer();
                }
                quiz.update();
            }

            if(isQuestionChange){
                trial.generateNewQuestions();
                models.attentionBlink.Question.deleteAllUnusedQuestion();
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
