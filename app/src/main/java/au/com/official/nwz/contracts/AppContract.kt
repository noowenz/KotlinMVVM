package au.com.official.nwz.contracts

class AppContract {
    companion object {
        const val DB_NAME = "nwz_mvvm.db"
        const val DEVICE_ID = "device_id"
    }

    class Extra {
        companion object {
            const val DATA = "extra_data"
            const val NOTIFICATION_ID = "notification_id"
            const val SHOW_CROP_VIEW = "show_crop_view"
            const val COMPRESS_IMAGE = "compress_image"
            const val OPENED_FROM = "opened_from"
        }
    }

    class Action {
        companion object {
            const val NOTIFICATION_RECEIVED = "notification_received"
        }
    }

    class RequestCode {
        companion object {
            const val CAMERA = 1000
            const val GALLERY = 1001

            const val EDIT_CONTACT_NUMBER = 2000
            const val COMPLETE_INFRINGEMENT = 2001
        }
    }

    class AccountSetupFragmentCodes {
        companion object {
            const val VERIFY_NUMBER = 1
            const val ADD_ABN = 2
            const val ADD_IDENTITY = 3
            const val ADD_BANK_ACCOUNT = 4
        }
    }

    class BankDetailsOpenCodes {
        companion object {
            const val PROFILE_COMPLETE = 1
            const val EDIT_PROFILE = 2
        }
    }

    class Permission {
        companion object {
            const val CAMERA = 1000
            const val GALLERY = 1001
            const val LOCATION = 1002
        }
    }
}
