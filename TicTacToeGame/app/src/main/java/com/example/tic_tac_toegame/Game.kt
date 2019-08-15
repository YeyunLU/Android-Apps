package com.example.tic_tac_toegame

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.graphics.Bitmap
import android.support.v7.app.AlertDialog
import kotlinx.android.synthetic.main.game.*
import android.content.Intent

class Game : AppCompatActivity() {

    var username:String=""
    var bmp: Bitmap?=null
    var turn = false
    var icon = MainActivity.ICON
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game)
        username = intent.getStringExtra("User Name")
        bmp = intent.getParcelableExtra("User Image")
        User.setText("$username")
        userimage.setImageBitmap(bmp)
        var choice = R.drawable.blank;
        var r = R.drawable.blank;
        if (icon=="cross"){
            choice = R.drawable.cross; }
        else if(icon=="light"){
            choice = R.drawable.light;
        }
        else{
            choice = R.drawable.paw;
        }
        var game = Array(3, {IntArray(3)});

            image11?.setOnClickListener() {
                if (game[0][0] == 0) {
                    if (turn == false) {
                        r = choice
                        game[0][0] = 1
                    } else {
                        r = R.drawable.circle
                        game[0][0] = -1
                    }
                    turn = (!turn);
                    image11.setImageResource(r);
                    judge(game);
                } else {
                    error();
                }
            }
            image12?.setOnClickListener() {
                if (game[0][1] == 0) {
                    if (turn == false) {
                        r = choice
                        game[0][1] = 1
                    } else {
                        r = R.drawable.circle
                        game[0][1] = -1
                    }
                    turn = (!turn);
                    image12.setImageResource(r);
                    judge(game);
                } else {
                    error();
                }
            }
            image13?.setOnClickListener() {
                if (game[0][2] == 0) {
                    if (turn == false) {
                        r = choice
                        game[0][2] = 1
                    } else {
                        r = R.drawable.circle
                        game[0][2] = -1
                    }
                    turn = (!turn);
                    image13.setImageResource(r);
                    judge(game);
                } else {
                    error();
                }
            }
            image21?.setOnClickListener() {
                if (game[1][0] == 0) {
                    if (turn == false) {
                        r = choice
                        game[1][0] = 1
                    } else {
                        r = R.drawable.circle
                        game[1][0] = -1
                    }
                    turn = (!turn);
                    image21.setImageResource(r);
                    judge(game);
                } else {
                    error();
                }
            }
            image22?.setOnClickListener() {
                if (game[1][1] == 0) {
                    if (turn == false) {
                        r = choice
                        game[1][1] = 1
                    } else {
                        r = R.drawable.circle
                        game[1][1] = -1
                    }
                    turn = (!turn);
                    image22.setImageResource(r);
                    judge(game);
                } else {
                    error();
                }
            }
            image23?.setOnClickListener() {
                if (game[1][2] == 0) {
                    if (turn == false) {
                        r = choice
                        game[1][2] = 1
                    } else {
                        r = R.drawable.circle
                        game[1][2] = -1
                    }
                    turn = (!turn);
                    image23.setImageResource(r);
                    judge(game);
                } else {
                    error();
                }
            }
            image31?.setOnClickListener() {
                if (game[2][0] == 0) {
                    if (turn == false) {
                        r = choice
                        game[2][0] = 1
                    } else {
                        r = R.drawable.circle
                        game[2][0] = -1
                    }
                    turn = (!turn);
                    image31.setImageResource(r);
                    judge(game);
                } else {
                    error();
                }
            }
            image32?.setOnClickListener() {
                if (game[2][1] == 0) {
                    if (turn == false) {
                        r = choice
                        game[2][1] = 1
                    } else {
                        r = R.drawable.circle
                        game[2][1] = -1
                    }
                    turn = (!turn);
                    image32.setImageResource(r);
                    judge(game);
                } else {
                    error();
                }
            }
            image33?.setOnClickListener() {
                if (game[2][2] == 0) {
                    if (turn == false) {
                        r = choice
                        game[2][2] = 1
                    } else {
                        r = R.drawable.circle
                        game[2][2] = -1
                    }
                    turn = (!turn);
                    image33.setImageResource(r);
                    judge(game);
                } else {
                    error();
                }

            }


    }
    private fun error(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("This position is not allowed!")
        val alert = builder.create()
        alert.show()
    }
    private fun judge(game: Array<IntArray>){
        if((game[0][0]+game[0][1]+game[0][2]==3)||(game[1][0]+game[1][1]+game[1][2]==3)
            ||(game[2][0]+game[2][1]+game[2][2]==3)||(game[0][0]+game[1][0]+game[2][0]==3)
            ||(game[0][1]+game[1][1]+game[2][1]==3)||(game[0][2]+game[1][2]+game[2][2]==3)
            ||(game[0][0]+game[1][1]+game[2][2]==3)||(game[2][0]+game[1][1]+game[0][2]==3))
        {
            result(1);
        }
        else if((game[0][0]+game[0][1]+game[0][2]==-3)||(game[1][0]+game[1][1]+game[1][2]==-3)
            ||(game[2][0]+game[2][1]+game[2][2]==-3)||(game[0][0]+game[1][0]+game[2][0]==-3)
            ||(game[0][1]+game[1][1]+game[2][1]==-3)||(game[0][2]+game[1][2]+game[2][2]==-3)
            ||(game[0][0]+game[1][1]+game[2][2]==-3)||(game[2][0]+game[1][1]+game[0][2]==-3))
        {
            result(-1);
        }
        else if(game[0][0]!=0&&game[0][1]!=0&&game[0][2]!=0&&game[1][0]!=0&&game[1][1]!=0&&game[1][2]!=0
            &&game[2][0]!=0&&game[2][1]!=0&&game[2][2]!=0)
        {
            result(0);
        }
        else{
            return;
        }
    }

    private fun result(role: Int){
        val builder = AlertDialog.Builder(this)
        var back = Intent(this,MainActivity::class.java)
        builder.setTitle("Game Over")
        if(role==1) {
            builder.setMessage("$icon win!")
            builder.setPositiveButton("Back") {dialog, which ->
                // Do something when user press the positive button
                startActivity(back) }
        }
        else if(role==-1) {
            builder.setMessage("Circle win!")
            builder.setPositiveButton("Back") {dialog, which ->
                // Do something when user press the positive button
                startActivity(back) }

        }
        else{
            builder.setMessage("Draw!")
            builder.setPositiveButton("Back") {dialog, which ->
                // Do something when user press the positive button
                startActivity(back) }
        }
        val alert = builder.create()
        alert.show()

    }

}