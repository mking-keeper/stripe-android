package com.stripe.android.paymentsheet

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.view.isVisible
import com.stripe.android.R
import com.stripe.android.databinding.DefaultPrimaryButtonBinding
import com.stripe.android.paymentsheet.model.ViewState
import com.stripe.android.paymentsheet.ui.PrimaryButton
import com.stripe.android.paymentsheet.ui.PrimaryButtonAnimator

internal open class DefaultPrimaryButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : PrimaryButton(context, attrs, defStyleAttr) {
    private var viewState: ViewState? = null
    private val animator = PrimaryButtonAnimator(context)

    internal val viewBinding = DefaultPrimaryButtonBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    private val confirmedIcon = viewBinding.confirmedIcon

    init {
        setBackgroundResource(R.drawable.stripe_paymentsheet_buy_button_default_background)

        isClickable = true
        isEnabled = false
    }

    fun setLabelText(text: String){
        viewBinding.label.text = text
    }

    fun setReady(){
        viewBinding.confirmingIcon.isVisible = false
    }

    fun setConfirm(){
        viewBinding.lockIcon.isVisible = false
        viewBinding.confirmingIcon.isVisible = true

        viewBinding.label.text = resources.getString(
            R.string.stripe_paymentsheet_pay_button_processing
        )
    }

    fun setCompleted(onAnimationEnd: () -> Unit){

        setBackgroundResource(R.drawable.stripe_paymentsheet_buy_button_confirmed_background)

        animator.fadeOut(viewBinding.label)
        animator.fadeOut(viewBinding.confirmingIcon)

        animator.fadeIn(confirmedIcon, width) {
            onAnimationEnd()
        }
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        setLockVisible(enabled)
        updateAlpha()
    }

    fun updateState(viewState: ViewState?) {
        this.viewState = viewState
        updateAlpha()
    }

    private fun updateAlpha() {
        if ((viewState == null || viewState!!.isReady()) && !isEnabled) {
            setLabelAlphaLow()
        } else {
            setLabelAlphaHigh()
        }
    }

    private fun setLabelAlphaLow() {
        viewBinding.label.alpha = 0.5f
    }

    private fun setLabelAlphaHigh() {
        viewBinding.label.alpha = 1.0f
    }

    private fun setLockVisible(visible: Boolean){
        viewBinding.lockIcon.isVisible = visible
    }

}