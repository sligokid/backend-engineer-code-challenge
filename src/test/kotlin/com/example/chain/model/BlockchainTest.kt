package com.example.chain.model

import com.example.chain.Blockchain
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class BlockchainTest {

    @Test
    fun blocksAreAdded() {
        val blockchain = Blockchain()
        val block1 = Block()

        blockchain.add(block1)

        var blocks = blockchain.getBlocks()
        assertEquals(block1, blocks[0])
    }

    @Test
    fun blocksAreAddedOutOfOrderSortedByTime() {
        val blockchain = Blockchain()
        val block0 = Block(1678482697100)
        val block1 = Block(1678482698001)
        val block2 = Block(1678482698010)
        val block3 = Block(1678482698100)

        blockchain.add(block2)
        blockchain.add(block3)
        blockchain.add(block1)
        blockchain.add(block0)

        var blocks = blockchain.getBlocks()


        assertEquals(block0, blocks[0])
        assertEquals(block1, blocks[1])
        assertEquals(block2, blocks[2])
        assertEquals(block3, blocks[3])
    }





}