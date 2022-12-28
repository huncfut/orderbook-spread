package orderbook.spread

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request

@Serializable
data class MarketOrderbook(val timestamp: Double, val bids: List<Pair<Double, Double>>, val asks: List<Pair<Double, Double>>, val ticker_id: String)

data class MarketSpread(val name: String, val spread: Number)

class GetSpread(private val client: HttpHandler, private val baseURL: String) {
  fun main(): MarketSpread {
    fetchMatket("USD_BTC")
    return MarketSpread("nice", 12)
  }

  private fun fetchMatket(name: String) {
    val request = Request(Method.GET, "$baseURL/orderbook/$name")

    val response = client(request)

    Json.decodeFromString<MarketOrderbook>(response.bodyString())
  }
}