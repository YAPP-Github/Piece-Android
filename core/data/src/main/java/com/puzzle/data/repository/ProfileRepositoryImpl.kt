package com.puzzle.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.core.net.toUri
import com.puzzle.common.suspendRunCatching
import com.puzzle.database.model.matching.ValuePickAnswer
import com.puzzle.database.model.matching.ValuePickEntity
import com.puzzle.database.model.matching.ValuePickQuestion
import com.puzzle.database.model.matching.ValueTalkEntity
import com.puzzle.database.source.profile.LocalProfileDataSource
import com.puzzle.datastore.datasource.token.LocalTokenDataSource
import com.puzzle.datastore.datasource.user.LocalUserDataSource
import com.puzzle.domain.model.profile.Contact
import com.puzzle.domain.model.profile.ValuePick
import com.puzzle.domain.model.profile.ValueTalk
import com.puzzle.domain.model.profile.ValueTalkAnswer
import com.puzzle.domain.repository.ProfileRepository
import com.puzzle.network.model.UNKNOWN_INT
import com.puzzle.network.source.profile.ProfileDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val profileDataSource: ProfileDataSource,
    private val localProfileDataSource: LocalProfileDataSource,
    private val localTokenDataSource: LocalTokenDataSource,
    private val localUserDataSource: LocalUserDataSource,
) : ProfileRepository {
    override suspend fun loadValuePicks(): Result<Unit> = suspendRunCatching {
        val valuePicks = profileDataSource.loadValuePicks()
            .getOrThrow()
            .toDomain()
            .filter { it.id != UNKNOWN_INT }

        val valuePickEntities = valuePicks.map { valuePick ->
            ValuePickEntity(
                valuePickQuestion = ValuePickQuestion(
                    id = valuePick.id,
                    category = valuePick.category,
                    question = valuePick.question,
                ),
                answers = valuePick.answers.map { answer ->
                    ValuePickAnswer(
                        questionsId = valuePick.id,
                        number = answer.number,
                        content = answer.content,
                    )
                }
            )
        }

        localProfileDataSource.replaceValuePicks(valuePickEntities)
    }

    override suspend fun loadValueTalks(): Result<Unit> = suspendRunCatching {
        val valueTalks = profileDataSource.loadValueTalks()
            .getOrThrow()
            .toDomain()
            .filter { it.id != UNKNOWN_INT }

        val valueTalkEntities = valueTalks.map {
            ValueTalkEntity(
                id = it.id,
                title = it.title,
                category = it.category,
                helpMessages = it.helpMessages,
            )
        }

        localProfileDataSource.replaceValueTalks(valueTalkEntities)
    }

    override suspend fun retrieveValuePick(): Result<List<ValuePick>> = suspendRunCatching {
        localProfileDataSource.retrieveValuePicks()
            .map(ValuePickEntity::toDomain)
    }

    override suspend fun retrieveValueTalk(): Result<List<ValueTalk>> = suspendRunCatching {
        localProfileDataSource.retrieveValueTalks()
            .map(ValueTalkEntity::toDomain)
    }

    override suspend fun checkNickname(nickname: String): Result<Boolean> =
        profileDataSource.checkNickname(nickname)

    override suspend fun uploadProfile(
        birthdate: String,
        description: String,
        height: Int,
        weight: Int,
        imageUrl: String,
        job: String,
        location: String,
        nickname: String,
        phoneNumber: String,
        smokingStatus: String,
        snsActivityLevel: String,
        contacts: List<Contact>,
        valuePicks: List<com.puzzle.domain.model.profile.ValuePickAnswer>,
        valueTalks: List<ValueTalkAnswer>
    ): Result<Unit> = suspendRunCatching {
        val uploadedImageUrl = resizeImage(context = context, uri = imageUrl.toUri())
            .use { imageInputStream ->
                profileDataSource.uploadProfileImage(imageInputStream)
                    .getOrThrow()
            }

        val response = profileDataSource.uploadProfile(
            birthdate = birthdate,
            description = description,
            height = height,
            weight = weight,
            imageUrl = uploadedImageUrl,
            job = job,
            location = location,
            nickname = nickname,
            phoneNumber = phoneNumber,
            smokingStatus = smokingStatus,
            snsActivityLevel = snsActivityLevel,
            contacts = contacts,
            valuePicks = valuePicks,
            valueTalks = valueTalks
        ).getOrThrow()

        coroutineScope {
            val accessTokenJob = launch {
                response.accessToken?.let { localTokenDataSource.setAccessToken(it) }
            }
            val refreshTokenJob = launch {
                response.refreshToken?.let { localTokenDataSource.setRefreshToken(it) }
            }
            val userRoleJob = launch {
                response.role?.let { localUserDataSource.setUserRole(it) }
            }

            accessTokenJob.join()
            refreshTokenJob.join()
            userRoleJob.join()
        }
    }

    private fun resizeImage(
        context: Context,
        uri: Uri,
        reqWidth: Int = 1024,
        reqHeight: Int = 1024,
    ): InputStream {
        val originImageStream = context.contentResolver.openInputStream(uri)

        originImageStream?.use { inputStream ->
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeStream(inputStream, null, options)

            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
            options.inJustDecodeBounds = false

            context.contentResolver.openInputStream(uri)?.use { newInputStream ->
                val resizedBitmap = BitmapFactory.decodeStream(newInputStream, null, options)

                val byteArrayOutputStream = ByteArrayOutputStream()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    resizedBitmap?.compress(
                        Bitmap.CompressFormat.WEBP_LOSSY, 100, byteArrayOutputStream
                    )
                } else {
                    resizedBitmap?.compress(
                        Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream
                    )
                }
                val byteArray = byteArrayOutputStream.toByteArray()

                return ByteArrayInputStream(byteArray)
            }
        }

        throw IllegalArgumentException("Unable to open InputStream for the given URI")
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }
}
