package models.sternbergSearch;

import models.User;

import play.db.ebean.*;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table (name = "sternberg_search_answer")
public class Answer extends Model{

    @Id
    public long id;
    public double usedTime;
    public boolean isCorect;
    @ManyToOne
    public User user;
    @ManyToOne 
    public Quiz quiz;

	@SuppressWarnings("unchecked")
	public static Finder<Long,Answer> find = new Finder(Long.class,Answer.class);
}
