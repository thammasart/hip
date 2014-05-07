package controllers;

import models.ExperimentSchedule;

import play.*;
import play.mvc.*;
import play.data.*;

import models.User;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import views.html.visualSearch.*;
import views.html.iframe.*;
import java.util.Date;

public class VisualSearch extends Controller{

    //แสดงหน้าข้อมูลการทดลอง
    @Security.Authenticated(Secured.class)
    public static Result info(){
        User user = User.find.byId(request().username());
        return ok(info.render(user));
    }

    //แสดงกรอบในหน้าข้อมูลการทดลอง
    @Security.Authenticated(Secured.class)
    public static Result infoIframe(){
        return ok(visual_search_iframe.render());
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
        return ok(visual_search_proc_iframe.render());
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
        double time = Double.parseDouble(reportData.get("usedTime"));
        int score = Integer.parseInt(reportData.get("score"));
        return ok(demoReport.render(score,time,1,"Demo Report",user));
    }

    //แสดงหน้าการทดลอง
    @Security.Authenticated(Secured.class)
    public static Result experiment(long trialId, int questionNo){
        return TODO;
    }


    @Security.Authenticated(Secured.class)
    public static Result saveAnswer(long trialId, int questionNo){
        return TODO;
    }

    //แสดงหน้าผลลัพธ์การทดลอง
    @Security.Authenticated(Secured.class)
    public static Result report(String username, long trialId){
        if(username.equals("") || trialId == 0){
            return redirect(controllers.routes.SimonEffect.info());
        }
        User user = User.find.byId(username);
        return TODO;
    }

}
