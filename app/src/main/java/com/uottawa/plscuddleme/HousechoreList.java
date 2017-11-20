package com.uottawa.plscuddleme;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

/**
 * Created by Yuhan on 11/19/2017.
 */

public class HousechoreList extends ArrayAdapter <Housechore> {
    private Activity context;
    List <Housechore> housechores;

    public HousechoreList(Activity context, List<Housechore> housechores) {
        super(context, R.layout.housechore_list, housechores);
        this.context = context;
        this.housechores = housechores;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.add_housechore, null, true);

        TextView textViewchoreName = (TextView) listViewItem.findViewById(R.id.enter_chore_name);
        TextView textViewAssignee = (TextView) listViewItem.findViewById(R.id.enter_assignee);
        TextView textViewDueDate = (TextView) listViewItem.findViewById(R.id.enter_dueDate);
        TextView textViewPriority = (TextView) listViewItem.findViewById(R.id.enter_priority);
        TextView textViewCategory = (TextView) listViewItem.findViewById(R.id.enter_category);
        TextView textViewRewards = (TextView) listViewItem.findViewById(R.id.enter_rewards);

        //TODO: fix the get values
        Housechore housechore = housechores.get(position);
        textViewchoreName.setText(housechore.getHousechoreName());
        textViewAssignee.setText(String.valueOf(housechore.getAssignedTo()));
        textViewDueDate.setText(String.valueOf(housechore.getDueDate()));
        textViewPriority.setText(String.valueOf(housechore.getPriority()));
        textViewCategory.setText(String.valueOf(housechore.getCategory()));

        return listViewItem;

    }

}