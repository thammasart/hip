package models.attentionBlink;

import models.ExperimentSchedule;
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
@Table (name = "attention_blink_trial")
public class Trial extends Model{
	@Id
	public long id;
    public QuestionType questionType = QuestionType.ENGLISH;
    public int numberOfQuiz = 3;
    public int totalUser = 0;
    public double totalScore = 0;
    public double totalUsedTime = 0;
    public TrialStatus status = TrialStatus.CLOSE;

    @ManyToOne
    public ExperimentSchedule schedule;

    @OneToMany(cascade=CascadeType.REMOVE)
    @JsonManagedReference
    public List<Quiz> quizzes = new ArrayList<Quiz>();
        
    //Constructor
    public Trial(){}

	public Trial(QuestionType questionType) {
            this.questionType = questionType;
	}

    public void updateResult(){
        this.totalScore = 0;
        this.totalUsedTime = 0;
        for(Quiz q:quizzes){
            this.totalScore += Answer.calculateTotalScore(q.findAnswers());
            this.totalUsedTime += Answer.calculateTotalUsedTime(q.findAnswers());
        }
        this.totalUser = TimeLog.calaulateTotalUserTakeExp(schedule,id);
        this.totalScore /=totalUser;
        this.totalUsedTime /=totalUser;
        this.update();
    }

    public static Trial create(ExperimentSchedule ex){
        Trial trial = new Trial();
        trial.schedule = ex;
        return trial;
    }

    public static Trial create(ExperimentSchedule ex, QuestionType questionType,int numberOfQuiz) {
        Trial trial = new Trial(questionType);
        trial.schedule = ex; 
        trial.numberOfQuiz = numberOfQuiz;
        return trial;
    }

    public static List<Trial> findAllTrial(int feature){

        if(feature == 1){ 
            return find.where().eq("question_type",QuestionType.THAI).findList();
        }else if(feature == 2 ){ 
            return find.where().eq("question_type",QuestionType.ENGLISH).findList();
        }else if(feature == 3 ){ 
            return find.where().eq("question_type",QuestionType.NUMBER).findList();
        }else{ 
            return find.all();
        }
    }

    public static List<Trial> findInvolving(ExperimentSchedule ex){
        return find.where().eq("schedule", ex).findList();
    }

    public void generateNewQuestions(){
        for(Quiz quiz : quizzes){
            Question question = Question.generateQuestion(questionType, quiz.length, quiz.numberOfTarget, quiz.isCorrect);
            quiz.question = question;
            quiz.update();
        }
    }

    public void changeQuestionType(String questionType){
        if(questionType.equals("ENGLISH")){
            this.questionType = QuestionType.ENGLISH;
        }else if(questionType.equals("THAI")){
            this.questionType = QuestionType.THAI;
        }
        else if(questionType.equals("NUMBER")){
            this.questionType = QuestionType.NUMBER;
        }
    }
    public void generateQuiz(){
        for(int i = 0; i < this.numberOfQuiz; i++){
            Quiz.create(this).save();
        }
    }

    public static void exportToFile(Workbook wb){

        try{
            int headerRowIndex = 0;

            int col = 0;

            //wb = new HSSFWorkbook();
            Sheet userSheet = wb.createSheet("Trial");

            Row headerRow = userSheet.createRow(headerRowIndex++);
            Cell tableName = headerRow.createCell(0);
            tableName.setCellValue("Attention Blink Trial");

            headerRow = userSheet.createRow(headerRowIndex++);

            Cell idHeaderCell = headerRow.createCell(col++);
            idHeaderCell.setCellValue("ID");

            Cell userCell = headerRow.createCell(col++);
            userCell.setCellValue("Question Type");

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

                String type = "";
                if (temp.questionType == QuestionType.ENGLISH)
                    type = "English";
                else if (temp.questionType == QuestionType.THAI)
                    type = "Thai";
                else if (temp.questionType == QuestionType.NUMBER)
                    type = "Number";
                Cell data1 = dataRow.createCell(col++);
                data1.setCellValue(type);

                Cell data2 = dataRow.createCell(col++);
                data2.setCellValue(temp.schedule.id);

                String quizzes_id = "";

                int subListSize = temp.quizzes.size();

                for (int i=0;i<subListSize;i++){
                    if (i < subListSize-1)
                        quizzes_id = quizzes_id + String.valueOf( temp.quizzes.get(i).id) + ",";
                    else
                        quizzes_id = quizzes_id + String.valueOf( temp.quizzes.get(i).id);
                }
                Cell data3 = dataRow.createCell(col++);
                data3.setCellValue(quizzes_id);

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
