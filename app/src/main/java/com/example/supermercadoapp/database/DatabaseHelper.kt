package com.example.supermercadoapp.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.supermercadoapp.Product

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object{
        private const val DB_VERSION = 1
        private const val DB_NAME = "mercado"
        private const val TABLE_PRODUTOS = "produtos"

        private const val PRODUTO_ID = "_id"
        private const val PRODUTO_NOME = "produtonome"
        private const val PRODUTO_PRECO = "produtopreco"
        private const val PRODUTO_CATEGORIA = "produtocategoria"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_PRODUTO_TABLE = "CREATE TABLE $TABLE_PRODUTOS (" +
                "$PRODUTO_ID INTEGER PRIMARY KEY, " +
                "$PRODUTO_NOME TEXT, " +
                "$PRODUTO_PRECO REAL, " +
                "$PRODUTO_CATEGORIA TEXT" + ")"
        db?.execSQL(CREATE_PRODUTO_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_PRODUTOS"
        db!!.execSQL(DROP_TABLE)
        onCreate(db)
    }

    fun addProduto(produto: Product): Long{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(PRODUTO_NOME, produto.name)
        contentValues.put(PRODUTO_PRECO, produto.price)
        contentValues.put(PRODUTO_CATEGORIA, produto.category)

        val success = db.insert(TABLE_PRODUTOS, null, contentValues)

        db.close()
        return success
    }

    fun viewProduto(): ArrayList<Product>{
        val produtoList: ArrayList<Product> = ArrayList<Product>()

        val selectQuery = "SELECT * FROM $TABLE_PRODUTOS"

        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException){
            db.execSQL(selectQuery)
            return ArrayList()
        }

        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    val id = it.getInt(it.getColumnIndexOrThrow(PRODUTO_ID))
                    val nome = it.getString(it.getColumnIndexOrThrow(PRODUTO_NOME))
                    val preco = it.getFloat(it.getColumnIndexOrThrow(PRODUTO_PRECO))
                    val categoria = it.getString(it.getColumnIndexOrThrow(PRODUTO_CATEGORIA))

                    val prod = Product(id = id, name = nome, price = preco, category = categoria)
                    produtoList.add(prod)
                } while (it.moveToNext())
            }
        }
        return produtoList
    }

    fun deleteProduto(produto: Product): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(PRODUTO_ID, produto.id)

        val success = db.delete(TABLE_PRODUTOS, PRODUTO_ID + "=" + produto.id, null)
        db.close()
        return success
    }
}