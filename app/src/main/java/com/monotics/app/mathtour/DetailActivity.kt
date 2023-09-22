package com.monotics.app.mathtour


import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.os.Build

import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper

import com.monotics.app.mathtour.databinding.ActivityDetailBinding
import android.util.Log
import com.google.android.gms.location.*
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapPolyline
import net.daum.mf.map.api.MapView
import java.util.*

private var lat = 0.0
private var log = 0.0
class DetailActivity : AppCompatActivity(){

    var lis: OnMarkerClickedEvent? = null

    private val binding by lazy { ActivityDetailBinding.inflate(layoutInflater) }
    private var  uLatitude = 0.0
    private var uLogitude = 0.0
    lateinit var myloc : MapPOIItem

    lateinit var mapView: MapView
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null // 현재 위치를 가져오기 위한 변수
    lateinit var mLastLocation: Location // 위치 값을 가지고 있는 객체
    internal lateinit var mLocationRequest: LocationRequest // 위치 정보 요청의 매개변수를 저장하는
    private val REQUEST_PERMISSION_LOCATION = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val address = intent?.getSerializableExtra("loc") as String

        mLocationRequest =  LocationRequest.create().apply {
            interval = 3000 // 업데이트 간격
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }


        // 위치 추척 시작
        if (checkPermissionForLocation(this)) {
            startLocationUpdates()
            runBlocking {
                launch {
                    delay(1000)
                }
            }
        }


