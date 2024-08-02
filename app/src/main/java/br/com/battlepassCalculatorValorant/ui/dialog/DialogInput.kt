package br.com.battlepassCalculatorValorant.ui.dialog

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import br.com.battlepassCalculatorValorant.BuildConfig
import br.com.battlepassCalculatorValorant.R
import br.com.battlepassCalculatorValorant.model.Historic.UserInputsTier
import br.com.battlepassCalculatorValorant.model.PassBattle.Tier
import br.com.battlepassCalculatorValorant.model.SingletonPassBattle.ManagerProperties
import br.com.battlepassCalculatorValorant.ui.Advertisement.Advertisement
import br.com.battlepassCalculatorValorant.ui.notification.NotificationChannel
import br.com.battlepassCalculatorValorant.ui.notification.NotificationReceiver
import com.google.android.gms.ads.InterstitialAd
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.dialog_tierinput.view.*
import kotlinx.android.synthetic.main.dialog_title.view.*


@Suppress("CAST_NEVER_SUCCEEDS")
class DialogInput(context: Context) : AlertDialog(context) {

    private val properties = ManagerProperties.getInstance(context)

    private var mInterstitialAd: InterstitialAd
    private var adv: Advertisement
    var tvTierIndex: TextInputEditText
    var tvTierExpMissing: TextInputEditText
    var mDialogView: View
    var builder: Builder
    lateinit var dialog: AlertDialog

    init {
        val inflater = this.layoutInflater

        val titleView: View = inflater.inflate(R.layout.dialog_title, null)
        titleView.title.text = context.getString(R.string.insira_seus_dados)

        mDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_tierinput, null)

        tvTierIndex = mDialogView.tierinput_dialog_et_level_current
        tvTierExpMissing = mDialogView.tierinput_dialog_et_exp_missing

        builder = Builder(context).setView(mDialogView).setCustomTitle(titleView)

        adv = Advertisement(context)
        mInterstitialAd = adv.createInterstitial()
        NotificationChannel(context).create()
    }

    override fun show() {
        dialog = builder.show()
        setOnClickListener()
    }

    fun setOnClickListener() {
        mDialogView.tierinput_dialog_btn_save.setOnClickListener {
            val tierInputCurrent =
                if (properties.historic.isEmpty()) null else properties.historic.last()
            val tierCurrent = properties.passBattle.getTier(tierInputCurrent?.tierCurrent ?: 1)
            if (validadeTierIndex(tvTierIndex, tierCurrent?.index ?: 0)) {
                val tierInput =
                    properties.passBattle.getTier(tvTierIndex.text.toString().toInt())!!
                if (validadeTierExpMissing(tvTierExpMissing, tierInput)) {
                    val tier = tvTierIndex.text.toString().toInt()
                    val expMissing = tvTierExpMissing.text.toString().toInt()
                    val inputUser = UserInputsTier(tier, expMissing)
                    properties.historic.create(inputUser)
                    createNotification()
                    dialog.dismiss()
                    launcherAdMob()
                }
            }
        }
        mDialogView.tierinput_dialog_btn_cancel.setOnClickListener {
            dialog.dismiss()
        }
    }

    fun createNotification() {
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
        val timeAtButtonClick = System.currentTimeMillis()
        val day = 1000 * 60 * 60 * 24
        val gameDuration = 1000 * 60 * 50
        val duration = if (BuildConfig.DEBUG) {
            (1000 * 15).toLong()
        } else {
            (day - gameDuration).toLong()
        }

        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            timeAtButtonClick + duration,
            pendingIntent
        )
    }

    fun launcherAdMob() {
        if (mInterstitialAd.isLoaded) {
            mInterstitialAd.show()
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.")
        }
    }

    fun validadeTierIndex(tv: TextView, index: Int): Boolean {
        val tierStr = tv.text.toString()
        val ultimoTier =
            if (properties.historic.isEmpty()) 0 else properties.historic.last().tierCurrent
        if (tierStr == "") tv.error = "Insira um tier!"
        if (tierStr.isNotEmpty()) {
            if (tierStr.length <= 3) {
                val tierInt = tierStr.toInt()
                if ((tierInt < index) or (tierInt > 50)) tv.error =
                    "Insira um tier entre ${index} e 50!"
                if (tierInt < ultimoTier) tv.error = "Insira um tier maior que ${ultimoTier}!"
            } else {
                tv.error = "Insira um tier menor que 50!"
            }
        }

        return tv.error == null
    }

    fun validadeTierExpMissing(tv: TextView, tier: Tier): Boolean {
        val tierStr = tv.text.toString()
        if (tierStr == "") tv.error = "Insira o EXP faltando!"
        if (tierStr.isNotEmpty()) {
            if (tierStr.length <= 5) {
                val tierInt = tierStr.toInt()

                var ultimoXp = tier.expMissing
                val ultimoTier =
                    if (properties.historic.isEmpty()) 0 else properties.historic.last().tierCurrent
                if (tier.index == ultimoTier) {
                    ultimoXp =
                        if (properties.historic.isEmpty()) 0 else properties.historic.last().tierExpMissing
                }
                if ((tierInt < 0) or (tierInt > ultimoXp)) tv.error =
                    "Insira um EXP entre 0 e ${ultimoXp}!"
            } else {
                tv.error = "Insira um EXP menor ou igual a ${tier.expMissing}!"
            }
        }
        return tv.error == null
    }
}