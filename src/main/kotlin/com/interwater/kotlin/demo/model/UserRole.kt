package com.interwater.kotlin.demo.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.*
import javax.persistence.*


@Entity
@Table(name = "user_roles")
data class UserRole(@Column(name = "role")
                    var role: String? = null) {
    constructor() : this(null)

    companion object {
        const val GUEST = "GUEST"
        const val ADMIN = "ADMIN"
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    var id: Long = 0

    @ManyToOne(fetch = FetchType.LAZY, cascade = arrayOf(CascadeType.ALL))
    @JoinColumn(name="user_id")
    @JsonIgnore
    var user: User? = null;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Column(name = "created_time", updatable = false)
    var createdTime: Date = Date()

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Column(name = "updated_time", insertable = false)
    var updatedTime: Date? = null
}