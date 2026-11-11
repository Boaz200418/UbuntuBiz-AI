package africa.ubuntubiz.ai.data

import android.content.Context
import africa.ubuntubiz.ai.domain.*
import org.json.JSONArray
import org.json.JSONObject

/** Device-private demo storage. No network, account, analytics or publishing. */
class LocalRepository(context: Context) {
    private val prefs = context.getSharedPreferences("ubuntubiz.private.v1", Context.MODE_PRIVATE)
    private fun json(key: String) = runCatching { JSONObject(prefs.getString(key, "{}") ?: "{}") }.getOrDefault(JSONObject())
    fun loadProfile(): Entrepreneur = json("profile").let { o -> Entrepreneur(o.optString("name"),o.optString("business"),o.optString("country"),o.optString("category"),o.optString("story"),o.optString("products"),o.optString("goals"),o.optString("skills")) }
    fun saveProfile(p: Entrepreneur): Boolean {
        val o=JSONObject().put("name",p.name).put("business",p.business).put("country",p.country).put("category",p.category).put("story",p.story).put("products",p.products).put("goals",p.goals).put("skills",p.skills)
        return prefs.edit().putString("profile",o.toString()).commit()
    }
    fun loadSections(key: String): List<ContentSection> = runCatching {
        val a = JSONArray(prefs.getString(key,"[]")); (0 until a.length()).map { a.getJSONObject(it).let { o -> ContentSection(o.getString("title"),o.getString("body")) } }
    }.getOrDefault(emptyList())
    fun saveSections(key: String, s: List<ContentSection>): Boolean {
        val a = JSONArray(); s.forEach { a.put(JSONObject().put("title",it.title).put("body",it.body)) }
        return prefs.edit().putString(key,a.toString()).commit()
    }
    fun clear(): Boolean = prefs.edit().clear().commit()
}
