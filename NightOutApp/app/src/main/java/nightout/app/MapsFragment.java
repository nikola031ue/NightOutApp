package nightout.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsFragment extends Fragment {
    String address, placeName;
    GoogleMap mMap;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            address = getArguments() != null ? getArguments().getString("address") : null;
            placeName = getArguments() != null ? getArguments().getString("placeNameMap") : null;

            List<Address> addressList = null;
            MarkerOptions placeMarkerOptions = new MarkerOptions();
            if(!TextUtils.isEmpty(address)) {
                Geocoder geocoder = new Geocoder(getContext());
                try {
                    addressList = geocoder.getFromLocationName(address, 6);
                    if(addressList != null) {
                        for(int i=0; i < addressList.size(); i++) {
                            Address placeAddress = addressList.get(i);
                            LatLng latLang = new LatLng(placeAddress.getLatitude(), placeAddress.getLongitude());

                            placeMarkerOptions.position(latLang);
                            placeMarkerOptions.title(placeName);
                            placeMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                            mMap.addMarker(placeMarkerOptions);
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLang));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                        }
                    } else {
                        Toast.makeText(getContext(), "Nije pronadjena lokacija", Toast.LENGTH_LONG).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getContext(), "Nema adrese", Toast.LENGTH_LONG).show();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            checkUserLocationPermission();
//        }

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}