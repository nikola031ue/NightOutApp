package nightout.app;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import nightout.app.activity.PictureActivity;
import nightout.app.entity.Place;

public class RegistrationPlaceFragment extends Fragment {
    private EditText etNamePlaceReg, etUsernameRegP, etPasswordRegP, etMusicType, etPlaceType, etDescription, etAddressRegP;
    private String image;
    AppDatabase database;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register_place, container, false);
        etNamePlaceReg = v.findViewById(R.id.etNamePlaceReg);
        etUsernameRegP = v.findViewById(R.id.etUsernameRegP);
        etPasswordRegP = v.findViewById(R.id.etPasswordRegP);
        etMusicType = v.findViewById(R.id.etMusicType);
        etPlaceType = v.findViewById(R.id.etPlaceType);
        etDescription = v.findViewById(R.id.etDescriptionPlace);
        etAddressRegP = v.findViewById(R.id.etAddressRegP);
        Button btnSavePlace = v.findViewById(R.id.btnSavePlace);
        Button btnCancelRegP = v.findViewById(R.id.btnCancelRegP);
        Button btnPicture = v.findViewById(R.id.btnPicture);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        database = AppDatabase.getInstance(getContext());

        btnPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PictureActivity.class);

                startActivityForResult(intent, 1);
            }
        });

        btnSavePlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Place p = new Place();
                p.setNamePlace(etNamePlaceReg.getText().toString());
                p.setUsernamePlace(etUsernameRegP.getText().toString());
                p.setPasswordPlace(etPasswordRegP.getText().toString());
                p.setTypePlace(etPlaceType.getText().toString());
                p.setMusic(etMusicType.getText().toString());
                p.setDescription(etDescription.getText().toString());
                p.setAddress(etAddressRegP.getText().toString());
                p.setPictureName(image);
                database.placeDao().insertPlace(p);

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

        btnCancelRegP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if (requestCode == 1 && data != null) {
                image = data.getStringExtra("image");
            } else {
                image = null;
            }
        }
    }

    private boolean isTablet(View v) {
        return v.findViewById(R.id.frameLeft) != null && v.findViewById(R.id.frameRight) != null;
    }
}
