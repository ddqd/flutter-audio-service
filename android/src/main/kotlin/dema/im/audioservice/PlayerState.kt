package dema.im.audioservice

class PlayerState private constructor(builder: Builder) {

    val state: String?
    val id: String?

    init {
        state = builder.state
        id = builder.id
    }

    class Builder {
        var state: String? = null
            private set
        var id: String? = null
            private set

        fun state(state: String) = apply {this.state = state}
        fun id(id: String?) = apply { this.id = id}

        fun build() = PlayerState(this)
    }
}

