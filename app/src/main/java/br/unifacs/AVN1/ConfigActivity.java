package br.unifacs.AVN1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class ConfigActivity extends AppCompatActivity {

    RadioGroup groupCoordenada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        groupCoordenada = findViewById(R.id.radioGroup);

        //Associando botoes de radio ||
        RadioButton radioDec = findViewById(R.id.radioBtnDec);
        RadioButton radioMinDec = findViewById(R.id.radioBtnMinDec);
        RadioButton radioKmh = findViewById(R.id.radioBtnKmh);
        RadioButton radioMph = findViewById(R.id.radioBtnMph);
        RadioButton radioNone = findViewById(R.id.radioBtnNone);
        RadioButton radioNorthUp = findViewById(R.id.radioBtnNorthUp);
        RadioButton radioVet = findViewById(R.id.radioBtnVet);
        RadioButton radioSat = findViewById(R.id.radioBtnSat);
        RadioButton radioMSDec = findViewById(R.id.radioBtnMSDec);
        RadioButton radioCourseUp = findViewById(R.id.radioBtnCourseUp);
        //RadioButton radioOn = findViewById(R.id.radioBtnOn);
        //RadioButton radioOff = findViewById(R.id.radioBtnOff);
        Switch switchOnOff = findViewById(R.id.switch1);

        //Persistindo escolhas dos botões de rádio em SharedPreferences ||
        radioDec.setOnCheckedChangeListener((buttonView, radioDec_isChecked)
                -> SaveIntoSharedPrefs("radioDec", radioDec_isChecked));

        radioMinDec.setOnCheckedChangeListener((buttonView, radioMinDec_isChecked)
                -> SaveIntoSharedPrefs("radioMinDec", radioMinDec_isChecked));

        radioMSDec.setOnCheckedChangeListener((buttonView, radioMSDec_isChecked)
                -> SaveIntoSharedPrefs("radioMSDec", radioMSDec_isChecked));

        radioKmh.setOnCheckedChangeListener((buttonView, radioKmh_isChecked)
                -> SaveIntoSharedPrefs("radioKmh", radioKmh_isChecked));

        radioMph.setOnCheckedChangeListener((buttonView, radioMph_isChecked)
                -> SaveIntoSharedPrefs("radioMph", radioMph_isChecked));

        radioNone.setOnCheckedChangeListener((buttonView, radioNone_isChecked)
                -> SaveIntoSharedPrefs("radioNone", radioNone_isChecked));

        radioNorthUp.setOnCheckedChangeListener((buttonView, radioNorthUp_isChecked)
                -> SaveIntoSharedPrefs("radioNorthUp", radioNorthUp_isChecked));

        radioCourseUp.setOnCheckedChangeListener((buttonView, radioCourseUp_isChecked)
                -> SaveIntoSharedPrefs("radioCourseUp", radioCourseUp_isChecked));

        radioVet.setOnCheckedChangeListener((buttonView, radioVet_isChecked)
                -> SaveIntoSharedPrefs("radioVet", radioVet_isChecked));

        radioSat.setOnCheckedChangeListener((buttonView, radioSat_isChecked)
                -> SaveIntoSharedPrefs("radioSat", radioSat_isChecked));

        switchOnOff.setOnCheckedChangeListener((buttonView, isChecked)
                ->SaveIntoSharedPrefs("switchOnOff", isChecked));
        //radioOn.setOnCheckedChangeListener((buttonView, radioOn_isChecked)
        //        -> SaveIntoSharedPrefs("radioOn", radioOn_isChecked));

        //radioOff.setOnCheckedChangeListener((buttonView, radioOff_isChecked)
        //       -> SaveIntoSharedPrefs("radioOff", radioOff_isChecked));

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
        switchOnOff.setChecked(Update("switchOnOff"));
        //radioOn.setChecked(Update("radioOn"));
        //radioOff.setChecked(Update("radioOff"));
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