package app.melon.group

import app.melon.data.entities.Group

interface GroupActions {
    fun onHolderClick(group: Group)
}