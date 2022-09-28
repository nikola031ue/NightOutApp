package nightout.app;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

import nightout.app.entity.User;

public class RegistrationUserFragment extends Fragment {

    private EditText etNameRegU, etUsernameRegU, etPasswordU;
    AppDatabase database;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register_user, container, false);
        etNameRegU = v.findViewById(R.id.etNameRegU);
        etUsernameRegU = v.findViewById(R.id.etUsernameRegU);
        etPasswordU = v.findViewById(R.id.etPasswordRegU);
        Button btnSaveUser = v.findViewById(R.id.btnSaveUser);
        Button btnCancelRegU = v.findViewById(R.id.btnCancelRegU);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        database = AppDatabase.getInstance(getContext());

        btnSaveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User u = new User();
                u.setUsername(etUsernameRegU.getText().toString().trim());
                u.setPassword(etPasswordU.getText().toString().trim());
                u.setName(etNameRegU.getText().toString().trim());
                database.userDao().insertUser(u);

                ListFragment listFragment = new ListFragment();
                SuccessFragment success = new SuccessFragment();
                if(isTablet(v)) {
                    //tablet
                    transaction.replace(R.id.frameLeft, listFragment);
                    transaction.replace(R.id.frameRight, success);

                } else {
                    //phone
                    transaction.replace(R.id.frame, listFragment);
                    transaction.addToBackStack(null);

                }
                transaction.commit();

            }
        });

        btnCancelRegU.setOnClickListener(new View.OnClickListener() {
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
