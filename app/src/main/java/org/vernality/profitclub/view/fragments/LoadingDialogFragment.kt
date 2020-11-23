package org.vernality.profitclub.view.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.ContentLoadingProgressBar
import fr.tvbarthel.lib.blurdialogfragment.BlurDialogEngine
import fr.tvbarthel.lib.blurdialogfragment.SupportBlurDialogFragment
import kotlinx.android.synthetic.main.dialog_fragment_error.view.*
import kotlinx.android.synthetic.main.dialog_fragment_loading.view.*
import kotlinx.android.synthetic.main.dialog_fragment_success.view.tv_message
import kotlinx.android.synthetic.main.dialog_fragment_success.view.tv_subMessage
import org.vernality.profitclub.R


class LoadingDialogFragment() : SupportBlurDialogFragment() {

    companion object {
        fun newInstance(): LoadingDialogFragment {
            return LoadingDialogFragment()
        }
    }


    private lateinit var progresBar: ContentLoadingProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.dialog_fragment_loading, container, false)

        dialog?.setCancelable(true)

        init(root)

        dialog?.window?.setDimAmount(1f)

        this.dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return root
    }

    private fun init(root: View){
        progresBar = root.progress_circular

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