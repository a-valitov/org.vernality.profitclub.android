package org.vernality.profitclub.view.supplier

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_of_creating_action.view.*
import kotlinx.android.synthetic.main.fragment_role.view.*
import org.koin.android.ext.android.get
import org.koin.android.viewmodel.ext.android.viewModel
import org.vernality.profitclub.R
import org.vernality.profitclub.model.data.AppState
import org.vernality.profitclub.utils.ui.selectDate
import org.vernality.profitclub.view.fragments.ErrorResultDialogFragment
import org.vernality.profitclub.view.fragments.LoadingDialogFragment
import org.vernality.profitclub.view.fragments.SuccessResultDialogFragment
import org.vernality.profitclub.view.fragments.TypeDialogFragment
import java.io.FileNotFoundException
import java.io.InputStream
import org.vernality.profitclub.utils.ui.ImagesUtils.*
import org.vernality.profitclub.utils.ui.ImagesUtils.Companion.getBitmapFromImagesUri
import org.vernality.profitclub.utils.ui.ImagesUtils.Companion.convertBitmapToPNGByteArray
import java.util.*


class ActionCreatingFragment : Fragment() {

    private val viewModel by viewModel<ActionCreatingFragmentViewModel>()

    private lateinit var createActionBTN: MaterialButton
    private lateinit var actionsImageIV: ShapeableImageView
    private lateinit var addPhotoLayout:LinearLayout
    private lateinit var changePhotoLayout:LinearLayout
    private lateinit var enterMessageET: TextInputEditText
    private lateinit var enterDescriptionET: TextInputEditText
    private lateinit var insertLinkET: TextInputEditText
    private lateinit var startOfActionTV: MaterialTextView
    private lateinit var endOfActionTV: MaterialTextView

    private lateinit var sendForProcessing: SuccessResultDialogFragment
    private lateinit var successResultDialog: SuccessResultDialogFragment
    private lateinit var errorResultDialog: ErrorResultDialogFragment
    private lateinit var loadingDialog: LoadingDialogFragment
    private lateinit var loadingLayout: FrameLayout

    private val observer = Observer<AppState> { renderData(it) }

