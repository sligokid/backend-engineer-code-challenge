package com.example.chain.service

import com.example.chain.Blockchain
import com.example.chain.model.Coin

class BlockchainService {

    fun findMaximumInboundVolumeAddress(blockchain: Blockchain, intervalStart: Long, intervalEnd: Long): Any? {
        val totals = mutableMapOf<String, ULong>()
        val blocks = blockchain.getBlocks()

        for (block in blocks) {
            if ((block.blockTime > intervalStart) && (block.blockTime < intervalEnd)) {
                for (tx in block.transactions) {
                    if (!tx.isCoinbase) {
                        for (coin in tx.outputs) {
                            if (coin.spenderTx == null) {
                                var value = totals.getOrDefault(coin.address, ULong.MIN_VALUE)
                                value += coin.amount
                                totals[coin.address] = value
                            }
                        }
                    }
                }
            }
        }

        var largest = ULong.MIN_VALUE
        var largestAddress = ""
        for ((address, total) in totals) {
            if (total > largest) {
                largest = total;
                largestAddress = address
            }
        }
        return largestAddress
    }

    fun findCoinbaseAncestors(coin: Coin): List<Coin> {
        val ancestors = mutableListOf<Coin>()
        val creatorTx = coin.creatorTx;

        for (c in creatorTx.inputs) {
            if (!c.creatorTx.isCoinbase) {
                ancestors.addAll(findCoinbaseAncestors(c))
            } else if (c.isAncestorOf(coin)) {
                ancestors.add(c)
            }
        }

        return ancestors
    }

}

