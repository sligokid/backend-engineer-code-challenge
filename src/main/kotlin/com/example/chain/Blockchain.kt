package com.example.chain

import com.example.chain.model.Block
import java.util.TreeSet

class Blockchain {

    private val blocks = TreeSet(compareBy<Block> { it.blockTime })

    fun getBlocks(): Array<Block> {
        return blocks.toList().toTypedArray();
    }

    fun add(block: Block) {
        blocks.add(block)
    }

}
