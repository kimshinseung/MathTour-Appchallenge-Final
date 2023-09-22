package com.monotics.app.mathtour

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.monotics.app.mathtour.ApiKey.Companion.WEATHER_API_KEY_en
import com.monotics.app.mathtour.WeatherApiData.Companion.BASE_URL
import com.monotics.app.mathtour.WeatherApiData.Companion.WeatherList
import kotlinx.coroutines.sync.Mutex
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

//http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst?serviceKey=인증키&numOfRows=10&pageNo=1 &base_date=20210628&base_time=0600&nx=55&ny=127
//https://min-wachya.tistory.com/163

class ModelWeather {
    var rainType = ""       // 강수 형태
    var humidity = ""       // 습도
    var sky = ""            // 하능 상태
    var temp = ""           // 기온
    var fcstTime = ""       // 예보시각

    fun rainTypeToString() : String {
        return when(rainType) {// 강수형태: 없음(0), 비(1), 비/눈(2), 눈(3), 빗방울(5), 빗방울눈날림(6), 눈날림(7)
            "0" -> "없음"
            "1" -> "비"
            "2" -> "비/눈"
            "3" -> "눈"
            "5" -> "빗방울"
            "6" -> "빗방울눈날림"
            "7" -> "눈날림"
            else -> "오류 rainType : " + rainType
        }
    }

    // 하늘 상태
    fun skyToString() : String {
        return when(sky) { //하늘상태: 맑음(1) 구름많은(3) 흐림(4)
            "1" -> "맑음"
            "3" -> "구름 많음"
            "4" -> "흐림"
            else -> "오류 sky : " + sky
        }
    }
}

class WeatherApiData(){
    companion object{

        const val BASE_URL = "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/"
        val WeatherList: ArrayList<Array<ModelWeather>> = ArrayList()
        val weatherListLiveData: MutableLiveData<ArrayList<Array<ModelWeather>>> = MutableLiveData()
        val locMap = hashMapOf(
            1 to listOf("60", "127"),
            2 to listOf("63", "89"),
            3 to listOf("66", "103"),
            4 to listOf("53", "108"),
            5 to listOf("56", "61"),
            6 to listOf("61", "72"),
            7 to listOf("57", "94"),
            8 to listOf("98", "76"),
            9 to listOf("102", "84"),
            10 to listOf("50", "108"),
            11 to listOf("89", "90"),
            12 to listOf("73", "134"),
            13 to listOf("74", "111"),
            14 to listOf("60", "121"),
            15 to listOf("52", "33"),
            16 to listOf("92", "131"),
            17 to listOf("48", "109")
        )
    }
}

// 결과 xml 파일에 접근해서 정보 가져오기
interface WeatherInterface {
    // getUltraSrtFcst : 초단기 예보 조회 + 인증키
    @GET("getUltraSrtFcst?serviceKey=" + WEATHER_API_KEY_en)

    fun GetWeather(@Query("numOfRows") num_of_rows : Int,   // 한 페이지 경과 수
                   @Query("pageNo") page_no : Int,          // 페이지 번호
                   @Query("dataType") data_type : String,   // 응답 자료 형식
                   @Query("base_date") base_date : String,  // 발표 일자
                   @Query("base_time") base_time : String,  // 발표 시각
                   @Query("nx") nx : String,                // 예보지점 X 좌표
                   @Query("ny") ny : String)                // 예보지점 Y 좌표
            : Call<WEATHER>
}


// xml 파일 형식을 data class로 구현
data class WEATHER (val response : RESPONSE)
data class RESPONSE(val header : HEADER, val body : BODY)
data class HEADER(val resultCode : Int, val resultMsg : String)
data class BODY(val dataType : String, val items : ITEMS, val totalCount : Int)
data class ITEMS(val item : List<ITEM>)

// category : 자료 구분 코드, fcstDate : 예측 날짜, fcstTime : 예측 시간, fcstValue : 예보 값
data class ITEM(val category : String, val fcstDate : String, val fcstTime : String, val fcstValue : String)


// retrofit을 사용하기 위한 빌더 생성
private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create(
        GsonBuilder()
            .setLenient()
            .create())
    )
    .build()

object ApiObject {
    val retrofitService: WeatherInterface by lazy {
        retrofit.create(WeatherInterface::class.java)
    }
}

