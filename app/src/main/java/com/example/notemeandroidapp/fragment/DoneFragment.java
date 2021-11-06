package com.example.notemeandroidapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.notemeandroidapp.R;
import com.example.notemeandroidapp.database.AppDatabase;
import com.example.notemeandroidapp.database.Task;

import java.util.List;
import java.util.Objects;

public class DoneFragment extends Fragment {

    private DoneFragmentAdapter doneFragmentAdapter;
    private List<Task> taskList;

    public DoneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_done, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.done_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        doneFragmentAdapter = new DoneFragmentAdapter(getContext());
        recyclerView.setAdapter(doneFragmentAdapter);
        AppDatabase db = AppDatabase.getDbInstance(Objects.requireNonNull(getActivity()).getApplicationContext());
        taskList =db.taskDao().getDoneTask();
        doneFragmentAdapter.setTaskList(taskList);
        return view;
    }
}