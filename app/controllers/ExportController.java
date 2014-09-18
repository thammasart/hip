package controllers;

import models.ExperimentSchedule;
import models.TimeLog;
import models.User;
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

    public static Result downloadMullerLayer() throws IOException{

        Workbook wb = new HSSFWorkbook();

        models.mullerLayer.Trial.exportToFile(wb);
        models.mullerLayer.Quiz.exportToFile(wb);
        models.mullerLayer.Question.exportToFile(wb);
        models.mullerLayer.Answer.exportToFile(wb);

        File file = new File("muller_layer.xls");
        FileOutputStream out = new FileOutputStream(file);
        wb.write(out);
        out.close();

        return ok(file);

    }

    public static Result downloadPositionError() throws IOException{

        Workbook wb = new HSSFWorkbook();

        models.positionError.Trial.exportToFile(wb);
        models.positionError.Quiz.exportToFile(wb);
        models.positionError.Question.exportToFile(wb);
        models.positionError.Answer.exportToFile(wb);

        File file = new File("position_error.xls");
        FileOutputStream out = new FileOutputStream(file);
        wb.write(out);
        out.close();

        return ok(file);

    }

    public static Result downloadSignalDetection() throws IOException{
        Workbook wb = new HSSFWorkbook();

        models.signalDetection.Trial.exportToFile(wb);
        models.signalDetection.Quiz.exportToFile(wb);
        models.signalDetection.Question.exportToFile(wb);
        models.signalDetection.Answer.exportToFile(wb);

        File file = new File("signal_detection.xls");
        FileOutputStream out = new FileOutputStream(file);
        wb.write(out);
        out.close();

        return ok(file);
    }

    public static Result downloadSimonEffect() throws IOException{
        Workbook wb = new HSSFWorkbook();

        models.simonEffect.Trial.exportToFile(wb);
        models.simonEffect.Quiz.exportToFile(wb);
        models.simonEffect.Question.exportToFile(wb);
        models.simonEffect.Answer.exportToFile(wb);

        File file = new File("simon_effect.xls");
        FileOutputStream out = new FileOutputStream(file);
        wb.write(out);
        out.close();

        return ok(file);
    }

    public static Result downloadSternbergSearch() throws IOException{
        Workbook wb = new HSSFWorkbook();

        models.sternbergSearch.Trial.exportToFile(wb);
        models.sternbergSearch.Quiz.exportToFile(wb);
        models.sternbergSearch.Question.exportToFile(wb);
        models.sternbergSearch.Answer.exportToFile(wb);

        File file = new File("sternberg_search.xls");
        FileOutputStream out = new FileOutputStream(file);
        wb.write(out);
        out.close();

        return ok(file);
    }

    public static Result downloadStroopEffect() throws IOException{
        Workbook wb = new HSSFWorkbook();

        models.stroopEffect.Trial.exportToFile(wb);
        models.stroopEffect.Quiz.exportToFile(wb);
        models.stroopEffect.Question.exportToFile(wb);
        models.stroopEffect.Answer.exportToFile(wb);

        File file = new File("stroop_effect.xls");
        FileOutputStream out = new FileOutputStream(file);
        wb.write(out);
        out.close();

        return ok(file);
    }

    public static Result downloadVisualSearch() throws IOException{
        Workbook wb = new HSSFWorkbook();

        models.visualSearch.Trial.exportToFile(wb);
        models.visualSearch.Quiz.exportToFile(wb);
        models.visualSearch.Question.exportToFile(wb);
        models.visualSearch.Answer.exportToFile(wb);

        File file = new File("visual_search.xls");
        FileOutputStream out = new FileOutputStream(file);
        wb.write(out);
        out.close();

        return ok(file);
    }

    public static Result downloadUser() throws IOException{
        Workbook wb = new HSSFWorkbook();

        User.exportToFile(wb);
        File file = new File("Users.xls");
        FileOutputStream out = new FileOutputStream(file);
        wb.write(out);
        out.close();

        return ok(file);
    }

    public static Result downloadTimeLog() throws IOException{
        Workbook wb = new HSSFWorkbook();

        TimeLog.exportToFile(wb);
        File file = new File("TimeLog.xls");
        FileOutputStream out = new FileOutputStream(file);
        wb.write(out);
        out.close();

        return ok(file);
    }

    public static Result downloadExperimentSchedule() throws IOException{
        Workbook wb = new HSSFWorkbook();

        ExperimentSchedule.exportToFile(wb);
        File file = new File("ExperimentSchedule.xls");
        FileOutputStream out = new FileOutputStream(file);
        wb.write(out);
        out.close();

        return ok(file);
    }

}
