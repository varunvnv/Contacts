package com.example.varun.assignment2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.R.attr.data;

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
        String filename = "myfile.txt";



        try {
            if(contact_list.size()==0) {
                deleteFile(filename);
            }
            String n="\n";
            FileOutputStream fos = new FileOutputStream(new File(getFilesDir(),filename));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            for(int i=0;i<contact_list.size();i++) {
                Contact c=contact_list.get(i);
                oos.write(c.getContact_name().getBytes());
                oos.write(n.getBytes());
                oos.write(c.getContact_name().getBytes());
                oos.write(n.getBytes());
                oos.writeObject(c.getRelation());
                oos.writeObject(n.getBytes());
            }
                oos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("stopped");
    }
    @Override
    public void onStart() {
        // always call super
        String filename = "myfile.txt";
        try {
            super.onStart();
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            while(ois.readObject()!=null) {
                Contact c = new Contact();
                String name=(String)ois.readObject();
                c.setContact_name(name);
                String number=(String)ois.readObject();
                c.setContact_number(number);
                ArrayList<Contact>l=(ArrayList<Contact>)ois.readObject();
                for(int i=0;i<l.size();i++)
                {
                    c.add_relation(l.get(i));
                }

                contact_list.add(c);
            }
            ois.close();
            initiate();
        }
        catch (FileNotFoundException e) {
            super.onStart();
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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
        File f = getFileStreamPath("myfile.txt");
        if (f.length() == 0) {
            deleteFile("myfile.txt");
        }

        System.out.println("destroyed");
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview = (ListView) findViewById(R.id.listview);
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
}
