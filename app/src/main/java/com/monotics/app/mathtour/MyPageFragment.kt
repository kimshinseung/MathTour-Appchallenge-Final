package com.monotics.app.mathtour

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.monotics.app.mathtour.databinding.ActivityMainBinding
import com.monotics.app.mathtour.databinding.FragmentMainBinding
import com.monotics.app.mathtour.databinding.FragmentMyPageBinding

class MyPageFragment : Fragment() {
    lateinit var binding: FragmentMyPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMyPageBinding.bind(view)
        
        //걸음수
        val pref1 = activity?.getSharedPreferences("count", 0)
        val currentStep = pref1?.getInt("step", 0)
        
        //문제수
        val pref =activity?.getSharedPreferences("solved_problem_count", AppCompatActivity.MODE_PRIVATE) //shared key 설정
        val edit = pref?.edit() // 수정모드

        binding.stepBtn.text = currentStep.toString()

        //완주 코스
        val pref2 =activity?.getSharedPreferences("solved_course", AppCompatActivity.MODE_PRIVATE) //shared key 설정
        val edit2 = pref2?.edit() // 수정모드

        val completecourse =
            pref2?.getInt("solved_course1",0)?.plus(pref2?.getInt("solved_course14",0)!!)
        binding.courseBtn.text = "${completecourse}/17"

        binding.one.isChecked = true


        val gyungbukcourse = pref?.getInt("gogung_solved",0)?.plus(pref?.getInt("gogung_solved2",0)!!)
            ?.plus(pref?.getInt("gyungbuk_solved",0)!!)?.plus(pref?.getInt("gyungbuk_solved2",0)!!)
            ?.plus(pref?.getInt("gongyae_solved",0)!!)?.plus(pref?.getInt("sori_solved",0)!!
            )?.plus(pref?.getInt("gyunghiru_solved",0)!!)
            ?.plus(pref?.getInt("dongsibjagak_solved",0)!!)

        val suwoncourse = pref?.getInt("hanggung_solved",0)?.plus(pref?.getInt("artmuseum_solved",0)!!)
            ?.plus(pref?.getInt("hanok_solved",0)!!)?.plus(pref?.getInt("janganmoon_solved",0)!!)
            ?.plus(pref?.getInt("booksuporu_solved",0)!!)?.plus(pref?.getInt("hwasumoon_solved",0)!!
            )

        binding.score.text = gyungbukcourse.toString()

        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            
            edit?.putInt("problem_solved" + "1", 123)// 값 넣기
            edit?.apply() // 적용하기



            when(checkedId){
                R.id.one -> binding.score.text = gyungbukcourse.toString()
                R.id.two -> binding.score.text = activity?.getSharedPreferences("solved_problem_count", 0)?.getInt("problem_solved" + 2, 0)
                    .toString()
                R.id.three -> binding.score.text = activity?.getSharedPreferences("solved_problem_count", 0)?.getInt("problem_solved" + 3, 0)
                    .toString()
                R.id.four -> binding.score.text = activity?.getSharedPreferences("solved_problem_count", 0)?.getInt("problem_solved" + 4, 0)
                    .toString()
                R.id.five -> binding.score.text = activity?.getSharedPreferences("solved_problem_count", 0)?.getInt("problem_solved" + 5, 0)
                    .toString()
                R.id.six -> binding.score.text = activity?.getSharedPreferences("solved_problem_count", 0)?.getInt("problem_solved" + 6, 0)
                    .toString()
                R.id.seven -> binding.score.text = activity?.getSharedPreferences("solved_problem_count", 0)?.getInt("problem_solved" + 7, 0)
                    .toString()
                R.id.eight -> binding.score.text = activity?.getSharedPreferences("solved_problem_count", 0)?.getInt("problem_solved" + 8, 0)
                    .toString()
                R.id.nine -> binding.score.text = activity?.getSharedPreferences("solved_problem_count", 0)?.getInt("problem_solved" + 9, 0)
                    .toString()
                R.id.ten -> binding.score.text = activity?.getSharedPreferences("solved_problem_count", 0)?.getInt("problem_solved" + 10, 0)
                    .toString()
                R.id.eleven -> binding.score.text = activity?.getSharedPreferences("solved_problem_count", 0)?.getInt("problem_solved" + 11, 0)
                    .toString()
                R.id.twelve -> binding.score.text = activity?.getSharedPreferences("solved_problem_count", 0)?.getInt("problem_solved" + 12, 0)
                    .toString()
                R.id.thirtin -> binding.score.text = activity?.getSharedPreferences("solved_problem_count", 0)?.getInt("problem_solved" + 13, 0)
                    .toString()
                R.id.fourtin -> binding.score.text = suwoncourse.toString()
                R.id.fivetin -> binding.score.text = activity?.getSharedPreferences("solved_problem_count", 0)?.getInt("problem_solved" + 15, 0)
                    .toString()
                R.id.sixtin -> binding.score.text = activity?.getSharedPreferences("solved_problem_count", 0)?.getInt("problem_solved" + 16, 0)
                    .toString()
                R.id.seventin -> binding.score.text = activity?.getSharedPreferences("solved_problem_count", 0)?.getInt("problem_solved" + 17, 0)
                    .toString()
            }
        }
        
        

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_page, container, false)
    }

}