        //맵 생성
        initMapView(address)
        //gps 이미지 최상단으로 가져오기
        binding.gps.bringToFront()

    }

    override fun onResume() {
        super.onResume()

        var nosolved = ""

        val pref = getSharedPreferences("solved_problem_count", AppCompatActivity.MODE_PRIVATE) //shared key 설정
        val edit = pref?.edit() // 수정모드

        val gyungbukcourse = pref?.getInt("gogung_solved",0)?.plus(pref?.getInt("gogung_solved2",0)!!)
            ?.plus(pref?.getInt("gyungbuk_solved",0)!!)?.plus(pref?.getInt("gyungbuk_solved2",0)!!)
            ?.plus(pref?.getInt("gongyae_solved",0)!!)?.plus(pref?.getInt("sori_solved",0)!!
            )?.plus(pref?.getInt("gyunghiru_solved",0)!!)
            ?.plus(pref?.getInt("dongsibjagak_solved",0)!!)
        if(pref?.getInt("gogung_solved",0) == 0){
            nosolved+="1 "
            //mapView?.removePOIItem()
        }
        if(pref?.getInt("gogung_solved2",0) == 0){
            nosolved+="2 "
        }
        if(pref?.getInt("gyungbuk_solved2",0) == 0){
            nosolved+="3 "
        }
        if(pref?.getInt("gyunghiru_solved",0) == 0){
            nosolved+="4 "
        }
        if(pref?.getInt("gyungbuk_solved",0) == 0){
            nosolved+="5 "
        }
        if(pref?.getInt("dongsibjagak_solved",0) == 0){
            nosolved+="6 "
        }
        if(pref?.getInt("gongyae_solved",0) == 0){
            nosolved+="7 "
        }
        if(pref?.getInt("sori_solved",0) == 0){
            nosolved+="8 "
        }
        if(nosolved==""){
            binding.quizNum.text = "모든 문제를 푸셨습니다!"
        }else {
            binding.quizNum.text = nosolved.toString()
        }
    }
    private fun startLocationUpdates() {

        //FusedLocationProviderClient의 인스턴스를 생성.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        // 기기의 위치에 관한 정기 업데이트를 요청하는 메서드 실행
        // 지정한 루퍼 스레드(Looper.myLooper())에서 콜백(mLocationCallback)으로 위치 업데이트를 요청
        mFusedLocationProviderClient!!.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
    }

    // 시스템으로 부터 위치 정보를 콜백으로 받음
    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            // 시스템에서 받은 location 정보를 onLocationChanged()에 전달
            locationResult.lastLocation
            onLocationChanged(locationResult.lastLocation)
        }
    }

    // 시스템으로 부터 받은 위치정보를 화면에 갱신해주는 메소드
    fun onLocationChanged(location: Location) {
        mLastLocation = location
        //binding.textView10.text = "위도 : " + mLastLocation.latitude // 갱신 된 위도
        //binding.textView11.text = "경도 : " + mLastLocation.longitude // 갱신 된 경도
        uLatitude = mLastLocation.latitude
        uLogitude = mLastLocation.longitude
        lat = mLastLocation.latitude
        log = mLastLocation.longitude
        //Log.e("kimss",uLatitude.toString()+uLogitude.toString())
    }


    // 위치 권한이 있는지 확인하는 메서드
    private fun checkPermissionForLocation(context: Context): Boolean {
        // Android 6.0 Marshmallow 이상에서는 위치 권한에 추가 런타임 권한이 필요
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                true
            } else {
                // 권한이 없으므로 권한 요청 알림 보내기
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSION_LOCATION)
                false
            }
        } else {
            true
        }
    }

    // 사용자에게 권한 요청 후 결과에 대한 처리 로직
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()

            } else {
                Log.d("ttt", "onRequestPermissionsResult() _ 권한 허용 거부")
                Toast.makeText(this, "권한이 없어 해당 기능을 실행할 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    //처음 맵 시작
    private fun initMapView(address: String){
        val mapView = MapView(this)
        lis = OnMarkerClickedEvent(this,this)
        mapView.setPOIItemEventListener(lis)
        binding.mapView.addView(mapView)
        //처음 가비지값 넣은 마커 생성
        startLocationUpdates()
        createMarker(mapView,"내 위치",uLatitude.toDouble(), uLogitude.toDouble(),0,R.drawable.human1)

            //현 위치 마커
            //화면갱신 해야됨
            Timer().scheduleAtFixedRate( object : TimerTask() {
                override fun run() {
                    // 전 위치마커를 지우고 새롭게 마커를 생성
                    mapView?.removePOIItem(myloc)
                    createMarker(mapView,"내 위치",uLatitude.toDouble(), uLogitude.toDouble(),0,R.drawable.human1)
                    Log.e("kimss","succ")
                }
            }, 0, 3000)


        //gps클릭 시 현위치로 이동
        binding.gps.setOnClickListener {
            if(checkPermissionForLocation(this)){
                startLocationUpdates()
                mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(uLatitude.toDouble(), uLogitude.toDouble()), true)
            }


        }

        if(address=="gyungbuk" || address == "gyungbuk-success"){
            mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.5767345, 126.9833623), true)
            mapView.setZoomLevel(3,true)
            createMarker(mapView,"1.국립 고궁 박물관",37.5766084, 126.974951,1,R.drawable.gogung)
            createMarker(mapView,"2.국립 고궁 박물관",37.576684, 126.975748,2,R.drawable.gogung2)

            createMarker(mapView,"3.경복궁",37.5785635,126.9769535,3,R.drawable.gyeongbuk2)

            createMarker(mapView,"4.경회루",37.5797077, 126.9758797,4,R.drawable.gyunghiru)

            createMarker(mapView,"5.경복궁",37.579617, 126.977041,5,R.drawable.gyeongbuk)
            createMarker(mapView,"6.동십자각",37.5760892, 126.9793858,6,R.drawable.dongsibjagak2)

            createMarker(mapView,"7.서울 공예 박물관",37.5767345, 126.9833623,7,R.drawable.gongyae)
            createMarker(mapView,"8.우리 소리 박물관",37.5770987, 126.9897786,8,R.drawable.sound)



            //선 그리기
            val polyline = MapPolyline()
            polyline.tag = 1000
            polyline.lineColor = Color.parseColor("#004C60")
            //polyline.lineColor = Color.argb(150, 31, 28, 40) // Polyline 컬러 지정.


            // Polyline 좌표 지정.
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.5766084, 126.974951)) //국립고궁
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.5765167,126.9761855))//용성문

            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.5775591,126.9762922))
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.5777032,126.9769315))
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.579617,126.977041)) //경복궁
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.5794314,126.9782651)) //외소지방
            //경복궁 끝
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.5778574,126.9778853)) //계조방
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.576628,126.978049))
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.576630,126.979345)) //경복궁 끝
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.576657,126.979642))//법련사
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.576033, 126.979989))
            //공예 박물관 전까지
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.575697,126.982853))
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.5767345, 126.9833623))
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.5767109,126.9841779))
            //이제 공예에서-> 소리
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.576001,126.9844471))
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.576641,126.986378))
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.5771576,126.9881465))

            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.5770987, 126.9897786))

            // Polyline 지도에 올리기
            mapView.addPolyline(polyline)

            //현재 위치

            //거리 및 시간
            binding.totaltime.text = "소요 시간 : 약 31분"
            binding.totaldistance.text = "거 리 : 약 2.0km"


        }else{
            mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(35.9174022, 127.8172645), true)
            mapView.setZoomLevel(12,true)
            createPin(mapView,"1코스) 서울 경복궁",37.579617, 126.977041,1)
            createPin(mapView,"2코스) 전주 한옥마을",35.8147082, 127.1526319,2)
            createPin(mapView,"3코스) 세종 호수공원",36.4976852, 127.2733177,3)
            createPin(mapView,"4코스) 서산 가야산",36.7046536, 126.6082699,4)
            createPin(mapView,"5코스) 전남 다산초당",34.5805408, 126.7451714,5)
            createPin(mapView,"6코스) 광주 무등산",35.134134, 126.9887555,6)
            createPin(mapView,"7코스) 통영 해상국립공원",34.8425341, 128.3565836,7)
            createPin(mapView,"8코스) 부산 이바구길",35.1163493, 129.039158,8)
            createPin(mapView,"9코스) 울산 주상절리",35.6340354, 129.4423168,9)
            createPin(mapView,"10코스) 영주 부석사",36.9984542, 128.6869587,10)
            createPin(mapView,"11코스) 대구 근대골목",35.8736548, 128.5924125,11)
            createPin(mapView,"12코스) 춘천 의암호",37.8909529, 127.7106109,12)
            createPin(mapView,"13코스) 충북 산막이 옛길",36.7516257, 127.8376611,13)
            createPin(mapView,"14코스) 수원 화성",37.2871202, 127.0119379,14)
            createPin(mapView,"15코스) 제주 용머리해안",33.231847, 126.314641,15)
            createPin(mapView,"16코스) 강원 경포대",37.7950361, 128.8966374,16)
            createPin(mapView,"17코스) 태안 해식동굴",36.7443942, 126.1334129,17)

            //선 그리기
            val polyline = MapPolyline()
            polyline.tag = 1000
            polyline.lineColor = Color.argb(150, 31, 28, 40) // Polyline 컬러 지정.


            // Polyline 좌표 지정.
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.579617, 126.977041))//서울
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.2871202, 127.0119379))//수원
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(36.7443942, 126.1334129))//태안
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(36.7046536, 126.6082699))//서산
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(36.7516257, 127.8376611))//충북
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(36.4976852, 127.2733177))//세종
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(35.8147082, 127.1526319))//전주
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(35.134134, 126.9887555))//광주
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(34.5805408, 126.7451714))//전남
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(33.231847, 126.314641))//제주
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(34.8425341, 128.3565836))//통영
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(35.1163493, 129.039158))//부산
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(35.6340354, 129.4423168))//울산
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(35.8736548, 128.5924125))//대구
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(36.9984542, 128.6869587))//영주
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.7950361, 128.8966374))//경포대
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.8909529, 127.7106109))//춘천
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.579617, 126.977041))//서울
            // Polyline 지도에 올리기
            mapView.addPolyline(polyline)
            Toast.makeText(this,"아직 미완성된 코스입니다", Toast.LENGTH_LONG).show()
        }

    }


    //특정이미지마커 생성하기
    private fun createMarker(
        mapView: MapView, name: String, x: Double, y: Double, tag:Int,
        img: Int
    ){


        val customMarker = MapPOIItem()
        //val mapView = MapView(activity)
        customMarker.itemName = name
        customMarker.tag = tag
        customMarker.mapPoint = MapPoint.mapPointWithGeoCoord(x, y)
        customMarker.markerType = MapPOIItem.MarkerType.CustomImage//마커타입 커스텀으로
        customMarker.customImageResourceId = img
        customMarker.isCustomImageAutoscale = false//true시 지도레벨이 커지면 작아짐
        customMarker.setCustomImageAnchor(
            0.5f,
            1.0f
        )
        mapView.addPOIItem(customMarker)
        if(name=="내 위치"){ //내 위치 마커 없앴다가 다시 나타나게
            //전역변수 myloc에 내 위치 마커가 생길때마다 값을 넣어줌
            myloc = customMarker
        }

    }

    //커스텀마커 생성하기
    private fun createPin(
        mapView: MapView, name: String, x: Double, y: Double, tag:Int
    ){
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

    class OnMarkerClickedEvent(val context: Context,val activity: Activity) : MapView.POIItemEventListener{
        override fun onPOIItemSelected(mapView: MapView?, marker: MapPOIItem?) {
            when (marker?.tag) {
                1 -> { //국립 고궁 박물관
                    mapView?.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.5766084, 126.974951), true)
                    mapView?.setZoomLevel(1,true)
                }
                5 -> { // 경복궁
                    mapView?.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.579617, 126.977041), true)
                    mapView?.setZoomLevel(1,true)
                }
                7 -> { //서울 공예 박물관
                    mapView?.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.5767345, 126.9833623), true)
                    mapView?.setZoomLevel(1,true)
                }
                8 -> { //우리 소리 박물관
                    mapView?.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.5770987, 126.9897786), true)
                    mapView?.setZoomLevel(1,true)
                }
            }


        }

        override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {

        }

        override fun onCalloutBalloonOfPOIItemTouched(
            p0: MapView?,
            p1: MapPOIItem?, //마커의 특정정보 불러옴
            p2: MapPOIItem.CalloutBalloonButtonType?
        ) {
            //마커누르면 나옴.
            when (p1?.tag) {
                0 -> { //내 위치
                    // 말풍선 클릭 시
//                    val builder = AlertDialog.Builder(context)
//                    val itemList = arrayOf("토스트", "마커 삭제", "취소")
//                    builder.setTitle("${p1?.itemName}")
//                    builder.setItems(itemList) { dialog, which ->
//                        when(which) {
//                            0 -> Toast.makeText(context, "토스트", Toast.LENGTH_SHORT).show()  // 토스트
//                            1 -> p0?.removePOIItem(p1)    // 마커 삭제 Log.e("kimss","success")
//                            2 -> dialog.dismiss()   // 대화상자 닫기
//                        }
//                    }
//                    builder.show()
                }


                1 -> { //국립 고궁 박물관
                    val builder = AlertDialog.Builder(context)
                    val itemList = arrayOf("문제 풀기", "길 찾기", "취소")
                    builder.setTitle("${p1?.itemName}")
                    builder.setItems(itemList) { dialog, which ->
                        when(which) {
                            0 -> gotoquiz(p1)
                            1 -> gotosite(p1.itemName)
                            2 -> dialog.dismiss()   // 대화상자 닫기
                        }
                    }
                    builder.show()

                }
                2 -> { // 국립고궁박물관 2
                    val builder = AlertDialog.Builder(context)
                    val itemList = arrayOf("문제 풀기", "길 찾기", "취소")
                    builder.setTitle("${p1?.itemName}")
                    builder.setItems(itemList) { dialog, which ->
                        when(which) {
                            0 -> gotoquiz(p1)
                            1 -> gotosite(p1.itemName)
                            2 -> dialog.dismiss()   // 대화상자 닫기
                        }
                    }
                    builder.show()
                }


                3 -> { //경복궁2
                    val builder = AlertDialog.Builder(context)
                    val itemList = arrayOf("문제 풀기", "길 찾기", "취소")
                    builder.setTitle("${p1?.itemName}")
                    builder.setItems(itemList) { dialog, which ->
                        when(which) {
                            0 -> gotoquiz(p1)
                            1 -> gotosite(p1.itemName)
                            2 -> dialog.dismiss()   // 대화상자 닫기
                        }
                    }
                    builder.show()
                }
                4 -> { //경회루
                    val builder = AlertDialog.Builder(context)
                    val itemList = arrayOf("문제 풀기", "길 찾기", "취소")
                    builder.setTitle("${p1?.itemName}")
                    builder.setItems(itemList) { dialog, which ->
                        when(which) {
                            0 -> gotoquiz(p1)
                            1 -> gotosite(p1.itemName)
                            2 -> dialog.dismiss()   // 대화상자 닫기
                        }
                    }
                    builder.show()
                }
                5 -> { //경복궁
                    val builder = AlertDialog.Builder(context)
                    val itemList = arrayOf("문제 풀기", "길 찾기", "취소")
                    builder.setTitle("${p1?.itemName}")
                    builder.setItems(itemList) { dialog, which ->
                        when(which) {
                            0 -> gotoquiz(p1)
                            1 -> gotosite(p1.itemName)
                            2 -> dialog.dismiss()   // 대화상자 닫기
                        }
                    }
                    builder.show()
                }
                
                
                6-> { //동십자각
                    val builder = AlertDialog.Builder(context)
                    val itemList = arrayOf("문제 풀기", "길 찾기", "취소")
                    builder.setTitle("${p1?.itemName}")
                    builder.setItems(itemList) { dialog, which ->
                        when(which) {
                            0 -> gotoquiz(p1)
                            1 -> gotosite(p1.itemName)
                            2 -> dialog.dismiss()   // 대화상자 닫기
                        }
                    }
                    builder.show()
                }
                7-> { //서울공예박물관
                    val builder = AlertDialog.Builder(context)
                    val itemList = arrayOf("문제 풀기", "길 찾기", "취소")
                    builder.setTitle("${p1?.itemName}")
                    builder.setItems(itemList) { dialog, which ->
                        when(which) {
                            0 -> gotoquiz(p1)
                            1 -> gotosite(p1.itemName)
                            2 -> dialog.dismiss()   // 대화상자 닫기
                        }
                    }
                    builder.show()
                }
                8-> { //우리소리박물관
                    val builder = AlertDialog.Builder(context)
                    val itemList = arrayOf("문제 풀기", "길 찾기", "취소")
                    builder.setTitle("${p1?.itemName}")
                    builder.setItems(itemList) { dialog, which ->
                        when(which) {
                            0 -> gotoquiz(p1)
                            1 -> gotosite(p1.itemName)
                            2 -> dialog.dismiss()   // 대화상자 닫기
                        }
                    }
                    builder.show()
                }
                
                
                
            }

        }

        
        fun gotoquiz(p1: MapPOIItem) {
            val intent = Intent(activity, QuizActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            val itemList = hashMapOf(
                "tag" to p1?.tag,
                "name" to p1?.itemName
            )
            intent.putExtra("map",itemList)
            activity.startActivity(intent)
        }

        fun gotosite(itemName: String) {
            when (itemName) {

                "1.국립 고궁 박물관" -> {
                    val url = "kakaomap://route?sp=${lat},${log}&ep=37.5766084,126.974951&by=FOOT"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    activity.startActivity(intent)
                }
                "2.국립 고궁 박물관" -> {
                    val url = "kakaomap://route?sp=${lat},${log}&ep=37.576684,126.975748&by=FOOT"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    activity.startActivity(intent)
                }

                "3.경복궁" -> {
                    val url = "kakaomap://route?sp=${lat},${log}&ep=37.5785635,126.9769535&by=FOOT"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    activity.startActivity(intent)
                }
                "4.경회루" -> {
                    val url = "kakaomap://route?sp=${lat},${log}&ep=37.5797077, 126.9758797&by=FOOT"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    activity.startActivity(intent)
                }
                "5.경복궁" -> {
                    val url = "kakaomap://route?sp=${lat},${log}&ep=37.579617, 126.977041&by=FOOT"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    activity.startActivity(intent)

                }
                "6.동십자각" -> {
                    val url = "kakaomap://route?sp=${lat},${log}&ep=37.5760892, 126.9793858&by=FOOT"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    activity.startActivity(intent)
                }
                "7.서울 공예 박물관" -> {
                    val url = "kakaomap://route?sp=${lat},${log}&ep=37.5767345, 126.9833623&by=FOOT"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    activity.startActivity(intent)
                }
                "8.우리 소리 박물관" -> {
                    val url = "kakaomap://route?sp=${lat},${log}&ep=37.5770987, 126.9897786&by=FOOT"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    activity.startActivity(intent)
                }






            }

        }

        override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {

        }

    }


}
