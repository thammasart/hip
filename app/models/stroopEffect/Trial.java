package models.stroopEffect;

import play.db.ebean.Model;
import javax.persistence.*;

import models.TimeLog;
import models.ExperimentSchedule;

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
@Table (name="stroop_trial")
public class Trial extends Model{
    @Id
    public long id;
    @Column(nullable=true, length=2)
    public long appearTime = 0L;
    @Column(nullable=true, length=20)
    public QuestionType questionType = QuestionType.ENGLISH;
    public double totalScore = 0;
    public double totalUsedTime = 0;
    public int totalUser = 0;
    
    public static final int TOTAL_QUESTION = 3;

    @ManyToOne
    public ExperimentSchedule schedule;

    @OneToMany(cascade=CascadeType.REMOVE)
    @JsonManagedReference
    public List<Quiz> quizzes = new ArrayList<Quiz>();

    public static void exportToFile(Workbook wb){

        try{
            int headerRowIndex = 0;

            int col = 0;

            //wb = new HSSFWorkbook();
            Sheet userSheet = wb.createSheet("Trial");

            Row headerRow = userSheet.createRow(headerRowIndex++);
            Cell tableName = headerRow.createCell(0);
            tableName.setCellValue("Stroop Effect Trial");

            headerRow = userSheet.createRow(headerRowIndex++);

            Cell someCell = headerRow.createCell(col++);
            someCell.setCellValue("ID");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Appear Time");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Question Type");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Experiment Schedule Id");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Quiz Ids");

            List<Trial> tempList = find.all();

            int listSize = tempList.size();
            for(int row=headerRowIndex;row-headerRowIndex<listSize;row++){

                col = 0;

                Trial temp = tempList.get(row-headerRowIndex);
                Row dataRow = userSheet.createRow(row);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.id);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.appearTime);

                String type = "";
                if (temp.questionType == QuestionType.ENGLISH)
                    type = "English";
                else if (temp.questionType == QuestionType.SHAPE)
                    type = "Shape";
                else if (temp.questionType == QuestionType.THAI)
                    type = "Thai";
                someCell = dataRow.createCell(col++);
                someCell.setCellValue(type);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.schedule.id);

                String quizzes_id = "";

                int subListSize = temp.quizzes.size();

                for (int i=0;i<subListSize;i++){
                    if (i < subListSize-1)
                        quizzes_id = quizzes_id + String.valueOf( temp.quizzes.get(i).id) + ",";
                    else
                        quizzes_id = quizzes_id + String.valueOf( temp.quizzes.get(i).id);
                }
                someCell = dataRow.createCell(col++);
                someCell.setCellValue(quizzes_id);

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

    public static Trial create(ExperimentSchedule experimentSchedule){
        Trial trial = new Trial();
        trial.schedule = experimentSchedule;
        return trial;
    }

    public Trial(){}

    public Trial(long appearTime,QuestionType type){
        this.appearTime = appearTime;
        this.questionType = type;
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
    
    public static Trial findById(long id){
        return find.byId(new Long(id));
    }

    public static List<Trial> findInvolving(ExperimentSchedule ex){
        return find.where().eq("schedule", ex).findList();
    }

    public static List<Trial> findAllTrial(int feature){

        if(feature == 1){ 
            return find.where().eq("question_type",QuestionType.THAI).findList();
        }else if(feature == 2 ){ 
            return find.where().eq("question_type",QuestionType.ENGLISH).findList();
        }else if(feature == 3 ){ 
            return find.where().eq("question_type",QuestionType.SHAPE).findList();
        }else{ 
            return find.all();
        }
    }

    public void toQuestionTYpe(String type){
        if(type.equals(QuestionType.ENGLISH.toString())){
            this.questionType = QuestionType.ENGLISH;
        }else if(type.equals(QuestionType.THAI.toString())){
            this.questionType = QuestionType.THAI;
        }else if(type.equals(QuestionType.SHAPE.toString())){
            this.questionType = QuestionType.SHAPE;
        }
        update();
    }

    public void randomNewQuestions(){
        List<Question> questions = Question.find.where().eq("questionType", questionType).findList();
        for(Quiz quiz : quizzes){
            quiz.randomToNewQuestion(questions);
        }
    }

    public void generateQuiz(){
        List<Question> questions = Question.find.where().eq("questionType", questionType).findList();
        for(int i = 0; i < TOTAL_QUESTION; i++){
            Quiz.create(this, Question.randomNewQuestion(questions)).save();
        }
    }

    @SuppressWarnings("unchecked")
    public static Model.Finder<Long,Trial> find = new Finder(Long.class, Trial.class);
}
