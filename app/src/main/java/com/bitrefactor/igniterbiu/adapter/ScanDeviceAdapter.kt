package com.bitrefactor.igniterbiu.adapter

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bitrefactor.igniterbiu.R
import com.bitrefactor.igniterbiu.data.bean.RxBluetoothDevice


class ScanDeviceAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var devices: ArrayList<RxBluetoothDevice> = arrayListOf()

    private val inflater: LayoutInflater
    private val context: Context

    private val ADD_VIEW = 1
    private val DATA_VIEW = 2

    init {
        inflater = LayoutInflater.from(context)
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == ADD_VIEW) {
            val view = inflater.inflate(R.layout.item_devices_add, parent, false)
            return AddViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.item_ble, parent, false)
            return ViewHolder(view)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return DATA_VIEW
    }

    @SuppressLint("MissingPermission", "CutPasteId", "SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            val blePos = devices[position].bluetoothDevice
            holder.title.text = blePos.address
            holder.itemView.findViewById<TextView>(R.id.ble_mac).text =
                blePos.address.toString()
            var name = devices[position].string

            holder.itemView.findViewById<TextView>(R.id.ble_name).text = name
            holder.itemView.findViewById<TextView>(R.id.ble_rssi).text =
                devices[position].rssi.toString() + "dB"
            val font: Typeface =
                Typeface.createFromAsset(context.assets, "BarlowCondensed-BlackItalic.ttf")
            holder.itemView.findViewById<TextView>(R.id.igniter_v).typeface = font
            if (devices[position].isbond == 2) {
                holder.itemView.findViewById<TextView>(R.id.add).visibility = View.VISIBLE
                holder.itemView.findViewById<TextView>(R.id.add).text = "BOND"
                holder.itemView.findViewById<TextView>(R.id.add).background =
                    context.getDrawable(R.drawable.circle_radius_shape_theme_fa)
                holder.itemView.findViewById<TextView>(R.id.add).isClickable = false
            } else if (devices[position].isbond == 1) {
                holder.itemView.findViewById<TextView>(R.id.add).visibility = View.VISIBLE
                holder.itemView.findViewById<TextView>(R.id.add).text = "ADD"
                holder.itemView.findViewById<TextView>(R.id.add).background =
                    context.getDrawable(R.drawable.circle_radius_shape_theme)
                holder.itemView.findViewById<TextView>(R.id.add).setOnClickListener {
                    this.evtclick?.connectClick(blePos, position)
                }
            } else if (devices[position].isbond == 3) {
                holder.itemView.findViewById<TextView>(R.id.add).visibility = View.GONE
            }
            if (name.contains("Igniter") || name.contains("igniter")) {
                holder.itemView.findViewById<TextView>(R.id.igniter_v).visibility = View.VISIBLE
            } else {
                holder.itemView.findViewById<TextView>(R.id.igniter_v).visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return devices.size;
    }

    // This removes the data from our Dataset and Updates the Recycler View.
    @SuppressLint("NotifyDataSetChanged")
    fun removeItem(infoData: RxBluetoothDevice) {
        devices.add(infoData)
        notifyDataSetChanged()
    }

    // This removes the data from our Dataset and Updates the Recycler View.
    @SuppressLint("NotifyDataSetChanged")
    fun clearItem() {
        devices.clear()
        notifyDataSetChanged()
    }

    // This method adds(duplicates) a Object (item ) to our Data set as well as Recycler View.
    @SuppressLint("NotifyDataSetChanged")
    fun addItem(infoData: RxBluetoothDevice) {
        if (getInstruction(devices, infoData.bluetoothDevice.address) == null) {
            devices.add(infoData)
        } else {
            infoData.isconnect = getInstruction(devices, infoData.bluetoothDevice.address)!!.isconnect
            devices[devices.indexOf(getInstruction(devices, infoData.bluetoothDevice.address))] =
                infoData
        }
        notifyDataSetChanged()
    }

    private fun getInstruction(
        devices: ArrayList<RxBluetoothDevice>,
        address: String
    ): RxBluetoothDevice? {
        val iterator: Iterator<RxBluetoothDevice> = devices.iterator()
        while (iterator.hasNext()) {
            val current: RxBluetoothDevice = iterator.next()
            if (current.bluetoothDevice.address.equals(address)) return current
        }
        return null
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.ble_name)
    }

    class AddViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }


    interface EventClick {
        fun connectClick(blePos: BluetoothDevice, position: Int)
    }

    private var evtclick: EventClick? = null

    //实现那个接口
    fun setEventClick(onItemClick: EventClick?) {
        this.evtclick = onItemClick
    }

    //数据工具类
    fun getDeviceByAddress(address: String): RxBluetoothDevice? {
        var rxi: RxBluetoothDevice? = null
        devices.forEach { i ->
            if (i.bluetoothDevice.address == address) {
                rxi = i
            }
        }
        return rxi
    }
    fun setDeviceConnectStatusByAddress(address: String) {
        val device = getInstruction(devices, address)
        devices.forEach { i ->
            i.isconnect = 0
            devices[devices.indexOf(i)] = i
        }
        device?.isconnect = 1
        devices[devices.indexOf(getInstruction(devices, address))] = device!!
    }
    fun setDeviceConnectLose() {
        devices.forEach { i ->
            i.isconnect = 0
            devices[devices.indexOf(i)] = i
        }
    }

}