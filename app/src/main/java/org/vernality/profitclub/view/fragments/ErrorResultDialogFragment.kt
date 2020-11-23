package org.vernality.profitclub.view.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import fr.tvbarthel.lib.blurdialogfragment.BlurDialogEngine
import fr.tvbarthel.lib.blurdialogfragment.SupportBlurDialogFragment
import kotlinx.android.synthetic.main.dialog_fragment_error.view.*
import kotlinx.android.synthetic.main.dialog_fragment_success.view.tv_message
import kotlinx.android.synthetic.main.dialog_fragment_success.view.tv_subMessage
import org.vernality.profitclub.R


class ErrorResultDialogFragment(private var message:String,
                                private var subMessage: String,
                                private var description: String) : SupportBlurDialogFragment() {

    companion object {
        fun newInstance(message: String = "Error", subMessage: String = "", description: String = "Description"): ErrorResultDialogFragment {
            return ErrorResultDialogFragment(message, subMessage, description)
        }
    }

    private lateinit var mBlurEngine: BlurDialogEngine
    private lateinit var mesageTV: TextView
    private lateinit var subMesageTV: TextView
    private lateinit var errorDescriptionTV: TextView



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.dialog_fragment_error, container, false)

        init(root)

        initTitle()

        dialog?.window?.setDimAmount(1f)

        this.dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return root
    }

    private fun init(root: View){
        mesageTV = root.tv_message
        subMesageTV = root.tv_subMessage
        errorDescriptionTV = root.tv_error_description

    }

    private fun initTitle(){
        mesageTV.setText(message)
        subMesageTV.setText(subMessage)
        errorDescriptionTV.setText(description)
    }



    fun setName(name: String){
        val message = mesageTV.text
        val subMessage = subMesageTV.text
        mesageTV.setText(message.toString().replace("name", name, true))
        subMesageTV.setText(subMessage.toString().replace("name", name, true))
    }


    fun setTitleOnViews(array: Array<String>){
        mesageTV.setText(array[0])
        subMesageTV.setText(array[1])
        errorDescriptionTV.setText(array[2])

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