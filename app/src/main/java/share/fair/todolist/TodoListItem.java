package share.fair.todolist;

/**
 * Created by Ori on 3/6/2016.
 */
public class TodoListItem {

    private static int idCounter =0;
    private String info;
    private int id;

    public TodoListItem(){

    }
    public TodoListItem(String info){
        this.info = info;
        this.id = idCounter++;
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
}
