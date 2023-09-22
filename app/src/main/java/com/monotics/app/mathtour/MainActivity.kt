package com.monotics.app.mathtour

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.pm.PackageManager
import android.hardware.SensorEventListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.maps.MapView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.monotics.app.mathtour.databinding.ActivityMainBinding
import java.security.MessageDigest
import android.hardware.*
import android.os.AsyncTask
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


class MainActivity : AppCompatActivity(),SensorEventListener{
    private val fragmentMain by lazy {MainFragment()}
    private val fragmentMap by lazy {MapFragment()}
    private val fragmentMyPage by lazy {MyPageFragment()}
    var sensorManager : SensorManager?=null
    private var running = false
    var timevalue = true
    private var totalStep = 0
    private var currentStep = 0

    val currentTime : Long = System.currentTimeMillis() // ms로 반환
    val dataFormat4 = SimpleDateFormat("HH:mm:ss")
    val time = dataFormat4.format(currentTime)
    override fun finish() {
        super.finish()
    }


    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    fun waitLoad(x: Int){
        while(
            WeatherApiData.weatherListLiveData.value?.get(x)?.get(0)?.rainType == null || WeatherApiData.weatherListLiveData.value?.get(x)?.get(0)?.sky == null ||
            WeatherApiData.weatherListLiveData.value?.get(x)?.get(1)?.rainType == null || WeatherApiData.weatherListLiveData.value?.get(x)?.get(1)?.sky == null ||
            WeatherApiData.weatherListLiveData.value?.get(x)?.get(2)?.rainType == null || WeatherApiData.weatherListLiveData.value?.get(x)?.get(2)?.sky == null ||
            WeatherApiData.weatherListLiveData.value?.get(x)?.get(3)?.rainType == null || WeatherApiData.weatherListLiveData.value?.get(x)?.get(3)?.sky == null ||
            WeatherApiData.weatherListLiveData.value?.get(x)?.get(4)?.rainType == null || WeatherApiData.weatherListLiveData.value?.get(x)?.get(4)?.sky == null ||
            WeatherApiData.weatherListLiveData.value?.get(x)?.get(5)?.rainType == null || WeatherApiData.weatherListLiveData.value?.get(x)?.get(5)?.sky == null);
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        weaterListLoad()

        runBlocking {
            launch {
                delay(4000)
            }
        }
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        // 해시값 구하기
        getAppKeyHash()
        
        //프래그먼트 시작
        //setTheFirstFragment()
        initNavigationBar()
        
        //스플래시에서 화면 넘어옴
        val start = intent.getSerializableExtra("start") as String
        if(start=="시작"){
            setTheFirstFragment()
        }


        //걸음수 측정
        val pref = getSharedPreferences("count", 0)
        currentStep = pref.getInt("step", 0)

        //걸음 수 초기화
        binding.resetbtn.setOnClickListener{
            currentStep = 0
            saveData(currentStep)
            //걸음수 측정
            val pref = getSharedPreferences("count", 0)
            currentStep = pref.getInt("step", 0)
            binding.stepcount.text = "걸음수: " + currentStep.toString()
        }


        // 활동 퍼미션 체크
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(
                this as Activity,
                arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),100);
        }
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // 매일 자정에 변수 초기화 작업 예약
//        val timer = Timer("01:24:05")
//        val midnight = getMidnightOfNextDay()
//        timer.scheduleAtFixedRate(object : TimerTask() {
//            override fun run() {
//                // 변수 초기화 작업
//                currentStep = 0
//                println("변수 초기화됨: $currentStep")
//                Log.e("kimss","초기화됨")
//            }
//        }, midnight, 24 * 60 * 60 * 1000) // 24시간마다 반복
//
//        // 예제를 실행한 채로 대기
//        readLine()
//        readLine()
//        // 타이머 종료
//        timer.cancel()

    }
    override fun onResume() {
        super.onResume()

        val stepCountSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepCountSensor != null) {
            sensorManager?.registerListener(
                this,
                stepCountSensor,
                SensorManager.SENSOR_DELAY_FASTEST
            );
            Log.d("kimshinseung", " sensor found");
        } else {
            Log.d("kimshinseung", "no sensor found");
        }
    }



    private fun setTheFirstFragment(){
        val fragmentTransaction : FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fl_container,fragmentMain)
        fragmentTransaction.commit()
        binding.bnvMain.setItemSelected(R.id.first)
    }

    private fun initNavigationBar() {
        var selectedItemId = 0
        binding.bnvMain.run {
            setOnItemSelectedListener { item->
                when(item) {
                    R.id.first -> {
                        changeFragment(fragmentMain)
                    }
                    R.id.second -> {
                        changeFragment(fragmentMap)
                    }
                    R.id.third -> {
                        changeFragment(fragmentMyPage)
                    }
                }
                true
            }
            selectedItemId = R.id.first
        }
    }
    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fl_container, fragment)
            .commit()
    }
    private fun getAppKeyHash() {
        try {
            val info =
                packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                var md: MessageDigest
                md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val something = String(Base64.encode(md.digest(), 0))
                Log.e("Hash key", something)
            }
        } catch (e: Exception) {

            Log.e("name not found", e.toString())
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {

            if(event.sensor.type == Sensor.TYPE_STEP_COUNTER){
                // 센서 이벤트가 발생할때 마다 걸음수 증가
                currentStep++;
                binding.stepcount.text = "걸음수: " + currentStep.toString()
                saveData(currentStep)
                Log.e("name not found",currentStep.toString())
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
    fun saveData( step :Int ){
        //앱네에 프리펀스로 걸음수 저장
        val pref =getSharedPreferences("count", MODE_PRIVATE) //shared key 설정
        val edit = pref.edit() // 수정모드
        edit.putInt("step", step) // 값 넣기
        edit.apply() // 적용하기
    }

    fun getMidnightOfNextDay(): Date {
        val today = LocalDate.now()
        val tomorrow = today.plusDays(1)
        val midnight = LocalDateTime.of(tomorrow, LocalDateTime.MIN.toLocalTime())
        val midnightMillis = midnight.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        return Date(midnightMillis)
    }


}


