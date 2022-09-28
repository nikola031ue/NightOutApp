package nightout.app;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.Calendar;

import nightout.app.entity.Event;
import nightout.app.entity.Place;

public class AddNewFragment extends Fragment {

    public AddNewFragment() { }
    TextView tvNamePlace, tvPerformer;
    static TextView tvDate, tvTime;
    Button btnDate, btnTime, btnCancel, btnAdd;
    private EditText etPlaceName, etPerformer;
    int idPlaceLogged;
    private boolean cancelVisible;
    AppDatabase database;



    public void setCancelVisible(boolean cancelVisible) {
        this.cancelVisible = cancelVisible;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        idPlaceLogged = getArguments().getInt("idPlace");

        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_add_event, container, false);
        tvNamePlace = v.findViewById(R.id.tvNamePlace);
        tvPerformer = v.findViewById(R.id.tvPerformer);
        tvDate = (TextView) v.findViewById(R.id.tvDate);
        tvTime = v.findViewById(R.id.tvTime);
        btnAdd = v.findViewById(R.id.btnAdd);
        btnCancel = v.findViewById(R.id.btnCancel);
        btnDate = v.findViewById(R.id.btnDate);
        btnTime = v.findViewById(R.id.btnTime);
        etPerformer = v.findViewById(R.id.etPerformer);
        etPlaceName = v.findViewById(R.id.etPlaceName);
        database = AppDatabase.getInstance(container.getContext());
        if(!this.cancelVisible) {
            btnCancel.setVisibility(View.INVISIBLE);
        }
        Place p = database.placeDao().loadPlaceById(idPlaceLogged);
        etPlaceName.setText(p.namePlace);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Event e = new Event();
                e.setPerformer(etPerformer.getEditableText().toString());
//                Place p = database.placeDao().loadPlaceById(idPlaceLogged);
                e.setEventPlaceId(idPlaceLogged);
                e.setDate(tvDate.getText().toString());
                e.setTime(tvTime.getText().toString());

                database.eventDao().insertEvent(e);
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                ListFragment listFragment = new ListFragment();
                transaction.replace(R.id.frame, listFragment);
                transaction.commit();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getParentFragmentManager().popBackStack();
                if (!isTablet(view)){
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    ListFragment listFragment = new ListFragment();
                    transaction.replace(R.id.frame, listFragment);
                    transaction.commit();
                }
            }
        });

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getParentFragmentManager(), "datePicker");
            }
        });
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getParentFragmentManager(), "timePicker");
            }
        });

        return v;
    }

    private boolean isTablet(View v) {
        return v.findViewById(R.id.frameLeft) != null && v.findViewById(R.id.frameRight) != null;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        AddNewFragment addNewFragment = new AddNewFragment();
        AboutFragment aboutFragment = new AboutFragment();

        switch (item.getItemId()) {
            case R.id.add_event:
                transaction.replace(R.id.frame, addNewFragment);
                transaction.addToBackStack("addEvent");
                addNewFragment.setCancelVisible(true);
                transaction.commit();
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

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(requireContext(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            tvDate.setText(day + "." + month + "." + year + ".");
        }
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            tvTime.setText(hourOfDay + ":" + minute);
        }
    }


}
