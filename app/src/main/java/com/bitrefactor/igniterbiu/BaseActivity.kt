package com.bitrefactor.igniterbiu

import android.content.res.Configuration
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {
    override fun getResources(): Resources {
        val res: Resources = super.getResources()
        val config = Configuration()
        config.setToDefaults()
        config.fontScale = 0.85F
        res.updateConfiguration(config, res.displayMetrics)
        return res
    }
}