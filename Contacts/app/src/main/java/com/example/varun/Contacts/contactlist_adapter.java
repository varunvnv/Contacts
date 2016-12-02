package com.example.varun.Contacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by varun on 10/15/2016.
 */

public class contactlist_adapter extends ArrayAdapter<Contact> {
    public contactlist_adapter(Context context, ArrayList<Contact> resource) {
        super(context, R.layout.contactlist_layout, resource);
    }

    int index;
    CheckBox checkBox;


    @Override
    public View getView(final int index, View row, final ViewGroup parent) {

        LayoutInflater minflater = LayoutInflater.from(getContext());
        row = minflater.inflate(R.layout.contactlist_layout, parent, false);
        checkBox=(CheckBox)row.findViewById(R.id.checkbox);

        String name=getItem(index).getContact_name();
        TextView contact_text=(TextView)row.findViewById(R.id.conatct_text);
        contact_text.setText(name);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb=(CheckBox)v.findViewById(R.id.checkbox);
                if(cb.isChecked())
                {
                    getItem(index).setSelectedforDeletion();
                }
            }
        });
     return row;
    }


}