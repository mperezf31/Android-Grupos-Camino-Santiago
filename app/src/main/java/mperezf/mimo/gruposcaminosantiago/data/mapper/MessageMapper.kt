package mperezf.mimo.gruposcaminosantiago.data.mapper

import mperezf.mimo.gruposcaminosantiago.domain.model.Message
import mperezf.mimo.gruposcaminosantiago.domain.model.MessageData

class MessageMapper : BaseMapper<MessageData, Message>() {

    public override fun map(dataModel: MessageData): Message {
        return Message(
            id = dataModel.id,
            author = UserMapper().map(dataModel.author),
            content = dataModel.content,
            whenSent = dataModel.whenSent
        )
    }

}