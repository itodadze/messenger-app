package ge.itodadze.messengerapp.domain.repository

import com.google.firebase.database.ValueEventListener
import ge.itodadze.messengerapp.viewmodel.callback.CallbackHandler
import ge.itodadze.messengerapp.viewmodel.models.Chat
import ge.itodadze.messengerapp.viewmodel.models.Message
import ge.itodadze.messengerapp.viewmodel.models.UserChats

interface ChatsRepository {
    fun getUsersChats(user_id: String?, handler: CallbackHandler<UserChats>?)
    fun getChat(chat_id: String?, handler: CallbackHandler<Chat>?)
    fun addChat(chat: Chat?, first_id: String?, second_id: String?, handler: CallbackHandler<Chat>?)
    fun addMessage(chat_id: String?, message: Message?, handler: CallbackHandler<Message>?)
    fun listenToChat(chat_id: String?, handler: CallbackHandler<Chat>?): ValueEventListener?
    fun stopListenToChat(chat_id: String?, valueEventListener: ValueEventListener?, handler: CallbackHandler<Chat>?)
    fun listenToUserChats(userId: String?, handler: CallbackHandler<UserChats>?): ValueEventListener?
    fun stopListenToUserChats(userId: String?, valueEventListener: ValueEventListener?, handler: CallbackHandler<UserChats>?)
}