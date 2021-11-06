package com.example.notemeandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.notemeandroidapp.database.AppDatabase;
import com.example.notemeandroidapp.database.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditTask extends AppCompatActivity {

    private int position;
    private EditText name , details;
    private ImageView email , phone, url, deadline_cal;
    private Button submit;
    private Spinner status;
    private String taskstatus;
    final Calendar myCalendar = Calendar.getInstance();
    private Task task;
    private AppDatabase db;
    TextView level;
    String deadline,email_string,phone_string,url_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        Intent mIntent = getIntent();
        position = mIntent.getIntExtra("editposition", 0);

        //get data from database
        db = AppDatabase.getDbInstance(getApplicationContext());
        task =db.taskDao().getDetailsTask(position);

        //find view
        status = findViewById(R.id.edit_status);
        name = findViewById(R.id.edit_taskname);
        details = findViewById(R.id.edit_taskdescription);
        email = findViewById(R.id.edit_email);
        phone = findViewById(R.id.edit_phone);
        url = findViewById(R.id.edit_url);
        submit = findViewById(R.id.edit_submit);
        deadline_cal = findViewById(R.id.edit_calendar);
        level = findViewById(R.id.edit_deadline);

        //set existing data to the view
        name.setText(task.taskName);
        details.setText(task.taskdescription);
        level.setText(task.deadline);

        //detect status
        initstatus();

        //date picker dialog
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                //update the deadline text
                updateLabel();
            }
        };

        //handle onclick on deadline date picker
        deadline_cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //detect deadline
                new DatePickerDialog(EditTask.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //handle email icon click
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(EditTask.this);
                dialog.setContentView(R.layout.email_dialog);
                Button save;
                EditText email;
                email = dialog.findViewById(R.id.email_edit_text);
                //set data
                email.setText(task.email);
                save = dialog.findViewById(R.id.save_email_dialog);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        email_string = email.getText().toString();
                        db.taskDao().updateemail(task.id,email_string.trim());
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });

        //handle phone icon click
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(EditTask.this);
                dialog.setContentView(R.layout.phone_dialog);
                Button save_phone;
                EditText phone;
                phone = dialog.findViewById(R.id.phone_dialog);
                phone.setText(task.phone);
                save_phone = dialog.findViewById(R.id.save_phone);
                save_phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        phone_string = phone.getText().toString();
                        db.taskDao().updatephone(task.id,phone_string.trim());
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });

        //handle url icon click
        url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(EditTask.this);
                dialog.setContentView(R.layout.url_dialog);
                Button save_url;
                EditText url_dialog;
                url_dialog = dialog.findViewById(R.id.url_dialog);
                url_dialog.setText(task.url);
                save_url = dialog.findViewById(R.id.save_url);
                save_url.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        url_string = url_dialog.getText().toString();
                        db.taskDao().updateurl(task.id,url_string.trim());
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });

        //save task to the db
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskname = name.getText().toString().trim();
                String taskdesc = details.getText().toString().trim();
                String taskdeadline = level.getText().toString().trim();
                db.taskDao().updatename(task.id,taskname);
                db.taskDao().updatedescripton(task.id,taskdesc);
                db.taskDao().updatedeadline(task.id,taskdeadline);
                db.taskDao().updatestatus(task.id,taskstatus.trim());
                showsuccessdialog();
               // savenewTask(taskname.getText().toString(),taskdescription.getText().toString(),created,deadline,email_string,phone_string,url_string,taskstatus);
            }
        });
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        level.setText(sdf.format(myCalendar.getTime()));
        deadline = (sdf.format(myCalendar.getTime()));
    }

    private void initstatus() {
        String[] items = new String[]{
                "Open","In-Progress","Test","Done"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditTask.this,android.R.layout.simple_spinner_item,items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        status.setAdapter(adapter);
        status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                taskstatus = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void showsuccessdialog(){
        final Dialog dialog = new Dialog(EditTask.this);
        dialog.setContentView(R.layout.success_dialog);
        Button success;
        success = dialog.findViewById(R.id.success_ok);
        success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                startActivity(new Intent(EditTask.this,MainActivity.class));
                finish();
            }
        });
        dialog.show();
    }
}