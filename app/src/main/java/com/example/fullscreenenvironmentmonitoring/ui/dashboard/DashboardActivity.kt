package com.example.fullscreenenvironmentmonitoring.ui.dashboard

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fullscreenenvironmentmonitoring.R
import com.example.fullscreenenvironmentmonitoring.data.models.*
import com.example.fullscreenenvironmentmonitoring.data.mqtt.PubTopics
import com.example.fullscreenenvironmentmonitoring.databinding.ActivityDashboardBinding
import com.example.fullscreenenvironmentmonitoring.ui.about.AboutActivity
import com.example.fullscreenenvironmentmonitoring.ui.graph.GraphActivity
import com.example.fullscreenenvironmentmonitoring.ui.settting.SettingActivity
import com.example.fullscreenenvironmentmonitoring.utils.*
import kotlinx.android.synthetic.main.activity_dashboard.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

var bt1Clicked = false
var bt2Clicked = false
var bt3Clicked = false

class DashboardActivity : AppCompatActivity(), KodeinAware {

    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var binding: ActivityDashboardBinding
    override val kodein by closestKodein()
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE)
        setParamsConstante()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        binding.lifecycleOwner = this
        supportActionBar?.title = "Dashboard"
        val factory by instance<DashboardViewModelFactory>()
        dashboardViewModel = ViewModelProvider(this, factory).get(DashboardViewModel::class.java)
        binding.viewmodel = dashboardViewModel
        ANDROID_ID = sharedPref.getString(PREFERENCES.CLIENT_ID, "AndroidMQTT")!!

        tvGraph1.setOnClickListener {
            val payload = sharedPref.getString(
                PREFERENCES.FROM_DATE,
                "12/15/08/2020"
            ) + "/" + sharedPref.getString(
                PREFERENCES.TO_DATE,
                "18/15/08/2020"
            ) + "/" + sharedPref.getString(PREFERENCES.GROUP_BY, "1m")
            dashboardViewModel.publish(PubTopics.PARAMS1.value, payload)
            bt1Clicked = true
        }
        tvGraph2.setOnClickListener {
            val payload = sharedPref.getString(
                PREFERENCES.FROM_DATE,
                "12/15/08/2020"
            ) + "/" + sharedPref.getString(
                PREFERENCES.TO_DATE,
                "18/15/08/2020"
            ) + "/" + sharedPref.getString(PREFERENCES.GROUP_BY, "1m")
            dashboardViewModel.publish(PubTopics.PARAMS2.value, payload)
            bt2Clicked = true
        }
        tvGraph3.setOnClickListener {
            val payload = sharedPref.getString(
                PREFERENCES.FROM_DATE,
                "12/15/08/2020"
            ) + "/" + sharedPref.getString(
                PREFERENCES.TO_DATE,
                "18/15/08/2020"
            ) + "/" + sharedPref.getString(PREFERENCES.GROUP_BY, "1m")
            dashboardViewModel.publish(PubTopics.PARAMS3.value, payload)
            bt3Clicked = true
        }

        dashboardViewModel.isPublished.observe(this, Observer {
            if (!it) Toast.makeText(this, "Not published", Toast.LENGTH_LONG).show()
        })

        dashboardViewModel.getParamsZone1().observe(this, Observer {
            if (bt1Clicked) {
                val intent = Intent(this, GraphActivity::class.java)
                intent.putExtra(DATA_TYPE, DataTypes.PARAMS1)
                paramsZone1 = it as ArrayList<ParamZone1>
                startActivity(intent)
                bt1Clicked = false
            }
        })

        dashboardViewModel.getParamsZone2().observe(this, Observer {
            if (bt2Clicked) {
                val intent = Intent(this, GraphActivity::class.java)
                intent.putExtra(DATA_TYPE, DataTypes.PARAMS2)
                paramsZone2 = it as ArrayList<ParamZone2>
                startActivity(intent)
                bt2Clicked = false
            }
        })

        dashboardViewModel.getParamsZone3().observe(this, Observer {
            if (bt3Clicked) {
                val intent = Intent(this, GraphActivity::class.java)
                intent.putExtra(DATA_TYPE, DataTypes.PARAMS3)
                paramsZone3 = it as ArrayList<ParamZone3>
                startActivity(intent)
                bt3Clicked = false
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.setting_menu -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.about_menu -> {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setParamsConstante() {
        ParamsLevel.temperatureLevel1 =
            sharedPref.getString(PREFERENCES.TEMPERATURE_LEVEL1, "18.5")!!.toDouble()
        ParamsLevel.temperatureLevel2 =
            sharedPref.getString(PREFERENCES.TEMPERATURE_LEVEL2, "30.5")!!.toDouble()
        ParamsLevel.humidityLevel1 =
            sharedPref.getString(PREFERENCES.HUMIDITY_LEVEL1, "45.5")!!.toDouble()
        ParamsLevel.humidityLevel2 =
            sharedPref.getString(PREFERENCES.HUMIDITY_LEVEL2, "75.5")!!.toDouble()
        ParamsLevel.airQualityLevel1 =
            sharedPref.getString(PREFERENCES.AIR_QUALITY_LEVEL1, "450.5")!!.toDouble()
        ParamsLevel.airQualityLevel2 =
            sharedPref.getString(PREFERENCES.AIR_QUALITY_LEVEL2, "800.0")!!.toDouble()
        ParamsLevel.radiationLevel1 =
            sharedPref.getString(PREFERENCES.RADIATION_LEVEL1, "150.2")!!.toDouble()
        ParamsLevel.radiationLevel2 =
            sharedPref.getString(PREFERENCES.RADIATION_LEVEL2, "400.0")!!.toDouble()
    }
}