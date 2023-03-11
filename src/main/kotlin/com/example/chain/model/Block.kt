package com.example.chain.model

data class Block(
    // The block time in milliseconds
    val blockTime: Long = System.currentTimeMillis(),
    // List of transactions in this block
    val transactions: MutableList<Transaction> = mutableListOf()
) : Comparable<Block> {
    // natural ordering of blocks by time
    override fun compareTo(other: Block): Int = when {
        blockTime != other.blockTime -> (other.blockTime - blockTime).toInt()
        else -> other.blockTime.compareTo(blockTime)
    }
}
