package com.example.chain

import com.example.chain.model.Block
import java.util.TreeSet

class Blockchain {

    private val blocks = TreeSet(compareBy<Block> { it.blockTime })

    fun getBlocks(): List<Block> {
        return blocks.toList();
    }

    fun add(block: Block) {
        blocks.add(block)
    }

}
