package be.howest.nmct.politiecontroles.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Niels on 28/04/2014.
 */
public class SnelheidsControlesResult {
    @SerializedName("veiligheid_bemande_snelheidsmetingen_2012_wgs84")
    public List<SnelheidsControle> controles;
}
