package co.aspirasoft.sams.utils

import co.aspirasoft.sams.MyApplication
import co.aspirasoft.sams.model.Student
import co.aspirasoft.sams.model.Teacher
import com.google.firebase.auth.ActionCodeSettings
import java.net.URLEncoder

object DynamicLinksUtils {

    private const val domain = "https://cygnus.page.link"
    private const val androidPackageName = "co.aspirasoft.sams"
    private const val iOSBundleId = "co.aspirasoft.sams"

    const val ACTION_REGISTRATION = "/finishSignUp"

    private fun encode(param: String): String {
        return URLEncoder.encode(param, "utf-8")
    }

    /**
     * Returns [ActionCodeSettings] to register a new [Teacher] with [referralCode].
     */
    fun createSignUpActionForTeacher(referralCode: String): ActionCodeSettings {
        val url = "$domain$ACTION_REGISTRATION?" +
                "${MyApplication.PARAM_REFERRAL_CODE}=${encode(referralCode)}&" +
                "${MyApplication.PARAM_ACCOUNT_TYPE}=${encode(Teacher::class.simpleName ?: "Teacher")}"

        return ActionCodeSettings.newBuilder()
                .setUrl(url)
                .setHandleCodeInApp(true)
                .setIOSBundleId(iOSBundleId)
                .setAndroidPackageName(androidPackageName, true, "1")
                .build()
    }

    /**
     * Returns [ActionCodeSettings] to register a new [Student] with [referralCode] and [rollNo].
     */
    fun createSignUpActionForStudent(referralCode: String, rollNo: String, classId: String): ActionCodeSettings {
        val url = "$domain$ACTION_REGISTRATION?" +
                "${MyApplication.PARAM_REFERRAL_CODE}=${encode(referralCode)}&" +
                "${MyApplication.PARAM_ACCOUNT_TYPE}=${encode(Student::class.simpleName ?: "Student")}&" +
                "${MyApplication.PARAM_STUDENT_CLASS_ID}=${encode(classId)}&" +
                "${MyApplication.PARAM_STUDENT_ROLL_NO}=${encode(rollNo)}"

        return ActionCodeSettings.newBuilder()
                .setUrl(url)
                .setHandleCodeInApp(true)
                .setIOSBundleId(iOSBundleId)
                .setAndroidPackageName(androidPackageName, true, "1")
                .build()
    }

}