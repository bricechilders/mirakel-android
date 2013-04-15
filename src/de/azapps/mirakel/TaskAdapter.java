package de.azapps.mirakel;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import org.joda.time.LocalDate;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class TaskAdapter extends ArrayAdapter<Task> {
	Context context;
	int layoutResourceId;
	List<Task> data = null;
	OnClickListener clickCheckbox;
	OnClickListener clickPrio;

	public TaskAdapter(Context context, int layoutResourceId, List<Task> data,
			OnClickListener clickCheckbox, OnClickListener click_prio) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.data = data;
		this.context = context;
		this.clickCheckbox = clickCheckbox;
		this.clickPrio = click_prio;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		TaskHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new TaskHolder();
			holder.taskRowDone = (CheckBox) row
					.findViewById(R.id.tasks_row_done);
			holder.taskRowName = (TextView) row
					.findViewById(R.id.tasks_row_name);
			holder.taskRowPriority = (TextView) row
					.findViewById(R.id.tasks_row_priority);
			holder.taskRowDue = (TextView) row.findViewById(R.id.tasks_row_due);

			row.setTag(holder);
		} else {
			holder = (TaskHolder) row.getTag();
		}

		Task task = data.get(position);
		holder.taskRowDone.setChecked(task.isDone());
		holder.taskRowDone.setOnClickListener(clickCheckbox);
		holder.taskRowDone.setTag(task);
		holder.taskRowName.setText(task.getName());
		if (task.isDone()) {
			holder.taskRowName.setTextColor(row.getResources().getColor(
					R.color.Grey));
		} else {
			holder.taskRowName.setTextColor(row.getResources().getColor(
					R.color.Black));
		}
		holder.taskRowPriority.setText("" + task.getPriority());
		holder.taskRowPriority.setBackgroundColor(Mirakel.PRIO_COLOR[task
				.getPriority() + 2]);
		holder.taskRowPriority.setOnClickListener(clickPrio);
		holder.taskRowPriority.setTag(task);

		holder.taskRowDue.setText(task.getDue().compareTo(
				new GregorianCalendar(1970, 1, 1)) < 0 ? ""
				: new SimpleDateFormat(context.getString(R.string.dateFormat), Locale.getDefault()).format(task
						.getDue().getTime()));

		LocalDate today = new LocalDate();
		LocalDate nextWeek = new LocalDate().plusDays(7);
		LocalDate due = new LocalDate(task.getDue());
		int cmpr = today.compareTo(due);
		if (task.isDone()) {
			// holder.taskRowDue.setTextColor(row.getResources().getColor(
			// R.color.Grey));
		} else if (cmpr > 0) {
			holder.taskRowDue.setTextColor(row.getResources().getColor(
					R.color.Red));
		} else if (cmpr == 0) {
			holder.taskRowDue.setTextColor(row.getResources().getColor(
					R.color.Orange));
		} else if (nextWeek.compareTo(due) >= 0) {
			holder.taskRowDue.setTextColor(row.getResources().getColor(
					R.color.Yellow));
		} else {
			holder.taskRowDue.setTextColor(row.getResources().getColor(
					R.color.Green));

		}

		return row;
	}

	static class TaskHolder {
		CheckBox taskRowDone;
		TextView taskRowName;
		TextView taskRowPriority;
		TextView taskRowDue;
	}

}