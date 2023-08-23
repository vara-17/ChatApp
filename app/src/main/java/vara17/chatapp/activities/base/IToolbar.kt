package vara17.chatapp.activities.base

import androidx.appcompat.widget.Toolbar

interface IToolbar {
    fun toolbarToLoad(toolbar: Toolbar)
    fun enableHomeDisplay(value: Boolean)
}