package orderbook.spread

import kotlinx.coroutines.delay
import org.http4k.client.ApacheClient
import org.http4k.core.HttpHandler

// Setup
suspend fun main() {
    val apiURL = "https://public.kanga.exchange/api/market"
    val client = ApacheClient()

    val app = App(client, apiURL)

    // Generate the file 60 seconds after creating the previous one
    while(true) {
        app.periodic()
        delay(60*1000)
    }
}

class App(private val client: HttpHandler, private val baseURL: String) {
    //
    private fun getMarkets() = GetMarkets(client, baseURL).start()
    private fun getSpread(name: String) = GetSpread(client, baseURL).start(name)
    private fun createFile(spreads: List<MarketSpread>) = CreateFile().start(spreads)

    // Generates the report and prints it to a file
    fun periodic() {
        val spreads = getMarkets().map { getSpread(it) }
        createFile(spreads)
    }
}