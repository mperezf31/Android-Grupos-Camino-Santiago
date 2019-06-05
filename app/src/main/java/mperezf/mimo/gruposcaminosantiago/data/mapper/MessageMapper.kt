package mperezf.mimo.gruposcaminosantiago.data.mapper

import mperezf.mimo.gruposcaminosantiago.data.model.MessageData
import mperezf.mimo.gruposcaminosantiago.domain.model.Message

class MessageMapper : BaseMapper<MessageData, Message>() {

    public override fun map(dataModel: MessageData): Message {

        return Message(
            id = dataModel.id,
            author = dataModel.author?.let { UserMapper().map(it) },
            content = dataModel.content,
            whenSent = dataModel.whenSent
        )
    }

    public override fun reverseMap(model: Message): MessageData {
        return MessageData(
            id = model.id,
            author = model.author?.let { UserMapper().reverseMap(it) },
            content = model.content,
            whenSent = model.whenSent
        )
    }

}