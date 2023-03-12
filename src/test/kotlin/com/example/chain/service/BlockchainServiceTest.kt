package com.example.chain.service

import com.example.chain.Blockchain
import com.example.chain.model.Block
import com.example.chain.model.Coin
import com.example.chain.model.Transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class BlockchainServiceTest {

    private val intervalStart = 1678482698000
    private val intervalEnd = 1678482710000

    @Test
    fun ignoreCoinBaseTx() {
        val blockchainService = BlockchainService()
        val blockchain = Blockchain()
        val block0 = Block(1678482699000)
        val coinbaseTx = Transaction(block0, true, mutableListOf(), mutableListOf())
        val coinA = Coin(coinbaseTx, null, "wallet-0-address")
        coinbaseTx.outputs.add(coinA)
        block0.transactions.add(coinbaseTx)
        blockchain.add(block0)

        val address = blockchainService.findMaximumInboundVolumeAddress(blockchain, intervalStart, intervalEnd)

        assertEquals("", address)
    }

    @Test
    fun ignoreSpentCoins() {
        val blockchainService = BlockchainService()
        val blockchain = Blockchain()
        val block1 = Block(1678482699000)
        val creatorTx = Transaction(block1, false, mutableListOf(), mutableListOf())
        val spenderTx = Transaction(block1, false, mutableListOf(), mutableListOf())
        val coin = Coin(creatorTx, spenderTx, "wallet-0-address")
        creatorTx.outputs.add(coin)
        block1.transactions.add(creatorTx)
        blockchain.add(block1)

        val address = blockchainService.findMaximumInboundVolumeAddress(blockchain, intervalStart, intervalEnd)

        assertEquals("", address)
    }

    @Test
    fun storeWalletAddressOfUnSpentCoin() {
        val blockchainService = BlockchainService()
        val blockchain = Blockchain()
        val block1 = Block(1678482699000)
        addValidTransactionToBlock(block1, blockchain)

        val address = blockchainService.findMaximumInboundVolumeAddress(blockchain, intervalStart, intervalEnd)

        assertEquals("wallet-0-address", address)
    }

    @Test
    fun ignoreBlocksBeforeTimeFrame() {
        val blockchainService = BlockchainService()
        val blockchain = Blockchain()
        val block0 = Block(1678482697000)

        addValidTransactionToBlock(block0, blockchain)

        val address = blockchainService.findMaximumInboundVolumeAddress(blockchain, intervalStart, intervalEnd)

        assertEquals("", address)
    }

    @Test
    fun ignoreBlocksAfterTimeFrame() {
        val blockchainService = BlockchainService()
        val blockchain = Blockchain()
        val block0 = Block(1678482710000)

        addValidTransactionToBlock(block0, blockchain)

        val address = blockchainService.findMaximumInboundVolumeAddress(blockchain, intervalStart, intervalEnd)

        assertEquals("", address)
    }

    private fun addValidTransactionToBlock(block1: Block, blockchain: Blockchain) {
        val creatorTx = Transaction(block1, false, mutableListOf(), mutableListOf())
        val spenderTx = null
        val coin = Coin(creatorTx, spenderTx, "wallet-0-address", 100000000u)
        creatorTx.outputs.add(coin)
        block1.transactions.add(creatorTx)
        blockchain.add(block1)
    }

    @Test
    fun findMaximumInboundVolumeAddress() {
        val blockchainService = BlockchainService()
        val blockchain = Blockchain()
        //
        val block1 = Block(1678482699000)
        val creatorTx = Transaction(block1, false, mutableListOf(), mutableListOf())
        val spenderTx = null
        val coin = Coin(creatorTx, spenderTx, "wallet-0-address", 10u)
        creatorTx.outputs.add(coin)
        block1.transactions.add(creatorTx)
        //
        val block2 = Block(1678482699100)
        val creatorTx2 = Transaction(block2, false, mutableListOf(), mutableListOf())
        val spenderTx2 = null
        val coin2 = Coin(creatorTx2, spenderTx2, "wallet-2-address", 100000000u)
        creatorTx2.outputs.add(coin2)
        block2.transactions.add(creatorTx2)
        //
        blockchain.add(block1)
        blockchain.add(block2)

        val address = blockchainService.findMaximumInboundVolumeAddress(blockchain, intervalStart, intervalEnd)

        assertEquals("wallet-2-address", address)
    }

    @Test
    fun coinbaseAncestors() {
        val blockchainService = BlockchainService()
        val block0 = Block()
        val coinbaseTx = Transaction(block0, true, mutableListOf(), mutableListOf())
        val coinA = Coin(coinbaseTx, null, "wallet-0-address")
        //
        val tx1 = Transaction(block0, false, mutableListOf(), mutableListOf())
        coinA.spenderTx = tx1
        tx1.inputs.add(coinA)
        val coin1 = Coin(tx1, null, "wallet-1-address")
        tx1.outputs.add(coin1);
        //
        val tx2 = Transaction(block0, false, mutableListOf(), mutableListOf())
        coin1.spenderTx = tx2
        tx2.inputs.add(coin1)
        val coin2 = Coin(tx2, null, "wallet-2-address")
        tx1.outputs.add(coin2);
        //
        val txB = Transaction(block0, false, mutableListOf(), mutableListOf())
        coin2.spenderTx = txB
        txB.inputs.add(coin2)
        val coinB = Coin(txB, null, "wallet-B-address")
        txB.outputs.add(coinB);

        val result = blockchainService.findCoinbaseAncestors(coinB)

        assertEquals(1, result.size)
        assertEquals("wallet-0-address", result[0].address)
    }


}