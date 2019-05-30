package mperezf.mimo.gruposcaminosantiago.data.mapper

import mperezf.mimo.gruposcaminosantiago.data.model.UserData
import mperezf.mimo.gruposcaminosantiago.domain.model.User

class UserMapper : BaseMapper<UserData, User>() {

    public override fun map(dataModel: UserData): User {
        return User(id = dataModel.id, email = dataModel.email, password = dataModel.password, name = dataModel.name, photo = dataModel.photo)
    }

    public override fun reverseMap(model: User): UserData {
        return UserData(id = model.id, email = model.email, password = model.password, name = model.name, photo = model.photo)
    }

}