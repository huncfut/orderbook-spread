package orderbook.spread

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status


data class MarketSpread(val name: String, val spread: Number?)

class GetSpread(private val client: HttpHandler, private val baseURL: String) {

  // Possibly should use Strings instead of Doubles, as the precision is very important
  @Serializable
  private data class MarketOrderbook(val timestamp: Long, val bids: List<List<Double>>, val asks: List<List<Double>>, val ticker_id: String)

  // return the market spread for a given market
  fun main(name: String): MarketSpread {
    val orderbook = fetchMatket(name)

    // Check if possible to calculate spread
    if(orderbook.asks.isEmpty() || orderbook.bids.isEmpty()) return MarketSpread(name, null)

    val minSell = orderbook.asks[0][0]
    val maxBuy = orderbook.bids[0][0]

    return MarketSpread(name, calcSpread(minSell, maxBuy))
  }

  // Fetch data for a given market
  private fun fetchMatket(name: String): MarketOrderbook {
    val request = Request(Method.GET, "$baseURL/orderbook/$name")

    val response = client(request)

    return Json.decodeFromString(response.bodyString())
  }

  // Calculate spread based on min sell and max buy values
  private fun calcSpread(sell: Double, buy: Double): Double {
    return ((sell - buy)/(sell + buy)) * 50
  }

}