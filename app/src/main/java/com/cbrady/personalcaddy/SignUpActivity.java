package com.cbrady.personalcaddy;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cbrady.personalcaddy.databinding.ActivitySignInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.cbrady.personalcaddy.R;
import com.cbrady.personalcaddy.databinding.ActivitySignUpBinding;
import com.cbrady.personalcaddy.models.User;

public class SignUpActivity extends BaseActivity implements View.OnClickListener {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        FirebaseApp.initializeApp(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        // Views

        // Click listeners
        binding.buttonRegister.setOnClickListener(this);
        binding.signInHere.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Check auth on Activity start
        if (mAuth.getCurrentUser() != null) {
            onAuthSuccess(mAuth.getCurrentUser());
        }
    }

    private void signIn() {
        startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
        finish();
    }

    private void signUp() {
        if (!validateForm()) {
            return;
        }

        showProgressBar();
        String email = binding.editTextEmailAddressSignUp.getText().toString();
        String password = binding.editTextPasswordSignUp.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideProgressBar();

                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            Toast.makeText(SignUpActivity.this, "Sign Up Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());

        // Write new user
        writeNewUser(user.getUid(), username, user.getEmail());

        // Go to MainActivity
        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
        finish();
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(binding.editTextEmailAddressSignUp.getText().toString())) {
            binding.editTextEmailAddressSignUp.setError("Required");
            result = false;
        } else {
            binding.editTextEmailAddressSignUp.setError(null);
        }

        if (TextUtils.isEmpty(binding.editTextPasswordSignUp.getText().toString())) {
            binding.editTextPasswordSignUp.setError("Required");
            result = false;
        } else {
            binding.editTextPasswordSignUp.setError(null);
        }

        return result;
    }

    // [START basic_write]
    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        mDatabase.child("users").child(userId).setValue(user);
    }
    // [END basic_write]

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.buttonRegister) {
            signUp();
        } else if (i == R.id.signInHere) {
            signIn();
        }
    }
}