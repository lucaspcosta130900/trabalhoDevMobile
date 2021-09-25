package br.unifacs.AVN1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioButton;

public class ConfigActivity extends AppCompatActivity {

    private RadioButton radioDec, radioMinDec, radioKmh, radioMph, radioNone, radioNorthUp, radioVet, radioSat, radioMSDec, radioCourseUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        //Associando botoes de radio ||
        radioDec = findViewById(R.id.radioBtnDec);
        radioMinDec = findViewById(R.id.radioBtnMinDec);
        radioKmh = findViewById(R.id.radioBtnKmh);
        radioMph = findViewById(R.id.radioBtnMph);
        radioNone = findViewById(R.id.radioBtnNone);
        radioNorthUp = findViewById(R.id.radioBtnNorthUp);
        radioNone = findViewById(R.id.radioBtnNone);
        radioNorthUp = findViewById(R.id.radioBtnNorthUp);
        radioVet = findViewById(R.id.radioBtnVet);
        radioSat = findViewById(R.id.radioBtnSat);
        radioMSDec = findViewById(R.id.radioBtnMSDec);
        radioCourseUp = findViewById(R.id.radioBtnCourseUp);

        //Persistindo escolhas dos botões de rádio em SharedPreferences ||
        radioDec.setOnCheckedChangeListener((buttonView, radioDec_isChecked)
                -> SaveIntoSharedPrefs("radioDec", radioDec_isChecked));

        radioMinDec.setOnCheckedChangeListener((buttonView, radioMinDec_isChecked)
                -> SaveIntoSharedPrefs("radioMinDec", radioMinDec_isChecked));

        radioKmh.setOnCheckedChangeListener((buttonView, radioKmh_isChecked)
                -> SaveIntoSharedPrefs("radioKmh", radioKmh_isChecked));

        radioMph.setOnCheckedChangeListener((buttonView, radioMph_isChecked)
                -> SaveIntoSharedPrefs("radioMph", radioMph_isChecked));

        radioNone.setOnCheckedChangeListener((buttonView, radioNone_isChecked)
                -> SaveIntoSharedPrefs("radioNone", radioNone_isChecked));

        radioNorthUp.setOnCheckedChangeListener((buttonView, radioNorthUp_isChecked)
                -> SaveIntoSharedPrefs("radioNorthUp", radioNorthUp_isChecked));

        radioVet.setOnCheckedChangeListener((buttonView, radioVet_isChecked)
                -> SaveIntoSharedPrefs("radioVet", radioVet_isChecked));

        radioSat.setOnCheckedChangeListener((buttonView, radioSat_isChecked)
                -> SaveIntoSharedPrefs("radioSat", radioSat_isChecked));
        radioMSDec.setOnCheckedChangeListener((buttonView, radioMSDec_isChecked)
                -> SaveIntoSharedPrefs("radioMSDec", radioMSDec_isChecked));
        radioCourseUp.setOnCheckedChangeListener((buttonView, radioCourseUp_isChecked)
                -> SaveIntoSharedPrefs("radioCourseUp", radioCourseUp_isChecked));

        //Recuperando estados dos botoes de radio salvos no SharedPreferences ||
        radioDec.setChecked(Update("radioDec"));
        radioMinDec.setChecked(Update("radioMinDec"));
        radioKmh.setChecked(Update("radioKmh"));
        radioMph.setChecked(Update("radioMph"));
        radioNone.setChecked(Update("radioNone"));
        radioNorthUp.setChecked(Update("radioNorthUp"));
        radioNone.setChecked(Update("radioNone"));
        radioNorthUp.setChecked(Update("radioNorthUp"));
        radioVet.setChecked(Update("radioVet"));
        radioSat.setChecked(Update("radioSat"));
        radioMSDec.setChecked(Update("radioMSDec"));
        radioCourseUp.setChecked(Update("radioCourseUp"));
    }

    //Metodo que salva os dados no SharedPreferences ||
    private void SaveIntoSharedPrefs(String key, boolean value) {
        SharedPreferences sp = getSharedPreferences("Seetings", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    //Metodo que busca os dados no SharedPreferences para atualizar os estados dos botoes de radio ||
    private boolean Update(String key) {
        SharedPreferences sp = getSharedPreferences("Seetings", MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }
}