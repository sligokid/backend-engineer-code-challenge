package com.example.chain

import com.example.chain.model.Block
import java.util.PriorityQueue
import java.util.Queue

class Blockchain {

    private val blocks: Queue<Block> = PriorityQueue();

    fun getBlocks(): List<Block> {
        return blocks.toList();
    }

    fun add(block: Block) {
        blocks.add(block)
    }

}
