package com.example.tic_tac_toegame

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import kotlinx.android.synthetic.main.activity_main.*
import android.graphics.Bitmap
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    companion object{
        var ICON:String? =""
    }
    var ImageIdx:Int = 0
    var username:String=""
    var bmp:Bitmap?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        layout?.setOnTouchListener(){
//
//        }
        // User Profile
        UP?.setOnClickListener(){
            var user = Intent(this,User::class.java)
            startActivityForResult(user,1)
        }
        // Two People
        PP?.setOnClickListener(){
            var P2P = Intent(this,Game::class.java)
            P2P.putExtra("User Name",username);
            P2P.putExtra("User Image",bmp);
            startActivity(P2P)
        }
        // One people
        PC?.setOnClickListener(){
            var P2C = Intent(this,Game::class.java)
            P2C.putExtra("User Name",username);
            P2C.putExtra("User Image",bmp);
            startActivity(P2C)
        }
        ttt.setOnTouchListener(object : OnSwipeListener(this) {
            init {
                setDragHorizontal(true)
                setExitScreenOnSwipe(true)
                setAnimationDelay(500)
            }

            override fun onSwipeRight(distance: Float) {
                //Toast.makeText(applicationContext, "swiped right!", Toast.LENGTH_SHORT).show()
                ImageIdx++;
                if(ImageIdx==0){
                    ttt.setImageResource(R.drawable.ttt)
                }
                else if(ImageIdx==1){
                    ttt.setImageResource(R.drawable.ttt2)
                }
                if(ImageIdx==-1){
                    ttt.setImageResource(R.drawable.ttt3)
                }

            }
            override fun onSwipeLeft(distance: Float) {
                //Toast.makeText(applicationContext, "swiped left!", Toast.LENGTH_SHORT).show()
                ImageIdx--;
                if(ImageIdx==0){
                    ttt.setImageResource(R.drawable.ttt)
                }
                else if(ImageIdx==1){
                    ttt.setImageResource(R.drawable.ttt2)
                }
                if(ImageIdx==-1){
                    ttt.setImageResource(R.drawable.ttt3)
                }

            }
        }
        )

    }
    override fun onActivityResult(requestCode:Int,resultCode:Int,data:Intent?){

        if(requestCode==1&&resultCode==Activity.RESULT_OK&&data!=null){
            username = data.getStringExtra("User Name")
            ICON = data.getStringExtra("Icon")
            bmp = data.getParcelableExtra("User Image")
        }
    }
}
