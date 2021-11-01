package com.wj.jd

import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloader
import com.wj.jd.activity.MuchCkActivity
import com.wj.jd.activity.MyWebActivity
import com.wj.jd.activity.SettingActivity
import com.wj.jd.bean.SimpleFileDownloadListener
import com.wj.jd.bean.VersionBean
import com.wj.jd.dialog.NewStyleDialog
import com.wj.jd.util.*
import com.zhy.base.fileprovider.FileProvider7
import java.io.File

class MainActivity : BaseActivity() {
    private lateinit var notificationUpdateReceiver: NotificationUpdateReceiver
    private lateinit var notificationUpdateReceiver1: NotificationUpdateReceiver1
    private lateinit var notificationUpdateReceiver2: NotificationUpdateReceiver2

    override fun setLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        setTitle("京豆")
        back?.visibility = View.GONE
    }

    override fun initData() {
        checkAppUpdate()
        initNotification()
        startUpdateService()
    }

    private fun startUpdateService() {
        /*
        * app进入重新启动更新数据后台服务
        * */
        if ("1" != CacheUtil.getString("startUpdateService")) {
            UpdateTask.updateAll()
        }
    }

    private fun initNotification() {
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.scott.sayhi")
        notificationUpdateReceiver = NotificationUpdateReceiver()
        registerReceiver(notificationUpdateReceiver, intentFilter)

        val intentFilter1 = IntentFilter()
        intentFilter1.addAction("com.scott.sayhi1")
        notificationUpdateReceiver1 = NotificationUpdateReceiver1()
        registerReceiver(notificationUpdateReceiver1, intentFilter1)

        val intentFilter2 = IntentFilter()
        intentFilter2.addAction("com.scott.sayhi2")
        notificationUpdateReceiver2 = NotificationUpdateReceiver2()
        registerReceiver(notificationUpdateReceiver2, intentFilter2)
    }


    private fun checkAppUpdate() {
        HttpUtil.getAppVer(object : StringCallBack {
            override fun onSuccess(result: String) {
                try {
                    var gson = Gson()
                    val versionBean = gson.fromJson(result, VersionBean::class.java)
                    if (DeviceUtil.getAppVersionName().equals(versionBean.release)) {
                        Toast.makeText(this@MainActivity, "当前已是最新版本", Toast.LENGTH_SHORT).show()
                    } else {
                        if ("1" == versionBean.isUpdate) {
                            createDialog("版本更新", versionBean.content, "更新", object : NewStyleDialog.OnRightClickListener {
                                override fun rightClick() {
                                    downLoadApk(versionBean.content_url)
                                }
                            })
                        } else {
                            createDialog("版本更新", versionBean.content, "取消", "更新", object : NewStyleDialog.OnLeftClickListener {
                                override fun leftClick() {
                                    disMissDialog()
                                }
                            }, object : NewStyleDialog.OnRightClickListener {
                                override fun rightClick() {
                                    disMissDialog()
                                    downLoadApk(versionBean.content_url)
                                }
                            })
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFail() {
            }

        })
    }

    //context.getExternalFilesDir()
    private fun downLoadApk(contentUrl: String?) {
        downLoad(contentUrl)
    }

    private lateinit var pd: ProgressDialog

    ///storage/emulated/0/Android/data/<包名>/files
    ///storage/emulated/0/Android/data/com.wj.jd/files
    private fun downLoad(contentUrl: String?) {
        if (TextUtils.isEmpty(contentUrl)) return

        pd = ProgressDialog(this)
        pd.setTitle("提示")
        pd.setMessage("软件版本更新中，请稍后...")
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL) //设置带进度条的

        pd.max = 100
        pd.setCancelable(false)
        pd.show()

        var pathParent = filesDir.path + "/downApk"
        var apkName = pathParent + System.currentTimeMillis() + ".apk"
        val file = File(pathParent)
        if (!file.exists()) {
            file.mkdirs()
        }

        FileDownloader.setup(this)
        FileDownloader.getImpl().create(contentUrl)
            .setPath(apkName)
            .setListener(object : SimpleFileDownloadListener() {
                override fun progress(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {
                    val per = soFarBytes / (totalBytes / 100)
                    pd.progress = per
                }

                override fun completed(task: BaseDownloadTask) {
                    pd.dismiss()
                    val file = File(apkName)
                    installApk(file)
                }
            }).start()

    }

    private fun installApk(file: File) {
        val intent = Intent(Intent.ACTION_VIEW)
        FileProvider7.setIntentDataAndType(this, intent, "application/vnd.android.package-archive", file, true)
        startActivity(intent)
    }

    override fun setEvent() {
        noCK.setOnClickListener {
            val intent = Intent(this, MyWebActivity::class.java)
            intent.putExtra("url","http://1.117.138.78:5700/")
            intent.putExtra("title","青龙自动挂京豆")
            startActivity(intent)
        }

        setting.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }

        muchCK.setOnClickListener {
            val intent = Intent(this, MuchCkActivity::class.java)
            startActivity(intent)
        }

        loginJd.setOnClickListener {
            val intent = Intent(this, MyWebActivity::class.java)
            intent.putExtra("url","https://plogin.m.jd.com/login/login")
            intent.putExtra("title","京东网页版获取CK")
            startActivity(intent)
        }

        updateCK.setOnClickListener {
            if (TextUtils.isEmpty(inputCK.text.toString())) {
                Toast.makeText(this, "CK为空，添加失败", Toast.LENGTH_SHORT).show()
            } else {
                CacheUtil.putString("ck", inputCK.text.toString())
                Toast.makeText(this, "CK添加成功", Toast.LENGTH_SHORT).show()
                inputCK.setText("")
                UpdateTask.widgetUpdateDataUtil1.updateWidget("ck")
            }
        }

        addQQGroup.setOnClickListener {
            joinQQGroup("uYo08bbpQEjTEXM24uY7N9T1W7F8jDhD")
        }

        addTGGroup.setOnClickListener {
            val intent = Intent()
            intent.action = "android.intent.action.VIEW"
            intent.data = Uri.parse("https://t.me/joinchat/VJICOAj1z2BmOGVl/")
            startActivity(intent)
        }
    }

    /****************
     *
     * 发起添加群流程。群号：不怕困难(151111614) 的 key 为： uYo08bbpQEjTEXM24uY7N9T1W7F8jDhD
     * 调用 joinQQGroup(uYo08bbpQEjTEXM24uY7N9T1W7F8jDhD) 即可发起手Q客户端申请加群 豆豆交流群。(908891563)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回false表示呼起失败
     */
    fun joinQQGroup(key: String): Boolean {
        val intent = Intent()
        intent.data = Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3D$key")
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return try {
            startActivity(intent)
            true
        } catch (e: java.lang.Exception) {
            // 未安装手Q或安装的版本不支持
            false
        }
    }

    inner class NotificationUpdateReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.i("====", "NotificationUpdateReceiver")
            UpdateTask.widgetUpdateDataUtil1.updateWidget("ck")
        }
    }

    inner class NotificationUpdateReceiver1 : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.i("====", "NotificationUpdateReceiver1")
            UpdateTask.widgetUpdateDataUtil2.updateWidget("ck1")
        }
    }

    inner class NotificationUpdateReceiver2 : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.i("====", "NotificationUpdateReceiver1")
            UpdateTask.widgetUpdateDataUtil3.updateWidget("ck2")
        }
    }
}
