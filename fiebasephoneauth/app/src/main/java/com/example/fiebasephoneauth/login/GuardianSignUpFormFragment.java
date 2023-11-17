package com.example.fiebasephoneauth.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fiebasephoneauth.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * <h3>보호자 회원가입 입력 폼</h3>
 *
 * 회원가입에서 보호자 버튼을 클릭했을 때 보여지는 입력 폼
 */
public class GuardianSignUpFormFragment extends Fragment implements View.OnClickListener{

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    TextView userInfo;
    TextView nameText;
    TextView phoneNumText;
    TextView accountInfo;
    TextView idText;
    TextView pwText;
    TextView pwConfirmText;
    EditText nameForm;
    EditText phoneNumForm;
    EditText idForm;
    EditText passwordForm;
    EditText passwordConfirmForm;

    Button signup_button;

    String name;
    String ID;
    String phoneNum;
    String password;
    String passwordConfirm;
    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View root = inflater.inflate(R.layout.fragment_guardian_sign_up_form, container, false);

        signup_button = root.findViewById(R.id.signup_button);
        signup_button.setOnClickListener(this);
        userInfo = root.findViewById(R.id.userInfo);
        nameText = root.findViewById(R.id.nameText);
        phoneNumText = root.findViewById(R.id.phoneNumText);
        accountInfo = root.findViewById(R.id.accountInfo);
        idText = root.findViewById(R.id.idText);
        pwText = root.findViewById(R.id.pwText);
        pwConfirmText = root.findViewById(R.id.pwConfirmText);
        nameForm = root.findViewById(R.id.nameForm);
        phoneNumForm = root.findViewById(R.id.phoneNumForm);
        idForm = root.findViewById(R.id.idForm);
        passwordForm = root.findViewById(R.id.passwordForm);
        passwordConfirmForm = root.findViewById(R.id.passwordConfirmForm);


        return root;
    }


    @Nullable
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    public void setSignupMode(){
        nameForm.setText("");
        phoneNumForm.setText("");
        idForm.setText("");
        passwordForm.setText("");
        passwordConfirmForm.setText("");
        signup_button.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        name = nameForm.getText().toString();
        phoneNum = phoneNumForm.getText().toString();
        ID = idForm.getText().toString();
        password  = passwordForm.getText().toString();
        passwordConfirm = passwordConfirmForm.getText().toString();

        Map<String,Object> user = new HashMap<>();
        user.put("Name",name);
        user.put("phoneNum",phoneNum);
        user.put("ID",ID);
        user.put("password",password);


        if (name.isEmpty() || phoneNum.isEmpty() || ID.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()){
            Toast.makeText(getActivity(), "사용자 정보를 모두 입력해주세요!", Toast.LENGTH_SHORT).show();

        }
        else if(!password.equals(passwordConfirm)){
            Toast.makeText(getActivity(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();

        }
        else {
            db.collection("Guardian_list").document(ID)
                    .set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getActivity(), "회원가입 완료!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), GuardianSignInActivity.class);
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "회원가입 실패!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }

}