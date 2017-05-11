package com.sasousuke.birthday;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.provider.ContactsContract.Data;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ListActivity{
	public ArrayList<Persona> contactDatos;
	public final String fieldContacID = Data.RAW_CONTACT_ID;
	public final String fieldContacDisplayName = Data.DISPLAY_NAME;
	public final String fieldContacBirthdayDate = Event.START_DATE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		contactDatos = new ArrayList<Persona>();
		this.ShowListContactBirthDay();
	}
	
	/*
	* Metodo que se invoca para obtener los datos y visualizarlos en un listado 
	* se invoca en los eventos onCreate y onResume que no es más cuando se ejecuta
	* la app y cuando se restaura despues de haber pasado a segundo plano
	*/
	public void ShowListContactBirthDay (){
        Date birthday = null;
        int pos;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Cursor cur = getContentResolver().query(Data.CONTENT_URI,
		        new String[] {fieldContacID, fieldContacDisplayName, fieldContacBirthdayDate},
				Data.MIMETYPE + "='" + Event.CONTENT_ITEM_TYPE + "' AND " + Event.TYPE + "='" + Event.TYPE_BIRTHDAY + "'"
		        , null, null);
		contactDatos.clear();
		if (cur.moveToFirst())
		{
			do
			{
		        try {
					birthday = sdf.parse(cur.getString(cur.getColumnIndex(fieldContacBirthdayDate)));
				} catch (ParseException e) {
					e.printStackTrace();
				}
		        Persona p = new Persona(
						Integer.valueOf(cur.getString(cur.getColumnIndex(fieldContacID))),
						cur.getString(cur.getColumnIndex(fieldContacDisplayName)),
						birthday);
				/*
				* Se agregan los objetos Persona en la lista. La posición de insercion es determinada
				* por los dias que le faltan para el siguiente cumpleaños
				*/
		        pos = -1;
		        for (int i = 0; i < contactDatos.size(); i ++)
					// Verificar que los dias que faltan es menor que alguno de los ya insertado
		        	if (contactDatos.get(i).dayForNextBirthday() > p.dayForNextBirthday()){
						// Se guarda la posicion 
		        		pos = i;
		        		//Log.i("TRACEON", "Posicion: " + i + " viejo:" + contactDatos.get(i).dayforbirth + " nuevo: " + p.dayforbirth );
		        		break;
		        	}
		        if (pos == -1)
		        	contactDatos.add(p);
		        else
		        	contactDatos.add(pos, p);
			} while (cur.moveToNext());
		}
		else
			Toast.makeText(getApplicationContext(), 
					getResources().getString(R.string.text_no_contact), Toast.LENGTH_LONG).show();
	    BirthdayAdapter adapter = new BirthdayAdapter(MainActivity.this, contactDatos);
	    this.setListAdapter(adapter);
		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onListItemClick(ListView parent, View v, int position, long id){
		/*
		 * Toast.makeText(getApplicationContext(),
		 * getResources().getString(R.string.text_selected_item) + " "+ position, Toast.LENGTH_SHORT).show(); 
		 */
				
	}
	
	@Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cmd_exit:
                // Launching preference activity
            	System.exit(0);
                break;
            case R.id.about_of:
            	Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            	startActivity(intent);
            	break;
            default:
            	String Texto = item.getTitle().toString();
            	Toast.makeText(getApplicationContext(), Texto, Toast.LENGTH_LONG).show();
                break;
        }
        return super.onMenuItemSelected(featureId, item);
    }
	
	@Override
	public void onResume (){
		super.onResume();
		this.ShowListContactBirthDay();
	}

}
