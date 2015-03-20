// Eddy Davila
// MDF 3
// Week 3
// Full Sail University

package com.davila.widget.android.androidwidget.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.davila.widget.android.androidwidget.R;
import com.davila.widget.android.androidwidget.storage.Record;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<Record> {

    private final Context context;
    private final ArrayList<Record> records;

    public CustomListAdapter(Context context, ArrayList<Record> records) {
        super(context, R.layout.rowlayout, records);
        this.context = context;
        this.records = records;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowlayout, parent, false);

        TextView tv_title = (TextView) rowView.findViewById(R.id.title);
        tv_title.setText(records.get(position).getTitle());
        TextView tv_author = (TextView) rowView.findViewById(R.id.author);
        tv_author.setText(records.get(position).getAuthor());
        TextView tv_date = (TextView) rowView.findViewById(R.id.date);
        tv_date.setText(records.get(position).getDater());

        return rowView;
    }
}
