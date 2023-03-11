package com.example.chain.service

import com.example.chain.Blockchain
import com.example.chain.model.Coin

class BlockchainService {

    fun findMaximumInboundVolumeAddress(blockchain: Blockchain): Any? {
        val blocks = blockchain.getBlocks()
        val totals = mutableMapOf<String, ULong>()

        for (block in blocks) {
            for (tx in block.transactions) {
                for (coin in tx.outputs) {
                    if (coin.spenderTx == null) {
                        var value = totals.getOrDefault(coin.address, ULong.MIN_VALUE)
                        value += coin.amount
                        totals[coin.address] = value
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

    fun findCoinbaseAncestors(coinBase: Coin): List<Coin> {
        val ancestors = mutableListOf<Coin>()
        val creatorTx = coinBase.creatorTx;
        if (creatorTx.isCoinbase) {
            for (coin in creatorTx.inputs) {
                ancestors.add(coin)
            }
        }
        return ancestors
    }

}

