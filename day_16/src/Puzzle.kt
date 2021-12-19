package src

import java.io.File
import java.lang.RuntimeException
import java.math.BigInteger

data class Packet(
    val version: Int,
    val typeID: Int,
    val size: Int = -1,
    val data: Long? = null,
    val subPackets: List<Packet> = ArrayList<Packet>()
)

fun decodePacket(msg: String): Packet {
    var pp: Int = 0 // packetPointer
    val version: Int = msg.substring(pp until pp + 3).toInt(2)
    pp += 3
    val type: Int = msg.substring(pp until pp + 3).toInt(2)
    pp += 3
    if (type == 4) {
        val parts: MutableList<String> = mutableListOf()
        while (true) {
            val part = msg.substring(pp until pp + 5)
            parts.add(part.substring(1..4))
            pp += 5
            if (part[0] == '0') break
        }
        val data: Long = parts.joinToString("").toLong(2)
        return Packet(version, type, pp, data)
    } else {
        val subPackets: MutableList<Packet> = mutableListOf()
        val LTID: Char = msg[pp] // Length Type ID
        pp += 1
        if (LTID == '0') {
            val subPacketsLength: Int = msg.substring(pp until pp + 15).toInt(2)
            pp += 15
            var totalRead: Int = 0
            while (totalRead != subPacketsLength) {
                val subPacket: Packet = decodePacket(msg.substring(pp until msg.length))
                totalRead += subPacket.size
                pp += subPacket.size
                subPackets.add(subPacket)
            }
            return Packet(version, type, pp, null, subPackets.toList())
        } else {
            val NOSP: Int = msg.substring(pp until pp + 11).toInt(2)  // Number Of SubPackets
            pp += 11
            repeat(NOSP) {
                val subPacket: Packet = decodePacket(msg.substring(pp until msg.length))
                pp += subPacket.size
                subPackets.add(subPacket)
            }
            return Packet(version, type, pp, null, subPackets.toList())
        }
    }
}

fun getVersionSum(packet: Packet): Int {
    var sum: Int = packet.version
    packet.subPackets.forEach {
        sum += getVersionSum(it)
    }
    return sum
}

fun calculate(packet: Packet): Long {
    return when (packet.typeID) {
        0 -> packet.subPackets.fold(0.toLong()) { acc, pac -> acc + calculate(pac) }    // ADD
        1 -> packet.subPackets.fold(1.toLong()) { acc, pac -> acc * calculate(pac) }    // MULTI
        2 -> packet.subPackets.minOf { calculate(it) }  // MIN
        3 -> packet.subPackets.maxOf { calculate(it) }  // MAX
        4 -> packet.data!!  // CONST
        5 -> if(calculate(packet.subPackets[0]) > calculate(packet.subPackets[1])) 1.toLong() else 0.toLong()   // GT
        6 -> if(calculate(packet.subPackets[0]) < calculate(packet.subPackets[1])) 1.toLong() else 0.toLong()   //LT
        7 -> if(calculate(packet.subPackets[0]) == calculate(packet.subPackets[1])) 1.toLong() else 0.toLong()  //EQ
        else -> throw RuntimeException("Invalid packet typeID: " + packet.typeID)
    }
}
fun main(args: Array<String>) {
    val fileName = "puzzle_data.txt"
    val mainHexPacket = File(fileName).readLines()[0]

    val binaryInput = mainHexPacket.map {
        BigInteger(it.toString(), 16).toString(2).padStart(4, '0')
    }.joinToString("");

    val packet = decodePacket(binaryInput)
//    println(packet)
    println("Sum of version numbers = " + getVersionSum(packet))
    print("Expression value = " + calculate(packet))
}

