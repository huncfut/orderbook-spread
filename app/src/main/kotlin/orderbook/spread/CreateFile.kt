package orderbook.spread

import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CreateFile() {
  enum class Groups { LOW, HIGH, NOT_FLUID, ERR }

  fun main(markets: List<MarketSpread>) {
    val grouped = groupMarkets(markets)

    // Print in case of unexpected error
    if(!grouped[Groups.ERR].isNullOrEmpty()) {
      println("Wrong grouped:")
      grouped[Groups.ERR]!!.map { println(it) }
    }

    // Open file output
    val file = File(getFileName())

    println(grouped)

    grouped.forEach { pair -> printGroup(file, pair.key, pair.value) }

  }

  private fun printGroup(file: File, group: Groups, markets: List<MarketSpread>) {
    file.appendText(
      when (group) {
        Groups.LOW -> "Spread <= 2%"
        Groups.HIGH -> "Spread > 2%"
        Groups.NOT_FLUID -> "Rynki bez płynności"
        Groups.ERR -> "ERROR"
      }
    )
    file.appendText("\nNazwa rynku Spread[%]\n")
    markets.map { market -> file.appendText("${market.name} ${market.spread}\n") }
  }

  // Group all the markets by their spreads
  private fun groupMarkets(markets: List<MarketSpread>): Map<Groups, List<MarketSpread>> {
    return markets
      .groupBy {
        when {
          it.spread == null -> Groups.NOT_FLUID
          it.spread <= 2 -> Groups.LOW
          it.spread > 2 -> Groups.HIGH
          else -> Groups.ERR
      } }
      .mapValues { pair -> pair.value.sortedBy { it.name } }
      .toSortedMap()
  }

  private fun getFileName(): String {
    val now = LocalDateTime.now()

    val dateFormatter = DateTimeFormatter.ofPattern("yyyy_MM_dd")
    val timeFormatter = DateTimeFormatter.ofPattern("HHmmss")

    val date = now.format(dateFormatter)
    val time = now.format(timeFormatter)

    return "report_spread_${date}T${time}Z.txt"
  }
}