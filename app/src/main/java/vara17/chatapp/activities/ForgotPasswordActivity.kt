package vara17.chatapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import vara17.chatapp.databinding.ActivityForgotPasswordBinding
import vara17.chatapp.gotoActivity
import vara17.chatapp.isValidEmail
import vara17.chatapp.toast

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListeners()
    }

    private fun setListeners() {
        binding.buttonGoLogin.setOnClickListener {
            gotoActivity<LoginActivity>()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        binding.buttonForgot.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            if(isValidEmail(email)){
                auth.sendPasswordResetEmail(email).addOnCompleteListener(this){
                    toast("An email has been sent to reset your password.")
                    gotoActivity<LoginActivity> {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }
            }else{
                toast("Plase make sure the email address is correct")
            }
        }
    }
}