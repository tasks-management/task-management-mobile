package com.duongll.succotask.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;

import com.duongll.succotask.R;
import com.duongll.succotask.api.UserApi;
import com.duongll.succotask.config.APIConfig;
import com.duongll.succotask.entity.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class ScanQRCodeActivity extends AppCompatActivity{

    private ImageView imgBarCode;
    private Button btnFindUser;
    private Long userId, teamId;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barcode_layout);
        Intent intent = this.getIntent();
        userId = intent.getLongExtra("user_id", new Long(0));
        role = intent.getStringExtra("role");
        if (!role.equals("admin")) {
            teamId = intent.getLongExtra("team_id", new Long(0));
        }
        btnFindUser = findViewById(R.id.btnFindUser);
        if (role.equals("user")) {
            btnFindUser.setVisibility(View.GONE);
            btnFindUser.setEnabled(false);
        }
        imgBarCode = findViewById(R.id.imgQrCode);
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(userId + "", BarcodeFormat.QR_CODE, 400,400);
            imgBarCode.setImageBitmap(bitmap);
        } catch (Exception e) {
            Log.e("Message", "Cannot generate bar code");
        }
    }

    public void clickToFindUserPage(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Retrofit retrofit = APIConfig.createRetrofitForAPI();
                UserApi userApi = APIConfig.getAPIFromClass(retrofit, UserApi.class);
                Long user;
                try {
                    user = Long.parseLong(result.getContents());
                } catch (Exception e) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ScanQRCodeActivity.this);
                    alertDialog.setTitle("Error Message");
                    alertDialog.setMessage("QR code not acceptable");
                    alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.show();
                    return;
                }
                Call<User> userCall = userApi.getUserInformation(user);
                final Intent intent = new Intent(ScanQRCodeActivity.this, QRCodeResultActivity.class);
                if (role.equals("admin")) {
                    userCall.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.code() == 200) {
                                User user = response.body();
                                intent.putExtra("user_id", user.getId());
                                intent.putExtra("role", user.getRole());
                                intent.putExtra("name", user.getName());
                                intent.putExtra("team_name", user.getTeamId().getName());
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ScanQRCodeActivity.this);
                            alertDialog.setTitle("Error Message");
                            alertDialog.setMessage("Cannot find that user with that qr code");
                            alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            alertDialog.show();
                        }
                    });
                } else {
                    userCall.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.code() == 200) {
                                if (response.body().getTeamId().getId() != teamId) {
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ScanQRCodeActivity.this);
                                    alertDialog.setTitle("Error Message");
                                    alertDialog.setMessage("You aren't not manager of this user");
                                    alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                                    alertDialog.show();
                                } else {
                                    User user = response.body();
                                    intent.putExtra("user_id", user.getId());
                                    intent.putExtra("role", user.getRole());
                                    intent.putExtra("name", user.getName());
                                    intent.putExtra("team_name", user.getTeamId().getName());
                                    startActivity(intent);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ScanQRCodeActivity.this);
                            alertDialog.setTitle("Error Message");
                            alertDialog.setMessage("Cannot find that user with that qr code");
                            alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            alertDialog.show();
                        }
                    });
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
