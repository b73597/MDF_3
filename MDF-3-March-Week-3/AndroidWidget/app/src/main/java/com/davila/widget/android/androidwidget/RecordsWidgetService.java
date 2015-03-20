// Eddy Davila
// MDF 3
// Week 3
// Full Sail University

package com.davila.widget.android.androidwidget;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.davila.widget.android.androidwidget.storage.FileReadAndWrite;

public class RecordsWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        FileReadAndWrite frw = new FileReadAndWrite();
        return new RecordsWidgetViewFactory(getApplicationContext(), frw.get_all_records_from_file());
    }

}
