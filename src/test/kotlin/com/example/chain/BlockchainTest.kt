package com.example.chain

import com.example.chain.model.Block
import com.example.chain.model.Coin
import com.example.chain.model.Transaction
import com.example.chain.service.BlockchainService
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
        val block1 = Block(1678482698001)
        val block2 = Block(1678482698010)
        val block3 = Block(1678482698100)

        blockchain.add(block2)
        blockchain.add(block3)
        blockchain.add(block1)

        var blocks = blockchain.getBlocks()

        assertEquals(block3, blocks[0])
        assertEquals(block2, blocks[1])
        assertEquals(block1, blocks[2])
    }

    @Test
    fun findMaximumInboundVolumeAddressTest() {
        val blockchain = Blockchain();
        val blockchainService = BlockchainService()

        val result = blockchainService.findMaximumInboundVolumeAddress(blockchain);

        assertEquals("", result)
    }

    @Test
    fun findCoinbaseAncestorsTest() {
        val blockchain = Blockchain();
        val block = Block()
        val creatorTx = Transaction(block)
        val spenderTx = Transaction(block)
        val address = ""
        val coin = Coin(creatorTx, spenderTx, address)
        val blockchainService = BlockchainService()

        val result = blockchainService.findCoinbaseAncestors(coin);

        assertEquals(listOf<Coin>(), result)
    }

}