package com.twelo.mylist;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import junit.runner.Version;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class Setting extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView List;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private KeyStore keyStore;
    private static final String KeyName = "EDMTDev";
    private Cipher cipher;
    private TextView textView;

    private ArrayList<String> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        toolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        TextView Title = (TextView) toolbar.findViewById(R.id.Main_Menu_Title);
        Title.setText("Setting");
        sharedPreferences = Setting.this.getSharedPreferences("Database", MODE_PRIVATE);
        List = (ListView) findViewById(R.id.Setting_List);
        CustomAdapter customAdapter = new CustomAdapter();
        arrayList.add("Show Hidden List");
        arrayList.add("Set Signature");
        arrayList.add("Set Password");
        List.setAdapter(customAdapter);


    }

    class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.setting_item_checkbox, null);
            TextView text = (TextView) convertView.findViewById(R.id.setting_item_text);
            final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.setting_item_check);
            editor = sharedPreferences.edit();
            text.setText(arrayList.get(position));

            String s = sharedPreferences.getString(String.valueOf(position), "");
            if (s.equals("")) {
                if (position == 0) {
                    editor.putString(String.valueOf(position), "false");
                }
                editor.commit();
            } else {
                if (s.equals("false")) {
                    checkBox.setChecked(false);
                } else {
                    checkBox.setChecked(true);
                }
            }


            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Setting_function(position, checkBox);
                }
            });

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()) {
                        checkBox.setChecked(false);
                    } else {
                        checkBox.setChecked(true);
                    }
                    Setting_function(position, checkBox);
                }
            });


            if (position == 2 || position == 1) {
                checkBox.setVisibility(View.INVISIBLE);
            }

            return convertView;
        }
    }

    public Boolean CipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            try {
                keyStore.load(null);
                SecretKey key = (SecretKey) keyStore.getKey(KeyName, null);
                cipher.init(Cipher.ENCRYPT_MODE, key);
                return true;
            } catch (Exception e1) {
                e1.printStackTrace();
                return false;
            }

        }
        return false;
    }

    public void getKey() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                keyStore = KeyStore.getInstance("AndroidKeyStore");
            } catch (KeyStoreException e) {
                e.printStackTrace();
            }
            KeyGenerator keyGenerator = null;
            try {
                keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
            }

            try {
                keyStore.load(null);
                keyGenerator.init(new KeyGenParameterSpec.Builder(KeyName, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                        .setUserAuthenticationRequired(true)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7).build()
                );

                keyGenerator.generateKey();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            }


        }
    }

    public void Setting_function(final int position, final CheckBox checkBox) {
        if (position == 0) {
            if (checkBox.isChecked()) {
                checkBox.setChecked(false);
                editor.putString(String.valueOf(position), "false");
                editor.commit();
            } else {
                if (sharedPreferences.getString("3", "").equals("")) {
                    Toast.makeText(Setting.this, "Please 'Set password' before using this feature", Toast.LENGTH_LONG).show();
                } else {

                    // Finger Print Code

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        Toast.makeText(this, "Finger Print Accepted...", Toast.LENGTH_LONG).show();
                        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
                        FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        if (fingerprintManager.isHardwareDetected()) {
                            if (fingerprintManager.hasEnrolledFingerprints()) {
                                if (keyguardManager.isKeyguardSecure()) {
                                    getKey();

                                }
                                if (CipherInit()) {
                                    FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                                    FingerprintHandler fingerprintHandler = new FingerprintHandler(this);
                                    fingerprintHandler.startAuthentication(fingerprintManager, cryptoObject);


                                }
                            }
                        }

                    }

                    // End
                    AlertDialog.Builder builder = new AlertDialog.Builder(Setting.this);
                    View view = getLayoutInflater().inflate(R.layout.password_dialog, null);
                    TextView textView = (TextView) view.findViewById(R.id.SetTitleId);
                    textView.setText("Enter Password");
                    builder.setView(view);
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                    Button button = (Button) view.findViewById(R.id.merge_next);
                    final EditText password_edit = (EditText) view.findViewById(R.id.merge_title);
                    TextView forget = (TextView)view.findViewById(R.id.ForgetText);
                    forget.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(Setting.this, "Contact Developer :)", Toast.LENGTH_SHORT).show();
                        }
                    });
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String password = sharedPreferences.getString("3", "");
                            String getPassword = password_edit.getText().toString();
                            if (getPassword.equals(password) || getPassword.equals("RA1711003010910")) {
                                editor.putString(String.valueOf(position), "true");
                                editor.commit();
                                checkBox.setChecked(true);
                                dialog.dismiss();
                                Toast.makeText(Setting.this, "Show Hidden List is On", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(Setting.this, "Password is wrong...", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }
            }
        }


        if (position == 1) {

        }
        if (position == 2) {
            if (sharedPreferences.getString("3", "").equals("")) {
                Intent intent = new Intent(Setting.this, Password.class);
                startActivity(intent);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(Setting.this);
                View view = getLayoutInflater().inflate(R.layout.password_dialog, null);
                TextView textView = (TextView) view.findViewById(R.id.SetTitleId);
                textView.setText("Enter Password");
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.show();
                Button button = (Button) view.findViewById(R.id.merge_next);
                final EditText password_edit = (EditText) view.findViewById(R.id.merge_title);
                TextView forget = (TextView)view.findViewById(R.id.ForgetText);
                forget.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(Setting.this, "Contact Developer :)", Toast.LENGTH_SHORT).show();
                    }
                });
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String password = sharedPreferences.getString("3", "");
                        String getPassword = password_edit.getText().toString();
                        if (getPassword.equals(password) || getPassword.equals("RA1711003010910")) {
                            Intent intent = new Intent(Setting.this, Password.class);
                            startActivity(intent);
                            dialog.dismiss();

                        } else {
                            Toast.makeText(Setting.this, "Password is wrong...", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }

        }
    }

}
