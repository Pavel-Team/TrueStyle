package ru.dm.android.truestyle.ui.screen

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.api.response.Gender
import ru.dm.android.truestyle.api.response.Stuff
import ru.dm.android.truestyle.databinding.*
import ru.dm.android.truestyle.viewmodel.AddClothesViewModel
import java.io.File
import java.lang.RuntimeException


private const val TAG = "AddClothesFragment"
private const val ARG_URI_PHOTO = "URI_PHOTO" //Bitmap изображения с прошлого фрагмента
private const val ARG_CATEGORY = "CATEGORY"   //Определенная нейронной сетью категория


class AddClothesFragment: Fragment() {
    private lateinit var viewModel: AddClothesViewModel
    private var _binding: FragmentAddClothesBinding? = null
    private val binding get() = _binding!!

    private var width = 0
    private var category: String = "" //Категория, полученная из аргументов (определенная нейронкой)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Получаем размеры ширины экрана
        val windowManager: WindowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            width = windowManager.currentWindowMetrics.bounds.width()
        } else {
            width = windowManager.defaultDisplay.width
        }
    }


    @SuppressLint("WrongThread")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView")
        viewModel = ViewModelProvider(this).get(AddClothesViewModel::class.java)
        _binding = FragmentAddClothesBinding.inflate(inflater, container, false)

        //Получаем аргументы
        viewModel.uriImage = arguments?.get(ARG_URI_PHOTO) as Uri
        category = arguments?.getString(ARG_CATEGORY, "")!!

        viewModel.liveData.value!!.articleType = category
        binding.autoCompleteTextViewCategory.setText(category)

        //Делаем высоту imageView равную ширине для вертикальной ориентации. Для горизонтальной - после создания элемента
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            binding.imageViewClothes.layoutParams = ViewGroup.LayoutParams(width, width)
        else {
            //После рассчета берем высоту
            binding.root.post(object : Runnable {
                override fun run() {
                    val height = binding.scrollViewClothes.getChildAt(0).measuredHeight
                    binding.imageViewClothes.layoutParams = ViewGroup.LayoutParams(height, height)
                }
            })
        }

        //Слушатель на кнопку назад
        binding.imageButtonBack.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                activity?.onBackPressed()
            }
        })

        //Настраиваем изображение
        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireContext().contentResolver, viewModel.uriImage!!))
        } else {
            MediaStore.Images.Media.getBitmap(requireContext().contentResolver, viewModel.uriImage!!)
        }
        binding.imageViewClothes.setImageBitmap(bitmap)

        //Первоначальная настройка всех полей ввода
        initFields()

        //Слушатель на кнопку "Добавить в гардероб"
        binding.buttonAddWardrobe.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                onClickButtonAddWardrobe(bitmap)
            }
        })

        //Слушатель на фокус EditText с названием
        binding.editTextTitle.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                if (binding.editTextTitle.text.isEmpty()) {
                    viewModel.liveDataIsCorrectTitle.value = false
                    binding.textViewTitle.setTextColor(resources.getColor(R.color.red))
                } else {
                    viewModel.liveDataIsCorrectTitle.value = true
                    binding.textViewTitle.setTextColor(resources.getColor(R.color.black))
                }
            }
        }

        val root: View = binding.root
        binding.lifecycleOwner = this@AddClothesFragment
        binding.viewModel = viewModel

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.liveData.observe(viewLifecycleOwner, Observer {
            binding.autoCompleteTextViewCategory.setText(it.articleType)
        })

        viewModel.liveDataCategories.observe(viewLifecycleOwner, Observer {
            initAutoCompleteTextViewCategory()
        })

        viewModel.liveDataStatusAddStuff.observe(viewLifecycleOwner, Observer {
            if (it.equals("Stuff ADDED")) {
                activity?.onBackPressed()
                Toast.makeText(requireContext(), R.string.succes_load_photo, Toast.LENGTH_SHORT).show()
            } else if (it.equals("Stuff didn't add, because The wardrobe is limited to 110 items!")) {
                Toast.makeText(requireContext(), R.string.limit_photo, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), R.string.error_load_user_stuff, Toast.LENGTH_SHORT).show()
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    //Первоначальная инициализация всех полей с заполнением
    private fun initFields() {
        //initAutoCompleteTextViewCategory() вызывается после прихода списка категорий в onCreateView
        initSpinnerSeason()
        initSpinnerGender()
    }


    //Первоначальная инициализация поля с категорией
    private fun initAutoCompleteTextViewCategory() {
        //val arrayCategories = resources.getStringArray(R.array.categories_stuff)
        val listCategories = viewModel.liveDataCategories.value!!
        val adapterCategory = ArrayAdapter<String>(
            requireContext(),
            R.layout.item_spinner_auto_complete_text_view,
            R.id.title_category,
            listCategories
        )
        binding.autoCompleteTextViewCategory.setAdapter(adapterCategory)
        binding.autoCompleteTextViewCategory.threshold = 1

        //Если к компоненту перешёл фокус
        binding.autoCompleteTextViewCategory.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                //Выводим выпадающий список
                try {
                    binding.autoCompleteTextViewCategory.showDropDown()
                } catch (e : WindowManager.BadTokenException) {
                    binding.autoCompleteTextViewCategory.dismissDropDown()
                    e.printStackTrace()
                }
            } else {
                if (listCategories.contains(binding.autoCompleteTextViewCategory.text.toString())) {
                    binding.textViewCategory.setTextColor(resources.getColor(R.color.black))
                    viewModel.liveDataIsCorrectCategory.value = true
                } else {
                    binding.textViewCategory.setTextColor(resources.getColor(R.color.red))
                    viewModel.liveDataIsCorrectCategory.value = false
                }
            }
        }
    }


    //Первоначальная инициализация поля с выбором сезона
    private fun initSpinnerSeason() {
        val arraySeasons = resources.getStringArray(R.array.seasons)
        val adapter = SpinnerAdapter(
            requireContext(),
            R.layout.item_spinner_season,
            arraySeasons
        )
        binding.spinnerSeason.adapter = adapter
        binding.spinnerSeason.setSelection(arraySeasons.size-1)

        binding.spinnerSeason.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                //Оповещаем viewModel
                viewModel.liveData.value!!.season = binding.spinnerSeason.selectedItem.toString()
                Log.d(TAG, viewModel.liveData.value!!.season)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                //...
            }
        }
    }


    //Первоначальная инициализация поля с выбором гендера
    private fun initSpinnerGender() {
        val arraySeasons = resources.getStringArray(R.array.genders)
        val adapter = SpinnerAdapter(
            requireContext(),
            R.layout.item_spinner_season,
            arraySeasons
        )
        binding.spinnerGender.adapter = adapter
        binding.spinnerGender.setSelection(arraySeasons.size-1)

        binding.spinnerGender.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                //Оповещаем viewModel
                val genderTitle = binding.spinnerGender.selectedItem.toString()
                viewModel.liveData.value!!.gender = viewModel.getServerGender(genderTitle)
                Log.d(TAG, viewModel.liveData.value!!.gender.toString())
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                //...
            }
        }
    }


    //Проверяет корректность полей ввода, меняя viewModel
    private fun checkCorrectInput() {
        //Название
        viewModel.liveDataIsCorrectTitle.value = binding.editTextTitle.text.isNotEmpty()

        //Категория
        viewModel.liveDataIsCorrectCategory.value = resources.getStringArray(R.array.categories_stuff).contains(binding.autoCompleteTextViewCategory.text.toString())

        //Сезон
        viewModel.liveDataIsCorrectSeason.value = binding.spinnerSeason.selectedItemId != resources.getStringArray(R.array.seasons).size.toLong() - 1

        //Гендер
        viewModel.liveDataIsCorrectGender.value = binding.spinnerGender.selectedItemId != resources.getStringArray(R.array.genders).size.toLong() - 1
    }


    //Действия по нажатию на кнопку "Добавить в гардероб"
    private fun onClickButtonAddWardrobe(bitmap: Bitmap) {
        var isCorrectData = true //Корректны ли все данные

        //Проверяем корректность ввода полей
        checkCorrectInput()

        //Подсветка каждого поля при ошибке
        if (viewModel.liveDataIsCorrectTitle.value!!) {
            binding.textViewTitle.setTextColor(resources.getColor(R.color.black))
        } else {
            binding.textViewTitle.setTextColor(resources.getColor(R.color.red))
            isCorrectData = false
        }

        if (viewModel.liveDataIsCorrectCategory.value!!) {
            binding.textViewCategory.setTextColor(resources.getColor(R.color.black))
        } else {
            binding.textViewCategory.setTextColor(resources.getColor(R.color.red))
            isCorrectData = false
        }

        if (viewModel.liveDataIsCorrectSeason.value!!) {
            binding.textViewSeason.setTextColor(resources.getColor(R.color.black))
        } else {
            binding.textViewSeason.setTextColor(resources.getColor(R.color.red))
            isCorrectData = false
        }

        if (viewModel.liveDataIsCorrectGender.value!!) {
            binding.textViewGender.setTextColor(resources.getColor(R.color.black))
        } else {
            binding.textViewGender.setTextColor(resources.getColor(R.color.red))
            isCorrectData = false
        }

        //Отправка на сервер
        if (isCorrectData) {
            viewModel.addUserStuff(bitmap)
            //Навигация происходит только после успешного добавления фото в onViewCreated()
            //По хорошему бы добавить анимацию и все дела
        } else {
            Toast.makeText(requireContext(), resources.getString(R.string.incorrect_fields), Toast.LENGTH_SHORT).show()
        }
    }



    companion object {
        fun newInstance(uri: Uri?, category: String) : AddClothesFragment {
            val args = Bundle().apply {
                putParcelable(ARG_URI_PHOTO, uri)
                putString(ARG_CATEGORY, category)
            }
            return AddClothesFragment().apply {
                arguments = args
            }
        }
    }



    /**Класс адаптера с подсказкой "Выберите" для выпадающих списков*/
    private inner class SpinnerAdapter(context: Context, res: Int, val array: Array<String>) : ArrayAdapter<String>(context, res, array) {

        override fun getCount(): Int {
            return array.size - 1 //Дабы не показывать "Выберите"
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            return getCustomView(position)
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            return getCustomViewSelected(position)
        }

        private fun getCustomView(position: Int): View {
            val bindingItem = ItemSpinnerSeasonBinding.inflate(requireActivity().layoutInflater)
            bindingItem.titleSeason.text = array.get(position)

            return bindingItem.root
        }

        private fun getCustomViewSelected(position: Int): View {
            val bindingItem = ItemSelectedSpinnerSeasonBinding.inflate(requireActivity().layoutInflater)
            bindingItem.titleSeason.text = array.get(position)

            return bindingItem.root
        }

    }
}