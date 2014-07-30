package models;

import play.db.ebean.Model;
import javax.persistence.*;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class TimeLog extends Model{
	@Id
	public long id;
	public Date startTime = new Date();
	public Date endTime;

	@ManyToOne
	public User user;
	public long trialId;

	@ManyToOne
	@JsonManagedReference("timelog")
	public ExperimentSchedule exp;

	public TimeLog(Date startTime){
		this.startTime = startTime;
	}

	public TimeLog(Date startTime, Date endTime){
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	public static TimeLog create(Date startTime, User user
		,long trialId,ExperimentSchedule exp){
		TimeLog timeLog = new TimeLog(startTime);
		timeLog.user = user;
		timeLog.trialId = trialId;
		timeLog.endTime = null;
		timeLog.exp = exp;
		return timeLog;
	}

	public static int calaulateTotalUserTakeExp(ExperimentSchedule exps ,long trialId ){
		return TimeLog.find.where().eq("exp",exps).eq("trialId",trialId).findList().size();
	}

	public static boolean isRepeatTrial(User user, long trialId, ExperimentSchedule exp){
		TimeLog timeLog = TimeLog.find.where().eq("user", user).eq("trialId", trialId).eq("exp", exp).findUnique();
		return timeLog != null;
	}

	public static TimeLog findByUserAndTrialId(User user, Long trialId){
		return TimeLog.find.where().eq("user", user).eq("trialId", trialId).findUnique();
	}
	public static TimeLog findByUserAndTrialId(User user, Long trialId,ExperimentSchedule exp){
		return TimeLog.find.where().eq("user", user).eq("trialId", trialId).eq("exp",exp).findUnique();
	}

	@SuppressWarnings("unchecked")
	public static Finder<Long, TimeLog> find = new Finder(Long.class, TimeLog.class);
}
