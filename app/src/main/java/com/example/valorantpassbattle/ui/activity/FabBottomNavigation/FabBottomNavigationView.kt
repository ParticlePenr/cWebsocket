package FabBottomNavigationView

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.animation.doOnEnd
import com.example.valorantpassbattle.R
import com.google.android.material.bottomappbar.BottomAppBarTopEdgeTreatment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel

@SuppressLint("RestrictedApi")
class FabBottomNavigationView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BottomNavigationView(context, attrs, defStyleAttr) {

//    https://medium.com/@vadim.zhukov/easy-way-to-make-curved-bottomnavigationview-with-floatingactionbutton-12b979009e64
    private var topCurvedEdgeTreatment: BottomAppBarTopEdgeTreatment
    private var materialShapeDrawable: MaterialShapeDrawable
    private var fabSize = 0F
    var fabCradleMargin = 0F
    var fabCradleRoundedCornerRadius = 0F
    var cradleVerticalOffset = 0F

    init {
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.FabBottomNavigationView, 0, 0)
        fabSize = ta.getDimension(R.styleable.FabBottomNavigationView_fab_size, 0F)
        fabCradleMargin = ta.getDimension(R.styleable.FabBottomNavigationView_fab_cradle_margin, 0F)
        fabCradleRoundedCornerRadius =
            ta.getDimension(R.styleable.FabBottomNavigationView_fab_cradle_rounded_corner_radius, 0F)
        cradleVerticalOffset = ta.getDimension(R.styleable.FabBottomNavigationView_cradle_vertical_offset, 0F)

        topCurvedEdgeTreatment = BottomAppBarTopEdgeTreatment(fabCradleMargin, fabCradleRoundedCornerRadius, cradleVerticalOffset).apply {
            fabDiameter = fabSize
        }

        val shapeAppearanceModel = ShapeAppearanceModel.Builder()
            .setTopEdge(topCurvedEdgeTreatment)
            .build()

        materialShapeDrawable = MaterialShapeDrawable(shapeAppearanceModel).apply {
            setTint(context.getColorFromAttr(R.attr.colorPrimaryVariant))
            paintStyle = Paint.Style.FILL_AND_STROKE
        }

        background = materialShapeDrawable
    }

    @ColorInt
    fun Context.getColorFromAttr(
        @AttrRes attrColor: Int,
        typedValue: TypedValue = TypedValue(),
        resolveRefs: Boolean = true
    ): Int {
        theme.resolveAttribute(attrColor, typedValue, resolveRefs)
        return typedValue.data
    }

    fun transform(fab: FloatingActionButton, status: Boolean) {
        if (!status) {
            fab.hide(object : FloatingActionButton.OnVisibilityChangedListener() {
                override fun onHidden(fab: FloatingActionButton?) {
                    super.onHidden(fab)
                    ValueAnimator.ofFloat(materialShapeDrawable.interpolation, 0F).apply {
                        addUpdateListener {
                            materialShapeDrawable.interpolation = it.animatedValue as Float
                        }
                        start()
                    }
                }
            })
        } else {
            ValueAnimator.ofFloat(materialShapeDrawable.interpolation, 1F).apply {
                addUpdateListener { materialShapeDrawable.interpolation = it.animatedValue as Float }
                doOnEnd { fab.show() }
                start()
            }
        }
    }
}
