package com.example.chain.model

data class Coin(
    // The transaction that created this coin - never null
    val creatorTx: Transaction,
    // The transaction that spent this coin - null if coin is not spent
    var spenderTx: Transaction?,
    // The wallet address of the holder
    val address: String,
    // amount of satoshis
    val amount: ULong = ULong.MIN_VALUE
) {
    fun isParentOf(coinB: Coin): Boolean {
        if (spenderTx == null) {
            return false;
        }
        return spenderTx!!.outputs.contains(coinB)
    }

    fun isAncestorOf(coinB: Coin): Boolean {
        if (isParentOf(coinB)) {
            return true
        }
        if (spenderTx != null) {
            val coins = spenderTx!!.outputs
            for (coin in coins) {
                if (coin == coinB) {
                    return true
                }
                if (coin.isParentOf(coinB)) {
                    return true
                }
            }
        }
        return false
    }

//    fun isAncestorOf(coinB: Coin, ignoreCoinBase : Boolean = true): Boolean {
//        if (isParentOf(coinB)) {
//            return true
//        }
//        if (spenderTx != null) {
//            val coins = spenderTx!!.outputs
//            for (coin in coins) {
//                if (coin == coinB || coin.isParentOf(coinB)) {
//                    if (ignoreCoinBase) {
//                        return true
//                    }
//                    return creatorTx.isCoinbase
//                }
//            }
//        }
//        return false
//    }
}

