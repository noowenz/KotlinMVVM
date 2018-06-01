package au.com.official.nwz.data.model.rest

import au.com.official.nwz.contracts.UrlContract
import com.google.gson.annotations.SerializedName

/**
 * Created by Nabin Shrestha on 6/1/2018.
 */
data class Landing(
        @SerializedName("firstname")
        val firstName: String?,
        @SerializedName("country_code")
        val countryCode: String = UrlContract.Values.COUNTRY_CODE,
        @SerializedName("phone_code")
        val phoneCode: String = UrlContract.Values.PHONE_CODE
)