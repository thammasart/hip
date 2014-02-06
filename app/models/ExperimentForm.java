package models;
import java.util.Date;
import static play.data.validation.Constraints.*;

public class ExperimentForm {

    @Required
    public String name;
    public int noOfTrial;
    @Required
    public Date startDate;
    @Required
    public Date endDate;
    public ExperimentType experimentType;
}