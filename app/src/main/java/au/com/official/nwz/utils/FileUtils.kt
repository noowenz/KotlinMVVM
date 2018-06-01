package au.com.official.nwz.utils

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.DatabaseUtils
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.media.ExifInterface
import android.support.v4.provider.DocumentFile
import android.util.Log
import android.webkit.MimeTypeMap
import java.io.*
import java.text.DecimalFormat
import java.util.*

/**
 * @author Peli
 * @author paulburke (ipaulpro)
 * @version 2013-12-11
 */
object FileUtils {

    /**
     * TAG for log messages.
     */
    internal val TAG = "FileUtils"
    private val DEBUG = false // Set to true to enable logging
    private val APP_NAME = "rangerX"

    val MIME_TYPE_AUDIO = "audio/*"
    val MIME_TYPE_TEXT = "text/*"
    val MIME_TYPE_IMAGE = "image/*"
    val MIME_TYPE_VIDEO = "video/*"
    val MIME_TYPE_APP = "application/*"

    val HIDDEN_PREFIX = "."

    private val IMAGE_PREFIX = "img_"
    private val IMAGE_POSTFIX = ".jpg"

    /**
     * Get the image directory to store photo taken in your app
     *
     * @return the image directory to save photo to
     */
    val imageDirectory: File?
        get() {
            val filePath = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), File.separator + APP_NAME)

            if (!filePath.exists()) {
                if (!(filePath.mkdirs() || filePath.isDirectory)) {
                    return null
                }
            }
            return filePath
        }

    /**
     * Get unique filename for photo.
     *
     * @return the unique filename for the photo
     */
    val uniqueImageName: String
        get() {
            return try {
                File.createTempFile(IMAGE_PREFIX, IMAGE_POSTFIX).name
            } catch (e: IOException) {
                IMAGE_PREFIX + System.currentTimeMillis() + IMAGE_POSTFIX
            }
        }

    /**
     * Get unique filename for documents.
     *
     * @return the unique filename for the document
     */
    val uniqueDocumentName: String
        get() {
            try {
                return File.createTempFile("doc_", ".pdf").name
            } catch (e: IOException) {
                return "doc_" + System.currentTimeMillis() + ".pdf"
            }

        }

    /**
     * File and folder comparator.
     *  @author paulburke
     */
    var sComparator: Comparator<File> = Comparator { f1, f2 ->
        // Sort alphabetically by lower case, which is much cleaner
        f1.name.toLowerCase().compareTo(
                f2.name.toLowerCase())
    }

    /**
     * File (not directories) filter.
     *
     * @author paulburke
     */
    var sFileFilter: FileFilter = FileFilter { file ->
        val fileName = file.name
        // Return files only (not directories) and skip hidden files
        file.isFile && !fileName.startsWith(HIDDEN_PREFIX)
    }

    /**
     * Folder (directories) filter.
     *
     * @author paulburke
     */
    var sDirFilter: FileFilter = FileFilter { file ->
        val fileName = file.name
        // Return directories only and skip hidden directories
        file.isDirectory && !fileName.startsWith(HIDDEN_PREFIX)
    }

    /**
     * Gets the extension of a file name, like ".png" or ".jpg".
     *
     * @param uri
     * @return Extension including the dot("."); "" if there is no extension;
     * null if uri was null.
     */
    fun getExtension(uri: String?): String? {
        if (uri == null) {
            return null
        }

        val dot = uri.lastIndexOf(".")
        return if (dot >= 0) {
            uri.substring(dot)
        } else {
            // No extension.
            ""
        }
    }

    /**
     * @return Whether the URI is a local one.
     */
    fun isLocal(url: String?): Boolean {
        return if (url != null && !url.startsWith("http://") && !url.startsWith("https://")) {
            true
        } else false
    }

    /**
     * @return True if Uri is a MediaStore Uri.
     * @author paulburke
     */
    fun isMediaUri(uri: Uri): Boolean {
        return "media".equals(uri.authority, ignoreCase = true)
    }

    /**
     * Convert File into Uri.
     *
     * @param file
     * @return uri
     */
    fun getUri(file: File?): Uri? {
        return if (file != null) {
            Uri.fromFile(file)
        } else null
    }

    /**
     * Returns the path only (without file name).
     *
     * @param file
     * @return
     */
    fun getPathWithoutFilename(file: File?): File? {
        if (file != null) {
            if (file.isDirectory) {
                // no file to be split off. Return everything
                return file
            } else {
                val filename = file.name
                val filepath = file.absolutePath

                // Construct path without file name.
                var pathwithoutname = filepath.substring(0,
                        filepath.length - filename.length)
                if (pathwithoutname.endsWith("/")) {
                    pathwithoutname = pathwithoutname.substring(0, pathwithoutname.length - 1)
                }
                return File(pathwithoutname)
            }
        }
        return null
    }

    /**
     * @return The MIME type for the given file.
     */
    fun getMimeType(file: File): String? {

        val extension = getExtension(file.name)

        return if (extension!!.length > 0) MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.substring(1)) else "application/octet-stream"

    }

    /**
     * @return The MIME type for the give Uri.
     */
    fun getMimeType(context: Context, uri: Uri): String? {
        val file = File(getPath(context, uri)!!)
        return getMimeType(file)
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     * @author paulburke
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     * @author paulburke
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     * @author paulburke
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Document.
     */
    fun isGoogleDocumentUri(uri: Uri): Boolean {
        //content://com.google.android.apps.docs.storage/document/acc%3D1%3Bdoc%3D2936
        return "com.google.android.apps.docs.storage" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     * @author paulburke
     */
    fun getDataColumn(context: Context, uri: Uri?, selection: String?,
                      selectionArgs: Array<String>?): String? {

        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)

        try {
            cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                if (DEBUG)
                    DatabaseUtils.dumpCursor(cursor)

                val column_index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(column_index)
            }
        } finally {
            if (cursor != null)
                cursor.close()
        }
        return null
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.<br></br>
     * <br></br>
     * Callers should check whether the path is local before assuming it
     * represents a local file.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     * @see .isLocal
     * @see .getFile
     */
    @SuppressLint("NewApi")
    fun getPath(context: Context, uri: Uri): String? {

        //        if (DEBUG)
        //            Log.INotificationActionPresenter(TAG + " File -",
        //                    "Authority: " + uri.getAuthority() +
        //                            ", Fragment: " + uri.getFragment() +
        //                            ", Port: " + uri.getPort() +
        //                            ", Query: " + uri.getQuery() +
        //                            ", Scheme: " + uri.getScheme() +
        //                            ", Host: " + uri.getHost() +
        //                            ", Segments: " + uri.getPathSegments().toString()
        //                    );

        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
            } else if (isDownloadsDocument(uri)) {
                return try {
                    val id = DocumentsContract.getDocumentId(uri)
                    val contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))
                    getDataColumn(context, contentUri, null, null)
                } catch (e: NumberFormatException) {
                    "-1"
                }
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                var contentUri: Uri? = null
                when (type) {
                    "image" -> contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    "video" -> contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    "audio" -> contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }

                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])
                return getDataColumn(context, contentUri, selection, selectionArgs)
            } else if (isGoogleDocumentUri(uri)) {
                return getFilePathFromUri(context, uri)
            }// MediaProvider
            // DownloadsProvider
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {

            // Return the remote nearbyAddress
            return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(context, uri, null, null)

        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }// File
        // MediaStore (and general)
        return null
    }

    /**
     * @param context The context
     * @param uri     Uri of google doc
     * @return path of the new file from uri
     */
    private fun getFilePathFromUri(context: Context, uri: Uri): String {
        val documentFile = DocumentFile.fromSingleUri(context, uri)
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), documentFile.name)
        val maxBufferSize = 1 * 1024 * 1024
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            Log.e("InputStream Size", "Size " + inputStream!!)
            val bytesAvailable = inputStream.available()
            //                    int bufferSize = 1024;
            val bufferSize = Math.min(bytesAvailable, maxBufferSize)
            val buffers = ByteArray(bufferSize)

            val outputStream = FileOutputStream(file)
            var read = 0
            while ({ read = inputStream.read(buffers);read }() != -1) {
                outputStream.write(buffers, 0, read)
            }
            Log.e("File Size", "Size " + file.length())
            inputStream.close()
            outputStream.close()
            Log.e("File Path", "Path " + file.path)
            Log.e("File Size", "Size " + file.length())
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return file.path
    }

    /**
     * Convert Uri into File, if possible.
     *
     * @return file A local file that the Uri was pointing to, or null if the
     * Uri is unsupported or pointed to a remote resource.
     * @author paulburke
     * @see .getPath
     */
    fun getFile(context: Context, uri: Uri?): File? {
        if (uri != null) {
            val path = getPath(context, uri)
            if (path != null && isLocal(path)) {
                return File(path)
            }
        }
        return null
    }

    /**
     * Get the file size in a human-readable string.
     *
     * @param size
     * @return
     * @author paulburke
     */
    fun getReadableFileSize(size: Int): String {
        val BYTES_IN_KILOBYTES = 1024
        val dec = DecimalFormat("###.#")
        val KILOBYTES = " KB"
        val MEGABYTES = " MB"
        val GIGABYTES = " GB"
        var fileSize = 0f
        var suffix = KILOBYTES

        if (size > BYTES_IN_KILOBYTES) {
            fileSize = (size / BYTES_IN_KILOBYTES).toFloat()
            if (fileSize > BYTES_IN_KILOBYTES) {
                fileSize = fileSize / BYTES_IN_KILOBYTES
                if (fileSize > BYTES_IN_KILOBYTES) {
                    fileSize = fileSize / BYTES_IN_KILOBYTES
                    suffix = GIGABYTES
                } else {
                    suffix = MEGABYTES
                }
            }
        }
        return dec.format(fileSize.toDouble()) + suffix
    }

    /**
     * Attempt to retrieve the thumbnail of given File from the MediaStore. This
     * should not be called on the UI thread.
     *
     * @param context
     * @param file
     * @return
     * @author paulburke
     */
    fun getThumbnail(context: Context, file: File): Bitmap? {
        return getThumbnail(context, getUri(file), getMimeType(file))
    }

    /**
     * Attempt to retrieve the thumbnail of given Uri from the MediaStore. This
     * should not be called on the UI thread.
     *
     * @param context
     * @param uri
     * @param mimeType
     * @return
     * @author paulburke
     */
    @JvmOverloads
    fun getThumbnail(context: Context, uri: Uri?, mimeType: String? = getMimeType(context, uri!!)): Bitmap? {
        if (DEBUG)
            Log.d(TAG, "Attempting to get thumbnail")

        if (!isMediaUri(uri!!)) {
            Log.e(TAG, "You can only retrieve thumbnails for images and videos.")
            return null
        }

        var bm: Bitmap? = null
        val resolver = context.contentResolver
        var cursor: Cursor? = null
        try {
            cursor = resolver.query(uri, null, null, null, null)
            if (cursor!!.moveToFirst()) {
                val id = cursor.getInt(0)
                if (DEBUG)
                    Log.d(TAG, "Got thumb ID: $id")

                if (mimeType!!.contains("video")) {
                    bm = MediaStore.Video.Thumbnails.getThumbnail(
                            resolver,
                            id.toLong(),
                            MediaStore.Video.Thumbnails.MINI_KIND, null)
                } else if (mimeType.contains(FileUtils.MIME_TYPE_IMAGE)) {
                    bm = MediaStore.Images.Thumbnails.getThumbnail(
                            resolver,
                            id.toLong(),
                            MediaStore.Images.Thumbnails.MINI_KIND, null)
                }
            }
        } catch (e: Exception) {
            if (DEBUG)
                Log.e(TAG, "getThumbnail", e)
        } finally {
            if (cursor != null)
                cursor.close()
        }
        return bm
    }

    /**
     * Get the Intent for selecting content to be used in an Intent Chooser.
     *
     * @return The intent for opening a file with Intent.createChooser()
     * @author paulburke
     */
    fun createGetContentIntent(): Intent {
        // Implicitly allow the user to select a particular kind of data
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        // The MIME data type filter
        intent.type = "*/*"
        // Only return URIs that can be opened with ContentResolver
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        return intent
    }

    /**
     * Resize the image to pre-defined size and compress to meet pre-defined file size
     *
     * @param context  context to use
     * @param filePath path of image to resize
     * @return resized image path
     */
    fun resizeAndCompressImageBeforeSend(context: Context, filePath: String): String {
        val MAX_IMAGE_SIZE = 500 * 1024 // max final file size in kilobytes
        val REQUIRED_WIDTH = 500
        val REQUIRED_HEIGHT = 500

        val resizedFileName = uniqueImageName

        // First decode with inJustDecodeBounds=true to check dimensions of image
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(filePath, options)

        // Calculate inSampleSize(First we are going to resize the image to 800x800 image, in order to not have a big but very low quality image.
        //resizing the image will already reduce the file size, but after resizing we will check the file size and loadData to compress image
        options.inSampleSize = calculateInSampleSize(options, REQUIRED_WIDTH, REQUIRED_HEIGHT)

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        options.inPreferredConfig = Bitmap.Config.ARGB_8888

        val bmpPic = BitmapFactory.decodeFile(filePath, options)

        var rotatedBitmap: Bitmap
        val exif: ExifInterface
        try {
            exif = ExifInterface(filePath)
            val orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION)
            val orientation = if (orientString != null) Integer.parseInt(orientString) else ExifInterface.ORIENTATION_NORMAL

            var rotationAngle = 0
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90
            if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180
            if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270

            val matrix = Matrix()
            matrix.setRotate(rotationAngle.toFloat(), bmpPic.width.toFloat() / 2, bmpPic.height.toFloat() / 2)
            rotatedBitmap = Bitmap.createBitmap(bmpPic, 0, 0, options.outWidth, options.outHeight, matrix, true)
        } catch (e: IOException) {
            e.printStackTrace()
            rotatedBitmap = bmpPic
        }

        var compressQuality = 100 // quality decreasing by 5 every loop.
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            Log.d("compressBitmap", "Quality: $compressQuality")
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
            Log.d("compressBitmap", "Size: " + streamLength / 1024 + " kb")
        } while (streamLength >= MAX_IMAGE_SIZE)

        try {
            //save the resized and compressed file to disk cache
            Log.d("compressBitmap", "cacheDir: " + context.cacheDir)
            val bmpFile = FileOutputStream(context.cacheDir.toString() + resizedFileName)
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpFile)
            bmpFile.flush()
            bmpFile.close()
        } catch (e: Exception) {
            Log.e("compressBitmap", "Error on saving file")
            // return the original image if we fail to save the compressed image
            return filePath
        }

        //return the path of resized and compressed file
        return context.cacheDir.toString() + resizedFileName
    }


    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val debugTag = "MemoryInformation"
        // Image nin islenmeden onceki genislik ve yuksekligi
        val height = options.outHeight
        val width = options.outWidth
        Log.d(debugTag, "image height: $height---image width: $width")
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight = height / 2
            val halfWidth = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize > reqHeight && halfWidth / inSampleSize > reqWidth) {
                inSampleSize *= 2
            }
        }
        Log.d(debugTag, "inSampleSize: $inSampleSize")
        return inSampleSize
    }

    /**
     * @param createdImagePath = path of the file to be deleted
     */
    fun deleteCreatedFile(createdImagePath: String?) {
        if (createdImagePath != null && !createdImagePath.isEmpty()) {
            val file = File(createdImagePath)
            if (file.exists()) {
                val isDeleted = file.delete()
                Log.d("FileUtils", "is created file deleted= $isDeleted")
            }
        }
    }

    interface FileUtilsCallback {
        fun showProgress()

        fun hideProgress()
    }
}
