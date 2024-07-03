package tech.prismlabs.reference.settings

import tech.prismlabs.prismsdk.api_client.ExistingUser
import tech.prismlabs.prismsdk.api_client.Height
import tech.prismlabs.prismsdk.api_client.HeightUnit
import tech.prismlabs.prismsdk.api_client.NewUser
import tech.prismlabs.prismsdk.api_client.TermsOfService
import tech.prismlabs.prismsdk.api_client.UserRegion
import tech.prismlabs.prismsdk.api_client.UserSex
import tech.prismlabs.prismsdk.api_client.Weight
import tech.prismlabs.prismsdk.api_client.WeightUnit
import java.time.LocalDate

data class UserData(
    val email: String,
    val sex: Int,
    val height: Int,
    val weight: Int,
    val age: Int,
    val show3dModel: Boolean
)

val UserData.userSex: UserSex
    get() = when (sex) {
    0 -> UserSex.MALE
    1 -> UserSex.FEMALE
    else -> UserSex.NEUTRAL
}

fun UserData.toNewPrismUser(): NewUser {
    val userSex: UserSex = when (sex) {
        0 -> UserSex.MALE
        1 -> UserSex.FEMALE
        else -> UserSex.NEUTRAL
    }

    return NewUser(
        token = email.lowercase(),
        email = email.lowercase(),
        sex = userSex,
        region = UserRegion.NORTH_AMERICA,
        usaResidence = null,
        birthDate = LocalDate.from(LocalDate.now().minusYears(age.toLong())),
        weight = Weight(value = weight.toDouble(), unit = WeightUnit.POUNDS),
        height = Height(value = height.toDouble(), unit = HeightUnit.INCHES),
        termsOfService = TermsOfService(accepted = true, version = null),
        researchConsent = false
    )
}

fun UserData.toExistingPrismUser(researchConsent: Boolean = false): ExistingUser {
    val userSex: UserSex = when (sex) {
        0 -> UserSex.MALE
        1 -> UserSex.FEMALE
        else -> UserSex.NEUTRAL
    }

    return ExistingUser(
        token = email.lowercase(),
        email = email.lowercase(),
        sex = userSex,
        region = UserRegion.NORTH_AMERICA,
        usaResidence = null,
        birthDate = LocalDate.from(LocalDate.now().minusYears(age.toLong())),
        weight = Weight(value = weight.toDouble(), unit = WeightUnit.POUNDS),
        height = Height(value = height.toDouble(), unit = HeightUnit.INCHES),
        researchConsent = researchConsent
    )
}