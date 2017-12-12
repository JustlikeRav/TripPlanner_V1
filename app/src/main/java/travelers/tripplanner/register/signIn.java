package travelers.tripplanner.register;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import travelers.tripplanner.MainActivity;
import travelers.tripplanner.R;

public class signIn extends AppCompatActivity implements View.OnClickListener{
    private EditText username, password;
    private FirebaseAuth mFirebaseAuth;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mFirebaseAuth = FirebaseAuth.getInstance();

        if(mFirebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
        dialog = new ProgressDialog(this);
        Button mButton = findViewById(R.id.btn);
        TextView mTextView = findViewById(R.id.tv);
        username =  findViewById(R.id.ET1);
        password =  findViewById(R.id.ET2);

        mButton.setOnClickListener(this);
        mTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.btn:
                if(!checkEmpty()){
                    login();
                } else{
                    AlertBox("Empty Field", "All fields must be filled", view);
                }
                break;
            case R.id.tv:
                Intent i = new Intent(signIn.this, signUp.class);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_righ);
                break;
        }
    }

    private void login() {
        dialog = ProgressDialog.show(this, "Please wait!", "Registering user...");
        mFirebaseAuth.signInWithEmailAndPassword(username.getText().toString().trim(), password.getText().toString().trim())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        dialog.dismiss();
                        if(task.isSuccessful()){
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    }
                });
    }

    private boolean checkEmpty() {
        if(username.getText().toString().equals("")) return true;
        if(password.getText().toString().equals("")) return true;
        return false;
    }

    private void AlertBox(String title, String msg, View view){
        AlertDialog.Builder a_builder = new AlertDialog.Builder(view.getContext());
        a_builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        AlertDialog alert = a_builder.create();
        alert.setTitle(title);
        alert.show();
    }

}
