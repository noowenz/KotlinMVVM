package au.com.official.nwz.data.rest

import au.com.official.nwz.base.model.BaseResponse
import au.com.official.nwz.contracts.UrlContract.Companion.GET_COMPETITIONS
import au.com.official.nwz.contracts.UrlContract.Companion.REGISTER
import au.com.official.nwz.data.model.db.Competitions
import au.com.official.nwz.data.model.rest.Landing
import io.reactivex.Observable
import retrofit2.http.*

interface IApiService {
    @POST(REGISTER)
    fun registerUser(@Body request: Landing): Observable<BaseResponse<Landing>>

    @GET(GET_COMPETITIONS)
    fun getCompetitions(): Observable<List<Competitions>>
}