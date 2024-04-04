package org.company.app

import com.google.gson.annotations.SerializedName

data class GetData(
    @SerializedName("graph")
    val graph: List<String>?,
    @SerializedName("max")
    val max: List<String>?,
    @SerializedName("min")
    val min: List<String>?,
    @SerializedName("mean")
    val mean: List<String>?,
    @SerializedName("median")
    val median: List<String>?,
    @SerializedName("noise")
    val noise: List<String>?,
    @SerializedName("std")
    val std: List<String>?,
)
