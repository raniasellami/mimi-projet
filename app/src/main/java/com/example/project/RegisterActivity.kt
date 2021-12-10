package com.example.project

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.project.models.User
import com.example.project.utils.ApiInterface
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {
    lateinit var txtfirstname: EditText
    lateinit var layoutfirstname: TextInputLayout
    lateinit var txtlastname: EditText
    lateinit var layoutlastname: TextInputLayout
    lateinit var txtEmail: EditText
    lateinit var layoutEmail: TextInputLayout
    lateinit var txtPassword: EditText
    lateinit var layoutPassword: TextInputLayout
    lateinit var btnSignup: Button
    lateinit var butimg: TextView


    lateinit var storage: FirebaseStorage
    val formater = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
    val now = Date()
    val fileName = formater.format(now)

    private var profilePic: CircleImageView? = null
    private var selectedImageUri: Uri? = null


    companion object{
        val IMAGE_REQUEST_CODE = 100
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        txtfirstname = findViewById(R.id.editTextName)
        layoutfirstname = findViewById(R.id.textInputName)

        txtlastname = findViewById(R.id.editTextLast)
        layoutlastname = findViewById(R.id.textInputLast)

        txtEmail = findViewById(R.id.editTextEmail)
        layoutEmail = findViewById(R.id.textInputemail)



        txtPassword = findViewById(R.id.editTextPassword)
        layoutPassword = findViewById(R.id.textInputPassword)

        butimg = findViewById(R.id.btn_pick_img)
        profilePic = findViewById(R.id.img_save)

        storage = Firebase.storage

        btnSignup = findViewById(R.id.cirRegisterButton)



        btnSignup.setOnClickListener {
            doSignup()
        }
        butimg.setOnClickListener{
            openGallery()


        }
    }
    private fun uploadImage()
    {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading Image ...")
        progressDialog.setCancelable(false)
        progressDialog.show()
        val storageReference = FirebaseStorage.getInstance().reference.child("images/$fileName")
        storageReference.putFile(selectedImageUri!!).
        addOnSuccessListener {
            profilePic!!.setImageURI(selectedImageUri)
            if(progressDialog.isShowing)
            {
                progressDialog.dismiss()
            }
            Toast.makeText(this,"Successfuly uploaded", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            if(progressDialog.isShowing)
            {
                progressDialog.dismiss()
            }
            Toast.makeText(this,"Sorry", Toast.LENGTH_SHORT).show()

        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100 && resultCode == RESULT_OK)
        {
            selectedImageUri = data?.data!!
            profilePic!!.setImageURI(selectedImageUri)
        }
    }

    private val startForResultOpenGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            selectedImageUri = result.data!!.data
            profilePic!!.setImageURI(selectedImageUri)
        }
    }

    private fun openGallery(){
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        startForResultOpenGallery.launch(intent)
    }
    //private fun openGallery() {
       // val intent = Intent()
       // intent.type = "image/*"
       // intent.action = Intent.ACTION_GET_CONTENT
       // startActivityForResult(intent,100)
   // }
    private fun doSignup() {
        if (!validate()) {



        }else {


            uploadImage()
            val apiInterface = ApiInterface.create()


            apiInterface.register(

                txtfirstname.text.toString(),
                txtlastname.text.toString(),
                txtEmail.text.toString(),

                txtPassword.text.toString(),
                fileName.toString(),









                ).enqueue(object :
                Callback<User> {

                override fun onResponse(call: Call<User>, response: Response<User>) {

                    val user = response.body()
                    Log.d("response", user.toString())
                    if (user != null) {
                        if (user.email == null) {

                            Toast.makeText(
                                this@RegisterActivity,
                                "email already exist",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        } else {
                            Toast.makeText(
                                this@RegisterActivity,
                                "user Success",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            startActivity(
                                Intent(
                                    this@RegisterActivity,
                                    LoginActivity::class.java
                                )
                            )

                        }
                    }


                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Connexion error!",
                        Toast.LENGTH_SHORT
                    )
                        .show()



                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }

            })

            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )


        }


    }

    fun onLoginClick(View: View?) {
        startActivity(Intent(this, LoginActivity::class.java))
        overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
    }
    private fun validate(): Boolean {
        layoutfirstname.error = null
        layoutlastname.error = null
        layoutEmail.error = null
        layoutPassword.error = null

        if (txtfirstname.text!!.isEmpty()){
            layoutfirstname.error = "waaa"
            return false
        }





        return true
    }
}
