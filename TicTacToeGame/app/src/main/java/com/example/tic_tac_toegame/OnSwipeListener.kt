package com.example.tic_tac_toegame

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

import android.content.*
import android.content.res.*
import android.view.*

open class OnSwipeListener
/**
 * Constructs a new listener for the given context (activity or fragment).
 */
    (context: Context) : View.OnTouchListener {
    private var dragHorizontal = false
    private var dragVertical = false
    private var dragSnapBack = true //This defines that the widget that you swipe will return to its initial positoin after the animation
    private var animated = true
    private var exitScreenOnSwipe = false
    private var animationDelay: Long = 500
    private var dragSnapThreshold = 10f
    private var swipeDistanceThreshold = 50f
    private var swipeVelocityThreshold = 50f
    private var dragPrevX: Float = 0.toFloat()
    private var dragPrevY: Float = 0.toFloat()
    private var gestureDetector: GestureDetector? = null
    private var swiper: Impl? = null
    private var draggedView: View? = null

    init {
        gestureDetector = GestureDetector(context, GestureListener())
        if (context is Impl) {
            swiper = context
        }
    }

    /**
     * You can override this method if you want to subclass OnSwipeListener.
     * Called when the user swipes the view to the left.
     */
    open fun onSwipeLeft(distance: Float) {
        if (swiper != null) {
            swiper!!.onSwipeLeft(distance)
        }
    }

    /**
     * You can override this method if you want to subclass OnSwipeListener.
     * Called when the user swipes the view to the right.
     */
    open fun onSwipeRight(distance: Float) {
        if (swiper != null) {
            swiper!!.onSwipeRight(distance)
        }
    }

    /**
     * You can override this method if you want to subclass OnSwipeListener.
     * Called when the user swipes the view upward.
     */
    open fun onSwipeUp(distance: Float) {
        if (swiper != null) {
            swiper!!.onSwipeUp(distance)
        }
    }

    /**
     * You can override this method if you want to subclass OnSwipeListener.
     * Called when the user swipes the view downward.
     */
    open fun onSwipeDown(distance: Float) {
        if (swiper != null) {
            swiper!!.onSwipeDown(distance)
        }
    }

    /**
     * Internal method used to implement mouse touch events.
     * Not to be called directly by clients.
     */
    override fun onTouch(view: View?, event: MotionEvent): Boolean {
        if (view != null) {
            draggedView = view
        }

        val gesture = gestureDetector!!.onTouchEvent(event)

        val action = event.action
        if (dragHorizontal || dragVertical) {
            if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN) {
                // initialViewX = view.getX();
                // initialViewY = view.getY();
            } else if (action == MotionEvent.ACTION_MOVE) {
                val dragCurrX = event.rawX
                val dragCurrY = event.rawY
                if (dragHorizontal) {
                    view!!.translationX = view.translationX + dragCurrX - dragPrevX
                }
                if (dragVertical) {
                    view!!.translationY = view.translationY + dragCurrY - dragPrevY
                }
            } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL
                || action == MotionEvent.ACTION_POINTER_UP
            ) {
                if (dragSnapBack) {
                    val dx = event.rawX - dragPrevX
                    val dy = event.rawY - dragPrevY

                    val shouldDoX = Math.abs(dx) <= dragSnapThreshold || dragSnapThreshold <= 0
                    val shouldDoY = Math.abs(dy) <= dragSnapThreshold || dragSnapThreshold <= 0

                    if (animated) {
                        val anim = view!!.animate()
                        if (shouldDoX) {
                            anim.translationX(0f)
                        }
                        if (shouldDoY) {
                            anim.translationY(0f)
                        }
                        anim.duration = animationDelay
                        anim.start()
                    } else {
                        if (shouldDoX) {
                            view!!.translationX = 0f
                        }
                        if (shouldDoY) {
                            view!!.translationY = 0f
                        }
                    }
                }
            }
            dragPrevX = event.rawX
            dragPrevY = event.rawY
        }

        return gesture
    }

    /**
     * Sets the number of pixels before the listener considers the user to have swiped.
     */
    fun setDistanceThreshold(px: Float): OnSwipeListener {
        swipeDistanceThreshold = px
        return this
    }

    /**
     * Sets the rate of finger speed before the listener considers the user to have swiped.
     */
    fun setVelocityThreshold(px: Float): OnSwipeListener {
        swipeVelocityThreshold = px
        return this
    }

    /**
     * Sets the number of pixels in which the view will "snap back" if dragged.
     */
    fun setDragSnapThreshold(px: Float): OnSwipeListener {
        dragSnapThreshold = px
        if (dragSnapThreshold > 0) {
            setDragSnapBack(true)
        }
        return this
    }

    /**
     * Sets the number of milliseconds long that each drag/snap animation will take.
     */
    fun setAnimationDelay(ms: Long): OnSwipeListener {
        animationDelay = ms
        setAnimated(animationDelay > 0)
        return this
    }

    /**
     * Sets whether the view should slide itself off the screen once it has been swiped.
     */
    fun setExitScreenOnSwipe(exit: Boolean): OnSwipeListener {
        exitScreenOnSwipe = exit
        return this
    }

    /**
     * Sets whether the view should track the user's finger as it is dragged horizontally.
     */
    fun setDragHorizontal(drag: Boolean): OnSwipeListener {
        dragHorizontal = drag
        return this
    }

    /**
     * Sets whether the view should track the user's finger as it is dragged vertically.
     */
    fun setDragVertical(drag: Boolean): OnSwipeListener {
        dragVertical = drag
        return this
    }

    /**
     * Sets whether the view should snap back into position when the user stops dragging it.
     */
    fun setDragSnapBack(snap: Boolean): OnSwipeListener {
        dragSnapBack = snap
        return this
    }

    /**
     * Sets whether the view should animate itself when it snaps back or slides off screen.
     */
    fun setAnimated(anim: Boolean): OnSwipeListener {
        animated = anim
        return this
    }

    /*
     * Internal class to implement finger gesture tracking.
     */
    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onFling(e1: MotionEvent, e2: MotionEvent, vx: Float, vy: Float): Boolean {
            val dx = e2.rawX - e1.rawX
            val dy = e2.rawY - e1.rawY

            val config = draggedView!!.context.applicationContext.resources.configuration
            val screenWidth = config.screenWidthDp
            val screenHeight = config.screenHeightDp

            if (Math.abs(dx) > Math.abs(dy)
                && Math.abs(dx) > swipeDistanceThreshold
                && Math.abs(vx) > swipeVelocityThreshold
            ) {
                if (dx > 0) {
                    onSwipeRight(dx)
                    dragEdgeHelper((screenWidth * 2).toFloat(), true, 0f, false)
                } else {
                    onSwipeLeft(-dx)
                    dragEdgeHelper((-screenWidth).toFloat(), true, 0f, false)
                }
                return true
            } else if (Math.abs(dy) > Math.abs(dx)
                && Math.abs(dy) > swipeDistanceThreshold
                && Math.abs(vy) > swipeVelocityThreshold
            ) {
                if (dy > 0) {
                    onSwipeDown(dy)
                    dragEdgeHelper(0f, false, (screenHeight * 2).toFloat(), true)
                } else {
                    onSwipeUp(-dy)
                    dragEdgeHelper(0f, false, (-screenHeight).toFloat(), true)
                }
                return true
            }

            return false
        }

        private fun dragEdgeHelper(tx: Float, useTX: Boolean, ty: Float, useTY: Boolean) {
            if (exitScreenOnSwipe && draggedView != null) {
                if (animated) {
                    val anim = draggedView!!.animate()
                        .setDuration(animationDelay)
                    if (useTX) {
                        anim.translationX(tx)
                    }
                    if (useTY) {
                        anim.translationY(ty)
                    }
                    anim.start()
                } else {
                    draggedView!!.visibility = View.INVISIBLE
                }
            }
        }
    }

    /**
     * Interface that can be implemented by your activity/fragment to make it
     * possible to put the onSwipe___ methods directly in your activity class.
     */
    interface Impl {
        fun onSwipeLeft(distance: Float)
        fun onSwipeRight(distance: Float)
        fun onSwipeUp(distance: Float)
        fun onSwipeDown(distance: Float)
    }
}
