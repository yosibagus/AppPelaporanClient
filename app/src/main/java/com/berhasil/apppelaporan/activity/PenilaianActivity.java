package com.berhasil.apppelaporan.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.berhasil.apppelaporan.R;
import com.berhasil.apppelaporan.api.BaseApiService;
import com.berhasil.apppelaporan.api.RestClient;
import com.berhasil.apppelaporan.utils.SharePrefManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PenilaianActivity extends AppCompatActivity {
    String tmpKategoriId;
    String tmpKtpUser;
    String tmpLaporanId;
    float ratingTmp;

    @BindView(R.id.rating)
    RatingBar rating;
    @BindView(R.id.ratingTmp)
    TextView tmpRating;
    @BindView(R.id.etKritikSaran)
    EditText kritiksaran;
    @BindView(R.id.btnInput)
    Button btnInput;

    BaseApiService mApiService;
    Context mContext;
    SharePrefManager sharePrefManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penilaian);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent in = getIntent();
        tmpKategoriId = in.getStringExtra("kategoriId");
        tmpLaporanId = in.getStringExtra("laporanId");
        getSupportActionBar().setTitle("Penilaian");
        ButterKnife.bind(this);
        mContext =this;
        mApiService = RestClient.getApiService();
        sharePrefManager = new SharePrefManager(this);
        tmpKtpUser = sharePrefManager.getSpNoKtp();


        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingTmp = rating;
                if (rating < 2) {
                    tmpRating.setText("Tidak Puas");
                } else if (rating < 3) {
                    tmpRating.setText("Cukup Puas");
                } else if (rating < 4){
                    tmpRating.setText("Puas");
                } else {
                    tmpRating.setText("Sangat Puas");
                }
//                tmpRating.setText(Float.toString(rating));
            }
        });

        btnInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPenilaianUser();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addPenilaianUser() {
        Call<ResponseBody> rest = mApiService.addRating(ratingTmp, kritiksaran.getText().toString(), tmpKategoriId, tmpKtpUser, Integer.parseInt(tmpLaporanId));
        rest.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(mContext, "Penilaian Berhasil Dikirim", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(mContext, "Penilaian Gagal Dikirim", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
