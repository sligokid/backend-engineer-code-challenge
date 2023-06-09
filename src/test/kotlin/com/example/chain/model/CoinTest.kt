package com.example.chain.model

import com.example.chain.Blockchain
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CoinTest {

    @Test
    fun coinAisParentOfCoinB() {
        val block0 = Block()
        val coinbaseTx = Transaction(block0, true, mutableListOf(), mutableListOf())
        val coinA = Coin(coinbaseTx, null, "wallet-0-address")

        val txB = Transaction(block0, false, mutableListOf(), mutableListOf())
        coinA.spenderTx = txB
        txB.inputs.add(coinA)
        val coinB = Coin(txB, null, "wallet-1-address")
        txB.outputs.add(coinB);

        assertFalse(coinA.isParentOf(coinA))
        assertFalse(coinB.isParentOf(coinA))
        assertTrue(coinA.isParentOf(coinB))
    }

    @Test
    fun coinAisAncestorOfCoinB() {
        val block0 = Block()
        val coinbaseTx = Transaction(block0, true, mutableListOf(), mutableListOf())
        val coinA = Coin(coinbaseTx, null, "wallet-0-address")

        val tx1 = Transaction(block0, false, mutableListOf(), mutableListOf())
        coinA.spenderTx = tx1
        tx1.inputs.add(coinA)
        val coin1 = Coin(tx1, null, "wallet-1-address")
        tx1.outputs.add(coin1);

        val tx2 = Transaction(block0, false, mutableListOf(), mutableListOf())
        coin1.spenderTx = tx2
        tx2.inputs.add(coin1)
        val coin2 = Coin(tx2, null, "wallet-2-address")
        tx1.outputs.add(coin2);

        val txB = Transaction(block0, false, mutableListOf(), mutableListOf())
        coin2.spenderTx = txB
        txB.inputs.add(coin2)
        val coinB = Coin(txB, null, "wallet-B-address")
        txB.outputs.add(coinB);

        assertTrue(coinA.isAncestorOf(coinB))
    }


    @Test
    fun coinAisDistantAncestorOfCoinB() {
        val blockchain = Blockchain()
        val block0 = Block()
        blockchain.add(block0)

        val tx0 = Transaction(block0, true, mutableListOf(), mutableListOf())
        val coin0 = Coin(tx0, null, "wallet-0-address")
        val coin1 = Coin(tx0, null, "wallet-1-address")
        tx0.outputs.add(coin0)
        tx0.outputs.add(coin1)
        block0.transactions.add(tx0)

        val tx1 = Transaction(block0, false, mutableListOf(), mutableListOf())
        val coin2 = Coin(tx1, null, "wallet-2-address")
        val coin3 = Coin(tx1, null, "wallet-3-address")
        coin1.spenderTx = tx1

        tx1.inputs.add(coin1)
        tx1.outputs.add(coin2)
        tx1.outputs.add(coin3)
        block0.transactions.add(tx1)

        val tx2 = Transaction(block0, false, mutableListOf(), mutableListOf())
        val coin4 = Coin(tx2, null, "wallet-4-address")
        coin0.spenderTx = tx2
        coin2.spenderTx = tx2

        tx2.inputs.add(coin0)
        tx2.inputs.add(coin2)
        tx2.outputs.add(coin4)
        block0.transactions.add(tx2)

        assertTrue(coin0.isParentOf(coin4))
        assertTrue(coin1.isParentOf(coin2))
        assertTrue(coin1.isParentOf(coin3))
        assertTrue(coin2.isParentOf(coin4))

        assertTrue(coin0.isAncestorOf(coin4))
        assertTrue(coin1.isAncestorOf(coin4))
        assertTrue(coin2.isAncestorOf(coin4))
    }

}