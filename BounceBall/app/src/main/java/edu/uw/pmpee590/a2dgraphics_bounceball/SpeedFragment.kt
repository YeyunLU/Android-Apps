package edu.uw.pmpee590.a2dgraphics_bounceball


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_speed.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class SpeedFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view: View = inflater!!.inflate(R.layout.fragment_speed, container, false)

        val low: Button? = view?.findViewById(R.id.low)
        val med: Button? = view?.findViewById(R.id.medium)
        val high: Button? = view?.findViewById(R.id.high)

        // Inflate the layout for this fragment
        low?.setOnClickListener {v: View ->
            lowmode(v)
        }
        med?.setOnClickListener {v: View ->
            medmode(v)
        }
        high?.setOnClickListener {v: View ->
           highmode(v)
        }
        // Return the fragment view/layout
        return view
    }
    fun lowmode(view: View){
        MainActivity.dx=1.0f
        MainActivity.dy=2.0f
        Toast.makeText(activity,"You choose low speed mode.",Toast.LENGTH_SHORT).show()
    }
    fun medmode(view: View){
        MainActivity.dx=2.0f
        MainActivity.dy=4.0f
        Toast.makeText(activity,"You choose medium speed mode.",Toast.LENGTH_SHORT).show()
    }
    fun highmode(view: View){
        MainActivity.dx=3.0f
        MainActivity.dy=6.0f
        Toast.makeText(activity,"You choose high speed mode.",Toast.LENGTH_SHORT).show()
    }

}



