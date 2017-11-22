package com.uottawa.plscuddleme;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Psymon on 2017-11-21.
 */



public class FamilyMemberAdapter extends ArrayAdapter {

    private static final String TAG = "FamilyMember Adapter";
    private final Context context;
    private final String[] members;
    public FamilyMemberAdapter(Context context, String[] famList) {
        super(context, R.layout.family_member_layout, famList);
        this.context = context;
        this.members = famList;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.family_member_layout, parent, false);
        TextView familyNameTextView = (TextView) rowView.findViewById(R.id.family_member_name);
        TextView familyMemberRole = (TextView) rowView.findViewById(R.id.family_member_role);
        ImageView famImage = (ImageView) rowView.findViewById(R.id.family_member_icon);
        Log.i(TAG, "position = " +position);
        familyNameTextView.setText(members[position]);
        familyMemberRole.setText(members[position] + " is a member!");
        return rowView;
    }
}
