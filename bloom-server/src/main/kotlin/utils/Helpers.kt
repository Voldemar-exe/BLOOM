package utils

import org.jetbrains.exposed.v1.dao.LongEntity

fun <DTO, DAO : LongEntity> syncEntities(
    incoming: List<DTO>,
    extractId: (DTO) -> Long,
    loadExisting: (List<Long>) -> Map<Long, DAO>,
    shouldUpdate: (DTO, DAO) -> Boolean,
    create: (DTO) -> Unit,
    update: (DTO, DAO) -> Unit,
) {
    if (incoming.isEmpty()) return

    val ids = incoming.map(extractId)
    val existingMap = loadExisting(ids)

    incoming.forEach { dto ->
        val id = extractId(dto)
        val existing = existingMap[id]

        when {
            existing == null -> create(dto)
            shouldUpdate(dto, existing) -> update(dto, existing)
        }
    }
}
