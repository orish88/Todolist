package share.fair.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ori on 3/5/2016.
 */



public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="TODOLIST_DB";
    private static final int DATABASE_VERSION =1;
    private static final String TABLE_TODOLIST_ITEMS ="TodoItems";
    private static final String KEY_ID ="id";
    private static final String KEY_INFO ="info";
    private static final String DATE ="date";

     public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_TODOLIST_ITEMS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_INFO + " TEXT," +DATE+" TEXT" +")";
        db.execSQL(CREATE_CONTACTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODOLIST_ITEMS);
        // Create tables again
        onCreate(db);
    }

    // Adding new contact
    public void addTodoListItem(TodoListItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("notes","add item to db- item: "+item.getInfo());

        ContentValues values = new ContentValues();
//        values.put(KEY_ID,item.getID());
        values.put(KEY_INFO, item.getInfo()); // Contact Name
        values.put(DATE, item.getDateString());
        // Inserting Row
        Log.d("notes","add item to db- values: "+values.toString());
        db.insert(TABLE_TODOLIST_ITEMS, null, values);
        db.close(); // Closing database connection
    }

    // Getting single contact
    public TodoListItem getTodoListItem(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TODOLIST_ITEMS, new String[]{KEY_ID,
                        KEY_INFO, DATE}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        TodoListItem item = new TodoListItem(cursor.getString(1),cursor.getString(3) ); //make another constructor
        // return contact
        return item;

    }

    // Getting All Contacts
    public List<TodoListItem> getAllItems() {
        List<TodoListItem> itemList = new ArrayList<TodoListItem>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TODOLIST_ITEMS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TodoListItem item = new TodoListItem();
                item.setID(Integer.parseInt(cursor.getString(0)));
                item.setInfo(cursor.getString(1));
                item.setDateString(cursor.getString(2));
                // Adding contact to list
                itemList.add(item);
            } while (cursor.moveToNext());
        }
        // return contact list
        for(int i=0;i<itemList.size();i++){
            Log.d("notes","db list: "+itemList.get(i).getInfo()+" id :"+itemList.get(i).getID()+" & ");

        }
        return itemList;
    }

    // Getting contacts Count
    public int getItemsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TODOLIST_ITEMS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        // return count
        return cursor.getCount();

    }
    // Updating single contact
    public int updateItem(TodoListItem item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_INFO, item.getInfo());

        // updating row
        return db.update(TABLE_TODOLIST_ITEMS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(item.getID())});

    }

    // Deleting single contact
    public void deleteItems(TodoListItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TODOLIST_ITEMS, KEY_ID + " = ?",
                new String[]{String.valueOf(item.getID())});
        db.close();
    }
    public void deleteItems(int id) { //todo: int string?
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TODOLIST_ITEMS, KEY_ID + " = ?",
                new String[] { String.valueOf(id)});
        db.close();
    }
    public void deleteItems(String info) { //todo: int string?
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TODOLIST_ITEMS, KEY_INFO + " = ?",
                new String[] {info});
        List<TodoListItem> itemList =this.getAllItems();
        for(int i=0;i<itemList.size();i++){
            Log.d("notes","db list: "+itemList.get(i).getInfo()+" id :"+itemList.get(i).getID()+" ^ ");

        }
        db.close();
    }
}
