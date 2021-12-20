package com.bitrefactor.igniterbiu

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bitrefactor.igniterbiu.IgniterApplication.Companion.db
import com.bitrefactor.igniterbiu.adapter.DeviceAdapter
import com.bitrefactor.igniterbiu.adapter.RelatedEventsAdapter
import com.bitrefactor.igniterbiu.adapter.ScanDeviceAdapter
import com.bitrefactor.igniterbiu.data.bean.*
import com.bitrefactor.igniterbiu.data.localsource.entity.BleDevice
import com.bitrefactor.igniterbiu.databinding.ActivityMainBinding
import com.bitrefactor.igniterbiu.utils.B2Str.parseBytesToHexString
import com.bitrefactor.igniterbiu.utils.B2Str.parseHexStringToBytes
import com.bitrefactor.igniterbiu.utils.BleAdvertisedData
import com.bitrefactor.igniterbiu.utils.BleUtil
import com.bitrefactor.igniterbiu.views.DeviceRecyclerView
import com.bitrefactor.igniterbiu.views.MarginDecoration
import com.bitrefactor.igniterbiu.views.WaveView
import com.tbruyelle.rxpermissions3.RxPermissions
import java.util.*
import kotlin.collections.ArrayList
import kotlin.experimental.and
import kotlin.system.exitProcess


