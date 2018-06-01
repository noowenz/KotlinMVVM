package au.com.official.nwz.base.model

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
        @SerializedName("message") val message: String,
        @SerializedName("results") val results: T?
)