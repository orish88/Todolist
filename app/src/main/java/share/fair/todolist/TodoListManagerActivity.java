package share.fair.todolist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TodoListManagerActivity extends AppCompatActivity {


//    EditText etNewItem;
    private static final int ADD_RESULT_KEY =1;
    ListView lvTodolist;
    Button btAddItem;
    ArrayList<TodoListItem> arrListTodolist;
//    ArrayAdapter arrAdapter;
    TodoListAdapter todolistAdapter;
    DatabaseHandler db;
    Firebase myFirebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Firebase.setAndroidContext(this);
        myFirebaseRef = new Firebase("https://crackling-inferno-381.firebaseio.com/");
//        myFirebaseRef.child("firstData").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                System.out.println(snapshot.getValue());  //prints "Do you have data? You'll love Firebase."
//                Log.d("notes", "print: " + snapshot.getValue());
//            }
//
//            @Override
//            public void onCancelled(FirebaseError error) {
//            }
//        });


        setContentView(R.layout.activity_todo_list_manager);
        Log.d("notes", "onCreatecalled");
        db = new DatabaseHandler(this, myFirebaseRef);
//        db.close();
//        this.deleteDatabase(db.getDatabaseName());



//        etNewItem = (EditText) findViewById(R.id.et_enter_todolist_item);
        lvTodolist= (ListView) findViewById(R.id.lv_todolist);
        arrListTodolist = (ArrayList)db.getAllItems();
//        arrListTodolist.add("debug123");
//        arrAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,arrListTodolist);
        todolistAdapter = new TodoListAdapter(getApplicationContext(),R.layout.todolist_item,arrListTodolist);
        lvTodolist.setAdapter(todolistAdapter);
//        btAddItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String itemStr = etNewItem.getText().toString();
//                if (itemStr.isEmpty()) {
//                    Toast.makeText(getApplicationContext(), "Enter some text dude", Toast.LENGTH_SHORT).show();
//                } else {
//                    TodoListItem newTodoItem = new TodoListItem(itemStr);
//                    db.addTodoListItem(newTodoItem);
////                    arrListTodolist = (ArrayList)db.getAllItems();
//                    arrListTodolist.add(newTodoItem);
//                    todolistAdapter.notifyDataSetChanged();
//                    Log.d("notes", "item added");
//                    etNewItem.setText("");
//                }
//            }
//        });
        registerForContextMenu(lvTodolist);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.lv_todolist) {
            Log.d("notes", "in menu func");
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            String infoText= arrListTodolist.get(info.position).getInfo();
            menu.setHeaderTitle(infoText);
//            String[] menuItems = {"Delete","Try"};
            String[] menuItems = getResources().getStringArray(R.array.str_arr_item_menu);
            int i =0;
            for ( i=0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
            if(infoText.startsWith("Call ")){
                menu.add(Menu.NONE,i,i,infoText);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        String[] menuItems = getResources().getStringArray(R.array.str_arr_item_menu);
        String menuItemName = "";
        if(menuItemIndex >= menuItems.length){
            //case of call click
            String number = arrListTodolist.get(info.position).getInfo().substring(5);
            Log.d("notes","number to dial: "+number);
            Uri call = Uri.parse("tel:" + number);
            Intent surf = new Intent(Intent.ACTION_DIAL, call);
            startActivity(surf);
            return false;


        }else {
             menuItemName= menuItems[menuItemIndex];
        }
        String listItemName = arrListTodolist.get(info.position).getInfo();
        Log.d("notes","info: "+listItemName+" pos: "+info.position);
        if(menuItemName.equals("DELETE")) {
            Log.d("notes", "deletion if entered");
            db.deleteItems(listItemName);
            for(int i=0; i < arrListTodolist.size(); i++){
                Log.d("notes", "arrItem1: " + i + " : " + arrListTodolist.get(i));
                if(arrListTodolist.get(i).getInfo().equals(listItemName)){
                    myFirebaseRef.child("todoListItems").child("id "+arrListTodolist.get(i).getID()).removeValue();
                    arrListTodolist.remove(i);
                }
            }
            for(int i=0; i < arrListTodolist.size(); i++){
                Log.d("notes","arrItem2: "+i+" : "+arrListTodolist.get(i) );
            }
            todolistAdapter.notifyDataSetChanged();
        }else if(menuItemName.equals("EDIT")){


        }


//        TextView text = (TextView)findViewById(R.id.tv_todolist_item);
//        text.setText(String.format("Selected %s for item %s", menuItemName, listItemName));
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_todo_list_manager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            Log.d("notes","action add clicked");
            Intent addDialog = new Intent(getApplicationContext(),AddNewTodoItemActivity.class);
            startActivityForResult(addDialog, ADD_RESULT_KEY);
            return true;
        }
        if(id == R.id.action_reset_db){
            new AlertDialog.Builder(this)
                    .setTitle("Reset Todolist")
                    .setMessage("Are you sure you want to permanently erase your current list?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            db.close();
                            getApplicationContext().deleteDatabase(db.getDatabaseName());
                            db = new DatabaseHandler(getApplicationContext(),myFirebaseRef);
                            arrListTodolist = (ArrayList)db.getAllItems();
                            todolistAdapter.clear();
                            todolistAdapter.notifyDataSetChanged();
                            todolistAdapter = new TodoListAdapter(getApplicationContext(),R.layout.todolist_item,arrListTodolist);
                            lvTodolist.setAdapter(todolistAdapter);
                            registerForContextMenu(lvTodolist);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        Log.d("notes", "onActivityResult called 1");
        if(requestCode == ADD_RESULT_KEY){
            Log.d("notes", "onActivityResult called 2");
            if(resultCode == Activity.RESULT_OK) {
                String infoStr = data.getStringExtra("title");
                Date dueDate = (Date) data.getSerializableExtra("dueDate");
                TodoListItem newTodoItem = new TodoListItem(infoStr, dueDate);
                Log.d("notes", "on result: info: " + newTodoItem.getInfo() + " date: " + newTodoItem.getDateString());
                db.addTodoListItem(newTodoItem);
                arrListTodolist.add(newTodoItem);
                todolistAdapter.notifyDataSetChanged();


                Log.d("notes", "item added in menu");
            }
        }
    }
}
