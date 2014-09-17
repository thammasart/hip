package models.mullerLayer;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import play.db.ebean.Model;
import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
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
@Table (name="muller_layer_question")
public class Question extends Model{
    @Id
    public long id;
    public LineType line1;
    public LineType line2;
    public LineType line3;
    public LineType line4;
    public LineType line5;
    @OneToMany(mappedBy = "question")
    private List<Quiz> quizzes = new ArrayList<Quiz>();

    public Question(){}

    @SuppressWarnings("unchecked")
    public static Finder<Long, Question> find = new Finder(Long.class,Question.class);

    public static void exportToFile(Workbook wb){

        try{
            int headerRowIndex = 0;
            int col = 0;
            //wb = new HSSFWorkbook();
            Sheet userSheet = wb.createSheet("Question");

            Row headerRow = userSheet.createRow(headerRowIndex++);
            Cell tableName = headerRow.createCell(0);
            tableName.setCellValue("Muller Layer Question");

            headerRow = userSheet.createRow(headerRowIndex++);

            Cell idHeaderCell = headerRow.createCell(col++);
            idHeaderCell.setCellValue("ID");

            Cell firstCell = headerRow.createCell(col++);
            firstCell.setCellValue("Line1");

            Cell secondCell = headerRow.createCell(col++);
            secondCell.setCellValue("Line2");

            Cell thirdCell = headerRow.createCell(col++);
            thirdCell.setCellValue("Line3");

            Cell forthCell = headerRow.createCell(col++);
            forthCell.setCellValue("Line4");

            Cell fifthCell = headerRow.createCell(col++);
            fifthCell.setCellValue("Line5");

            Cell quizCell = headerRow.createCell(col++);
            quizCell.setCellValue("Quiz Id");

            List<Question> tempList = find.all();

            int listSize = tempList.size();
            for(int row=headerRowIndex;row-headerRowIndex<listSize;row++){
                Question temp = tempList.get(row-headerRowIndex);

                col = 0;

                Row dataRow = userSheet.createRow(row);

                Cell dataId = dataRow.createCell(col++);
                dataId.setCellValue(temp.id);

                String type = "";
                if (temp.line1 == LineType.IN)
                    type = "In";
                else if (temp.line1 == LineType.LEFT)
                    type = "Left";
                else if (temp.line1 == LineType.NONE)
                    type = "None";
                else if (temp.line1 == LineType.RIGHT)
                    type = "Right";
                else if (temp.line1 == LineType.OUT)
                    type = "Out";
                Cell data1 = dataRow.createCell(col++);
                data1.setCellValue(type);

                type = "";
                if (temp.line2 == LineType.IN)
                    type = "In";
                else if (temp.line2 == LineType.LEFT)
                    type = "Left";
                else if (temp.line2 == LineType.NONE)
                    type = "None";
                else if (temp.line2 == LineType.RIGHT)
                    type = "Right";
                else if (temp.line2 == LineType.OUT)
                    type = "Out";
                Cell data2 = dataRow.createCell(col++);
                data2.setCellValue(type);

                type = "";
                if (temp.line3 == LineType.IN)
                    type = "In";
                else if (temp.line3 == LineType.LEFT)
                    type = "Left";
                else if (temp.line3 == LineType.NONE)
                    type = "None";
                else if (temp.line3 == LineType.RIGHT)
                    type = "Right";
                else if (temp.line3 == LineType.OUT)
                    type = "Out";
                Cell data3 = dataRow.createCell(col++);
                data3.setCellValue(type);

                type = "";
                if (temp.line4 == LineType.IN)
                    type = "In";
                else if (temp.line4 == LineType.LEFT)
                    type = "Left";
                else if (temp.line4 == LineType.NONE)
                    type = "None";
                else if (temp.line4 == LineType.RIGHT)
                    type = "Right";
                else if (temp.line4 == LineType.OUT)
                    type = "Out";
                Cell data4 = dataRow.createCell(col++);
                data4.setCellValue(type);

                type = "";
                if (temp.line5 == LineType.IN)
                    type = "In";
                else if (temp.line5 == LineType.LEFT)
                    type = "Left";
                else if (temp.line5 == LineType.NONE)
                    type = "None";
                else if (temp.line5 == LineType.RIGHT)
                    type = "Right";
                else if (temp.line5 == LineType.OUT)
                    type = "Out";
                Cell data5 = dataRow.createCell(col++);
                data5.setCellValue(type);

                String quizzes_id = "";

                int subListSize = temp.quizzes.size();

                for (int i=0;i<subListSize;i++){
                    if (i < subListSize-1)
                        quizzes_id = quizzes_id + String.valueOf( temp.quizzes.get(i).id) + ",";
                    else
                        quizzes_id = quizzes_id + String.valueOf( temp.quizzes.get(i).id);
                }
                Cell data6 = dataRow.createCell(col++);
                data6.setCellValue(quizzes_id);

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

    public static Question generateQuestion() {
        Question question = new Question();
        shuffleLine(question);

        question.save();
        return question;
    }

    public static void shuffleLine(Question question){
        List<LineType> lines = new ArrayList<LineType>();
        for(LineType line : LineType.values()){
            lines.add(line);
        }
        Collections.shuffle(lines);
        question.line1 = lines.get(0);
        question.line2 = lines.get(1);
        question.line3 = lines.get(2);
        question.line4 = lines.get(3);
        question.line5 = lines.get(4);
    }
}
