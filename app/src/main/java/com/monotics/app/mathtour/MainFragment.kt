package com.monotics.app.mathtour

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.monotics.app.mathtour.databinding.FragmentMainBinding

class MainFragment : Fragment(), SensorEventListener {

    lateinit var binding: FragmentMainBinding
    var sensorManager : SensorManager?=null
    private var running = false

    private var totalStep = 0f
    private var previousTotalStep = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
        //걸음 수 측정
        //권한
        if(ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),100);
        }
        sensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager

        //리사이클러뷰
        val list = ArrayList<ListData>()
        list.add(ListData("서울 경복궁","1"))
        list.add(ListData("전주 한옥 마을","2"))
        list.add(ListData("세종 호수공원","3"))
        list.add(ListData("서산 가야산","4"))
        list.add(ListData("전남 다산 초당","5"))
        list.add(ListData("광주 무등산","6"))
        list.add(ListData("통영 해상국립공원","7"))
        list.add(ListData("부산 이바구길","8"))
        list.add(ListData("울산 주상절리","9"))
        list.add(ListData("영주 부석사","10"))
        list.add(ListData("대구 근대골목","11"))
        list.add(ListData("춘천 의암호","12"))
        list.add(ListData("충북 산막이 옛길","13"))
        list.add(ListData("수원 화성","14"))
        list.add(ListData("제주 용머리해안","15"))
        list.add(ListData("강원 경포대","16"))
        list.add(ListData("태안 해식동굴","17"))

        var manager = GridLayoutManager(context,2)
        var adapter = ListAdapter(list, this)

        binding.listRecycler.layoutManager = manager
        binding.listRecycler.adapter=adapter
        binding.listRecycler.addItemDecoration(DistanceItemDecorator(25))

        //weaterListLoad()

        //이 방식은 프래그먼트에서는 리사이클러뷰가 실행되지 않음.
//        var RecyclerView = list_recycler.apply {
//            adapter = adapter
//            layoutManager = manager
//        }



    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onSensorChanged(event: SensorEvent?) {

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

}