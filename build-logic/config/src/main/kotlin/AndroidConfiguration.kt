object AndroidConfiguration {
    val buildTools: Version = Version(major = 34, minor = 0, patch = 0)

    object Sdk {
        const val COMPILATION: Int = 35
        const val MIN: Int = 26
        const val TARGET: Int = COMPILATION
    }

    object Application {
        const val CODE: Int = 1
        val version: Version = Version(major = 1, minor = 0, patch = 2)
    }
}

data class Version(
    private val major: Int,
    private val minor: Int,
    private val patch: Int
) {
    override fun toString(): String = "$major.$minor.$patch"
}