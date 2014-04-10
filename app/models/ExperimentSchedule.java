package models;
import models.brownPeterson.Trial;
import play.db.ebean.*;
import javax.persistence.*;
import java.util.Date;
import play.data.format.Formats;
import java.util.List;
import com.avaje.ebean.*;
import java.util.ArrayList;
import static play.data.validation.Constraints.*;


@Entity
public class ExperimentSchedule extends Model{
	@Id
	public long id;
	@Required
	@Column(nullable=false, length=30)
	public String name = "Blank Experiment";
	@Column(nullable=false, length=2)
	public int noOfTrial = 3;
	@Column(nullable=false)
	@Required
	@Formats.DateTime(pattern="dd/mm/yyyy")
	public Date startDate;
	@Required
	@Column(nullable=false)
	@Formats.DateTime(pattern="dd/mm/yyyy")
	public Date expireDate;
	public ExperimentType experimentType;
	
	@OneToMany(mappedBy="schedule")
	public List<models.brownPeterson.Trial> trials = new ArrayList<models.brownPeterson.Trial>();
	@OneToMany(mappedBy="schedule")
	public List<models.stroopEffect.Trial> stroopTrials = new ArrayList<models.stroopEffect.Trial>();


	public ExperimentSchedule(String name, int noOfTrial, Date startDate, Date expireDate, ExperimentType experimentType) {
		this.name = name;
		this.noOfTrial = noOfTrial;
		this.startDate = startDate;
		this.expireDate = expireDate;
		this.experimentType = experimentType;
	}

	public static List<ExperimentSchedule> getAllWorkingExperiments() {
		List<ExperimentSchedule> experiments = find.where().betweenProperties("startDate", "expireDate", new Date()).findList();
		return experiments;
	}

	public static List<ExperimentSchedule> getWorkingExperimentsByType(ExperimentType expType) {
		List<ExperimentSchedule> allWorkingExp = getAllWorkingExperiments();
		List<ExperimentSchedule> exps = new ArrayList<ExperimentSchedule>();
		for(ExperimentSchedule exp : allWorkingExp) {
			if(exp.experimentType == expType) {
				exps.add(exp);
			}
		}
		return exps;
	}


	public static List<ExperimentSchedule> getWorkingExperimentsByType(String expType){
		List<ExperimentSchedule> expList = null;
		final String BROWNPETERSON = "BROWNPETERSON";
		final String STROOPEFFECT = "STROOPEFFECT";
        final String SIGNALDETECTION = "SIGNALDETECTION";
        final String ATTENTIONBLINK = "ATTENTIONBLINK";
        final String POSITIONERROR = "POSITIONERROR";

        if (expType.equals(BROWNPETERSON)){
            expList = getWorkingExperimentsByType(ExperimentType.BROWNPETERSON);
        }
        else if (expType.equals(STROOPEFFECT)){
            expList = getWorkingExperimentsByType(ExperimentType.STROOPEFFECT);
        }
        else if (expType.equals(SIGNALDETECTION)){
            expList = getWorkingExperimentsByType(ExperimentType.SIGNALDETECTION);
        }
        else if (expType.equals(ATTENTIONBLINK)){
            expList = getWorkingExperimentsByType(ExperimentType.ATTENTIONBLINK);
        }
        else if (expType.equals(POSITIONERROR)){
            expList = getWorkingExperimentsByType(ExperimentType.POSITIONERROR);
        }

        return expList;
	}
	
	public String getExperimentTypeToString(){
		String type = "Unknown";
		if(this.experimentType != null){
			switch(this.experimentType){
				case BROWNPETERSON : type = "Brown Peterson"; break;
				case STROOPEFFECT  : type = "Stroof Effect"; break;
				case SIGNALDETECTION : type = "Signal Detection"; break;
				case ATTENTIONBLINK : type = "Attention Blink";break;
				case POSITIONERROR : type = "Position Error";break;
				default : type = "Unknown";
			}
		}
		return type;
	}

	public void generateTrials(){
		switch(this.experimentType){
			case BROWNPETERSON : generateBrownPetersonTrial() ;break;
			case STROOPEFFECT : generateStroopEffectTrial(); break;
			case SIGNALDETECTION : break;
			case ATTENTIONBLINK : generateAttentionBlinkTrial(); break;
			case POSITIONERROR : break;
		}
	}

	private void generateBrownPetersonTrial(){
		for(int i = 0; i < this.noOfTrial; i++){
			models.brownPeterson.Trial trial = models.brownPeterson.Trial.create(this);
            trial.save();
            trial.generateQuiz();
		}
	}
	private void generateStroopEffectTrial(){
		for(int i = 0; i < this.noOfTrial; i++){
			models.stroopEffect.Trial trial = models.stroopEffect.Trial.create(this);
            trial.save();
            trial.generateQuiz();
		}
	}

	private void generateAttentionBlinkTrial(){
		for(int i = 0; i < this.noOfTrial; i++){
			models.attentionBlink.Trial trial = models.attentionBlink.Trial.create(this);
			trial.save();
			trial.generateQuiz();
		}
	}

	@SuppressWarnings("unchecked")
	public static Finder<Long,ExperimentSchedule> find = new Finder(Long.class,ExperimentSchedule.class);
}