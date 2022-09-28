package nightout.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import nightout.app.entity.Event;
import nightout.app.entity.Place;


public class ListFragment extends Fragment {
    EventAdapter eventAdapter;
    RecyclerView recyclerView;
    List<Event> events = new ArrayList<>();
    ListFragment listFragment;
    LoginFragment loginFragment;
    boolean showButtons;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    AppDatabase database;
    int idPlaceLogged;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        sharedPreferences = this.getActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        showButtons = sharedPreferences.getBoolean("isLogged", false);
        idPlaceLogged = sharedPreferences.getInt("idPlaceLogged", 0);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        database = AppDatabase.getInstance(getContext());
        events = database.eventDao().getAllEvents();

        eventAdapter = new EventAdapter(events, database);
        listFragment = new ListFragment();
        loginFragment = new LoginFragment();

        View v = inflater.inflate(R.layout.fragment_list, container, false);
        recyclerView = v.findViewById(R.id.recyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(eventAdapter);

        Button btnUser = v.findViewById(R.id.btnUser);
        Button btnPlace = v.findViewById(R.id.btnPlace);
        ImageButton btnLogout = v.findViewById(R.id.btnLogout);


        //if we are logged, buttons will hide
        if(showButtons) {
            btnPlace.setVisibility(View.INVISIBLE);
            btnUser.setVisibility(View.INVISIBLE);
        }

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("isLogged", false);
                editor.putString("userDataForLog", null);
                editor.apply();
                showButtons = false;
                AddNewFragment addNewFragment = new AddNewFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                if(isTablet(view)) {
                    transaction.add(R.id.frameLeft, listFragment);
                    transaction.replace(R.id.frameRight, addNewFragment);
                } else {
                    //phone
//                    btnLogout.setVisibility(View.INVISIBLE);
                    transaction.replace(R.id.frame, listFragment);
                    transaction.addToBackStack(null);
                }
                transaction.commit();
            }
        });

        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putBoolean("userFlag", true);
                loginFragment.setArguments(bundle);
                if(isTablet(v)) {
                    //tablet
                    transaction.add(R.id.frameLeft, listFragment);
                    transaction.replace(R.id.frameRight, loginFragment);
                } else {
                    //phone
                    transaction.replace(R.id.frame, loginFragment);
                    transaction.addToBackStack(null);
                }
                transaction.commit();
            }
        });

        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putBoolean("placeFlag", true);
                loginFragment.setArguments(bundle);
                if(isTablet(v)) {
                    //tablet
                    transaction.add(R.id.frameLeft, listFragment);
                    transaction.add(R.id.frameRight, loginFragment);
                } else {
                    //phone
                    transaction.replace(R.id.frame, loginFragment);
                    transaction.addToBackStack(null);
                }
                transaction.commit();
            }
        });

        return v;
    }

    public void addEvent(Event event) {
        eventAdapter.eventList.add(event);
        recyclerView.getAdapter().notifyItemInserted(eventAdapter.eventList.size() - 1);
    }

    private boolean isTablet(View v) {
        return v.findViewById(R.id.frameLeft) != null && v.findViewById(R.id.frameRight) != null;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        AddNewFragment addNewFragment = new AddNewFragment();
        AboutFragment aboutFragment = new AboutFragment();
        switch (item.getItemId()) {
            case R.id.add_event:
                if(idPlaceLogged != 0 && showButtons) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("idPlace", idPlaceLogged);
                    addNewFragment.setArguments(bundle);
                    transaction.replace(R.id.frame, addNewFragment);
                    transaction.addToBackStack("addEvent");
                    addNewFragment.setCancelVisible(true);
                    transaction.commit();
                } else {
                    Toast.makeText(getActivity(), "Niste prijavljeni kao vlasnik lokala.", Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.settings:
                Toast.makeText(getActivity(), "Podesavanja", Toast.LENGTH_LONG).show();
                return true;
            case R.id.about:
                transaction.replace(R.id.frame, aboutFragment);
                transaction.addToBackStack("about");
                transaction.commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}