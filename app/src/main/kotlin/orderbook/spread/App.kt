package orderbook.spread

import org.http4k.client.ApacheClient
import org.http4k.core.HttpHandler
import org.http4k.filter.ClientFilters
import org.http4k.filter.RequestFilters

// Setup
suspend fun main() {
    val apiURL = "https://public.kanga.exchange/api/market"
    val client = ApacheClient()

    val app = App(client, apiURL)

    app.createFile()
}

class App(private val client: HttpHandler, private val baseURL: String) {
    // Reusable components
    private fun getMarkets() = GetMarkets(client, baseURL).main()
    private fun getSpread(name: String) = GetSpread(client, baseURL).main(name)

    private enum class Groups { LOW, HIGH, NOT_FLUID, ERR }

    fun createFile() {
        val markets = getMarkets()

        val spreads = markets.map { market -> getSpread(market) }
            .groupBy{ when {
                    it.spread == null -> Groups.NOT_FLUID
                    it.spread <= 2 -> Groups.LOW
                    it.spread > 2 -> Groups.HIGH// 2 is going to be in LOW
                    else -> Groups.ERR
            } }

        spreads.map { println(it) }
    }
}