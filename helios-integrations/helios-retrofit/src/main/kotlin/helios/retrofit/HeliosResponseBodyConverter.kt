package helios.retrofit

import arrow.core.flatMap
import arrow.core.getOrElse
import helios.core.Json
import helios.typeclasses.Decoder
import okhttp3.ResponseBody
import retrofit2.Converter
import java.nio.ByteBuffer

class HeliosResponseBodyConverter<T>(private val decoder: Decoder<T>) : Converter<ResponseBody, T> {
  override fun convert(value: ResponseBody): T? = try {
    Json
      .parseFromByteBuffer(ByteBuffer.wrap(value.byteStream().readBytes()))
      .flatMap { it.decode(decoder) }
      .getOrElse { null }
      .also { value.close() }
  } catch (ex: Exception) {
    null
  }
}
