package six.czh.com.gankio.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.ImageView
import six.czh.com.gankio.util.LogUtils

const val TAG = "DragImageView"

const val DEFAULT_MAX_SCALE = 5.0f
const val DEFAULT_MIDDLE_SCALE = 3.75f

class DragImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
                                              defStyleRes: Int = 0): ImageView(context, attrs, defStyleRes) {

    private lateinit var mGestureDetector: GestureDetector

    private lateinit var mScaleGestureDetector: ScaleGestureDetector

    private var mScaleType: ScaleType? = ScaleType.FIT_CENTER

    private var mBaseMatrix: Matrix = Matrix()
    private var mSuppMatrix: Matrix = Matrix()
    private var mDrawMatrix: Matrix = Matrix()
    private val mMatrixValues = FloatArray(9)

    private val mDisplayRect = RectF()
    /**
     * 系统触发的最小滑动距离
     */
    private var mTouchSlop: Int = 0

    private var isCanDrag: Boolean = false

    init {
        initView()
        initViewGesture()
    }

    private fun initView() {
        super.setScaleType(ScaleType.MATRIX)
        //需要加此监听器，否则会导致图片大小显示异常
        addOnLayoutChangeListener { p0, p1, p2, p3, p4, p5, p6, p7, p8 -> updateBaseMatrix(drawable) }
        updateBaseMatrix(drawable)

        //系统触发的最小滑动距离
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop()

        Log.d(TAG, "mTouchSlop = $mTouchSlop")
    }

    override fun setScaleType(scaleType: ScaleType?) {

        if(scaleType != mScaleType) {
            mScaleType = scaleType
            updateBaseMatrix(drawable)
        }
//
    }

    fun getScale(): Float {
        return Math.sqrt((Math.pow(getValue(mSuppMatrix, Matrix.MSCALE_X).toDouble(), 2.0).toFloat() + Math.pow(getValue(mSuppMatrix, Matrix.MSKEW_Y).toDouble(), 2.0).toFloat()).toDouble()).toFloat()
    }

    /**
     * Helper method that 'unpacks' a Matrix and returns the required value
     *
     * @param matrix     Matrix to unpack
     * @param whichValue Which value from Matrix.M* to return
     * @return returned value
     */
    private fun getValue(matrix: Matrix, whichValue: Int): Float {
        matrix.getValues(mMatrixValues)
        return mMatrixValues[whichValue]
    }

    private fun initViewGesture() {
        mGestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDoubleTap(event: MotionEvent?): Boolean {
                LogUtils.d("双击了 " + getScale())
                /**
                 * 全屏放大
                 * 1.当目前缩放比大于全屏显示状态时，缩小至原来的大小
                 * 2.当目前缩放比小于全屏显示状态时，放大至全屏大小
                 */
                val x = event!!.x
                val y = event.y
                if (getScale() < DEFAULT_MIDDLE_SCALE) {
                    mSuppMatrix.setScale(DEFAULT_MIDDLE_SCALE, DEFAULT_MIDDLE_SCALE, x, y)
                } else {
                    mSuppMatrix.setScale(1.0f, 1.0f, x, y)
                }
                checkAndDisplayMatrix()
                return true
            }

            override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
                return false
            }

//            override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
////                parent.requestDisallowInterceptTouchEvent(true)
//                return super.onScroll(e1, e2, distanceX, distanceY)
//            }

        })

        mScaleGestureDetector = ScaleGestureDetector(context, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
                return true
            }

            override fun onScale(detector: ScaleGestureDetector?): Boolean {
                LogUtils.d("缩放监听")
                var focusX = detector!!.focusX
                var focusY = detector.focusY
                mSuppMatrix.postScale(detector.scaleFactor, detector.scaleFactor, focusX, focusY)
                checkAndDisplayMatrix()
                return true
            }
        })
    }

    //获取一个指针(手指)的唯一标识符ID，在手指按下和抬起之间ID始终不变。
    val INVAILD_POINTID = -1
    var mActionPointId: Int = INVAILD_POINTID
    var mLastPointX = 0.0f
    var mLastPointY = 0.0f
    var mPointCount = 0
    override fun onTouchEvent(event: MotionEvent?): Boolean {



        if(mGestureDetector.onTouchEvent(event)) {
            return true
        }

        mScaleGestureDetector.onTouchEvent(event)


        var pointX = event!!.x
        var pointY = event.y
        var rectF = getDisplayRect(getDrawMatrix())
        var pointCount = event.pointerCount
        if(mPointCount != pointCount) {
            isCanDrag = false
            mLastPointX = pointX
            mLastPointY = pointY
        }
        mPointCount = pointCount
        when(event?.actionMasked) {
            //在多点触控中，出现这个事件说明所有手指均抬起
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                mPointCount = 0
                mActionPointId = INVAILD_POINTID
            }

            //在多点触控中，出现这个事件说明第一根手指落下
            MotionEvent.ACTION_DOWN -> {
                if((rectF?.width()!! - width) > 0.01|| (rectF.height() - height) > 0.01){
                    parent.requestDisallowInterceptTouchEvent(true)
                }

            }

            MotionEvent.ACTION_MOVE -> {
//                if(mActionPointId == INVAILD_POINTID) {
//                    return false
//                }
                var dx = pointX - mLastPointX
                var dy = pointY - mLastPointY

                if((rectF?.width()!! - width) > 0.01|| (rectF.height() - height) > 0.01){
                    parent.requestDisallowInterceptTouchEvent(true)
                }
                if(!isCanDrag) {
                    isCanDrag = isMoveAction(dx, dy)
                }

                if(isCanDrag) {
                    Log.d(TAG, " dx = $dx + dy = $dy")
                    mSuppMatrix.postTranslate(dx, dy)
                    checkAndDisplayMatrix()
                }

                mLastPointX = pointX
                mLastPointY = pointY
            }
            //有手指按下
            MotionEvent.ACTION_POINTER_DOWN -> {
            }
            //有手指抬起
            MotionEvent.ACTION_POINTER_UP -> {

            }
        }
        return true
    }


    /**
     * 图片显示矩阵
     */
    private fun getImageViewWidth(imageView: ImageView): Int {
        return imageView.width - imageView.paddingLeft - imageView.paddingRight
    }

    private fun getImageViewHeight(imageView: ImageView): Int {
        return imageView.height - imageView.paddingTop - imageView.paddingBottom
    }
    /**
     * Calculate Matrix for FIT_CENTER
     *
     * @param drawable - Drawable being displayed
     */
    private fun updateBaseMatrix(drawable: Drawable?) {
        if (drawable == null) {
            return
        }
        val viewWidth: Float = getImageViewWidth(this).toFloat()
        val viewHeight: Float = getImageViewHeight(this).toFloat()
        val drawableWidth = drawable.intrinsicWidth
        val drawableHeight = drawable.intrinsicHeight


        mBaseMatrix.reset()

        val widthScale = viewWidth / drawableWidth
        val heightScale = viewHeight / drawableHeight

        if (mScaleType == ScaleType.CENTER) {
            mBaseMatrix.postTranslate((viewWidth - drawableWidth) / 2f,
                    (viewHeight - drawableHeight) / 2f)

        } else if (mScaleType == ScaleType.CENTER_CROP) {
            val scale = Math.max(widthScale, heightScale)
            mBaseMatrix.postScale(scale, scale)
            mBaseMatrix.postTranslate((viewWidth - drawableWidth * scale) / 2f,
                    (viewHeight - drawableHeight * scale) / 2f)

        } else if (mScaleType == ScaleType.CENTER_INSIDE) {
            val scale = Math.min(1.0f, Math.min(widthScale, heightScale))
            mBaseMatrix.postScale(scale, scale)
            mBaseMatrix.postTranslate((viewWidth - drawableWidth * scale) / 2f,
                    (viewHeight - drawableHeight * scale) / 2f)

        } else {
            var mTempSrc = RectF(0f, 0f, drawableWidth.toFloat(), drawableHeight.toFloat())
            val mTempDst = RectF(0f, 0f, viewWidth, viewHeight)

//            if (mBaseRotation.toInt() % 180 != 0) {
//                mTempSrc = RectF(0f, 0f, drawableHeight.toFloat(), drawableWidth.toFloat())
//            }

            when (mScaleType) {
                ImageView.ScaleType.FIT_CENTER -> mBaseMatrix.setRectToRect(mTempSrc, mTempDst, Matrix.ScaleToFit.CENTER)

                ImageView.ScaleType.FIT_START -> mBaseMatrix.setRectToRect(mTempSrc, mTempDst, Matrix.ScaleToFit.START)

                ImageView.ScaleType.FIT_END -> mBaseMatrix.setRectToRect(mTempSrc, mTempDst, Matrix.ScaleToFit.END)

                ImageView.ScaleType.FIT_XY -> mBaseMatrix.setRectToRect(mTempSrc, mTempDst, Matrix.ScaleToFit.FILL)

                else -> {
                }
            }
        }

        resetMatrix()
    }


    /**
     * Helper method that maps the supplied Matrix to the current Drawable
     *
     * @param matrix - Matrix to map Drawable against
     * @return RectF - Displayed Rectangle
     */

    private fun getDisplayRect(matrix: Matrix): RectF? {
        val d = drawable
        if (d != null) {
            mDisplayRect.set(0f, 0f, d!!.intrinsicWidth.toFloat(),
                    d!!.intrinsicHeight.toFloat())
            matrix.mapRect(mDisplayRect)
            return mDisplayRect
        }
        return null
    }

    private fun checkMatrixBounds(): Boolean {

        val rect = getDisplayRect(getDrawMatrix()) ?: return false

        val height = rect!!.height()
        val width = rect!!.width()
        var deltaX = 0f
        var deltaY = 0f

        val viewHeight = getImageViewHeight(this)
        if (height <= viewHeight) {
            when (mScaleType) {
                ImageView.ScaleType.FIT_START -> deltaY = -rect!!.top
                ImageView.ScaleType.FIT_END -> deltaY = viewHeight.toFloat() - height - rect!!.top
                else -> deltaY = (viewHeight - height) / 2 - rect!!.top
            }
        } else if (rect!!.top > 0) {
            deltaY = -rect!!.top
        } else if (rect!!.bottom < viewHeight) {
            deltaY = viewHeight - rect!!.bottom
        }

        val viewWidth = getImageViewWidth(this)
        if (width <= viewWidth) {
            when (mScaleType) {
                ImageView.ScaleType.FIT_START -> deltaX = -rect!!.left
                ImageView.ScaleType.FIT_END -> deltaX = viewWidth.toFloat() - width - rect!!.left
                else -> deltaX = (viewWidth - width) / 2 - rect!!.left
            }
//            mScrollEdge = EDGE_BOTH
        } else if (rect!!.left > 0) {
//            mScrollEdge = EDGE_LEFT
            deltaX = -rect!!.left
        } else if (rect!!.right < viewWidth) {
            deltaX = viewWidth - rect!!.right
//            mScrollEdge = EDGE_RIGHT
        } else {
//            mScrollEdge = EDGE_NONE
        }

        // Finally actually translate the matrix
        mSuppMatrix.postTranslate(deltaX, deltaY)
        return true
    }

    /**
     * Resets the Matrix back to FIT_CENTER, and then displays its contents
     */
    private fun resetMatrix() {
        mSuppMatrix.reset()

//        setRotationBy(mBaseRotation)
//        setImageViewMatrix(getDrawMatrix())
        imageMatrix = getDrawMatrix()
        checkMatrixBounds()

    }

    private fun getDrawMatrix(): Matrix {
        mDrawMatrix.set(mBaseMatrix)
        mDrawMatrix.postConcat(mSuppMatrix)
        return mDrawMatrix
    }

    /**
     * Helper method that simply checks the Matrix, and then displays the result
     */
    private fun checkAndDisplayMatrix() {
        if (checkMatrixBounds()) {
            imageMatrix = getDrawMatrix()

        }
    }

    /**
     * 判断滑动的距离是否触发滑动的临界条件
     * @param dx
     * @param dy
     * @return
     */
    private fun isMoveAction(dx: Float, dy: Float): Boolean {
        return Math.sqrt((dx * dx + dy * dy).toDouble()) > mTouchSlop
    }

}