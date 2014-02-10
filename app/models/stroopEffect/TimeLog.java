package models.stroopEffect;

import play.db.ebean.Model;

import javax.persistence.*;

import java.util.Date;

import models.User;

@Entity
@Table (name="stroop_time_log")
public class TimeLog extends Model{

    @Id
    public long time_log_id;
    public Date start_time;
    public Date end_time;

    @ManyToOne
    public Trial trial;
    @ManyToOne
    public User user;

    public TimeLog(){

    }

    public TimeLog(Date starTime,Date endTime){
        this.start_time = starTime;
        this.end_time = endTime;
    }

    public static TimeLog create(Date starTime,Date endTime, User user, Trial trial) {
        TimeLog timeLog = new TimeLog(starTime, endTime);
        timeLog.user = user;
        timeLog.trial = trial;
        return timeLog;
    }

    public static boolean isRepeatTrial(User user, Trial trial){
        TimeLog timeLog = TimeLog.find.where().eq("user", user).eq("trial", trial).findUnique();
        return timeLog != null;
    }

    @SuppressWarnings("unchecked")
    public static Finder<Long, TimeLog> find = new Finder(Long.class, TimeLog.class);

}
