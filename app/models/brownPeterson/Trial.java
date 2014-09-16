package models.brownPeterson;

import models.*;
import models.TimeLog;
import models.TrialStatus;

import play.db.ebean.*;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonManagedReference;

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
@Table (name="brown_peterson_trial")
public class Trial extends Model {

	public static final String WORD = "word";
	public static final String NON_SENSE = "nonsense";
	public static final String ENGLISH = "english";
	public static final String THAI = "thai";
	public double totalScore = 0;
    public double totalUsedTime = 0;
    public int totalUser = 0;
    public TrialStatus status = TrialStatus.CLOSE;

	@Id
	public long id;
	@Column(nullable=false, length=20)
	public String trigramType = WORD;
	@Column(nullable=false, length=20)
	public String trigramLanguage = ENGLISH;

	public static final int TOTAL_QUESTION = 3;

	@ManyToOne
	public ExperimentSchedule schedule;

    @OneToMany(cascade=CascadeType.REMOVE)
    @JsonManagedReference
    public List<Quiz> quizzes = new ArrayList<Quiz>();
        
	public static Trial create(ExperimentSchedule experimentSchedule){
		Trial trial = new Trial();
		trial.schedule = experimentSchedule;
		return trial;
	}

    public Trial(){}

	public static Trial findById(int id) {
		return find.byId(new Long(id));
	}

	public static List<Trial> findInvolving(ExperimentSchedule ex){
		return find.where().eq("schedule", ex).findList();
	}

    public void updateResult(){
        this.totalScore = 0;
        this.totalUsedTime = 0;
        for(Quiz q:quizzes){
            this.totalScore += Answer.calculateTotalScore(q.findAnswer());
            this.totalUsedTime += Answer.calculateTotalUsedTime(q.findAnswer());
        }
        this.totalUser = TimeLog.calaulateTotalUserTakeExp(schedule,id);
        this.totalScore /=totalUser;
        this.totalUsedTime /=totalUser;
        this.update();
    }

	public int calculateAverageScore(){
		int totalScore = 0;
		for(Quiz quiz : this.quizzes){
			totalScore += Answer.calculateTotalScore(quiz.findAnswer());
		}
		//return totalScore/this.calculateTotalUser();
        return 1;
	}

	public void randomNewQuestions(){
		List<Question> questions 
			= Question.find.where().eq("trigramType", trigramType).eq("trigramLanguage", trigramLanguage).findList();
		for(Quiz quiz : quizzes){
			quiz.randomToNewQuestion(questions);
		}	
	}

	public void generateQuiz(){
		List<Question> questions 
			= Question.find.where().eq("trigramType", trigramType).eq("trigramLanguage", trigramLanguage).findList();
		for(int i = 0; i < TOTAL_QUESTION; i++){
			Quiz.create(Quiz.DEFAULT_INITCOUNTDOWN, 
				Quiz.DEFAULT_FLASHTIME, this, Question.randomNewQuestion(questions)).save();
		}
	}

/*
	public int calculateTotalUser(){
		return this.timeLogs.size();
	}
*/

    public static void exportToFile(Workbook wb){

        try{
            int headerRowIndex = 0;

            int col = 0;

            //wb = new HSSFWorkbook();
            Sheet userSheet = wb.createSheet("Trial");

            Row headerRow = userSheet.createRow(headerRowIndex++);
            Cell tableName = headerRow.createCell(0);
            tableName.setCellValue("Brown Peterson Trial");

            headerRow = userSheet.createRow(headerRowIndex++);

            Cell idHeaderCell = headerRow.createCell(col++);
            idHeaderCell.setCellValue("ID");

            Cell userCell = headerRow.createCell(col++);
            userCell.setCellValue("Trigram Type");

            Cell correctCell = headerRow.createCell(col++);
            correctCell.setCellValue("Trigram Language");

            Cell expCell = headerRow.createCell(col++);
            expCell.setCellValue("Experiment Schedule Id");

            Cell quizCell = headerRow.createCell(col++);
            quizCell.setCellValue("Quiz Ids");

            List<Trial> tempList = find.all();

            int listSize = tempList.size();
            for(int row=headerRowIndex;row-headerRowIndex<listSize;row++){

                col = 0;

                Trial temp = tempList.get(row-headerRowIndex);
                Row dataRow = userSheet.createRow(row);

                Cell dataId = dataRow.createCell(col++);
                dataId.setCellValue(temp.id);

                Cell data1 = dataRow.createCell(col++);
                data1.setCellValue(temp.trigramType);

                Cell data2 = dataRow.createCell(col++);
                data2.setCellValue(temp.trigramLanguage);

                Cell data3 = dataRow.createCell(col++);
                data3.setCellValue(temp.schedule.id);

                String quizzes_id = "";

                int subListSize = temp.quizzes.size();

                for (int i=0;i<subListSize;i++){
                    if (i < subListSize-1)
                        quizzes_id = quizzes_id + String.valueOf( temp.quizzes.get(i).id) + ",";
                    else
                        quizzes_id = quizzes_id + String.valueOf( temp.quizzes.get(i).id);
                }
                Cell data4 = dataRow.createCell(col++);
                data4.setCellValue(quizzes_id);

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

	@SuppressWarnings("unchecked")
	public static Finder<Long, Trial> find = new Finder(Long.class, Trial.class);

}
