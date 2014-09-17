package controllers;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.ss.usermodel.Workbook;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.admin.export;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;




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

    public static Result downloadChangeBlindness() throws IOException{

        Workbook wb = new HSSFWorkbook();

        models.changeBlindness.Trial.exportToFile(wb);
        models.changeBlindness.Quiz.exportToFile(wb);
        models.changeBlindness.Question.exportToFile(wb);
        models.changeBlindness.Answer.exportToFile(wb);

        File file = new File("change_blindness.xls");
        FileOutputStream out = new FileOutputStream(file);
        wb.write(out);
        out.close();

        return ok(file);

    }

    public static Result downloadGarnerInterference() throws IOException{

        Workbook wb = new HSSFWorkbook();

        models.garnerInterference.Trial.exportToFile(wb);
        models.garnerInterference.Quiz.exportToFile(wb);
        models.garnerInterference.Question.exportToFile(wb);
        models.garnerInterference.Color.exportToFile(wb);
        models.garnerInterference.Answer.exportToFile(wb);

        File file = new File("garner_interference.xls");
        FileOutputStream out = new FileOutputStream(file);
        wb.write(out);
        out.close();

        return ok(file);

    }

    public static Result downloadMagicNumber7() throws IOException{

        Workbook wb = new HSSFWorkbook();

        models.magicNumber7.Trial.exportToFile(wb);
        models.magicNumber7.Quiz.exportToFile(wb);
        models.magicNumber7.Question.exportToFile(wb);
        models.magicNumber7.Answer.exportToFile(wb);

        File file = new File("magic_number_7.xls");
        FileOutputStream out = new FileOutputStream(file);
        wb.write(out);
        out.close();

        return ok(file);

    }

}
