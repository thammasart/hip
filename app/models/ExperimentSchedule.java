package models;

import play.db.ebean.*;
import javax.persistence.*;
import java.util.Date;
import play.data.format.Formats;

import java.util.List;
import java.util.ArrayList;
import static play.data.validation.Constraints.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import play.db.DB;
import play.mvc.Controller;
import play.mvc.Result;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
    public ScheduleStatus status = ScheduleStatus.CLOSE;

    @OneToMany(mappedBy="schedule", cascade=CascadeType.REMOVE)
    private List<models.attentionBlink.Trial> attentionTrials = new ArrayList<models.attentionBlink.Trial>();

    @OneToMany(mappedBy="schedule", cascade=CascadeType.REMOVE)
    private List<models.brownPeterson.Trial> browntrials = new ArrayList<models.brownPeterson.Trial>();

    @OneToMany(mappedBy="schedule", cascade=CascadeType.REMOVE)
    private List<models.changeBlindness.Trial> changeBlindnesstrials = new ArrayList<models.changeBlindness.Trial>();

    @OneToMany(mappedBy="schedule", cascade=CascadeType.REMOVE)
    private List<models.garnerInterference.Trial> garnertrials = new ArrayList<models.garnerInterference.Trial>();

    @OneToMany(mappedBy="schedule", cascade=CascadeType.REMOVE)
    private List<models.magicNumber7.Trial> magic7trials = new ArrayList<models.magicNumber7.Trial>();

    @OneToMany(mappedBy="schedule", cascade=CascadeType.REMOVE)
    private List<models.mullerLayer.Trial> mullertrials = new ArrayList<models.mullerLayer.Trial>();

    @OneToMany(mappedBy="schedule", cascade=CascadeType.REMOVE)
    private List<models.positionError.Trial> positionErrortrials = new ArrayList<models.positionError.Trial>();

    @OneToMany(mappedBy="schedule", cascade=CascadeType.REMOVE)
    private List<models.signalDetection.Trial> signaltrials = new ArrayList<models.signalDetection.Trial>();

    @OneToMany(mappedBy="schedule", cascade=CascadeType.REMOVE)
    private List<models.simonEffect.Trial> simontrials = new ArrayList<models.simonEffect.Trial>();

    @OneToMany(mappedBy="schedule", cascade=CascadeType.REMOVE)
    private List<models.sternbergSearch.Trial> sternbergSearchtrials = new ArrayList<models.sternbergSearch.Trial>();

    @OneToMany(mappedBy="schedule", cascade=CascadeType.REMOVE)
    private List<models.stroopEffect.Trial> stroopTrials = new ArrayList<models.stroopEffect.Trial>();

    @OneToMany(mappedBy="schedule", cascade=CascadeType.REMOVE)
    private List<models.visualSearch.Trial> visualSearchTrials = new ArrayList<models.visualSearch.Trial>();

    @OneToMany(mappedBy="exp", cascade=CascadeType.ALL)
    private List<TimeLog> timelog = new ArrayList<TimeLog>();


    public static void exportToFile(Workbook wb){

        try{
            int headerRowIndex = 0;
            int col = 0;
            //wb = new HSSFWorkbook();
            Sheet userSheet = wb.createSheet("ExperimentSchedule");

            Row headerRow = userSheet.createRow(headerRowIndex++);

            Cell someCell = headerRow.createCell(0);
            someCell.setCellValue("ExperimentSchedule");

            headerRow = userSheet.createRow(headerRowIndex++);

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("ID");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Name");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Start Date(dd/MM/yyyy HH:mm:ss)");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Expire Date(dd/MM/yyyy HH:mm:ss)");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Experiment Type");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("ExperimentSchedule Status");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Trial Ids");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("TimeLog Ids");

            List<ExperimentSchedule> tempList = find.all();

            int listSize = tempList.size();
            for(int row=headerRowIndex;row-headerRowIndex<listSize;row++){
                ExperimentSchedule temp = tempList.get(row-headerRowIndex);
                col = 0;
                Row dataRow = userSheet.createRow(row);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.id);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.name);

                DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                someCell = dataRow.createCell(col++);
                someCell.setCellValue(df.format(temp.startDate));

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(df.format(temp.expireDate));

                String type = "";
                if (temp.experimentType == ExperimentType.ATTENTIONBLINK)
                    type = "Attention Blink";
                else if (temp.experimentType == ExperimentType.BROWNPETERSON)
                    type = "Brown Peterson";
                else if (temp.experimentType == ExperimentType.CHANGEBLINDNESS)
                    type = "Change Blindness";
                else if (temp.experimentType == ExperimentType.GARNERINTERFERENCE)
                    type = "Garnener Interference";
                else if (temp.experimentType == ExperimentType.MAGICNUMBER7)
                    type = "Magic Number 7";
                else if (temp.experimentType == ExperimentType.MULLERLAYER)
                    type = "Muller Layer";
                else if (temp.experimentType == ExperimentType.POSITIONERROR)
                    type = "Position Error";
                else if (temp.experimentType == ExperimentType.SIGNALDETECTION)
                    type = "Signal Detection";
                else if (temp.experimentType == ExperimentType.SIMONEFFECT)
                    type = "Simon Effect";
                else if (temp.experimentType == ExperimentType.STERNBERGSEARCH)
                    type = "Strenberg Search";
                else if (temp.experimentType == ExperimentType.STROOPEFFECT)
                    type = "Stroop Effect";
                else if (temp.experimentType == ExperimentType.VISUALSEARCH)
                    type = "Visual Search";
                someCell = dataRow.createCell(col++);
                someCell.setCellValue(type);

                type = "";
                if (temp.status == ScheduleStatus.OPEN)
                    type = "Open";
                else if (temp.status == ScheduleStatus.CLOSE)
                    type = "Close";
                else if (temp.status == ScheduleStatus.DISABLED)
                    type = "Disabled";
                someCell = dataRow.createCell(col++);
                someCell.setCellValue(type);

                String trial_Id = "";
                someCell = dataRow.createCell(col++);
                if (temp.attentionTrials.size() > 0){
                    trial_Id = trial_Id + "Attention Blink: ";
                    int subListSize = temp.attentionTrials.size();
                    for (int i=0;i<subListSize;i++){
                        if (i < subListSize-1)
                            trial_Id = trial_Id + String.valueOf( temp.attentionTrials.get(i).id) + ",";
                        else
                            trial_Id = trial_Id + String.valueOf( temp.attentionTrials.get(i).id);
                    }
                }
                if (temp.browntrials.size() > 0){
                    trial_Id = trial_Id + "Brown Peterson: ";
                    int subListSize = temp.browntrials.size();
                    for (int i=0;i<subListSize;i++){
                        if (i < subListSize-1)
                            trial_Id = trial_Id + String.valueOf( temp.browntrials.get(i).id) + ",";
                        else
                            trial_Id = trial_Id + String.valueOf( temp.browntrials.get(i).id);
                    }
                }
                if (temp.changeBlindnesstrials.size() > 0){
                    trial_Id = trial_Id + "Change Blindness: ";
                    int subListSize = temp.changeBlindnesstrials.size();
                    for (int i=0;i<subListSize;i++){
                        if (i < subListSize-1)
                            trial_Id = trial_Id + String.valueOf( temp.changeBlindnesstrials.get(i).id) + ",";
                        else
                            trial_Id = trial_Id + String.valueOf( temp.changeBlindnesstrials.get(i).id);
                    }
                }
                if (temp.garnertrials.size() > 0){
                    trial_Id = trial_Id + "Garner Interference: ";
                    int subListSize = temp.garnertrials.size();
                    for (int i=0;i<subListSize;i++){
                        if (i < subListSize-1)
                            trial_Id = trial_Id + String.valueOf( temp.garnertrials.get(i).id) + ",";
                        else
                            trial_Id = trial_Id + String.valueOf( temp.garnertrials.get(i).id);
                    }
                }
                if (temp.magic7trials.size() > 0){
                    trial_Id = trial_Id + "Magic Number 7: ";
                    int subListSize = temp.magic7trials.size();
                    for (int i=0;i<subListSize;i++){
                        if (i < subListSize-1)
                            trial_Id = trial_Id + String.valueOf( temp.magic7trials.get(i).id) + ",";
                        else
                            trial_Id = trial_Id + String.valueOf( temp.magic7trials.get(i).id);
                    }
                }
                if (temp.mullertrials.size() > 0){
                    trial_Id = trial_Id + "Muller Layer: ";
                    int subListSize = temp.mullertrials.size();
                    for (int i=0;i<subListSize;i++){
                        if (i < subListSize-1)
                            trial_Id = trial_Id + String.valueOf( temp.mullertrials.get(i).id) + ",";
                        else
                            trial_Id = trial_Id + String.valueOf( temp.mullertrials.get(i).id);
                    }
                }
                if (temp.positionErrortrials.size() > 0){
                    trial_Id = trial_Id + "Position Error: ";
                    int subListSize = temp.positionErrortrials.size();
                    for (int i=0;i<subListSize;i++){
                        if (i < subListSize-1)
                            trial_Id = trial_Id + String.valueOf( temp.positionErrortrials.get(i).id) + ",";
                        else
                            trial_Id = trial_Id + String.valueOf( temp.positionErrortrials.get(i).id);
                    }
                }
                if (temp.signaltrials.size() > 0){
                    trial_Id = trial_Id + "Signal Detection: ";
                    int subListSize = temp.signaltrials.size();
                    for (int i=0;i<subListSize;i++){
                        if (i < subListSize-1)
                            trial_Id = trial_Id + String.valueOf( temp.signaltrials.get(i).id) + ",";
                        else
                            trial_Id = trial_Id + String.valueOf( temp.signaltrials.get(i).id);
                    }
                }
                if (temp.simontrials.size() > 0){
                    trial_Id = trial_Id + "Simon Effect: ";
                    int subListSize = temp.simontrials.size();
                    for (int i=0;i<subListSize;i++){
                        if (i < subListSize-1)
                            trial_Id = trial_Id + String.valueOf( temp.simontrials.get(i).id) + ",";
                        else
                            trial_Id = trial_Id + String.valueOf( temp.simontrials.get(i).id);
                    }
                }
                if (temp.sternbergSearchtrials.size() > 0){
                    trial_Id = trial_Id + "Strenberg Search: ";
                    int subListSize = temp.sternbergSearchtrials.size();
                    for (int i=0;i<subListSize;i++){
                        if (i < subListSize-1)
                            trial_Id = trial_Id + String.valueOf( temp.sternbergSearchtrials.get(i).id) + ",";
                        else
                            trial_Id = trial_Id + String.valueOf( temp.sternbergSearchtrials.get(i).id);
                    }
                }
                if (temp.stroopTrials.size() > 0){
                    trial_Id = trial_Id + "Stroop Effect: ";
                    int subListSize = temp.stroopTrials.size();
                    for (int i=0;i<subListSize;i++){
                        if (i < subListSize-1)
                            trial_Id = trial_Id + String.valueOf( temp.stroopTrials.get(i).id) + ",";
                        else
                            trial_Id = trial_Id + String.valueOf( temp.stroopTrials.get(i).id);
                    }
                }
                if (temp.visualSearchTrials.size() > 0){
                    trial_Id = trial_Id + "Visual Search: ";
                    int subListSize = temp.visualSearchTrials.size();
                    for (int i=0;i<subListSize;i++){
                        if (i < subListSize-1)
                            trial_Id = trial_Id + String.valueOf( temp.visualSearchTrials.get(i).id) + ",";
                        else
                            trial_Id = trial_Id + String.valueOf( temp.visualSearchTrials.get(i).id);
                    }
                }
                someCell.setCellValue(trial_Id);

                String timeLog_Id = "";
                int subListSize2 = temp.timelog.size();
                for (int i=0;i<subListSize2;i++){
                    if (i < subListSize2-1)
                        trial_Id = trial_Id + String.valueOf( temp.timelog.get(i).id) + ",";
                    else
                        trial_Id = trial_Id + String.valueOf( temp.timelog.get(i).id);
                }

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(timeLog_Id);

            }

            //File file = new File("brown_peterson_user.xls");
            //FileOutputStream out = new FileOutputStream(file);
            //wb.write(out);
            //out.close();
            //return file;

        } catch (Exception e) {
            e.printStackTrace();
            //return null;
        }
    }

	public ExperimentSchedule(String name, int noOfTrial, Date startDate, Date expireDate, ExperimentType experimentType) {
		this.name = name;
		this.noOfTrial = noOfTrial;
		this.startDate = startDate;
		this.expireDate = expireDate;
		this.experimentType = experimentType;
	}

	public static List<ExperimentSchedule> findAllWorkingExperiments() {
		List<ExperimentSchedule> experiments = find.where().betweenProperties("startDate", "expireDate", new Date()).findList();
		return experiments;
	}

    @Override
    public void save(){
        this.startDate.setHours(0);
        this.startDate.setMinutes(0);
        this.startDate.setSeconds(0);
        this.expireDate.setHours(23);
        this.expireDate.setMinutes(59);
        this.expireDate.setSeconds(59);
        super.save();
    }

	public static List<ExperimentSchedule> findWorkingExperimentsByType(ExperimentType expType) {
		List<ExperimentSchedule> allWorkingExp = findAllWorkingExperiments();
		List<ExperimentSchedule> exps = new ArrayList<ExperimentSchedule>();
		for(ExperimentSchedule exp : allWorkingExp) {
			if(exp.experimentType == expType) {
				exps.add(exp);
			}
		}
		return exps;
	}

	public static List<ExperimentSchedule> findWorkingExperimentsByType(String expType){
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
            expList = findWorkingExperimentsByType(ExperimentType.BROWNPETERSON);
        }
        else if (expType.equals(STROOPEFFECT)){
            expList = findWorkingExperimentsByType(ExperimentType.STROOPEFFECT);
        }
        else if (expType.equals(SIGNALDETECTION)){
            expList = findWorkingExperimentsByType(ExperimentType.SIGNALDETECTION);
        }
        else if (expType.equals(ATTENTIONBLINK)){
            expList = findWorkingExperimentsByType(ExperimentType.ATTENTIONBLINK);
        }
        else if (expType.equals(POSITIONERROR)){
            expList = findWorkingExperimentsByType(ExperimentType.POSITIONERROR);
        }
        else if (expType.equals(STERNBERGSEARCH)){
            expList = findWorkingExperimentsByType(ExperimentType.STERNBERGSEARCH);
        }
        else if (expType.equals(MAGICNUMBER7)){
            expList = findWorkingExperimentsByType(ExperimentType.MAGICNUMBER7);
        }
        else if (expType.equals(SIMONEFFECT)){
            expList = findWorkingExperimentsByType(ExperimentType.SIMONEFFECT);
        }
        else if (expType.equals(MULLERLAYER)){
            expList = findWorkingExperimentsByType(ExperimentType.MULLERLAYER);
        }
        else if (expType.equals(GARNERINTERFERENCE)){
            expList = findWorkingExperimentsByType(ExperimentType.GARNERINTERFERENCE);
        }
        else if (expType.equals(VISUALSEARCH)){
            expList = findWorkingExperimentsByType(ExperimentType.VISUALSEARCH);
        }
        else if (expType.equals(CHANGEBLINDNESS)){
            expList = findWorkingExperimentsByType(ExperimentType.CHANGEBLINDNESS);
        }
        return expList;
	}

	public String findExperimentTypeToString(){
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
            case VISUALSEARCH: generateVisualSearchTrial(); break;
            case CHANGEBLINDNESS:generateChangeBlindnessTrial();break;
		}
	}

    private void generateChangeBlindnessTrial() {
        for(int i=0; i < this.noOfTrial; i++){
            models.changeBlindness.Trial trial = models.changeBlindness.Trial.create(this);
            trial.save();
            trial.generateQuiz();
        }
    }


    private void generateGarnerInterferenceTrial() {
        for(int i=0; i < this.noOfTrial; i++){
            models.garnerInterference.Trial trial = models.garnerInterference.Trial.create(this);
            trial.save();
            trial.generateQuiz();
        }
    }
    private void generateVisualSearchTrial() {
        for(int i=0;i<this.noOfTrial;i++){
            models.visualSearch.Trial trial = models.visualSearch.Trial.create(this);
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

    public void deleteExpAndRelative(){
        this.delete();
    }


	@SuppressWarnings("unchecked")
	public static Finder<Long,ExperimentSchedule> find = new Finder(Long.class,ExperimentSchedule.class);
}
