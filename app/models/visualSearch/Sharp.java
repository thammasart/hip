package models.visualSearch;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;

/**
 * Created by Ratthasorn Thassana on 6/2/14.
 */
public class Sharp {
    private int top;
    private int left;
    private ShapeType shapeType;

    public Sharp(int top, int left, ShapeType shapeType){
        this.top = top;
        this.left = left;
        this.shapeType = shapeType;
    }

    public static JsonNode toJson(String sharps){
        return Json.parse(sharps);
    }
}
