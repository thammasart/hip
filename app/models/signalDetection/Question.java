package models.signalDetection;

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
@Table (name="signal_detection_question")
public class Question extends Model{

    private static final String ENGLISH_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    @Id
    public long id;
    public char target;
    public char noise;

    @OneToOne(mappedBy = "question")
    private List<Quiz> quizzes = new ArrayList<Quiz>();

    public static void exportToFile(Workbook wb){

        try{
            int headerRowIndex = 0;
            int col = 0;
            //wb = new HSSFWorkbook();
            Sheet userSheet = wb.createSheet("Question");

            Row headerRow = userSheet.createRow(headerRowIndex++);
            Cell tableName = headerRow.createCell(0);
            tableName.setCellValue("Signal Detection Question");

            headerRow = userSheet.createRow(headerRowIndex++);

            Cell someCell = headerRow.createCell(col++);
            someCell.setCellValue("ID");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Target");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Noise");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Quiz Id");

            List<Question> tempList = find.all();

            int listSize = tempList.size();
            for(int row=headerRowIndex;row-headerRowIndex<listSize;row++){
                Question temp = tempList.get(row-headerRowIndex);

                col = 0;

                Row dataRow = userSheet.createRow(row);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.id);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.target);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.noise);


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

    public Question(){}

    public Question(char target,char noise){
        this.target = target;
        this.noise = noise;
    }

    public static Question create(){
        Random random = new Random();
        char target = ENGLISH_CASE.charAt(random.nextInt(ENGLISH_CASE.length()));
        char noise = ENGLISH_CASE.charAt(random.nextInt(ENGLISH_CASE.length()));
        return new Question(target, noise);
    }

    @SuppressWarnings("unchecked")
    public static Finder<Long, Question> find = new Finder(Long.class,Question.class);

}
