package edu.uw.pmpee590.a2dgraphics_bounceball

import android.app.PendingIntent.getActivity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.media.MediaPlayer
import android.support.v7.app.AlertDialog
import android.util.Log
import android.content.Intent
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity(), SensorEventListener {

    val manager = supportFragmentManager

    companion object {
        var ddx:Float = 0.0f
        var flag:Boolean  = true
        var score:Float = 0f
        var max_score:Float = 0f
        var scores:FloatArray=FloatArray(5)
        var idx:Int = 0
        var dx: Float = 1.0f
        var dy: Float = 2.0f
        var show:Boolean=false
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    var play:Boolean = false
    private lateinit var mp: MediaPlayer
    lateinit var sensorManager: SensorManager

    override fun onSensorChanged(event: SensorEvent?) {
        ddx = event!!.values[0]
//        textView.text = "x = ${event!!.values[0]}\n\n"+
//                "y = ${event.values[1]}\n\n"+
//                "z = ${event.values[2]}"
        max.text = "Max Record = ${max_score}"
        textView.text = "Current Score = ${score}"
        if(dx==1.0f){
            speed.text = "Speed: Low "}
        else if(dx==2.0f){
            speed.text = "Speed: Medium "}
        else{
            speed.text = "Speed: High "
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mp = MediaPlayer.create (this, R.raw.music)
        mp.start ()

        score = 0f
        flag = true

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_GAME
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        mp.stop()
        sensorManager.unregisterListener(this)
        calculate()

    }
    //function to animate the ball

    fun startAnim(view: View){
        Thread(Runnable{
            animation_loop()
            this@MainActivity.runOnUiThread(java.lang.Runnable {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Game Over")
                builder.setMessage("You Lose! Score:$score")

                builder.setPositiveButton("Show Recent Results") { dialog, which ->
                   calculate() // calculate the recorded scores
                    var result = Intent(this,Result::class.java)
                    startActivity(result)
                }

                builder.setNegativeButton("Restart") { dialog, which ->
                    calculate()
                    finish()
                    startActivity(getIntent())
                }
                builder.show()

            })

        }).start()

    }

    fun calculate(){ // calculate the recorded scores
        max_score = Math.max(max_score, score)
        idx = 0
        while(scores[idx]>score&&idx<5){
            idx++
        }
        if(idx<5){
            var i:Int=4
            while(i>idx&&i>0){
                scores[i]=scores[i-1]
                i--
            }
            scores[idx]=score
        }
    }
    fun restartAnim(view: View){
        finish()
        startActivity(getIntent())
    }

    fun animation_loop(){
        while(flag){
            ball.move_ball(ddx) //layout_ball is the ID of the xml of the VIew object
            Thread.sleep(2)
        }
    }


    fun music(view: View){
        if(play){
            mp.start ()
            mp.isLooping = true
        }
        else{
            mp.pause()
        }
        play = !play;

    }

    fun createFragment(view:View){

        show=!show
        val transaction = manager.beginTransaction()


        if(show) {
            val fragment = SpeedFragment()
            transaction.replace(R.id.fragmentContainer, fragment) // replace at where we want
            transaction.addToBackStack(null)
            transaction.commit()
        }
        else{
            val fragment = BlankFragment()
            transaction.replace(R.id.fragmentContainer, fragment) // replace at where we want
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }


}