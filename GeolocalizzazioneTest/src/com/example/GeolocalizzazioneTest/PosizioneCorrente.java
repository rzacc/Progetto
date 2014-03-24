package com.example.GeolocalizzazioneTest;

public class PosizioneCorrente{
    private String latitude;
    private String longitude;

    public PosizioneCorrente(String latitude, String longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString(){
        return "Latitude: " + latitude + "  Longitude: " + longitude;
    }
}