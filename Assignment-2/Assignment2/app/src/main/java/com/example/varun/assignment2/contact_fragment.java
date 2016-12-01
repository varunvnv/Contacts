package com.example.varun.assignment2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class contact_fragment extends Fragment {
    private Activity myActivity;

    TextView name_display_text,phone_display_text;
    ArrayList<Contact>relation_list=new ArrayList<>();
    String name_to_display;
    String num_to_display;
    ListView relationlist;
    public contact_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_contact_fragment, container, false);

        relationlist=(ListView)view.findViewById(R.id.relationlist);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myActivity = getActivity();

        Bundle bundle=getArguments();
        if(bundle!=null)
        {
            Log.d("Bundle","BUNDLE NOT NULL");
            name_to_display=bundle.getString("name");
            System.out.println(name_to_display);
            num_to_display=bundle.getString("number");
            relation_list=(ArrayList<Contact>)bundle.getSerializable("rel_list");
        }
        else{
            Log.d("Bundle","BUNDLE IS NULL");
        }

        name_display_text=(TextView)myActivity.findViewById(R.id.name_display_text);
        phone_display_text=(TextView)myActivity.findViewById(R.id.phone_display_text);

        if(name_display_text!=null) {
            name_display_text.setText(name_to_display);
            phone_display_text.setText(num_to_display);
        }

            ListAdapter rel_list_adapter = new contact_profile_list_adapter(getActivity(), relation_list);
            relationlist.setAdapter(rel_list_adapter);

            relationlist.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> list,
                                                View row,
                                                int index,
                                                long rowID) {
                            Contact tem_contact = relation_list.get(index);
                            String name = tem_contact.getContact_name();
                            String number = tem_contact.getContact_number();
                            ArrayList<Contact> rel = tem_contact.getRelation();

                            contact_fragment frag = new contact_fragment();
                            android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            Bundle bun = new Bundle();
                            bun.putString("name", name);
                            bun.putString("number", number);
                            bun.putSerializable("rel_list", rel);
                            transaction.replace(R.id.frame, frag);
                            transaction.addToBackStack(null);
                            frag.setArguments(bun);
                            transaction.commit();
                        }
                    }
            );
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