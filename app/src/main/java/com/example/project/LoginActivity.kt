package com.example.project

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.edit
import com.example.project.models.User
import com.example.project.utils.ApiInterface

import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response




  class LoginActivity : AppCompatActivity() {

        lateinit var txtLogin: EditText
        lateinit var textInputEmail: TextInputLayout
        lateinit var txtPassword: EditText
        lateinit var textInputPassword: TextInputLayout
        lateinit var btnLogin: Button
      private lateinit var mSharedPref: SharedPreferences



      override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_login)
          mSharedPref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

            txtLogin = findViewById(R.id.editTextEmail)
            textInputEmail = findViewById(R.id.textInputEmail)


            txtPassword = findViewById(R.id.editTextPassword)
            textInputPassword= findViewById(R.id.textInputPassword)



            btnLogin = findViewById(R.id.cirLoginButton)



            btnLogin.setOnClickListener{
                doLogin()
            }
        }

      private fun doLogin() {
          if (!validate()) {


          } else {
              Log.d("username",txtLogin.text.toString())
              Log.d("password",txtPassword.text.toString())


              val apiInterface = ApiInterface.create()
              apiInterface.seConnecter(txtLogin.text.toString(), txtPassword.text.toString())
                  .enqueue(object : Callback<User> {
                      /*override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                          Log.d("erreur1",call.toString())
                          //handle error here
                      }

                      override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                          //your raw string response
                          val stringResponse = response.body()?.string()
                          Log.d("waaaaaaaaa",stringResponse.toString())                        }

                  })
              }*/

                      override fun onResponse(call: Call<User>, response: Response<User>) {

                          val user = response.body()
                          Log.d("response", user.toString())
                          if (user != null) {
                              if (user.email == null) {





                                  Toast.makeText(
                                      this@LoginActivity,
                                      "user not found",
                                      Toast.LENGTH_SHORT
                                  )
                                      .show()


                              } else {

                                  mSharedPref.edit {
                                      putString("username", user.firstname)
                                      putString("phone", user.lastname)
                                      putString("email", user.email)
                                      putString("password", user.password)
                                      putString("avatar", user.avatar)
                                      commit()


                                  }


                                  Toast.makeText(
                                      this@LoginActivity,
                                      "user Success",
                                      Toast.LENGTH_SHORT
                                  )
                                      .show()
                                  startActivity(
                                      Intent(
                                          this@LoginActivity,
                                          MainActivity::class.java
                                      )
                                  )



                              }

                          }

                          window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                      }


                      override fun onFailure(call: Call<User>, t: Throwable) {
                          Toast.makeText(
                              this@LoginActivity,
                              "Connexion error!",
                              Toast.LENGTH_SHORT
                          )
                              .show()



                          window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                      }

                  })


          }
      }



            private fun validate(): Boolean {
            textInputEmail.error = null
            textInputPassword.error = null



            if (txtPassword.text!!.isEmpty()) {
                textInputPassword.error = "wrong password "
                return false
            }




            return true
        }
            fun onLoginClick(View: View?) {
                startActivity(Intent(this, RegisterActivity::class.java))
                overridePendingTransition(R.anim.slide_in_right, R.anim.stay) }

    }
