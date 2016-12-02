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
 * Created by varun on 10/20/2016.
 */

public class add_contact_list_adapter extends ArrayAdapter<Contact> {
    public add_contact_list_adapter(Context context, ArrayList<Contact> resource) {
        super(context, R.layout.add_contact_list_layout, resource);
    }

    int index;
    CheckBox checkBox;
    TextView contact_text;

    @Override
    public View getView(final int index, View row, final ViewGroup parent) {

        LayoutInflater minflater = LayoutInflater.from(getContext());
        row = minflater.inflate(R.layout.add_contact_list_layout, parent, false);
        checkBox=(CheckBox)row.findViewById(R.id.checkbox);

        String name=getItem(index).getContact_name();
        System.out.println(name+" is adding to adapter");
        contact_text=(TextView)row.findViewById(R.id.conatct_text);
        contact_text.setText(name);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb=(CheckBox)v.findViewById(R.id.checkbox);
                if(cb.isChecked())
                {
                    getItem(index).setSelected(true);
                }
            }
        });
        return row;
    }


}