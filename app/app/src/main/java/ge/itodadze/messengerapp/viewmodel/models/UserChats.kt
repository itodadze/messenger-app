package ge.itodadze.messengerapp.viewmodel.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class UserChats(val chat_ids: MutableList<ChatAndPartner>? = null, val user_id: String? = null)