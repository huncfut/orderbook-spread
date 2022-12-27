package orderbook.spread

import org.http4k.client.ApacheClient
import org.http4k.core.HttpHandler

// Setup
fun main() {
    val apiURL = "https://public.kanga.exchange/api/market"
    val client = ApacheClient()

    val app = App(client, apiURL)

    app.createFile()
}

class App(private val client: HttpHandler, private val baseURL: String) {
    private fun getMarkets() = GetMarkets(client, baseURL).main()
    private fun getSpread() = GetSpread(client, baseURL).main()

    fun createFile() {
        println(getMarkets())
        println(getSpread())
    }
}