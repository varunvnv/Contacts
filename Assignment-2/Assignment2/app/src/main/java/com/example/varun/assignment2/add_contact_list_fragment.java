package com.example.varun.assignment2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


public class add_contact_list_fragment extends android.app.Fragment {
    private Activity myActivity;

    EditText nametext, phonetext;
    ArrayList<Contact> contact_list = new ArrayList<>();
    ListView relationlist;
    Button add_button;
    public boolean l = false;
    get_updated_list new_list;
    public interface get_updated_list{
        public void updated_list(ArrayList<Contact> list);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            new_list = (get_updated_list) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_add_contact_list_fragment, container, false);
        add_button = (Button)view.findViewById(R.id.add_button);
        relationlist = (ListView) view.findViewById(R.id.relationlist);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Add clicked");
                String name = nametext.getText().toString();
                String num = phonetext.getText().toString();


                Contact contact = new Contact();


                contact.setContact_name(name);
                contact.setContact_number(num);

                for (int i = 0; i < contact_list.size(); i++) {
                    if (contact_list.get(i).getSelected() == true) {
                        Contact c = contact_list.get(i);
                        c.setSelected(false);
                        addToRelation(contact, c);
                    }
                }
                nametext.setText(null);
                phonetext.setText(null);
                System.out.println("Sending");
                contact_list.add(contact);
                new_list.updated_list(contact_list);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        System.out.println("Entered add fragment");

        myActivity = getActivity();

        Bundle bundle=getArguments();
        if(bundle!=null)
        {
            Log.d("Bundle","BUNDLE NOT NULL");
            contact_list=(ArrayList<Contact>) bundle.getSerializable("contact_list");

        }
        else{
            Log.d("Bundle","BUNDLE IS NULL");
        }

        nametext = (EditText) myActivity.findViewById(R.id.nametext);
        //imm.hideSoftInputFromWindow(nametext.getWindowToken(), 0);

        phonetext = (EditText) myActivity.findViewById(R.id.phonetext);
        //imm.hideSoftInputFromWindow(phonetext.getWindowToken(), 0);



            for(int i=0;i<contact_list.size();i++)
            {
                Contact contact=contact_list.get(i);
                System.out.println(contact.getContact_name());
            }
if(contact_list.size()>0) {
    ListAdapter contactlist = new add_contact_list_adapter(getActivity(), contact_list);
    relationlist.setAdapter(contactlist);
}
        }





    public void addToRelation(Contact contact1, Contact contact2) {
        ArrayList<Contact> rel_list = contact1.getRelation();

        for (int j = 0; j < rel_list.size(); j++) {
            if (rel_list.get(j).getContact_name().equals(contact2.getContact_name()) || contact1.getContact_name().equals(contact2.getContact_name())) {
                return;
            }
        }
        contact1.add_relation(contact2);
        contact2.add_relation(contact1);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){

            Log.e("On Config Change","LANDSCAPE");

        }else{
            setRetainInstance(true);
           /* Intent intent = new Intent(this, contactdetails_activity.class);
            intent.putExtra("relation_list",relation_list);


            intent.putExtra("name", name_to_display);
            intent.putExtra("number", num_to_display);

            startActivity(intent);*/
            Log.e("On Config Change","PORTRAIT");
        }
    }
}

