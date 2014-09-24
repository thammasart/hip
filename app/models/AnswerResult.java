package models;

import java.util.Date;

public interface AnswerResult {

    public ExperimentSchedule getExperimentScheduleObject();
    public long getTrialIdLong();
    public String getParameterType();
    public User getUserObject();
    public long getQuestionIdLong();
    public long getQuizIdLong();
    public String getIsCorrectString();
    public double getUsedTimeDouble();
    public TimeLog getTimeLogObject();
    public String getStartTimeToString();
    public String getEndTimeToString();
}
