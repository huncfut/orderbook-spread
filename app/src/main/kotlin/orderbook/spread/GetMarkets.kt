package orderbook.spread

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request

@Serializable
data class MarketPair(val ticker_id: String, val base: String, val target: String)

class GetMarkets(private val client: HttpHandler, private val baseURL: String) {
  fun main(): List<String> {
    val pairs = fetchMarkets()

    // Return only the names
    return pairs.map{pair -> pair.ticker_id}
  }

  // Fetch all the markets
  private fun fetchMarkets(): List<MarketPair> {
    val request = Request(Method.GET, "$baseURL/pairs")

    val response = client(request)

    return Json.decodeFromString(response.bodyString())
  }
}