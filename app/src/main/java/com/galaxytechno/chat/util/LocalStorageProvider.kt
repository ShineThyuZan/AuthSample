package com.galaxytechno.chat.util

import android.annotation.TargetApi
import android.database.Cursor
import android.database.MatrixCursor
import android.os.Build
import android.os.CancellationSignal
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.DocumentsContract
import android.provider.DocumentsProvider
import com.galaxytechno.chat.R
import java.io.File
import java.io.FileNotFoundException


@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class LocalStorageProvider : DocumentsProvider() {

    companion object {
        const val AUTHORITY = "com.galaxy_techno.seller"

        private val DEFAULT_ROOT_PROJECTION = arrayOf(
            DocumentsContract.Root.COLUMN_ROOT_ID,
            DocumentsContract.Root.COLUMN_FLAGS,
            DocumentsContract.Root.COLUMN_TITLE,
            DocumentsContract.Root.COLUMN_DOCUMENT_ID,
            DocumentsContract.Root.COLUMN_ICON,
            DocumentsContract.Root.COLUMN_AVAILABLE_BYTES
        )

        private val DEFAULT_DOCUMENT_PROJECTION = arrayOf(
            DocumentsContract.Document.COLUMN_DOCUMENT_ID,
            DocumentsContract.Document.COLUMN_DISPLAY_NAME,
            DocumentsContract.Document.COLUMN_FLAGS,
            DocumentsContract.Document.COLUMN_MIME_TYPE,
            DocumentsContract.Document.COLUMN_SIZE,
            DocumentsContract.Document.COLUMN_LAST_MODIFIED
        )
    }

    @Throws(FileNotFoundException::class)
    private fun includeFile(result: MatrixCursor, file: File) {
        val row = result.newRow()
        // These columns are required
        row.add(DocumentsContract.Document.COLUMN_DOCUMENT_ID, file.absolutePath)
        row.add(DocumentsContract.Document.COLUMN_DISPLAY_NAME, file.name)
        val mimeType = getDocumentType(file.absolutePath)
        row.add(DocumentsContract.Document.COLUMN_MIME_TYPE, mimeType)
        var flags = if (file.canWrite())
            DocumentsContract.Document.FLAG_SUPPORTS_DELETE or DocumentsContract.Document.FLAG_SUPPORTS_WRITE
        else
            0
        // We only show thumbnails for image files - expect a call to
        // openDocumentThumbnail for each file that has
        // this flag set
        if (mimeType.startsWith("image/"))
            flags = flags or DocumentsContract.Document.FLAG_SUPPORTS_THUMBNAIL
        row.add(DocumentsContract.Document.COLUMN_FLAGS, flags)
        // COLUMN_SIZE is required, but can be null
        row.add(DocumentsContract.Document.COLUMN_SIZE, file.length())
        // These columns are optional
        row.add(DocumentsContract.Document.COLUMN_LAST_MODIFIED, file.lastModified())
        // Document.COLUMN_ICON can be a resource id identifying a custom icon.
        // The system provides default icons
        // based on mime type
        // Document.COLUMN_SUMMARY is optional additional information about the
        // file
    }

    @Suppress("IMPLICIT_BOXING_IN_IDENTITY_EQUALS")
    override fun openDocument(
        documentId: String?,
        mode: String?,
        signal: CancellationSignal?
    ): ParcelFileDescriptor {
        val file = File(documentId!!)
        val isWrite = mode?.indexOf('w') !== -1
        return if (isWrite) {
            ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_WRITE)
        } else {
            ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        }
    }

    override fun queryChildDocuments(
        parentDocumentId: String?,
        projection: Array<out String>?,
        sortOrder: String?
    ): Cursor {
        // Create a cursor with either the requested fields, or the default
        // projection if "projection" is null.
        val result = MatrixCursor(projection ?: DEFAULT_DOCUMENT_PROJECTION)
        val parent = File(parentDocumentId!!)
        for (file in parent.listFiles()!!) {
            // Don't show hidden files/folders
            if (!file.name.startsWith(".")) {
                // Adds the file's display name, MIME type, size, and so on.
                includeFile(result, file)
            }
        }
        return result
    }

    override fun queryDocument(documentId: String?, projection: Array<out String>?): Cursor {
        // Create a cursor with either the requested fields, or the default
        // projection if "projection" is null.
        val result = MatrixCursor(projection ?: DEFAULT_DOCUMENT_PROJECTION)
        includeFile(result, File(documentId!!))
        return result
    }

    override fun onCreate() = true

    override fun queryRoots(projection: Array<out String>?): Cursor {
        // Create a cursor with either the requested fields, or the default
        // projection if "projection" is null.
        val result = MatrixCursor(projection ?: DEFAULT_ROOT_PROJECTION)
        // Add Home directory
        val homeDir = Environment.getExternalStorageDirectory()
        val row = result.newRow()
        // These columns are required
        row.add(DocumentsContract.Root.COLUMN_ROOT_ID, homeDir.absolutePath)
        row.add(DocumentsContract.Root.COLUMN_DOCUMENT_ID, homeDir.absolutePath)
        row.add(DocumentsContract.Root.COLUMN_TITLE, context!!.getString(R.string.app_name))
        row.add(
            DocumentsContract.Root.COLUMN_FLAGS,
            DocumentsContract.Root.FLAG_LOCAL_ONLY or DocumentsContract.Root.FLAG_SUPPORTS_CREATE
        )
        row.add(DocumentsContract.Root.COLUMN_ICON, R.drawable.ic_folder)
        // These columns are optional
        row.add(DocumentsContract.Root.COLUMN_AVAILABLE_BYTES, homeDir.freeSpace)
        // Root.COLUMN_MIME_TYPE is another optional column and useful if you
        // have multiple roots with different
        // types of mime types (roots that don't match the requested mime type
        // are automatically hidden)
        return result
    }
}