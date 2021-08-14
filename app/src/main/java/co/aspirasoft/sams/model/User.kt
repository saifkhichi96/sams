package co.aspirasoft.sams.model

sealed class User {
    var id = 0
    var name: String? = null
    var account: Account? = null
}

class Student : User() {
    var type: String? = null
    var universityName: String? = null

    @Transient
    var department: Department? = null

    @Transient
    var school: School? = null

    @Transient
    var university: University? = null
}

class Admin : User()
class Teacher : User()
class Warden : User()