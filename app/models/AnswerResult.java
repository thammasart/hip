package models;

import java.util.Date;

public interface AnswerResult {

    public ExperimentSchedule getExperimentSchedule();
    public long getTrialId();
    public String getParameterType();
    public User getUser();
    public long getQuestionId();
    public long getQuizId();
    public String getIsCorrect();
    public double getUsedTime();
    public TimeLog getTimeLog();
}
