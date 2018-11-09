package com.interwater.kotlin.demo.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*
import javax.persistence.*


@Entity
@Table(name = "users")
data class User(@Column(name = "username")
                var username: String,
                @Column(name = "password") var password: String? = null,
                @OneToMany(cascade = arrayOf(CascadeType.ALL), orphanRemoval = true, mappedBy = "user", fetch = FetchType.EAGER)
                val roles: MutableList<UserRole> = mutableListOf(UserRole(UserRole.GUEST))) {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    var id: Long = 0

    @Column(name = "remember_me")
    @JsonProperty("remeber_me")
    var rememberMe: Boolean? = false

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Column(name = "created_time", updatable = false)
    var createdTime: Date = Date()

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Column(name = "updated_time", insertable = false)
    var updatedTime: Date? = null

    override fun toString(): String {
        return this.username + "---" + this.password
    }
}