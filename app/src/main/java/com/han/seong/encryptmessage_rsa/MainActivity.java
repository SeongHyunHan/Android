package com.han.seong.encryptmessage_rsa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

import javax.crypto.Cipher;

public class MainActivity extends AppCompatActivity {

    static final String TAG = "AsymmetricAlgorithmRSA";

    Button encrypt, decrypt;
    EditText plainMessage;
    TextView encryptedM, decryptedM;
    byte[] encodedBytes;
    Key publicKey, privateKey;

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
        // Set up public and private key for 1024-bit RSA encryption and decryption
        publicKey = null;
        privateKey = null;
        try{
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(1024);
            KeyPair kp = kpg.genKeyPair();
            publicKey = kp.getPublic();
            privateKey = kp.getPrivate();
        }catch(Exception e){
            Log.e(TAG, "RSA key Pair Error");
        }
    }

    private void encrypting(){
        // Encode the original data with Private Key
        encodedBytes = null;
        try{
            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.ENCRYPT_MODE, privateKey);
            encodedBytes = c.doFinal(String.valueOf(plainMessage.getText()).getBytes());
        }catch (Exception e){
            Log.e(TAG, "RSA encryption error");
        }

        encryptedM.setText("Encrypted Message:\n" + Base64.encodeToString(encodedBytes, Base64.DEFAULT));
        decrypt.setEnabled(true);
        encrypt.setEnabled(false);
        plainMessage.setEnabled(false);
        decryptedM.setText("Decrypted Text Display here!");
    }

    private void decrypting(){
        // Decode the encrypted message with Public Key
        byte[] decodedBytes = null;
        try{
            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.DECRYPT_MODE, publicKey);
            decodedBytes = c.doFinal(encodedBytes);
        }catch (Exception e){
            Log.e(TAG, "RSA decryption Error");
        }
        decryptedM.setText("Decrypted Message:\n" + new String(decodedBytes));
        decrypt.setEnabled(false);
        encrypt.setEnabled(true);
        plainMessage.setEnabled(true);
        plainMessage.getText().clear();
    }
}
