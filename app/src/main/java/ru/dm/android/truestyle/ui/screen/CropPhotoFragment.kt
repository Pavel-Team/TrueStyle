package ru.dm.android.truestyle.ui.screen

import android.R.attr.x
import android.R.attr.y
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore.Images.Media.getBitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.databinding.FragmentCropPhotoBinding
import ru.dm.android.truestyle.viewmodel.CropPhotoViewModel


private const val TAG = "CropPhotoFragment"
private const val ARG_URI_PHOTO = "URI_PHOTO"

class CropPhotoFragment: Fragment() {

    private lateinit var viewModel: CropPhotoViewModel
    private var _binding: FragmentCropPhotoBinding? = null
    private val binding get() = _binding!!

    private var bitmapImage: Bitmap? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(CropPhotoViewModel::class.java)

        //Получаем аргументы
        viewModel.liveDataPhotoUri.value = arguments?.get(ARG_URI_PHOTO) as Uri

        _binding = FragmentCropPhotoBinding.inflate(inflater, container, false)

        val root: View = binding.root
        binding.lifecycleOwner = this@CropPhotoFragment

        binding.buttonApply.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                val cropBox = binding.cropView.getCropBox() //Выделенная область в CropView

                if (cropBox != null && bitmapImage != null) {

                    val bitmapImageView = (binding.imageViewCropPhoto.drawable as BitmapDrawable).bitmap
                    Log.d(TAG, "width = ${bitmapImageView.width}")
                    Log.d(TAG, "height = ${bitmapImageView.height}")

                    val matrix = Matrix()
                    //Случай, если imageView всё перевернуло
                    if (cropBox.bottom > bitmapImageView.height)
                        matrix.setRotate(90f)

                    val cropBitmap = Bitmap.createBitmap(
                        bitmapImageView,
                        cropBox.left.toInt(),
                        cropBox.top.toInt(),
                        (cropBox.right - cropBox.left).toInt(),
                        (cropBox.bottom - cropBox.top).toInt(),
                        matrix,
                        true
                    )

//                    val width = (binding.cropView.right - binding.cropView.left)
//                    val height = (binding.cropView.bottom - binding.cropView.top)
//                    val centerX = width/2
//                    val centerY = height/2
//
//                    val startY = if (centerY - cropBox.top > height/2) 0 else centerY - cropBox.top

                    binding.imageViewCropPhoto.setImageBitmap(cropBitmap)
                    Toast.makeText(requireContext(), "good", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), R.string.error_crop, Toast.LENGTH_SHORT).show()
                }
            }

        })

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.liveDataPhotoUri.observe(viewLifecycleOwner, Observer {
            if (it!=null) {
                val inputStream = context?.contentResolver?.openInputStream(it)
                bitmapImage = BitmapFactory.decodeStream(inputStream)
                binding.imageViewCropPhoto.setImageBitmap(bitmapImage)
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }



    companion object {
        fun newInstance(uri: Uri?) : CropPhotoFragment {
            val args = Bundle().apply {
                putParcelable(ARG_URI_PHOTO, uri)
            }
            return CropPhotoFragment().apply {
                arguments = args
            }
        }
    }
}