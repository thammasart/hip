package models.brownPeterson;

import javax.persistence.*;
import play.db.ebean.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
@Table (name="brown_peterson_quiz")
public class Quiz extends Model{
	@Id
	public long id;
	@Column(length=3)
	public int initCountdown = 100;
	@Column(length=20)
	public int flashTime =5;

	@ManyToOne
    @JsonBackReference
	public Trial trial;

	@ManyToOne
	public Question question;

	@OneToMany(mappedBy="quiz",cascade=CascadeType.REMOVE)
	private List<Answer> answers = new ArrayList<Answer>();

	public static final int DEFAULT_FLASHTIME = 5;
	public static final int DEFAULT_INITCOUNTDOWN = 100;

	public Quiz(int initCountdown, int flashTime){
		this.initCountdown = initCountdown;
		this.flashTime = flashTime;
	}

	public static Quiz create(int initCountdown, int flashTime, Trial trial, Question question){
		Quiz quiz = new Quiz(initCountdown, flashTime);
		quiz.trial = trial;
		quiz.question = question;
		return quiz;
	}

	public static List<Quiz> findInvolving(Trial trial){
		return find.where().eq("trial", trial).findList();
	}
	
	public void randomToNewQuestion(){
		List<Question> questions 
			= Question.find.where().eq("trigramType", trial.trigramType).eq("trigramLanguage", trial.trigramLanguage).findList();
		Random random = new Random();
		int index = random.nextInt(questions.size());
		question = questions.get(index);
		update();
	}

    public Question generateNewQuestion(){
        List<Question> questions
                = Question.find.where().eq("trigramType", trial.trigramType).eq("trigramLanguage", trial.trigramLanguage).findList();
        Random random = new Random();
        int index = random.nextInt(questions.size());
        return questions.get(index);
    }

	public void randomToNewQuestion(List<Question> questions){
		Random random = new Random();
		int index = random.nextInt(questions.size());
		question = questions.get(index);
		update();
	}

    public List<Answer> findAnswer(){
        return answers;
    }

    public static void exportToFile(Workbook wb){

        try{
            int headerRowIndex = 0;

            int col = 0;
            //wb = new HSSFWorkbook();
            Sheet userSheet = wb.createSheet("Quiz");

            Row headerRow = userSheet.createRow(headerRowIndex++);
            Cell tableName = headerRow.createCell(0);
            tableName.setCellValue("Brown Peterson Quiz");

            headerRow = userSheet.createRow(headerRowIndex++);

            Cell idHeaderCell = headerRow.createCell(col++);
            idHeaderCell.setCellValue("ID");

            Cell initCoundownCell = headerRow.createCell(col++);
            initCoundownCell.setCellValue("Init Countdown");

            Cell flashTimeCell = headerRow.createCell(col++);
            flashTimeCell.setCellValue("Flash Time");

            Cell trialCell = headerRow.createCell(col++);
            trialCell.setCellValue("Trial Id");

            Cell questionCell = headerRow.createCell(col++);
            questionCell.setCellValue("Question Id");

            List<Quiz> tempList = find.all();

            int listSize = tempList.size();
            for(int row=headerRowIndex;row-headerRowIndex<listSize;row++){

                col = 0;

                Quiz temp = tempList.get(row-headerRowIndex);
                Row dataRow = userSheet.createRow(row);

                Cell dataId = dataRow.createCell(col++);
                dataId.setCellValue(temp.id);

                Cell data1 = dataRow.createCell(col++);
                data1.setCellValue(temp.initCountdown);

                Cell data2 = dataRow.createCell(col++);
                data2.setCellValue(temp.flashTime);

                Cell data3 = dataRow.createCell(col++);
                data3.setCellValue(temp.trial.id);

                Cell data4 = dataRow.createCell(col++);
                data4.setCellValue(temp.question.id);

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
	public static Finder<Long, Quiz> find = new Finder(Long.class, Quiz.class);
}
