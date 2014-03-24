package com.example.GeolocalizzazioneTest;

import android.app.Activity;
import android.content.Context;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import java.util.ArrayList;

import android.widget.CheckBox;
import android.location.LocationManager;
import android.location.Location;
import android.location.LocationListener;

public class MainActivity extends Activity {

    //L'ArrayList per contenere gli elementi della lista
    ArrayList<PosizioneCorrente> posizione = new ArrayList<PosizioneCorrente>();
    //L'ArrayAdapter
    ArrayAdapter<PosizioneCorrente> adapter;
    //Il provider dei servizi di localizzazione
    String provider;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //Creo un adapter per collegare l'ArrayList alla ListView
        adapter = new ArrayAdapter<PosizioneCorrente>(this, android.R.layout.simple_list_item_1, posizione);
        //Recupero il riferimento alla ListView
        final ListView listView = (ListView) findViewById(R.id.resultList);
        //Collego l'adapter alla ListView
        listView.setAdapter(adapter);

        //Ottengo i riferimenti alle altre componenti della UI
        final Button avviaButton = (Button) findViewById(R.id.avvia);
        final Button terminaButton = (Button) findViewById(R.id.termina);
        final CheckBox networkCB = (CheckBox) findViewById(R.id.networkCheckBox);
        final CheckBox gpsCB = (CheckBox) findViewById(R.id.gpsCheckBox);

        //final TextView tvLatitude = (TextView)findViewById(R.id.tvLatitude);
        //final TextView tvLongitude = (TextView)findViewById(R.id.tvLongitude);

        //Ottengo l'handle per il LocationManager
        final LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        //Oggetto per ricevere aggiornamenti sulla posizione dal LocationManager
        final LocationListener locListen = new LocListener();

        /*Location loc = lm.getLastKnownLocation("network");
        tvLatitude.setText(Double.toString(loc.getLatitude()));
        tvLongitude.setText(Double.toString(loc.getLongitude())); */

        //Definizione listener per il Button "Avvia localizzazione"
        avviaButton.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            //Connessione al Network location service
                            Location loc = lm.getLastKnownLocation(provider);
                            //Inserisci voce nella lista
                            PosizioneCorrente pos = new PosizioneCorrente(Double.toString(loc.getLatitude()), Double.toString(loc.getLongitude()));
                            posizione.add(pos);
                            adapter.notifyDataSetChanged();
                            //chiedi al LocationManager di inviare aggiornamenti sulla posizione
                            lm.requestLocationUpdates(provider, 30000L, 2.0f, locListen);

                        }

                        }

        );

        //Definizione listener per il Button "Termina localizzazione"
        terminaButton.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }
        );

    }

    public void onGPSCheckboxClicked(View view){
      /*  //Chiedi attivazione GPS se non attivo
        boolean gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        //Creare una finestra che chiede all'utente di attivare il GPS */

        //La checkbox è spuntata?
        boolean checked = ((CheckBox)view).isChecked();
        if(checked){provider = "gps";}
    }

    public void onNetworkCheckboxClicked(View view){
        //La checkbox è spuntata?
        boolean checked = ((CheckBox)view).isChecked();
        if(checked){provider = "network";}
    }

    private class LocListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            //Inserisci voce nella lista
            PosizioneCorrente pos = new PosizioneCorrente(Double.toString(location.getLatitude()), Double.toString(location.getLongitude()));
            posizione.add(pos);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

}

