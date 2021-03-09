package com.bda.finance_test.ui.chart

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bda.finance_test.R
import com.bda.finance_test.databinding.FragmentChartBinding
import com.bda.finance_test.model.database.entity.CurrencyPair
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import org.koin.androidx.viewmodel.ext.android.viewModel


class ChartFragment : Fragment() {

    private var numTrade: Int = 0

    private val viewModel: ChartViewModel by viewModel()
    private var _binding: FragmentChartBinding? = null

    private val binding get() = _binding!!

    private lateinit var chart: LineChart

    private val valuesBid: ArrayList<Entry> = ArrayList()
    private val valuesAsk: ArrayList<Entry> = ArrayList()

    lateinit var currencyPair: CurrencyPair

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currencyPair = requireArguments().getParcelable("pair")!!
        viewModel.currencyPair.value = currencyPair
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChartBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.viewModel = viewModel
        binding.executePendingBindings()
        initChart()

        viewModel.tickerData.observe(viewLifecycleOwner) { ticker ->
            if (ticker == null) return@observe
            val setBid: LineDataSet
            val setAsk: LineDataSet

            if (numTrade == 0) {
                val xAxis = chart.xAxis
                xAxis.textSize = 11f
                xAxis.textColor = Color.WHITE
                xAxis.setDrawGridLines(false)
                xAxis.setDrawAxisLine(false)

                val leftAxis = chart.axisLeft
                leftAxis.textColor = ColorTemplate.getHoloBlue()
                leftAxis.axisMaximum = ticker.high.toFloat()
                leftAxis.axisMinimum = ticker.low.toFloat()
                leftAxis.setDrawGridLines(true)
                leftAxis.isGranularityEnabled = true

                val rightAxis = chart.axisRight
                rightAxis.textColor = Color.RED
                rightAxis.axisMaximum =
                    if (ticker.ask > ticker.bid) (ticker.ask + 100).toFloat()
                    else (ticker.bid + 100).toFloat()
                rightAxis.axisMinimum =
                    if (ticker.ask < ticker.bid) (ticker.ask - 100).toFloat()
                    else (ticker.bid - 100).toFloat()
                rightAxis.setDrawGridLines(false)
                rightAxis.setDrawZeroLine(false)
                rightAxis.isGranularityEnabled = false
            }

            valuesBid.add(Entry(numTrade.toFloat(), ticker.bid.toFloat()))
            valuesAsk.add(Entry(numTrade.toFloat(), ticker.ask.toFloat()))

            if (chart.data != null &&
                chart.data.dataSetCount > 0
            ) {
                setBid = chart.data.getDataSetByIndex(0) as LineDataSet
                setAsk = chart.data.getDataSetByIndex(1) as LineDataSet
                setBid.values = valuesBid
                setAsk.values = valuesAsk
                chart.data.notifyDataChanged()
                chart.notifyDataSetChanged()
                chart.invalidate()
            } else {
                // create a dataset and give it a type
                setBid = LineDataSet(valuesBid, "BID");

                setBid.axisDependency = YAxis.AxisDependency.LEFT
                setBid.color = ColorTemplate.getHoloBlue()
                setBid.setCircleColor(Color.WHITE)
                setBid.lineWidth = 2f
                setBid.circleRadius = 3f
                setBid.fillAlpha = 65
                setBid.fillColor = ColorTemplate.getHoloBlue()
                setBid.highLightColor = Color.rgb(244, 117, 117)
                setBid.setDrawCircleHole(false)

                // create a dataset and give it a type
                setAsk = LineDataSet(valuesAsk, "ASK")
                setAsk.axisDependency = YAxis.AxisDependency.RIGHT
                setAsk.color = Color.RED
                setAsk.setCircleColor(Color.WHITE)
                setAsk.lineWidth = 2f
                setAsk.circleRadius = 3f
                setAsk.fillAlpha = 65
                setAsk.fillColor = Color.RED
                setAsk.setDrawCircleHole(false)
                setAsk.highLightColor = Color.rgb(244, 117, 117)

                // create a data object with the data sets
                val data = LineData(setBid, setAsk)
                data.setValueTextColor(Color.WHITE)
                data.setValueTextSize(9f)

                // set data
                chart.data = data
            }
            numTrade++
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.apply {
            title = " ${currencyPair.title}"
            setNavigationIcon(R.drawable.ic_left_arrow)
            setNavigationOnClickListener {
                requireActivity().onBackPressed()
            }
        }

    }

    private fun initChart() {
        chart = binding.chartView
        chart.description.isEnabled = false

        // enable touch gestures
        //chart.setTouchEnabled(true)

        chart.dragDecelerationFrictionCoef = 0.9f

        // enable scaling and dragging
        chart.isDragEnabled = true
        chart.setScaleEnabled(true)
        chart.setDrawGridBackground(false)
        chart.isHighlightPerDragEnabled = true

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(true)

        // set an alternative background color
        chart.setBackgroundColor(Color.LTGRAY)

        chart.animateX(1500)

        // get the legend (only possible after setting data)
        val l = chart.legend

        // modify the legend ...
        l.form = LegendForm.LINE
        l.textSize = 11f
        l.textColor = Color.WHITE
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)

        chart.legend.isEnabled = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}