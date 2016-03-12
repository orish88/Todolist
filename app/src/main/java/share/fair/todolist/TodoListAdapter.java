package share.fair.todolist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ori on 3/3/2016.
 */
public class TodoListAdapter extends ArrayAdapter {

    private int rowResource;
    private ArrayList<TodoListItem> arrTodoList;
    Context context;
    public TodoListAdapter(Context context, int resource,ArrayList<TodoListItem> arrTodoList) {
        super(context, resource,arrTodoList);
        this.rowResource =resource;
        this.arrTodoList = arrTodoList;
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.d("notes", "ConvertView " + String.valueOf(position));
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.todolist_item, null);
        }
        ImageView imgItemImg= (ImageView) convertView.findViewById(R.id.img_todolist_item);
        imgItemImg.setImageResource(R.drawable.pin);
        final TextView tvItemText = (TextView) convertView.findViewById(R.id.tv_todolist_item);
        tvItemText.setText(arrTodoList.get(position).getInfo());
        if(position%2==0) {
            tvItemText.setTextColor(Color.parseColor("#308aff"));
        }else {
            tvItemText.setTextColor(Color.parseColor("#ff4b20"));
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("notes","item with "+tvItemText.getText().toString()+" was clicked.");
            }
        });
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return false;
            }
        });
        return convertView;
    }
}


