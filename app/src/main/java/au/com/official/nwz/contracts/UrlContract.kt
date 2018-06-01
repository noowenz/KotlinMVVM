package au.com.official.nwz.contracts

class UrlContract {
    companion object {
        const val LOGIN = "auth/login"
        const val RESEND_EMAIL_VERIFICATION = "auth/resendVerification"//POST
        const val REGISTER = "auth/register"
        const val LOG_OUT = "auth/logout" //GET

        const val CHANGE_PASSWORD = "auth/changePassword"
        const val FORGOT_PASSWORD = "password/forgot"

        const val GET_COMPETITIONS = "competitions"
    }

    class Keys {
        companion object {
            const val AUTHORIZATION = "Authorization"
        }
    }

    class Values {
        companion object {
            const val COUNTRY_CODE = "AU"
            const val PHONE_CODE = "+61"
            const val SLUG_TERMS_AND_CONDITIONS = "terms-and-conditions"
            const val SLUG_PRIVACY_POLICY = "privacy-policy"
        }
    }
}


