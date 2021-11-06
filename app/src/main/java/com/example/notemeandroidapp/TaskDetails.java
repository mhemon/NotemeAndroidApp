package com.example.notemeandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notemeandroidapp.database.AppDatabase;
import com.example.notemeandroidapp.database.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

public class TaskDetails extends AppCompatActivity {

    int position;
    TextView created, deadline, status, name , details;
    ImageView delete, email , phone, url;
    private Task task;
    private AppDatabase db;
    private FloatingActionButton edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);
        Intent mIntent = getIntent();
        position = mIntent.getIntExtra("position", 0);

        //find view by id
        created = findViewById(R.id.details_created);
        deadline = findViewById(R.id.details_deadline);
        status = findViewById(R.id.details_status);
        name = findViewById(R.id.details_name);
        details = findViewById(R.id.details_description);
        delete = findViewById(R.id.details_delete);
        email = findViewById(R.id.details_email);
        phone = findViewById(R.id.details_phone);
        url = findViewById(R.id.details_url);
        edit = findViewById(R.id.details_edit);

        //get data from database
        db = AppDatabase.getDbInstance(getApplicationContext());
        task =db.taskDao().getDetailsTask(position);

        //set data to view
        created.setText(task.created);
        deadline.setText(task.deadline);
        status.setText(task.status);
        name.setText(task.taskName);
        details.setText(task.taskdescription);

        //handle click action
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               db.taskDao().delete(task);
               showsuccessdialog();
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TaskDetails.this, "Email:- "+task.email, Toast.LENGTH_SHORT).show();
            }
        });
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TaskDetails.this, "Phone:- "+task.phone, Toast.LENGTH_SHORT).show();
            }
        });
        url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TaskDetails.this, "Url:- "+task.url, Toast.LENGTH_SHORT).show();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskDetails.this, EditTask.class);
                intent.putExtra("editposition",position);
                startActivity(intent);
                finish();
            }
        });
    }

    private void showsuccessdialog(){
        final Dialog dialog = new Dialog(TaskDetails.this);
        dialog.setContentView(R.layout.success_dialog);
        Button success;
        success = dialog.findViewById(R.id.success_ok);
        TextView txt = dialog.findViewById(R.id.success_dialog_message);
        txt.setText("Task Deleted\nSuccessfull");
        success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                startActivity(new Intent(TaskDetails.this,MainActivity.class));
                finish();
            }
        });
        dialog.show();
    }
}