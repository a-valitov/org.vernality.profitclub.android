package org.vernality.profitclub.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.card_login.view.*
import kotlinx.android.synthetic.main.card_registration.view.cv_bottom_sheet
import kotlinx.android.synthetic.main.card_registration.view.et_enter_login
import kotlinx.android.synthetic.main.card_registration.view.et_enter_password
import kotlinx.android.synthetic.main.fragment_login.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.vernality.profitclub.R
import org.vernality.profitclub.model.data.AppState
import org.vernality.profitclub.view.activities.SelectOrganizationActivity
import org.vernality.profitclub.view_model.LoginFragmentViewModel


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [RegistrationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {

    private val viewModel by viewModel<LoginFragmentViewModel>()

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var loadingLayout: FrameLayout

    private val observer = Observer<AppState> { renderData(it) }

    private lateinit var errorResultDialog: ErrorResultDialogFragment

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
        val root = inflater.inflate(R.layout.fragment_login, container, false)

        init(root)
        if(savedInstanceState == null) showAnimation(root)
        return root
    }

    fun showAnimation(root: View){
        val show = AnimationUtils.loadAnimation(requireContext(),
            R.anim.show_video
        );
        root.cv_bottom_sheet.startAnimation(show)
    }


    fun init(root:View){
        val loginET = root.et_enter_login
        val editPasswordET = root.et_enter_password
        val enterBTN = root.btn_enter
        val registerAccountTV = root.tv_register
        val forgotPasswordTV = root.tv_forgotPassword
        loadingLayout = root.loading_frame_layout

        setRxToLoginET(loginET)
        setRxToPassET(editPasswordET)
        setRxToEnterBtn(enterBTN)
        setRxToRegisterAccTV(registerAccountTV)
        setRxForgotPasswordTV(forgotPasswordTV)

    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success<*> -> {
                navigateTo()
                loadingLayout.visibility = View.GONE

            }
            is AppState.Loading -> {
                loadingLayout.visibility = View.VISIBLE
                Toast.makeText(requireActivity(), "Loading", Toast.LENGTH_LONG).show()
            }
            is AppState.Error -> {
                loadingLayout.visibility = View.GONE
                errorResultDialog =
                    ErrorResultDialogFragment.newInstance(description = appState.error.message?:getString(R.string._minus1))
                errorResultDialog.show(parentFragmentManager, this.toString())
            }
        }
    }


    private fun showSuccessDialog(){

    }




    fun navigateTo(){
        val intent = Intent(requireActivity(), SelectOrganizationActivity::class.java)
        startActivity(intent)
        //findNavController().navigate(R.id.action_registrationFragment_to_roleFragment)
//        findNavController().navigate(R.id.action_registrationFragment_to_mainFragment)
        Toast.makeText(requireActivity(), "TV resume clicked ", Toast.LENGTH_LONG).show()
    }







    private fun setRxToRegisterAccTV(registerAccountTV: TextView) {
        val disposableTvRegister = RxView.clicks(registerAccountTV)
            .subscribe {
                Toast.makeText(requireActivity(), "TV enter clicked ", Toast.LENGTH_LONG).show()
                viewModel.enterInAccount()
                findNavController().navigate(R.id.action_loginFragment_to_registrationFragment2)

            }

        compos.add(disposableTvRegister)
    }

    private fun setRxForgotPasswordTV(forgotPasswordTV: TextView) {
        val disposableTvForgotPassword = RxView.clicks(forgotPasswordTV)
            .subscribe {
                Toast.makeText(requireActivity(), "button registration clicked ", Toast.LENGTH_LONG).show()

                findNavController().navigate(R.id.action_loginFragment_to_resetPasswordActivity)



            }

        compos.add(disposableTvForgotPassword)
    }

    private fun setRxToEnterBtn(registrBTN: MaterialButton) {
        val disposableBtnReg = RxView.clicks(registrBTN)
            .subscribe {
                Toast.makeText(requireActivity(), "button registration clicked ", Toast.LENGTH_LONG).show()


                viewModel.getLiveDataAndStartGetResult().observe(requireActivity(), observer)
            }

        compos.add(disposableBtnReg)
    }


    private fun setRxToPassET(editPassword1ET: TextInputEditText) {
        val disposablePass1: Disposable = RxTextView.textChanges(editPassword1ET)
            .subscribe(object : Consumer<CharSequence?> {
                @Throws(Exception::class)
                override fun accept(charSequence: CharSequence?) {
                    //Add your logic to work on the Charsequence
                    //Toast.makeText(requireActivity(), "password 1 enter", Toast.LENGTH_LONG).show()
                    viewModel.setPassword(charSequence.toString())
                }
            })

        compos.add(disposablePass1)
    }


    private fun setRxToLoginET(loginET: TextInputEditText) {
        val disposableLogin: Disposable = RxTextView.textChanges(loginET)
            .subscribe(object : Consumer<CharSequence?> {
                @Throws(Exception::class)
                override fun accept(charSequence: CharSequence?) {
                    //Add your logic to work on the Charsequence
                    //Toast.makeText(requireActivity(), "password 1 enter", Toast.LENGTH_LONG).show()
                    viewModel.setLogin(charSequence.toString())
                }
            })

        compos.add(disposableLogin)
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
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}