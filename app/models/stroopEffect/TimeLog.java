package models.stroopEffect;

import models.User;
import play.db.ebean.Model;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table (name="stroop_time_log")
public class TimeLog extends Model{
    @Id
    public long id;
    public Date startTime = new Date();
    public Date endTime;

    @ManyToOne
    public User user;
    @ManyToOne
    public Trial trial;

    public TimeLog(Date startTime){
        this.startTime = startTime;
    }

    public TimeLog(Date starTime,Date endTime){
        this.startTime = starTime;
        this.endTime = endTime;
    }

    public static TimeLog create(Date startTime, User user, Trial trial){
        TimeLog timeLog = new TimeLog(startTime);
        timeLog.user = user;
        timeLog.trial = trial;
        timeLog.endTime = null;
        return timeLog;
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
