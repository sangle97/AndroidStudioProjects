package com.example.myapplication;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;



import com.example.myapplication.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;


public class Login extends AppCompatActivity {
    private Button btn_dangnhap;
    private TextView tv_dangki;
    private MenuItem MenuItem;
    private EditText ed_email;
    private EditText ed_pass;
    private List<User> listUser;
    private boolean checkLogin=false;
    private String id_user;
    private String name_user;
    private String email_user;

    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        firebaseFirestore=FirebaseFirestore.getInstance();
        init();
       // clickRegister();
        login();
        //clickLogin();


    }
    private void login() {
        btn_dangnhap.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String loginEmail = ed_email.getText().toString().trim();
                final String password = ed_pass.getText().toString().trim();
                if (!loginEmail.equals( "" ) && !password.equals( "" )) {
                    for(int i =0 ;i < listUser.size();i++){
                        if(loginEmail.equals( listUser.get( i ).email) && password.equals( listUser.get( i ).pass ) ){
                            id_user=listUser.get( i ).getId();
                            name_user=listUser.get( i ).getHoten();
                            email_user=listUser.get( i ).getEmail();
                            checkLogin = true;
                            break;
                        }
                    }
                    if(checkLogin){
                        Intent intent= new Intent( Login.this,MainActivity.class );
                        intent.putExtra( "IdUser",id_user );
                        intent.putExtra( "NameUser",name_user );
                        intent.putExtra( "EmailUser",email_user );
                        startActivity(intent);
                        finish();
                        Toast.makeText( Login.this, "Dang nhap thanh cong", Toast.LENGTH_SHORT ).show();

                    }else {
                        Toast.makeText( Login.this, "Tai khoan mat khau khong dung", Toast.LENGTH_SHORT ).show();
                    }
                }else{
                    Toast.makeText( Login.this, "Nhap tai khoan mat khau", Toast.LENGTH_SHORT ).show();
                }
            }
        } );
    }
    public void clickRegister(){

    }
    public  void init(){
        btn_dangnhap=findViewById( R.id.btn_dangnhap );
        tv_dangki=findViewById( R.id.tv_dangki );
        ed_email=findViewById( R.id.edit_email );
        ed_pass=findViewById( R.id.edit_pass );
        listUser = new ArrayList<>(  );
        tv_dangki.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent( Login.this, Register.class );
                startActivity( intent );
                finish();
            }
        } );
        Query query=firebaseFirestore.collection( "user" );

        query .get().addOnCompleteListener( new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.e( "vvv", document.getId() + " => " + document.getData() + document.get( "email" ) );
                        listUser.add( new User( document.getId().toString()+"",document.get( "name" ).toString()+"",document.get( "email" ).toString()+"",document.get( "pass" ).toString()+"") );
                    }
                } else {
                    Toast.makeText( Login.this, "Tai khoan khong dung", Toast.LENGTH_SHORT ).show();
                }

            }} ).addOnFailureListener( new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String error= e.getMessage();
                Toast.makeText( Login.this, "Tai khoan khong dung"+error, Toast.LENGTH_SHORT ).show();
            }
        } );

    }
    public  void clickLogin(){
        btn_dangnhap.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
//                Intent intent= new Intent( Login.this,MainActivity.class );
//                startActivity(intent);
            }
        } );

//        tv_dangki.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent =new Intent( Login.this, Register.class );
//                startActivity( intent );
//            }
//        } );
        }
    }


