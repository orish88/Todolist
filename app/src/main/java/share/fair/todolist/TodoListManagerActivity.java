package share.fair.todolist;

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

import java.util.ArrayList;
import java.util.List;

public class TodoListManagerActivity extends AppCompatActivity {


    EditText etNewItem;
    ListView lvTodolist;
    Button btAddItem;
    ArrayList<TodoListItem> arrListTodolist;
//    ArrayAdapter arrAdapter;
    TodoListAdapter todolistAdapter;
    DatabaseHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list_manager);
        Log.d("notes", "onCreatecalled");

        db = new DatabaseHandler(this);

        etNewItem = (EditText) findViewById(R.id.et_enter_todolist_item);
        lvTodolist= (ListView) findViewById(R.id.lv_todolist);
        btAddItem = (Button) findViewById(R.id.bt_add_item);
        arrListTodolist = (ArrayList)db.getAllItems();
//        arrListTodolist.add("debug123");
//        arrAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,arrListTodolist);
        todolistAdapter = new TodoListAdapter(getApplicationContext(),R.layout.todolist_item,arrListTodolist);
        lvTodolist.setAdapter(todolistAdapter);
        btAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemStr = etNewItem.getText().toString();
                if (itemStr.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter some text dude", Toast.LENGTH_SHORT).show();
                } else {
                    TodoListItem newTodoItem = new TodoListItem(itemStr);
                    db.addTodoListItem(newTodoItem);
//                    arrListTodolist = (ArrayList)db.getAllItems();
                    arrListTodolist.add(newTodoItem);
                    todolistAdapter.notifyDataSetChanged();
                    Log.d("notes", "item added");
                    etNewItem.setText("");
                }
            }
        });
        registerForContextMenu(lvTodolist);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.lv_todolist) {
            Log.d("notes", "in menu func");
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(arrListTodolist.get(info.position).getInfo());
//            String[] menuItems = {"Delete","Try"};
            String[] menuItems = getResources().getStringArray(R.array.str_arr_item_menu);
            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        String[] menuItems = getResources().getStringArray(R.array.str_arr_item_menu);
        String menuItemName = menuItems[menuItemIndex];
        String listItemName = arrListTodolist.get(info.position).getInfo();
        Log.d("notes","info: "+listItemName+" pos: "+info.position);
        if(menuItemName.equals("DELETE")) {
            Log.d("notes", "deletion if entered");
            db.deleteItems(listItemName);
            for(int i=0; i < arrListTodolist.size(); i++){
                Log.d("notes", "arrItem1: " + i + " : " + arrListTodolist.get(i));
                if(arrListTodolist.get(i).getInfo().equals(listItemName)){
                    arrListTodolist.remove(i);
                }
            }

            for(int i=0; i < arrListTodolist.size(); i++){
                Log.d("notes","arrItem2: "+i+" : "+arrListTodolist.get(i) );
            }
            todolistAdapter.notifyDataSetChanged();

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
            String itemStr = etNewItem.getText().toString();
            if (itemStr.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Enter some text dude", Toast.LENGTH_SHORT).show();
            } else {
                TodoListItem newTodoItem = new TodoListItem(itemStr);
                db.addTodoListItem(newTodoItem);
//                    arrListTodolist = (ArrayList)db.getAllItems();
                arrListTodolist.add(newTodoItem);
                todolistAdapter.notifyDataSetChanged();
                Log.d("notes", "item added in menu");
                etNewItem.setText("");
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
