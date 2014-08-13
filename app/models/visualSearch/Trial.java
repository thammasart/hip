package models.visualSearch;

import models.TimeLog;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import models.ExperimentSchedule;
import play.db.ebean.Model;
import play.libs.Json;

import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table (name="visual_search_trial")
public class Trial extends Model{

    public static final int SMALLER_WIDTH = 300;
    public static final int SMALLER_HEIGHT = 200;
    public static final int SMALL_WIDTH = 500;
    public static final int SMALL_HEIGHT = 300;
    public static final int MEDIUM_WIDTH = 600;
    public static final int MEDIUM_HEIGHT = 400;
    public static final int BIG_WIDTH = 800;
    public static final int BIG_HEIGHT = 500;
    public static final int EXTRA_WIDTH = 1000;
    public static final int EXTRA_HEIGHT = 500;

    @Id
    public long id;
    @ManyToOne
    public ExperimentSchedule schedule;
    @OneToOne(mappedBy = "trial",cascade = CascadeType.ALL)
    @JsonManagedReference(value="trial")
    public Quiz quiz;
    public double totalScore = 0;
    public double totalUsedTime = 0;
    public int totalUser = 0;

    public Trial(ExperimentSchedule schedule){
    	this.schedule = schedule;
    }

    public void updateResult(){
        this.totalScore = 0;
        this.totalUsedTime = 0;
    //    for(Quiz q:quizzes){
    //        this.totalScore += Answer.calculateTotalScore(q.findAnswers());
    //        this.totalUsedTime += Answer.calculateTotalUsedTime(q.findAnswers());
     //   }
        this.totalUser = TimeLog.calaulateTotalUserTakeExp(schedule,id);
        this.totalScore /=totalUser;
        this.totalUsedTime /=totalUser;
        this.update();
    }

    public static Trial findById(long id){
        return find.byId(new Long(id));
    }

    public static List<Trial> findInvolving(ExperimentSchedule ex){
        return find.where().eq("schedule", ex).findList();
    }

    public static void displayJson(){
        System.out.println(Json.stringify(Json.toJson(Trial.find.all().get(0))));
    }
    public String toJson(){
        return Json.stringify(Json.toJson(this));
    }
    public static String toJsonArray(List<Trial> trials){
        String result = "[";
        for(int i=0;i< trials.size();i++){
            result += trials.get(i).toJson();
            if(i < trials.size() - 1){
                result += ",";
            }
        }
        result += "]";
        return result;

    }

    @SuppressWarnings("unchecked")
    public static Finder<Long, Trial> find = new Finder(Long.class, Trial.class);

    public static Trial create(ExperimentSchedule schedule) {
        Trial trial = new Trial(schedule);
        return trial;
    }

    public void generateQuiz() {
        Quiz quiz = Quiz.create(this);
        quiz.save();
    }
}
