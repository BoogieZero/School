package com.example.boogiezero.todolist;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Adapter for populating listView with custom item layout.
 * Two texView components.
 */
public class ListArrayAdapter extends ArrayAdapter<TaskList.Task> {
    Activity context;
    List<TaskList.Task> objects;

    public ListArrayAdapter(Activity context, int textViewResourceId, List<TaskList.Task> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.objects = objects;

    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.list_num_text, null);
        }

        TaskList.Task item = objects.get(position);

        if (item != null) {
            TextView name = (TextView) v.findViewById(R.id.lbTaskName_list);
            TextView time = (TextView) v.findViewById(R.id.lbTaskTime_list);
            if (name != null) {
                name.setText(item.name);
            }
            if (time != null) {
                time.setText(item.getRemainingTimeString());
            }
        }

        return v;
    }
}
