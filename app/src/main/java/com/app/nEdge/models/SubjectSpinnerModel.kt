package com.app.nEdge.models

class SubjectSpinnerModel {
    private var title: String = ""
    private var selected = false

    fun getTitle(): String? {
        return title
    }

    fun setTitle(title: String) {
        this.title = title
    }

    fun isSelected(): Boolean {
        return selected
    }

    fun setSelected(selected: Boolean) {
        this.selected = selected
    }
}