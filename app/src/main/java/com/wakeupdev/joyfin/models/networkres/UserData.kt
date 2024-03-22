package com.wakeupdev.joyfin.models.networkres

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.ArrayList

data class UserData(
    @Expose
    val id: String?,
    @Expose
    val email: String?,
    @Expose
    @SerializedName("first_name") val firstName: String?,

    @Expose
    @SerializedName("last_name") val lastName: String?,

    @Expose
    val tier: String?,

    @Expose
    @SerializedName("phone_number") val phoneNumber: String?,

    @Expose
    @SerializedName("smart_saver_balance") val smartSaverBalance: Double,

    @Expose
    @SerializedName("green_saver_balance") val greenSaverBalance: Double,

    @Expose
    @SerializedName("fixed_deposit_balance") val fixedDepositBalance: Double,

    @Expose
    @SerializedName("email_verified") val emailVerified: String?, // Consider converting to Boolean later

    @Expose
    @SerializedName("phone_verified") val phoneVerified: String?,  // Consider converting to Boolean later
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString(),
        parcel.readString(),
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(email)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(tier)
        parcel.writeString(phoneNumber)
        parcel.writeDouble(smartSaverBalance)
        parcel.writeDouble(greenSaverBalance)
        parcel.writeDouble(fixedDepositBalance)
        parcel.writeString(emailVerified)
        parcel.writeString(phoneVerified)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserData> {
        override fun createFromParcel(parcel: Parcel): UserData {
            return UserData(parcel)
        }

        override fun newArray(size: Int): Array<UserData?> {
            return arrayOfNulls(size)
        }
    }
}


data class Transaction(
    @Expose
    val amount: Double,
    @Expose
    val type: String?,
    @Expose
    val narration: String?,
    @SerializedName("date_created")
    @Expose
    val dateCreated: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readDouble(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(amount)
        parcel.writeString(type)
        parcel.writeString(narration)
        parcel.writeString(dateCreated)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Transaction> {
        override fun createFromParcel(parcel: Parcel): Transaction {
            return Transaction(parcel)
        }

        override fun newArray(size: Int): Array<Transaction?> {
            return arrayOfNulls(size)
        }
    }
}

data class ApiResponse(
    @Expose
    @SerializedName("user_data") val userData: UserData?,

    @Expose
    @SerializedName("smart_saver_transactions")
    val smartSaverTransactions: ArrayList<Transaction>?,

    @Expose
    @SerializedName("green_saver_transactions")
    val greenSaverTransactions: ArrayList<Transaction>?,

    @Expose
    @SerializedName("fixed_deposit_transactions")
    val fixedDepositTransactions: ArrayList<Transaction>?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(UserData::class.java.classLoader),
        parcel.createTypedArrayList(Transaction),
        parcel.createTypedArrayList(Transaction),
        parcel.createTypedArrayList(Transaction)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(userData, flags)
        parcel.writeTypedList(smartSaverTransactions)
        parcel.writeTypedList(greenSaverTransactions)
        parcel.writeTypedList(fixedDepositTransactions)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ApiResponse> {
        override fun createFromParcel(parcel: Parcel): ApiResponse {
            return ApiResponse(parcel)
        }

        override fun newArray(size: Int): Array<ApiResponse?> {
            return arrayOfNulls(size)
        }
    }
}

