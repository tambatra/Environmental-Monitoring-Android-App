package com.example.fullscreenenvironmentmonitoring.ui.graph

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.fullscreenenvironmentmonitoring.R
import com.example.fullscreenenvironmentmonitoring.data.models.paramsZone1
import com.example.fullscreenenvironmentmonitoring.data.models.paramsZone2
import com.example.fullscreenenvironmentmonitoring.data.models.paramsZone3
import com.example.fullscreenenvironmentmonitoring.utils.DataTypes
import com.example.fullscreenenvironmentmonitoring.utils.DATA_TYPE
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.activity_graph.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.max
import kotlin.math.min


lateinit var dType: DataTypes
var startTime:Long = 0L
var airQMax = 0f
var airQMin = 0f
var humMax = 0f
var humMin = 0f
var tempMax = 0f
var tempMin = 0f
var radMax = 0f
var radMin = 0f
var isTempVisible = false
var isHumVisible = false
var isAirQVisible = true
var isRadVisible = false
var radValue = ArrayList<Entry>()
var tempValue = ArrayList<Entry>()
var humValue = ArrayList<Entry>()
var airQValue = ArrayList<Entry>()
class GraphActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)
        dType = intent.extras?.get(DATA_TYPE) as DataTypes
        airQValue.clear()
        tempValue.clear()
        humValue.clear()
        radValue.clear()
        setCheckBoxVisibility()
        initData()
        with(lineChart) {
            description.isEnabled = false
            //setTouchEnabled(true)
            isDragEnabled = true
            dragDecelerationFrictionCoef = 0.9f
            setScaleEnabled(true)
            setDrawGridBackground(false)
            isHighlightPerDragEnabled = true
            setPinchZoom(true)
            setBackgroundColor(Color.WHITE)
            setViewPortOffsets(0f, 0f, 0f, 0f)
            animateY(1000)
        }
        val l: Legend = lineChart.legend
        l.isEnabled = false
        val xAxis = lineChart.xAxis
        with(xAxis) {
            position = XAxis.XAxisPosition.TOP_INSIDE
            typeface = Typeface.SERIF
            textSize = 14f
            setDrawGridLines(false)
            setDrawAxisLine(false)
            textColor = resources.getColor(R.color.colorText)
            granularity = 1f // one hour
            valueFormatter = object : ValueFormatter() {
                val mFormatter = SimpleDateFormat("dd MMM HH:mm", Locale.ENGLISH)
                override fun getFormattedValue(value: Float): String {
                    val base:Long = startTime + value.toLong()
                    return mFormatter.format(Date(base))
                }
            }
        }
        val leftAxis = lineChart.axisLeft
        with(leftAxis) {
            typeface = Typeface.SERIF
            textSize = 12f
            setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
            axisMaximum = airQMax
            axisMinimum = airQMin
            setDrawGridLines(false)
            isGranularityEnabled = true
            setDrawZeroLine(true)
            yOffset = -10f
            textColor = resources.getColor(R.color.colorText)
        }
        val rightAxis = lineChart.axisRight
        with(rightAxis){
            typeface = Typeface.SERIF
            textSize = 12f
            setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
            if(dType == DataTypes.PARAMS3){
                axisMaximum = radMax
                axisMinimum = radMin
            }
            else{
                axisMaximum = humMax
                axisMinimum = tempMin
            }
            setDrawGridLines(false)
            isGranularityEnabled = true
            setDrawZeroLine(true)
            yOffset = -10f
            textColor = resources.getColor(R.color.colorText)
        }
        setData(isTempVisible, isHumVisible, isAirQVisible, isRadVisible)
        lineChart.invalidate()

        cbTemperature.setOnCheckedChangeListener { _, isChecked ->
            isTempVisible = isChecked
            if(isTempVisible && isHumVisible) {
                lineChart.getAxis(YAxis.AxisDependency.RIGHT).axisMinimum = tempMin
                lineChart.getAxis(YAxis.AxisDependency.RIGHT).axisMaximum = humMax
            }
            else if(isTempVisible && !isHumVisible){
                lineChart.getAxis(YAxis.AxisDependency.RIGHT).axisMaximum = tempMax
                lineChart.getAxis(YAxis.AxisDependency.RIGHT).axisMinimum = tempMin
            }
            else {
                lineChart.getAxis(YAxis.AxisDependency.RIGHT).axisMaximum = humMax
                lineChart.getAxis(YAxis.AxisDependency.RIGHT).axisMinimum = humMin
            }
            setData(isTempVisible, isHumVisible, isAirQVisible, isRadVisible)
            lineChart.invalidate()
        }

        cbHumidity.setOnCheckedChangeListener { buttonView, isChecked ->
            isHumVisible = isChecked
            if(isTempVisible && isHumVisible) {
                lineChart.getAxis(YAxis.AxisDependency.RIGHT).axisMinimum = tempMin
                lineChart.getAxis(YAxis.AxisDependency.RIGHT).axisMaximum = humMax
            }
            else if(isTempVisible && !isHumVisible){
                lineChart.getAxis(YAxis.AxisDependency.RIGHT).axisMaximum = tempMax
                lineChart.getAxis(YAxis.AxisDependency.RIGHT).axisMinimum = tempMin
            }
            else{
                lineChart.getAxis(YAxis.AxisDependency.RIGHT).axisMaximum = humMax
                lineChart.getAxis(YAxis.AxisDependency.RIGHT).axisMinimum = humMin
            }
            setData(isTempVisible, isHumVisible, isAirQVisible, isRadVisible)
            lineChart.invalidate()
        }

        cbAirQuality.setOnCheckedChangeListener { buttonView, isChecked ->
            //val setAirQ = lineChart.data.getDataSetByIndex(2)
            isAirQVisible = isChecked
            //setAirQ.isVisible = isAirQVisible
            if(isRadVisible){
                lineChart.getAxis(YAxis.AxisDependency.RIGHT).axisMaximum = radMax
                lineChart.getAxis(YAxis.AxisDependency.RIGHT).axisMinimum = radMin
            }
            setData(isTempVisible, isHumVisible, isAirQVisible, isRadVisible)
            lineChart.invalidate()
        }
        cbRadiation.setOnCheckedChangeListener { buttonView, isChecked ->
            isRadVisible = isChecked
            if(isRadVisible){
                lineChart.getAxis(YAxis.AxisDependency.RIGHT).axisMaximum = radMax
                lineChart.getAxis(YAxis.AxisDependency.RIGHT).axisMinimum = radMin
            }
            setData(isTempVisible, isHumVisible, isAirQVisible, isRadVisible)
            lineChart.invalidate()
        }
    }

    private fun configSet(set:LineDataSet, mColor:Int, mIsVisible:Boolean){
        with(set){
            color = mColor
            isVisible = mIsVisible
            lineWidth = 1.5f
            isHighlightEnabled = false
            mode = LineDataSet.Mode.CUBIC_BEZIER
            setDrawCircles(false)
            setDrawValues(false)
            fillAlpha = 65
            fillColor = ColorTemplate.getHoloBlue()
            highLightColor = Color.rgb(244, 117, 117)
            setDrawCircleHole(false)
        }
    }

    private fun setData(tempIsVisible:Boolean, humIsVisible:Boolean, airQIsVisible:Boolean, radIsVisible:Boolean) {
        val dataSets: ArrayList<ILineDataSet> = ArrayList()
        when(dType){
            DataTypes.PARAMS1 ->{
                val tempSet = LineDataSet(tempValue, "Temperature")
                val humSet = LineDataSet(humValue, "Humidity")
                val airQSet = LineDataSet(airQValue, "Air quality")
                configSet(tempSet, resources.getColor(R.color.colorTemperature), tempIsVisible)
                configSet(humSet, resources.getColor(R.color.colorHumidity), humIsVisible)
                configSet(airQSet, resources.getColor(R.color.colorAirQuality), airQIsVisible)
                tempSet.axisDependency = YAxis.AxisDependency.RIGHT
                humSet.axisDependency = YAxis.AxisDependency.RIGHT
                airQSet.axisDependency = YAxis.AxisDependency.LEFT
                dataSets.add(tempSet)
                dataSets.add(humSet)
                dataSets.add(airQSet)
            }
            DataTypes.PARAMS2 ->{
                val tempSet = LineDataSet(tempValue, "Temperature")
                val humSet = LineDataSet(humValue, "Humidity")
                val airQSet = LineDataSet(airQValue, "Air quality")
                configSet(tempSet, resources.getColor(R.color.colorTemperature), tempIsVisible)
                configSet(humSet, resources.getColor(R.color.colorHumidity), humIsVisible)
                configSet(airQSet, resources.getColor(R.color.colorAirQuality), airQIsVisible)
                tempSet.axisDependency = YAxis.AxisDependency.RIGHT
                humSet.axisDependency = YAxis.AxisDependency.RIGHT
                airQSet.axisDependency = YAxis.AxisDependency.LEFT
                dataSets.add(tempSet)
                dataSets.add(humSet)
                dataSets.add(airQSet)
            }
            DataTypes.PARAMS3 ->{
                val radSet = LineDataSet(radValue, "Radiation")
                val airQSet = LineDataSet(airQValue, "Air quality")
                configSet(radSet, resources.getColor(R.color.colorRadiation), radIsVisible)
                configSet(airQSet, resources.getColor(R.color.colorAirQuality), airQIsVisible)
                radSet.axisDependency = YAxis.AxisDependency.RIGHT
                airQSet.axisDependency = YAxis.AxisDependency.LEFT
                dataSets.add(radSet)
                dataSets.add(airQSet)
            }
        }
        val data = LineData(dataSets)
        lineChart.data = data
    }

    private fun initData(){
        when (dType) {
            DataTypes.PARAMS1 -> {
                isTempVisible = false
                isHumVisible = false
                isAirQVisible = true
                isRadVisible = false
                startTime = paramsZone1[0].time
                airQMax = paramsZone1[0].airQuality.toFloat()
                airQMin = paramsZone1[0].airQuality.toFloat()
                humMax = paramsZone1[0].humidity.toFloat()
                humMin = paramsZone1[0].humidity.toFloat()
                tempMax = paramsZone1[0].temperature.toFloat()
                tempMin = paramsZone1[0].temperature.toFloat()
                paramsZone1.forEach {paramZone1 ->
                    airQMax = max(airQMax, paramZone1.airQuality.toFloat())
                    airQMin = min(airQMin, paramZone1.airQuality.toFloat())
                    humMax = max(humMax, paramZone1.humidity.toFloat())
                    humMin = min(humMin, paramZone1.humidity.toFloat())
                    tempMax = max(tempMax, paramZone1.temperature.toFloat())
                    tempMin = min(tempMin, paramZone1.temperature.toFloat())
                    val t = paramZone1.time - startTime
                    airQValue.add(Entry(
                        t.toFloat(),
                        paramZone1.airQuality.toFloat()
                    ))
                    tempValue.add(Entry(
                        t.toFloat(),
                        paramZone1.temperature.toFloat()
                    ))
                    humValue.add(Entry(
                        t.toFloat(),
                        paramZone1.humidity.toFloat()
                    ))
                }
                airQMax += airQMax * 0.2f
                airQMin -= airQMin * 0.3f
                tempMax += tempMax * 0.2f
                tempMin -= tempMin * 0.3f
                humMax += humMax * 0.2f
                humMin -= humMin * 0.3f
            }
            DataTypes.PARAMS2 -> {
                isTempVisible = false
                isHumVisible = false
                isAirQVisible = true
                isRadVisible = false
                startTime = paramsZone2[0].time
                airQMax = paramsZone2[0].airQuality.toFloat()
                airQMin = paramsZone2[0].airQuality.toFloat()
                humMax = paramsZone2[0].humidity.toFloat()
                humMin = paramsZone2[0].humidity.toFloat()
                tempMax = paramsZone2[0].temperature.toFloat()
                tempMin = paramsZone2[0].temperature.toFloat()
                paramsZone2.forEach {paramZone2 ->
                    airQMax = max(airQMax, paramZone2.airQuality.toFloat())
                    airQMin = min(airQMin, paramZone2.airQuality.toFloat())
                    humMax = max(humMax, paramZone2.humidity.toFloat())
                    humMin = min(humMin, paramZone2.humidity.toFloat())
                    tempMax = max(tempMax, paramZone2.temperature.toFloat())
                    tempMin = min(tempMin, paramZone2.temperature.toFloat())
                    val t = paramZone2.time - startTime
                    airQValue.add(Entry(
                        t.toFloat(),
                        paramZone2.airQuality.toFloat()
                    ))
                    tempValue.add(Entry(
                        t.toFloat(),
                        paramZone2.temperature.toFloat()
                    ))
                    humValue.add(Entry(
                        t.toFloat(),
                        paramZone2.humidity.toFloat()
                    ))
                }
                airQMax += airQMax * 0.1f
                airQMin -= airQMin * 0.1f
                tempMax += tempMax * 0.1f
                tempMin -= tempMin * 0.1f
                humMax += humMax * 0.1f
                humMin -= humMin * 0.1f
            }
            DataTypes.PARAMS3 -> {
                Log.d("GraphActivity_dataType", "PARAMS3 EEEEE")
                isTempVisible = false
                isHumVisible = false
                isAirQVisible = true
                isRadVisible = false
                startTime = paramsZone3[0].time
                airQMax = paramsZone3[0].airQuality.toFloat()
                airQMin = paramsZone3[0].airQuality.toFloat()
                radMax = paramsZone3[0].radiation.toFloat()
                radMin = paramsZone3[0].radiation.toFloat()
                paramsZone3.forEach {paramZone3 ->
                    airQMax = max(airQMax, paramZone3.airQuality.toFloat())
                    airQMin = min(airQMin, paramZone3.airQuality.toFloat())
                    radMin = min(radMin, paramZone3.radiation.toFloat())
                    radMax = max(radMax, paramZone3.radiation.toFloat())
                    val t = paramZone3.time - startTime
                    airQValue.add(Entry(
                        t.toFloat(),
                        paramZone3.airQuality.toFloat()
                    ))
                    radValue.add(Entry(
                        t.toFloat(),
                        paramZone3.radiation.toFloat()
                    ))
                }
                airQMax += airQMax * 0.1f
                airQMin -= airQMin * 0.1f
                radMax += radMax * 0.1f
                radMin -= radMin * 0.1f
            }
        }
    }

    private fun setCheckBoxVisibility(){
        if(dType == DataTypes.PARAMS3){
            cbRadiation.visibility = View.VISIBLE
            cbTemperature.visibility = View.GONE
            cbHumidity.visibility = View.GONE
            cbAirQuality.visibility = View.VISIBLE
        }
        else{
            cbRadiation.visibility = View.INVISIBLE
            cbTemperature.visibility = View.VISIBLE
            cbHumidity.visibility = View.VISIBLE
            cbAirQuality.visibility = View.VISIBLE
        }
    }
}