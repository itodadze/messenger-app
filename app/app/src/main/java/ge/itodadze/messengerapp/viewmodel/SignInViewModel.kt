package ge.itodadze.messengerapp.viewmodel

import androidx.lifecycle.*
import ge.itodadze.messengerapp.domain.repository.UsersFirebaseRepository
import ge.itodadze.messengerapp.viewmodel.models.User
import ge.itodadze.messengerapp.domain.repository.UsersRepository
import ge.itodadze.messengerapp.viewmodel.callback.SignInCallbackHandler
import ge.itodadze.messengerapp.viewmodel.listener.CallbackListener
import kotlinx.coroutines.launch

class SignInViewModel(private val usersRepository: UsersRepository): ViewModel() {

    private val _failure = MutableLiveData<String>()
    val failure: LiveData<String>
        get() = _failure

    private val _signedInNickname = MutableLiveData<String>()
    val signedInNickname: LiveData<String>
        get() = _signedInNickname

    fun trySignIn(nickname: String?, passwordHash: String?) {
        usersRepository.get(nickname,
            SignInCallbackHandler(
                User(nickname=nickname, passwordHash=passwordHash),
                object : CallbackListener {
                    override fun onSuccess() {
                        viewModelScope.launch{
                            _signedInNickname.value = nickname
                        }
                    }

                    override fun onFailure(message: String?) {
                        viewModelScope.launch{
                            _failure.value = message
                        }
                    }
                })
        )
    }

    companion object {
        fun getSignInViewModelFactory(): SignInViewModelFactory {
            return SignInViewModelFactory()
        }
    }

}

@Suppress("UNCHECKED_CAST")
class SignInViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SignInViewModel(UsersFirebaseRepository()) as T
    }
}