package mperezf.mimo.gruposcaminosantiago.data.mapper

import mperezf.mimo.gruposcaminosantiago.domain.model.User
import mperezf.mimo.gruposcaminosantiago.domain.model.UserData

class UserMapper : BaseMapper<UserData, User>() {

    public override fun map(dataModel: UserData): User {
        return User(id = dataModel.id, email = dataModel.email, name = dataModel.name, photo = dataModel.photo)
    }

}