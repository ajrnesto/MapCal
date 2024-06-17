package com.geogridconverter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore DB;
    FirebaseAuth AUTH;
    FirebaseUser USER;

    private void initializeFirebase() {
        DB = FirebaseFirestore.getInstance();
        AUTH = FirebaseAuth.getInstance();
        USER = AUTH.getCurrentUser();
    }
    ConstraintLayout clGeoToGrid, clGridToGeo;
    ScrollView svGeoToGrid, svGridToGeo;
    TabLayout tabLayout;
    TextInputEditText etLatDeg, etLatMin, etLatSec, etLongDeg, etLongMin, etLongSec, etEasting, etNorthing;
    AutoCompleteTextView menuZone, menuZone2;
    MaterialButton btnGeoToGrid, btnGridToGeo;
    TextView tvEasting, tvNorthing, tvLatitude, tvLongitude, tvCentralMeridian, tvCentralMeridian2;
    ArrayList<GeoLatRow> arrGeoLat;
    ArrayList<GridLatRow> arrGridLat;

    // Spinner items
    String[] itemsZone;
    ArrayAdapter<String> adapterZone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeFirebase();
        listenToStatusChanges();

        Gson gson = new Gson();
        GeoLatRow[] primitive_arrGeoLat = gson.fromJson(loadJSONFromAsset(getApplicationContext(), "geo_to_grid.json"), GeoLatRow[].class);
        arrGeoLat = new ArrayList<GeoLatRow>(Arrays.asList(primitive_arrGeoLat));

        Gson gson2 = new Gson();
        GridLatRow[] primitive_arrGridLat = gson2.fromJson(loadJSONFromAsset(getApplicationContext(), "grid_to_geo.json"), GridLatRow[].class);
        arrGridLat = new ArrayList<GridLatRow>(Arrays.asList(primitive_arrGridLat));

        clGeoToGrid = findViewById(R.id.clGeoToGrid);
        svGeoToGrid = findViewById(R.id.svGeoToGrid);
        clGridToGeo = findViewById(R.id.clGridToGeo);
        svGridToGeo = findViewById(R.id.svGridToGeo);
        tabLayout = findViewById(R.id.tabLayout);
        etLatDeg = findViewById(R.id.etLatDeg);
        etLatMin = findViewById(R.id.etLatMin);
        etLatSec = findViewById(R.id.etLatSec);
        etLongDeg = findViewById(R.id.etLongDeg);
        etLongMin = findViewById(R.id.etLongMin);
        etLongSec = findViewById(R.id.etLongSec);
        etEasting = findViewById(R.id.etEasting);
        etNorthing = findViewById(R.id.etNorthing);
        menuZone = findViewById(R.id.menuZone);
        menuZone2 = findViewById(R.id.menuZone2);
        btnGeoToGrid = findViewById(R.id.btnGeoToGrid);
        btnGridToGeo = findViewById(R.id.btnGridToGeo);
        tvEasting = findViewById(R.id.tvEasting);
        tvNorthing = findViewById(R.id.tvNorthing);
        tvLatitude = findViewById(R.id.tvLatitude);
        tvLongitude = findViewById(R.id.tvLongitude);
        tvCentralMeridian = findViewById(R.id.tvCentralMeridian);
        tvCentralMeridian2 = findViewById(R.id.tvCentralMeridian2);

        itemsZone = new String[]{"I", "II", "III", "IV", "V"};
        adapterZone = new ArrayAdapter<>(this, R.layout.list_item, itemsZone);
        menuZone.setAdapter(adapterZone);
        menuZone2.setAdapter(adapterZone);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    clGeoToGrid.setVisibility(View.VISIBLE);
                    svGeoToGrid.setVisibility(View.VISIBLE);
                    clGridToGeo.setVisibility(View.GONE);
                    svGridToGeo.setVisibility(View.GONE);
                }
                else {
                    clGeoToGrid.setVisibility(View.GONE);
                    svGeoToGrid.setVisibility(View.GONE);
                    clGridToGeo.setVisibility(View.VISIBLE);
                    svGridToGeo.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        etLatMin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (!Objects.requireNonNull(etLatMin.getText()).toString().isEmpty()) {
                    if (Integer.parseInt(s.toString()) < 0) {
                        etLatMin.setText("0");
                        etLatMin.setSelection(Objects.requireNonNull(etLatMin.getText()).length());
                    }
                    else if (Integer.parseInt(s.toString()) > 59) {
                        etLatMin.setText("59");
                        etLatMin.setSelection(Objects.requireNonNull(etLatMin.getText()).length());
                    }
                }
            }
        });

        etLongMin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (!Objects.requireNonNull(etLongMin.getText()).toString().isEmpty()) {
                    if (Integer.parseInt(s.toString()) < 0) {
                        etLongMin.setText("0");
                        etLongMin.setSelection(Objects.requireNonNull(etLongMin.getText()).length());
                    }
                    else if (Integer.parseInt(s.toString()) > 59) {
                        etLongMin.setText("59");
                        etLongMin.setSelection(Objects.requireNonNull(etLongMin.getText()).length());
                    }
                }
            }
        });

        menuZone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tvCentralMeridian.setText((position == 0) ? "117" : (position == 1) ? "119" : (position == 2) ? "121" : (position == 3) ? "123" : "125");
            }
        });

        menuZone2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tvCentralMeridian2.setText((position == 0) ? "117" : (position == 1) ? "119" : (position == 2) ? "121" : (position == 3) ? "123" : "125");
            }
        });

        btnGeoToGrid.setOnClickListener(view -> validateInputForGeoToGrid());
        btnGridToGeo.setOnClickListener(view -> validateInputForGridToGeo());
    }

    private void listenToStatusChanges() {
        DB.collection("users").document(AUTH.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                if (snapshot.getString("status").equals("SUSPENDED")) {
                    startActivity(new Intent(MainActivity.this, SuspendedActivity.class));
                    finish();
                }
            }
        });
    }

    private void validateInputForGridToGeo() {
        if (Objects.requireNonNull(etEasting.getText()).toString().isEmpty() ||
                Objects.requireNonNull(etNorthing.getText()).toString().isEmpty()) {
            Toast.makeText(this, "Invalid Input: All fields can't be left empty", Toast.LENGTH_LONG).show();
            return;
        }

        convertToGeo();
    }

    private void validateInputForGeoToGrid() {
        if (Objects.requireNonNull(etLatDeg.getText()).toString().isEmpty() ||
                Objects.requireNonNull(etLatMin.getText()).toString().isEmpty() ||
                Objects.requireNonNull(etLatSec.getText()).toString().isEmpty() ||
                Objects.requireNonNull(etLongDeg.getText()).toString().isEmpty() ||
                Objects.requireNonNull(etLongMin.getText()).toString().isEmpty() ||
                Objects.requireNonNull(etLongSec.getText()).toString().isEmpty()) {
            Toast.makeText(this, "Invalid Input: All fields can't be left empty", Toast.LENGTH_LONG).show();
            return;
        }

        if (Integer.parseInt(etLatDeg.getText().toString()) < 4) {
            Toast.makeText(this, "Invalid Input: Latitude can't be less than 4°", Toast.LENGTH_LONG).show();
            return;
        }
        else if (Integer.parseInt(etLatDeg.getText().toString()) > 21) {
            Toast.makeText(this, "Invalid Input: Latitude can't be more than 21°", Toast.LENGTH_LONG).show();
            return;
        }

        if (Integer.parseInt(etLongDeg.getText().toString()) < 116) {
            Toast.makeText(this, "Invalid Input: Longitude can't be less than 116°", Toast.LENGTH_LONG).show();
            return;
        }
        else if (Integer.parseInt(etLongDeg.getText().toString()) > 127) {
            Toast.makeText(this, "Invalid Input: Longitude can't be more than 127°", Toast.LENGTH_LONG).show();
            return;
        }

        convertToGrid();
    }

    private void convertToGeo() {
        // hide keyboard
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        double easting = Double.parseDouble(Objects.requireNonNull(etEasting.getText()).toString());
        double northing = Double.parseDouble(Objects.requireNonNull(etNorthing.getText()).toString());
        double centralMeridian2 = Double.parseDouble(Objects.requireNonNull(tvCentralMeridian2.getText()).toString());

        // final validation
        if (northing < 442254.016 || northing > 2431690.908) {
            tvLatitude.setText("Out of bounds");
            tvLongitude.setText("Out of bounds");
            return;
        }

        // search grid row index with corresponding lat deg and min
        int rowIndex = 0;
        for (int i = 0; i < arrGridLat.size(); i++) {
            if (arrGridLat.get(i).I > northing) {
                rowIndex = i-1;
                break;
            }
        }

        GridLatRow point1 = arrGridLat.get(rowIndex);
        double point1DegDec = (double)point1.LAT_DEG + ((double)point1.LAT_MIN / 60);
        GridLatRow point2 = arrGridLat.get(rowIndex+1);
        double point2DegDec = (double)point2.LAT_DEG + ((double)point2.LAT_MIN / 60);

        double p_prime = (((northing - point1.I) / (point2.I - point1.I)) * (point2DegDec - point1DegDec)) + point1DegDec;
        int p_prime_deg = (int) p_prime;
        double p_prime_min_dec =  (p_prime - p_prime_deg) * 60;
        int p_prime_min = (int) p_prime_min_dec;
        double p_prime_sec = (p_prime_min_dec - p_prime_min) * 60;
        double I = point1.I;
        double II = point1.I_DIFF;
        double VII = point1.VII;
        double VII_DIFF = point1.VII_DIFF;
        double VIII = point1.VIII;
        double IX = point1.IX;
        double IX_DIFF = point1.IX_DIFF;
        double X = point1.X;
        double X_DIFF = point1.X_DIFF;
        double XI = point1.XI;

        double vii = (p_prime_sec * VII_DIFF) + VII;
        double ix = (p_prime_sec * IX_DIFF) + IX;
        double x = (p_prime_sec * X_DIFF) + X;
        double q = 0.000001 * (easting - 500000);

        double latitude = p_prime - (vii * ((q * q) + (VIII * (q * q * q * q))) / 3600);
        int lat_deg = (int)latitude;
        double lat_min_dec = (latitude - lat_deg) * 60;
        int lat_min = (int)lat_min_dec;
        double lat_sec = (lat_min_dec - lat_min) * 60;

        double delta_lambda = ((ix * q) - (x * (q * q * q)) + (XI * (q * q * q * q * q)));
        double longitude = (delta_lambda / 3600) + centralMeridian2;
        int long_deg = (int)longitude;
        double long_min_dec = (longitude - long_deg) * 60;
        int long_min = (int)long_min_dec;
        double long_sec = (long_min_dec - long_min) * 60;

        tvLatitude.setText(lat_deg+"°\n"+lat_min+"\'\n"+lat_sec+"\"");
        tvLongitude.setText(long_deg+"°\n"+long_min+"\'\n"+long_sec+"\"");
    }

    private void convertToGrid() {
        // hide keyboard
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        int latDegree = Integer.parseInt(Objects.requireNonNull(etLatDeg.getText()).toString());
        int latMinute = Integer.parseInt(Objects.requireNonNull(etLatMin.getText()).toString());
        double latSecond = Double.parseDouble(Objects.requireNonNull(etLatSec.getText()).toString());
        int longDegree = Integer.parseInt(Objects.requireNonNull(etLongDeg.getText()).toString());
        int longMinute = Integer.parseInt(Objects.requireNonNull(etLongMin.getText()).toString());
        double longSecond = Double.parseDouble(Objects.requireNonNull(etLongSec.getText()).toString());
        double centralMeridian = Double.parseDouble(Objects.requireNonNull(tvCentralMeridian.getText()).toString());

        double latDecimal = Math.signum(Double.parseDouble(String.valueOf(latDegree))) * (Math.abs(Double.parseDouble(String.valueOf(latDegree))) + (Double.parseDouble(String.valueOf(latMinute)) / 60.0) + (Double.parseDouble(String.valueOf(latSecond)) / 3600.0));
        double longDecimal = Math.signum(Double.parseDouble(String.valueOf(longDegree))) * (Math.abs(Double.parseDouble(String.valueOf(longDegree))) + (Double.parseDouble(String.valueOf(longMinute)) / 60.0) + (Double.parseDouble(String.valueOf(longSecond)) / 3600.0));

        // search row with lat deg and min
        GeoLatRow point = null;
        for (int i = 0; i < arrGeoLat.size(); i++) {
            if (arrGeoLat.get(i).LAT_DEG == latDegree && arrGeoLat.get(i).LAT_MIN == latMinute) {
                point = arrGeoLat.get(i);
                break;
            }
        }

        double I = point.I;
        double I_DIFF = point.I_DIFF;
        double II = point.II;
        double II_DIFF = point.II_DIFF;
        double III = point.III;
        double IV = point.IV;
        double IV_DIFF = point.IV_DIFF;
        double V = point.V;
        double V_DIFF = point.V_DIFF;
        double VI = point.VI;

        double p = 0.0001 * ((longDecimal - centralMeridian) * 3600);
        double i = (latSecond * I_DIFF) + I;
        double ii = (latSecond * II_DIFF) + II;
        double iv = (latSecond * IV_DIFF) + IV;
        double v = (latSecond * V_DIFF) + V;
        double northing = i + (ii * (p * p)) + (III * (p * p * p * p));
        double easting = 500000 + ((iv * p) + (v * (p * p * p)) + (VI * (p * p * p * p * p)));

        tvEasting.setText(String.valueOf(easting));
        tvNorthing.setText(String.valueOf(northing));
    }

    public String loadJSONFromAsset(Context context, String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}