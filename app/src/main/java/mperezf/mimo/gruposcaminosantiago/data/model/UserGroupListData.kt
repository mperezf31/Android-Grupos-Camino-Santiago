package mperezf.mimo.gruposcaminosantiago.data.model


data class UserGroupListData(

    var groupsCreated: List<GroupData> = emptyList(),
    var groupsAssociated: List<GroupData> = emptyList(),
    var otherGroups: List<GroupData> = emptyList()

)
