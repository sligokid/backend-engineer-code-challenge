package com.example.chain.model

data class Coin(
    // The transaction that created this coin - never null
    val creatorTx: Transaction,
    // The transaction that spent this coin - null if coin is not spent
    val spenderTx: Transaction?,
    // The wallet address of the holder
    val address: String,
    // amount of satoshis
    val amount: ULong = ULong.MIN_VALUE
)
