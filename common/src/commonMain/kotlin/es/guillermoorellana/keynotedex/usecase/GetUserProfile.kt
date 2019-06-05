package es.guillermoorellana.keynotedex.usecase

import es.guillermoorellana.keynotedex.repository.NetworkRepository

class GetUserProfile(
    private val networkRepository: NetworkRepository
) {
    suspend fun execute(userId: String) =
        networkRepository.userProfile(userId)
}
