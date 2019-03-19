package learn

import com.aramis.library.http.ArRxVolley
import com.kymjs.rxvolley.client.HttpCallback
import com.kymjs.rxvolley.client.HttpParams
import org.json.JSONObject
import java.lang.Exception

fun main(args: Array<String>) {
    println("asdf")
    try {
        val url = ""
        val httpParams = HttpParams()
        val localObject = JSONObject()
        localObject.put("page", 1)
        localObject.put("perPage", 15)
        localObject.put("uId", "")
        val data = AesEncryptionUtil.encrypt(localObject.toString(), "625202f9149maomi", "5efd3f6060emaomi")
        localObject.put("data", data)
        httpParams.putJsonParams(localObject.toString())
        ArRxVolley.post(url, httpParams, object : HttpCallback() {
            override fun onSuccess(t: String?) {
                super.onSuccess(t)
                println("success")
                println(t)
            }

            override fun onFailure(errorNo: Int, strMsg: String?) {
                super.onFailure(errorNo, strMsg)
                println("fail errorNo:$errorNo,strMsg:$strMsg")
            }
        })
    } catch (e: Exception) {
        e.printStackTrace()
    }

}