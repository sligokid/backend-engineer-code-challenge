package com.example.chain.model

data class Transaction(
    // The block this transaction belongs to
    val block: Block?,
    // If this is the tranactopn that mints the coin and pays miner
    val isCoinbase: Boolean = false,
    // list of ancestors, empty it this is the coinbase tx
    val inputs: MutableList<Coin> = mutableListOf(),
    // list of outputs
    val outputs: MutableList<Coin> = mutableListOf()
)
