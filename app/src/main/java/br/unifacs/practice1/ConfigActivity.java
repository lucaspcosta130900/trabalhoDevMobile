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

public class ConfigActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        Button botaoConfirma=(Button) findViewById(R.id.buttonConfirmPassword);
        Button botaoCancel=(Button) findViewById(R.id.buttonCancelPassword);
        botaoConfirma.setOnClickListener(this);
        botaoCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonConfirmPassword:
                EditText editTextUsuario=(EditText) findViewById(R.id.editTextChangeUser);
                EditText editTextSenha1=(EditText) findViewById(R.id.editTextChangePassword);
                EditText editTextSenha2=(EditText) findViewById(R.id.editTextConfirmPassword);
                if (editTextUsuario.getText().toString().equals("") ||
                        editTextSenha1.getText().toString().equals("")) {
                    Toast.makeText(this,"Usuário/Senha não podem ser nulos",Toast.LENGTH_LONG).show();
                }
                else {
                    if (!editTextSenha1.getText().toString().equals(editTextSenha2.getText().toString())) {
                        Toast.makeText(this,"As senhas tem que ser iguais",Toast.LENGTH_LONG).show();
                    }
                    else {
                        String novoUsuario=editTextUsuario.getText().toString();
                        String novaSenha=editTextSenha1.getText().toString();
                        SharedPreferences sharedPrefs=getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);;
                        SharedPreferences.Editor sharedPrefsEditor=sharedPrefs.edit();
                        if (sharedPrefsEditor!=null) {
                            sharedPrefsEditor.putString("Usuario", novoUsuario);
                            sharedPrefsEditor.putString("Senha", novaSenha);
                            sharedPrefsEditor.commit();
                            finish();
                        }
                    }
                }
                break;
            case R.id.buttonCancelPassword:
                finish();
                break;
        }
    }
}