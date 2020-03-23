package com.duongll.succotask.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import com.duongll.succotask.R;
import com.duongll.succotask.entity.Task;

public class TaskAdapter extends BaseAdapter {
    private List<Task> taskList;

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    @Override
    public int getCount() {
        return taskList.size();
    }

    @Override
    public Object getItem(int position) {
        return taskList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return taskList.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.task_item_list, parent, false);
        }
        Task task = this.taskList.get(position);
        TextView taskId = convertView.findViewById(R.id.taskId);
        TextView taskName = convertView.findViewById(R.id.taskName);
        TextView taskStatus = convertView.findViewById(R.id.taskStatus);
        TextView taskDate = convertView.findViewById(R.id.txtTaskDate);
        LinearLayout linearLayout = convertView.findViewById(R.id.layoutTaskListItem);
        taskId.setText(task.getId() + "");
        taskName.setText(task.getName());
        String status = task.getTaskStatus();
        if (status.equals("SUBMITTED")) {
            linearLayout.setBackgroundColor(Color.parseColor("#eae265"));
        } else if (status.equals("SUCCEED")) {
            linearLayout.setBackgroundColor(Color.parseColor("#9fe365"));
        } else if (status.equals("FAIL")) {
            linearLayout.setBackgroundColor(Color.parseColor("#ee642e"));
        } else if (status.equals("IN PROGRESS")){
            linearLayout.setBackgroundColor(Color.parseColor("#309dcd"));
        } else if (status.equals("PENDING")){
            linearLayout.setBackgroundColor(Color.parseColor("#b1b3b1"));
        } else {
            linearLayout.setBackgroundColor(Color.parseColor("#ee642e"));
        }
        taskStatus.setText(status);
        String[] strTmpStart = task.getStartDate().toString().split(" ");
        String dayFrom = strTmpStart[2];
        String monthFrom = strTmpStart[1];
        String yearFrom = strTmpStart[5];
        String[] strTmpEnd = task.getEndDate().toString().split(" ");
        String dayEnd = strTmpEnd[2];
        String monthEnd = strTmpEnd[1];
        String yearEnd = strTmpEnd[5];
        String start = yearFrom + "/" +monthFrom + "/" + dayFrom;
        String end = yearEnd + "/" + monthEnd + "/" + dayEnd;
        taskDate.setText("From: " + start  + " -- " + end);
        return convertView;
    }
}
