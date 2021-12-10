package com.example.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.project.R
import com.example.project.fragments.recordfragment
import com.example.project.fragments.AccountFragment
import com.example.project.fragments.DiscussionFragment
import com.example.project.fragments.HomeFragment
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*


const val PREF_NAME = "DATA_CV_PREF"

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val homeFragment = HomeFragment()
        val cameraFragment = recordfragment()
        val discussionFragment = DiscussionFragment()
        val accountFragment = AccountFragment()

        makeCurrentFragment(homeFragment)

        bottom_2navigation.setOnNavigationItemSelectedListener {


            when (it.itemId) {

                R.id.ic_home -> makeCurrentFragment(homeFragment)
                R.id.camera -> makeCurrentFragment(cameraFragment)
                R.id.ic_message -> makeCurrentFragment(discussionFragment)
                R.id.ic_account -> makeCurrentFragment(accountFragment)

            }
            true
        }

        var floating_action_botton =
            findViewById<ExtendedFloatingActionButton>(R.id.floating_action_button)

        // floating_action_botton.setOnClickListener {
        //   startActivity(Intent(this@MainActivity, CameraActivity::class.java))
        //  }
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()


        }
}