// baseTime 설정하기
fun getBaseTime(h : String, m : String) : String {
    var result = ""

    // 45분 전이면
    if (m.toInt() < 45) {
        // 0시면 2330
        if (h == "00") result = "2330"
        // 아니면 1시간 전 날씨 정보 부르기
        else {
            var resultH = h.toInt() - 1
            // 1자리면 0 붙여서 2자리로 만들기
            if (resultH < 10) result = "0" + resultH + "30"
            // 2자리면 그대로
            else result = resultH.toString() + "30"
        }
    }
    // 45분 이후면 바로 정보 받아오기
    else result = h + "30"

    return result
}

public fun setWeather(courseId: Int): Array<ModelWeather> {
    // 준비 단계 : base_date(발표 일자), base_time(발표 시각)
    // 현재 날짜, 시간 정보 가져오기
    val cal = Calendar.getInstance()
    //var liveWeatherData: MutableLiveData<Array<ModelWeather>> = MutableLiveData()
    val weatherArr = arrayOf(
        ModelWeather(),
        ModelWeather(),
        ModelWeather(),
        ModelWeather(),
        ModelWeather(),
        ModelWeather()
    )

    //var base_date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

    var base_date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
    val timeH = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH"))
    val timeM = LocalDateTime.now().format(DateTimeFormatter.ofPattern("mm"))


    // API 가져오기 적당하게 변환

    var base_time = getBaseTime(timeH, timeM)
    // 현재 시각이 00시이고 45분 이하여서 baseTime이 2330이면 어제 정보 받아오기
    if (timeH == "00" && base_time == "2330") {
        cal.add(Calendar.DATE, -1).toString()
        base_date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.time)
    }

    // 날씨 정보 가져오기
    // (한 페이지 결과 수 = 60, 페이지 번호 = 1, 응답 자료 형식-"JSON", 발표 날싸, 발표 시각, 예보지점 좌표)

    val call = ApiObject.retrofitService.GetWeather(
        60,
        1,
        "JSON",
        base_date,
        base_time,
        WeatherApiData.locMap[courseId]?.get(0).toString(),
        WeatherApiData.locMap[courseId]?.get(1).toString()
    )

    // 비동기적으로 실행하기
    call.enqueue(object : retrofit2.Callback<WEATHER> {
        // 응답 성공 시
        override fun onResponse(call: Call<WEATHER>, response: Response<WEATHER>) {
            if (response.isSuccessful) {
                // 날씨 정보 가져오기
                val it: List<ITEM> = response.body()!!.response.body.items.item

                // 현재 시각부터 1시간 뒤의 날씨 6개를 담을 배열

                // 배열 채우기
                var index = 0
                val totalCount = response.body()!!.response.body.totalCount - 1
                for (i in 0..totalCount) {
                    index %= 6
                    when (it[i].category) {
                        "PTY" -> weatherArr[index].rainType = it[i].fcstValue     // 강수 형태
                        "REH" -> weatherArr[index].humidity = it[i].fcstValue     // 습도
                        "SKY" -> weatherArr[index].sky = it[i].fcstValue          // 하늘 상태
                        "T1H" -> weatherArr[index].temp = it[i].fcstValue         // 기온
                        else -> continue
                    }
                    Log.d(
                        "api success",
                        courseId.toString()+ " "+ index.toString()+ " " + weatherArr[index].rainTypeToString() +" " + weatherArr[index].humidity +" " + weatherArr[index].skyToString() +' ' + weatherArr[index].temp
                    )
                    index++
                }

                // 각 날짜 배열 시간 설정
                for (i in 0..5) weatherArr[i].fcstTime = it[i].fcstTime
            }
        }

        // 응답 실패 시
        override fun onFailure(call: Call<WEATHER>, t: Throwable) {
            Log.d("api fail", t.message.toString())
        }

    })
    return weatherArr
}

fun weaterListLoad(){
    WeatherList.add(setWeather(1))
    WeatherList.add(setWeather(2))
    WeatherList.add(setWeather(3))
    WeatherList.add(setWeather(4))
    WeatherList.add(setWeather(5))
    WeatherList.add(setWeather(6))
    WeatherList.add(setWeather(7))
    WeatherList.add(setWeather(8))
    WeatherList.add(setWeather(9))
    WeatherList.add(setWeather(10))
    WeatherList.add(setWeather(11))
    WeatherList.add(setWeather(12))
    WeatherList.add(setWeather(13))
    WeatherList.add(setWeather(14))
    WeatherList.add(setWeather(15))
    WeatherList.add(setWeather(16))
    WeatherList.add(setWeather(17))
    WeatherApiData.weatherListLiveData.value = WeatherList
}