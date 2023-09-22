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

class ConfirmDialog(
    confirmDialogInterface: ConfirmDialogInterface,
    text: String, address: String, complete: String
) : DialogFragment() {

    // 뷰 바인딩 정의
    private var _binding: DialogConfirmBinding? = null
    private val binding get() = _binding!!

    private var confirmDialogInterface: ConfirmDialogInterface? = null

    private var text: String? = null
    private var address: String? = null
    private var complete : String? = null


    init {
        this.text = text
        this.address = address
        this.confirmDialogInterface = confirmDialogInterface
        this.complete = complete
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

        if(complete == "complete"){
            binding.successBtn.setImageResource(R.drawable.complete)
            // 취소 버튼 클릭
            binding.noButton.setOnClickListener {
                dismiss()
            }

            // 확인 버튼 클릭
            binding.yesButton.setOnClickListener {
                this.confirmDialogInterface?.onYesButtonClick(id!!)
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("loc",address)
                context?.startActivity(intent)
                dismiss()
            }
        }else { //완주 아님
            // 취소 버튼 클릭
            binding.noButton.setOnClickListener {
                dismiss()
            }

            // 확인 버튼 클릭
            binding.yesButton.setOnClickListener {
                this.confirmDialogInterface?.onYesButtonClick(id!!)
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("loc",address)
                context?.startActivity(intent)
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

interface ConfirmDialogInterface {
    fun onYesButtonClick(id: Int)
}