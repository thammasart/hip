package models.changeBlindness;

import models.AnswerResult;
import models.User;
import models.ExperimentSchedule;
import models.TimeLog;
import play.db.ebean.Model;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;
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
@Table (name="change_blindness_answer")
public class Answer extends Model implements AnswerResult{
    @Id
    public long id;
    public double usedTime;
    public double positionOfChangeX;
    public double positionOfChangeY;
    public boolean isCorrect;
    @ManyToOne
    public User user;
    @ManyToOne 
    public Quiz quiz;

    public Answer(User user, Quiz quiz){
    	this.user = user;
    	this.quiz = quiz;
    }

    public static List<Answer> findInvolving(User user, List<Quiz> quizzes){
        List<Answer> answers = new ArrayList<Answer>();
        for(Quiz quiz:quizzes){
            answers.add(find.where().eq("user" ,user).eq("quiz",quiz).findUnique());
        }
        return answers;
    }

    public static int calculateTotalScore(List<Answer> answers){
        int totalScore = 0;
        for(Answer ans : answers){
            if(ans.isCorrect) totalScore++;
        }
        return totalScore;
    }

    public static double calculateTotalUsedTime(List<Answer> answers){
        double totalUsedTime = 0;
        for(Answer ans : answers){
            totalUsedTime += ans.usedTime;
        }
        return totalUsedTime;
    }
    public ExperimentSchedule getExperimentScheduleObject(){
        return this.quiz.trial.schedule;
    }
    public long getTrialIdLong(){
        return this.quiz.trial.id;
    }
    public String getParameterType(){
        return "------";
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

    public static void exportToFile(Workbook wb){

        try{
            int headerRowIndex = 0;
            int col = 0;
            //wb = new HSSFWorkbook();
            Sheet userSheet = wb.createSheet("Answer");

            Row headerRow = userSheet.createRow(headerRowIndex++);

            Cell tableName = headerRow.createCell(0);
            tableName.setCellValue("Change Blindness Answer");

            headerRow = userSheet.createRow(headerRowIndex++);
            Cell idHeaderCell = headerRow.createCell(col++);
            idHeaderCell.setCellValue("ID");

            Cell answerCell = headerRow.createCell(col++);
            answerCell.setCellValue("Position Of Change X");

            Cell answerCell2 = headerRow.createCell(col++);
            answerCell2.setCellValue("Position Of Change Y");

            Cell correctCell = headerRow.createCell(col++);
            correctCell.setCellValue("IsCorrect");

            Cell timeCell = headerRow.createCell(col++);
            timeCell.setCellValue("Used time");

            Cell userCell = headerRow.createCell(col++);
            userCell.setCellValue("UserName");

            Cell quizCell = headerRow.createCell(col++);
            quizCell.setCellValue("Quiz Id");

            List<Answer> tempList = find.all();

            int listSize = tempList.size();
            for(int row=headerRowIndex;row-headerRowIndex<listSize;row++){
                Answer temp = tempList.get(row-headerRowIndex);
                col = 0;
                Row dataRow = userSheet.createRow(row);

                Cell dataId = dataRow.createCell(col++);
                dataId.setCellValue(temp.id);

                Cell dataAns = dataRow.createCell(col++);
                dataAns.setCellValue(temp.positionOfChangeX);

                Cell dataAns2 = dataRow.createCell(col++);
                dataAns2.setCellValue(temp.positionOfChangeY);

                Cell dataCorrect = dataRow.createCell(col++);
                dataCorrect.setCellValue(temp.isCorrect);

                Cell dataTime = dataRow.createCell(col++);
                dataTime.setCellValue(temp.usedTime);

                Cell userId = dataRow.createCell(col++);
                userId.setCellValue(temp.user.username);

                Cell quizId = dataRow.createCell(col++);
                quizId.setCellValue(temp.quiz.id);

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
    public static Finder<Long, Answer> find = new Finder(Long.class, Answer.class);

}
