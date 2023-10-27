package com.example.fiebasephoneauth.login;

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

import com.example.fiebasephoneauth.GuardianInfo;
import com.example.fiebasephoneauth.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * <h3>보호자 회원가입 입력 폼</h3>
 *
 * 회원가입에서 보호자 버튼을 클릭했을 때 보여지는 입력 폼
 */
public class GuardianSignUpFormFragment extends Fragment implements View.OnClickListener{
    /**
     * jsm512
     * Firebase DB 보호자 정보 저장
     */
    private DatabaseReference mPostreference;

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
    long phoneNum;
    long password;
    long passwordConfirm;
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
    public View onCreate(@NonNull LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState){
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
    public void setSignupMode(){
        nameForm.setText("");
        phoneNumForm.setText("");
        idForm.setText("");
        passwordForm.setText("");
        passwordConfirmForm.setText("");
        signup_button.setEnabled(true);
    }

    public void postFirebaseDatabase(boolean add){
        mPostreference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add){
            GuardianInfo post = new GuardianInfo(name,phoneNum,ID,password,passwordConfirm);
            postValues = post.toMap();
        }
        childUpates.put("/Guardian_list/" + phoneNum, postValues);
        mPostreference.updateChildren(childUpates);
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.signup_button){
            name = nameForm.getText().toString();
            phoneNum = Long.parseLong(phoneNumForm.getText().toString());
            ID = idForm.getText().toString();
            password  = Long.parseLong(passwordForm.getText().toString());
            passwordConfirm = Long.parseLong(passwordConfirmForm.getText().toString());

            postFirebaseDatabase(true);
            setSignupMode();
            Toast.makeText(getActivity(),"존재아이디",Toast.LENGTH_SHORT).show();
        }

    }
}