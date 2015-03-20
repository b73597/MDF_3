// Eddy Davila
// MDF 3
// Week 3
// Full Sail University

package com.davila.widget.android.androidwidget.storage;

import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileReadAndWrite {

    private final String folder_name = "AndroidWidget";
    private final String file_name = "records.txt";

    private final String RECORD_ID = "id";
    private final String RECORD_TITLE = "title";
    private final String RECORD_AUTHOR = "author";
    private final String RECORD_DATER = "dater";

    public void write_to_file(ArrayList<Record> records) {

        try {
            File folder = new File(Environment.getExternalStorageDirectory(), folder_name);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            File file = new File(folder, file_name);
            if (file.exists()) {
                file.delete();
            }
            if (!file.exists()) {
                file.createNewFile();
                JSONArray array = new JSONArray();
                JSONObject obj = new JSONObject();
                try {
                    obj.put("records", array);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            JSONArray jsonArray = new JSONArray();
            JSONObject obj = new JSONObject();

            for (int i = 0; i < records.size(); i++) {
                Record record = records.get(i);
                JSONObject json_record = new JSONObject();
                json_record.put(RECORD_ID, (i + 1));
                json_record.put(RECORD_TITLE, record.getTitle());
                json_record.put(RECORD_AUTHOR, record.getAuthor());
                json_record.put(RECORD_DATER, record.getDater());
                jsonArray.put(json_record);
            }
            obj.put("records", jsonArray);

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write(obj.toString());
            bufferedWriter.flush();
            bufferedWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public String file_read_all_text() {
        StringBuilder text = new StringBuilder();
        try {
            File folder = new File(Environment.getExternalStorageDirectory(), folder_name);
            File file = new File(folder, file_name);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
            }
            br.close();
        } catch (IOException e) {
        }
        return text.toString();
    }

    public ArrayList<Record> get_all_records_from_file() {
        ArrayList<Record> records = new ArrayList<Record>();
        String fileText = file_read_all_text();
        try {

            JSONObject jsonObject = new JSONObject(fileText);
            JSONArray json_records = jsonObject.getJSONArray("records");

            for (int i = 0; i < json_records.length(); i++) {
                JSONObject json_record = json_records.getJSONObject(i);

                Record record = new Record();
                record.setId(json_record.getInt(RECORD_ID));
                record.setTitle(json_record.getString(RECORD_TITLE));
                record.setAuthor(json_record.getString(RECORD_AUTHOR));
                record.setDater(json_record.getString(RECORD_DATER));

                records.add(record);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return records;
    }

    public void delete_record_from_file(Record record) {
        ArrayList<Record> records = get_all_records_from_file();
        boolean change = false;
        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getId() == record.getId()) {
                change = true;
                records.remove(i);
            }
        }
        if (change) {
            write_to_file(records);
        }
    }

    public void update_record_from_file(Record record) {
        ArrayList<Record> records = get_all_records_from_file();
        boolean change = false;
        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getId() == record.getId()) {
                change = true;
                records.get(i).setTitle(record.getTitle());
                records.get(i).setAuthor(record.getAuthor());
                records.get(i).setDater(record.getDater());
            }
        }
        if (change) {
            write_to_file(records);
        }
    }

    public void add_record_to_file(Record record) {
        ArrayList<Record> records = get_all_records_from_file();
        records.add(record);
        write_to_file(records);
    }

}
