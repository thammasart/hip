package models.stroopEffect;

import models.AnswerResult;
import models.ExperimentSchedule;
import models.TimeLog;
import models.User;

import play.db.ebean.Model;
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
@Table (name="stroop_answer")
public class Answer extends Model implements AnswerResult{

    @Id
    public long id;
    @Column(length=20)
    public String answer;
    public double usedTime;
    public boolean isCorrect;

    @ManyToOne
    public User user;
    @ManyToOne
    public Quiz quiz;

    public static void exportToFile(Workbook wb){

        try{
            int headerRowIndex = 0;
            int col = 0;
            //wb = new HSSFWorkbook();
            Sheet userSheet = wb.createSheet("Answer");

            Row headerRow = userSheet.createRow(headerRowIndex++);

            Cell someCell = headerRow.createCell(0);
            someCell.setCellValue("Stroop Effect Answer");

            headerRow = userSheet.createRow(headerRowIndex++);

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("ID");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Answer");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("IsCorrect");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Used time");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("UserName");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Quiz Id");

            List<Answer> tempList = find.all();

            int listSize = tempList.size();
            for(int row=headerRowIndex;row-headerRowIndex<listSize;row++){
                Answer temp = tempList.get(row-headerRowIndex);
                col = 0;
                Row dataRow = userSheet.createRow(row);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.id);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.answer);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.isCorrect);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.usedTime);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.user.username);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.quiz.id);

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

    public Answer(String answer,double usedTime){
        this.answer = answer;
        this.usedTime = usedTime;
    }

    public static Answer create(String answer, double usedTime,User user, Quiz quiz){
        Answer newAnswer = new Answer(answer,usedTime);
        newAnswer.user = user;
        newAnswer.quiz = quiz;
        newAnswer.isCorrect = quiz.question.inkColor.equalsIgnoreCase(answer);
        return newAnswer;
    }

    public static List<Answer> findInvolving(User user, List<Quiz> quizzes){
        List<Answer> answers = new ArrayList<Answer>();
        for(Quiz quiz:quizzes){
            answers.add(find.where().eq("user" ,user).eq("quiz",quiz).findUnique());
        }
        return answers;
    }

    public static double calculateTotalUsedTime(List<Answer> answers) {
        double totalUsedTime = 0.0;
        for(Answer ans : answers) {
            totalUsedTime += ans.usedTime;
        }
        return totalUsedTime;
    }

    public static int calculateTotalScore(List<Answer> answers) {
        int totalScore = 0;
        for(Answer ans : answers) {
            if(ans.isCorrect) {
                totalScore++;
            }
        }
        return totalScore;
    }
    public ExperimentSchedule getExperimentScheduleObject(){
        return this.quiz.trial.schedule;
    }
    public long getTrialIdLong(){
        return this.quiz.trial.id;
    }
    public String getParameterType(){
        String type = "";
        if (this.quiz.question.questionType == QuestionType.ENGLISH)
            return "English ";
        else if (this.quiz.question.questionType == QuestionType.THAI)
            return "Thai ";
        else if (this.quiz.question.questionType == QuestionType.SHAPE)
            return "Shape ";
        if (this.quiz.question.isMatch()){
            type = type + "Match";
        }
        else type = type + "Not-Match";

        return type;
    }
    public User getUserObject(){
        return this.user;
    }
    public long getQuestionIdLong(){
        return this.quiz.question.id;
    }
    public long getQuizIdLong(){
        return this.quiz.id;
    }
    public String getIsCorrectString(){
        return String.valueOf(this.isCorrect);
    }
    public double getUsedTimeDouble(){
        return this.usedTime;
    }
    public TimeLog getTimeLogObject(){
        return TimeLog.findByUserAndTrialId(this.user,new Long(this.quiz.trial.id),this.quiz.trial.schedule);
    }

    public String getStartTimeToString(){
        TimeLog timelog = getTimeLogObject();
        if(timelog == null){
            return "-- null --";
        }
        else if(timelog.startTime == null){
            return "-- null --";
        }
        else{
            return timelog.startTime.getDate()+"/"+timelog.startTime.getMonth()+"/"+timelog.startTime.getMonth();
        }
    }

    public String getEndTimeToString(){
        TimeLog timelog = getTimeLogObject();
        if(timelog == null){
            return "-- null --";
        }
        else if(timelog.endTime == null){
            return "-- null --";
        }
        else{
            return timelog.endTime.getDate()+"/"+timelog.endTime.getMonth()+"/"+timelog.endTime.getMonth();
        }
    }

    @SuppressWarnings("unchecked")
    public static Finder<Long,Answer> find = new Finder(Long.class,Answer.class);

}
