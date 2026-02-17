package ru.otus.cryptosample.coins.feature.adapter

import android.animation.Animator
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView

class ItemAnimator : DefaultItemAnimator() {

    override fun animateAdd(holder: RecyclerView.ViewHolder): Boolean {
        with(holder.itemView) {
            animate().cancel()
            reset()

            val realWidth = if (width==0) measuredWidth else width

            alpha = 0f
            translationX = -1f * realWidth
            scaleX = 0.9f
            scaleY = 0.9f

            animate().alpha(1f)
                .translationX(0f)
                .scaleY(1f)
                .scaleX(1f)
                .setDuration(300)
                .setInterpolator(LinearInterpolator())
                .withStartAction {
                    dispatchAddStarting(holder)
                }
                .withEndAction {
                    reset()
                    dispatchAddFinished(holder)
                }
                .setListener(getListener(holder))
                .start()

        }

        return true
    }

    override fun animateRemove(holder: RecyclerView.ViewHolder): Boolean {

        with(holder.itemView) {
            animate().cancel()
            reset()

            animate().alpha(0f)
                .translationX(-1f * width)
                .scaleY(0.9f)
                .scaleX(0.9f)
                .setDuration(300)
                .setInterpolator(LinearInterpolator())
                .withStartAction { dispatchRemoveStarting(holder) }
                .withEndAction {
                    reset()
                    dispatchRemoveFinished(holder)
                }
                .setListener(getListener(holder))
                .start()
        }

        return true
    }

    private fun getListener(holder: RecyclerView.ViewHolder) = object: Animator.AnimatorListener{
        override fun onAnimationCancel(animation: Animator) {
            holder.setIsRecyclable(true)
        }

        override fun onAnimationEnd(animation: Animator) {
            holder.setIsRecyclable(true)
        }

        override fun onAnimationRepeat(animation: Animator) {
            holder.setIsRecyclable(false)
        }

        override fun onAnimationStart(animation: Animator) {
            holder.setIsRecyclable(false)
        }
    }

    private fun View.reset() {
        alpha = 1f
        translationX = 0f
        scaleY = 1f
        scaleX = 1f
    }
}