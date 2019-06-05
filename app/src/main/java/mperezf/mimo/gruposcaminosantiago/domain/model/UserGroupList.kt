package mperezf.mimo.gruposcaminosantiago.domain.model


data class UserGroupList(

    var groupsCreated: List<Group> = emptyList(),
    var groupsAssociated: List<Group> = emptyList(),
    var otherGroups: List<Group> = emptyList()

)
