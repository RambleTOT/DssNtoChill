package org.company.app

import com.google.gson.annotations.SerializedName

data class GetData(
    @SerializedName("graph")
    val graph: String?,
    @SerializedName("max")
    val max: String?,
    @SerializedName("min")
    val min: String?,
    @SerializedName("mean")
    val mean: String?,
    @SerializedName("median")
    val median: String?,
    @SerializedName("noise")
    val noise: String?,
    @SerializedName("std")
    val std: String?,
)
