package nightout.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.MapView;

import nightout.app.entity.Place;

public class PlaceViewFragment extends Fragment {

    public PlaceViewFragment() {
    }

    TextView tvPlaceName, tvPlaceDesc, tvTypePlace, tvMusic, tvPlaceItemAddress;
    ImageView ivPlace;
    Button btnClose;
    AppDatabase database;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        database = AppDatabase.getInstance(getContext());

        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_place, container, false);
        tvPlaceName = v.findViewById(R.id.tvPlaceItemName);
        tvPlaceDesc = v.findViewById(R.id.tvPlaceItemDesc);
        tvMusic = v.findViewById(R.id.tvMusic);
        tvTypePlace = v.findViewById(R.id.tvTypePlace);
        tvPlaceItemAddress = v.findViewById(R.id.tvPlaceItemAddres);
        btnClose = v.findViewById(R.id.btnClose);
        ivPlace = v.findViewById(R.id.ivPlace);

        int itemPlaceId = getArguments() != null ? getArguments().getInt("itemPlace") : 0;
        Place p = database.placeDao().loadPlaceById(itemPlaceId);

        tvPlaceName.setText(p.namePlace);
        tvPlaceDesc.setText(p.description);
        tvTypePlace.setText(p.typePlace);
        tvMusic.setText(p.music);
        tvPlaceItemAddress.setText(p.address);
        ivPlace.setImageBitmap(stringToBitmap(p.pictureName));

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });

        return v;
    }

    private static Bitmap stringToBitmap(String encodedString) {
        try {
            byte[] encodedByte = Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodedByte, 0, encodedByte.length);
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
}
