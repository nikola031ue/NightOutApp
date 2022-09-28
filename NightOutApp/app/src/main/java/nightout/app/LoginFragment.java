package nightout.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nightout.app.activity.MainActivity;
import nightout.app.entity.Place;
import nightout.app.entity.User;

public class LoginFragment extends Fragment {

    private ListFragment listFragment;
    private EditText etUsernameLogin, etPasswordLogin;
    private TextView tvMessageLogin;
    SharedPreferences sharedpreferences;
    AppDatabase database;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        sharedpreferences = this.getActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        etUsernameLogin = v.findViewById(R.id.etUsernameLogin);
        etPasswordLogin = v.findViewById(R.id.etPasswordLogin);
        tvMessageLogin = v.findViewById(R.id.tvMessageLogin);
        Button btnLogin = v.findViewById(R.id.btnLogin);
        Button btnRegister = v.findViewById(R.id.btnRegister);
        Button btnCancelLogin = v.findViewById(R.id.btnCancelLogin);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsernameLogin.getText().toString().trim();
                String password = etPasswordLogin.getText().toString().trim();

                SharedPreferences.Editor editor = sharedpreferences.edit();

                database = AppDatabase.getInstance(getContext());
                User user = database.userDao().loadUser(username, password);
                Place place = database.placeDao().loadPlace(username, password);

                if (user != null || place != null) {
                    //if we loggin successfully, then we will redirect on listFrame
                    editor.putBoolean("isLogged", true);
                    editor.putInt("idUserLogged", user != null ? user.idUser : 0);
                    editor.putInt("idPlaceLogged", place != null ? place.idPlace : 0);
                    editor.putString("userDataForLog", "" + (user != null ? user.username : null) + 
                            " " + (user != null ? user.password : null));
                    editor.apply();

                    listFragment = new ListFragment();
                    transaction.replace(R.id.frame, listFragment);
                    transaction.commit();
                } else {
                    //if login failed, then message would show
                    String message = "Nemate nalog ili su pogresni podaci. Probajte ponovo ili se registrujte.";
                    tvMessageLogin.setText(message);
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean userFlag = getArguments().getBoolean("userFlag");
                boolean placeFlag = getArguments().getBoolean("placeFlag");
                if(userFlag) {
                    //user register redirection
                    //TODO kad dodam usera ili place treba da se vrati nazad u listFrame i da se updateuje lista
                    RegistrationUserFragment regFrame = new RegistrationUserFragment();
                    if(isTablet(view)) {
                        transaction.replace(R.id.frameRight, regFrame);
                    } else {
                        transaction.replace(R.id.frame, regFrame);
                        transaction.addToBackStack(null);
                    }
                } else if(placeFlag) {
                    //place register redirection
                    RegistrationPlaceFragment regFrameP = new RegistrationPlaceFragment();
                    if(isTablet(view)) {
                        transaction.replace(R.id.frameRight, regFrameP);
                    } else {
                        transaction.replace(R.id.frame, regFrameP);
                        transaction.addToBackStack(null);
                    }
                }
                transaction.commit();
            }
        });

        btnCancelLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });

        return v;
    }

    private boolean isTablet(View v) {
        return v.findViewById(R.id.frameLeft) != null && v.findViewById(R.id.frameRight) != null;
    }
}