private const val SCAN_PERIOD: Long = 10000

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    companion object {
        const val TAG = "MainActivity"
        const val REQUEST_ENABLE_BLUETOOTH = 1
    }

    var grant: Boolean = false      //权限

    private var mScanning: Boolean = false   //扫描启动
    private var leScanCallback: MainActivity.LeScanCallback? = null
    var bluetoothGatt: BluetoothGatt? = null
    var bleServiceList:ArrayList<BLEServiceBean>?= arrayListOf()
    var characteristic: BluetoothGattCharacteristic? = null

    var leDeviceListAdapter: DeviceAdapter? = null
    var scanAdapter: ScanDeviceAdapter? = null

    var contentViews: View? = null
    var waveView: WaveView? = null
    var bottomDialog: Dialog? = null

    var aboutDialog:Dialog? = null

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPermissions()  //初始化蓝牙相关权限
        if (!grant) {
            exitProcess(1)
        }
        initView()
        initLocalData()
        initBLE5()
        leScanCallback = LeScanCallback()

        createDialog()  //创建扫描窗口
        initConnectToast()
        mScanning = true
        scanLeDevice(leScanCallback!!)

        createAboutDialog()
        leDeviceListAdapter!!.setEventClick(object : DeviceAdapter.EventClick {
            @SuppressLint("NotifyDataSetChanged")
            override fun addClick() {
                bottomDialog?.show()
                scanAdapter!!.setDeviceConnectLose()
                bluetoothGatt?.disconnect()
                mScanning = true
                scanLeDevice(leScanCallback!!)
            }

            override fun fireClick() {
                var serviceUUID:UUID?=null
                var characteristicUUID:UUID?= null
                    bluetoothGatt?.services?.forEach constituting@ { service->
                    service.characteristics.forEach { characteristic->
                        if (characteristic.properties.toByte() and 8 == (8).toByte() ){
                            serviceUUID=service.uuid
                            characteristicUUID = characteristic.uuid
                            Log.d(TAG,serviceUUID.toString()+"-----------"+characteristicUUID.toString()+"====="+characteristic.properties.toByte())
                            return@constituting
                      }
                    }
                }
                val service =
                    bluetoothGatt?.getService(serviceUUID);
                if (service != null) {
                    characteristic =
                        service.getCharacteristic(characteristicUUID);
                    if (characteristic != null) {
                        bluetoothGatt?.setCharacteristicNotification(
                            characteristic,
                            true
                        )
                        Log.d(TAG,"write successful")
                        characteristic?.value = parseHexStringToBytes("fire");
                        bluetoothGatt?.writeCharacteristic(characteristic); }
                }else{
                    Log.d(TAG,"write fail")
                    Toast.makeText(this@MainActivity, R.string.serviceIsNull, Toast.LENGTH_SHORT).show()
                }
            }

            override fun connectDbDeviceEvt(position: Int) {
                if (scanAdapter?.getDeviceByAddress(leDeviceListAdapter!!.devices[position].bleAddress!!) == null) {
                    Toast.makeText(this@MainActivity, R.string.sofar, Toast.LENGTH_SHORT).show()
                }else if(scanAdapter?.getDeviceByAddress(leDeviceListAdapter!!.devices[position].bleAddress!!)!!.isconnect == 1){
                    Toast.makeText(this@MainActivity, R.string.disconnected, Toast.LENGTH_SHORT).show()
                    scanAdapter!!.setDeviceConnectLose()
                    bluetoothGatt!!.disconnect()
                } else {
                    connectGATT(
                        scanAdapter?.getDeviceByAddress(leDeviceListAdapter!!.devices[position].bleAddress!!)!!.bluetoothDevice,
                        SUCCESSFUL,
                        position
                    )
                }
            }
        })

        binding.related.setOnClickListener {
            aboutDialog?.show()
        }

    }

    private fun createAboutDialog() {
        aboutDialog = Dialog(this@MainActivity, R.style.BottomDialog)
        var aboutContentViews = LayoutInflater.from(this@MainActivity)
            .inflate(R.layout.dialog_about, null)
        aboutDialog!!.setContentView(aboutContentViews!!)
        val params = aboutContentViews!!.layoutParams as ViewGroup.MarginLayoutParams
        params.width = resources.displayMetrics!!.widthPixels
        params.bottomMargin = 0
        aboutContentViews!!.layoutParams = params
        aboutDialog!!.window!!.setGravity(Gravity.BOTTOM)
        aboutDialog!!.window!!.setWindowAnimations(R.style.BottomInBottomOut)
        val rc = aboutContentViews.findViewById<RecyclerView>(R.id.recyclerView)
        val layoutManager = object :GridLayoutManager(this, 4){
            override fun canScrollVertically(): Boolean {
                return false;
            }
        }
        rc.layoutManager = layoutManager
        val evt:ArrayList<Evt> = arrayListOf()
        evt.add(Evt(R.string.aboutWe,R.drawable.about_ico){openAboutSoftware()})
        evt.add(Evt(R.string.instructions,R.drawable.using_ico) {openOutWeb("https://github.com/bitrefactor/igniterBiu/blob/main/README.md")})
        evt.add(Evt(R.string.bitrefactor,R.drawable.bitrefactor_ico) { openOutWeb("https://www.bitrefactor.com") })
        evt.add(Evt(R.string.github,R.drawable.github_ico) { openOutWeb("https://github.com/bitrefactor") })
        val adapter = RelatedEventsAdapter(this, evt )
        rc.adapter=adapter

        aboutContentViews.findViewById<ImageView>(R.id.back_iv).setOnClickListener {
            aboutDialog?.dismiss()
        }
    }
    /**
     * 打开外部浏览器
     * Open external browser
     */
    private fun openAboutSoftware(){
        val softToast = Toast(this@MainActivity)
        val softContentViews = LayoutInflater.from(this@MainActivity)
            .inflate(R.layout.dialog_software, null)
        softToast.setView(softContentViews!!)
        softToast.setGravity(Gravity.CENTER,0,0)
        softToast.duration = Toast.LENGTH_LONG
        softToast.show()
    }

    /**
     * 打开外部浏览器
     * Open external browser
     */
    private fun openOutWeb(url:String){
        val uri: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    /**
     * 初始化蓝牙相关的权限
     * Initialize Bluetooth related permissions
     */
    private fun initPermissions(): Boolean {
        val rxPermissions = RxPermissions(this)
        rxPermissions.request(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ).subscribe { granted ->
            grant = if (granted) {
                true
            } else {
                Toast.makeText(this, R.string.requestPermission, Toast.LENGTH_SHORT).show()
                false
            }
        }
        return grant
    }

    /**
     * 初始化本地数据库保存的设备数据
     * Initialize the device data saved by the local database
     */
    private fun initLocalData() {
        leDeviceListAdapter?.devices = db?.bleDeviceDao()?.getAll() as ArrayList<BleDevice>
    }


    /**
     * 初始化布局
     * init view
     */
    private fun initView() {

        val font: Typeface =
            Typeface.createFromAsset(this.assets, "BarlowCondensed-BlackItalic.ttf")
        binding.title.typeface = font
        val rc = findViewById<DeviceRecyclerView>(R.id.recyclerView)
        val layoutManager = GridLayoutManager(this, 2)
        rc.layoutManager = layoutManager
        rc.addItemDecoration(MarginDecoration(this, 15F))
        scanAdapter = ScanDeviceAdapter(this@MainActivity)
        leDeviceListAdapter = DeviceAdapter(this)
        rc.adapter = leDeviceListAdapter

    }

    /**
     * 初始化蓝牙相关
     * Init Bluetooth
     */
    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }
    private val BluetoothAdapter.isDisabled: Boolean
        get() = !isEnabled

    @SuppressLint("MissingPermission")
    private fun initBLE5() {
        bluetoothAdapter?.takeIf { it.isDisabled }?.apply {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BLUETOOTH)
        }
    }

    /**
     * 初始化扫描弹窗
     * Init scanning pop
     */
    @SuppressLint("InflateParams", "NotifyDataSetChanged", "MissingPermission")
    private fun createDialog() {
        bottomDialog = Dialog(this@MainActivity, R.style.BottomDialog)
        contentViews = LayoutInflater.from(this@MainActivity)
            .inflate(R.layout.item_add_ble_dialog, null)
        bottomDialog!!.setContentView(contentViews!!)
        val params = contentViews!!.layoutParams as ViewGroup.MarginLayoutParams
        params.width = resources.displayMetrics!!.widthPixels
        params.bottomMargin = 0
        contentViews!!.layoutParams = params
        bottomDialog!!.window!!.setGravity(Gravity.BOTTOM)
        bottomDialog!!.window!!.setWindowAnimations(R.style.BottomInBottomOut)

        val rcScan = contentViews!!.findViewById<RecyclerView>(R.id.recyclerView_scan)
        val layoutManager = GridLayoutManager(this@MainActivity, 2)
        rcScan.layoutManager = layoutManager
        rcScan.addItemDecoration(MarginDecoration(this@MainActivity, 15F))
        rcScan.adapter = scanAdapter
        waveView = contentViews!!.findViewById(R.id.wave_view)
        waveView?.bringToFront()
        contentViews?.findViewById<ImageView>(R.id.scan_iv)?.bringToFront()

        contentViews?.findViewById<ImageView>(R.id.scan_iv)?.setOnClickListener {
            waveView?.toggle()
            mScanning = waveView?.isWaveRunning == true
            scanLeDevice(leScanCallback!!)
            Toast.makeText(
                this@MainActivity, if (waveView?.isWaveRunning == true) R.string.rescan else R.string.stopscan,
                Toast.LENGTH_SHORT
            ).show()
        }

        contentViews!!.findViewById<ImageView>(R.id.back_iv)
            .setOnClickListener {
                bottomDialog?.dismiss();leDeviceListAdapter?.devices = db?.bleDeviceDao()
                ?.getAll() as ArrayList<BleDevice>;
                leDeviceListAdapter!!.notifyDataSetChanged()
            }

        scanAdapter!!.setEventClick(object : ScanDeviceAdapter.EventClick {
            override fun connectClick(blePos: BluetoothDevice, position: Int) {
                bottomDialog?.dismiss()
                cToast?.show()
                connectGATT(blePos, SUCCESSFUL_ON_SCAN, position)
            }
        })
    }

    @SuppressLint("MissingPermission")
    private fun connectGATT(device: BluetoothDevice, successfulMsgWhat: Int, position: Int) {
        bluetoothGatt = device.connectGatt(this@MainActivity, false,
            object : BluetoothGattCallback() {
                override fun onConnectionStateChange(
                    gatt: BluetoothGatt,
                    status: Int,
                    newState: Int
                ) {
                    if (newState == BluetoothProfile.STATE_CONNECTED) {
                        bluetoothGatt?.discoverServices();
                        biuHandler(position, successfulMsgWhat, "successful")
                        bluetoothGatt?.services?.forEach{service->
                            val characteristicBeanList:ArrayList<CharacteristicBean> = arrayListOf()
                            service.characteristics.forEach {characteristic->
                                val characteristicBean = CharacteristicBean(characteristic.uuid,characteristic.permissions)
                                characteristicBeanList.add(characteristicBean)
                            }
                            bleServiceList?.add(BLEServiceBean(service.uuid,characteristicBeanList))
                        }

                    }else if(newState == BluetoothProfile.STATE_DISCONNECTED){
                        scanAdapter!!.setDeviceConnectLose()
                    }
                    if (status == BluetoothGatt.GATT_FAILURE){
                        biuHandler(-1, FAIL, "fail")
                    }
                }

                override fun onCharacteristicWrite(
                    gatt: BluetoothGatt?,
                    characteristic: BluetoothGattCharacteristic?,
                    status: Int
                ) {
                    if (gatt != null) {
                        biuHandler(-1, WRITE_SUCCESSFUL, "write successfully")
                        Log.e(TAG, gatt.device.name + " write successfully")
                    };
                }

                override fun onCharacteristicRead(
                    gatt: BluetoothGatt?,
                    characteristic: BluetoothGattCharacteristic?,
                    status: Int
                ) {
                    val response: String = parseBytesToHexString(
                        characteristic!!.value
                    )
                    Log.e(TAG, "The response is $response")
                }
            })
    }

    /**
     * 触发handler
     * Trigger Handler
     */
    fun biuHandler(position: Int, status: Int, msg: String) {
        Log.e(TAG, msg)
        val msg = Message.obtain()
        msg.what = status
        val bundle = Bundle()
        bundle.putInt("position", position)
        msg.data = bundle
        handler.sendMessage(msg)
    }

    @SuppressLint("MissingPermission", "NotifyDataSetChanged")
    var handler: Handler = Handler { p0 ->
        cToast?.dismiss()
        when (p0.what) {
            SUCCESSFUL_ON_SCAN -> {
                Toast.makeText(this@MainActivity, R.string.connected, Toast.LENGTH_SHORT).show()
                val cacheDevice: RxBluetoothDevice =
                    scanAdapter!!.devices[p0.data.getInt("position")]
                db?.bleDeviceDao()?.insertAll(
                    BleDevice(
                        cacheDevice.bluetoothDevice.name,
                        cacheDevice.bluetoothDevice.address,
                        null,
                        cacheDevice.bluetoothDevice.name.toString()
                            .contains("Igniter") || cacheDevice.bluetoothDevice.name.toString()
                            .contains("igniter")
                    )
                )
                initLocalData()
                scanAdapter?.setDeviceConnectStatusByAddress(scanAdapter!!.devices[p0.data.getInt("position")].bluetoothDevice.address)
                leDeviceListAdapter?.notifyDataSetChanged()
            }
            SUCCESSFUL -> {
                Toast.makeText(this@MainActivity, R.string.connected, Toast.LENGTH_SHORT).show()
                scanAdapter?.setDeviceConnectStatusByAddress(
                    leDeviceListAdapter?.devices?.get(
                        p0.data.getInt(
                            "position"
                        )
                    )?.bleAddress.toString()
                )
                leDeviceListAdapter?.notifyDataSetChanged()
            }
            FAIL -> {
                Toast.makeText(this@MainActivity, R.string.connectFail, Toast.LENGTH_SHORT).show()
            }
            WRITE_SUCCESSFUL->{
                Toast.makeText(this@MainActivity, R.string.writeSuccessfully, Toast.LENGTH_SHORT).show()
            }
        }
        true
    }

    /**
     * 创建连接loading
     * Create a connection loading
     */
    private var cToast: Dialog? = null
    private fun initConnectToast() {
        cToast = Dialog(this@MainActivity)
        var cToasts = LayoutInflater.from(this@MainActivity)
            .inflate(R.layout.dialog_loading, null)
        cToast!!.setContentView(cToasts)
        cToast!!.window?.setDimAmount(0F)
    }

    /**
     * 蓝牙扫描回调函数
     * Bluetooth scanning callback function
     */
    @SuppressLint("MissingPermission")
    private inner class LeScanCallback : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
        }

        @RequiresApi(Build.VERSION_CODES.R)
        @SuppressLint("NotifyDataSetChanged")
        override fun onBatchScanResults(results: List<ScanResult>) {
            for (result in results) {
                runOnUiThread {
                    var name = "unknown_device"
                    try {
                        val bigdata: BleAdvertisedData =
                            BleUtil.parseAdvertisedData(result.scanRecord?.bytes) //可能扫描不到蓝牙名 # May scanning the Bluetooth name
                        if (result.device.name.isNullOrEmpty()) {
                            if (!bigdata.name.isNullOrEmpty()) {
                                name = bigdata.name.toString()
                            }
                        } else {
                            name = result.device.name
                        }
                    } catch (e: Exception) {
                    }
                    var isbond = 3
                    if (result.device.type == BluetoothDevice.DEVICE_TYPE_LE) {
                        isbond = 1
                    }
                    leDeviceListAdapter?.devices?.forEach constituting@{ l ->
                        if (result.device.address == l.bleAddress) {
                            isbond = 2
                            return@constituting
                        }
                    }
                    if (isbond != 3) {
                        scanAdapter?.addItem(
                            RxBluetoothDevice(
                                name,
                                result.rssi,
                                isbond,
                                0,
                                result.device
                            )
                        )
                    }
                    scanAdapter?.notifyDataSetChanged()
                    leDeviceListAdapter?.notifyDataSetChanged()
                }
            }
        }

        override fun onScanFailed(errorCode: Int) {
            Log.d("DBG", "BLE Scan Failed with code $errorCode")
        }
    }

    /**
     * 蓝牙扫描执行函数
     * Bluetooth scan execution function
     */
    @SuppressLint("MissingPermission")
    private fun scanLeDevice(leScanCallback: ScanCallback) {
        val settings: ScanSettings = ScanSettings.Builder()
            .setLegacy(false)
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .setReportDelay(500)
            .build()
        when (mScanning) {
            true -> {
                scanAdapter?.clearItem()
                mScanning = true
                bluetoothAdapter?.bluetoothLeScanner?.startScan(null, settings, leScanCallback)
            }
            else -> {
                mScanning = false
                bluetoothAdapter?.bluetoothLeScanner?.stopScan(leScanCallback)
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onDestroy() {
        super.onDestroy()
        bluetoothGatt?.disconnect()
    }
}