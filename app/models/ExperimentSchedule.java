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
	@Formats.DateTime(pattern="dd-MM-yyyy")
	public Date startDate;
	@Required
	@Column(nullable=false)
	@Formats.DateTime(pattern="dd-MM-yyyy")
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

//    @Override
    public void save(){
        this.startDate.setHours(0);
        this.startDate.setMinutes(0);
        this.startDate.setSeconds(0);
        this.expireDate.setHours(23);
        this.expireDate.setMinutes(59);
        this.expireDate.setSeconds(59);
        super.save();
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
        final String STERNBERGSEARCH = "STERNBERGSEARCH";
        final String MAGICNUMBER7 = "MAGICNUMBER7";
        final String SIMONEFFECT = "SIMONEFFECT";
        final String MULLERLAYER = "MULLERLAYER";
        final String GARNERINTERFERENCE = "GARNERINTERFERENCE";
        final String VISUALSEARCH = "VISUALSEARCH";
        final String CHANGEBLINDNESS = "CHANGEBLINDNESS";

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
        else if (expType.equals(STERNBERGSEARCH)){
            expList = getWorkingExperimentsByType(ExperimentType.STERNBERGSEARCH);
        }
        else if (expType.equals(MAGICNUMBER7)){
            expList = getWorkingExperimentsByType(ExperimentType.MAGICNUMBER7);
        }
        else if (expType.equals(SIMONEFFECT)){
            expList = getWorkingExperimentsByType(ExperimentType.SIMONEFFECT);
        }
        else if (expType.equals(MULLERLAYER)){
            expList = getWorkingExperimentsByType(ExperimentType.MULLERLAYER);
        }
        else if (expType.equals(GARNERINTERFERENCE)){
            expList = getWorkingExperimentsByType(ExperimentType.GARNERINTERFERENCE);
        }
        else if (expType.equals(VISUALSEARCH)){
            expList = getWorkingExperimentsByType(ExperimentType.VISUALSEARCH);
        }
        else if (expType.equals(CHANGEBLINDNESS)){
            expList = getWorkingExperimentsByType(ExperimentType.CHANGEBLINDNESS);
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
				case STERNBERGSEARCH : type = "Sternberg Search";break;
                case MAGICNUMBER7 : type = "Magic Number 7";break;
                case SIMONEFFECT : type = "Simon Effect";break;
                case MULLERLAYER: type = "Muller Layer";break;
                case GARNERINTERFERENCE : type = "Garner Interference";break;
                case VISUALSEARCH : type = "Visual Search";break;
                case CHANGEBLINDNESS : type = "Change Blindness";break;
				default : type = "Unknown";
			}
		}
		return type;
	}

	public void generateTrials(){
		switch(this.experimentType){
			case BROWNPETERSON : generateBrownPetersonTrial() ;break;
			case STROOPEFFECT : generateStroopEffectTrial(); break;
			case SIGNALDETECTION : generateSignalDetectionTrial(); break;
			case ATTENTIONBLINK : generateAttentionBlinkTrial(); break;
			case POSITIONERROR : generatePositionErrorTrial();break;
			case STERNBERGSEARCH : generateSternbergSearchTrial();break;
            case MAGICNUMBER7: generateMagicNumber7Trial(); break;
            case SIMONEFFECT : generateSimonEffectTrial(); break;
            case MULLERLAYER: generateMullerLayerTrial();break;
            case GARNERINTERFERENCE: generateGarnerInterferenceTrial();break;
            case VISUALSEARCH: break;
            case CHANGEBLINDNESS: break;
		}
	}

    private void generateGarnerInterferenceTrial() {
        for(int i=0; i < this.noOfTrial; i++){
            models.garnerInterference.Trial trial = models.garnerInterference.Trial.create(this);
            trial.save();
            trial.generateQuiz();
        }
    }

    private void generateMullerLayerTrial() {
        for(int i =0; i < this.noOfTrial; i++){
            models.mullerLayer.Trial trial = models.mullerLayer.Trial.create(this);
            trial.save();
            trial.generateQuiz();
        }
    }

    private void generateSimonEffectTrial() {
        for(int i=0; i < this.noOfTrial; i++){
            models.simonEffect.Trial trial = models.simonEffect.Trial.create(this);
            trial.save();
            trial.generateQuiz();
        }
    }

    private void generateSternbergSearchTrial() {
        for(int i = 0; i < this.noOfTrial; i++){
            models.sternbergSearch.Trial trial = models.sternbergSearch.Trial.create(this);
            trial.save();
            trial.generateQuiz();
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
    private void generateSignalDetectionTrial(){
		for(int i = 0; i < this.noOfTrial; i++){
			models.signalDetection.Trial trial = models.signalDetection.Trial.create(this);
			trial.save();
			trial.generateQuiz();
		}
	}

    private void generatePositionErrorTrial(){
        for (int i = 0; i < this.noOfTrial; i++){
            models.positionError.Trial trial = models.positionError.Trial.create(this);
            trial.save();
            trial.generateQuiz();
        }
    }

    private void generateMagicNumber7Trial(){
        for(int i = 0; i < this.noOfTrial; i++){
            models.magicNumber7.Trial trial = models.magicNumber7.Trial.create(this);
            trial.save();
            trial.generateQuiz();
        }
    }

	@SuppressWarnings("unchecked")
	public static Finder<Long,ExperimentSchedule> find = new Finder(Long.class,ExperimentSchedule.class);
}
