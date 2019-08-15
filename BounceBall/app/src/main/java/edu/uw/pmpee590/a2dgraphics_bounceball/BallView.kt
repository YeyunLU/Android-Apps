package edu.uw.pmpee590.a2dgraphics_bounceball

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import java.util.*
import kotlin.concurrent.schedule


class BallView(context: Context, attrs: AttributeSet) : View(context, attrs)
{
    //Define the ball shape
    private val size = 100f
    private var ballX = 0f
    private var ballY = 0f
    private var prevY = 0f
    private var dx = MainActivity.dx
    private var dy = MainActivity.dy

    private val sizeB = 100f
    private var ballBX = 500f
    private var ballBY = 1450f
    private var dxB = 0f
    private var sign:Int = -1


    override fun onDraw (canvas: Canvas?){
        super.onDraw(canvas)
        if (canvas == null) return

        val panty = Paint()
        panty.setARGB(180,0,150,200)
        //Parameters to draw text
        panty.typeface = Typeface.create(Typeface.SANS_SERIF,Typeface.BOLD)
        panty.textSize = 50f

        //For the animation example
        canvas.drawOval(RectF(ballX, ballY, ballX+size,ballY+size),panty)
        canvas.drawOval(RectF(ballBX, ballBY, ballBX+sizeB,ballBY+sizeB),panty)

        /*  EXAMPLES*/

                //get size of the screen in pixels
 /*       val scWidth = Resources.getSystem().getDisplayMetrics().widthPixels.toFloat()
        val scHeight = Resources.getSystem().getDisplayMetrics().heightPixels.toFloat()*/
        //Log.d("Tag","Heigh is $scHeight and Width is $scWidth")

/*        //Example draw Oval
        // draw an oval which is one 20% of the screen size
        //for the initial draw example
        canvas.drawOval(RectF(10f,10f, scWidth*0.2f,scWidth*0.2f),panty)*/


        //Example draw text
        //x and y refer to the margins, not the text size!
        //canvas.drawText("X",width.toFloat()/2,height.toFloat()/2,panty)

/*        //Example draw a bitmap
        val pict = BitmapFactory.decodeResource(resources,R.drawable.bored)
        canvas.drawBitmap(pict,350f,600f,null)*/

        // Note about drawing Bitmap: avoid having to decode the image every time you launch the app
        // (that is why Android Studio highlights the line of va pict=... in yellow
        // Avoid object allocations during draw/layout operations (preallocate and reuse instead)
        // You should avoid allocating objects during a drawing or layout operation.
        // These are called frequently, so a smooth UI can be interrupted by garbage collection pauses
        // caused by the object allocations.
        // The way this is generally handled is to allocate the needed objects up front and
        // to reuse them for each drawing operation


    }

    //animation
    fun move_ball(ddx:Float){

        var col:Boolean=false

        dxB = (ddx).toFloat()*sign
        ballBX += dxB
        if (ballBX + sizeB >= width || ballBX <=0){
            sign=(-1*sign)
        }

        ballX += dx
        if (ballX + size >= width || ballX <=0){
            dx = -dx
        }
        ballY += dy
        if (ballY <=0){
            dy = -dy
        }
        if (ballY>=1450){
            MainActivity.flag=false
        }
        var distance:Double = Math.sqrt(Math.pow((ballX-ballBX).toDouble(), 2.toDouble()) + Math.pow((ballY-ballBY).toDouble(), 2.toDouble()))
        if(distance<=100.0 && ballY>prevY ){
                //dy += 0.2f
                dy = -dy
                MainActivity.score+=MainActivity.dx
        }
        prevY=ballY
        postInvalidate() //call to function onDraw

    }

}