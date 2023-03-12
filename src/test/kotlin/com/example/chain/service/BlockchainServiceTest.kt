package com.example.chain.service

import com.example.chain.Blockchain
import com.example.chain.model.Block
import com.example.chain.model.Coin
import com.example.chain.model.Transaction
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class BlockchainServiceTest {

    @Test
    fun ignoreCoinBaseTx(){
        val blockchainService = BlockchainService()
        val blockchain = Blockchain()
        val block0 = Block()
        val coinbaseTx = Transaction(block0, true, mutableListOf(), mutableListOf())
        val coinA = Coin(coinbaseTx, null, "wallet-0-address")
        coinbaseTx.outputs.add(coinA)
        block0.transactions.add(coinbaseTx)
        blockchain.add(block0)

        val address = blockchainService.findMaximumInboundVolumeAddress(blockchain)

        assertEquals("", address)
    }

    @Test
    fun ignoreSpentCoins(){
        val blockchainService = BlockchainService()
        val blockchain = Blockchain()
        val block1 = Block()
        val creatorTx = Transaction(block1, false, mutableListOf(), mutableListOf())
        val spenderTx = Transaction(block1, false, mutableListOf(), mutableListOf())
        val coin = Coin(creatorTx, spenderTx, "wallet-0-address")
        creatorTx.outputs.add(coin)
        block1.transactions.add(creatorTx)
        blockchain.add(block1)

        val address = blockchainService.findMaximumInboundVolumeAddress(blockchain)

        assertEquals("", address)
    }

    @Test
    fun storeWalletAddressOfUnSpentCoin(){
        val blockchainService = BlockchainService()
        val blockchain = Blockchain()
        val block1 = Block()
        val creatorTx = Transaction(block1, false, mutableListOf(), mutableListOf())
        val spenderTx = null
        val coin = Coin(creatorTx, spenderTx, "wallet-0-address", 100000000u)
        creatorTx.outputs.add(coin)
        block1.transactions.add(creatorTx)
        blockchain.add(block1)

        val address = blockchainService.findMaximumInboundVolumeAddress(blockchain)

        assertEquals("wallet-0-address", address)
    }

    @Test
    fun findMaximumInboundVolumeAddress(){
        val blockchainService = BlockchainService()
        val blockchain = Blockchain()
        //
        val block1 = Block()
        val creatorTx = Transaction(block1, false, mutableListOf(), mutableListOf())
        val spenderTx = null
        val coin = Coin(creatorTx, spenderTx, "wallet-0-address", 10u)
        creatorTx.outputs.add(coin)
        block1.transactions.add(creatorTx)
        //
        val block2 = Block()
        val creatorTx2 = Transaction(block2, false, mutableListOf(), mutableListOf())
        val spenderTx2 = null
        val coin2 = Coin(creatorTx2, spenderTx2, "wallet-2-address", 100000000u)
        creatorTx2.outputs.add(coin2)
        block2.transactions.add(creatorTx2)
        //
        blockchain.add(block1)
        blockchain.add(block2)

        val address = blockchainService.findMaximumInboundVolumeAddress(blockchain)

        assertEquals("wallet-2-address", address)
    }

}