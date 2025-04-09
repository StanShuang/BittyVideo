package com.stan.video.bittyvideo.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.classic.common.MultipleStatusView
import com.stan.video.bittyvideo.app.MyApplication
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

/**
 * Created by Stan
 * on 2019/5/31.
 * activity基类
 */
abstract class BaseActivity: AppCompatActivity(),EasyPermissions.PermissionCallbacks {
    /**
     * 多种状态view的切换
     */
    protected var mLayoutStatusView: MultipleStatusView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId())
        initData()
        initView()
        start()
        initListener()
    }

    /**
     * 加载布局
     */
    abstract fun layoutId(): Int
    /**
     * 初始化数据
     */
    abstract fun initData()
    /**
     * 初始化view
     */
    abstract fun initView()
    /**
     * 开始请求
     */
    abstract fun start()

    /**
     * MultipleStatusView的监听事件
     */
    private fun initListener(){
        mLayoutStatusView?.setOnClickListener(mRetryClickListener)
    }
    open val mRetryClickListener: View.OnClickListener = View.OnClickListener {
        start()
    }

    /**
     * 打开软件盘
     */
    fun openKeyBord(mEditText: EditText,mContext: Context){
        val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(mEditText,InputMethodManager.RESULT_SHOWN)
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY)
    }
    /**
     * 关闭软件盘
     */
    fun closeKeyBord(mEditText: EditText,mContext: Context){
        val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mEditText.windowToken,0)
    }

    /**
     * 重写要申请权限的Activity或者Fragment的onRequestPermissionsResult()方法，
     * 在里面调用EasyPermissions.onRequestPermissionsResult()，实现回调。
     *
     * @param requestCode  权限请求的识别码
     * @param permissions  申请的权限
     * @param grantResults 授权结果
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this)
    }
    /**
     * 当权限被成功申请的时候执行回调
     *
     * @param requestCode 权限请求的识别码
     * @param perms       申请的权限的名字
     */
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Log.i("EasyPermissions","获取成功的权限$perms")
    }
    /**
     * 当权限申请失败的时候执行的回调
     *
     * @param requestCode 权限请求的识别码
     * @param perms       申请的权限的名字
     */
    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        //处理权限名字字符串
        val stringBuffer = StringBuffer()
        for(str in perms){
            stringBuffer.append(str)
            stringBuffer.append("\n")
        }
        stringBuffer.replace(stringBuffer.length-2,stringBuffer.length,"")
        //用户点击拒绝并不在询问是调用
        if (EasyPermissions.somePermissionPermanentlyDenied(this,perms)){
            Toast.makeText(this, "已拒绝权限" + stringBuffer + "并不再询问", Toast.LENGTH_SHORT).show()
            AppSettingsDialog.Builder(this)
                    .setRationale("此功能需要" + stringBuffer + "权限，否则无法正常使用，是否打开设置")
                    .setPositiveButton("好")
                    .setNegativeButton("不好")
                    .build().show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}