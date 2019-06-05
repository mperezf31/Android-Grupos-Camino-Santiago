package mperezf.mimo.gruposcaminosantiago.domain.model

data class Message(
    val id: Int?,
    val author: User?,
    val content: String?,
    val whenSent: Int?
)
