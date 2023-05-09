package com.example.ex06_database;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.ex06_database.R;

public class MainActivity extends Activity implements OnClickListener {
    EditText id, name, pnumber;
    Button Insert, Delete, Update, View, ViewAll;
    SQLiteDatabase db;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        id = (EditText) findViewById(R.id.id);
        name = (EditText) findViewById(R.id.name);
        pnumber = (EditText) findViewById(R.id.pnumber);
        Insert = (Button) findViewById(R.id.Insert);
        Delete = (Button) findViewById(R.id.Delete);
        Update = (Button) findViewById(R.id.Update);
        View = (Button) findViewById(R.id.View);
        ViewAll = (Button) findViewById(R.id.ViewAll);

        Insert.setOnClickListener(this);
        Delete.setOnClickListener(this);
        Update.setOnClickListener(this);
        View.setOnClickListener(this);
        ViewAll.setOnClickListener(this);

// Creating database and table
        db = openOrCreateDatabase("RegistrationDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS registration(id VARCHAR,name VARCHAR, pnumber VARCHAR); ");
    }

    public void onClick(View view) {
// Inserting a record to the Student table
        if(view==Insert) {
// Checking for empty fields
            if(id.getText().toString().trim().length()==0||

                    name.getText().toString().trim().length() == 0 || pnumber.getText().toString().trim().length() == 0)
            {
                showMessage("Error", "Please enter all values");
                return;
            }
            db.execSQL("INSERT INTO registration VALUES ('" + id.getText() + "', '"+name.getText()+"', '"+pnumber.getText()+"'); "); showMessage(" Success ", " Record added "); clearText();
        }
// Deleting a record from the Student table
        if(view==Delete)
        {
// Checking for empty roll number
            if(id.getText().toString().trim().length()==0)
            {

                showMessage("Error", "Please enter ID: ");
                return;
            }
            Cursor c = db.rawQuery("SELECT * FROM registration WHERE id='" + id.getText() + "'", null);
            if (c.moveToFirst()) {
                db.execSQL("DELETE FROM registration WHERE id='" + id.getText() + "'");
                showMessage("Success", "Record Deleted");
            } else {
                showMessage("Error", "Invalid ID");
            }
            clearText();
        }
// Updating a record in the Student table
        if(view==Update)
        {
// Checking for empty roll number
            if(id.getText().toString().trim().length()==0)
            {
                showMessage("Error", "Please enter ID: ");
                return;
            }
            Cursor c = db.rawQuery("SELECT * FROM registration WHERE id = '"+id.getText()+"'", null); if(c.moveToFirst()) { db.execSQL("UPDATE registration SET name='" + name.getText() + "',pnumber='" + pnumber.getText() + "' WHERE id='" + id.getText() + "'");
            showMessage("Success", "Record Modified");
        }
        else{
            showMessage("Error", "Invalid ID");
        }
            clearText();
        }

        // Display a record from the Student table
        if(view==View)
        {
// Checking for empty roll number
            if(id.getText().toString().trim().length()==0)
            {
                showMessage("Error", "Please enter ID: ");
                return;
            }
            Cursor c = db.rawQuery("SELECT * FROM registration WHERE id = '"+id.getText()+"'", null); if(c.moveToFirst())
        {
            name.setText(c.getString(1));
            pnumber.setText(c.getString(2));
        }
        else
        {
            showMessage("Error", "Invalid ID");
            clearText();
        }
        }

        // Displaying all the records
        if(view==ViewAll)
        {
            Cursor c = db.rawQuery("SELECT * FROM registration", null);
            if (c.getCount() == 0) {
                showMessage("Error", "No records found");
                return;
            }
            StringBuffer buffer = new StringBuffer();
            while (c.moveToNext()) {
                buffer.append("ID: " + c.getString(0) + "\n");
                buffer.append("Name: " + c.getString(1) + "\n");
                buffer.append("Phone Number: " + c.getString(2) + "\n\n");
            }
            showMessage("Registration Details", buffer.toString());
        }
    }

    public void showMessage(String title, String message) {
        Builder builder = new Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void clearText() {
        id.setText("");
        name.setText("");
        pnumber.setText("");
        id.requestFocus(); }
}