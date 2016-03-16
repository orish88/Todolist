package share.fair.todolist;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ori on 3/6/2016.
 */
public class TodoListItem {

    private static int idCounter =0;
    private String info;
    private int id;
    private Date date;
    private String dateStr;

    public TodoListItem(){

    }
    public TodoListItem(String info,Date date){
        this.info = info;
        this.id = idCounter++;
        this.date =date;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        dateStr= sdf.format(date);

    }
    public TodoListItem(String info,String dateStr){
        this.info = info;
        this.id = idCounter++;
        this.dateStr =dateStr;
//        DateFormat formatter = new SimpleDateFormat("d-MMM-yyyy");
//        try {
//            this.date = formatter.parse(dateStr);
//        } catch (ParseException e) {
//            Log.d("notes","date parse exception: "+e.toString());
//            e.printStackTrace();
//        }
    }

    public String getInfo(){
        return this.info;
    }
    public void setInfo(String newInfo){
        this.info=newInfo;
    }
    public int getID(){
        return this.id;
    }
    public void setID(int newId){
        this.id = newId;
    }
    public void setDate(Date date){
        this.date = date;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        dateStr= sdf.format(date);
    }
    public Date getDate(){
        return this.date;
    }

    public void setDateString(String dateStr){
        this.dateStr = dateStr;
        DateFormat formatter = new SimpleDateFormat("d-MMM-yyyy");
//        try {
//            this.date = formatter.parse(dateStr);
//        } catch (ParseException e) {
//            Log.d("notes","date parse exception: "+e.toString());
//            e.printStackTrace();
//        }
    }
    public String getDateString(){
        return this.dateStr;
    }

}
