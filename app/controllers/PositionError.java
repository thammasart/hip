package controllers;

import models.ExperimentSchedule;
import models.User;

import play.*;
import play.mvc.*;
import play.data.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import views.html.positionError.*;
import views.html.iframe.*;
import java.util.Date;

public class PositionError extends Controller{

    @Security.Authenticated(Secured.class)
    public static Result info(){
        User user = User.find.byId(request().username());
        return ok(info.render(user));
    }
    //แสดงกรอบในหน้าข้อมูลการทดลอง
    @Security.Authenticated(Secured.class)
    public static Result infoIframe(){
        return ok(position_error_iframe.render());
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
        return ok(position_error_proc_iframe.render());
    }
    @Security.Authenticated(Secured.class)
    public static Result demoPage(){
        return ok(demo.render());
    }
    @Security.Authenticated(Secured.class)
    public static Result demoReport(){

        DynamicForm reportData = Form.form().bindFromRequest();
        User user = User.find.byId(session().get("username"));
        double time = Double.parseDouble(reportData.get("usedTime"));
        String answer = reportData.get("answer");
        String solve = "ABCDEFGHIJ";
        int score = 1;
        for (int i=0;i<solve.length();i++){
            if (solve.charAt(i) != answer.charAt(i)){
                score = 0;
                break;
            }
        }
        return ok(demoReport.render(score,time,1,"Demo Report",user));

        //return TODO;
    }

    //ตรวจสอบว่าผู้ใช้ทำการทดลองหรือยัง
    @Security.Authenticated(Secured.class)
    public static Result checkUserTakeRepeatExperiment() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        if(user == null) {
            return redirect(routes.Application.index());
        }
        /*
        if(models.TimeLog.isRepeatTrial(user, 1, ExperimentSchedule.find.byId(7L))) {
            flash("repeat", "คุณเคยทำการทดลองนี้แล้ว หากต้องการทำต่อโปรดติดต่อผู้ดูแลระบบ");
            return ok(proc.render(user));
        }
        models.TimeLog.create(new Date(), user, 1,ExperimentSchedule.find.byId(7L)).save();
        return redirect(routes.AttentionBlink.experiment(4,0));
        */
        return TODO;
    }

}
