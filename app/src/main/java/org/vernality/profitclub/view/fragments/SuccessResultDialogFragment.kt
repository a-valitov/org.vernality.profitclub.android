package org.vernality.profitclub.view.fragments

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import fr.tvbarthel.lib.blurdialogfragment.BlurDialogEngine
import fr.tvbarthel.lib.blurdialogfragment.SupportBlurDialogFragment
import kotlinx.android.synthetic.main.dialog_fragment_registration_success.view.*
import org.vernality.profitclub.R
import org.vernality.profitclub.view.activities.ActivityWithStatusBar


class SuccessResultDialogFragment(_clickListener: View.OnClickListener) : SupportBlurDialogFragment() {

    companion object {
        fun newInstance(clickListener: View.OnClickListener): SuccessResultDialogFragment {
            return SuccessResultDialogFragment(clickListener)
        }
    }

    lateinit var mBlurEngine: BlurDialogEngine

    var clickListener: View.OnClickListener = _clickListener


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.dialog_fragment_registration_success, container, false)
        root.tv_resume.setOnClickListener {
            Toast.makeText(requireActivity(), "TV resume clicked ", Toast.LENGTH_LONG).show()
            //findNavController().navigate(R.id.action_registrationFragment_to_roleFragment)
            val intent = Intent(requireActivity(), ActivityWithStatusBar::class.java)
            requireActivity().startActivity(intent)
            dismiss()
        }

        dialog?.window?.setDimAmount(1f)

        this.dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return root
    }



    override fun getBlurRadius(): Int {
        return 25
    }

    override fun isRenderScriptEnable(): Boolean {
        return true
    }


    override fun isDimmingEnable(): Boolean {
        return false
    }

    override fun isActionBarBlurred(): Boolean {
        return false
    }

    override fun isDebugEnable(): Boolean {
        return false
    }
}