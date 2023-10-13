package com.monotics.app.mathtour


import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64


import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.monotics.app.mathtour.databinding.ActivityDetailcourseBinding


class DetailcourseActivity : AppCompatActivity(){

    val binding by lazy { ActivityDetailcourseBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val address = intent.getSerializableExtra("loc") as String
        val id = intent.getSerializableExtra("id") as Int
        locationDetail(address)

        val viewManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val viewAdapter = WeatherApiData.weatherListLiveData.value?.get(id)?.let { List2Adapter(it) }
        val recyclerView = binding.weatherRecycler.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)
            // use a linear layout manager
            layoutManager = viewManager
            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
            addItemDecoration(DistanceItemDecorator(10))
        }
        binding.learnmore.setOnClickListener {
            if(address == "서울 경복궁"){
                val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://ko.wikipedia.org/wiki/%EA%B2%BD%EB%B3%B5%EA%B6%81"))
                //ACTION_VIEW = 뒤에 있는 것을 보여주라는 뜻
                startActivity(webIntent)
            }else if(address == "수원 화성"){
                val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://ko.wikipedia.org/wiki/%EC%88%98%EC%9B%90_%ED%99%94%EC%84%B1"))
                //ACTION_VIEW = 뒤에 있는 것을 보여주라는 뜻
                startActivity(webIntent)
            }
        }

    }



    private fun locationDetail(address: String){
        if(address=="서울 경복궁") {
            val viewManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            val pref = getSharedPreferences("camera", AppCompatActivity.MODE_PRIVATE) //shared key 설정
            val edit = pref?.edit() // 수정모드

            val img1 = pref?.getString("gyungbukcourse1","")
            val img2 = pref?.getString("gyungbukcourse5","")
            val img3 = pref?.getString("gyungbukcourse7","")
            val img4 = pref?.getString("gyungbukcourse8","")


            val imgList = arrayOf(R.mipmap.gogung2, R.mipmap.gyungbookgung2 , R.mipmap.gongyae2, R.mipmap.sound2)

            val cameralist = arrayOf(img1,img2,img3,img4)

            val nameList = arrayOf("국립 고궁 박물관","경복궁","서울 공예 박물관","우리 소리 박물관")
            val viewAdapter = List3Adapter(imgList,nameList,cameralist)
            val recyclerView = binding.showimg.apply {
                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                setHasFixedSize(true)
                // use a linear layout manager
                layoutManager = viewManager
                // specify an viewAdapter (see also next example)
                adapter = viewAdapter
                addItemDecoration(DistanceItemDecorator(10))
            }
            binding.infotext.text = "1코스) ${address}"
        }
        else if(address == "수원 화성"){

            val viewManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            val pref = getSharedPreferences("camera", AppCompatActivity.MODE_PRIVATE) //shared key 설정
            val edit = pref?.edit() // 수정모드

            val img1 = pref?.getString("suwoncourse1","")
            val img2 = pref?.getString("suwoncourse3","")
            val img3 = pref?.getString("suwoncourse4","")
            val img4 = pref?.getString("suwoncourse6","")

            val imgList = arrayOf(R.mipmap.hanggung,R.mipmap.hanok,R.mipmap.janganmoon,R.mipmap.hwasumoon)
            val nameList = arrayOf("화성행궁광장","한옥기술전시관","장안문","화서문")

            val cameralist = arrayOf(img1,img2,img3,img4)
            val viewAdapter = List3Adapter(imgList, nameList, cameralist)
            val recyclerView = binding.showimg.apply {
                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                setHasFixedSize(true)
                // use a linear layout manager
                layoutManager = viewManager
                // specify an viewAdapter (see also next example)
                adapter = viewAdapter
                addItemDecoration(DistanceItemDecorator(10))
            }
            binding.info.text = "수원 화성(水原 華城)은 대한민국 경기도 수원시 팔달구 장안동에 있는 길이 5.52킬로미터의 성곽이다.1963년 대한민국의 사적 제3호로 지정되었으며, 1997년 유네스코 세계문화유산으로 등록되었다. 시발점은 한국전쟁 당시 일부가 파괴되어 이후 재건한 건축물이지만, 화성을 계획할 당시 그림과 글로 설계도와 관련 내용을 기록해둔 수원화성의궤가 남아있어 이를 바탕으로 복원하였기 때문에 역사적 가치를 인정받을 수 있었다."
            binding.infotext.text = "14코스) ${address}"
        }


        else if(address == "전주 한옥 마을"){
            val viewManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            val imgList = arrayOf(R.mipmap.nomap)
            val nameList = arrayOf("","2","3","4")
            val pref = getSharedPreferences("camera", AppCompatActivity.MODE_PRIVATE) //shared key 설정
            val edit = pref?.edit() // 수정모드

            val img1 = pref?.getString("junjucourse1","")
            val img2 = pref?.getString("junjucourse2","")
            val img3 = pref?.getString("junjucourse3","")
            val img4 = pref?.getString("junjucourse4","")


            val cameralist = arrayOf(img1,img2,img3,img4)
            val viewAdapter = List3Adapter(imgList, nameList, cameralist)
            val recyclerView = binding.showimg.apply {
                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                setHasFixedSize(true)
                // use a linear layout manager
                layoutManager = viewManager
                // specify an viewAdapter (see also next example)
                adapter = viewAdapter
                addItemDecoration(DistanceItemDecorator(10))
            }
            //경복궁 외에는 텍스트 숨김
            binding.info.visibility = View.INVISIBLE
            binding.learnmore.visibility = View.INVISIBLE


            binding.infotext.text = "2코스) ${address}"
            Toast.makeText(this,"아직 미완성된 코스입니다", Toast.LENGTH_LONG).show()
        }
        else if(address == "세종 호수공원"){
            val viewManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            val imgList = arrayOf(R.mipmap.nomap)
            val nameList = arrayOf("","2","3","4")
            val pref = getSharedPreferences("camera", AppCompatActivity.MODE_PRIVATE) //shared key 설정
            val edit = pref?.edit() // 수정모드

            val img1 = pref?.getString("junjucourse1","")
            val img2 = pref?.getString("junjucourse2","")
            val img3 = pref?.getString("junjucourse3","")
            val img4 = pref?.getString("junjucourse4","")


            val cameralist = arrayOf(img1,img2,img3,img4)
            val viewAdapter = List3Adapter(imgList, nameList, cameralist)
            val recyclerView = binding.showimg.apply {
                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                setHasFixedSize(true)
                // use a linear layout manager
                layoutManager = viewManager
                // specify an viewAdapter (see also next example)
                adapter = viewAdapter
                addItemDecoration(DistanceItemDecorator(10))
            }
            //경복궁 외에는 텍스트 숨김
            binding.info.visibility = View.INVISIBLE
            binding.learnmore.visibility = View.INVISIBLE

            binding.infotext.text = "3코스) ${address}"
            Toast.makeText(this,"아직 미완성된 코스입니다", Toast.LENGTH_LONG).show()
        }
        else if(address == "서산 가야산"){
            val viewManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            val imgList = arrayOf(R.mipmap.nomap)
            val nameList = arrayOf("","2","3","4")
            val pref = getSharedPreferences("camera", AppCompatActivity.MODE_PRIVATE) //shared key 설정
            val edit = pref?.edit() // 수정모드

            val img1 = pref?.getString("junjucourse1","")
            val img2 = pref?.getString("junjucourse2","")
            val img3 = pref?.getString("junjucourse3","")
            val img4 = pref?.getString("junjucourse4","")


            val cameralist = arrayOf(img1,img2,img3,img4)
            val viewAdapter = List3Adapter(imgList, nameList, cameralist)
            val recyclerView = binding.showimg.apply {
                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                setHasFixedSize(true)
                // use a linear layout manager
                layoutManager = viewManager
                // specify an viewAdapter (see also next example)
                adapter = viewAdapter
                addItemDecoration(DistanceItemDecorator(10))
            }
            //경복궁 외에는 텍스트 숨김
            binding.info.visibility = View.INVISIBLE
            binding.learnmore.visibility = View.INVISIBLE

            binding.infotext.text = "4코스) ${address}"
            Toast.makeText(this,"아직 미완성된 코스입니다", Toast.LENGTH_LONG).show()
        }
        else if(address == "전남 다산 초당"){
            val viewManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            val imgList = arrayOf(R.mipmap.nomap)
            val nameList = arrayOf("","2","3","4")
            val pref = getSharedPreferences("camera", AppCompatActivity.MODE_PRIVATE) //shared key 설정
            val edit = pref?.edit() // 수정모드

            val img1 = pref?.getString("junjucourse1","")
            val img2 = pref?.getString("junjucourse2","")
            val img3 = pref?.getString("junjucourse3","")
            val img4 = pref?.getString("junjucourse4","")


            val cameralist = arrayOf(img1,img2,img3,img4)
            val viewAdapter = List3Adapter(imgList, nameList, cameralist)
            val recyclerView = binding.showimg.apply {
                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                setHasFixedSize(true)
                // use a linear layout manager
                layoutManager = viewManager
                // specify an viewAdapter (see also next example)
                adapter = viewAdapter
                addItemDecoration(DistanceItemDecorator(10))
            }
            //경복궁 외에는 텍스트 숨김
            binding.info.visibility = View.INVISIBLE
            binding.learnmore.visibility = View.INVISIBLE

            binding.infotext.text = "5코스) ${address}"
            Toast.makeText(this,"아직 미완성된 코스입니다", Toast.LENGTH_LONG).show()
        }
        else if(address == "광주 무등산"){
            val viewManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            val imgList = arrayOf(R.mipmap.nomap)
            val nameList = arrayOf("","2","3","4")
            val pref = getSharedPreferences("camera", AppCompatActivity.MODE_PRIVATE) //shared key 설정
            val edit = pref?.edit() // 수정모드

            val img1 = pref?.getString("junjucourse1","")
            val img2 = pref?.getString("junjucourse2","")
            val img3 = pref?.getString("junjucourse3","")
            val img4 = pref?.getString("junjucourse4","")


            val cameralist = arrayOf(img1,img2,img3,img4)
            val viewAdapter = List3Adapter(imgList, nameList, cameralist)
            val recyclerView = binding.showimg.apply {
                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                setHasFixedSize(true)
                // use a linear layout manager
                layoutManager = viewManager
                // specify an viewAdapter (see also next example)
                adapter = viewAdapter
                addItemDecoration(DistanceItemDecorator(10))
            }
            //경복궁 외에는 텍스트 숨김
            binding.info.visibility = View.INVISIBLE
            binding.learnmore.visibility = View.INVISIBLE

            binding.infotext.text = "6코스) ${address}"
            Toast.makeText(this,"아직 미완성된 코스입니다", Toast.LENGTH_LONG).show()
        }
        else if(address == "통영 해상국립공원"){
            val viewManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            val imgList = arrayOf(R.mipmap.nomap)
            val nameList = arrayOf("","2","3","4")
            val pref = getSharedPreferences("camera", AppCompatActivity.MODE_PRIVATE) //shared key 설정
            val edit = pref?.edit() // 수정모드

            val img1 = pref?.getString("junjucourse1","")
            val img2 = pref?.getString("junjucourse2","")
            val img3 = pref?.getString("junjucourse3","")
            val img4 = pref?.getString("junjucourse4","")


            val cameralist = arrayOf(img1,img2,img3,img4)
            val viewAdapter = List3Adapter(imgList, nameList, cameralist)
            val recyclerView = binding.showimg.apply {
                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                setHasFixedSize(true)
                // use a linear layout manager
                layoutManager = viewManager
                // specify an viewAdapter (see also next example)
                adapter = viewAdapter
                addItemDecoration(DistanceItemDecorator(10))
            }
            //경복궁 외에는 텍스트 숨김
            binding.info.visibility = View.INVISIBLE
            binding.learnmore.visibility = View.INVISIBLE

            binding.infotext.text = "7코스) ${address}"
            Toast.makeText(this,"아직 미완성된 코스입니다", Toast.LENGTH_LONG).show()
        }
        else if(address == "부산 이바구길"){
            val viewManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            val imgList = arrayOf(R.mipmap.nomap)
            val nameList = arrayOf("","2","3","4")
            val pref = getSharedPreferences("camera", AppCompatActivity.MODE_PRIVATE) //shared key 설정
            val edit = pref?.edit() // 수정모드

            val img1 = pref?.getString("junjucourse1","")
            val img2 = pref?.getString("junjucourse2","")
            val img3 = pref?.getString("junjucourse3","")
            val img4 = pref?.getString("junjucourse4","")


            val cameralist = arrayOf(img1,img2,img3,img4)
            val viewAdapter = List3Adapter(imgList, nameList, cameralist)
            val recyclerView = binding.showimg.apply {
                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                setHasFixedSize(true)
                // use a linear layout manager
                layoutManager = viewManager
                // specify an viewAdapter (see also next example)
                adapter = viewAdapter
                addItemDecoration(DistanceItemDecorator(10))
            }
            //경복궁 외에는 텍스트 숨김
            binding.info.visibility = View.INVISIBLE
            binding.learnmore.visibility = View.INVISIBLE

            binding.infotext.text = "8코스) ${address}"
            Toast.makeText(this,"아직 미완성된 코스입니다", Toast.LENGTH_LONG).show()
        }
        else if(address == "울산 주상절리"){
            val viewManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            val imgList = arrayOf(R.mipmap.nomap)
            val nameList = arrayOf("","2","3","4")
            val pref = getSharedPreferences("camera", AppCompatActivity.MODE_PRIVATE) //shared key 설정
            val edit = pref?.edit() // 수정모드

            val img1 = pref?.getString("junjucourse1","")
            val img2 = pref?.getString("junjucourse2","")
            val img3 = pref?.getString("junjucourse3","")
            val img4 = pref?.getString("junjucourse4","")


            val cameralist = arrayOf(img1,img2,img3,img4)
            val viewAdapter = List3Adapter(imgList, nameList, cameralist)
            val recyclerView = binding.showimg.apply {
                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                setHasFixedSize(true)
                // use a linear layout manager
                layoutManager = viewManager
                // specify an viewAdapter (see also next example)
                adapter = viewAdapter
                addItemDecoration(DistanceItemDecorator(10))
            }
            //경복궁 외에는 텍스트 숨김
            binding.info.visibility = View.INVISIBLE
            binding.learnmore.visibility = View.INVISIBLE

            binding.infotext.text = "9코스) ${address}"
            Toast.makeText(this,"아직 미완성된 코스입니다", Toast.LENGTH_LONG).show()
        }
        else if(address == "영주 부석사"){
            val viewManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            val imgList = arrayOf(R.mipmap.nomap)
            val nameList = arrayOf("","2","3","4")
            val pref = getSharedPreferences("camera", AppCompatActivity.MODE_PRIVATE) //shared key 설정
            val edit = pref?.edit() // 수정모드

            val img1 = pref?.getString("junjucourse1","")
            val img2 = pref?.getString("junjucourse2","")
            val img3 = pref?.getString("junjucourse3","")
            val img4 = pref?.getString("junjucourse4","")


            val cameralist = arrayOf(img1,img2,img3,img4)
            val viewAdapter = List3Adapter(imgList, nameList, cameralist)
            val recyclerView = binding.showimg.apply {
                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                setHasFixedSize(true)
                // use a linear layout manager
                layoutManager = viewManager
                // specify an viewAdapter (see also next example)
                adapter = viewAdapter
                addItemDecoration(DistanceItemDecorator(10))
            }
            //경복궁 외에는 텍스트 숨김
            binding.info.visibility = View.INVISIBLE
            binding.learnmore.visibility = View.INVISIBLE

            binding.infotext.text = "10코스) ${address}"
            Toast.makeText(this,"아직 미완성된 코스입니다", Toast.LENGTH_LONG).show()
        }
        else if(address == "대구 근대골목"){
            val viewManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            val imgList = arrayOf(R.mipmap.nomap)
            val nameList = arrayOf("","2","3","4")
            val pref = getSharedPreferences("camera", AppCompatActivity.MODE_PRIVATE) //shared key 설정
            val edit = pref?.edit() // 수정모드

            val img1 = pref?.getString("junjucourse1","")
            val img2 = pref?.getString("junjucourse2","")
            val img3 = pref?.getString("junjucourse3","")
            val img4 = pref?.getString("junjucourse4","")


            val cameralist = arrayOf(img1,img2,img3,img4)
            val viewAdapter = List3Adapter(imgList, nameList, cameralist)
            val recyclerView = binding.showimg.apply {
                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                setHasFixedSize(true)
                // use a linear layout manager
                layoutManager = viewManager
                // specify an viewAdapter (see also next example)
                adapter = viewAdapter
                addItemDecoration(DistanceItemDecorator(10))
            }
            //경복궁 외에는 텍스트 숨김
            binding.info.visibility = View.INVISIBLE
            binding.learnmore.visibility = View.INVISIBLE

            binding.infotext.text = "11코스) ${address}"
            Toast.makeText(this,"아직 미완성된 코스입니다", Toast.LENGTH_LONG).show()
        }
        else if(address == "춘천 의암호"){
            val viewManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            val imgList = arrayOf(R.mipmap.nomap)
            val nameList = arrayOf("","2","3","4")
            val pref = getSharedPreferences("camera", AppCompatActivity.MODE_PRIVATE) //shared key 설정
            val edit = pref?.edit() // 수정모드

            val img1 = pref?.getString("junjucourse1","")
            val img2 = pref?.getString("junjucourse2","")
            val img3 = pref?.getString("junjucourse3","")
            val img4 = pref?.getString("junjucourse4","")


            val cameralist = arrayOf(img1,img2,img3,img4)
            val viewAdapter = List3Adapter(imgList, nameList, cameralist)
            val recyclerView = binding.showimg.apply {
                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                setHasFixedSize(true)
                // use a linear layout manager
                layoutManager = viewManager
                // specify an viewAdapter (see also next example)
                adapter = viewAdapter
                addItemDecoration(DistanceItemDecorator(10))
            }
            //경복궁 외에는 텍스트 숨김
            binding.info.visibility = View.INVISIBLE
            binding.learnmore.visibility = View.INVISIBLE

            binding.infotext.text = "12코스) ${address}"
            Toast.makeText(this,"아직 미완성된 코스입니다", Toast.LENGTH_LONG).show()
        }
        else if(address == "충북 산막이 옛길"){
            val viewManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            val imgList = arrayOf(R.mipmap.nomap)
            val nameList = arrayOf("","2","3","4")
            val pref = getSharedPreferences("camera", AppCompatActivity.MODE_PRIVATE) //shared key 설정
            val edit = pref?.edit() // 수정모드

            val img1 = pref?.getString("junjucourse1","")
            val img2 = pref?.getString("junjucourse2","")
            val img3 = pref?.getString("junjucourse3","")
            val img4 = pref?.getString("junjucourse4","")


            val cameralist = arrayOf(img1,img2,img3,img4)
            val viewAdapter = List3Adapter(imgList, nameList, cameralist)
            val recyclerView = binding.showimg.apply {
                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                setHasFixedSize(true)
                // use a linear layout manager
                layoutManager = viewManager
                // specify an viewAdapter (see also next example)
                adapter = viewAdapter
                addItemDecoration(DistanceItemDecorator(10))
            }
            //경복궁 외에는 텍스트 숨김
            binding.info.visibility = View.INVISIBLE
            binding.learnmore.visibility = View.INVISIBLE

            binding.infotext.text = "13코스) ${address}"
            Toast.makeText(this,"아직 미완성된 코스입니다", Toast.LENGTH_LONG).show()
        }
        else if(address == "제주 용머리해안"){
            val viewManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            val imgList = arrayOf(R.mipmap.nomap)
            val nameList = arrayOf("","2","3","4")
            val pref = getSharedPreferences("camera", AppCompatActivity.MODE_PRIVATE) //shared key 설정
            val edit = pref?.edit() // 수정모드

            val img1 = pref?.getString("junjucourse1","")
            val img2 = pref?.getString("junjucourse2","")
            val img3 = pref?.getString("junjucourse3","")
            val img4 = pref?.getString("junjucourse4","")


            val cameralist = arrayOf(img1,img2,img3,img4)
            val viewAdapter = List3Adapter(imgList, nameList, cameralist)
            val recyclerView = binding.showimg.apply {
                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                setHasFixedSize(true)
                // use a linear layout manager
                layoutManager = viewManager
                // specify an viewAdapter (see also next example)
                adapter = viewAdapter
                addItemDecoration(DistanceItemDecorator(10))
            }
            //경복궁 외에는 텍스트 숨김
            binding.info.visibility = View.INVISIBLE
            binding.learnmore.visibility = View.INVISIBLE

            binding.infotext.text = "15코스) ${address}"
            Toast.makeText(this,"아직 미완성된 코스입니다", Toast.LENGTH_LONG).show()
        }
        else if(address == "강원 경포대"){
            val viewManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            val imgList = arrayOf(R.mipmap.nomap)
            val nameList = arrayOf("","2","3","4")
            val pref = getSharedPreferences("camera", AppCompatActivity.MODE_PRIVATE) //shared key 설정
            val edit = pref?.edit() // 수정모드

            val img1 = pref?.getString("junjucourse1","")
            val img2 = pref?.getString("junjucourse2","")
            val img3 = pref?.getString("junjucourse3","")
            val img4 = pref?.getString("junjucourse4","")


            val cameralist = arrayOf(img1,img2,img3,img4)
            val viewAdapter = List3Adapter(imgList, nameList, cameralist)
            val recyclerView = binding.showimg.apply {
                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                setHasFixedSize(true)
                // use a linear layout manager
                layoutManager = viewManager
                // specify an viewAdapter (see also next example)
                adapter = viewAdapter
                addItemDecoration(DistanceItemDecorator(10))
            }
            //경복궁 외에는 텍스트 숨김
            binding.info.visibility = View.INVISIBLE
            binding.learnmore.visibility = View.INVISIBLE

            binding.infotext.text = "16코스) ${address}"
            Toast.makeText(this,"아직 미완성된 코스입니다", Toast.LENGTH_LONG).show()
        }
        else if(address == "태안 해식동굴"){
            val viewManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            val imgList = arrayOf(R.mipmap.nomap)
            val nameList = arrayOf("","2","3","4")
            val pref = getSharedPreferences("camera", AppCompatActivity.MODE_PRIVATE) //shared key 설정
            val edit = pref?.edit() // 수정모드

            val img1 = pref?.getString("junjucourse1","")
            val img2 = pref?.getString("junjucourse2","")
            val img3 = pref?.getString("junjucourse3","")
            val img4 = pref?.getString("junjucourse4","")


            val cameralist = arrayOf(img1,img2,img3,img4)
            val viewAdapter = List3Adapter(imgList, nameList, cameralist)
            val recyclerView = binding.showimg.apply {
                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                setHasFixedSize(true)
                // use a linear layout manager
                layoutManager = viewManager
                // specify an viewAdapter (see also next example)
                adapter = viewAdapter
                addItemDecoration(DistanceItemDecorator(10))
            }
            //경복궁 외에는 텍스트 숨김
            binding.info.visibility = View.INVISIBLE
            binding.learnmore.visibility = View.INVISIBLE

            binding.infotext.text = "17코스) ${address}"
            Toast.makeText(this,"아직 미완성된 코스입니다", Toast.LENGTH_LONG).show()
        }
        
        
    }


}