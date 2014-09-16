package controllers;

import models.brownPeterson.Answer;
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

import views.html.admin.*;



/**
 * Created by ohmini on 12/9/2557.
 */
public class ExportController extends Controller {

    public static Result downloadPage(){
        return ok(export.render());
    }

    public static Result downloadAttentionBlink() throws IOException{

        Workbook wb = new HSSFWorkbook();

        models.attentionBlink.Trial.exportToFile(wb);
        models.attentionBlink.Quiz.exportToFile(wb);
        models.attentionBlink.Question.exportToFile(wb);
        models.attentionBlink.Answer.exportToFile(wb);

        File file = new File("attention_blink.xls");
        FileOutputStream out = new FileOutputStream(file);
        wb.write(out);
        out.close();

        return ok(file);
    }

    public static Result downloadBrownPeterson() throws IOException{

        Workbook wb = new HSSFWorkbook();

        models.brownPeterson.Trial.exportToFile(wb);
        models.brownPeterson.Quiz.exportToFile(wb);
        models.brownPeterson.Question.exportToFile(wb);
        models.brownPeterson.Answer.exportToFile(wb);

        File file = new File("brown_peterson.xls");
        FileOutputStream out = new FileOutputStream(file);
        wb.write(out);
        out.close();

        return ok(file);
    }

}
