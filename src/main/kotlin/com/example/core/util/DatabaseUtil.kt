package com.example.core.util

import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.isNull
import org.jetbrains.exposed.sql.`java-time`.datetime
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

fun now(): LocalDateTime = ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime()

abstract class ExtendedUUIDTable(name: String, pk: String = "uid") :
    UUIDTable(name = name, columnName = pk) {
    val createAt = datetime("create_at").clientDefault { now() }
    val updateAt = datetime("update_at").nullable()
    val deleteAt = datetime("delete_at").nullable()
}

abstract class ExtendedUUIDEntity(id: EntityID<UUID>, table: ExtendedUUIDTable) : UUIDEntity(id) {
    val createAt by table.createAt
    var updateAt by table.updateAt
    private var deleteAt by table.deleteAt

    val uid: UUID
        get() = id.value

    override fun delete() {
        deleteAt = now()
    }
}

abstract class ExtendedUUIDEntityClass<Entity : ExtendedUUIDEntity>(private val extendedTable: ExtendedUUIDTable) :
    UUIDEntityClass<Entity>(extendedTable) {

    private var includeSoftDeleted = false

    init {
        EntityHook.subscribe { action ->
            if (action.changeType == EntityChangeType.Updated) {
                try {
                    action.toEntity(this)?.updateAt = now()
                } catch (ignore: Exception) {
                }
            }
        }
    }

    fun includeDeleted() = apply {
        includeSoftDeleted = true
    }

    override fun all(): SizedIterable<Entity> {
        return if (includeSoftDeleted) {
            all()
        } else {
            wrapRows(table.select { extendedTable.deleteAt.isNull() }.notForUpdate())
        }
    }

    override fun searchQuery(op: Op<Boolean>): Query {
        var query = super.searchQuery(op)
        if (!includeSoftDeleted) {
            query = query.andWhere { extendedTable.deleteAt.isNull() }
        }
        return query
    }
}