package com.example.loginappefe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity {
    Button btn_sign_out, btnSendApi;
    TextView txtDolar, txtEuro, txtUtm, txtUf, txtBitcoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        txtUf = findViewById(R.id.txtUf);
        txtDolar = findViewById(R.id.txtDolar);
        txtEuro = findViewById(R.id.txtEuro);
        txtUtm = findViewById(R.id.txtUtm);
        txtBitcoin = findViewById(R.id.txtBitcoin);

        btn_sign_out = findViewById(R.id.btn_sign_out);
        btnSendApi = findViewById(R.id.btnSendApi);
        btn_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(HomeActivity.this, "Sesión Cerrada", Toast.LENGTH_SHORT).show();
                goLogin();
            }
        });
        btnSendApi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDataApi();
                Toast.makeText(HomeActivity.this, "Los valores son mostrados en pasos chilenos, bitcoin en dólares.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void goLogin() {
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    private void getDataApi() {
        String url = "https://mindicador.cl/api";
        StringRequest postRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String uf = getString(R.string.UF) +": "+ "$" + jsonObject.getJSONObject("uf").getString("valor");
                    String dolar = getString(R.string.dolar)+": " + "$" + jsonObject.getJSONObject("dolar").getString("valor");
                    String euro = getString(R.string.euro)+": " + "€" + jsonObject.getJSONObject("euro").getString("valor");
                    String utm = getString(R.string.utm)+": " + "$" + jsonObject.getJSONObject("utm").getString("valor");
                    String bit = getString(R.string.bitDolar)+": " + "$" + jsonObject.getJSONObject("bitcoin").getString("valor");
                    txtUf.setText(uf);
                    txtUtm.setText(utm);
                    txtDolar.setText(dolar);
                    txtEuro.setText(euro);
                    txtBitcoin.setText(bit);
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(this).add(postRequest);
    }
}