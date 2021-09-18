package br.unifacs.practice1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor sharedPrefsEditor;
    private int tentativas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPrefs=getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        tentativas=sharedPrefs.getInt("Tentativas", 0);
        if (tentativas>=5) {
            blockApp();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button botaoOk= (Button) findViewById(R.id.buttonOk);
        Button botaoCancel = (Button) findViewById(R.id.buttonCancel);
        botaoOk.setOnClickListener(this);
        botaoCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonOk:
                VerifyUserAndPassword();
                break;
            case R.id.buttonCancel:
                finish();
                break;
        }
    }
    private void VerifyUserAndPassword() {
        EditText edittextUser=(EditText) findViewById((R.id.editTextUser));
        EditText edittextPassword=(EditText) findViewById((R.id.editTextPassword));
        String usuarioInformado=edittextUser.getText().toString();
        String senhaInformada=edittextPassword.getText().toString();
        String usuario=sharedPrefs.getString("Usuario", "admin");
        String senha=sharedPrefs.getString("Senha", "admin");
        if (usuario.equals(usuarioInformado) && senha.equals(senhaInformada)) {
            tentativas=0;
            sharedPrefsEditor=sharedPrefs.edit();
            if (sharedPrefsEditor!=null) {
                sharedPrefsEditor.putInt("Tentativas", tentativas);
                sharedPrefsEditor.commit();
            }
            Intent i=new Intent(this,MenuActivity.class);
            startActivity(i);
            finish();
        }
        else {
            tentativas++;
            sharedPrefsEditor=sharedPrefs.edit();
            if (sharedPrefsEditor!=null) {
                sharedPrefsEditor.putInt("Tentativas", tentativas);
                sharedPrefsEditor.commit();
            }
            if (tentativas==3) {
                blockApp();
            }
            else {
                edittextPassword.setText("");
                Toast.makeText(this,"Você tem somente "+(3-tentativas)+" antes do bloqueio",Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void blockApp() {
        Intent i=new Intent(this,BlockActivity.class);
        startActivity(i);
        finish();
    }
}