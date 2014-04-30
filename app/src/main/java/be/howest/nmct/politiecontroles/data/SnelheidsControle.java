package be.howest.nmct.politiecontroles.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Niels on 28/04/2014.
 */
public class SnelheidsControle {
    public int jaar ;
    public int maand;
    public String straat;
    public String postcode;
    public String gemeente;
    @SerializedName("aantal_controles")
    public int aantalControles;
    @SerializedName("gepasseerde_voertuigen")
    public int gepasseerdeVoertuigen;
    @SerializedName("vtg_in_overtreding")
    public int aantalOvertredingen;
    @SerializedName("long")
    public float longitude;
    @SerializedName("lat")
    public float latititude;
}
