package com.example.varun.Contacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by varun on 10/20/2016.
 */

public class contact_profile_list_adapter extends ArrayAdapter<Contact> {
    public contact_profile_list_adapter(Context context, ArrayList<Contact> resource) {
        super(context, R.layout.contactprofile_list_layout, resource);
    }

    int index;
    @Override
    public View getView(final int index, View row, final ViewGroup parent) {

        LayoutInflater minflater = LayoutInflater.from(getContext());
        row = minflater.inflate(R.layout.contactprofile_list_layout, parent, false);

        String name=getItem(index).getContact_name();
        TextView contact_text=(TextView)row.findViewById(R.id.conatct_text);
        contact_text.setText(name);

        return row;
    }


}