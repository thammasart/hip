package models.garnerInterference;

import play.db.ebean.Model;
import javax.persistence.*;

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
@Table (name="garner_interference_color")
public class Color extends Model{
    @Id
    public long id;
    public String colorCode;
    public String color;
    public int saturation;
    
    public Color(){
    	
    }

    public static void exportToFile(Workbook wb){

        try{
            int headerRowIndex = 0;
            int col = 0;
            //wb = new HSSFWorkbook();
            Sheet userSheet = wb.createSheet("Color");

            Row headerRow = userSheet.createRow(headerRowIndex++);

            Cell tableName = headerRow.createCell(0);
            tableName.setCellValue("Garner Interference Color");

            headerRow = userSheet.createRow(headerRowIndex++);
            Cell idHeaderCell = headerRow.createCell(col++);
            idHeaderCell.setCellValue("ID");

            Cell codeCell = headerRow.createCell(col++);
            codeCell.setCellValue("Color Code");

            Cell colorCell = headerRow.createCell(col++);
            colorCell.setCellValue("Color");

            Cell saturationCell = headerRow.createCell(col++);
            saturationCell.setCellValue("Saturation");

            List<Color> tempList = find.all();

            int listSize = tempList.size();
            for(int row=headerRowIndex;row-headerRowIndex<listSize;row++){
                Color temp = tempList.get(row-headerRowIndex);
                col = 0;
                Row dataRow = userSheet.createRow(row);

                Cell dataId = dataRow.createCell(col++);
                dataId.setCellValue(temp.id);

                Cell dataCode = dataRow.createCell(col++);
                dataCode.setCellValue(temp.colorCode);

                Cell dataColor = dataRow.createCell(col++);
                dataColor.setCellValue(temp.color);

                Cell dataSaturation = dataRow.createCell(col++);
                dataSaturation.setCellValue(temp.saturation);
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
    public static Finder<Long, Color> find = new Finder(Long.class, Color.class);

}
