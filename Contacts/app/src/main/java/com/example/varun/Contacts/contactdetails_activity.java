package com.example.varun.Contacts;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class contactdetails_activity extends AppCompatActivity {

    TextView name_display_text,phone_display_text;
    ArrayList<Contact>relation_list=new ArrayList<>();
    String name_to_display;
    String num_to_display;
    ListView relationlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactdetails_activity);
        Intent intent = getIntent();

        name_display_text=(TextView)findViewById(R.id.name_display_text);
        phone_display_text=(TextView)findViewById(R.id.phone_display_text);
        relationlist=(ListView)findViewById(R.id.relationlist);

relation_list=(ArrayList<Contact>)intent.getSerializableExtra("relation_list");
         name_to_display=intent.getStringExtra("name");
        num_to_display=intent.getStringExtra("number");


        name_display_text.setText(name_to_display);
        phone_display_text.setText(num_to_display);

        ListAdapter relationcontactlist=new contact_profile_list_adapter(this,relation_list);
        relationlist.setAdapter(relationcontactlist);

        relationlist.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> list,
                                            View row,
                                            int index,
                                            long rowID) {
                        contactdetails(index);
                    }
                }
        );
    }
    public void contactdetails(int index) {
        Intent intent = new Intent(this, contactdetails_activity.class);

        String name = relation_list.get(index).getContact_name();
        String number = relation_list.get(index).getContact_number();

        intent.putExtra("name", name);
        intent.putExtra("number", number);

        ArrayList<Contact> relation=relation_list.get(index).getRelation();

        intent.putExtra("relation_list", relation);

        startActivity(intent);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
System.out.println("sending contact");
            Log.e("On Config Change","LANDSCAPE");
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("name_of_action","show_contact");
            intent.putExtra("name", name_to_display);
            intent.putExtra("number", num_to_display);
            intent.putExtra("rel_list_to",relation_list);
            startActivity(intent);
        }else{

            Log.e("On Config Change","PORTRAIT");
        }
    }
}
