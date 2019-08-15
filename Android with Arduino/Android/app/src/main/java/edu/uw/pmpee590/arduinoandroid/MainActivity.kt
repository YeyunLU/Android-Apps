package edu.uw.pmpee590.arduinoandroid

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Intent
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), BLE.Callback {


    // Bluetooth
    private var ble: BLE? = null
    private var messages: TextView? = null
    private var temp: TextView? = null
    private var buzzState: Boolean? = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        val adapter: BluetoothAdapter?
        adapter = BluetoothAdapter.getDefaultAdapter()
        if (adapter != null) {
            if (!adapter.isEnabled) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)

            }        }

        // Get Bluetooth
        messages = findViewById(R.id.bluetoothText)
        messages!!.movementMethod = ScrollingMovementMethod()
        ble = BLE(applicationContext, DEVICE_NAME)

        // Check permissions
        ActivityCompat.requestPermissions(this,
            arrayOf( Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 1)


    }

    fun lightRed (v:View){
        ble!!.send("red")

    }

    fun lightGreen (v:View){
        ble!!.send("green")

    }

    fun lightBlue (v:View){
        ble!!.send("blue")

    }

    fun buzz (v:View){
        if (buzzState == true){
            Buzz.setText("BUZZ OFF !");
            ble!!.send("BUZZOFF")
            buzzState=false;
        }
        else {
            Buzz.setText("BUZZ ON !");
            ble!!.send("BUZZON")
            buzzState=true;
        }
    }

    fun start (v:View){
        ble!!.send("start")
        ble!!.send(time.text.toString())
    }

    fun clearText (v:View){
        messages!!.text=""

    }

    override fun onResume() {
        super.onResume()
        //updateButtons(false)
        ble!!.registerCallback(this)
    }


    override fun onStop() {
        super.onStop()
        ble!!.unregisterCallback(this)
        ble!!.disconnect()
    }

    fun connect(v: View) {
        startScan()
    }

    private fun startScan() {
        writeLine("Scanning for devices ...")
        ble!!.connectFirstAvailable()
    }


    /**
     * Figure out which button got pressed to
     */
    fun getTemp(v: View) {
        ble!!.send("readtemp")
        Log.i("BLE", "READ TEMP")
    }

    /**
     * Writes a line to the messages textbox
     * @param text: the text that you want to write
     */
    private fun writeLine(text: CharSequence) {
        runOnUiThread {
            messages!!.append(text)
            messages!!.append("\n")
        }
    }

      /**
     * Called when a UART device is discovered (after calling startScan)
     * @param device: the BLE device
     */
    override fun onDeviceFound(device: BluetoothDevice) {
        writeLine("Found device : " + device.name)
        writeLine("Waiting for a connection ...")
    }

    /**
     * Prints the devices information
     */
    override fun onDeviceInfoAvailable() {
        writeLine(ble!!.deviceInfo)
    }

    /**
     * Called when UART device is connected and ready to send/receive data
     * @param ble: the BLE UART object
     */
    override fun onConnected(ble: BLE) {
        writeLine("Connected!")

    }

    /**
     * Called when some error occurred which prevented UART connection from completing
     * @param ble: the BLE UART object
     */
    override fun onConnectFailed(ble: BLE) {
        writeLine("Error connecting to device!")
      }

    /**
     * Called when the UART device disconnected
     * @param ble: the BLE UART object
     */
    override fun onDisconnected(ble: BLE) {
        writeLine("Disconnected!")
    }

    /**
     * Called when data is received by the UART
     * @param ble: the BLE UART object
     * @param rx: the received characteristic
     */
    override fun onReceive(ble: BLE, rx: BluetoothGattCharacteristic) {
        writeLine("Received value: " + rx.getStringValue(0))

    }

    companion object {
        private val DEVICE_NAME = "PMP590 is awesome"
        private val REQUEST_ENABLE_BT = 0
    }
}
