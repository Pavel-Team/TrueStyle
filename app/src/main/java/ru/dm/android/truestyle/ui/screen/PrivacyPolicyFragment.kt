/**Фрагмент с политикой конфидециальности ИЛИ условиями пользования (в зависимости от url присланного в newInstance()*/
package ru.dm.android.truestyle.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import ru.dm.android.truestyle.databinding.FragmentPrivacyPolicyBinding
import ru.dm.android.truestyle.viewmodel.PrivacyPolicyViewModel

private const val ARG_URL = "URL" //URL на html-страницу с правилами/условиями

class PrivacyPolicyFragment : Fragment() {

    private lateinit var viewModel: PrivacyPolicyViewModel
    private var _binding: FragmentPrivacyPolicyBinding? = null
    private val binding get() = _binding!!

    private val args: PrivacyPolicyFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(PrivacyPolicyViewModel::class.java)

        //Получаем аргументы
        viewModel.liveDataHtmlPrivacyPolicy.value = args.url

        _binding = FragmentPrivacyPolicyBinding.inflate(inflater, container, false)
        binding.webViewPrivacyPolicy.webChromeClient = object : WebChromeClient() { //webChromeClient - для обработки событий
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                if (newProgress == 100) {
                    binding.progressBarPrivacyPolicy.visibility = View.GONE
                } else {
                    binding.progressBarPrivacyPolicy.visibility = View.VISIBLE
                    binding.progressBarPrivacyPolicy.progress = newProgress
                }
            }
        }

        val root: View = binding.root
        binding.lifecycleOwner = this@PrivacyPolicyFragment

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Загрузилась инфа - показываем её
        viewModel.liveDataHtmlPrivacyPolicy.observe(viewLifecycleOwner, Observer {
            binding.webViewPrivacyPolicy.loadUrl(it)
        })

    }


    override fun onDestroy() {
        super.onDestroy()
        binding.webViewPrivacyPolicy.webChromeClient=null
        _binding = null
    }




    companion object {
        const val ARG_URL = "URL" //URL на html-страницу с правилами/условиями

        fun newInstance(url: String) : PrivacyPolicyFragment {
            val args = Bundle().apply {
                putString(ARG_URL, url)
            }
            return PrivacyPolicyFragment().apply {
                arguments = args
            }
        }
    }
}