package models.garnerInterference;

import play.db.ebean.Model;
import javax.persistence.*;

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

    @SuppressWarnings("unchecked")
    public static Finder<Long, Color> find = new Finder(Long.class, Color.class);

}
