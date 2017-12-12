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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import travelers.tripplanner.MainActivity;
import travelers.tripplanner.R;

public class signUp extends AppCompatActivity implements View.OnClickListener {
    private EditText email, password, confirmPassword, name;
    private FirebaseAuth mFirebaseAuth;
    private ProgressDialog dialog;
    private Button mButton;
    private TextView mTextView;
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mUserIdRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mFirebaseAuth = FirebaseAuth.getInstance();

        if(mFirebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        dialog = new ProgressDialog(this);

        mButton = findViewById(R.id.btn);
        mTextView = findViewById(R.id.tv);
        name =  findViewById(R.id.ET0);
        email =  findViewById(R.id.ET1);
        password =  findViewById(R.id.ET2);
        confirmPassword =  findViewById(R.id.ET3);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mButton.setOnClickListener(this);
        mTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.btn:

                if(!checkEmpty()){
                    if(valid()){
                        if(!validEmail()){
                            AlertBox("Invalid Email!", "You entered an invalid email address", view);
                            break;
                        }

                        if(!validPassword()){
                            AlertBox("Invalid Password!", "Password must contain atleast 6 characters", view);
                            break;
                        }
                        SignUp();
                    } else{
                        AlertBox("Wrong Password", "Password did not match, try again!", view);
                    }
                } else{
                    AlertBox("Empty Field", "All fields must be filled", view);
                }
                break;
            case R.id.tv:
                Intent i = new Intent(signUp.this, signIn.class);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }
    }

    private boolean validPassword() {
        return 6<=password.getText().toString().length();
    }

    private void SignUp() {
        dialog = ProgressDialog.show(this, "Please wait!", "Registering user...");
        mFirebaseAuth.createUserWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            mUserIdRef = mRootRef.child(mFirebaseAuth.getCurrentUser().getUid());
                            DatabaseReference mUserNameRef = mUserIdRef.child("Name");
                            mUserNameRef.setValue(name.getText().toString());
                            Intent i = new Intent(signUp.this, MainActivity.class);
                            finish();
                            startActivity(i);
                        }else{
                            Toast.makeText(signUp.this, "Could not make the user!", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });
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

    private boolean checkEmpty() {
        if(name.getText().toString().equals("")) return true;
        if(email.getText().toString().equals("")) return true;
        if(password.getText().toString().equals("")) return true;
        if(confirmPassword.getText().toString().equals("")) return true;
        return false;
    }

    private boolean valid() {
        return password.getText().toString().equals(confirmPassword.getText().toString());
    }

    private boolean validEmail(){
        CharSequence target = email.getText();
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

}
