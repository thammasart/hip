package models.changeBlindness;

import com.fasterxml.jackson.annotation.JsonBackReference;
import play.db.ebean.Model;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
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
@Table (name="change_blindness_quiz")
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

    public Quiz(){}

    public Quiz(Trial trial, Question question){
    	this.trial = trial;
    	this.question = question;
    }

    public List<Answer> findAnswers(){
        return answers;
    }

    @SuppressWarnings("unchecked")
    public static Finder<Long, Quiz> find = new Finder(Long.class, Quiz.class);

    public static Quiz create(Trial trial, List<Question> questions) {
        Quiz quiz = new Quiz();
        quiz.trial = trial;
        Question question = questions.get(new Random().nextInt(questions.size()));
        quiz.question = question;
        questions.remove(question);
        if(questions.size() == 0)
            questions.addAll(Question.find.all());
        return quiz;
    }

    public static void exportToFile(Workbook wb){

        try{
            int headerRowIndex = 0;

            int col = 0;
            //wb = new HSSFWorkbook();
            Sheet userSheet = wb.createSheet("Quiz");

            Row headerRow = userSheet.createRow(headerRowIndex++);
            Cell tableName = headerRow.createCell(0);
            tableName.setCellValue("Change Blindness Quiz");

            headerRow = userSheet.createRow(headerRowIndex++);

            Cell idHeaderCell = headerRow.createCell(col++);
            idHeaderCell.setCellValue("ID");

            Cell trialId = headerRow.createCell(col++);
            trialId.setCellValue("Trial Id");

            Cell questionId = headerRow.createCell(col++);
            questionId.setCellValue("Question Id");

            Cell answerId = headerRow.createCell(col++);
            answerId.setCellValue("Answer Ids");

            List<Quiz> tempList = find.all();

            int listSize = tempList.size();
            for(int row=headerRowIndex;row-headerRowIndex<listSize;row++){

                col = 0;

                Quiz temp = tempList.get(row-headerRowIndex);
                Row dataRow = userSheet.createRow(row);

                Cell dataId = dataRow.createCell(col++);
                dataId.setCellValue(temp.id);

                Cell data1 = dataRow.createCell(col++);
                data1.setCellValue(temp.trial.id);

                Cell data2 = dataRow.createCell(col++);
                data2.setCellValue(temp.question.id);

                String answers_id = "";

                int subListSize = temp.answers.size();

                for (int i=0;i<subListSize;i++){
                    if (i < subListSize-1)
                        answers_id = answers_id + String.valueOf( temp.answers.get(i).id) + ",";
                    else
                        answers_id = answers_id + String.valueOf( temp.answers.get(i).id);
                }
                Cell data3 = dataRow.createCell(col++);
                data3.setCellValue(answers_id);

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
}
