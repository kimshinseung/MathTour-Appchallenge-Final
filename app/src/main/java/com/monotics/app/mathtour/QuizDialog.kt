package com.monotics.app.mathtour

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.monotics.app.mathtour.databinding.DialogConfirmBinding

class QuizDialog(
    confirmDialogInterface: QuizActivity,
    text: String, ox: String , name: String , tag: Int
) : DialogFragment() {

    // 뷰 바인딩 정의
    private var _binding: DialogConfirmBinding? = null
    private val binding get() = _binding!!

    private var confirmDialogInterface: ConfirmDialogInterface? = null

    private var text: String? = null
    private var ox: String? = null
    private var name: String? = null
    private var tag: Int?= null


    init {
        this.text = text
        this.ox = ox
        this.confirmDialogInterface = confirmDialogInterface
        this.name = name
        this.tag = tag
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogConfirmBinding.inflate(inflater, container, false)
        val view = binding.root

        // 레이아웃 배경을 투명하게 해줌, 필수 아님
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.confirmTextView.text = text
        
        if(ox == "correct"){
            binding.yesButton.text = "다음 문제 도전"
            binding.noButton.text = "취소"

            // 취소 버튼 클릭
            binding.noButton.setOnClickListener {
                dismiss()
            }
            binding.yesButton.setOnClickListener{


                val intent = Intent(activity, QuizActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                val itemList = hashMapOf(
                    "tag" to tag,
                    "name" to name
                )
                intent.putExtra("map",itemList)
                activity?.startActivity(intent)
                dismiss()
                activity?.finish()
            }
            
        }else if(ox == "wrong"){
            binding.yesButton.text = "다시 풀어보기"
            binding.noButton.text = "힌트 확인"

            // 힌트확인하기
            binding.noButton.setOnClickListener {

                if(tag == 1) {
                    binding.confirmTextView.text = "b에는 0이 들어갑니다."
                }
                else if(tag == 2) {
                    binding.confirmTextView.text = "x1!=x2일 때, y-y1 = (y2-y1)/(x2-x1) * (x-x1)입니다."
                }
                else if(tag == 3) {
                    binding.confirmTextView.text = "순열 - 8C5"
                }
                else if(tag == 4) {
                    binding.confirmTextView.text = "한 변의 길이는 45cm입니다."
                }
                else if(tag == 5) {
                    binding.confirmTextView.text = "r은 10입니다."
                }
                else if(tag == 6) {
                    binding.confirmTextView.text = "제 2사분면은 90도와 180도 사이에 있습니다"
                }
                else if(tag == 7) {
                    binding.confirmTextView.text = "노란 원의 반지름은 1입니다."
                }
                else if(tag == 8) {
                    binding.confirmTextView.text = "시그모이드함수는 모든 사분면을 지납니다."
                }


                binding.noButton.text = ""
            }
            //다시 풀어보기
            binding.yesButton.setOnClickListener{
                dismiss()
            }


        }
        else if(ox == "end"){
            binding.yesButton.text = "확인"
            binding.noButton.text = "취소"

            binding.yesButton.setOnClickListener {
                dismiss()
                activity?.finish()
            }
            binding.noButton.setOnClickListener {
                dismiss()
            }

        }


        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

interface QuizDialogInterface {
    fun onYesButtonClick(id: Int)
}