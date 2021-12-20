package com.bitrefactor.igniterbiu.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bitrefactor.igniterbiu.IgniterApplication.Companion.db
import com.bitrefactor.igniterbiu.MainActivity
import com.bitrefactor.igniterbiu.R
import com.bitrefactor.igniterbiu.data.localsource.entity.BleDevice


class DeviceAdapter(context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var devices: ArrayList<BleDevice> = arrayListOf()
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
            val view = inflater.inflate(R.layout.item_device, parent, false)
            return ViewHolder(view)
        }

    }

    override fun getItemViewType(position: Int): Int {

        return if (position == devices.size) ADD_VIEW else DATA_VIEW
    }


    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {

            val font: Typeface =
                Typeface.createFromAsset(context.assets, "BarlowCondensed-BlackItalic.ttf")
            val blePos = devices[position]
            holder.bleMac.text = blePos.bleAddress.toString()
            holder.title.text = blePos.bleName
            val device =
                (context as MainActivity).scanAdapter?.getDeviceByAddress(blePos.bleAddress!!)
            if (device !== null) {
                holder.bleRssi.text = device.rssi.toString() + "dB"
                if (device.isconnect == 1) {
                    holder.connect.setImageDrawable(context.getDrawable(R.drawable.connect_lost_ico))
                    holder.connect.background = context.getDrawable(R.drawable.circle_top_radius_shape_top_right_dark)
                    holder.fire.setImageDrawable(context.getDrawable(R.drawable.connect_ed_ico))
                    holder.fire.setOnClickListener {
                        evtclick?.fireClick()
                    }
                } else {
                    holder.connect.setImageDrawable(context.getDrawable(R.drawable.connect_ico))
                    holder.connect.background = context.getDrawable(R.drawable.circle_top_radius_shape_top_right)
                    holder.fire.setImageDrawable(context.getDrawable(R.drawable.connect_no))
                    holder.fire.setOnClickListener {
                        Toast.makeText(context, R.string.leftSlip, Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                holder.bleRssi.text = "-0dB"
            }
            holder.igniterV.typeface = font
            if (blePos.v) {
                holder.igniterV.visibility = View.VISIBLE
            } else {
                holder.igniterV.visibility = View.GONE
            }
            holder.delDevice.setOnClickListener {
                var dbDevice: BleDevice? = null
                db?.bleDeviceDao()?.getAll()?.forEach constituting@{ bled ->
                    if (bled.bleAddress == devices[position].bleAddress) {
                        dbDevice = bled
                        return@constituting
                    }
                }
                db?.bleDeviceDao()?.delete(dbDevice!!)
                devices.remove(devices[position])
                notifyDataSetChanged()
            }
            holder.connect.setOnClickListener {
                evtclick?.connectDbDeviceEvt(position)
            }
            holder.itemView.setOnClickListener {
                Log.d("lalalalla", "------------------------------------------l")
            }
        } else if (holder is AddViewHolder) {
            holder.itemView.setOnClickListener {
                this.evtclick?.addClick()
            }
        }
    }

    override fun getItemCount(): Int {
        return devices.size + 1;
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.ble_name)
        var layout: LinearLayout = view.findViewById(R.id.item_device)
        var connect: ImageView = view.findViewById(R.id.connect)
        var bleRssi: TextView = view.findViewById(R.id.ble_rssi)
        var bleMac: TextView = view.findViewById(R.id.ble_mac)
        var delDevice: ImageView = view.findViewById(R.id.del_device)
        var fire: ImageView = view.findViewById(R.id.fire)
        var igniterV: TextView = view.findViewById(R.id.igniter_v)
    }

    class AddViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

    interface EventClick {
        fun addClick()
        fun fireClick()
        fun connectDbDeviceEvt(position: Int)
    }

    private var evtclick: EventClick? = null

    //实现那个接口
    fun setEventClick(onItemClick: EventClick?) {
        this.evtclick = onItemClick
    }

}