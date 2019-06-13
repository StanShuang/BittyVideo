package view.recycleview

/**
 * Created by Stan
 * on 2019/6/13.
 * desc: 多布局条目类型
 */
interface MultipleType<in T> {
    fun getLayoutId(item: T,position: Int): Int
}