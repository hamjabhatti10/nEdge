package com.app.nEdge.customData.enums

enum class EducationType(private val value: String) {
    School_College("School/College"),
    University("University"),
    CSS_Professional("CSS/Professional");

    override fun toString() = value


    open fun fromString(value: String?): EducationType? {
        for (b in EducationType.values()) {
            if (b.value == value) {
                return b
            }
        }
        return null
    }
}