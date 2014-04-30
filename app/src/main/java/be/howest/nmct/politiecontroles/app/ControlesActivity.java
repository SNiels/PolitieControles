package be.howest.nmct.politiecontroles.app;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import be.howest.nmct.politiecontroles.data.Maand;
import be.howest.nmct.politiecontroles.data.SnelheidsControle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ControlesActivity extends ActionBarActivity implements ControlesFragment.OnFragmentInteractionListener, ActionBar.OnNavigationListener {
    private ControlesFragment controlesFragment;
    private GoogleMap mMap;
    private Map<SnelheidsControle,Marker> controleMarkers=new HashMap<SnelheidsControle, Marker>();
    private List<SnelheidsControle> filteredControles;
    private DrawerLayout drawer;
    private SnelheidsControle selectedControle;
    private final List<String> filters=new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controles);
        ActionBar bar = getSupportActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        filters.add("Alle maanden");
        for(Maand maand:Maand.values())
            filters.add(maand.Name);

        bar.setListNavigationCallbacks(
                new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,filters),this
        );
        drawer=(DrawerLayout)findViewById(R.id.drawer);

        if (savedInstanceState == null) {
            controlesFragment=new ControlesFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.listcontainer, controlesFragment)
                    .commit();
        }else{
            controlesFragment=(ControlesFragment)getSupportFragmentManager().findFragmentById(R.id.listcontainer);
        }
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null&&controleMarkers!=null&&filteredControles!=null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        mMap.clear();

        controleMarkers.clear();
        String aantal_overtredingen=getString(R.string.aantal_overtredingen);
        for(SnelheidsControle controle:filteredControles)
        {
            MarkerOptions options =new MarkerOptions().title(controle.straat).snippet(aantal_overtredingen + " " + controle.aantalOvertredingen).position(new LatLng(controle.latititude, controle.longitude));
            float fact;
            float ratio = (float)controle.aantalOvertredingen/(float)controle.aantalControles;
            if(ratio>=5&&ratio<10) {
                fact=BitmapDescriptorFactory.HUE_ORANGE;
            }else if(ratio>=20){
                fact=BitmapDescriptorFactory.HUE_RED;
            }else{
                fact=BitmapDescriptorFactory.HUE_YELLOW;
            }

         options.icon(BitmapDescriptorFactory.defaultMarker(fact));
            controleMarkers.put(controle,
                    mMap.addMarker(options)
            );
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.controles, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onControlesChanged(List<SnelheidsControle> controles) {
        filteredControles=controles;

        setUpMap();
        if(filteredControles.size()>0) {
            SnelheidsControle firstcontrole = filteredControles.get(0);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(firstcontrole.latititude, firstcontrole.longitude), 13));
        }
    }

    @Override
    public void onControleSelected(SnelheidsControle controle) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(controle.latititude,controle.longitude),15));
        if(selectedControle!=null)
        {
            Marker marker=controleMarkers.get(selectedControle);
            if(marker!=null)
                marker.hideInfoWindow();
        }
        selectedControle=controle;
        controleMarkers.get(controle).showInfoWindow();

        closeDrawers();
    }

    private void closeDrawers(){
        if(drawer!=null)
        {
            drawer.closeDrawer(Gravity.START);
        }
    }

    private void openDrawers(){
        if(drawer!=null)
        {
            drawer.openDrawer(Gravity.START);
        }
    }

    @Override
    public boolean onNavigationItemSelected(int i, long l) {
        controlesFragment.filter(i);
        return false;
    }
}
