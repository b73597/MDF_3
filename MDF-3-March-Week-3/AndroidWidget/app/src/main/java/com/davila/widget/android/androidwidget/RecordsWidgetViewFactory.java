// Eddy Davila
// MDF 3
// Week 3
// Full Sail University

package com.davila.widget.android.androidwidget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.davila.widget.android.androidwidget.storage.FileReadAndWrite;
import com.davila.widget.android.androidwidget.storage.Record;

import java.util.ArrayList;

public class RecordsWidgetViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final int ID_CONSTANT = 0x0101010;
    private ArrayList<Record> records;
    private Context mContext;

    public RecordsWidgetViewFactory(Context context, ArrayList<Record> records) {
        this.mContext = context;
        this.records = new ArrayList<Record>();
        this.records = records;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public long getItemId(int position) {
        return ID_CONSTANT + position;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Record article = records.get(position);
        RemoteViews itemView = new RemoteViews(mContext.getPackageName(),
                R.layout.rowlayout);

        itemView.setTextViewText(R.id.title, article.getTitle());
        itemView.setTextViewText(R.id.author, article.getAuthor());
        itemView.setTextViewText(R.id.date, article.getDater());

        Intent intent = new Intent();
        intent.putExtra(RecordsWidgetProvider.EXTRA_ITEM, article);
        itemView.setOnClickFillInIntent(R.id.article_item, intent);

        return itemView;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onDataSetChanged() {
        // Heavy lifting code can go here without blocking the UI.
        // You would update the data in your collection here as well.
        FileReadAndWrite frw = new FileReadAndWrite();
        records.clear();
        records.addAll(frw.get_all_records_from_file());
    }

    @Override
    public void onDestroy() {
        records.clear();
    }
}
