package be.howest.nmct.politiecontroles.app;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import be.howest.nmct.politiecontroles.data.Maand;
import be.howest.nmct.politiecontroles.data.PolitieService;
import be.howest.nmct.politiecontroles.data.SnelheidsControle;

import java.util.ArrayList;
import java.util.List;

public class ControlesFragment extends ListFragment {
    public final static String Tag="ListFragment";
    private OnFragmentInteractionListener mListener;
    private List<SnelheidsControle> controles=new ArrayList<SnelheidsControle>();
    private List<SnelheidsControle> filteredcontroles=new ArrayList<SnelheidsControle>();
    private SnelheidsControlesAdapter adapter;
    private int selectedItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ControlesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: Change Adapter to display your content
        adapter=new SnelheidsControlesAdapter(getActivity(),R.layout.row_controle,filteredcontroles);
        setListAdapter(adapter);

        setRetainInstance(true);

        new SnelheidsControlesAsyncTask().execute();
        Log.d(Tag,"onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_controles, container, false);
        Log.d(Tag,"onCreateView");
        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                + " must implement OnFragmentInteractionListener");
        }
        Log.d(Tag,"onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        Log.d(Tag,"onDetach");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(Tag,"onDestroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(Tag,"onDestroyView");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(Tag,"onStop");
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        selectedItem=position;
       //v.setBackgroundResource(R.color.blue);
        adapter.notifyDataSetChanged();
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onControleSelected(filteredcontroles.get(position));
        }
    }

    public void onControlesChanged(List<SnelheidsControle> controles){
        if(mListener!=null)
        {
            mListener.onControlesChanged(new ArrayList<SnelheidsControle>(controles));
        }
    }

    public void filter(int i) {
        filteredcontroles.clear();
        if(i==0)
        {
            filteredcontroles.addAll(controles);
        }else {
            for (SnelheidsControle controle : controles) {
                if (controle.maand == i)
                    filteredcontroles.add(controle);
            }
        }
        onControlesChanged(filteredcontroles);
        if(adapter!=null)
            adapter.notifyDataSetChanged();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onControlesChanged(List<SnelheidsControle> controles);
        public void onControleSelected(SnelheidsControle controle);
    }

    private class SnelheidsControlesAdapter extends ArrayAdapter<SnelheidsControle>{
        LayoutInflater inflater;
        int orange;
        int red;
        int black;
        int white;
        int blue;
        String aantal_controles;
        String aantal_overtredingen;
        public SnelheidsControlesAdapter(Context context, int resource, List<SnelheidsControle> objects) {
            super(context, resource, objects);
            inflater= (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            Resources res = getResources();
            orange=res.getColor(R.color.orange);
            red =res.getColor(R.color.red);
            black =res.getColor(R.color.black);
            white =res.getColor(android.R.color.white);
            blue=res.getColor(R.color.darkblue);
            aantal_controles=res.getString(R.string.aantal_controles);
            aantal_overtredingen=res.getString(R.string.aantal_overtredingen);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            SnelheidsControle controle=(SnelheidsControle)getItem(position);
            if(convertView==null)
            {
                convertView=inflater.inflate(R.layout.row_controle,parent,false);
                holder=new ViewHolder(
                        (TextView)convertView.findViewById(R.id.straat),
                        (TextView)convertView.findViewById(R.id.maand),
                        (TextView)convertView.findViewById(R.id.aantal_controles),
                    (TextView)convertView.findViewById(R.id.aantal_overtredingen),
                        convertView.findViewById(R.id.root)
                );
                convertView.setTag(holder);
                //viewholder
            }
            holder=(ViewHolder)convertView.getTag();
            float ratio = (float)controle.aantalOvertredingen/(float)controle.aantalControles;
            int backgroundcolor=white;
            int textcolor=black;

            if(ratio>=5&&ratio<10) {
               backgroundcolor=orange;
                textcolor=black;
            }else if(ratio>=20){
                backgroundcolor=red;
                textcolor=white;
            }
            if(selectedItem==position)
            {
                backgroundcolor=blue;
                textcolor=white;
            }

            holder.Root.setBackgroundColor(backgroundcolor);
            holder.Straat.setTextColor(textcolor);
            holder.Maand.setTextColor(textcolor);
            holder.Aantal_controles.setTextColor(textcolor);
            holder.Aantal_overtredingen.setTextColor(textcolor);

            holder.Straat.setText(controle.straat);
            holder.Maand.setText(Maand.getMaand(controle.maand).Name);
            holder.Aantal_controles.setText(aantal_controles+controle.aantalControles);
            holder.Aantal_overtredingen.setText(aantal_overtredingen+controle.aantalOvertredingen);
            return convertView;
        }



        private class ViewHolder {
            public TextView Straat;
            public TextView Maand;
            public TextView Aantal_controles;
            public TextView Aantal_overtredingen;
            public View Root;
            public ViewHolder(TextView straat,TextView maand,TextView aantal_controles,TextView aantal_overtredingen,View root)
            {
                Straat=straat;
                Maand=maand;
                Aantal_controles=aantal_controles;
                Aantal_overtredingen=aantal_overtredingen;
                Root=root;
            }
        }
    }

    private class SnelheidsControlesAsyncTask extends AsyncTask<Void,Void,List<SnelheidsControle>>{

        @Override
        protected List<SnelheidsControle> doInBackground(Void... params) {
            return PolitieService.getInstance().getControles();
        }

        @Override
        protected void onPostExecute(List<SnelheidsControle> snelheidsControles) {
            super.onPostExecute(snelheidsControles);
            controles.clear();
            controles.addAll(snelheidsControles);
            filteredcontroles.clear();
            filteredcontroles.addAll(snelheidsControles);
            adapter.notifyDataSetChanged();
            onControlesChanged(controles);
        }
    }
}
