package mperezf.mimo.gruposcaminosantiago.data.mapper

import mperezf.mimo.gruposcaminosantiago.domain.model.Group
import mperezf.mimo.gruposcaminosantiago.domain.model.GroupData

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

}