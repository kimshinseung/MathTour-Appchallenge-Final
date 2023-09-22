package com.monotics.app.mathtour
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.monotics.app.mathtour.databinding.FragmentMapBinding
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class MapFragment : Fragment(),ConfirmDialogInterface {
    lateinit var binding: FragmentMapBinding
//    var lis: OnMarkerClickedEvent? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        //문제수
        val pref =activity?.getSharedPreferences("solved_problem_count", AppCompatActivity.MODE_PRIVATE) //shared key 설정
        val edit = pref?.edit() // 수정모드
        //완주 코스
        val pref2 =activity?.getSharedPreferences("solved_course", AppCompatActivity.MODE_PRIVATE) //shared key 설정
        val edit2 = pref2?.edit() // 수정모드
        val gyungbukcourse = pref?.getInt("gogung_solved",0)?.plus(pref?.getInt("gogung_solved2",0)!!)
            ?.plus(pref?.getInt("gyungbuk_solved",0)!!)?.plus(pref?.getInt("gyungbuk_solved2",0)!!)
            ?.plus(pref?.getInt("gongyae_solved",0)!!)?.plus(pref?.getInt("sori_solved",0)!!
            )?.plus(pref?.getInt("gyunghiru_solved",0)!!)
            ?.plus(pref?.getInt("dongsibjagak_solved",0)!!)
        if(gyungbukcourse == 8) {
            binding.gyungbuk.setImageResource(R.drawable.success2)
            edit2?.putInt("solved_course1", 1) // 값 넣기
            edit2?.apply() // 적용하기
        }else{
            binding.gyungbuk.setImageResource(R.drawable.pin3)
            edit2?.putInt("solved_course1", 0) // 값 넣기
            edit2?.apply() // 적용하기
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMapBinding.bind(view)

        //문제수
        val pref =activity?.getSharedPreferences("solved_problem_count", AppCompatActivity.MODE_PRIVATE) //shared key 설정
        val edit = pref?.edit() // 수정모드

        val gyungbukcourse = pref?.getInt("gogung_solved",0)?.plus(pref?.getInt("gogung_solved2",0)!!)
            ?.plus(pref?.getInt("gyungbuk_solved",0)!!)?.plus(pref?.getInt("gyungbuk_solved2",0)!!)
            ?.plus(pref?.getInt("gongyae_solved",0)!!)?.plus(pref?.getInt("sori_solved",0)!!
            )?.plus(pref?.getInt("gyunghiru_solved",0)!!)
            ?.plus(pref?.getInt("dongsibjagak_solved",0)!!)

        //완주 코스
        val pref2 =activity?.getSharedPreferences("solved_course", AppCompatActivity.MODE_PRIVATE) //shared key 설정
        val edit2 = pref2?.edit() // 수정모드


        if(gyungbukcourse == 8) {
            binding.gyungbuk.setImageResource(R.drawable.success2)
            edit2?.putInt("solved_course1", 1) // 값 넣기
            edit2?.apply() // 적용하기
        }else{
            binding.gyungbuk.setImageResource(R.drawable.pin3)
            edit2?.putInt("solved_course1", 0) // 값 넣기
            edit2?.apply() // 적용하기
        }

        binding.gyungbuk.setOnClickListener {
            if(gyungbukcourse == 8){
                selectedItem("gyungbuk-success")
            }else {
                selectedItem("gyungbuk")
            }
        }
        binding.junju.setOnClickListener {
            selectedItem("junju")
        }
        binding.sejong.setOnClickListener {
            selectedItem("sejong")
        }
        binding.susan.setOnClickListener {
            selectedItem("susan")
        }
        binding.dasan.setOnClickListener {
            selectedItem("dasan")
        }
        binding.gawngju.setOnClickListener {
            selectedItem("gawngju")
        }
        binding.tongyuong.setOnClickListener {
            selectedItem("tongyuong")
        }
        binding.busan.setOnClickListener {
            selectedItem("busan")
        }
        binding.ulsan.setOnClickListener {
            selectedItem("ulsan")
        }
        binding.youngju.setOnClickListener {
            selectedItem("youngju")
        }
        binding.daegu.setOnClickListener {
            selectedItem("daegu")
        }
        binding.chunchun.setOnClickListener {
            selectedItem("chunchun")
        }
        binding.chungbuk.setOnClickListener {
            selectedItem("chungbuk")
        }
        binding.suwon.setOnClickListener {
            selectedItem("suwon")
        }
        binding.jeju.setOnClickListener {
            selectedItem("jeju")
        }
        binding.gyungpodae.setOnClickListener {
            selectedItem("gyungpodae")
        }
        binding.taean.setOnClickListener {
            selectedItem("taean")
        }




        //카카오맵
        //initMapView()

        //지도 줌인 아웃 코드


        // 내 위치

    }
    //지도로 변경
    private fun selectedItem(address: String) {
        if(address == "gyungbuk") {
            val dialog = ConfirmDialog(
                this, "1코스) 경복궁 코스에 " +
                        "입장하시겠습니까?", address, "nocomplete"
            )
            dialog.isCancelable = false
            dialog.show(activity?.supportFragmentManager!!, "ConfirmDialog")
        }
        else if(address == "gyungbuk-success"){
            val dialog = ConfirmDialog(
                this, "완주한 코스입니다!  " +
                        "다시 입장하시겠습니까?", address, "complete"
            )
            dialog.isCancelable = false
            dialog.show(activity?.supportFragmentManager!!, "ConfirmDialog")
        }
        else{
            Toast.makeText(context, "미구현된 코스입니다. 현재 경복궁 코스만이 구현되어 있습니다.", Toast.LENGTH_SHORT).show()
//            val dialog = ConfirmDialog(
//                this, "미완성된 코스입니다 입장하시겠습니까?", address
//            )
//            dialog.isCancelable = false
//            dialog.show(activity?.supportFragmentManager!!, "ConfirmDialog")
        }


//        val intent = Intent(context, DetailActivity::class.java)
//        intent.putExtra("loc",address)
//        context?.startActivity(intent)
    }



    //마커 생성하기
    private fun createMarker(mapView: MapView, name: String, x: Double, y: Double, tag:Int){
        val customMarker = MapPOIItem()
        //val mapView = MapView(activity)
        customMarker.itemName = name
        customMarker.tag = tag
        customMarker.mapPoint = MapPoint.mapPointWithGeoCoord(x, y)
        customMarker.markerType = MapPOIItem.MarkerType.CustomImage//마커타입 커스텀으로

        customMarker.customImageResourceId = R.drawable.pin
        customMarker.isCustomImageAutoscale = false//true시 지도레벨이 커지면 작아짐
        customMarker.setCustomImageAnchor(
            0.5f,
            1.0f
        )
        mapView.addPOIItem(customMarker)

    }





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onYesButtonClick(id: Int) {
        super.onDestroy()
    }
//    class OnMarkerClickedEvent() : MapView.POIItemEventListener{
//        override fun onPOIItemSelected(mapView: MapView?, marker: MapPOIItem?) {
//            //Log.e("kimss", marker?.itemName ?: "a")
//
//        }
//
//        override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {
//
//        }
//
//        override fun onCalloutBalloonOfPOIItemTouched(
//            p0: MapView?,
//            p1: MapPOIItem?,
//            p2: MapPOIItem.CalloutBalloonButtonType?
//        ) {
//            Log.d("kimss",p0.toString())
//
//        }
//
//        override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {
//        }
//    }

}
