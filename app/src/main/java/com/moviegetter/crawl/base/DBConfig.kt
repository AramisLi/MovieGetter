package com.moviegetter.crawl.base

/**
 *Created by Aramis
 *Date:2018/6/22
 *Description:
 */
class DBConfig(val tableName: String,
               val columns: List<DBItem>)

class DBItem(val name: String, val cate: String, val length: Int? = null, val comment: String? = null)