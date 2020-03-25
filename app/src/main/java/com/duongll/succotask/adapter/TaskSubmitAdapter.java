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

public class TaskSubmitAdapter extends BaseAdapter {
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
            convertView = inflater.inflate(R.layout.task_submited_item_list, parent, false);
        }
        Task task = this.taskList.get(position);
        TextView taskId = convertView.findViewById(R.id.taskIdSubmit);
        TextView taskName = convertView.findViewById(R.id.taskNameSubmit);
        TextView taskStatus = convertView.findViewById(R.id.txtTaskHandlerSubmit);
        TextView taskDate = convertView.findViewById(R.id.txtTaskDateSubmit);
        LinearLayout layout = convertView.findViewById(R.id.layoutSubmittedItem);
        if (task.getTaskStatus().equals("PENDING")) {
            layout.setBackgroundColor(Color.parseColor("#bfbcc0"));
        } else if (task.getTaskStatus().equals("SUBMITTED")) {
            layout.setBackgroundColor(Color.parseColor("#eae265"));
        } else {
            layout.setBackgroundColor(Color.parseColor("#bfbcc0"));
        }
        taskId.setText(task.getId() + "");
        taskName.setText(task.getName());
        taskStatus.setText(task.getHandlerId().getId() + " - " + task.getHandlerId().getName());
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
