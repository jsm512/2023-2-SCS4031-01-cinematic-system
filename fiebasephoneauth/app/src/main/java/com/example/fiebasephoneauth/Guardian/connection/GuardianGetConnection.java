package com.example.fiebasephoneauth.Guardian.connection;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fiebasephoneauth.databinding.ActivityGuardianGetConnectionBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *  <h3> 계정 연동 페이지 </h3>
 *
 * 보호자가 계정 연동을 진행하는 페이지
 */
public class GuardianGetConnection extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseAuth mAuth;
    String verificationID;
    boolean check;
    String idTxt;

    private ActivityGuardianGetConnectionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGuardianGetConnectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //피보호자 정보를 로그인 한 보호자 firestore db에 추가할 때 사용
        Intent intent = getIntent();
        String GuardianidTxt = intent.getStringExtra("id");

        // 레이아웃 요소들
        EditText nameForm = binding.nameForm;
        EditText phoneForm = binding.phoneNumForm;
        EditText phoneNumConfirmForm = binding.phoneNumConfirmForm;
        Button requestAuthNumButton = binding.requestAuthNumButton;
        Button confirmAuthNumButton = binding.confirmAuthNumButton;
        TextView logoutText = binding.logoutText;
        Button singup_button = binding.signupButton;
        mAuth = FirebaseAuth.getInstance();

        logoutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuardianGetConnection.this, GuardianNotConnected.class);
                startActivity(intent);
            }
        });
        requestAuthNumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty((nameForm.getText().toString()))){
                    Toast.makeText(GuardianGetConnection.this, "피보호자 ID를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                if(TextUtils.isEmpty(phoneForm.getText().toString())){
                    Toast.makeText(GuardianGetConnection.this, "전화번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    String number = phoneForm.getText().toString();
                    sendverificationcode(number);
                }
            }
        });

        confirmAuthNumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(phoneNumConfirmForm.getText().toString())){
                    Toast.makeText(GuardianGetConnection.this, "인증번호를 잘못 입력했습니다.", Toast.LENGTH_SHORT).show();
                }
                else{
                    verifycode(phoneNumConfirmForm.getText().toString());
                }
            }
        });
        singup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check){
                    Intent intent = new Intent(GuardianGetConnection.this, GuardianConnected.class);
                    idTxt = nameForm.getText().toString();

                    /**
                       입력받은 피보호자 id에 속한 모든 필드를 로그인한 보호자 필드에 추가하는 코드 작성 중
                     */
                    DocumentReference targetRef = db.collection("Guardian_list").document(GuardianidTxt);
                    DocumentReference docRef = db.collection("CareReceiver_list").document(idTxt);
                    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.exists()){
                                Object value = documentSnapshot.get("Name");
                                if(value != null){
                                    Map<String,Object> newData = new HashMap<>();
                                    newData.put("CareReceiverName",value);
                                    targetRef.update(newData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                        }
                                    });
                                }
                            }
                        }
                    });
                    intent.putExtra("id",GuardianidTxt);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(GuardianGetConnection.this, "연동을 먼저 해주세요!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendverificationcode(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+82" + phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
    mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
            final String code = credential.getSmsCode();
            if(code!=null){
                verifycode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(GuardianGetConnection.this,"인증 실패",Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCodeSent(@NonNull String s,
                               PhoneAuthProvider.ForceResendingToken token) {

            super.onCodeSent(s, token);
            verificationID = s;
        }
    };

    private void verifycode(String Code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, Code);
        signinbyCredentials(credential);
    }

    private void signinbyCredentials(PhoneAuthCredential credential) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(GuardianGetConnection.this,"연동 성공",Toast.LENGTH_SHORT).show();
                            check = true;
                        }

                    }
                });
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        if(currentUser != null){
//            Intent intent = new Intent(GuardianGetConnection.this, GuardianHome.class);
//            EditText nameForm = binding.nameForm;
//            final String idTxt = nameForm.getText().toString();
//            intent.putExtra("id",idTxt);
//            startActivity(intent);
//            finish();
//        }
//    }
}