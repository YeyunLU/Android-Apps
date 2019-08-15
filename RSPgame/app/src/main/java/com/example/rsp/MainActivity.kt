package com.example.rsp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageButton
import android.widget.Toast
import android.content.Intent
import android.support.v4.view.ViewCompat
import android.widget.ImageView
import android.widget.TextView
import kotlin.random.Random
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var score:Int = 0
    var my_choice:String=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        var rock = findViewById<ImageButton>(R.id.rock)
//        var scissor = findViewById<ImageButton>(R.id.scissor)
//        var paper = findViewById<ImageButton>(R.id.paper)
        var iv_me = findViewById<ImageView>(R.id.me)
        var iv_score = findViewById<TextView>(R.id.score)
        rock?.setOnClickListener(){
            //Toast.makeText(this@MainActivity,"rock",Toast.LENGTH_LONG).show()
            my_choice = "rock"
            var r = R.drawable.rock
            iv_me.setImageResource(r);
            calculate(my_choice)
            iv_score.setText("SCORE: $score")

        }
        scissor?.setOnClickListener(){
            //Toast.makeText(this@MainActivity,"scissor",Toast.LENGTH_LONG).show()
            my_choice = "scissor"
            var s = R.drawable.scissor
            iv_me.setImageResource(s);
            calculate(my_choice)
            iv_score.setText("SCORE: $score")
        }
        paper?.setOnClickListener(){
            //Toast.makeText(this@MainActivity,"paper",Toast.LENGTH_LONG).show()
            my_choice = "paper"
            var p = R.drawable.paper
            iv_me.setImageResource(p);
            calculate(my_choice)
            iv_score.setText("SCORE: $score")
        }
    }




    private fun calculate(my_choice:String) {

        val random =   Random.nextInt(3)
        var com_choice: String
        var iv_com = findViewById<ImageView>(R.id.robot)
        if(random==0){
            com_choice = "rock"
            iv_com.setImageResource(R.drawable.rock);
        }
        else if(random==1){
            com_choice = "scissor"
            iv_com.setImageResource(R.drawable.scissor);

        }
        else {
            com_choice = "paper"
            iv_com.setImageResource(R.drawable.paper);
        }

        if(com_choice=="rock" && my_choice=="paper"){
            score+=10
        }
        else if(com_choice=="paper" && my_choice=="scissor"){
            score+=10
        }
        else if(com_choice=="scissor" && my_choice=="rock"){
            score+=10
        }
        else if(com_choice=="rock" && my_choice=="scissor"){
            score-=10
        }
        else if(com_choice=="paper" && my_choice=="rock"){
            score-=10
        }
        else if(com_choice=="scissor" && my_choice=="paper"){
            score-=10
        }


    }
}