    val compos = CompositeDisposable()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_of_creating_action, container, false)

        initViews(view)

        return view
    }


    fun initViews(view: View){
        createActionBTN = view.btn_action_create_action
        actionsImageIV = view.iv_actions_image
        addPhotoLayout = view.layout_add_photo
        changePhotoLayout = view.layout_change_photo
        enterMessageET = view.et_enter_message
        enterDescriptionET = view.et_enter_description
        insertLinkET = view.et_insert_link
        loadingLayout = view.loading_frame_layout
        startOfActionTV = view.tv_start_of_action
        endOfActionTV = view.tv_end_of_action

        viewModel.getMessage()?.let { enterMessageET.textView.setText(it) }
        viewModel.getDescription()?.let { enterDescriptionET.textView.setText(it) }
        viewModel.getLink()?.let { insertLinkET.textView.setText(it) }

        setRxToEnterMessageET(enterMessageET)
        setRxToEnterDescriptionET(enterDescriptionET)
        setRxToInsertLinkET(insertLinkET)

        createActionBTN.setOnClickListener{
            viewModel.getLiveDataAndStartGetResult().observe(requireActivity(), observer) }


        addPhotoLayout.setOnClickListener {
            Toast.makeText(requireActivity(), "add photo frame clicked", Toast.LENGTH_LONG).show()
            addPhoto()
        }

        changePhotoLayout.setOnClickListener {
            Toast.makeText(requireActivity(), "add photo frame clicked", Toast.LENGTH_LONG).show()
            addPhoto()
        }

        startOfActionTV.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            selectDate(context, R.style.DatePickerDialogTheme, startOfActionTV, calendar)
            viewModel.setStartDate(calendar.time)
        }

        endOfActionTV.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            selectDate(context, R.style.DatePickerDialogTheme, endOfActionTV, calendar)
            viewModel.setEndDate(calendar.time)
        }


    }

    private fun addPhoto(){
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        //Тип получаемых объектов - image:
        photoPickerIntent.type = "image/*"
        //Запускаем переход с ожиданием обратного результата в виде информации об изображении:
        startActivityForResult(photoPickerIntent, Pick_image)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        println("-----onActivityResult()")
        when(requestCode) {
            Pick_image ->
                if (resultCode == AppCompatActivity.RESULT_OK) {
                    try {
                        println("-----onActivityResult = Ok")
                        //Получаем URI изображения, преобразуем его в Bitmap
                        //объект и отображаем в элементе ImageView нашего интерфейса:
                        val imageUri: Uri? = data?.data
//                        val imageStream: InputStream? =
//                            requireActivity().getContentResolver().openInputStream(imageUri!!)
//                        val selectedImage: Bitmap = BitmapFactory.decodeStream(imageStream)
                        val bitmap = getBitmapFromImagesUri(imageUri!!)
                        actionsImageIV.setImageBitmap(bitmap)

                        viewModel.setImageFile(convertBitmapToPNGByteArray(bitmap))
                        addPhotoLayout.visibility = View.GONE
                        changePhotoLayout.visibility = View.VISIBLE
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }
                }
        }
    }


    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success<*> -> {
                loadingLayout.visibility = View.GONE
                val data = appState.data
                when(data){
                    ResultOfCreatingAction.SuccessCheckFields -> showDialogResume()
                    ResultOfCreatingAction.SuccessCreating -> showDialogSuccessCreating()
                }
            }
            is AppState.Loading -> {
                Toast.makeText(requireActivity(), "Loading", Toast.LENGTH_LONG).show()
                loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                loadingLayout.visibility = View.GONE
                errorResultDialog =
                    ErrorResultDialogFragment.newInstance(description = appState.error.message.toString())
                Toast.makeText(requireActivity(), "Error \n ${appState.error}", Toast.LENGTH_LONG).show()
                errorResultDialog.show(parentFragmentManager, this.toString())
            }
        }
    }

    private fun showDialogSuccessCreating() {
         requireActivity().onBackPressed()


    }

    private fun showDialogResume() {

        Toast.makeText(requireActivity(), "showDialogResume()", Toast.LENGTH_LONG).show()
        sendForProcessing =
            SuccessResultDialogFragment.newInstance(TypeDialogFragment.SubmitAction
            ) { viewModel.getResult() }

        sendForProcessing.show(parentFragmentManager, this.toString())
    }


    private fun navigateToFragmentOnCreatingAction(){

        Toast.makeText(requireActivity(), "the action creation button is clicked", Toast.LENGTH_LONG).show()
    }

    private fun setRxToEnterMessageET(enterMessageET: TextInputEditText) {
        val disposablePass2: Disposable = RxTextView.textChanges(enterMessageET)
            .subscribe(object : Consumer<CharSequence?> {
                @Throws(Exception::class)
                override fun accept(charSequence: CharSequence?) {
                    //Add your logic to work on the Charsequence
                    if(charSequence!!.isEmpty())Toast.makeText(requireActivity(), "password 2 enter", Toast.LENGTH_LONG).show()
                    viewModel.setMessage(charSequence.toString())
                }
            })

        compos.add(disposablePass2)
    }

    private fun setRxToEnterDescriptionET(enterDescriptionET: TextInputEditText) {
        val disposablePass2: Disposable = RxTextView.textChanges(enterDescriptionET)
            .subscribe(object : Consumer<CharSequence?> {
                @Throws(Exception::class)
                override fun accept(charSequence: CharSequence?) {
                    //Add your logic to work on the Charsequence
                    if(charSequence!!.isEmpty())Toast.makeText(requireActivity(), "password 2 enter", Toast.LENGTH_LONG).show()
                    viewModel.setDescription(charSequence.toString())
                }
            })

        compos.add(disposablePass2)
    }

    private fun setRxToInsertLinkET(insertLinkET: TextInputEditText) {
        val disposablePass2: Disposable = RxTextView.textChanges(insertLinkET)
            .subscribe(object : Consumer<CharSequence?> {
                @Throws(Exception::class)
                override fun accept(charSequence: CharSequence?) {
                    //Add your logic to work on the Charsequence
                    if(charSequence!!.isEmpty())Toast.makeText(requireActivity(), "password 2 enter", Toast.LENGTH_LONG).show()
                    viewModel.setLink(charSequence.toString())
                }
            })

        compos.add(disposablePass2)
    }


    private fun checkingFirstLoginToAccountOfThisOrganization():Boolean{
        //same code


        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compos.clear()
    }


    companion object {
        private const val Pick_image = 8907
        fun newInstance(): ActionCreatingFragment {
            val fragment = ActionCreatingFragment()
            return fragment
        }
    }
}
