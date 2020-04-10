package com.cygnus.model

import co.aspirasoft.model.BaseModel

/**
 * SchoolClass is a class in a [School].
 *
 * A class consists of a group of [Student]s, and is assigned to
 * one [Teacher] who manages that class. Students in one class
 * study different [Subject]s, each taught by a particular
 * teacher.
 *
 * @param name Name of the class.
 * @param teacherId Unique id of the class teacher.
 *
 * @author saifkhichi96
 * @since 1.0.0
 */
class SchoolClass(name: String, teacherId: String) : BaseModel() {

    // no-arg constructor required for Firebase
    constructor() : this("", "")

    var name = name
        set(value) {
            field = value
            notifyObservers()
        }

    var teacherId = teacherId
        set(value) {
            field = value
            notifyObservers()
        }

}