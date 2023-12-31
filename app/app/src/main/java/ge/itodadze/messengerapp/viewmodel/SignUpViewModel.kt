package ge.itodadze.messengerapp.viewmodel

import android.content.Context
import androidx.lifecycle.*
import ge.itodadze.messengerapp.R
import ge.itodadze.messengerapp.domain.repository.UsersFirebaseRepository
import ge.itodadze.messengerapp.viewmodel.models.User
import ge.itodadze.messengerapp.domain.repository.UsersRepository
import ge.itodadze.messengerapp.viewmodel.callback.SignUpCallbackHandler
import ge.itodadze.messengerapp.viewmodel.callback.SignUpCheckExistsCallbackHandler
import ge.itodadze.messengerapp.viewmodel.listener.CallbackListener
import ge.itodadze.messengerapp.viewmodel.listener.CallbackListenerWithResult
import kotlinx.coroutines.launch

class SignUpViewModel(val logInManager: LogInManager,
                      val usersRepository: UsersRepository): ViewModel() {

    private val _failure = MutableLiveData<String>()
    val failure: LiveData<String>
        get() = _failure

    private val _signedUpId = MutableLiveData<String>()
    val signedUpId: LiveData<String>
        get() = _signedUpId

    fun trySignUp(nickname: String?, passwordHash: String?, profession: String?) {
        usersRepository.getByNickname(nickname, SignUpCheckExistsCallbackHandler(
            object: CallbackListener {
                override fun onSuccess() {
                    usersRepository.add(
                        User(nickname=nickname, passwordHash=passwordHash, profession=profession),
                        SignUpCallbackHandler(object: CallbackListenerWithResult<User> {
                            override fun onSuccess(result: User) {
                                logInManager.logIn(result.identifier!!)
                                viewModelScope.launch{
                                    _signedUpId.value = result.identifier
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

                override fun onFailure(message: String?) {
                    viewModelScope.launch{
                        _failure.value = message
                    }
                }

            }
        ))
    }

    companion object {
        fun getSignUpViewModelFactory(context: Context): SignUpViewModelFactory {
            return SignUpViewModelFactory(context)
        }
    }

}

@Suppress("UNCHECKED_CAST")
class SignUpViewModelFactory(private val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SignUpViewModel(
            LogInManager(context.getSharedPreferences(LogInManager.FILE_NAME, Context.MODE_PRIVATE)),
            UsersFirebaseRepository(context.resources.getString(R.string.db_location))) as T
    }
}