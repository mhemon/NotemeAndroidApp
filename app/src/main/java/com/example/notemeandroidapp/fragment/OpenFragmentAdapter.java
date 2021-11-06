package com.example.notemeandroidapp.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notemeandroidapp.R;
import com.example.notemeandroidapp.TaskDetails;
import com.example.notemeandroidapp.database.AppDatabase;
import com.example.notemeandroidapp.database.Task;

import java.util.List;

public class OpenFragmentAdapter extends RecyclerView.Adapter<OpenFragmentAdapter.MyViewHolder>{

    private Context context;
    private List<Task> taskList;
    private Task task;
    private int sID;
    private String taskname;


    public OpenFragmentAdapter(Context context) {
        this.context = context;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.task_item, parent, false);
        context = parent.getContext();
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String nTask = this.taskList.get(holder.getAbsoluteAdapterPosition()).taskName;
        sID = this.taskList.get(holder.getAbsoluteAdapterPosition()).id;
        holder.tvtaskName.setText(this.taskList.get(holder.getAbsoluteAdapterPosition()).taskName);
        holder.tvcreated.setText(this.taskList.get(holder.getAbsoluteAdapterPosition()).created);
        holder.tvtaskdeadline.setText(this.taskList.get(holder.getAbsoluteAdapterPosition()).deadline);
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_edit);
                int width = WindowManager.LayoutParams.MATCH_PARENT;
                int height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setLayout(width,height);
                dialog.show();
                EditText taskname = dialog.findViewById(R.id.edit_task_name);
                Button btnupdate = dialog.findViewById(R.id.update);
                taskname.setText(nTask);
                btnupdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        String uTaskName = taskname.getText().toString().trim();
                        AppDatabase db  = AppDatabase.getDbInstance(context.getApplicationContext());
                        db.taskDao().updatename(sID,uTaskName);
                        taskList.clear();
                        taskList.addAll(db.taskDao().getOpenTask());
                        notifyDataSetChanged();
                    }
                });
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDatabase db  = AppDatabase.getDbInstance(context.getApplicationContext());
                Task task = taskList.get(position);
                db.taskDao().delete(task);
                taskList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, taskList.size());
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TaskDetails.class);
                intent.putExtra("position",sID);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.taskList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvtaskName;
        TextView tvcreated;
        TextView tvtaskdeadline;
        ImageView edit,delete;

        public MyViewHolder(View view) {
            super(view);
            context = view.getContext();
            tvtaskName = view.findViewById(R.id.task_name_item);
            tvcreated = view.findViewById(R.id.created_item);
            tvtaskdeadline = view.findViewById(R.id.deadline_item);
            edit = view.findViewById(R.id.task_edit);
            delete = view.findViewById(R.id.task_delete);
        }
    }
}
