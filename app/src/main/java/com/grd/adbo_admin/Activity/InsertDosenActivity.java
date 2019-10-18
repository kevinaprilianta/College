package com.grd.adbo_admin.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.grd.adbo_admin.Model.DosenModel;
import com.grd.adbo_admin.Model.ResultDosenModel;
import com.grd.adbo_admin.Model.ValueModel;
import com.grd.adbo_admin.R;
import com.grd.adbo_admin.Retrofit.ApiClient;
import com.grd.adbo_admin.Retrofit.RegisterAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsertDosenActivity extends AppCompatActivity {
    private EditText etNIP,etNama,etPin;
    private RadioGroup radioGroupjenis;
    private RadioButton rbjeniskelamin;
    private Button btnDaftar;
    private ProgressDialog progress;
    RegisterAPI mApiInterface;
    String nim, nama, jenis_kelamin,pin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_dosen);
        etNIP = (EditText) findViewById(R.id.editTextNIP);
        etNama = (EditText) findViewById(R.id.editTextNama);
        etPin = (EditText) findViewById(R.id.editTextPin);
        radioGroupjenis = (RadioGroup) findViewById(R.id.radioJenis);
        btnDaftar = (Button) findViewById(R.id.buttonDaftar);
        mApiInterface = ApiClient.getClient().create(RegisterAPI.class);
        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(etNIP.getText()) ||
                        TextUtils.isEmpty(etNama.getText()) ||
                        TextUtils.isEmpty(etPin.getText())) {
                    Toast.makeText(InsertDosenActivity.this,
                            "All column is required", Toast.LENGTH_SHORT).show();
                } else {
                    daftardosen();
                }
            }
        });
    }
    public void daftardosen() {
        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Loading ...");
        progress.show();

        nim = etNIP.getText().toString();
        nama = etNama.getText().toString();
        pin = etPin.getText().toString();
        int selectedId = radioGroupjenis.getCheckedRadioButtonId();
        // mencari id radio button
        rbjeniskelamin = (RadioButton) findViewById(selectedId);
        jenis_kelamin = rbjeniskelamin.getText().toString();
        Call<DosenModel> dosenModelCall =
                mApiInterface.daftar_dosen(nim,nama,jenis_kelamin,pin);
        dosenModelCall.enqueue(new Callback<DosenModel>() {
            @Override
            public void onResponse(Call<DosenModel> call, Response<DosenModel> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();
                progress.dismiss();
                if (value.equals("1")) {
                    Toast.makeText(InsertDosenActivity.this, message, Toast.LENGTH_SHORT).show();
                    etNIP.setText("");
                    etNama.setText("");
                    etPin.setText("");
                    rbjeniskelamin.setSelected(false);
                    startActivity(new Intent(InsertDosenActivity.this, ViewDosenActivity.class));
                    finish();
                } else {
                    Toast.makeText(InsertDosenActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DosenModel> call, Throwable t) {
                Toast.makeText(InsertDosenActivity.this, "Error connection!", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
