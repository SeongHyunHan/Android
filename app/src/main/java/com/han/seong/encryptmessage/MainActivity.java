package com.han.seong.encryptmessage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.SecureRandom;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    static final String TAG = "Symmetric Algorithm AES";

    Button encrypt, decrypt;
    EditText plainMessage;
    TextView encryptedM, decryptedM;
    SecretKeySpec sks;
    byte[] encodedBytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        encrypt = (Button) findViewById(R.id.encrypt);
        decrypt = (Button) findViewById(R.id.decrypt);
        encryptedM = (TextView) findViewById(R.id.encryptedM);
        decryptedM = (TextView) findViewById(R.id.decryptedM);
        plainMessage = (EditText) findViewById(R.id.plainText);

        setUpSecretKey();
        encrypt.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(!plainMessage.getText().equals(null)) {
                    encrypting();
                }else{
                    Toast.makeText(MainActivity.this, "Message can not be Null!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        decrypt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                decrypting();
            }
        });
    }

    private void setUpSecretKey(){
        // Set up secret key spec for 128-bit AES encryption and decryption
        sks = null;
        try{
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed("any data used as random seed".getBytes());
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(128, sr);
            sks = new SecretKeySpec((kg.generateKey()).getEncoded(), "AES");
        }catch(Exception e){
            Log.e(TAG, "AES secret key spec error");
        }
    }

    private void encrypting(){
        // Encode the original data with AES
        encodedBytes = null;
        try{
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.ENCRYPT_MODE, sks);
            encodedBytes = c.doFinal(String.valueOf(plainMessage.getText()).getBytes());
        }catch (Exception e){
            Log.e(TAG, "AES encryption error");
        }

        encryptedM.setText("Encrypted Message:\n" + Base64.encodeToString(encodedBytes, Base64.DEFAULT));
        decrypt.setEnabled(true);
        encrypt.setEnabled(false);
        plainMessage.setEnabled(false);
        decryptedM.setText("Decrypted Text Display here!");
    }

    private void decrypting(){
        byte[] decodedBytes = null;
        try{
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, sks);
            decodedBytes = c.doFinal(encodedBytes);
        }catch (Exception e){
            Log.e(TAG, "AES decryption Error");
        }
        decryptedM.setText("Decrypted Message:\n" + new String(decodedBytes));
        decrypt.setEnabled(false);
        encrypt.setEnabled(true);
        plainMessage.setEnabled(true);
        plainMessage.getText().clear();
    }

}
