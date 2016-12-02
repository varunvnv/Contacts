package com.example.varun.Contacts;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class addcontact_activity extends AppCompatActivity {

    EditText nametext, phonetext;
    ArrayList<Contact> contact_list = new ArrayList<>();
    ListView relationlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcontact_activity);
        Intent intent = getIntent();

        nametext = (EditText) findViewById(R.id.nametext);
        phonetext = (EditText) findViewById(R.id.phonetext);
        relationlist = (ListView) findViewById(R.id.relationlist);

        contact_list = (ArrayList) intent.getSerializableExtra("contact_list");



    ListAdapter contactlist = new add_contact_list_adapter(this, contact_list);
    relationlist.setAdapter(contactlist);


    }


    public void addcontact_click(View view) {
        String name = nametext.getText().toString();
        String number = phonetext.getText().toString();

        Contact contact = new Contact();
        contact.setContact_name(name);
        contact.setContact_number(number);
        contact_list.add(contact);



    for (int i = 0; i < contact_list.size(); i++) {
        if (contact_list.get(i).getSelected() == true) {
            Contact c = contact_list.get(i);
            c.setSelected(false);
            addToRelation(contact, c);
        }

}
        Intent intent = new Intent();
        intent.putExtra("contact_list", contact_list);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void addToRelation(Contact contact1,Contact contact2) {
        ArrayList<Contact> rel_list = contact1.getRelation();

        for (int j = 0; j < rel_list.size(); j++) {
            if (rel_list.get(j).getContact_name().equals(contact2.getContact_name())||contact1.getContact_name().equals(contact2.getContact_name())) {
                return;
            }
        }
            contact1.add_relation(contact2);
        contact2.add_relation(contact1);
        System.out.println(contact2.getContact_name());


        }
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
System.out.println("orientation changed");
            Log.e("On Config Change","LANDSCAPE");

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("name_of_action","add_contact");
            startActivity(intent);

        }else{

            Log.e("On Config Change","PORTRAIT");
        }
    }
}


