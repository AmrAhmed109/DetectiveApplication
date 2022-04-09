object Firebase {
    private const val FIREBASE_DATABASE_VERSION = "20.0.2"
    private const val FIREBASE_AUTH_VERSION = "21.0.1"
    private const val FIREBASE_FIRESTORE_VERSION = "23.0.3"
    private const val FIREBASE_STORAGE_VERSION = "20.0.0"
    private const val FIREBASE_BOM_VERSION = "26.1.1"


    const val firebaseAuth = "com.google.firebase:firebase-auth:${FIREBASE_AUTH_VERSION}"

    const val firebaseDatabase =
        "com.google.firebase:firebase-database:${FIREBASE_DATABASE_VERSION}"

    const val firebaseStorage =
        "com.google.firebase:firebase-storage:${FIREBASE_STORAGE_VERSION}"

    const val firebaseFirestore =
        "com.google.firebase:firebase-firestore:${FIREBASE_FIRESTORE_VERSION}"

    const val FIREBASE_CORE =
        "com.google.firebase:firebase-core:15.0.0"

    const val firebaseBom =
        "com.google.firebase:firebase-bom:${FIREBASE_BOM_VERSION}"
}