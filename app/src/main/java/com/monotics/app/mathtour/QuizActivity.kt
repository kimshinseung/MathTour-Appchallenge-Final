package com.monotics.app.mathtour

import android.content.Intent
import android.content.Intent.EXTRA_ALLOW_MULTIPLE
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.monotics.app.mathtour.databinding.ActivityQuizBinding

class QuizActivity :AppCompatActivity(), ConfirmDialogInterface {
    val binding by lazy { ActivityQuizBinding.inflate(layoutInflater) }

    //경복궁
    private val gogung_answer = "1"
    private val gogung_answer2 = "1"
    private val gyungbukgung_answer2 = "3"
    private val gyunghiru_answer = "3"
    private val gyungbukgung_answer = "4"
    private val dongsibjagak_answer = "2"
    private val gongyae_answer = "4"
    private val urisori_answer = "1"
    
    //수원
    private val hanggung_answer = "1"
    private val artmuseum_answer = "1"
    private val hanok_answer = "3"
    private val janganmoon_answer = "3"
    private val booksuporu_answer = "4"
    private val hwasumoon_answer = "2"


    private val PICK_IMAGE_REQUEST = 1

    override fun onDestroy() {
        super.onDestroy()
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_REQUEST) {
            val inf = intent.getSerializableExtra("map") as HashMap<String,Any>
            val tag = (inf["tag"] as? Int)
            val address = (inf["address"] as? String)
            val bitmap = data?.extras?.get("data") as Bitmap

            val pref = getSharedPreferences("camera", AppCompatActivity.MODE_PRIVATE) //shared key 설정
            val edit = pref?.edit() // 수정모드

            val converter = BitmapConverter()

            lateinit var imageBitmap: Bitmap

            if(address == "gyungbuk"){
                if (tag != null) {
                    edit?.putString("gyungbukcourse${tag}" , converter.bitmapToString(bitmap))
                } // 값 넣기
                edit?.apply() // 적용하기
            }else if(address == "suwon"){
                if (tag != null) {
                    edit?.putString("suwoncourse${tag}" , converter.bitmapToString(bitmap))
                } // 값 넣기
                edit?.apply() // 적용하기
            }


            //Log.e("kimss", (inf["tag"] as? Int).toString()+bitmap.toString())

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        val inf = intent.getSerializableExtra("map") as HashMap<String,Any>
        binding.textView4.text= (inf["tag"] as? Int).toString() + " 번째 문제"
        //binding.textView7.text=inf["name"] as? String
        val address = inf["address"] as String


        val answer = binding.answerBtn.text //정답란 텍스트

        if(address =="gyungbuk") { //경복궁 퀴즈
            //이름 변경
            if (inf["name"] == "1.국립 고궁 박물관") {
                binding.coursename.text = "경복궁 코스 - 국립고궁박물관"
                binding.quizText.text = getString(R.string.gogung)
                binding.quizImg.setImageResource(R.mipmap.quiz1)
                binding.quizTextAnsList.text = getString(R.string.gogungans)

            } else if (inf["name"] == "5.경복궁") {
                binding.coursename.text = "경복궁 코스 - 경복궁"
                //binding.quizImg.setImageResource(R.mipmap.quiz5)
                Glide.with(this).load(R.drawable.cycloid).override(560, 560).into(binding.quizImg)
                binding.quizText.text = getString(R.string.gyungbuk)
                binding.quizTextAnsList.text = getString(R.string.gyungbukans)

            } else if (inf["name"] == "7.서울 공예 박물관") {
                binding.coursename.text = "경복궁 코스 - 서울공예박물관"
                binding.quizImg.setImageResource(R.mipmap.quiz7)
                binding.quizText.text = getString(R.string.gongyae)
                binding.quizTextAnsList.text = getString(R.string.gongyaeans)
            } else if (inf["name"] == "8.우리 소리 박물관") {
                binding.coursename.text = "경복궁 코스 - 우리소리박물관"
                binding.quizImg.setImageResource(R.mipmap.quiz8)
                binding.quizText.text = getString(R.string.sori)
                binding.quizTextAnsList.text = getString(R.string.sorians)
            } else if (inf["name"] == "4.경회루") {
                binding.cameraBtn.visibility = View.INVISIBLE
                binding.coursename.text = "경복궁 코스 - 경회루"
                binding.quizImg.setImageResource(R.mipmap.quiz4)
                binding.quizText.text = getString(R.string.gyunghiru)
                binding.quizTextAnsList.text = getString(R.string.gyunghiruans)
            } else if (inf["name"] == "6.동십자각") {
                binding.cameraBtn.visibility = View.INVISIBLE
                binding.coursename.text = "경복궁 코스 - 동십자각"
                binding.quizImg.setImageResource(R.mipmap.quiz6)
                binding.quizText.text = getString(R.string.dongsibjagak)
                binding.quizTextAnsList.text = getString(R.string.dongsibjagakans)
            } else if (inf["name"] == "2.국립 고궁 박물관") {
                binding.cameraBtn.visibility = View.INVISIBLE
                binding.coursename.text = "경복궁 코스 - 국립고궁박물관2"
                binding.quizImg.setImageResource(R.mipmap.quiz2)
                binding.quizText.text = getString(R.string.gogung2)
                binding.quizTextAnsList.text = getString(R.string.gogung2ans)
            } else if (inf["name"] == "3.경복궁") {
                binding.cameraBtn.visibility = View.INVISIBLE
                binding.coursename.text = "경복궁 코스 - 경복궁2"
                binding.quizImg.setImageResource(R.mipmap.quiz3)
                binding.quizText.text = getString(R.string.gyungbuk2)
                binding.quizTextAnsList.text = getString(R.string.gyungbuk2ans)
            }
        }
        else if(address =="suwon"){//수원 퀴즈
            if (inf["name"] == "1.화성행궁광장") {
                binding.coursename.text = "수원화성 코스 - 화성행궁광장"
                binding.quizText.text = getString(R.string.gogung)
                binding.quizImg.setImageResource(R.mipmap.quiz1)
                binding.quizTextAnsList.text = getString(R.string.gogungans)

            } else if (inf["name"] == "3.한옥기술전시관") {
                binding.coursename.text = "수원화성 코스 - 한옥기술전시관"
                //binding.quizImg.setImageResource(R.mipmap.quiz5)
                Glide.with(this).load(R.drawable.cycloid).override(560, 560).into(binding.quizImg)
                binding.quizText.text = getString(R.string.gyungbuk)
                binding.quizTextAnsList.text = getString(R.string.gyungbukans)

            } else if (inf["name"] == "4.장안문") {
                binding.coursename.text = "수원화성 코스 - 장안문"
                binding.quizImg.setImageResource(R.mipmap.quiz7)
                binding.quizText.text = getString(R.string.gongyae)
                binding.quizTextAnsList.text = getString(R.string.gongyaeans)
            } else if (inf["name"] == "6.화서문") {
                binding.coursename.text = "수원화성 코스 - 화서문"
                binding.quizImg.setImageResource(R.mipmap.quiz8)
                binding.quizText.text = getString(R.string.sori)
                binding.quizTextAnsList.text = getString(R.string.sorians)
            }
            else if (inf["name"] == "2.수원시립미술관") {
                binding.cameraBtn.visibility = View.INVISIBLE
                binding.coursename.text = "수원화성 코스 - 수원시립미술관"
                binding.quizImg.setImageResource(R.mipmap.quiz4)
                binding.quizText.text = getString(R.string.gyunghiru)
                binding.quizTextAnsList.text = getString(R.string.gyunghiruans)
            }
            else if (inf["name"] == "5.북서포루") {
                binding.cameraBtn.visibility = View.INVISIBLE
                binding.coursename.text = "수원화성 코스 - 북서포루"
                binding.quizImg.setImageResource(R.mipmap.quiz4)
                binding.quizText.text = getString(R.string.gyunghiru)
                binding.quizTextAnsList.text = getString(R.string.gyunghiruans)
            }
        }

        //카메라 버튼
        binding.cameraBtn.setOnClickListener {
            val cameraPermissionCheck = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            )
            if (cameraPermissionCheck != PackageManager.PERMISSION_GRANTED) { // 권한이 없는 경우
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.CAMERA),
                    1000
                )
            } else { //권한이 있는 경우
                val REQUEST_IMAGE_CAPTURE = 1
                val intent = Intent(ACTION_IMAGE_CAPTURE)
//                intent.type = "image/*"
                intent.putExtra(ACTION_IMAGE_CAPTURE, true)
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
            }
        }


        val pref = getSharedPreferences("solved_problem_count", AppCompatActivity.MODE_PRIVATE) //shared key 설정
        val edit = pref?.edit() // 수정모드



            //제출 버튼 클릭 시 동작코드
        binding.corBtn.setOnClickListener{

            if(address == "gyungbuk"){
                //경복궁
                if(inf["name"] == "1.국립 고궁 박물관"){ // 국립고궁박물관 문제
                    if(answer.toString() == gogung_answer){ //정답

                        edit?.putInt("gogung_solved" , 1) // 값 넣기
                        edit?.apply() // 적용하기

                        val dialog = QuizDialog(
                            this, "정답입니다!  " +
                                    "다음 문제 위치는 국립고궁박물관입니다", "correct","2.국립 고궁 박물관",2
                        )
                        dialog.isCancelable = false
                        dialog.show(supportFragmentManager!!, "ConfirmDialog")
                    }
                    else{//오답

                        edit?.putInt("gogung_solved" , 0) // 값 넣기
                        edit?.apply() // 적용하기

                        //Toast.makeText(this,"오답입니다, 다시 풀어보겠습니까?", Toast.LENGTH_LONG).show()
                        val dialog = QuizDialog(
                            this, "오답입니다, 다시 풀어보겠습니까?","wrong","gogung",1
                        )
                        dialog.isCancelable = false
                        dialog.show(supportFragmentManager!!, "ConfirmDialog")
                    }
                }

                else if(inf["name"] == "2.국립 고궁 박물관"){ // 국립고궁박물관2 문제
                    binding.coursename.text = "경복궁 코스 - 국립 고궁 박물관2"
                    if(answer.toString() == gogung_answer2){ //정답

                        edit?.putInt("gogung_solved2" , 1) // 값 넣기
                        edit?.apply() // 적용하기

                        val dialog = QuizDialog(
                            this, "정답입니다!  " +
                                    "다음 문제 위치는 경복궁입니다", "correct","3.경복궁",3
                        )
                        dialog.isCancelable = false
                        dialog.show(supportFragmentManager!!, "ConfirmDialog")
                    }
                    else{//오답
                        //Toast.makeText(this,"오답입니다, 다시 풀어보겠습니까?", Toast.LENGTH_LONG).show()
                        edit?.putInt("gogung_solved2" , 0) // 값 넣기
                        edit?.apply() // 적용하기
                        val dialog = QuizDialog(
                            this, "오답입니다, 다시 풀어보겠습니까?","wrong","gogung",2
                        )
                        dialog.isCancelable = false
                        dialog.show(supportFragmentManager!!, "ConfirmDialog")
                    }
                }


                else if(inf["name"] == "3.경복궁"){ // 경복궁2 문제
                    binding.coursename.text = "경복궁 코스 - 경복궁2"
                    if(answer.toString() == gyungbukgung_answer2){ //정답


                        edit?.putInt("gyungbuk_solved2" , 1) // 값 넣기
                        edit?.apply() // 적용하기

                        val dialog = QuizDialog(
                            this, "정답입니다!  " +
                                    "다음 문제 위치는 경회루입니다", "correct","4.경회루",4
                        )
                        dialog.isCancelable = false
                        dialog.show(supportFragmentManager!!, "ConfirmDialog")

                    }
                    else{//오답
                        edit?.putInt("gyungbuk_solved2" , 0) // 값 넣기
                        edit?.apply() // 적용하기
                        //Toast.makeText(this,"오답입니다, 다시 풀어보겠습니까?", Toast.LENGTH_LONG).show()
                        val dialog = QuizDialog(
                            this, "오답입니다, 다시 풀어보겠습니까?","wrong","gogung",3
                        )
                        dialog.isCancelable = false
                        dialog.show(supportFragmentManager!!, "ConfirmDialog")
                    }
                }
                else if(inf["name"] == "4.경회루"){ // 경회루 문제
                    binding.coursename.text = "경복궁 코스 - 경회루"
                    if(answer.toString() == gyunghiru_answer){ //정답


                        edit?.putInt("gyunghiru_solved" , 1) // 값 넣기
                        edit?.apply() // 적용하기

                        val dialog = QuizDialog(
                            this, "정답입니다!  " +
                                    "다음 문제 위치는 경복궁입니다", "correct","5.경복궁",5
                        )
                        dialog.isCancelable = false
                        dialog.show(supportFragmentManager!!, "ConfirmDialog")

                    }
                    else{//오답
                        edit?.putInt("gyunghiru_solved" , 0) // 값 넣기
                        edit?.apply() // 적용하기
                        //Toast.makeText(this,"오답입니다, 다시 풀어보겠습니까?", Toast.LENGTH_LONG).show()
                        val dialog = QuizDialog(
                            this, "오답입니다, 다시 풀어보겠습니까?","wrong","gogung",4
                        )
                        dialog.isCancelable = false
                        dialog.show(supportFragmentManager!!, "ConfirmDialog")
                    }
                }
                else if(inf["name"] == "5.경복궁"){ // 경복궁 문제
                    binding.coursename.text = "경복궁 코스 - 경복궁"
                    if(answer.toString() == gyungbukgung_answer){ //정답


                        edit?.putInt("gyungbuk_solved" , 1) // 값 넣기
                        edit?.apply() // 적용하기

                        val dialog = QuizDialog(
                            this, "정답입니다!  " +
                                    "다음 문제 위치는 동십자각입니다", "correct","6.동십자각",6
                        )
                        dialog.isCancelable = false
                        dialog.show(supportFragmentManager!!, "ConfirmDialog")

                    }
                    else{//오답
                        edit?.putInt("gyungbuk_solved" , 0) // 값 넣기
                        edit?.apply() // 적용하기
                        //Toast.makeText(this,"오답입니다, 다시 풀어보겠습니까?", Toast.LENGTH_LONG).show()
                        val dialog = QuizDialog(
                            this, "오답입니다, 다시 풀어보겠습니까?","wrong","gogung",5
                        )
                        dialog.isCancelable = false
                        dialog.show(supportFragmentManager!!, "ConfirmDialog")
                    }
                }
                else if(inf["name"] == "6.동십자각"){ // 동십자각 문제
                    binding.coursename.text = "경복궁 코스 - 동십자각"
                    if(answer.toString() == dongsibjagak_answer){ //정답

                        edit?.putInt("dongsibjagak_solved" , 1) // 값 넣기
                        edit?.apply() // 적용하기

                        val dialog = QuizDialog(
                            this, "정답입니다!  " +
                                    "다음 문제 위치는 서울공예박물관입니다", "correct","7.서울 공예 박물관",7
                        )
                        dialog.isCancelable = false
                        dialog.show(supportFragmentManager!!, "ConfirmDialog")
                    }
                    else{//오답
                        //Toast.makeText(this,"오답입니다, 다시 풀어보겠습니까?", Toast.LENGTH_LONG).show()
                        edit?.putInt("dongsibjagak_solved" , 0) // 값 넣기
                        edit?.apply() // 적용하기
                        val dialog = QuizDialog(
                            this, "오답입니다, 다시 풀어보겠습니까?","wrong","gogung",6
                        )
                        dialog.isCancelable = false
                        dialog.show(supportFragmentManager!!, "ConfirmDialog")
                    }
                }

                else if(inf["name"] == "7.서울 공예 박물관"){ // 서울공예박물관 문제
                    binding.coursename.text = "경복궁 코스 - 서울공예박물관"
                    if(answer.toString() == gongyae_answer){ //정답

                        edit?.putInt("gongyae_solved" , 1) // 값 넣기
                        edit?.apply() // 적용하기

                        val dialog = QuizDialog(
                            this, "정답입니다!  " +
                                    "다음 문제 위치는 우리소리박물관입니다", "correct","8.우리 소리 박물관",8
                        )
                        dialog.isCancelable = false
                        dialog.show(supportFragmentManager!!, "ConfirmDialog")
                    }
                    else{//오답
                        //Toast.makeText(this,"오답입니다, 다시 풀어보겠습니까?", Toast.LENGTH_LONG).show()
                        edit?.putInt("gongyae_solved" , 0) // 값 넣기
                        edit?.apply() // 적용하기
                        val dialog = QuizDialog(
                            this, "오답입니다, 다시 풀어보겠습니까?","wrong","gogung",7
                        )
                        dialog.isCancelable = false
                        dialog.show(supportFragmentManager!!, "ConfirmDialog")
                    }
                }
                else if(inf["name"] == "8.우리 소리 박물관"){ // 우리소리박물관 문제
                    binding.coursename.text = "경복궁 코스 - 우리소리박물관"
                    if(answer.toString() == urisori_answer){ //정답

                        edit?.putInt("sori_solved" , 1) // 값 넣기
                        edit?.apply() // 적용하기

                        val pref = getSharedPreferences("solved_problem_count", AppCompatActivity.MODE_PRIVATE) //shared key 설정
                        val edit = pref?.edit() // 수정모드

                        val gyungbukcourse = pref?.getInt("gogung_solved",0)?.plus(pref?.getInt("gogung_solved2",0)!!)
                            ?.plus(pref?.getInt("gyungbuk_solved",0)!!)?.plus(pref?.getInt("gyungbuk_solved2",0)!!)
                            ?.plus(pref?.getInt("gongyae_solved",0)!!)?.plus(pref?.getInt("sori_solved",0)!!
                            )?.plus(pref?.getInt("gyunghiru_solved",0)!!)
                            ?.plus(pref?.getInt("dongsibjagak_solved",0)!!)


                        if(gyungbukcourse == 8){//모든 문제가 다 풀렸으면
                            val dialog = QuizDialog(
                                this, "정답입니다!  " +
                                        "모든 문제를 맞추셨습니다! " +
                                        "코스가 종료됩니다", "end","end",2
                            )
                            dialog.isCancelable = false
                            dialog.show(supportFragmentManager!!, "ConfirmDialog")
                        }else{ // 문제가 다 안풀렸을 때
                            val dialog = QuizDialog(
                                this, "정답입니다!  " +
                                        "안 푼 문제가 있습니다. " +
                                        "남은 문제를 풀어주세요", "end","notend",2
                            )
                            dialog.isCancelable = false
                            dialog.show(supportFragmentManager!!, "ConfirmDialog")
                        }
                    }

                    else{//오답
                        //Toast.makeText(this,"오답입니다, 다시 풀어보겠습니까?", Toast.LENGTH_LONG).show()
                        edit?.putInt("sori_solved" , 0) // 값 넣기
                        edit?.apply() // 적용하기
                        val dialog = QuizDialog(
                            this, "오답입니다, 다시 풀어보겠습니까?","wrong","gogung",8
                        )
                        dialog.isCancelable = false
                        dialog.show(supportFragmentManager!!, "ConfirmDialog")
                    }
                }
            }

            else if(address=="suwon"){

                if(inf["name"] == "1.화성행궁광장"){ // 국립고궁박물관 문제
                    if(answer.toString() == hanggung_answer){ //정답

                        edit?.putInt("hanggung_solved" , 1) // 값 넣기
                        edit?.apply() // 적용하기

                        val dialog = QuizDialog(
                            this, "정답입니다!  " +
                                    "다음 문제 위치는 국립고궁박물관입니다", "correct","2.국립 고궁 박물관",2
                        )
                        dialog.isCancelable = false
                        dialog.show(supportFragmentManager!!, "ConfirmDialog")
                    }
                    else{//오답

                        edit?.putInt("hanggung_solved" , 0) // 값 넣기
                        edit?.apply() // 적용하기

                        //Toast.makeText(this,"오답입니다, 다시 풀어보겠습니까?", Toast.LENGTH_LONG).show()
                        val dialog = QuizDialog(
                            this, "오답입니다, 다시 풀어보겠습니까?","wrong","gogung",1
                        )
                        dialog.isCancelable = false
                        dialog.show(supportFragmentManager!!, "ConfirmDialog")
                    }
                }
                else if(inf["name"] == "2.수원시립미술관"){ // 국립고궁박물관2 문제
                    binding.coursename.text = "경복궁 코스 - 국립 고궁 박물관2"
                    if(answer.toString() == artmuseum_answer){ //정답

                        edit?.putInt("artmuseum_solved" , 1) // 값 넣기
                        edit?.apply() // 적용하기

                        val dialog = QuizDialog(
                            this, "정답입니다!  " +
                                    "다음 문제 위치는 경복궁입니다", "correct","3.경복궁",3
                        )
                        dialog.isCancelable = false
                        dialog.show(supportFragmentManager!!, "ConfirmDialog")
                    }
                    else{//오답
                        //Toast.makeText(this,"오답입니다, 다시 풀어보겠습니까?", Toast.LENGTH_LONG).show()
                        edit?.putInt("artmuseum_solved" , 0) // 값 넣기
                        edit?.apply() // 적용하기
                        val dialog = QuizDialog(
                            this, "오답입니다, 다시 풀어보겠습니까?","wrong","gogung",2
                        )
                        dialog.isCancelable = false
                        dialog.show(supportFragmentManager!!, "ConfirmDialog")
                    }
                }
                else if(inf["name"] == "3.한옥기술전시관"){ // 국립고궁박물관2 문제
                    binding.coursename.text = "경복궁 코스 - 국립 고궁 박물관2"
                    if(answer.toString() == hanok_answer){ //정답

                        edit?.putInt("hanok_solved" , 1) // 값 넣기
                        edit?.apply() // 적용하기

                        val dialog = QuizDialog(
                            this, "정답입니다!  " +
                                    "다음 문제 위치는 경복궁입니다", "correct","3.경복궁",3
                        )
                        dialog.isCancelable = false
                        dialog.show(supportFragmentManager!!, "ConfirmDialog")
                    }
                    else{//오답
                        //Toast.makeText(this,"오답입니다, 다시 풀어보겠습니까?", Toast.LENGTH_LONG).show()
                        edit?.putInt("hanok_solved" , 0) // 값 넣기
                        edit?.apply() // 적용하기
                        val dialog = QuizDialog(
                            this, "오답입니다, 다시 풀어보겠습니까?","wrong","gogung",2
                        )
                        dialog.isCancelable = false
                        dialog.show(supportFragmentManager!!, "ConfirmDialog")
                    }
                }
                else if(inf["name"] == "4.장안문"){ // 국립고궁박물관2 문제
                    binding.coursename.text = "경복궁 코스 - 국립 고궁 박물관2"
                    if(answer.toString() == janganmoon_answer){ //정답

                        edit?.putInt("janganmoon_solved" , 1) // 값 넣기
                        edit?.apply() // 적용하기

                        val dialog = QuizDialog(
                            this, "정답입니다!  " +
                                    "다음 문제 위치는 경복궁입니다", "correct","3.경복궁",3
                        )
                        dialog.isCancelable = false
                        dialog.show(supportFragmentManager!!, "ConfirmDialog")
                    }
                    else{//오답
                        //Toast.makeText(this,"오답입니다, 다시 풀어보겠습니까?", Toast.LENGTH_LONG).show()
                        edit?.putInt("janganmoon_solved" , 0) // 값 넣기
                        edit?.apply() // 적용하기
                        val dialog = QuizDialog(
                            this, "오답입니다, 다시 풀어보겠습니까?","wrong","gogung",2
                        )
                        dialog.isCancelable = false
                        dialog.show(supportFragmentManager!!, "ConfirmDialog")
                    }
                }
                else if(inf["name"] == "5.북서포루"){ // 국립고궁박물관2 문제
                    binding.coursename.text = "경복궁 코스 - 국립 고궁 박물관2"
                    if(answer.toString() == booksuporu_answer){ //정답

                        edit?.putInt("booksuporu_solved" , 1) // 값 넣기
                        edit?.apply() // 적용하기

                        val dialog = QuizDialog(
                            this, "정답입니다!  " +
                                    "다음 문제 위치는 경복궁입니다", "correct","3.경복궁",3
                        )
                        dialog.isCancelable = false
                        dialog.show(supportFragmentManager!!, "ConfirmDialog")
                    }
                    else{//오답
                        //Toast.makeText(this,"오답입니다, 다시 풀어보겠습니까?", Toast.LENGTH_LONG).show()
                        edit?.putInt("booksuporu_solved" , 0) // 값 넣기
                        edit?.apply() // 적용하기
                        val dialog = QuizDialog(
                            this, "오답입니다, 다시 풀어보겠습니까?","wrong","gogung",2
                        )
                        dialog.isCancelable = false
                        dialog.show(supportFragmentManager!!, "ConfirmDialog")
                    }
                }
                else if(inf["name"] == "6.화서문"){ // 우리소리박물관 문제
                    binding.coursename.text = "경복궁 코스 - 우리소리박물관"
                    if(answer.toString() == hwasumoon_answer){ //정답

                        edit?.putInt("hwasumoon_solved" , 1) // 값 넣기
                        edit?.apply() // 적용하기

                        val pref = getSharedPreferences("solved_problem_count", AppCompatActivity.MODE_PRIVATE) //shared key 설정
                        val edit = pref?.edit() // 수정모드

                        val gyungbukcourse = pref?.getInt("gogung_solved",0)?.plus(pref?.getInt("gogung_solved2",0)!!)
                            ?.plus(pref?.getInt("gyungbuk_solved",0)!!)?.plus(pref?.getInt("gyungbuk_solved2",0)!!)
                            ?.plus(pref?.getInt("gongyae_solved",0)!!)?.plus(pref?.getInt("sori_solved",0)!!
                            )?.plus(pref?.getInt("gyunghiru_solved",0)!!)
                            ?.plus(pref?.getInt("dongsibjagak_solved",0)!!)


                        if(gyungbukcourse == 8){//모든 문제가 다 풀렸으면
                            val dialog = QuizDialog(
                                this, "정답입니다!  " +
                                        "모든 문제를 맞추셨습니다! " +
                                        "코스가 종료됩니다", "end","end",2
                            )
                            dialog.isCancelable = false
                            dialog.show(supportFragmentManager!!, "ConfirmDialog")
                        }else{ // 문제가 다 안풀렸을 때
                            val dialog = QuizDialog(
                                this, "정답입니다!  " +
                                        "안 푼 문제가 있습니다. " +
                                        "남은 문제를 풀어주세요", "end","notend",2
                            )
                            dialog.isCancelable = false
                            dialog.show(supportFragmentManager!!, "ConfirmDialog")
                        }
                    }

                    else{//오답
                        //Toast.makeText(this,"오답입니다, 다시 풀어보겠습니까?", Toast.LENGTH_LONG).show()
                        edit?.putInt("hwasumoon_solved" , 0) // 값 넣기
                        edit?.apply() // 적용하기
                        val dialog = QuizDialog(
                            this, "오답입니다, 다시 풀어보겠습니까?","wrong","gogung",8
                        )
                        dialog.isCancelable = false
                        dialog.show(supportFragmentManager!!, "ConfirmDialog")
                    }
                }



            }

        }





    }


    override fun onYesButtonClick(id: Int) {
        TODO("Not yet implemented")
    }
}