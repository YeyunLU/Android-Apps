package com.example.tic_tac_toegame

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.app.Activity
import kotlinx.android.synthetic.main.activity_user.*
import android.graphics.Bitmap
import android.provider.MediaStore

class User : AppCompatActivity() {

    var bmp:Bitmap?=null
    var icon:String?=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        var username = findViewById<EditText>(R.id.username)
        update?.setOnClickListener(){
            var intent = Intent(this,MainActivity::class.java)
            intent.putExtra("User Name",username.text.toString());
            intent.putExtra("User Image",bmp);
            intent.putExtra("Icon",icon);
            setResult(Activity.RESULT_OK,intent);
            finish();
        }
        user?.setOnClickListener(){
            var camera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(camera,123)
        }
        idx1?.setOnClickListener(){
            icon = "cross";
        }
        idx2?.setOnClickListener(){
            icon = "circle";
        }
        idx3?.setOnClickListener(){
            icon = "paw";
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==123 && resultCode==RESULT_OK && data!=null){
            bmp = data.extras.get("data") as Bitmap
            bmp = Bitmap.createScaledBitmap(bmp,250,300,false)
            user.setImageBitmap(bmp)
        }
    }
}
