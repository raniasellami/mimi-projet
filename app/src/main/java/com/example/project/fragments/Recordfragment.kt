package com.example.project.fragments


import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.project.R
import java.io.File
import android.os.Environment
import com.google.firebase.firestore.util.Assert
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*
import android.widget.Toast.makeText as makeText1
import com.google.firebase.storage.UploadTask

import android.webkit.MimeTypeMap

import android.content.ContentResolver
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.OnProgressListener


class recordfragment  : Fragment()
{

    private val REQUEST_ID_READ_WRITE_PERMISSION = 99
    private val REQUEST_ID_IMAGE_CAPTURE = 100
    private val REQUEST_ID_VIDEO_CAPTURE = 101
    lateinit var videoView: VideoView
    lateinit var btncamera:Button
    lateinit var btngallery:Button
    private var ourRequestcode :Int =123// any number
    private var selectedVideoUri: Uri? = null

    val formater = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
    val now = Date()
    val fileName = formater.format(now)
   // File file = new File(getPath(uri));


    lateinit var storage: FirebaseStorage
    private var videoAdd: CircleImageView? = null
    private var selectedVideoViewUri: Uri? = null


    companion object{
        val MediaStore.Video_REQUEST_CODE: Int
            get() = 100
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

/*     var resultLauncher = registerForActivityResult(StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                this.askPermissionAndCaptureVideo()
            }
        }*/
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recordfragment, container, false)
    }
    private val startForResultOpenGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            videouri = result.data!!.data
            videoView.setVideoURI(videouri)
            uploadvideo()
        }
    }

    private fun openGallery(){
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "video/*"
        startForResultOpenGallery.launch(intent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        videoView =requireActivity().findViewById(R.id.fragmentvideoView)
        btncamera =requireActivity().findViewById(R.id.btncamera)
        btngallery =requireActivity().findViewById(R.id.btngallery)
        btncamera.setOnClickListener {
            // startVideo()

            this.askPermissionAndCaptureVideo()


        }
        btngallery.setOnClickListener {
            // startVideo()

            openGallery()

            //videoAdd = view.findViewById(R.id.btncamera)

            storage = Firebase.storage


        }
        //now set up media controller for the play pause next prev
        val mediaCollection = MediaController(activity)
        mediaCollection.setAnchorView(videoView)
        videoView.setMediaController(mediaCollection)



    }




    /*fun startVideo() {
        //start intent to capture video
        val intent = Intent(MediaStore.INTENT_ACTION_VIDEO_CAMERA)
        if (activity?.let { intent.resolveActivity(it.packageManager) } != null) {
            requireActivity().startActivityForResult(intent, ourRequestcode)


        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode ==ourRequestcode && resultCode == AppCompatActivity.RESULT_OK){
            // get data from uri
            val videoUri =data?.data
            videoView.setVideoURI(videoUri)
            videoView.start()

        }
    }*/

    private fun askPermissionAndCaptureVideo() {

        // With Android Level >= 23, you have to ask the user
        // for permission to read/write data on the device.


        // Check if we have read/write permission
        val readPermission = ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        val writePermission = ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (writePermission != PackageManager.PERMISSION_GRANTED ||
            readPermission != PackageManager.PERMISSION_GRANTED
        ) {
            // If don't have permission so prompt the user.
            requestPermissions(
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                REQUEST_ID_READ_WRITE_PERMISSION
            )
            return
        }

        captureVideo()
    }

    var videouri: Uri? = null

    private fun getfiletype(videouri: Uri): String? {
        val r: ContentResolver = requireActivity().getContentResolver()
        // get the file type ,in this case its mp4
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(r.getType(videouri))
    }
        fun uploadvideo() {
    if (videouri != null) {
        // save the selected video in Firebase storage
        val storageReference = FirebaseStorage.getInstance().reference.child("videos/$fileName")
        storageReference.putFile(videouri!!).
        addOnSuccessListener {

            Toast.makeText(requireContext(),"Successfuly uploaded", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{

            Toast.makeText(requireContext(),"Sorry", Toast.LENGTH_SHORT).show()

        }
    }
    }


    private fun captureVideo() {
        try {
            // Create an implicit intent, for video capture.
            val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)

            /* // The external storage directory.
             val dir: File = Environment.getExternalStorageDirectory()
             if (!dir.exists()) {
                 dir.mkdirs()
             }
             // file:///storage/emulated/0/myvideo.mp4
             val savePath: String = dir.getAbsolutePath().toString() + "/myvideo.mp4"
             val videoFile = File(savePath)
             val videoUri: Uri = Uri.fromFile(videoFile)

             // Specify where to save video files.
             intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri as Uri)
             intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

             // ================================================================================================
             // To Fix Error (**)
             // ================================================================================================
             val builder = VmPolicy.Builder()
             StrictMode.setVmPolicy(builder.build())

             // ================================================================================================
             // You may get an Error (**) If your app targets API 24+
             // "android.os.FileUriExposedException: file:///storage/emulated/0/xxx exposed beyond app through.."
             //  Explanation: https://stackoverflow.com/questions/38200282
             // ================================================================================================
 */
            // Start camera and wait for the results.
            aaa.launch(intent) // (**)
        } catch (e: Exception) {
            makeText1(context, "Error capture video: " + e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_ID_READ_WRITE_PERMISSION -> {


                // Note: If request is cancelled, the result arrays are empty.
                // Permissions granted (read/write).
                if (grantResults.size > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    makeText1(requireContext(), "Permission granted!", Toast.LENGTH_LONG).show()
                    captureVideo()
                } else {
                    makeText1(requireContext(), "Permission denied!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // When results returned
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ID_VIDEO_CAPTURE) {
            if (resultCode == RESULT_OK) {
                val videoUri = data!!.data
                Log.e("MyLog", "Video saved to: $videoUri")
                makeText1(
                    context, "Video saved to:$videoUri".trimIndent(), Toast.LENGTH_LONG
                ).show()

                videouri = data.getData()
                uploadvideo();

                videoView.setVideoURI(videoUri)
                videoView.start()
            } else if (resultCode == RESULT_CANCELED) {
                makeText1(
                    context, "Action Cancelled.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                makeText1(
                    context, "Action Failed",
                    Toast.LENGTH_LONG
                ).show()
            }
        }


        val file = File(Environment.getExternalStorageDirectory(), "read.me")
        val uri = Uri.fromFile(file)
        val auxFile = File(uri.toString())
      //  Assert.assertEquals(file.absolutePath, auxFile.absolutePath)




        fun playVideoInDevicePlayer(videoPath: String) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoPath))
            intent.setDataAndType(Uri.parse(videoPath), "video/mp4")
            startActivity(intent)
        }



    }
    private fun openGalleryForVideo() {
        val intent = Intent()
        intent.type = "video/*"
        intent.action = Intent.ACTION_PICK
        startActivityForResult(Intent.createChooser(intent, "Select Video"),    REQUEST_ID_VIDEO_CAPTURE)
    }

    private val aaa = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val videoUri = result.data!!.data
            Log.e("MyLog", "Video saved to: $videoUri")
            makeText1(
                context, "Video saved to:$videoUri".trimIndent(), Toast.LENGTH_LONG
            ).show()

            //videouri = data.getData()
            //uploadvideo();

            videoView.setVideoURI(videoUri)
            videoView.start()
            //uploadvideo()
        }
    }

    /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
         super.onActivityResult(requestCode, resultCode, data)
         if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
             if (data?.data != null) {
                 val uriPathHelper = URIPathHelper()
                 val videoFullPath = uriPathHelper.getPath(this, data?.data) // Use this video path according to your logic
                 // if you want to play video just after picking it to check is it working
                 if (videoFullPath != null) {
                     playVideoInDevicePlayer(videoFullPath);
                 }
             }
         }
     }*/


}


