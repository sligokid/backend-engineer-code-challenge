package com.example.chain.service

import com.example.chain.Blockchain

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
//
//    fun findCoinbaseAncestors(coinBase: Coin): List<Coin> {
//        val ancestors = mutableListOf<Coin>()
//        val creatorTx = coinBase.creatorTx;
//        if (creatorTx.isCoinbase) {
//            for (coin in creatorTx.inputs) {
//                ancestors.add(coin)
//            }
//        }
//        return ancestors
//    }

}

