package org.vernality.profitclub.view.fragments

import android.R.attr.button
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.card.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.vernality.profitclub.R
import org.vernality.profitclub.view_model.RegistrationViewModel
import org.vernality.profitclub.view_model.Result


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"



/**
 * A simple [Fragment] subclass.
 * Use the [RegistrationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegistrationFragment : Fragment() {

    private val viewModel by viewModel<RegistrationViewModel>()

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        val root = inflater.inflate(R.layout.fragment_registration, container, false)

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
        val gmailET = root.et_enter_gmail
        val editPassword1ET = root.et_enter_password
        val editPassword2ET = root.et_confirm_password
        val registrBTN = root.btn_registr
        val enterAccountTV = root.tv_enter

        setRxToLoginET(loginET)
        setRxToGmailET(gmailET)
        setRxToPass1ET(editPassword1ET)
        setRxToPass2ET(editPassword2ET)
        setRxToRegBtn(registrBTN)
        setRxToEnterAccTV(enterAccountTV)

        initResultRegistration()

    }

    private fun initResultRegistration(){
        viewModel.resultLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer{
            if(it == Result.Success) {
                Toast.makeText(requireActivity(), "Registration is success", Toast.LENGTH_LONG).show()
                showSuccessDialog()
            }
            else {
                Toast.makeText(requireActivity(), "Registration is error", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun showSuccessDialog(){
        val successResultDialog
                = SuccessResultDialogFragment.newInstance(View.OnClickListener {
            navig()

        })
        successResultDialog.show(parentFragmentManager, "ggg")


    }

    fun navig(){

        //findNavController().navigate(R.id.action_registrationFragment_to_roleFragment)
        findNavController().navigate(R.id.action_registrationFragment_to_mainFragment)
        Toast.makeText(requireActivity(), "TV resume clicked ", Toast.LENGTH_LONG).show()
    }

    private fun setRxToEnterAccTV(enterAccountTV: TextView) {
        val disposableTvEnter = RxView.clicks(enterAccountTV)
            .subscribe {
                Toast.makeText(requireActivity(), "TV enter clicked ", Toast.LENGTH_LONG).show()
                viewModel.enterInAccount()
                findNavController().navigate(R.id.action_registrationFragment_to_roleFragment)
            }

        compos.add(disposableTvEnter)
    }

    private fun setRxToRegBtn(registrBTN: MaterialButton) {
        val disposableBtnReg = RxView.clicks(registrBTN)
            .subscribe {
                Toast.makeText(requireActivity(), "button registration clicked ", Toast.LENGTH_LONG).show()
                viewModel.registration()
            }

        compos.add(disposableBtnReg)
    }

    private fun setRxToPass2ET(editPassword2ET: TextInputEditText) {
        val disposablePass2: Disposable = RxTextView.textChanges(editPassword2ET)
            .subscribe(object : Consumer<CharSequence?> {
                @Throws(Exception::class)
                override fun accept(charSequence: CharSequence?) {
                    //Add your logic to work on the Charsequence
                    if(charSequence!!.isEmpty())Toast.makeText(requireActivity(), "password 2 enter", Toast.LENGTH_LONG).show()
                    viewModel.checkPassword(charSequence.toString())
                }
            })

        compos.add(disposablePass2)
    }

    private fun setRxToPass1ET(editPassword1ET: TextInputEditText) {
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

    private fun setRxToGmailET(gmailET: TextInputEditText) {
        val disposableGmail: Disposable = RxTextView.textChanges(gmailET)
            .subscribe(object : Consumer<CharSequence?> {
                @Throws(Exception::class)
                override fun accept(charSequence: CharSequence?) {
                    //Add your logic to work on the Charsequence
                    //Toast.makeText(requireActivity(), "password 1 enter", Toast.LENGTH_LONG).show()
                    viewModel.setGmail(charSequence.toString())
                }
            })

        compos.add(disposableGmail)
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
            RegistrationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}