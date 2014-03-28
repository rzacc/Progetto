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
    //Il LocationManager
    LocationManager lm;
    //Il provider dei servizi di localizzazione
    String provider;
    //Il secondo provider (utilizzato quando si localizza sia tramite GPS che tramite network)
    String provider2;
    //Il LocationListener
    LocationListener locListen;
    boolean localizzazioneOn = false;

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
        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        //Oggetto per ricevere aggiornamenti sulla posizione dal LocationManager
        locListen = new LocListener();

        /*Location loc = lm.getLastKnownLocation("network");
        tvLatitude.setText(Double.toString(loc.getLatitude()));
        tvLongitude.setText(Double.toString(loc.getLongitude())); */

        //Definizione listener per il Button "Avvia localizzazione"
        avviaButton.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) throws IllegalArgumentException{
                        try {
                            //chiedi al LocationManager di inviare aggiornamenti sulla posizione
                            lm.requestLocationUpdates(provider, 30000L, 2.0f, locListen);
                            lm.requestLocationUpdates(provider2, 30000L, 2.0f, locListen);
                            localizzazioneOn = true;

                        }catch(IllegalArgumentException e){localizzazioneOn = false; /* mostrare un messaggio di errore */ }
                        }

                        }

        );

        //Definizione listener per il Button "Termina localizzazione"
        terminaButton.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lm.removeUpdates(locListen);
                        localizzazioneOn = false;
                    }
                }
        );

    }

    public void onGPSCheckboxClicked(View view) throws IllegalArgumentException{
        try {
      /*  //Chiedi attivazione GPS se non attivo
        boolean gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        //Creare una finestra che chiede all'utente di attivare il GPS */
            if(localizzazioneOn==false) {
                //La checkbox è spuntata?
                boolean gpsChecked = ((CheckBox) view).isChecked();
                //E' spuntata anche la checkbox "Network"?
                final CheckBox networkCB = (CheckBox) findViewById(R.id.networkCheckBox);
                boolean networkChecked = networkCB.isChecked();
                if (gpsChecked && networkChecked) {
                    provider = "gps";
                    provider2 = "network";
                } else if (gpsChecked) {
                    provider = "gps";
                    provider2 = "gps";
                } else if (networkChecked) {
                    provider = "network";
                    provider2 = "network";
                } else {
                    provider = null;
                    provider2 = null;
                }
            }
            else {
                //modifica a runtime la modalità di localizzazione

                //La checkbox è spuntata?
                boolean gpsChecked = ((CheckBox) view).isChecked();
                //E' spuntata anche la checkbox "Network"?
                final CheckBox networkCB = (CheckBox) findViewById(R.id.networkCheckBox);
                boolean networkChecked = networkCB.isChecked();
                if (gpsChecked && networkChecked) {
                    lm.removeUpdates(locListen); //ferma gli aggiornamenti della posizione
                    //aggiorna i provider
                    provider = "gps";
                    provider2 = "network";
                    //richiedi aggiornamenti della posizione
                    lm.requestLocationUpdates(provider, 30000L, 2.0f, locListen);
                    lm.requestLocationUpdates(provider2, 30000L, 2.0f, locListen);
                    localizzazioneOn = true;
                } else if (gpsChecked) {
                    lm.removeUpdates(locListen); //ferma gli aggiornamenti della posizione
                    //aggiorna i provider
                    provider = "gps";
                    provider2 = "gps";
                    //richiedi aggiornamenti della posizione
                    lm.requestLocationUpdates(provider, 30000L, 2.0f, locListen);
                    lm.requestLocationUpdates(provider2, 30000L, 2.0f, locListen);
                    localizzazioneOn = true;
                } else if (networkChecked) {
                    lm.removeUpdates(locListen); //ferma gli aggiornamenti della posizione
                    //aggiorna i provider
                    provider = "network";
                    provider2 = "network";
                    //richiedi aggiornamenti della posizione
                    lm.requestLocationUpdates(provider, 30000L, 2.0f, locListen);
                    lm.requestLocationUpdates(provider2, 30000L, 2.0f, locListen);
                    localizzazioneOn = true;
                } else {
                    provider = null;
                    provider2 = null;
                }
            }
        }catch(IllegalArgumentException e){localizzazioneOn = false; /* mostra un messaggio di errore */ }
    }

    public void onNetworkCheckboxClicked(View view)throws IllegalArgumentException{
        try{
        if(localizzazioneOn == false) {
            //La checkbox è spuntata?
            boolean networkChecked = ((CheckBox) view).isChecked();
            //E' spuntata anche la checkbox "GPS"?
            final CheckBox gpsCB = (CheckBox) findViewById(R.id.gpsCheckBox);
            boolean gpsChecked = gpsCB.isChecked();
            if (networkChecked && gpsChecked) {
                provider = "network";
                provider2 = "gps";
            } else if (networkChecked) {
                provider = "network";
                provider2 = "network";
            } else if (gpsChecked) {
                provider = "gps";
                provider2 = "gps";
            } else {
                provider = null;
                provider2 = null;
            }
        }
            else{
            //modifica a runtime la modalità di localizzazione

            //La checkbox è spuntata?
            boolean networkChecked = ((CheckBox) view).isChecked();
            //E' spuntata anche la checkbox "GPS"?
            final CheckBox gpsCB = (CheckBox) findViewById(R.id.gpsCheckBox);
            boolean gpsChecked = gpsCB.isChecked();

            if (networkChecked && gpsChecked) {
                lm.removeUpdates(locListen); //ferma gli aggiornamenti della posizione
                //aggiorna i provider
                provider = "network";
                provider2 = "gps";
                //richiedi aggiornamenti della posizione
                lm.requestLocationUpdates(provider, 30000L, 2.0f, locListen);
                lm.requestLocationUpdates(provider2, 30000L, 2.0f, locListen);
                localizzazioneOn = true;
            } else if (networkChecked) {
                lm.removeUpdates(locListen); //ferma gli aggiornamenti della posizione
                //aggiorna i provider
                provider = "network";
                provider2 = "network";
                //richiedi aggiornamenti della posizione
                lm.requestLocationUpdates(provider, 30000L, 2.0f, locListen);
                lm.requestLocationUpdates(provider2, 30000L, 2.0f, locListen);
                localizzazioneOn = true;
            } else if (gpsChecked) {
                lm.removeUpdates(locListen); //ferma gli aggiornamenti della posizione
                //aggiorna i provider
                provider = "gps";
                provider2 = "gps";
                //richiedi aggiornamenti della posizione
                lm.requestLocationUpdates(provider, 30000L, 2.0f, locListen);
                lm.requestLocationUpdates(provider2, 30000L, 2.0f, locListen);
                localizzazioneOn = true;
            } else {
                provider = null;
                provider2 = null;
            }
        }
        }catch(IllegalArgumentException e){localizzazioneOn = false; /*mostra messaggio di errore */}
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

