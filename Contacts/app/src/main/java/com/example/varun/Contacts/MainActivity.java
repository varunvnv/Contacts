package com.example.varun.Contacts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements add_contact_list_fragment.get_updated_list{
    ListView listview;
    Button add_button;
    Button delete_button;
    Contact contact;
    ArrayList<Contact>contact_list=new ArrayList<>();
    private static final int REQ_CODE = 123;


    @Override
    public void onPause() {
        super.onPause(); // always call super
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putString("killed", "yes");

        System.out.println("paused");
    }
    @Override
    public void onResume() {
        super.onResume(); // always call super
        System.out.println("rseumed");
    }
    @Override
    public void onStop() {
        super.onStop(); // always call super

        System.out.println("stopped");
    }
    @Override
    public void onStart() {
        // always call super
        try{
            super.onStart();
            SharedPreferences sp=getSharedPreferences("userinfo",Context.MODE_PRIVATE);
            int i=0;
            while(sp.contains("name"+i))
            {
                Contact c1=new Contact();
                c1.setContact_name(sp.getString("name"+i,null));
                c1.setContact_number(sp.getString("number"+i,null));
                contact_list.add(c1);
            }
            for(int j=0;j<i;j++)
            {
                Contact c1=new Contact();
                c1=contact_list.get(j);
                Set<String>tempset=new LinkedHashSet<>();
                tempset=sp.getStringSet("rel"+j,null);
                ArrayList<String>t=new ArrayList<>();
                t.addAll(tempset);

                for(int k=0;k<t.size();k++)
                {
                    String name=t.get(k);
                    for(int l=0;l<contact_list.size();l++) {
                        Contact c2 = new Contact();
                        c2 = contact_list.get(l);

                        if(c2.getContact_name().equals(name))
                        {
                            addToRelation(c1,c2);
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            super.onStart();
        }
        System.out.println("started");
    }
    @Override
    public void onRestart() {
        super.onRestart(); // always call super
        System.out.println("restarted");
    }
    @Override
    public void onDestroy() {
        super.onDestroy(); // always call super
        android.os.Debug.stopMethodTracing();
        SharedPreferences sp=getSharedPreferences("userinfo",Context.MODE_PRIVATE);
        SharedPreferences.Editor ed=sp.edit();
        ed.clear();

        for(int i=0;i<contact_list.size();i++)
        {
            ed.putString("name"+i,contact_list.get(i).getContact_name().toString());
        }
        for (int j=0;j<contact_list.size();j++)
        {
            ed.putString("number"+j,contact_list.get(j).getContact_name().toString());
        }

        Set<String>tempset=new LinkedHashSet<>();
        for (int k=0;k<contact_list.size();k++)
        {
            ArrayList<Contact>t=contact_list.get(k).getRelation();
            ArrayList<String> temp=new ArrayList<>();
            for(int l=0;l<t.size();l++)
            {
                temp.add(t.get(l).getContact_name().toString());
            }
            tempset.addAll(temp);
            ed.putStringSet("rel"+k,tempset);
            tempset.clear();
        }
        ed.apply();
        System.out.println("destroyed");
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview = (ListView) findViewById(R.id.listview);
        populate();
        if(getIntent().getExtras()!=null)
        {

            for(int i=0;i<contact_list.size();i++)
            {
                System.out.println(contact_list.get(i).getContact_name());
            }

            Intent intent=getIntent();
            String name=intent.getStringExtra("name_of_action");
            if(name!=null){
                if(name.equals("add_contact"))
                {

                    System.out.println("Sending data to add_contact fragment");
                    add_contact_list_fragment frag=new add_contact_list_fragment();
                    android.app.FragmentTransaction transaction=getFragmentManager().beginTransaction();
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("contact_list",contact_list);
                    transaction.replace(R.id.frame,frag);
                    transaction.addToBackStack(null);
                    frag.setArguments(bundle);
                    transaction.commit();
                }
                else{
                    String name_to_display=intent.getStringExtra("name");
                    String num_to_display=intent.getStringExtra("number");
                    ArrayList<Contact> rel_list=(ArrayList<Contact>)intent.getSerializableExtra("rel_list_to");
                    System.out.println("name to display: "+name_to_display);
                    contact_fragment frag = new contact_fragment();
                    android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    Bundle bun = new Bundle();
                    bun.putString("name", name_to_display);
                    bun.putString("number", num_to_display);
                    bun.putSerializable("rel_list", rel_list);
                    transaction.replace(R.id.frame, frag);
                    transaction.addToBackStack(null);
                    frag.setArguments(bun);
                    transaction.commit();
                }

            }}

        add_button = (Button) findViewById(R.id.add_button);
        delete_button = (Button) findViewById(R.id.delete_button);

        if(savedInstanceState!=null)
        {
            contact_list=(ArrayList<Contact>)savedInstanceState.getSerializable("list");
            ListAdapter contactlist_adapter = new contactlist_adapter(this, contact_list);
            listview.setAdapter(contactlist_adapter);

        }

        listview.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> list,
                                            View row,
                                            int index,
                                            long rowID) {
                        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                            landscapeview_contactprofile(index);
                        }
                        else {
                            contactdetails(index);
                        }
                    }
                }
        );

        add_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                add();
            }
        });


    }

    public void populate(){
        ListAdapter contactlist_adapter = new contactlist_adapter(this, contact_list);
        listview.setAdapter(contactlist_adapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        Log.d("onSaveInstanceState","onSaveInstanceState called");
        super.onSaveInstanceState(outState);
        outState.putSerializable("list",contact_list);
    }

    public void updated_list(ArrayList<Contact> list)
    {
        contact_list=list;

        System.out.println("After adding in landscapeview");
        for(int i=0;i<contact_list.size();i++)
        {
            Contact contact=contact_list.get(i);
            System.out.println(contact.getContact_name());
        }
        ListAdapter contactlist_adapter = new contactlist_adapter(this, contact_list);
        listview.setAdapter(contactlist_adapter);
    }

    public void landscapeview_contactprofile(int index)
    {
        if(contact_list.size()>0) {
            Contact tem_contact = contact_list.get(index);
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
        else{
            empty_contact_fragment frag=new empty_contact_fragment();
            android.app.FragmentTransaction transaction=getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame,frag);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }



    public void add() {

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            add_contact_list_fragment frag=new add_contact_list_fragment();
            android.app.FragmentTransaction transaction=getFragmentManager().beginTransaction();
            Bundle bundle=new Bundle();
            bundle.putSerializable("contact_list",contact_list);
            transaction.replace(R.id.frame,frag);
            transaction.addToBackStack(null);
            frag.setArguments(bundle);
            transaction.commit();
        }
        else{
            Intent intent = new Intent(this, addcontact_activity.class);
            intent.putExtra("contact_list",contact_list);
            startActivityForResult(intent, REQ_CODE);
        }
    }


    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQ_CODE) {

            contact_list = (ArrayList<Contact>) intent.getSerializableExtra("contact_list");

            System.out.println("After adding in portraitview");
            for(int i=0;i<contact_list.size();i++)
            {
                Contact contact=contact_list.get(i);
                System.out.println(contact.getContact_name());
            }

            ListAdapter contactlist_adapter = new contactlist_adapter(this, contact_list);
            listview.setAdapter(contactlist_adapter);
        }

    }
    public void initiate()
    {
        ListAdapter contactlist_adapter = new contactlist_adapter(this, contact_list);
        listview.setAdapter(contactlist_adapter);
    }

    public void contactdetails(int index) {
        Intent intent = new Intent(this, contactdetails_activity.class);
        Contact c1=contact_list.get(index);
        ArrayList<Contact>relation=new ArrayList<>();
        relation=c1.getRelation();
        intent.putExtra("relation_list",relation);


        String name_to_display=contact_list.get(index).getContact_name();
        String phone_num_to_display=contact_list.get(index).getContact_number();

        intent.putExtra("name", name_to_display);
        intent.putExtra("number", phone_num_to_display);

        startActivity(intent);
    }

    public void delete_click(View view) {

        for(int i=0;i<contact_list.size();i++)
        {
            if(contact_list.get(i).getSelectionforDeletion()==true)
            {
                for(int j=0;j<contact_list.size();j++)
                {
                    update_relationlist(contact_list.get(j),contact_list.get(i));
                }
                contact_list.remove(i);
            }
        }

        System.out.println("After deleting in portraitview");
        for(int i=0;i<contact_list.size();i++)
        {
            Contact contact=contact_list.get(i);
            System.out.println(contact.getContact_name());
        }
        ListAdapter contactlist_adapter = new contactlist_adapter(this, contact_list);
        listview.setAdapter(contactlist_adapter);
    }

    public void update_relationlist(Contact con,Contact c)
    {
        for(int j=0;j<c.getRelation().size();j++)
        {

            con.remove_relation(c);
        }
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
}
