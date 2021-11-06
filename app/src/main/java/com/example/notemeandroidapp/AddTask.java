package com.example.notemeandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddTask extends AppCompatActivity {
    private Spinner status;
    private String taskstatus;
    final Calendar myCalendar = Calendar.getInstance();
    ImageView calendar,email,phone,url;
    TextView level;
    String deadline,email_string,phone_string,url_string;
    Button submit;
    EditText taskname, taskdescription;
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        status = findViewById(R.id.status);
        calendar = findViewById(R.id.imv_calendar);
        level = findViewById(R.id.addtask_level_text);
        email = findViewById(R.id.add_task_email);
        phone = findViewById(R.id.add_task_phone);
        url = findViewById(R.id.add_task_url);
        submit = findViewById(R.id.add_task_submit);
        taskname = findViewById(R.id.add_task_name);
        taskdescription = findViewById(R.id.add_task_description);

        //by default optional value
        email_string = "";
        phone_string = "";
        url_string = "";

        //initialized db
        db = AppDatabase.getDbInstance(this.getApplicationContext());

        // created date
        String created = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

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
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //detect deadline
                new DatePickerDialog(AddTask.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //handle email icon click
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(AddTask.this);
                dialog.setContentView(R.layout.email_dialog);
                Button save;
                EditText email;
                email = dialog.findViewById(R.id.email_edit_text);
                save = dialog.findViewById(R.id.save_email_dialog);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        email_string = email.getText().toString();
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
                final Dialog dialog = new Dialog(AddTask.this);
                dialog.setContentView(R.layout.phone_dialog);
                Button save_phone;
                EditText phone;
                phone = dialog.findViewById(R.id.phone_dialog);
                save_phone = dialog.findViewById(R.id.save_phone);
                save_phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        phone_string = phone.getText().toString();
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
                final Dialog dialog = new Dialog(AddTask.this);
                dialog.setContentView(R.layout.url_dialog);
                Button save_url;
                EditText url_dialog;
                url_dialog = dialog.findViewById(R.id.url_dialog);
                save_url = dialog.findViewById(R.id.save_url);
                save_url.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        url_string = url_dialog.getText().toString();
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
                savenewTask(taskname.getText().toString(),taskdescription.getText().toString(),created,deadline,email_string,phone_string,url_string,taskstatus);
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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddTask.this,android.R.layout.simple_spinner_item,items);
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

    private void savenewTask(String taskname,String taskdescription,String created, String deadline,String email, String phone, String url, String status){
        Task task = new Task();
        task.taskName = taskname;
        task.taskdescription = taskdescription;
        task.created = created;
        task.deadline = deadline;
        task.email = email;
        task.phone = phone;
        task.url = url;
        task.status = status;
        db.taskDao().insertTask(task);
        showsuccessdialog();
    }

    private void showsuccessdialog(){
        final Dialog dialog = new Dialog(AddTask.this);
        dialog.setContentView(R.layout.success_dialog);
        Button success;
        success = dialog.findViewById(R.id.success_ok);
        success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                startActivity(new Intent(AddTask.this,MainActivity.class));
                finish();
            }
        });
        dialog.show();
    }
}