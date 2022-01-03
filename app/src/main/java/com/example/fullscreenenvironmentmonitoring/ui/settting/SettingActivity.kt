package com.example.fullscreenenvironmentmonitoring.ui.settting

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.fullscreenenvironmentmonitoring.R
import com.example.fullscreenenvironmentmonitoring.databinding.ActivitySettingBinding
import com.example.fullscreenenvironmentmonitoring.ui.SplashActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import kotlin.system.exitProcess

class SettingActivity : AppCompatActivity(), KodeinAware {
    private lateinit var settingViewModel: SettingViewModel
    private lateinit var binding: ActivitySettingBinding
    private lateinit var sharedPref: SharedPreferences
    override val kodein by closestKodein()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting)
        val factory by instance<SettingViewModelFactory>()
        settingViewModel = ViewModelProvider(this, factory).get(SettingViewModel::class.java)
        binding.viewmodel = settingViewModel
        binding.lifecycleOwner = this
        sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE)
        settingViewModel.initializeUi(sharedPref)
        supportActionBar?.title = "Settings"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        progressBar.visibility = View.GONE

        btSave.setOnClickListener {
            with(MaterialAlertDialogBuilder(this)) {
                setTitle("Save settings")
                setMessage("Do you want to save changes?\n\nConnection config needs application restart!")
                setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                    if (settingViewModel.save(sharedPref)) {
                        showRestartDialog()
                    } else Toast.makeText(this@SettingActivity, "Error", Toast.LENGTH_LONG).show()
                })
                setNegativeButton("No", DialogInterface.OnClickListener { dialog, which -> })
                show()
            }
        }

        btPublish.setOnClickListener {
            settingViewModel.publishThresholds(sharedPref)
        }
    }

    private fun showRestartDialog(){
        with(MaterialAlertDialogBuilder(this)) {
            setTitle("Info")
            setMessage("Restart the application?")
            setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                showProgressBar()
            })
            setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->
                finish()
            })
            show()
        }
    }

    private fun restart(context: Activity) {
        val intent = Intent(context, SplashActivity::class.java)
        val mPendingIntentId = 123456
        val mPendingIntent = PendingIntent.getActivity(
            context,
            mPendingIntentId,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        val mgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent)
        exitProcess(0)
    }

    private fun showProgressBar(){
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            delay(1000)
        }.invokeOnCompletion {
            progressBar.visibility = View.GONE
            lifecycleScope.launch {
                delay(500)
            }.invokeOnCompletion {
                restart(this@SettingActivity)
            }
        }
    }
}