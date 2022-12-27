package orderbook.spread

import org.http4k.core.HttpHandler

data class MarketSpread(val name: String, val spread: Number)

class GetSpread(private val client: HttpHandler, private val baseURL: String) {
  fun main(): MarketSpread {
    return MarketSpread("nice", 12)
  }
}