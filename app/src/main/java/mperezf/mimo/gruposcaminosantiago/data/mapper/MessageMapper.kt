package mperezf.mimo.gruposcaminosantiago.data.mapper

import mperezf.mimo.gruposcaminosantiago.data.model.MessageData
import mperezf.mimo.gruposcaminosantiago.domain.model.Message

class MessageMapper : BaseMapper<MessageData, Message>() {

    public override fun map(dataModel: MessageData): Message {
        return Message(
            id = dataModel.id,
            author = UserMapper().map(dataModel.author),
            content = dataModel.content,
            whenSent = dataModel.whenSent
        )
    }

    public override fun reverseMap(model: Message): MessageData {
        return MessageData(
            id = model.id,
            author = UserMapper().reverseMap(model.author),
            content = model.content,
            whenSent = model.whenSent
        )
    }

}