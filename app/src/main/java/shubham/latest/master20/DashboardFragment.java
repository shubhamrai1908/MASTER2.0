package shubham.latest.master20;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_dashboard, container, false);
         //TextInputLayout textInputLayout= requireView().findViewById(R.id.menu);
        final SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        TextView textView=rootView.findViewById(R.id.tv);
        AutoCompleteTextView autoCompleteTextView= rootView.findViewById(R.id.serversim);
        AutoCompleteTextView autoCompleteTextView1= rootView.findViewById(R.id.groundsim);
        ArrayList<String> sim_cards=new ArrayList<>();
        sim_cards.add("SIM-1");
        sim_cards.add("SIM-2");
        ArrayAdapter<String> stringArrayAdapter=new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_item,sim_cards);
        autoCompleteTextView.setAdapter(stringArrayAdapter);
        autoCompleteTextView1.setAdapter(stringArrayAdapter);
        //autoCompleteTextView.setThreshold(1);
        int pos = preferences.getInt("SelectedSim", -1);
        if(pos>=0)
            autoCompleteTextView.setText(sim_cards.get(pos),false);

        int pos1 = preferences.getInt("SelectedSim1", -1);
        if(pos1>=0)
            autoCompleteTextView1.setText(sim_cards.get(pos1),false);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                preferences.edit().putInt("SelectedSim",position).apply();
                Toast.makeText(getActivity(),sim_cards.get(position)+" is Selected as Default sim",Toast.LENGTH_SHORT).show();
            }


        });

        autoCompleteTextView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                preferences.edit().putInt("SelectedSim1",position).apply();
                Toast.makeText(getActivity(),sim_cards.get(position)+" is Selected as Default sim",Toast.LENGTH_SHORT).show();
            }


        });
        return rootView;
    }
}