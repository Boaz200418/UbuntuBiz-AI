package africa.ubuntubiz.ai.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.compose.runtime.*
import africa.ubuntubiz.ai.data.LocalRepository
import africa.ubuntubiz.ai.domain.*

class UbuntuViewModel(application: Application): AndroidViewModel(application) {
    private val repository = LocalRepository(application)
    var profile by mutableStateOf(repository.loadProfile()); private set
    var story by mutableStateOf(repository.loadSections("story")); private set
    var document by mutableStateOf(repository.loadSections("document")); private set
    var message by mutableStateOf<String?>(null)
    var error by mutableStateOf<String?>(null)
    var money by mutableStateOf(MoneyInputs())
    fun edit(p: Entrepreneur) { profile = p; message = null; error = null }
    fun save() { if(profile.name.isBlank()) { error="Please enter your name before saving."; return }; if(repository.saveProfile(profile)) { message="Profile saved on this device."; error=null } else error="Could not save. Please try again." }
    fun transform() {
        error = profile.validation(); if(error != null) return
        val generated = LocalGenerator.story(profile)
        if(repository.saveProfile(profile) && repository.saveSections("story", generated)) { story=generated; message="Draft saved on this device. Review before sharing." } else error="Could not save your draft. Please try again."
    }
    fun generate(type: String) {
        runCatching { LocalGenerator.document(type,profile,money) }.fold(onSuccess = { sections ->
            if(repository.saveProfile(profile) && repository.saveSections("document",sections)) { document=sections; error=null; message="Document draft saved on this device." } else error="Could not save the document. Please try again."
        },onFailure = { error=it.message ?: "Check your information and try again." })
    }
    fun deleteAll() { if(repository.clear()) { profile=Entrepreneur(); story=emptyList(); document=emptyList(); money=MoneyInputs(); error=null; message="Local profile and generated drafts deleted." } else error="Could not delete local data. Please try again." }
}
