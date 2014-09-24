package models.stroopEffect;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

import javax.persistence.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table (name="stroop_quiz")
public class Quiz extends Model{
    @Id
    public long id;

    @ManyToOne
    @JsonBackReference
    public Trial trial;
    @ManyToOne
    public Question question;
    @OneToMany(cascade=CascadeType.REMOVE, mappedBy = "quiz")
    private List<Answer> answers = new ArrayList<Answer>();

    public static void exportToFile(Workbook wb){

        try{
            int headerRowIndex = 0;

            int col = 0;
            //wb = new HSSFWorkbook();
            Sheet userSheet = wb.createSheet("Quiz");

            Row headerRow = userSheet.createRow(headerRowIndex++);
            Cell tableName = headerRow.createCell(0);
            tableName.setCellValue("Stroop Effect Quiz");

            headerRow = userSheet.createRow(headerRowIndex++);

            Cell someCell = headerRow.createCell(col++);
            someCell.setCellValue("ID");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Trial Id");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Question Id");

            List<Quiz> tempList = find.all();

            int listSize = tempList.size();
            for(int row=headerRowIndex;row-headerRowIndex<listSize;row++){

                col = 0;

                Quiz temp = tempList.get(row-headerRowIndex);
                Row dataRow = userSheet.createRow(row);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.id);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.trial.id);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.question.id);

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

    public List<Answer> findAnswers(){
        return answers;
    }

    public static Quiz create(Trial trial,Question question){
        Quiz quiz = new Quiz();
        quiz.trial = trial;
        quiz.question = question;
        return quiz;
    }

    public void switchRandomQuestion(){
        Random random = new Random();
        if(!question.isMatch()){
            List<Question> questions = Question.findAllMatchQuestion(this.trial.questionType);
            int index = random.nextInt(questions.size());
            question = questions.get(index);
        }else{
            List<Question> questions = Question.findAllNotMatchQuestion(this.trial.questionType);
            int index = random.nextInt(questions.size());
            question = questions.get(index);
        }
        update();
    }

    public void randomToNewQuestion(List<Question> questions){
        Random random = new Random();
        int index = random.nextInt(questions.size());
        question = questions.get(index);
        update();
    }

    @SuppressWarnings("unchecked")
    public static Finder<Long,Quiz> find = new Finder(Long.class,Quiz.class);

}
