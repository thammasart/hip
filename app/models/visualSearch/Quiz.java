package models.visualSearch;

import com.fasterxml.jackson.annotation.JsonBackReference;
import play.db.ebean.Model;
import play.libs.Json;

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
@Table (name="visual_search_quiz")
public class Quiz extends Model{
    @Id
    public long id;
    public double positionXofTarget;
    public double positionYofTarget;
    public ShapeType target = ShapeType.CIRCLE_BLUE;
    public int squareBlue;
    public int squareGreen;
    public int squareRed;
    public int circleGreen;
    public int circleRed;
    public int circleBlue;
    public FrameSize frameSize;
    @OneToOne
    public Question question;
    @OneToMany(cascade=CascadeType.REMOVE)
    private List<Answer> answers = new ArrayList<Answer>();
    @OneToOne(cascade = CascadeType.ALL)
    @JsonBackReference(value="trial")
    public Trial trial;

    public static void exportToFile(Workbook wb){

        try{
            int headerRowIndex = 0;

            int col = 0;
            //wb = new HSSFWorkbook();
            Sheet userSheet = wb.createSheet("Quiz");

            Row headerRow = userSheet.createRow(headerRowIndex++);
            Cell tableName = headerRow.createCell(0);
            tableName.setCellValue("Visual Search Quiz");

            headerRow = userSheet.createRow(headerRowIndex++);

            Cell someCell = headerRow.createCell(col++);
            someCell.setCellValue("ID");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("positionX of Target");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("positionY of Target");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Target");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Square Red");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Square Green");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Square Blue");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Circle Red");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Circle Green");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Circle Blue");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Frame Size");

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
                someCell.setCellValue(temp.positionXofTarget);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.positionYofTarget);

                String type = "";
                if (temp.target == ShapeType.SQAURE_RED)
                    type = "Square Red";
                else if (temp.target == ShapeType.SQAURE_GREEN)
                    type = "Square Green";
                else if (temp.target == ShapeType.SQAURE_BLUE)
                    type = "Square Blue";
                else if (temp.target == ShapeType.CIRCLE_RED)
                    type = "Circle Red";
                else if (temp.target == ShapeType.CIRCLE_GREEN)
                    type = "Circle Green";
                else if (temp.target == ShapeType.CIRCLE_BLUE)
                    type = "Circle Blue";
                someCell = dataRow.createCell(col++);
                someCell.setCellValue(type);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.squareRed);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.squareGreen);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.squareBlue);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.circleRed);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.circleGreen);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.circleBlue);

                type = "";
                if (temp.frameSize == FrameSize.SMALLER)
                    type = "Smaller";
                else if (temp.frameSize == FrameSize.SMALL)
                    type = "Small";
                else if (temp.frameSize == FrameSize.MEDIUM)
                    type = "Medium";
                else if (temp.frameSize == FrameSize.BIG)
                    type = "Big";
                else if (temp.frameSize == FrameSize.EXTRA)
                    type = "Extra";
                someCell = dataRow.createCell(col++);
                someCell.setCellValue(type);

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

    public Quiz(){}
    public Quiz(Trial trial){
        this.trial = trial;
    }

    public List<Answer> findAnswers(){
        return answers;
    }

    public static List<Quiz> findInvolving(Trial trial){
        return Quiz.find.where().eq("trial", trial).findList();
    }

    public String toJson(){
        return Json.stringify(Json.toJson(this));
    }

    public static String toJsonArray(List<Quiz> quizzes){
        String result = "[";
        for(int i=0;i< quizzes.size();i++){
            result += quizzes.get(i).toJson();
            if(i < quizzes.size() - 1){
                result += ",";
            }
        }
        result += "]";
        return result;

    }

    @SuppressWarnings("unchecked")
    public static Finder<Long, Quiz> find = new Finder(Long.class, Quiz.class);

    public static Quiz create(Trial trial) {
        Quiz quiz = new Quiz(trial);
        quiz.frameSize = FrameSize.MEDIUM;
        quiz.circleGreen = 5;
        quiz.circleRed = 5;
        quiz.squareBlue = 5;
        quiz.squareGreen = 5;
        quiz.squareRed = 5;
        Question question = new Question();
        question.save();
        quiz.question = question;
        return quiz;
    }

}
