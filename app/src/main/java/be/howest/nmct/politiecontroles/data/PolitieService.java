package be.howest.nmct.politiecontroles.data;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Niels on 28/04/2014.
 */
public class PolitieService {
    private static PolitieService service=new PolitieService();
    private static final String url="http://data.drk.be/kortrijk/veiligheid_bemande_snelheidsmetingen_2012_wgs84.json";
    private List<SnelheidsControle> controles;
    private PolitieService(){

    }

    public static PolitieService getInstance()
    {
        return service;
    }

    public List<SnelheidsControle> getControles(){
        if(controles==null)
        {
            controles = getRemoteControles();
        }
        return new ArrayList<SnelheidsControle>(controles);
    }

    private List<SnelheidsControle> getRemoteControles() {
        List<SnelheidsControle> result = new ArrayList<SnelheidsControle>();
        try{
            InputStream source = new URL(url).openStream();
            Gson gson =new Gson();
            Reader reader = new InputStreamReader(source);
            result = gson.fromJson(reader, SnelheidsControlesResult.class).controles;
            reader.close();
            source.close();
        }catch(IOException ex)
        {
            ex.printStackTrace();
        }
        return result;
    }
}
