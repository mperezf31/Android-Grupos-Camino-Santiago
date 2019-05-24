package mperezf.mimo.gruposcaminosantiago.data.mapper

import mperezf.mimo.gruposcaminosantiago.data.model.GroupData
import mperezf.mimo.gruposcaminosantiago.domain.model.Group

class GroupMapper : BaseMapper<GroupData, Group>() {

    public override fun map(dataModel: GroupData): Group {
        return Group(
            id = dataModel.id,
            photo = dataModel.photo,
            title = dataModel.title,
            description = dataModel.description,
            departureDate = dataModel.departureDate,
            arrivalDate = dataModel.arrivalDate,
            departurePlace = dataModel.departurePlace,
            latitude = dataModel.latitude,
            longitude = dataModel.longitude,
            founder = UserMapper().map(dataModel.founder),
            members = dataModel.members.map { UserMapper().map(it) },
            messages = dataModel.posts.map { MessageMapper().map(it) }
        )
    }

    public override fun reverseMap(model: Group): GroupData {
        return GroupData(
            id = model.id,
            photo = model.photo,
            title = model.title,
            description = model.description,
            departureDate = model.departureDate,
            arrivalDate = model.arrivalDate,
            departurePlace = model.departurePlace,
            latitude = model.latitude,
            longitude = model.longitude,
            founder = UserMapper().reverseMap(model.founder),
            members = model.members.map { UserMapper().reverseMap(it) },
            posts = model.messages.map { MessageMapper().reverseMap(it) }
        )
    }

}