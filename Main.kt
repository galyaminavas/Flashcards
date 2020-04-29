package flashcards

import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random.Default.nextInt

fun main(args: Array<String>) {
    val scanner = Scanner(System.`in`)
    val cards = mutableMapOf<String, String>()
    val mistakes = mutableMapOf<String, Int>()
    var command = ""
    var log = mutableListOf<String>()
    val promptForCommand = "Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):"
    val promptForCard = "The card:"
    val promptForDef = "The definition of the card:"
    val promptForFile = "File name:"

    var exportAfterExit = false
    var exportFileName = ""
    if (args.size == 2) {
        if (args[0] == "-import") {
            if (File(args[1]).exists()) {
                val lines = File(args[1]).readLines()
                for (line in lines) {
                    val card = line.split(" -- ")[0]
                    val def = line.split(" -- ")[1]
                    val mistake = line.split(" -- ")[2].toInt()
                    cards[card] = def
                    mistakes[card] = mistake
                }
                println("${lines.size} cards have been loaded.\n")
                log.add("${lines.size} cards have been loaded.\n")

            } else {
                println("File not found.\n")
                log.add("File not found.\n")
            }
        } else if (args[0] == "-export") {
            exportAfterExit = true
            exportFileName = args[1]
        }
    } else if (args.size == 4) {
        if (args[0] == "-import") {
            if (File(args[1]).exists()) {
                val lines = File(args[1]).readLines()
                for (line in lines) {
                    val card = line.split(" -- ")[0]
                    val def = line.split(" -- ")[1]
                    val mistake = line.split(" -- ")[2].toInt()
                    cards[card] = def
                    mistakes[card] = mistake
                }
                println("${lines.size} cards have been loaded.\n")
                log.add("${lines.size} cards have been loaded.\n")

            } else {
                println("File not found.\n")
                log.add("File not found.\n")
            }

            if (args[2] == "-export") {
                exportAfterExit = true
                exportFileName = args[3]
            }

        } else if (args[2] == "-import") {
            if (File(args[3]).exists()) {
                val lines = File(args[3]).readLines()
                for (line in lines) {
                    val card = line.split(" -- ")[0]
                    val def = line.split(" -- ")[1]
                    val mistake = line.split(" -- ")[2].toInt()
                    cards[card] = def
                    mistakes[card] = mistake
                }
                println("${lines.size} cards have been loaded.\n")
                log.add("${lines.size} cards have been loaded.\n")

            } else {
                println("File not found.\n")
                log.add("File not found.\n")
            }

            if (args[0] == "-export") {
                exportAfterExit = true
                exportFileName = args[1]
            }

        }
    }

    while (command != "exit") {
        println(promptForCommand)
        log.add(promptForCommand)

        command = scanner.nextLine()
        log.add(command)

        if (command == "add") {
            // add a card
            println(promptForCard)
            log.add(promptForCard)

            val card = scanner.nextLine()
            log.add(card)

            if (cards.containsKey(card)) {
                println("The card \"${card}\" already exists.\n")
                log.add("The card \"${card}\" already exists.\n")

                continue
            }
            println(promptForDef)
            log.add(promptForDef)

            val def = scanner.nextLine()
            log.add(def)
            if (cards.containsValue(def)) {
                println("The definition \"${def}\" already exists.\n")
                log.add("The definition \"${def}\" already exists.\n")

                continue
            }
            cards[card] = def
            mistakes[card] = 0

            println("The pair (\"${card}\":\"${def}\") has been added.\n")
            log.add("The pair (\"${card}\":\"${def}\") has been added.\n")

            continue
        } else if (command == "remove") {
            // remove a card
            println(promptForCard)
            log.add(promptForCard)

            val card = scanner.nextLine()
            log.add(card)
            if (cards.containsKey(card)) {
                cards.remove(card)
                mistakes.remove(card)

                println("The card has been removed.\n")
                log.add("The card has been removed.\n")
                continue
            } else {
                println("Can't remove \"${card}\": there is no such card.\n")
                log.add("Can't remove \"${card}\": there is no such card.\n")
                continue
            }
        } else if (command == "import") {
            // load cards from file
            println(promptForFile)
            log.add(promptForFile)

            val fileName = scanner.nextLine()
            log.add(fileName)

            if (File(fileName).exists()) {
                val lines = File(fileName).readLines()
                for (line in lines) {
                    val card = line.split(" -- ")[0]
                    val def = line.split(" -- ")[1]
                    val mistake = line.split(" -- ")[2].toInt()
                    cards[card] = def
                    mistakes[card] = mistake
//                    if (!mistakes.containsKey(card)) {
//                        mistakes[card] = 0
//                    }
                }
                println("${lines.size} cards have been loaded.\n")
                log.add("${lines.size} cards have been loaded.\n")

                continue
            } else {
                println("File not found.\n")
                log.add("File not found.\n")
                continue
            }
        } else if (command == "export") {
            // save cards to file
            println(promptForFile)
            log.add(promptForFile)

            val fileName = scanner.nextLine()
            log.add(fileName)

            var fileContent = ""
            for ((card, def) in cards) {
                fileContent += "$card -- $def -- ${mistakes[card]}\n"
            }
//            fileContent.dropLast(1)
            File(fileName).writeText(fileContent)
            println("${cards.size} cards have been saved.\n")
            log.add("${cards.size} cards have been saved.\n")

            continue
        } else if (command == "ask") {
            // ask for a definition of some random card
            println("How many times to ask?")
            log.add("How many times to ask?")

            var n = scanner.nextLine().toInt()
            log.add(n.toString())

            while (n > 0) {
                val random = nextInt(0, cards.size)
                val card = cards.entries.elementAt(random)
                println("Print the definition of \"${card.key}\":")
                log.add("Print the definition of \"${card.key}\":")

                val answer = scanner.nextLine()
                log.add(answer)

                println(if (answer == card.value) {
                    log.add("\"Correct answer.\"")
                    "Correct answer."
                } else if (cards.containsValue(answer)) {
                    val key = cards.filterValues { it == answer }.keys.first()
                    log.add("Wrong answer. The correct one is \"${card.value}\", " +
                            "you\'ve just written the definition of " +
                            "\"${key}\".")
                    mistakes[card.key] = mistakes[card.key]!! + 1
                    "Wrong answer. The correct one is \"${card.value}\", " +
                            "you\'ve just written the definition of " +
                            "\"${key}\"."
                } else {
                    log.add("Wrong answer. The correct one is \"${card.value}\".")
                    mistakes[card.key] = mistakes[card.key]!! + 1
                    "Wrong answer. The correct one is \"${card.value}\"."
                })
                n--
            }
            continue
        } else if (command == "log") {
            // log
            println(promptForFile)
            log.add(promptForFile)

            val fileName = scanner.nextLine()
            log.add(fileName)

            File(fileName).writeText(log.joinToString(separator = "\n"))
            println("The log has been saved.\n")
            log.add("The log has been saved.\n")

            continue
        } else if (command == "hardest card") {
            // hardest card
            var maxMistakes = 0
            var wordsWithMistakes = mutableListOf<String>()
            for ((key, value) in mistakes) {
                if (value > maxMistakes) {
                    maxMistakes = value
                    wordsWithMistakes.clear()
                    wordsWithMistakes.add(key)
                } else if (value == maxMistakes) {
                    wordsWithMistakes.add(key)
                }
            }

            if (maxMistakes == 0) {
                println("There are no cards with errors.\n")
                log.add("There are no cards with errors.\n")
            } else if (wordsWithMistakes.size == 1) {
                println("The hardest card is \"${wordsWithMistakes[0]}\". You have ${maxMistakes} errors answering it.\n")
                log.add("The hardest card is \"${wordsWithMistakes[0]}\". You have ${maxMistakes} errors answering it.\n")
            } else {
                println("The hardest cards are ${wordsWithMistakes.joinToString(separator = ", ") {it -> "\"${it}\"" } }. " +
                        "You have ${maxMistakes} errors answering them.\n")
                log.add("The hardest cards are ${wordsWithMistakes.joinToString(separator = ", ") {it -> "\"${it}\"" } }. " +
                        "You have ${maxMistakes} errors answering them.\n")

            }

            continue
        } else if (command == "reset stats") {
            // reset stats
            for (card in mistakes) {
                mistakes[card.key] = 0
            }

            println("Card statistics has been reset.\n")
            log.add("Card statistics has been reset.\n")

            continue
        }
    }
    println("Bye bye!")

    if (exportAfterExit) {
        // save cards to file
        var fileContent = ""
        for ((card, def) in cards) {
            fileContent += "$card -- $def -- ${mistakes[card]}\n"
        }
//            fileContent.dropLast(1)
        File(exportFileName).writeText(fileContent)
        println("${cards.size} cards have been saved.\n")
        log.add("${cards.size} cards have been saved.\n")
    }
}
