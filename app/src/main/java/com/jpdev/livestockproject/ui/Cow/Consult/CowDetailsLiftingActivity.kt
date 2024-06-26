package com.jpdev.livestockproject.ui.Cow.Consult

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidplot.xy.CatmullRomInterpolator
import com.androidplot.xy.LineAndPointFormatter
import com.androidplot.xy.SimpleXYSeries
import com.androidplot.xy.XYGraphWidget
import com.androidplot.xy.XYSeries
import com.jpdev.livestockproject.R
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ActivityCowDetailsBinding
import com.jpdev.livestockproject.domain.model.LiftingPerformance
import com.jpdev.livestockproject.ui.Cow.Consult.AdapterNewsLifting.LiftingAdapter
import com.jpdev.livestockproject.ui.Cow.HomeCow.HomeCowActivity
import com.jpdev.livestockproject.ui.Cow.Lifting.Register.RegisterNewsLiftingActivity
import java.text.FieldPosition
import java.text.Format
import java.text.ParsePosition
import java.util.Arrays
import kotlin.math.roundToInt

class CowDetailsLiftingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCowDetailsBinding
    private lateinit var firebaseInstance: FirebaseInstance
    private var NewsList = mutableListOf<LiftingPerformance>()
    private var Keys = mutableListOf<String>()
    private lateinit var adapter: LiftingAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCowDetailsBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)
        val user = intent.extras?.getString("userKey")
        val farmKey = intent.extras?.getString("farmKey")
        val cowKey = intent.extras?.getString("cowKey")

        firebaseInstance = FirebaseInstance(this)

        printInfo(user, farmKey, cowKey)
        getListCowsNews(user, farmKey, cowKey)
        initListeners(user, farmKey, cowKey)
    }

    override fun onResume() {
        super.onResume()
        binding = ActivityCowDetailsBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)
        val user = intent.extras?.getString("userKey")
        val farmKey = intent.extras?.getString("farmKey")
        val cowKey = intent.extras?.getString("cowKey")

        firebaseInstance = FirebaseInstance(this)

        printInfo(user, farmKey, cowKey)
        getListCowsNews(user, farmKey, cowKey)
        initListeners(user, farmKey, cowKey)
    }

    private fun printInfo(user: String?, farmKey: String?, cowKey: String?) {
        firebaseInstance.getCowDetails(user, farmKey, cowKey) {
            val details = getString(R.string.register_news) + " ${it.marking}"
            binding.tvRegisteredCows.text = details
        }
    }

    private fun initListeners(user: String?, farmKey: String?, cowKey: String?) {
        binding.viewToolBar.back.setOnClickListener {
            val intent = Intent(this,CowResumeActivity::class.java)
            intent.putExtra("userKey",user)
            intent.putExtra("farmKey",farmKey)
            intent.putExtra("cowKey",cowKey)
            startActivity(intent)
            finish()
        }
        binding.btnRegisterWeight.setOnClickListener {
            val intent = Intent(this, RegisterNewsLiftingActivity::class.java)
            intent.putExtra("userKey", user)
            intent.putExtra("farmKey", farmKey)
            intent.putExtra("cowKey", cowKey)
            startActivity(intent)
        }
    }

    private fun getListCowsNews(user: String?, farm: String?, cowKey: String?) {
        firebaseInstance.getCowNewsLifting(
            user.toString(),
            farm.toString(),
            cowKey.toString()
        ) { news, keys ->
            if (news != null) {
                news?.let {
                    NewsList.clear()
                    NewsList.addAll(news)
                    keys?.let {
                        Keys.clear()
                        Keys.addAll(keys)
                        setUpRecyclerView(user.toString(), farm.toString(), cowKey.toString())
                    }
                }
            }
        }
    }

    private fun setUpRecyclerView(user: String?, farm: String?, cowKey: String?) {
        adapter =
            LiftingAdapter(NewsList, Keys, user.toString(), farm.toString(), cowKey.toString())
        binding.rvCows.adapter = adapter
        binding.rvCows.layoutManager = LinearLayoutManager(this)
        adapter.notifyDataSetChanged()
        setGraph()
    }

    private fun setGraph() {

        val domain = mutableListOf<Number>()
        val weight = mutableListOf<Number>()

        domain.add(0)
        weight.add(0)

        for (element in NewsList){
            weight.add(element.PLWeight)
        }

        for (i in 1..NewsList.size) {
            domain.add(i)
        }

        val weightList: Array<Number> = weight.toList().toTypedArray()
        val domainLabels: Array<Number> = domain.toList().toTypedArray()

        val series1: XYSeries = SimpleXYSeries(
            listOf(* weightList), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Peso de la Vaca"
        )

        val series1Format = LineAndPointFormatter(Color.BLUE, Color.BLACK, Color.GRAY, null)
        series1Format.interpolationParams = CatmullRomInterpolator.Params(10,
            CatmullRomInterpolator.Type.Centripetal)

        binding.graphic.addSeries(series1, series1Format)

        binding.graphic.graph.getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).format =
            object : Format() {

                override fun format(
                    obj: Any?,
                    toAppendTo: StringBuffer?,
                    pos: FieldPosition?
                ): StringBuffer {
                    val i = (obj as Number).toFloat().roundToInt()
                    return toAppendTo!!.append(domainLabels[i])
                }

                override fun parseObject(source: String?, pos: ParsePosition?): Any? {
                    return null
                }

            }

    }
}