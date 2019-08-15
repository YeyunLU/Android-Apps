package edu.uw.pmpee590.a2dgraphics_bounceball


import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.series.DataPoint
import kotlinx.android.synthetic.main.scores.*


class Result : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scores)

        val series = BarGraphSeries(
            arrayOf(
                DataPoint(0.0, MainActivity.scores[0].toDouble()),
                DataPoint(1.0, MainActivity.scores[1].toDouble()),
                DataPoint(2.0, MainActivity.scores[2].toDouble()),
                DataPoint(3.0, MainActivity.scores[3].toDouble()),
                DataPoint(4.0, MainActivity.scores[4].toDouble())
            )
        )
        mGraphX.addSeries(series)
// styling
        series.setValueDependentColor { data ->
            Color.argb(150,
                0,
                150, 200
            )
        }
        series.spacing = 50
// draw values on top
        series.isDrawValuesOnTop = true
        series.valuesOnTopColor = Color.BLACK
        restart?.setOnClickListener(){
            var intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }
}