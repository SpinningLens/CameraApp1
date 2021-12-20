package com.example.cameraapp

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    val REQUEST_IMAGE_CAPTURE = 1

    //creating variables for RecyclerView
    val labelList = mutableListOf<String>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var manager: RecyclerView.LayoutManager
    private lateinit var myAdapter: LabelAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.photolabel)
        manager = LinearLayoutManager(this)

        //Build a button that launches a camera app
        findViewById<Button>(R.id.button).setOnClickListener {
            dispatchTakePictureIntent(REQUEST_IMAGE_CAPTURE)

        }

    }
    //ToDo Launch Camera app

    //Create an intent that launched the camera and takes a picture
    private fun dispatchTakePictureIntent(resultCode: Int) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            // Grab bitmap from that was captured in camera
            val imageBitmap = data?.extras?.get("data") as Bitmap

            //Set bitmp as ImageView
            findViewById<ImageView>(R.id.imageView).setImageBitmap(imageBitmap)

            //Prepare bitmap for ML Kit APIs
            val imageForMlKit = InputImage.fromBitmap(imageBitmap, 0)

            //Utilization of image labeling API
            val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

            labeler.process(imageForMlKit)
                .addOnSuccessListener { labels ->
                    Log.i("Canon", "Successful processed photo")
                    for (label in labels) {
                        // what is detected in the image
                        val text = label.text
                        //The confidience score of what was detected
                        val confidence = label.confidence
                        var result = ""
                        result = "\n" + result + "\n" + label.text + "-" + (label.confidence * 100).roundToInt() + '%'
                        labelList.add("$text : ${"%.1f".format(confidence)}%")
                        Log.i("Canon", "detected:" + text + "with confidence:" + confidence)

                    }

                    recyclerView.apply {
                        myAdapter = LabelAdapter(labelList)
                        layoutManager = manager
                        adapter = myAdapter
                    }


                }
                .addOnFailureListener { e ->
                    Log.e("Canon", "Error processing of image")
                    // Task failed with an exception
                    // ...
                }

            //To bind bitmap image to Image view
            findViewById<ImageView>(R.id.imageView).setImageBitmap(imageBitmap)
        }
    }


}







