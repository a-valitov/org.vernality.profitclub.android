package org.vernality.profitclub.view.supplier

import android.content.Intent
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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.jakewharton.rxbinding2.widget.RxTextView
import com.parse.ParseFile
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_of_creating_offer.view.*
import kotlinx.android.synthetic.main.fragment_role.view.*
import kotlinx.android.synthetic.main.supply_bottom_dialog_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.vernality.profitclub.R
import org.vernality.profitclub.model.data.AppState
import org.vernality.profitclub.model.data.CommercialOffer
import org.vernality.profitclub.utils.ui.ImagesUtils
import org.vernality.profitclub.utils.ui.ImagesUtils.Companion.convertBitmapToPNGByteArray
import org.vernality.profitclub.utils.ui.ImagesUtils.Companion.getBitmapFromByteArray
import org.vernality.profitclub.utils.ui.ImagesUtils.Companion.getBitmapFromImagesUri
import org.vernality.profitclub.utils.ui.openFileInExternalApp
import org.vernality.profitclub.view.fragments.ErrorResultDialogFragment
import org.vernality.profitclub.view.fragments.LoadingDialogFragment
import org.vernality.profitclub.view.fragments.SuccessResultDialogFragment
import org.vernality.profitclub.view.fragments.TypeDialogFragment
import org.vernality.profitclub.view.organization.adapter.LoadingStateDoc
import org.vernality.profitclub.view.organization.adapter.OffersDocListRVAdapter
import java.io.FileNotFoundException


class OfferCreatingFragment : Fragment() {

    private val viewModel by viewModel<OfferCreatingFragmentViewModel>()

    private lateinit var createOfferBTN: MaterialButton
    private lateinit var offersImageIV: ShapeableImageView
    private lateinit var addPhotoLayout:LinearLayout
    private lateinit var changePhotoLayout:LinearLayout
    private lateinit var enterMessageET: TextInputEditText
    private lateinit var addDocsIV: FrameLayout

    private lateinit var sendForProcessing: SuccessResultDialogFragment
    private lateinit var successResultDialog: SuccessResultDialogFragment
    private lateinit var errorResultDialog: ErrorResultDialogFragment
    private lateinit var loadingDialog: LoadingDialogFragment
    private lateinit var loadingLayout: FrameLayout

    private val adapter: OffersDocListRVAdapter by lazy { OffersDocListRVAdapter(onListItemClickListener) }
    private lateinit var rv: RecyclerView

    private lateinit var offer: CommercialOffer

    private val onListItemClickListener: OffersDocListRVAdapter.OnListItemClickListener =
        object : OffersDocListRVAdapter.OnListItemClickListener {
            override fun onItemClick(numberFileInList: Int, format:String) {
                Toast.makeText(requireActivity()," number = $numberFileInList", Toast.LENGTH_SHORT).show()
                println("------parse file")
                val list = offer.getList<ParseFile>("attachmentFiles")
                val file = list?.get(numberFileInList)?.file

                openFileInExternalApp(file, format, requireContext())

            }
        }

    private val observer = Observer<AppState> { renderData(it) }

    private val observerForDocs = Observer<List<LoadingStateDoc>> { renderDocsList(it) }

    private val observerForImage = Observer<ByteArray> { renderImage(it) }

    val compos = CompositeDisposable()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_of_creating_offer, container, false)

        println(resources.getString(R.string._122))

        initViews(view)

        return view
    }


    private fun initViews(view: View){
        createOfferBTN = view.btn_offer_create
        offersImageIV = view.iv_offers_image
        addPhotoLayout = view.layout_add_photo
        changePhotoLayout = view.layout_change_photo
        enterMessageET = view.et_enter_message_offer
        loadingLayout = view.loading_frame_layout
        addDocsIV = view.add_doc

        rv = view.rv_offers_doc

        viewModel.getLiveForDocs().observe(viewLifecycleOwner, observerForDocs)
        viewModel.getLiveForImage().observe(viewLifecycleOwner, observerForImage)


        viewModel.getMessage()?.let {
            enterMessageET.setText(it)
        }

        setRxToEnterMessageET(enterMessageET)

        createOfferBTN.setOnClickListener{
            viewModel.getLiveDataAndStartGetResult().observe(requireActivity(), observer) }


        addPhotoLayout.setOnClickListener {
            Toast.makeText(requireActivity(), "add photo frame clicked", Toast.LENGTH_LONG).show()
            addPhoto()
        }

        changePhotoLayout.setOnClickListener {
            Toast.makeText(requireActivity(), "add photo frame clicked", Toast.LENGTH_LONG).show()
            addPhoto()
        }

        addDocsIV.setOnClickListener {
            Toast.makeText(requireActivity(), "add doc clicked", Toast.LENGTH_LONG).show()
            addDocs()
        }

    }

    private fun addDocs(){
//        val photoPickerIntent = Intent(Intent.ACTION_PICK)
//        //Тип получаемых объектов - image:
//        photoPickerIntent.type = "*/*"
//        //Запускаем переход с ожиданием обратного результата в виде информации об изображении:
//        startActivityForResult(photoPickerIntent, Pick_doc)

        val intentPDF = Intent(Intent.ACTION_GET_CONTENT)
        intentPDF.type = "*/*"
        intentPDF.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
        intentPDF.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(Intent.createChooser(intentPDF, "Select Picture"), Pick_doc)

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

                        imageUri?.let { viewModel.uploadImageToOffer(it) }

                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }
                }
            Pick_doc ->
                if (resultCode == AppCompatActivity.RESULT_OK) {
                    try {

                        //Получаем URI изображения, преобразуем его в Bitmap
                        //объект и отображаем в элементе ImageView нашего интерфейса:
                        val docUri: Uri? = data?.data
                        println("-----file doc uri = "+ docUri)

                        docUri?.let { viewModel.uploadFileToOffer(it) }

                        Toast.makeText(requireActivity(), "doc ok", Toast.LENGTH_LONG).show()
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
                    ErrorResultDialogFragment.newInstance(description = appState.error.message?:getString(R.string._minus1))
                errorResultDialog.show(parentFragmentManager, this.toString())
            }
        }
    }

    private fun renderDocsList(list:List<LoadingStateDoc>){

        rv.adapter = adapter
        adapter.setData(list = list)

        list.forEach {

        }
    }

    private fun renderImage(byte: ByteArray){
        offersImageIV.setImageBitmap(getBitmapFromByteArray(byte))

        addPhotoLayout.visibility = View.GONE
        changePhotoLayout.visibility = View.VISIBLE

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
        public const val Pick_doc = 7890
        var mimetypes =
            arrayOf("application/pdf","application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document")

        private const val MIME_FILE_TYPE ="application/pdf|application/msword"

        fun newInstance(): OfferCreatingFragment {
            val fragment = OfferCreatingFragment()
            return fragment
        }
    }
}
