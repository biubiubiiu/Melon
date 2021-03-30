package app.melon.group

import app.melon.data.entities.InterestGroup

interface GroupActions {
    fun onHolderClick(group: InterestGroup)
}