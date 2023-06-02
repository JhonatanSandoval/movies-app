package pro.jsandoval.movieapp.utils.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding> constructor(
    private val bindingFactory: (LayoutInflater) -> VB,
) : AppCompatActivity() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = bindingFactory(layoutInflater)
        setContentView(binding.root)
        init()
    }

    abstract fun init()

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}