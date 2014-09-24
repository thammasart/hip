package models.brownPeterson;
import models.*;
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
@Table (name="brown_peterson_answer")
public class Answer extends Model implements AnswerResult{
	@Id
	public long id;
	@Column(length=20)
	public String firstWord;
	@Column(length=20)
	public String secondWord;
	@Column(length=20)
	public String thirdWord;
	public double usedTime;
	@Column(length=20)
	public String countdownResult;
	public boolean isCorrect;

	@ManyToOne
	public User user;
	@ManyToOne
	public Quiz quiz;

	public Answer(String firstWord,String secondWord,String thirdWord,double usedTime,String countdownResult, User user, Quiz quiz){
		this.firstWord = firstWord;
		this.secondWord = secondWord;
		this.thirdWord = thirdWord;
		this.usedTime = usedTime;
		this.countdownResult = countdownResult;
		this.user = user;
		this.quiz = quiz;

		Question q = quiz.question;
		if(!q.firstWord.equalsIgnoreCase(firstWord) && !q.firstWord.equalsIgnoreCase(secondWord) && !q.firstWord.equalsIgnoreCase(thirdWord)){
			this.isCorrect = false;
		}
		else if(!q.secondWord.equalsIgnoreCase(firstWord) && !q.secondWord.equalsIgnoreCase(secondWord) && !q.secondWord.equalsIgnoreCase(thirdWord)){
			this.isCorrect = false;
		}
		else if(!q.thirdWord.equalsIgnoreCase(firstWord) && !q.thirdWord.equalsIgnoreCase(secondWord) && !q.thirdWord.equalsIgnoreCase(thirdWord)){
			this.isCorrect = false;
		}
		else{
			this.isCorrect = true;
		}
	}

	public static List<Answer> findInvolving(User user,List<Quiz> quizzes){
		List<Answer> answers = new ArrayList<Answer>();
		for(Quiz quiz:quizzes){
			answers.add(find.where().eq("user" ,user).eq("quiz",quiz).findUnique());
		}
		return answers;
	}

	public static List<Answer> findInvolving(ExperimentSchedule exp){
		List<Answer> answers = new ArrayList<Answer>();
		for(Trial trial : models.brownPeterson.Trial.findInvolving(exp)){
			for(Quiz quiz : trial.quizzes){
				answers.addAll(quiz.findAnswer());
			}
		}
		return answers;
	}
        
        public static double calculateAverageScore(){
                List<Answer> answers = Answer.find.all();
                double sum = Answer.calculateTotalScore(answers);
                return sum/(answers.size());
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
		for(Answer answer : answers){
			if(answer.isCorrect)
				totalScore++;
		}
		return totalScore;
	}

    public static void exportToFile(Workbook wb){

        try{
            int headerRowIndex = 0;
            int col = 0;
            //wb = new HSSFWorkbook();
            Sheet userSheet = wb.createSheet("Answer");

            Row headerRow = userSheet.createRow(headerRowIndex++);

            Cell tableName = headerRow.createCell(0);
            tableName.setCellValue("Brown Peterson Answer");

            headerRow = userSheet.createRow(headerRowIndex++);
            Cell idHeaderCell = headerRow.createCell(col++);
            idHeaderCell.setCellValue("ID");

            Cell firstCell = headerRow.createCell(col++);
            firstCell.setCellValue("first word");

            Cell secondCell = headerRow.createCell(col++);
            secondCell.setCellValue("second word");

            Cell thirdCell = headerRow.createCell(col++);
            thirdCell.setCellValue("third word");

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

                Cell dataFirst = dataRow.createCell(col++);
                dataFirst.setCellValue(temp.firstWord);

                Cell dataSecond = dataRow.createCell(col++);
                dataSecond.setCellValue(temp.secondWord);

                Cell dataThird = dataRow.createCell(col++);
                dataThird.setCellValue(temp.thirdWord);

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

    public ExperimentSchedule getExperimentScheduleObject(){
        return this.quiz.trial.schedule;
    }
    public long getTrialIdLong(){
        return this.quiz.trial.id;
    }
    public String getParameterType(){
        String type = "";
        if (this.quiz.trial.trigramType.equals(Trial.NON_SENSE))
            type = type + "Non-Sense ";
        else if(this.quiz.trial.trigramType.equals(Trial.WORD))
            type = type + "Word ";

        if (this.quiz.trial.trigramLanguage.equals(Trial.ENGLISH))
            type = type + "English";
        else if(this.quiz.trial.trigramLanguage.equals(Trial.THAI))
            type = type + "Thai";

        return type;
    }
    public User getUserObject(){
        return this.user;
    }
    public long getQuestionIdLong(){
        return this.quiz.question.id.longValue();
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
	public static Finder<Long, Answer> find = new Finder(Long.class, Answer.class);

}
