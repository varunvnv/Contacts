package com.example.varun.Contacts;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by varun on 10/19/2016.
 */

public class Contact implements Serializable {
    private String contact_name;
    private  String contact_number;
    private ArrayList<Contact> relation=new ArrayList<>();
    boolean selected=false;
    boolean selectionfordeletion=false;

    public void setContact_name(String name)
    {
        contact_name=name;
    }
    public void setContact_number(String number)
    {
        contact_number=number;
    }
    public String getContact_name()
    {
        return contact_name;
    }
    public String getContact_number()
    {
        return  contact_number;
    }
    public void setSelected(boolean val)
    {
        selected=val;
    }
    public boolean getSelected()
    {
        return selected;
    }
    public void setSelectedforDeletion(){selectionfordeletion=true;}
    public boolean getSelectionforDeletion(){return selectionfordeletion;}

    public void add_relation(Contact contact)
    {
        System.out.println("Adding "+contact.getContact_name()+" to "+getContact_name());
        relation.add(contact);
    }

    public ArrayList<Contact> getRelation()
    {
        return relation;
    }

    public void remove_relation(Contact con)
    {
     for(int i=0;i<relation.size();i++)
     {
         String name1=relation.get(i).getContact_name();
         System.out.println(name1);

         String name2=con.getContact_name();
         System.out.println(name2);
         if(name1.equals(name2))
         {
             System.out.println("removing "+con.getContact_name());
             relation.remove(i);
         }
     }

        for(int j=0;j<relation.size();j++) {
            System.out.println("after update "+relation.get(j).getContact_name());
        }
        }


}
