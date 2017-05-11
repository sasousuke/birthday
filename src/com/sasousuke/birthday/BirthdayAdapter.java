package com.sasousuke.birthday;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BirthdayAdapter extends BaseAdapter {
	
	private Activity activity;
	private static LayoutInflater inflater=null;
	private ArrayList<Persona> data;
	
	public BirthdayAdapter (Activity a, ArrayList<Persona> d){
		activity = a;
		data = d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View vi = convertView;
        if(convertView == null)
            vi = inflater.inflate(R.layout.list_row, null);

        TextView artistText = (TextView)vi.findViewById(R.id.artist);
        TextView titleText = (TextView)vi.findViewById(R.id.title);
        TextView datebirthday = (TextView)vi.findViewById(R.id.datebirthday);
        TextView dayfornext = (TextView)vi.findViewById(R.id.dayfornext);
        ImageView thumbImage = (ImageView)vi.findViewById(R.id.list_image);
        
        Persona hero = data.get(position);
                        
        // Setting all values in listView
        String ts;
        int ti;
        
        titleText.setText(hero.nombre);
        
        ts = "";
        ti = hero.age();
        if (ti == 0)
        	ts = activity.getApplicationContext().getResources().getString(R.string.text_no_year);
        else
        	if (ti == 1)
        		ts = ti + " " + activity.getApplicationContext().getResources().getString(R.string.text_year);
        	else
        		ts = ti + " " + activity.getApplicationContext().getResources().getString(R.string.text_years);
        artistText.setText(ts);
        
        ts = "";
        ti = hero.dayForNextBirthday();
        
        if (ti == 0)
        	ts = activity.getApplicationContext().getResources().getString(R.string.text_today);
        else {
        	if (ti == 1)
        		ts = activity.getApplicationContext().getResources().getString(R.string.text_tomorrow);
        	else
        		ts = ti + " " + activity.getApplicationContext().getResources().getString(R.string.text_days);
        	ts = activity.getApplicationContext().getResources().getString(R.string.text_next) + ": " + ts;
        }
        dayfornext.setText(ts);
        
        datebirthday.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(hero.nacimiento));
        
        thumbImage.setImageBitmap(
        		BitmapFactory.decodeStream(
        				openPhoto(
        						Integer.valueOf(
        								hero.id)
        )));
        return vi;
	}
	
	/*
	 * Retrieving the thumbnail - sized photo
	 * @param contacId ID of contact
	 * @return thumbnail photo 
	 */
	public InputStream openPhoto(long contactId) {
	     Uri contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId);
	     Uri photoUri = Uri.withAppendedPath(contactUri, Contacts.Photo.CONTENT_DIRECTORY);
	     Cursor cursor = activity.getContentResolver().query(photoUri,
	          new String[] {android.provider.ContactsContract.CommonDataKinds.Photo.PHOTO}, null, null, null);
	     if (cursor == null) {
	         return null;
	     }
	     try {
	         if (cursor.moveToFirst()) {
	             byte[] data = cursor.getBlob(0);
	             if (data != null) {
	                 return new ByteArrayInputStream(data);
	             }
	         }
	     } finally {
	         cursor.close();
	     }
	     return null;
	 }

}
