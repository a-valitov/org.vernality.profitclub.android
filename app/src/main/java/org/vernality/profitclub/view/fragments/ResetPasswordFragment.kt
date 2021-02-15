package org.vernality.profitclub.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.setMargins
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_registration.view.*
import kotlinx.android.synthetic.main.fragment_reset_password.view.*
import kotlinx.android.synthetic.main.fragment_reset_password.view.loading_frame_layout
import org.koin.android.viewmodel.ext.android.viewModel
import org.vernality.profitclub.R
import org.vernality.profitclub.model.data.AppState
import org.vernality.profitclub.view.activities.MainActivity
import org.vernality.profitclub.view.activities.OnBackPresser
import org.vernality.profitclub.view_model.ResetPasswordFragmentViewModel


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [RegistrationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResetPasswordFragment : Fragment() {

    private val viewModel by viewModel<ResetPasswordFragmentViewModel>()

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var loadingLayout: FrameLayout
    private lateinit var successResultDialog: SuccessResultDialogFragment
    private lateinit var errorResultDialog: ErrorResultDialogFragment

    lateinit var relative: RelativeLayout
    lateinit var invalidGmailTV: TextView
    lateinit var enterGmailET: TextInputEditText

    private val observer = Observer<AppState> { renderData(it) }

    val compos = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_reset_password, container, false)

        init(root)

        return root
    }




    fun init(root:View){
        enterGmailET = root.et_enter_gmail
        val resetBTN = root.btn_reset
        val backCL = root.back
        invalidGmailTV = root.tv_invalid_email
        relative = root.relative

        loadingLayout = root.loading_frame_layout

        setRxBackTV(backCL)
        setRxToResetBtn(resetBTN)
        setRxToGmailET(enterGmailET)
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success<*> -> {
                loadingLayout.visibility = View.GONE
                val successResultDialog
                        = SuccessResultDialogFragment.newInstance(TypeDialogFragment.ResetPassword
                ) { navigateTo() }

                successResultDialog.show(parentFragmentManager, this@ResetPasswordFragment.toString())

            }
            is AppState.Loading -> {
                Toast.makeText(requireActivity(), "Loading", Toast.LENGTH_LONG).show()
                loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                loadingLayout.visibility = View.GONE
                setAlertColor()
                errorResultDialog =
                    ErrorResultDialogFragment.newInstance(description = appState.error.message?:getString(R.string._minus1))
                errorResultDialog.show(parentFragmentManager, this.toString())

            }
        }
    }



    fun setAlertColor(){
        relative.background = requireContext().getDrawable(R.drawable.gradient_trans_red)
        invalidGmailTV.visibility = View.VISIBLE
        enterGmailET.setTextColor(resources.getColor(R.color.colorAlert))
    }


    fun navigateTo(){

        //findNavController().navigate(R.id.action_registrationFragment_to_roleFragment)
        val intent = Intent(requireActivity(), MainActivity::class.java)
        requireActivity().startActivity(intent)
        requireActivity().onBackPressed()
//        findNavController().navigate(R.id.action_global_loginFragment)
        Toast.makeText(requireActivity(), "TV resume clicked ", Toast.LENGTH_LONG).show()
    }


    private fun setRxBackTV(backTV: ConstraintLayout) {
        val disposableTvRegister = RxView.clicks(backTV)
            .subscribe {
                Toast.makeText(requireActivity(), "TV enter clicked ", Toast.LENGTH_LONG).show()

                navigateTo()
                requireActivity().onBackPressed()

            }

        compos.add(disposableTvRegister)
    }


    private fun setRxToResetBtn(registrBTN: MaterialButton) {
        val disposableBtnReg = RxView.clicks(registrBTN)
            .subscribe {
                Toast.makeText(requireActivity(), "button registration clicked ", Toast.LENGTH_LONG).show()


                viewModel.getLiveDataAndStartGetResult().observe(requireActivity(), observer)
            }

        compos.add(disposableBtnReg)
    }


    private fun setRxToGmailET(gmailET: TextInputEditText) {
        val disposablePass1: Disposable = RxTextView.textChanges(gmailET)
            .subscribe(object : Consumer<CharSequence?> {
                @Throws(Exception::class)
                override fun accept(charSequence: CharSequence?) {
                    //Add your logic to work on the Charsequence
                    //Toast.makeText(requireActivity(), "password 1 enter", Toast.LENGTH_LONG).show()
                    viewModel.setGmail(charSequence.toString())
                }
            })

        compos.add(disposablePass1)
    }




    override fun onDestroyView() {
        super.onDestroyView()
        compos.clear()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RegistrationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ResetPasswordFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}