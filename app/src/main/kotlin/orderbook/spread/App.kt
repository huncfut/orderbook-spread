package orderbook.spread

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.http4k.client.ApacheClient
import org.http4k.core.HttpHandler

// Setup
suspend fun main() {
    val apiURL = "https://public.kanga.exchange/api/market"
    val client = ApacheClient()

    val app = App(client, apiURL)

    while(true) {
        app.periodic()
        delay(60*1000)
    }
}

class App(private val client: HttpHandler, private val baseURL: String) {
    // Reusable components
    private fun getMarkets() = GetMarkets(client, baseURL).main()
    private fun getSpread(name: String) = GetSpread(client, baseURL).main(name)
    private fun createFile(spreads: List<MarketSpread>) = CreateFile().main(spreads)

    fun periodic() {
        val spreads = getMarkets().map { getSpread(it) }
        createFile(spreads)
    }
}