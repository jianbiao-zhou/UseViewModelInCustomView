package com.example.useviewmodelincustomview

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.example.useviewmodelincustomview.databinding.CustomLayoutBinding

class CustomView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs), ViewModelStoreOwner, LifecycleOwner {

    private val lifecycleRegistry by lazy { LifecycleRegistry(this) }
    private val viewModel by lazy {
        ViewModelProvider(this)[CustomViewModel::class.java]
    }
    private var viewModelStore = ViewModelStore()

    init {
        val view = inflate(context, R.layout.custom_layout, this)
        val binding = CustomLayoutBinding.bind(view)
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
        viewModel.nameLiveData.observe(this) {
            binding.tvName.text = it
        }
        viewModel.loadName()
    }

    override fun getViewModelStore(): ViewModelStore {
        return viewModelStore
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        viewModelStore.clear()
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
    }

    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)
        if (visibility == VISIBLE) {
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        } else if (visibility == GONE || visibility == INVISIBLE) {
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        }
    }
}